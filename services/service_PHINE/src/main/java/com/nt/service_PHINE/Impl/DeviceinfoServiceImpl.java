package com.nt.service_PHINE.Impl;

import com.nt.dao_Org.OrgTree;
import com.nt.dao_PHINE.*;
import com.nt.dao_PHINE.Vo.*;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_PHINE.DeviceCommunication.*;
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
import javax.xml.ws.Holder;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
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

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private Project2deviceMapper project2deviceMapper;

    // WCF服务地址
    private static URL WSDL_LOCATION;

    static {
        try {
            WSDL_LOCATION = new URL("http://localhost:8734/WcfServiceLib_VerityPlatform/DeviceService/?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    // WCF接口名称
    private static final QName SERVICE_NAME = new QName("http://tempuri.org/", "DeviceService");

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
            boardinfoListVo.setBoardslotid(item.getBoardslotid());
            boardinfoListVoList.add(boardinfoListVo);
        }

        // 获取到的信息保存到Vo中返回给前台
        deviceinfoVo.setId(id);
        deviceinfoVo.setMachineroomid(machineroominfo.getId());
        deviceinfoVo.setCabinetid(cabinetinfo.getId());
        deviceinfoVo.setDeviceid(deviceinfo.getDeviceid());
        deviceinfoVo.setDevicetype(deviceinfo.getDevicetype());
        deviceinfoVo.setCompanyid(deviceinfo.getCompanyid());
        deviceinfoVo.setCabinetslotid(deviceinfo.getCabinetslotid());
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
            boardinfo.setBoardslotid(boardinfoListVo.getBoardslotid());             // 所在设备槽位
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
     * @Method getCurrentConnStatus
     * @Author MYT
     * @Description 获取当前设备连接状态
     * @Date 2020/2/10 15:27
     **/
    @Override
    public List<CurrentConnStatusVo> getCurrentConnStatus(String projectid) {
        // 获取当前项目的设备列表
        // region 通过项目ID获取所有设备ID
        Project2device tmpModel = new Project2device();
        tmpModel.setProjectid(projectid);
        List<Project2device> project2deviceList = project2deviceMapper.select(tmpModel);
        // 当前项目下没有设备时，则返回
        if (project2deviceList.size() == 0) {
            return null;
        }
        // endregion
        List<CurrentConnStatusVo> currentConnStatusVoList = new ArrayList<>();
        DeviceService ss = new DeviceService(WSDL_LOCATION, SERVICE_NAME);
        IDeviceService port = ss.getBasicHttpBindingIDeviceService();
        Holder<ArrayOfDeviceConnState> deviceConnStates = new Holder<>();
        Holder<Boolean> getCurrentConnStatusResult = new Holder<>(false);
        port.getCurrentConnStatus(deviceConnStates, getCurrentConnStatusResult);
        ArrayOfDeviceConnState arrayOfDeviceConnState = deviceConnStates.value;
        arrayOfDeviceConnState.getDeviceConnState().forEach(item -> {
            CurrentConnStatusVo currentConnStatusVo = new CurrentConnStatusVo();
            Deviceinfo deviceinfo = new Deviceinfo();
            deviceinfo.setDeviceid(item.getDeviceId().getValue());
            List<Deviceinfo> deviceinfoList = deviceinfoMapper.select(deviceinfo);
            if (deviceinfoList != null && deviceinfoList.size() > 0 ) {
                Project2device tmp = project2deviceList.get(0);
                tmp.setDeviceid(deviceinfoList.get(0).getId());
                if(project2deviceList.contains(tmp)) {
                    currentConnStatusVo.setId(deviceinfoList.get(0).getId());
                }
            } else {
                return;
            }
            currentConnStatusVo.setDeviceid(item.getDeviceId().getValue());
            String status = "";
            switch (item.getConnStatus()) {
                case 2:
                    status = "未连接";
                    break;
                case 1:
                    status = "已连接";
                    break;
                case 0:
                    status = "离线";
                    break;
                default:
                    status = "未知";
                    break;
            }
            currentConnStatusVo.setConnstatus(status);
            currentConnStatusVoList.add(currentConnStatusVo);
        });
        return currentConnStatusVoList;
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
        DeviceService ss = new DeviceService(WSDL_LOCATION, SERVICE_NAME);
        IDeviceService port = ss.getBasicHttpBindingIDeviceService();
        ConnectionResult result = port.openConnection(deviceinfo.getDeviceid());
        String message = "通信连接成功";
        switch (result.value()) {
            case "Unknown":
                message = "发生未知错误，请联系管理员或稍后重试！";
                break;
            case "Error":
                message = "连接失败，请稍后重试！";
                break;
            case "Occupied":
                message = "设备被占用，请稍后重试！";
                break;
        }
        if (!result.value().equals("OK")) {
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
        DeviceService ss = new DeviceService(WSDL_LOCATION, SERVICE_NAME);
        IDeviceService port = ss.getBasicHttpBindingIDeviceService();
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
    public ApiResult logicFileLoad(TokenModel tokenModel, List<Fileinfo> fileinfoList) {
        // 设备通信-->逻辑加载处理
        DeviceService ss = new DeviceService(WSDL_LOCATION, SERVICE_NAME);
        IDeviceService port = ss.getBasicHttpBindingIDeviceService();
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
                    result = port.startConfigFpgaByFile(deviceinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), fileinfo.getUrl());
                    boolean loopFlg = false;
                    while (!loopFlg) {
                        // 循环获取Fpga执行结果
                        Holder<ConfigStatus> configResult = new Holder<>();
                        Holder<Boolean> getFpgaConfigStatusResult = new Holder<>(false);
                        // 获取当前Config状态
                        port.getFpgaConfigStatus(deviceinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), configResult, getFpgaConfigStatusResult);
                        switch (configResult.value.toString()) {
                            // 配置中
                            case "Configing":
                                Holder<Long> progress = new Holder<Long>(0L);
                                Holder<Boolean> getFpgaConfigProgressResult = new Holder<>(false);
                                // 调用GetFpgaConfigProgress()获取当前Config进度
                                port.getFpgaConfigProgress(deviceinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), progress, getFpgaConfigProgressResult);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                break;
                            // 配置成功
                            case "Succeed":
                                result = true;
                                loopFlg = true;
                                break;
                            // 配置失败
                            default:
                                result = false;
                                loopFlg = true;
                                break;
                        }
                    }
                    configurationtype = "FPGA加载";
                    break;
                case "FMC":         // 执行FMC加载
                    result = port.setFmcVoltageByFile(fileinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), 0L, fileinfo.getUrl());
                    configurationtype = "FMC加载";
                    break;
                case "PLL":         // 执行PLL加载
                    result = port.setPllClockByFile(fileinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), 0L, fileinfo.getUrl());
                    configurationtype = "PLL加载";
                    break;
            }
            fileinfo.setRemarks(result ? "成功" : "失败");
            // 添加操作记录详情
            operationdetail.setId(UUID.randomUUID().toString());
            operationdetail.setConfigurationtype(configurationtype);
            operationdetail.setDevicename(deviceinfo.getId());
            operationdetail.setFilename(fileinfo.getFilename());
            operationdetail.setOperationresult(result ? "成功" : "失败");
            operationdetail.setOperationid(operationId);
            detailist.add(operationdetail);
        }
        // 添加操作记录
        operationRecordVo.setOperationid(operationId);
        operationRecordVo.setDetailist(detailist);
        operationRecordVo.setContent("加载了" + fileinfoList.size() + "文件");
        operationRecordVo.setTitle("逻辑加载");
        operationRecordVo.setProjectid(fileinfoList.get(0).getProjectid());
        operationrecordService.addOperationrecord(tokenModel, operationRecordVo);
        return ApiResult.success(fileinfoList);
    }

    @Override
    public List<ReadWriteTestVo> readWriteTest(TokenModel tokenModel, String projectid) {

        List<ReadWriteTestVo> readWriteTestVoList = new ArrayList<>();

        // region 通过项目ID获取所有设备ID
        Project2device tmpModel = new Project2device();
        tmpModel.setProjectid(projectid);
        List<Project2device> project2deviceList = project2deviceMapper.select(tmpModel);
        List<Deviceinfo> deviceinfoList = new ArrayList<>();
        project2deviceList.forEach(item -> {
            deviceinfoList.add(deviceinfoMapper.selectByPrimaryKey(item.getDeviceid()));
        });
        // endregion

        // region 设备读写测试
        DeviceService ss = new DeviceService(WSDL_LOCATION, SERVICE_NAME);
        IDeviceService port = ss.getBasicHttpBindingIDeviceService();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        deviceinfoList.forEach(item -> {
            ReadWriteTestVo readWriteTestVo = new ReadWriteTestVo();
            String msg = df.format(new Date()) + "-FPGA1 ";
            // 读寄存器
            Holder<Long> regData = new Holder<Long>(0L);
            Holder<Boolean> holderResult = new Holder<>(false);
            port.regRead(item.getDeviceid(), 1L, Long.parseLong("000C", 16), regData, holderResult);
            msg += holderResult.value ? "读取成功," : "读取失败,";
            // 写寄存器
            Boolean result = port.regWrite(item.getDeviceid(), 1L, Long.parseLong("000C", 16), 1L);
            msg += result ? "写入成功！" : "写入失败！";
            readWriteTestVo.setDeviceid(item.getDeviceid());
            readWriteTestVo.setResult(msg);
            readWriteTestVoList.add(readWriteTestVo);
        });
        // endregion

        return readWriteTestVoList;
    }

    /**
     * @return
     * @Method getDeviceChartInfo
     * @Author SKAIXX
     * @Description 获取企业/设备图表数据
     * @Date 2020/2/19 13:57
     * @Param
     **/
    @Override
    public List<ChartDataRow> getDeviceChartInfo() {
        List<ChartDataRow> chartDataRowList = deviceinfoMapper.getDeviceChartInfo();
        // 通过公司ID获取公司名称
        chartDataRowList.forEach(item -> {
            if (!item.getName().equals("未分配")) {
                OrgTree orgTree = new OrgTree();
                orgTree.set_id(item.getName());
                try {
                    item.setName(orgTreeService.getById(orgTree).get(0).getCompanyshortname());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return chartDataRowList;
    }

    @Override
    public List<ChartDataRow> getDeviceStatusChartInfo() {
        return deviceinfoMapper.getDeviceStatusChartInfo();
    }
}
