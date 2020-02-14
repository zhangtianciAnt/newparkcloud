package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.*;
import com.nt.dao_PHINE.Vo.BoardinfoListVo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.DeviceinfoVo;
import com.nt.dao_PHINE.Vo.OperationRecordVo;
import com.nt.service_PHINE.DeviceCommunication.ConnectionResult;
import com.nt.service_PHINE.DeviceCommunication.HardwareDeviceService;
import com.nt.service_PHINE.DeviceCommunication.IHardwareDeviceService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.OperationrecordService;
import com.nt.service_PHINE.mapper.*;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.util.StringUtil;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: DeviceinfoServiceImpl
 * @Description: 设备信息实现类
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@Service
public class DeviceinfoServiceImpl implements DeviceinfoService {

    @Autowired
    private DeviceinfoMapper deviceinfoMapper;

    @Autowired
    private BoardinfoMapper boardinfoMapper;

    @Autowired
    private ChipinfoMapper chipinfoMapper;

    @Autowired
    private CabinetinfoMapper cabinetinfoMapper;

    @Autowired
    private MachineroominfoMapper machineroominfoMapper;

    @Autowired
    private OperationrecordService operationrecordService;

    // WCF服务地址
    private static URL WSDL_LOCATION;

    static {
        try {
            WSDL_LOCATION = new URL("http://localhost:8734/WcfServiceLib_HardwareDevice/HardwareDeviceService/?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // WCF接口名称
    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "HardwareDeviceService");

    /**
     * @return List<ProjectListVo>平台项目信息列表
     * @Method getProjectInfoList
     * @Author SKAIXX
     * @Description 平台项目画面获取项目列表
     * @Date 2020/1/30 15:27
     * @Param TODO:权限部分暂未实装
     **/
    @Override
    public List<DeviceListVo> getDeviceInfoList() {

        return deviceinfoMapper.getDeviceInfoList();
    }

    /**
     * @Method getDeviceListByProjectId
     * @Author MYT
     * @Description 根据项目ID获取设备列表信息
     * @Date 2020/1/31 15:27
     * @Param projectid 项目ID
     **/
    @Override
    public List<DeviceListVo> getDeviceListByProjectId(String projectid) {
        List<DeviceListVo> deviceList = deviceinfoMapper.getDeviceListByProjectId(projectid);
        // 按照设备ID和设备地址分组，重构数据库数据
        Map<String, List<String>> tmpMap = new HashMap<>();
        for (DeviceListVo vo : deviceList) {
            if (tmpMap.containsKey(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress()))) {
                tmpMap.get(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress())).add(vo.getBoardid());
            } else {
                //map中不存在，新建key，用来存放数据
                List<String> tmpList = new ArrayList<>();
                if (StringUtil.isNotEmpty(vo.getBoardid())) {
                    tmpList.add(vo.getBoardid());
                }
                tmpMap.put(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress()), tmpList);
            }
        }
        // 重新构建返回值
        List<DeviceListVo> rtnDeviceList = new ArrayList<>();
        Set<Map.Entry<String, List<String>>> set = tmpMap.entrySet();
        for (Map.Entry<String, List<String>> entry : set) {
            DeviceListVo deviceListVo = new DeviceListVo();
            String key[] = entry.getKey().split(",");
            deviceListVo.setDeviceid(key[0]);
            deviceListVo.setMachineroomaddress(key[1]);
            deviceListVo.setBoardList(entry.getValue());
            rtnDeviceList.add(deviceListVo);
        }
        // 结果集按照设备ID排序
        Collections.sort(rtnDeviceList);

        return rtnDeviceList;
    }

    /**
     * @Method getDeviceListByCompanyId
     * @Author MYT
     * @Description 根据企业ID获取设备列表信息
     * @Date 2020/1/31 15:27
     * @Param companyid 企业ID
     **/
    @Override
    public List<DeviceListVo> getDeviceListByCompanyId(String companyid) {
        return deviceinfoMapper.getDeviceListByCompanyId(companyid);
    }

    @Override
    public ApiResult saveDeviceInfo(TokenModel tokenModel, DeviceinfoVo deviceinfoVo) {
        // region 设备信息
        Deviceinfo deviceinfo = new Deviceinfo();
        String deviceid = UUID.randomUUID().toString();
        deviceinfo.setId(deviceid);
        deviceinfo.setCabinetid(deviceinfoVo.getCabinetid());       // 机柜ID
        deviceinfo.setDeviceid(deviceinfoVo.getDeviceid());         // 设备编号
        deviceinfo.setDevicetype(deviceinfoVo.getDevicetype());     // 设备类型
        deviceinfo.setCompanyid(deviceinfoVo.getCompanyid());       // 所属公司ID
        deviceinfo.preInsert(tokenModel);
        deviceinfoMapper.insert(deviceinfo);
        // endregion

        // region 创建板卡&芯片信息
        createBoardChip(tokenModel, deviceid, deviceinfoVo);
        // endregion

        return ApiResult.success(MsgConstants.INFO_01, deviceinfo.getId());
    }

    /**
     * @return
     * @Method getDeviceInfo
     * @Author SKAIXX
     * @Description 获取设备详情
     * @Date 2020/2/7 15:04
     * @Param
     **/
    @Override
    public DeviceinfoVo getDeviceInfo(String id) {
        DeviceinfoVo deviceinfoVo = new DeviceinfoVo();

        // 获取设备信息
        Deviceinfo deviceinfo = deviceinfoMapper.selectByPrimaryKey(id);

        // 获取机柜信息
        Cabinetinfo cabinetinfo = cabinetinfoMapper.selectByPrimaryKey(deviceinfo.getCabinetid());

        // 获取机房信息
        Machineroominfo machineroominfo = machineroominfoMapper.selectByPrimaryKey(cabinetinfo.getMachineroomid());

        // 获取板卡信息
        Boardinfo boardinfo = new Boardinfo();
        boardinfo.setDeviceid(id);
        List<Boardinfo> boardinfoList = boardinfoMapper.select(boardinfo);
        List<BoardinfoListVo> boardinfoListVoList = new ArrayList<>();
        for (Boardinfo item : boardinfoList) {
            BoardinfoListVo boardinfoListVo = new BoardinfoListVo();
            // 获取芯片信息
            Chipinfo chipinfo = new Chipinfo();
            chipinfo.setBoardid(item.getId());
            boardinfoListVo.setId(item.getId());
            boardinfoListVo.setBoardid(item.getBoardid());
            boardinfoListVo.setBoardipaddress(item.getBoardipaddress());
            boardinfoListVo.setBoardtype(item.getBoardtype());
            boardinfoListVo.setChipinfoList(chipinfoMapper.select(chipinfo));
            boardinfoListVoList.add(boardinfoListVo);
        }

        // 获取到的信息保存到Vo中返回给前台
        deviceinfoVo.setId(id);
        deviceinfoVo.setMachineroomid(machineroominfo.getId());
        deviceinfoVo.setCabinetid(cabinetinfo.getId());
        deviceinfoVo.setDeviceid(deviceinfo.getDeviceid());
        deviceinfoVo.setDevicetype(deviceinfo.getDevicetype());
        deviceinfoVo.setCompanyid(deviceinfo.getCompanyid());
        deviceinfoVo.setBoardinfoList(boardinfoListVoList);

        return deviceinfoVo;
    }

    /**
     * @return
     * @Method updateDeviceInfo
     * @Author SKAIXX
     * @Description 更新设备
     * @Date 2020/2/7 19:23
     * @Param
     **/
    @Override
    public ApiResult updateDeviceInfo(TokenModel tokenModel, DeviceinfoVo deviceinfoVo) {
        // region 设备信息更新
        Deviceinfo deviceinfo = new Deviceinfo();
        deviceinfo.setId(deviceinfoVo.getId());                     // 设备ID
        deviceinfo.setCabinetid(deviceinfoVo.getCabinetid());       // 机柜ID
        deviceinfo.setDeviceid(deviceinfoVo.getDeviceid());         // 设备编号
        deviceinfo.setDevicetype(deviceinfoVo.getDevicetype());     // 设备类型
        deviceinfo.setCompanyid(deviceinfoVo.getCompanyid());       // 所属公司ID
        deviceinfo.preUpdate(tokenModel);
        deviceinfoMapper.updateByPrimaryKey(deviceinfo);
        // endregion

        // region 删除板卡&芯片信息
        deleteBoardChip(deviceinfoVo);
        // endregion

        // region 创建板卡&芯片信息
        createBoardChip(tokenModel, deviceinfoVo.getId(), deviceinfoVo);
        // endregion

        return ApiResult.success(MsgConstants.INFO_01, deviceinfo.getId());
    }

    /**
     * @return
     * @Method deleteDeviceInfo
     * @Author SKAIXX
     * @Description 删除设备
     * @Date 2020/2/7 19:24
     * @Param
     **/
    @Override
    public ApiResult deleteDeviceInfo(String id) {

        DeviceinfoVo deviceinfoVo = getDeviceInfo(id);

        // 删除设备
        deviceinfoMapper.deleteByPrimaryKey(deviceinfoVo.getId());

        // region 删除板卡&芯片信息
        deleteBoardChip(deviceinfoVo);
        // endregion
        return ApiResult.success(MsgConstants.INFO_01);
    }

    /**
     * @return
     * @Method deleteBoardChinp
     * @Author SKAIXX
     * @Description 删除卡板&芯片信息
     * @Date 2020/2/7 19:52
     * @Param
     **/
    private void deleteBoardChip(DeviceinfoVo deviceinfoVo) {
        // 删除板卡
        Boardinfo boardinfo = new Boardinfo();
        boardinfo.setDeviceid(deviceinfoVo.getId());
        List<Boardinfo> boardinfoList = boardinfoMapper.select(boardinfo);
        boardinfoMapper.delete(boardinfo);

        // 删除芯片
        for (Boardinfo tmp : boardinfoList) {
            Chipinfo chipinfo = new Chipinfo();
            chipinfo.setBoardid(tmp.getId());
            chipinfoMapper.delete(chipinfo);
        }
    }

    /**
     * @return
     * @Method createBoardChip
     * @Author SKAIXX
     * @Description 创建板卡&芯片信息
     * @Date 2020/2/7 19:55
     * @Param
     **/
    private void createBoardChip(TokenModel tokenModel, String deviceid, DeviceinfoVo deviceinfoVo) {
        // region 板卡信息
        for (BoardinfoListVo boardinfoListVo : deviceinfoVo.getBoardinfoList()) {
            Boardinfo boardinfo = new Boardinfo();
            String boardid = UUID.randomUUID().toString();
            boardinfo.setId(boardid);
            boardinfo.setBoardid(boardinfoListVo.getBoardid());                     // 板卡编号
            boardinfo.setBoardtype(boardinfoListVo.getBoardtype());                 // 板卡类型
            boardinfo.setBoardipaddress(boardinfoListVo.getBoardipaddress());       // 板卡IP地址
            boardinfo.setDeviceid(deviceid);                                        // 板卡所属设备ID
            boardinfo.preInsert(tokenModel);
            boardinfoMapper.insert(boardinfo);

            // region 芯片信息
            for (Chipinfo chipinfo : boardinfoListVo.getChipinfoList()) {
                chipinfo.setId(UUID.randomUUID().toString());
                chipinfo.setBoardid(boardid);                                       // 芯片所属板卡ID
                chipinfo.preInsert(tokenModel);
                chipinfoMapper.insert(chipinfo);
            }
            // endregion
        }
        // endregion
    }

    /**
     * @Method getAllDeviceStatus
     * @Author MYT
     * @Description 获取全部设备状态信息
     * @Date 2020/2/10 15:27
     **/
    @Override
    public List<DeviceListVo> getAllDeviceStatus() {
        List<DeviceListVo> deviceList = deviceinfoMapper.getAllDeviceStatus();
        // 按照设备ID和设备地址分组，重构数据库数据
        Map<String, List<String>> tmpMap = new HashMap<>();
        for (DeviceListVo vo : deviceList) {
            if (tmpMap.containsKey(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress().concat(",").concat(vo.getDevicestatus())))) {
                tmpMap.get(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress().concat(",").concat(vo.getDevicestatus()))).add(vo.getBoardid());
            } else {
                //map中不存在，新建key，用来存放数据
                List<String> tmpList = new ArrayList<>();
                if (StringUtil.isNotEmpty(vo.getBoardid())) {
                    tmpList.add(vo.getBoardid());
                }
                tmpMap.put(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress().concat(",").concat(vo.getDevicestatus())), tmpList);
            }
        }
        // 重新构建返回值
        List<DeviceListVo> rtnDeviceList = new ArrayList<>();
        Set<Map.Entry<String, List<String>>> set = tmpMap.entrySet();
        for (Map.Entry<String, List<String>> entry : set) {
            DeviceListVo deviceListVo = new DeviceListVo();
            String[] key = entry.getKey().split(",");
            deviceListVo.setDeviceid(key[0]);
            deviceListVo.setMachineroomaddress(key[1]);
            deviceListVo.setDevicestatus(key[2]);
            deviceListVo.setBoardList(entry.getValue());
            rtnDeviceList.add(deviceListVo);
        }
        // 结果集按照设备ID排序
        Collections.sort(rtnDeviceList);

        return rtnDeviceList;
    }

    /**
     * @Method getCommunicationDeviceInfo
     * @Author MYT
     * @Description 获取通信操作设备信息
     * @Date 2020/2/10 15:27
     **/
    @Override
    public List<DeviceListVo> getCommunicationDeviceInfo(TokenModel tokenModel, String projectid) {
        List<DeviceListVo> deviceList = deviceinfoMapper.getCommunicationDeviceInfo(projectid);
        for (DeviceListVo vo : deviceList) {
            if (vo != null) {
                vo.setCurrentloginuser(tokenModel.getUserId());
            }
        }
        return deviceList;
    }

    /**
     * @Method createConnection
     * @Author MYT
     * @Description 设备创建连接
     * @Date 2020/2/10 15:27
     **/
    @Override
    public ApiResult createConnection(TokenModel tokenModel, String deviceid) {
        // TODO :操作用户权限暂时不封装
        // region 通信连接
        Deviceinfo deviceinfo = deviceinfoMapper.selectByPrimaryKey(deviceid);
        HardwareDeviceService ss = new HardwareDeviceService(WSDL_LOCATION, SERVICE_NAME);
        IHardwareDeviceService port = ss.getBasicHttpBindingIHardwareDeviceService();
        ConnectionResult result = port.openConnection(deviceinfo.getDeviceid());
        String message = "通信连接成功";
        switch (result.value()) {
            case "Result_Unknown":
                message = "发生未知错误，请联系管理员或稍后重试！";
                break;
            case "Result_Error":
                message = "连接失败，请稍后重试！";
                break;
            case "Result_Occupied":
                message = "设备被占用，请稍后重试！";
                break;
        }
        if (!result.value().equals("Result_OK")) {
            return ApiResult.fail(message);
        }
        // endregion
        // 创建连接成功后，更新数据库
        Deviceinfo uptDeviceinfo = new Deviceinfo();
        uptDeviceinfo.setId(deviceid);
        uptDeviceinfo.setCurrentuser(tokenModel.getUserId());
        uptDeviceinfo.preUpdate(tokenModel);
        deviceinfoMapper.updateCommunicationDeviceInfo(uptDeviceinfo);
        return ApiResult.success(message);
    }

    /**
     * @Method closeConnection
     * @Author MYT
     * @Description 设备关闭连接
     * @Date 2020/2/10 15:27
     **/
    @Override
    public ApiResult closeConnection(TokenModel tokenModel, String deviceid) {
        // TODO :操作用户权限暂时不封装
        // region 通信关闭
        Deviceinfo deviceinfo = deviceinfoMapper.selectByPrimaryKey(deviceid);
        HardwareDeviceService ss = new HardwareDeviceService(WSDL_LOCATION, SERVICE_NAME);
        IHardwareDeviceService port = ss.getBasicHttpBindingIHardwareDeviceService();
        Boolean result = port.closeConnection(deviceinfo.getDeviceid());
        if (!result) {
            return ApiResult.fail("通信关闭失败，请稍后重试！");
        }
        // endregion
        // 关闭连接成功后，更新数据库
        Deviceinfo uptDeviceinfo = new Deviceinfo();
        uptDeviceinfo.setId(deviceid);
        uptDeviceinfo.setCurrentuser(null);
        uptDeviceinfo.preUpdate(tokenModel);
        deviceinfoMapper.updateCommunicationDeviceInfo(uptDeviceinfo);
        return ApiResult.success("通信关闭成功！");
    }

    /**
     * @return
     * @Method logicFileLoad
     * @Author SKAIXX
     * @Description 逻辑加载
     * @Date 2020/2/12 21:08
     * @Param
     **/
    @Override
    public ApiResult logicFileLoad(TokenModel tokenMode, List<Fileinfo> fileinfoList) {
        // 设备通信-->逻辑加载处理
        HardwareDeviceService ss = new HardwareDeviceService(WSDL_LOCATION, SERVICE_NAME);
        IHardwareDeviceService port = ss.getBasicHttpBindingIHardwareDeviceService();
        Boolean result = false;     // 加载操作执行结果
        OperationRecordVo operationRecordVo = new OperationRecordVo();
        String operationId = UUID.randomUUID().toString();
        List<Operationdetail> detailist = new ArrayList<>();
        for (Fileinfo fileinfo : fileinfoList) {
            Operationdetail operationdetail = new Operationdetail();
            Deviceinfo deviceinfo = deviceinfoMapper.selectByPrimaryKey(fileinfo.getDeviceid());
            String configurationtype = "";
            switch (fileinfo.getFiletype()) {
                case "FPGA":        // 执行FPGA加载
                    result = port.configFpga(deviceinfo.getDeviceid(), Integer.parseInt(fileinfo.getFpgaid()), fileinfo.getUrl());
                    configurationtype = "FPGA加载";
                    break;
                case "FMC":         // TODO:执行FMC加载
//                    result = port.setFmcVoltage(fileinfo.getDeviceid(), )
                    configurationtype = "FMC加载";
                    break;
                case "PLL":         // TODO:执行PLL加载
//                    result = port.setPllClock(fileinfo.getDeviceid(), )
                    configurationtype = "PLL加载";
                    break;
            }
            fileinfo.setRemarks(result ? "成功" : "失败");
            // 添加操作记录详情
            operationdetail.setId(UUID.randomUUID().toString());
            operationdetail.setConfigurationtype(configurationtype);
            operationdetail.setDevicename(deviceinfo.getDeviceid());
            operationdetail.setFilename(fileinfo.getFilename());
            operationdetail.setOperationresult(result ? "成功" : "失败");
            operationdetail.setOperationid(operationId);
            operationdetail.preInsert(tokenMode);
            detailist.add(operationdetail);
        }
        // 添加操作记录
        operationRecordVo.setOperationid(operationId);
        operationRecordVo.setDetailist(detailist);
        operationRecordVo.setContent("加载了" + fileinfoList.size() + "文件");
        operationRecordVo.setTitle("逻辑加载");
        operationRecordVo.setProjectid(fileinfoList.get(0).getProjectid());
        operationrecordService.addOperationrecord(operationRecordVo);
        return ApiResult.success(fileinfoList);
    }
}
