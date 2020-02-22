package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Deviceinfo;
import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Operationdetail;
import com.nt.service_PHINE.AsyncService;
import com.nt.service_PHINE.DeviceCommunication.ConfigStatus;
import com.nt.service_PHINE.DeviceCommunication.DeviceService;
import com.nt.service_PHINE.DeviceCommunication.IDeviceService;
import com.nt.service_PHINE.mapper.DeviceinfoMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;

@Service
public class AsyncServiceImpl implements AsyncService {

    private final DeviceinfoMapper deviceinfoMapper;

    // 全局变量：存储FpgaConfig进度
    private static Map<String, List<Fileinfo>> configProgressMap = new HashMap<String, List<Fileinfo>>();

    public AsyncServiceImpl(DeviceinfoMapper deviceinfoMapper) {
        this.deviceinfoMapper = deviceinfoMapper;
    }

    @Override
    @Async
    public Future<List<Operationdetail>> doLogicFileLoad(List<Fileinfo> fileinfoList, TokenModel tokenModel, String operationId, URL WSDL_LOCATION, QName SERVICE_NAME) {
        // 设备通信-->逻辑加载处理
        DeviceService ss = new DeviceService(WSDL_LOCATION, SERVICE_NAME);
        IDeviceService port = ss.getBasicHttpBindingIDeviceService();
        Boolean result = false;     // 加载操作执行结果

        List<Operationdetail> detailist = new ArrayList<>();

        for (Fileinfo fileinfo : fileinfoList) {
            Operationdetail operationdetail = new Operationdetail();
            Deviceinfo deviceinfo = deviceinfoMapper.selectByPrimaryKey(fileinfo.getDeviceid());
            String configurationtype = "";
            switch (fileinfo.getFiletype()) {
                case "FPGA":        // 执行FPGA加载
                    result = port.startConfigFpgaByFile(deviceinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), fileinfo.getUrl());
                    boolean loopFlg = false;
                    int idx = 0;
                    while (!loopFlg) {
                        idx++;
                        // 循环获取Fpga执行结果
                        Holder<ConfigStatus> configResult = new Holder<>();
                        Holder<Boolean> getFpgaConfigStatusResult = new Holder<>(false);
                        // 获取当前Config状态
                        port.getFpgaConfigStatus(deviceinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), configResult, getFpgaConfigStatusResult);
                        System.out.println("第" + idx + "次调用getFpgaConfigStatus ---------------返回值：" + configResult.value.toString());
                        switch (configResult.value.value()) {
                            // 配置中
                            case "Configing":
                                Holder<Long> progress = new Holder<Long>(0L);
                                Holder<Boolean> getFpgaConfigProgressResult = new Holder<>(false);
                                // 调用GetFpgaConfigProgress()获取当前Config进度
                                port.getFpgaConfigProgress(deviceinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), progress, getFpgaConfigProgressResult);
                                // 更新处理进度到Fileinfo
                                fileinfo.setRemarks(progress.value.toString());
                                configProgressMap.put(tokenModel.getToken(), fileinfoList);
                                System.out.println("第" + idx + "次调用getFpgaConfigProgress ---------------返回值：" + progress.value);
                                try {

                                    System.out.println("第" + idx + "次Before sleep ---------------");
                                    Thread.sleep(1000);
                                    System.out.println("第" + idx + "次After sleep ---------------");
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
            operationdetail.setFileid(fileinfo.getFileid());
            detailist.add(operationdetail);
        }

        return new AsyncResult<>(detailist);
    }

    @Override
    public List<Fileinfo> getConfigProgressMap(TokenModel tokenModel) {
        return configProgressMap.get(tokenModel.getToken());
    }

    @Override
    public void clearConfigProgressByToken(TokenModel tokenModel) {
        configProgressMap.remove(tokenModel.getToken());
    }
}
