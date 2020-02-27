package com.nt.service_PHINE;

import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Vo.*;
import com.nt.utils.ApiResult;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE
 * @ClassName: DeviceinfoService
 * @Description: 设备信息Service接口
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
public interface DeviceinfoService {

    // 设备一览画面获取设备列表
    List<DeviceListVo> getDeviceInfoList();

    // 项目创建页面根据项目ID获取设备信息列表
    List<DeviceListVo> getDeviceListByProjectId(String projectid);

    // 项目创建页面根据企业ID获取设备信息列表
    List<DeviceListVo> getDeviceListByCompanyId(String companyid);

    // 新建设备
    ApiResult saveDeviceInfo(TokenModel tokenModel, DeviceinfoVo deviceinfoVo);

    // 获取设备详情
    DeviceinfoVo getDeviceInfo(String id);

    // 更新设备
    ApiResult updateDeviceInfo(TokenModel tokenModel, DeviceinfoVo deviceinfoVo);

    // 删除设备
    ApiResult deleteDeviceInfo(String id);

    // 获取全部设备状态信息
    List<DeviceListVo> getAllDeviceStatus(String companyid);

    // 获取通信操作设备信息
    List<DeviceListVo> getCommunicationDeviceInfo(TokenModel tokenModel, String projectid);

    // 设备创建连接
    ApiResult createConnection(TokenModel tokenModel, String deviceid);

    // 设备关闭连接
    ApiResult closeConnection(TokenModel tokenModel, String deviceid);

    // 逻辑加载
    ApiResult logicFileLoad(TokenModel tokenMode, List<Fileinfo> fileinfoList);

    // 测试读写
    List<ReadWriteTestVo> readWriteTest(TokenModel tokenModel, String projectid);

    // 获取企业/设备数据
    List<ChartDataRow> getDeviceChartInfo();

    // 获取设备状态数据
    List<ChartDataRow> getDeviceStatusChartInfo();

    // 获取设备当前连接状态
    List<CurrentConnStatusVo> getCurrentConnStatus(TokenModel tokenModel, String projectid);

    List<Fileinfo> getConfigProgressMap(TokenModel tokenModel);

    void clearConfigProgressByToken(TokenModel tokenModel);

    // 加载策略时，如果选择了"ALL"，则返回所有FPGAID的List
    ApiResult getALLFpga(List<FpgaDataVo> fpgaDataVoList) throws Exception;

    // 测试读写Step1:系统互联检测开始
    ApiResult interConnTestStart(TokenModel tokenModel, String projectId, String filePath) throws Exception;

    // 测试读写Step2.1:获取系统互联检测进度
    ApiResult interConnGetProgress(TokenModel tokenModel) throws Exception;

    // 测试读写Step2.2:清除系统互联检测进度
    ApiResult interConnClearProgress(TokenModel tokenModel) throws Exception;

    // 测试读写Step3:获取互联检测结果
    ApiResult interConnGetResult(List<InterConnDetailVo> interConnDetailVoList) throws Exception;
}
