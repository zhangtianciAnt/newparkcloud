package com.nt.service_PHINE.Impl;

import com.nt.dao_PHINE.Deviceinfo;
import com.nt.dao_PHINE.Fileinfo;
import com.nt.dao_PHINE.Operationdetail;
import com.nt.service_PHINE.AsyncService;
import com.nt.service_PHINE.DeviceService.ConfigStatus;
import com.nt.service_PHINE.DeviceService.DeviceService;
import com.nt.service_PHINE.DeviceService.IDeviceService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.mapper.DeviceinfoMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class AsyncServiceImpl implements AsyncService {

    private final DeviceinfoMapper deviceinfoMapper;

    @Autowired
    private DeviceinfoService deviceinfoService;

    // 全局变量：存储FpgaConfig进度
//    private static Map<String, List<Fileinfo>> configProgressMap = new HashMap<String, List<Fileinfo>>();

    public AsyncServiceImpl(DeviceinfoMapper deviceinfoMapper) {
        this.deviceinfoMapper = deviceinfoMapper;
    }

    // 设备文件类型排序方法
    private int myCompareTemp(String fileType) {
        int temp = 0;
        if ("FMC".equals(fileType)) {
            temp = 1;
        } else if ("FPGA".equals(fileType)) {
            temp = 2;
        } else if ("PLL".equals(fileType)) {
            temp = 3;
        }
        return temp;
    }

    @Override
    @Async
    public Future<List<Operationdetail>> doLogicFileLoad(List<Fileinfo> fileinfoList, TokenModel tokenModel, String operationId, QName SERVICE_NAME, Map<String, List<Fileinfo>> configProgressMap) throws Exception {
        // 设备通信-->逻辑加载处理
        // 获取当前设备服务地址
        URL wsdlLocation = deviceinfoService.getWsdlLocation(deviceinfoMapper.selectByPrimaryKey(fileinfoList.get(0).getDeviceid()).getDeviceid());
        DeviceService ss = new DeviceService(wsdlLocation, SERVICE_NAME);
        IDeviceService port = ss.getBasicHttpBindingIDeviceService();
        Boolean result = false;     // 加载操作执行结果

        List<Operationdetail> detailist = new ArrayList<>();

        //  先加载 FMC、然后 FPGA、最后 PLL
        Collections.sort(fileinfoList, (f1, f2) -> {
            int f1Temp, f2Temp = 0;
            f1Temp = myCompareTemp(f1.getFiletype());
            f2Temp = myCompareTemp(f2.getFiletype());
            return f1Temp - f2Temp;
        });

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
                        switch (configResult.value.value()) {
                            // 配置中
                            case "Configing":
                                Holder<Long> progress = new Holder<Long>(0L);
                                Holder<Boolean> getFpgaConfigProgressResult = new Holder<>(false);
                                // 调用GetFpgaConfigProgress()获取当前Config进度
                                port.getFpgaConfigProgress(deviceinfo.getDeviceid(), Long.parseLong(fileinfo.getFpgaid()), progress, getFpgaConfigProgressResult);
                                // 获取当前token的FileList
                                List<Fileinfo> tmpList = configProgressMap.get(tokenModel.getToken());
                                // 更新处理进度到全局Map中
                                tmpList.stream().filter(item -> item.getFileid().equals(fileinfo.getFileid())).collect(Collectors.toList()).forEach(item -> item.setRemarks(progress.value.toString()));
                                configProgressMap.put(tokenModel.getToken(), tmpList);
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
                    result = port.setFmcVoltageByFile(fileinfo.getDeviceid(), fileinfo.getUrl());
                    configurationtype = "FMC加载";
                    break;
                case "PLL":         // 执行PLL加载
                    // Todo By Skaixx At 2020/4/17 :  PLL时钟加载
                    port.startSetPllClockByFile(fileinfo.getDeviceid(), fileinfo.getUrl());
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

//    @Override
//    public List<Fileinfo> getConfigProgressMap(TokenModel tokenModel) {
//        return configProgressMap.get(tokenModel.getToken());
//    }
//
//    @Override
//    public void clearConfigProgressByToken(TokenModel tokenModel) {
//        configProgressMap.remove(tokenModel.getToken());
//    }
}
