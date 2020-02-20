package com.nt.service_PHINE.DeviceCommunication;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.18
 * 2020-02-21T05:00:18.564+08:00
 * Generated source version: 3.1.18
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "IDeviceService")
@XmlSeeAlso({ObjectFactory.class})
public interface IDeviceService {

    @WebMethod(operationName = "OfflineDevice", action = "http://tempuri.org/IDeviceService/OfflineDevice")
    @Action(input = "http://tempuri.org/IDeviceService/OfflineDevice", output = "http://tempuri.org/IDeviceService/OfflineDeviceResponse")
    @RequestWrapper(localName = "OfflineDevice", targetNamespace = "http://tempuri.org/", className = "DeviceService.OfflineDevice")
    @ResponseWrapper(localName = "OfflineDeviceResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.OfflineDeviceResponse")
    @WebResult(name = "OfflineDeviceResult", targetNamespace = "http://tempuri.org/")
    public Boolean offlineDevice(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId
    );

    @WebMethod(operationName = "RegWrite", action = "http://tempuri.org/IDeviceService/RegWrite")
    @Action(input = "http://tempuri.org/IDeviceService/RegWrite", output = "http://tempuri.org/IDeviceService/RegWriteResponse")
    @RequestWrapper(localName = "RegWrite", targetNamespace = "http://tempuri.org/", className = "DeviceService.RegWrite")
    @ResponseWrapper(localName = "RegWriteResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.RegWriteResponse")
    @WebResult(name = "RegWriteResult", targetNamespace = "http://tempuri.org/")
    public Boolean regWrite(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId,
            @WebParam(name = "regAddr", targetNamespace = "http://tempuri.org/")
                    Long regAddr,
            @WebParam(name = "regData", targetNamespace = "http://tempuri.org/")
                    Long regData
    );

    @WebMethod(operationName = "SetFmcVoltageByFile", action = "http://tempuri.org/IDeviceService/SetFmcVoltageByFile")
    @Action(input = "http://tempuri.org/IDeviceService/SetFmcVoltageByFile", output = "http://tempuri.org/IDeviceService/SetFmcVoltageByFileResponse")
    @RequestWrapper(localName = "SetFmcVoltageByFile", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetFmcVoltageByFile")
    @ResponseWrapper(localName = "SetFmcVoltageByFileResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetFmcVoltageByFileResponse")
    @WebResult(name = "SetFmcVoltageByFileResult", targetNamespace = "http://tempuri.org/")
    public Boolean setFmcVoltageByFile(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId,
            @WebParam(name = "fmcId", targetNamespace = "http://tempuri.org/")
                    Long fmcId,
            @WebParam(name = "configFilePath", targetNamespace = "http://tempuri.org/")
                    String configFilePath
    );

    @WebMethod(operationName = "GetFpgaConfigStatus", action = "http://tempuri.org/IDeviceService/GetFpgaConfigStatus")
    @Action(input = "http://tempuri.org/IDeviceService/GetFpgaConfigStatus", output = "http://tempuri.org/IDeviceService/GetFpgaConfigStatusResponse")
    @RequestWrapper(localName = "GetFpgaConfigStatus", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetFpgaConfigStatus")
    @ResponseWrapper(localName = "GetFpgaConfigStatusResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetFpgaConfigStatusResponse")
    public void getFpgaConfigStatus(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId,
            @WebParam(mode = WebParam.Mode.INOUT, name = "configStatus", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<ConfigStatus> configStatus,
            @WebParam(mode = WebParam.Mode.OUT, name = "GetFpgaConfigStatusResult", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Boolean> getFpgaConfigStatusResult
    );

    @WebMethod(operationName = "GetCurrentConnStatus", action = "http://tempuri.org/IDeviceService/GetCurrentConnStatus")
    @Action(input = "http://tempuri.org/IDeviceService/GetCurrentConnStatus", output = "http://tempuri.org/IDeviceService/GetCurrentConnStatusResponse")
    @RequestWrapper(localName = "GetCurrentConnStatus", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetCurrentConnStatus")
    @ResponseWrapper(localName = "GetCurrentConnStatusResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetCurrentConnStatusResponse")
    public void getCurrentConnStatus(
            @WebParam(mode = WebParam.Mode.INOUT, name = "deviceConnStates", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<ArrayOfDeviceConnState> deviceConnStates,
            @WebParam(mode = WebParam.Mode.OUT, name = "GetCurrentConnStatusResult", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Boolean> getCurrentConnStatusResult
    );

    @WebMethod(operationName = "StartConfigFpgaByFile", action = "http://tempuri.org/IDeviceService/StartConfigFpgaByFile")
    @Action(input = "http://tempuri.org/IDeviceService/StartConfigFpgaByFile", output = "http://tempuri.org/IDeviceService/StartConfigFpgaByFileResponse")
    @RequestWrapper(localName = "StartConfigFpgaByFile", targetNamespace = "http://tempuri.org/", className = "DeviceService.StartConfigFpgaByFile")
    @ResponseWrapper(localName = "StartConfigFpgaByFileResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.StartConfigFpgaByFileResponse")
    @WebResult(name = "StartConfigFpgaByFileResult", targetNamespace = "http://tempuri.org/")
    public Boolean startConfigFpgaByFile(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId,
            @WebParam(name = "configFilePath", targetNamespace = "http://tempuri.org/")
                    String configFilePath
    );

    @WebMethod(operationName = "OpenConnection", action = "http://tempuri.org/IDeviceService/OpenConnection")
    @Action(input = "http://tempuri.org/IDeviceService/OpenConnection", output = "http://tempuri.org/IDeviceService/OpenConnectionResponse")
    @RequestWrapper(localName = "OpenConnection", targetNamespace = "http://tempuri.org/", className = "DeviceService.OpenConnection")
    @ResponseWrapper(localName = "OpenConnectionResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.OpenConnectionResponse")
    @WebResult(name = "OpenConnectionResult", targetNamespace = "http://tempuri.org/")
    public ConnectionResult openConnection(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId
    );

    @WebMethod(operationName = "CloseConnection", action = "http://tempuri.org/IDeviceService/CloseConnection")
    @Action(input = "http://tempuri.org/IDeviceService/CloseConnection", output = "http://tempuri.org/IDeviceService/CloseConnectionResponse")
    @RequestWrapper(localName = "CloseConnection", targetNamespace = "http://tempuri.org/", className = "DeviceService.CloseConnection")
    @ResponseWrapper(localName = "CloseConnectionResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.CloseConnectionResponse")
    @WebResult(name = "CloseConnectionResult", targetNamespace = "http://tempuri.org/")
    public Boolean closeConnection(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId
    );

    @WebMethod(operationName = "GetFmcVoltage", action = "http://tempuri.org/IDeviceService/GetFmcVoltage")
    @Action(input = "http://tempuri.org/IDeviceService/GetFmcVoltage", output = "http://tempuri.org/IDeviceService/GetFmcVoltageResponse")
    @RequestWrapper(localName = "GetFmcVoltage", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetFmcVoltage")
    @ResponseWrapper(localName = "GetFmcVoltageResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetFmcVoltageResponse")
    public void getFmcVoltage(
            @WebParam(mode = WebParam.Mode.INOUT, name = "fmcVoltageObject", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<FmcVoltageObject> fmcVoltageObject,
            @WebParam(mode = WebParam.Mode.OUT, name = "GetFmcVoltageResult", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Boolean> getFmcVoltageResult
    );

    @WebMethod(operationName = "SetPllClockByFile", action = "http://tempuri.org/IDeviceService/SetPllClockByFile")
    @Action(input = "http://tempuri.org/IDeviceService/SetPllClockByFile", output = "http://tempuri.org/IDeviceService/SetPllClockByFileResponse")
    @RequestWrapper(localName = "SetPllClockByFile", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetPllClockByFile")
    @ResponseWrapper(localName = "SetPllClockByFileResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetPllClockByFileResponse")
    @WebResult(name = "SetPllClockByFileResult", targetNamespace = "http://tempuri.org/")
    public Boolean setPllClockByFile(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId,
            @WebParam(name = "pllId", targetNamespace = "http://tempuri.org/")
                    Long pllId,
            @WebParam(name = "configFilePath", targetNamespace = "http://tempuri.org/")
                    String configFilePath
    );

    @WebMethod(operationName = "OnlineDevice", action = "http://tempuri.org/IDeviceService/OnlineDevice")
    @Action(input = "http://tempuri.org/IDeviceService/OnlineDevice", output = "http://tempuri.org/IDeviceService/OnlineDeviceResponse")
    @RequestWrapper(localName = "OnlineDevice", targetNamespace = "http://tempuri.org/", className = "DeviceService.OnlineDevice")
    @ResponseWrapper(localName = "OnlineDeviceResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.OnlineDeviceResponse")
    @WebResult(name = "OnlineDeviceResult", targetNamespace = "http://tempuri.org/")
    public Boolean onlineDevice(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId
    );

    @WebMethod(operationName = "GetPllClock", action = "http://tempuri.org/IDeviceService/GetPllClock")
    @Action(input = "http://tempuri.org/IDeviceService/GetPllClock", output = "http://tempuri.org/IDeviceService/GetPllClockResponse")
    @RequestWrapper(localName = "GetPllClock", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetPllClock")
    @ResponseWrapper(localName = "GetPllClockResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetPllClockResponse")
    public void getPllClock(
            @WebParam(mode = WebParam.Mode.INOUT, name = "pllClockObject", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<PllClockObject> pllClockObject,
            @WebParam(mode = WebParam.Mode.OUT, name = "GetPllClockResult", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Boolean> getPllClockResult
    );

    @WebMethod(operationName = "GetFpgaConfigProgress", action = "http://tempuri.org/IDeviceService/GetFpgaConfigProgress")
    @Action(input = "http://tempuri.org/IDeviceService/GetFpgaConfigProgress", output = "http://tempuri.org/IDeviceService/GetFpgaConfigProgressResponse")
    @RequestWrapper(localName = "GetFpgaConfigProgress", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetFpgaConfigProgress")
    @ResponseWrapper(localName = "GetFpgaConfigProgressResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.GetFpgaConfigProgressResponse")
    public void getFpgaConfigProgress(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId,
            @WebParam(mode = WebParam.Mode.INOUT, name = "progress", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Long> progress,
            @WebParam(mode = WebParam.Mode.OUT, name = "GetFpgaConfigProgressResult", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Boolean> getFpgaConfigProgressResult
    );

    @WebMethod(operationName = "RegRead", action = "http://tempuri.org/IDeviceService/RegRead")
    @Action(input = "http://tempuri.org/IDeviceService/RegRead", output = "http://tempuri.org/IDeviceService/RegReadResponse")
    @RequestWrapper(localName = "RegRead", targetNamespace = "http://tempuri.org/", className = "DeviceService.RegRead")
    @ResponseWrapper(localName = "RegReadResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.RegReadResponse")
    public void regRead(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId,
            @WebParam(name = "regAddr", targetNamespace = "http://tempuri.org/")
                    Long regAddr,
            @WebParam(mode = WebParam.Mode.INOUT, name = "regData", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Long> regData,
            @WebParam(mode = WebParam.Mode.OUT, name = "RegReadResult", targetNamespace = "http://tempuri.org/")
                    javax.xml.ws.Holder<Boolean> regReadResult
    );

    @WebMethod(operationName = "ReConfigFpga", action = "http://tempuri.org/IDeviceService/ReConfigFpga")
    @Action(input = "http://tempuri.org/IDeviceService/ReConfigFpga", output = "http://tempuri.org/IDeviceService/ReConfigFpgaResponse")
    @RequestWrapper(localName = "ReConfigFpga", targetNamespace = "http://tempuri.org/", className = "DeviceService.ReConfigFpga")
    @ResponseWrapper(localName = "ReConfigFpgaResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.ReConfigFpgaResponse")
    @WebResult(name = "ReConfigFpgaResult", targetNamespace = "http://tempuri.org/")
    public Boolean reConfigFpga(
            @WebParam(name = "deviceId", targetNamespace = "http://tempuri.org/")
                    String deviceId,
            @WebParam(name = "fpgaId", targetNamespace = "http://tempuri.org/")
                    Long fpgaId
    );

    @WebMethod(operationName = "StartConfigFpga", action = "http://tempuri.org/IDeviceService/StartConfigFpga")
    @Action(input = "http://tempuri.org/IDeviceService/StartConfigFpga", output = "http://tempuri.org/IDeviceService/StartConfigFpgaResponse")
    @RequestWrapper(localName = "StartConfigFpga", targetNamespace = "http://tempuri.org/", className = "DeviceService.StartConfigFpga")
    @ResponseWrapper(localName = "StartConfigFpgaResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.StartConfigFpgaResponse")
    @WebResult(name = "StartConfigFpgaResult", targetNamespace = "http://tempuri.org/")
    public Boolean startConfigFpga(
            @WebParam(name = "fpgaConfigObject", targetNamespace = "http://tempuri.org/")
                    FpgaConfigObject fpgaConfigObject
    );

    @WebMethod(operationName = "SetPllClock", action = "http://tempuri.org/IDeviceService/SetPllClock")
    @Action(input = "http://tempuri.org/IDeviceService/SetPllClock", output = "http://tempuri.org/IDeviceService/SetPllClockResponse")
    @RequestWrapper(localName = "SetPllClock", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetPllClock")
    @ResponseWrapper(localName = "SetPllClockResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetPllClockResponse")
    @WebResult(name = "SetPllClockResult", targetNamespace = "http://tempuri.org/")
    public Boolean setPllClock(
            @WebParam(name = "pllClockObject", targetNamespace = "http://tempuri.org/")
                    PllClockObject pllClockObject
    );

    @WebMethod(operationName = "SetFmcVoltage", action = "http://tempuri.org/IDeviceService/SetFmcVoltage")
    @Action(input = "http://tempuri.org/IDeviceService/SetFmcVoltage", output = "http://tempuri.org/IDeviceService/SetFmcVoltageResponse")
    @RequestWrapper(localName = "SetFmcVoltage", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetFmcVoltage")
    @ResponseWrapper(localName = "SetFmcVoltageResponse", targetNamespace = "http://tempuri.org/", className = "DeviceService.SetFmcVoltageResponse")
    @WebResult(name = "SetFmcVoltageResult", targetNamespace = "http://tempuri.org/")
    public Boolean setFmcVoltage(
            @WebParam(name = "fmcVoltageObject", targetNamespace = "http://tempuri.org/")
                    FmcVoltageObject fmcVoltageObject
    );
}
