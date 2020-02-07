package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.*;
import com.nt.dao_PHINE.Vo.BoardinfoListVo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.DeviceinfoVo;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.mapper.*;
import com.nt.utils.ApiResult;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.opencv.aruco.Board;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // 执行数据查询
        List<DeviceListVo> deviceList = deviceinfoMapper.getDeviceListByProjectId(projectid);
        // 按照设备ID和设备地址分组，重构数据库数据
        Map<String, List<String>> tmpMap = new HashMap<String, List<String>>();
        for(DeviceListVo vo : deviceList){
            if(tmpMap.containsKey(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress()))){
                tmpMap.get(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress())).add(vo.getBoardid());
            }else{//map中不存在，新建key，用来存放数据
                List<String> tmpList = new ArrayList<String>();
                tmpList.add(vo.getBoardid());
                tmpMap.put(vo.getDeviceid().concat(",").concat(vo.getMachineroomaddress()), tmpList);
            }
        }
        // 重新构建返回值
        List<DeviceListVo> rtnDeviceList = new ArrayList<DeviceListVo>();
        Set<Map.Entry<String,List<String>>> set = tmpMap.entrySet();
        for(Map.Entry<String,List<String>> entry:set){
            DeviceListVo deviceListVo = new DeviceListVo();
            String key[] =entry.getKey().split(",");
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
        deviceinfo.preInsert();
        deviceinfoMapper.insert(deviceinfo);
        // endregion

        // region 板卡信息
        for (BoardinfoListVo boardinfoListVo : deviceinfoVo.getBoardinfoList()) {
            Boardinfo boardinfo = new Boardinfo();
            String boardid = UUID.randomUUID().toString();
            boardinfo.setId(boardid);
            boardinfo.setBoardid(boardinfoListVo.getBoardid());                     // 板卡编号
            boardinfo.setBoardtype(boardinfoListVo.getBoardtype());                 // 板卡类型
            boardinfo.setBoardipaddress(boardinfoListVo.getBoardipaddress());       // 板卡IP地址
            boardinfo.setDeviceid(deviceid);                                        // 板卡所属设备ID
            boardinfo.preInsert();
            boardinfoMapper.insert(boardinfo);

            // region 芯片信息
            for (Chipinfo chipinfo : boardinfoListVo.getChipinfoList()) {
                chipinfo.setId(UUID.randomUUID().toString());
                chipinfo.setBoardid(boardid);                                       // 芯片所属板卡ID
                chipinfo.preInsert();
                chipinfoMapper.insert(chipinfo);
            }
            // endregion
        }
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
        Deviceinfo deviceinfo = new Deviceinfo();
        deviceinfo = deviceinfoMapper.selectByPrimaryKey(id);

        // 获取机柜信息
        Cabinetinfo cabinetinfo = new Cabinetinfo();
        cabinetinfo = cabinetinfoMapper.selectByPrimaryKey(deviceinfo.getCabinetid());

        // 获取机房信息
        Machineroominfo machineroominfo = new Machineroominfo();
        machineroominfo = machineroominfoMapper.selectByPrimaryKey(cabinetinfo.getMachineroomid());

        // 获取板卡信息
        Boardinfo boardinfo = new Boardinfo();
        boardinfo.setDeviceid(id);
        List<Boardinfo> boardinfoList = boardinfoMapper.select(boardinfo);
        List<BoardinfoListVo> boardinfoListVoList = new ArrayList<>();
        for (Boardinfo item : boardinfoList) {
            BoardinfoListVo boardinfoListVo = new BoardinfoListVo();
            // 获取芯片信息
            Chipinfo chipinfo = new Chipinfo();
            chipinfo.setBoardid(item.getBoardid());
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
}
