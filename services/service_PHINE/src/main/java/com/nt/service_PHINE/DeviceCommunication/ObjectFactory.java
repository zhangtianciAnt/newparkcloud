
package com.nt.service_PHINE.DeviceCommunication;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the DeviceService package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _ArrayOfstring_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfstring");
    private final static QName _ArrayOfint_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfint");
    private final static QName _ArrayOfboolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfboolean");
    private final static QName _ArrayOffloat_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOffloat");
    private final static QName _BoardInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "BoardInfo");
    private final static QName _BoardType_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "BoardType");
    private final static QName _ChipType_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ChipType");
    private final static QName _RunningStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "RunningStatus");
    private final static QName _DeviceInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "DeviceInfo");
    private final static QName _ConnectionStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ConnectionStatus");
    private final static QName _OnlineStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "OnlineStatus");
    private final static QName _OnLineDeviceInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "OnLineDeviceInfo");
    private final static QName _ArrayOfDeviceConnState_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ArrayOfDeviceConnState");
    private final static QName _DeviceConnState_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "DeviceConnState");
    private final static QName _ConnectionResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ConnectionResult");
    private final static QName _FmcVoltageObject_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "FmcVoltageObject");
    private final static QName _PllClockObject_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "PllClockObject");
    private final static QName _FpgaConfigObject_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "FpgaConfigObject");
    private final static QName _FpgaControlBit_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "FpgaControlBit");
    private final static QName _ConfigStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ConfigStatus");
    private final static QName _ObjectFpgaBatchAccess_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "Object_FpgaBatchAccess");
    private final static QName _ArrayOfObjectFpgaAccess_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ArrayOfObject_FpgaAccess");
    private final static QName _ObjectFpgaAccess_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "Object_FpgaAccess");
    private final static QName _ArrayOfDeviceSlotInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ArrayOfDeviceSlotInfo");
    private final static QName _DeviceSlotInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "DeviceSlotInfo");
    private final static QName _InterConnTestState_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "InterConnTestState");
    private final static QName _InterConnTestItem_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "InterConnTestItem");
    private final static QName _AddBoardBoardInfo_QNAME = new QName("http://tempuri.org/", "boardInfo");
    private final static QName _RemoveBoardBoardId_QNAME = new QName("http://tempuri.org/", "boardId");
    private final static QName _AddDeviceDeviceInfo_QNAME = new QName("http://tempuri.org/", "deviceInfo");
    private final static QName _RemoveDeviceDeviceId_QNAME = new QName("http://tempuri.org/", "deviceId");
    private final static QName _OnlineDeviceAllOnlineInfo_QNAME = new QName("http://tempuri.org/", "OnlineInfo");
    private final static QName _GetCurrentConnStatusDeviceConnStates_QNAME = new QName("http://tempuri.org/", "deviceConnStates");
    private final static QName _SetFmcVoltageByFileConfigFilePath_QNAME = new QName("http://tempuri.org/", "configFilePath");
    private final static QName _SetFmcVoltageFmcVoltageObject_QNAME = new QName("http://tempuri.org/", "fmcVoltageObject");
    private final static QName _SetPllClockPllClockObject_QNAME = new QName("http://tempuri.org/", "pllClockObject");
    private final static QName _StartConfigFpgaFpgaConfigObject_QNAME = new QName("http://tempuri.org/", "fpgaConfigObject");
    private final static QName _RegBatReadFpgaBatchAccessObject_QNAME = new QName("http://tempuri.org/", "fpgaBatchAccessObject");
    private final static QName _SelfTestFilepath_QNAME = new QName("http://tempuri.org/", "filepath");
    private final static QName _InterconnTestStartDeviceSlotInfo_QNAME = new QName("http://tempuri.org/", "deviceSlotInfo");
    private final static QName _InterconnTestStartFilePath_QNAME = new QName("http://tempuri.org/", "FilePath");
    private final static QName _InterconnGetProcessCurrTestState_QNAME = new QName("http://tempuri.org/", "CurrTestState");
    private final static QName _InterconnGetResultInterConnStatus_QNAME = new QName("http://tempuri.org/", "InterConnStatus");
    private final static QName _InterconnGetResultResultFilePath_QNAME = new QName("http://tempuri.org/", "ResultFilePath");
    private final static QName _DeviceSlotInfoDeviceId_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "DeviceId");
    private final static QName _DeviceConnStateDeviceId_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "deviceId");
    private final static QName _ObjectFpgaBatchAccessAccessObjects_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "AccessObjects");
    private final static QName _FpgaConfigObjectBinFilePath_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "BinFilePath");
    private final static QName _PllClockObjectOutputClockEnables_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "OutputClockEnables");
    private final static QName _PllClockObjectOutputClockFrequencys_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "OutputClockFrequencys");
    private final static QName _OnLineDeviceInfoOnlineResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "OnlineResult");
    private final static QName _DeviceInfoDeviceType_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "deviceType");
    private final static QName _DeviceInfoDeviceUUID_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "deviceUUID");
    private final static QName _DeviceInfoOwnedBoardIds_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ownedBoardIds");
    private final static QName _BoardInfoBelongDeviceSlotId_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "belongDeviceSlotId");
    private final static QName _BoardInfoBoardId_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "boardId");
    private final static QName _BoardInfoIpAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ipAddress");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: DeviceService
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AddBoard }
     *
     */
    public AddBoard createAddBoard() {
        return new AddBoard();
    }

    /**
     * Create an instance of {@link BoardInfo }
     *
     */
    public BoardInfo createBoardInfo() {
        return new BoardInfo();
    }

    /**
     * Create an instance of {@link AddBoardResponse }
     *
     */
    public AddBoardResponse createAddBoardResponse() {
        return new AddBoardResponse();
    }

    /**
     * Create an instance of {@link RemoveBoard }
     *
     */
    public RemoveBoard createRemoveBoard() {
        return new RemoveBoard();
    }

    /**
     * Create an instance of {@link RemoveBoardResponse }
     *
     */
    public RemoveBoardResponse createRemoveBoardResponse() {
        return new RemoveBoardResponse();
    }

    /**
     * Create an instance of {@link AddDevice }
     *
     */
    public AddDevice createAddDevice() {
        return new AddDevice();
    }

    /**
     * Create an instance of {@link DeviceInfo }
     *
     */
    public DeviceInfo createDeviceInfo() {
        return new DeviceInfo();
    }

    /**
     * Create an instance of {@link AddDeviceResponse }
     *
     */
    public AddDeviceResponse createAddDeviceResponse() {
        return new AddDeviceResponse();
    }

    /**
     * Create an instance of {@link RemoveDevice }
     *
     */
    public RemoveDevice createRemoveDevice() {
        return new RemoveDevice();
    }

    /**
     * Create an instance of {@link RemoveDeviceResponse }
     *
     */
    public RemoveDeviceResponse createRemoveDeviceResponse() {
        return new RemoveDeviceResponse();
    }

    /**
     * Create an instance of {@link OnlineDevice }
     *
     */
    public OnlineDevice createOnlineDevice() {
        return new OnlineDevice();
    }

    /**
     * Create an instance of {@link OnlineDeviceResponse }
     *
     */
    public OnlineDeviceResponse createOnlineDeviceResponse() {
        return new OnlineDeviceResponse();
    }

    /**
     * Create an instance of {@link OnlineDeviceAll }
     *
     */
    public OnlineDeviceAll createOnlineDeviceAll() {
        return new OnlineDeviceAll();
    }

    /**
     * Create an instance of {@link OnLineDeviceInfo }
     *
     */
    public OnLineDeviceInfo createOnLineDeviceInfo() {
        return new OnLineDeviceInfo();
    }

    /**
     * Create an instance of {@link OnlineDeviceAllResponse }
     *
     */
    public OnlineDeviceAllResponse createOnlineDeviceAllResponse() {
        return new OnlineDeviceAllResponse();
    }

    /**
     * Create an instance of {@link OfflineDevice }
     *
     */
    public OfflineDevice createOfflineDevice() {
        return new OfflineDevice();
    }

    /**
     * Create an instance of {@link OfflineDeviceResponse }
     *
     */
    public OfflineDeviceResponse createOfflineDeviceResponse() {
        return new OfflineDeviceResponse();
    }

    /**
     * Create an instance of {@link GetCurrentConnStatus }
     *
     */
    public GetCurrentConnStatus createGetCurrentConnStatus() {
        return new GetCurrentConnStatus();
    }

    /**
     * Create an instance of {@link ArrayOfDeviceConnState }
     *
     */
    public ArrayOfDeviceConnState createArrayOfDeviceConnState() {
        return new ArrayOfDeviceConnState();
    }

    /**
     * Create an instance of {@link GetCurrentConnStatusResponse }
     *
     */
    public GetCurrentConnStatusResponse createGetCurrentConnStatusResponse() {
        return new GetCurrentConnStatusResponse();
    }

    /**
     * Create an instance of {@link OpenConnection }
     *
     */
    public OpenConnection createOpenConnection() {
        return new OpenConnection();
    }

    /**
     * Create an instance of {@link OpenConnectionResponse }
     *
     */
    public OpenConnectionResponse createOpenConnectionResponse() {
        return new OpenConnectionResponse();
    }

    /**
     * Create an instance of {@link CloseConnection }
     *
     */
    public CloseConnection createCloseConnection() {
        return new CloseConnection();
    }

    /**
     * Create an instance of {@link CloseConnectionResponse }
     *
     */
    public CloseConnectionResponse createCloseConnectionResponse() {
        return new CloseConnectionResponse();
    }

    /**
     * Create an instance of {@link SetFmcVoltageByFile }
     *
     */
    public SetFmcVoltageByFile createSetFmcVoltageByFile() {
        return new SetFmcVoltageByFile();
    }

    /**
     * Create an instance of {@link SetFmcVoltageByFileResponse }
     *
     */
    public SetFmcVoltageByFileResponse createSetFmcVoltageByFileResponse() {
        return new SetFmcVoltageByFileResponse();
    }

    /**
     * Create an instance of {@link SetFmcVoltage }
     *
     */
    public SetFmcVoltage createSetFmcVoltage() {
        return new SetFmcVoltage();
    }

    /**
     * Create an instance of {@link FmcVoltageObject }
     *
     */
    public FmcVoltageObject createFmcVoltageObject() {
        return new FmcVoltageObject();
    }

    /**
     * Create an instance of {@link SetFmcVoltageResponse }
     *
     */
    public SetFmcVoltageResponse createSetFmcVoltageResponse() {
        return new SetFmcVoltageResponse();
    }

    /**
     * Create an instance of {@link GetFmcVoltage }
     *
     */
    public GetFmcVoltage createGetFmcVoltage() {
        return new GetFmcVoltage();
    }

    /**
     * Create an instance of {@link GetFmcVoltageResponse }
     *
     */
    public GetFmcVoltageResponse createGetFmcVoltageResponse() {
        return new GetFmcVoltageResponse();
    }

    /**
     * Create an instance of {@link SetPllClockByFile }
     *
     */
    public SetPllClockByFile createSetPllClockByFile() {
        return new SetPllClockByFile();
    }

    /**
     * Create an instance of {@link SetPllClockByFileResponse }
     *
     */
    public SetPllClockByFileResponse createSetPllClockByFileResponse() {
        return new SetPllClockByFileResponse();
    }

    /**
     * Create an instance of {@link SetPllClock }
     *
     */
    public SetPllClock createSetPllClock() {
        return new SetPllClock();
    }

    /**
     * Create an instance of {@link PllClockObject }
     *
     */
    public PllClockObject createPllClockObject() {
        return new PllClockObject();
    }

    /**
     * Create an instance of {@link SetPllClockResponse }
     *
     */
    public SetPllClockResponse createSetPllClockResponse() {
        return new SetPllClockResponse();
    }

    /**
     * Create an instance of {@link GetPllClock }
     *
     */
    public GetPllClock createGetPllClock() {
        return new GetPllClock();
    }

    /**
     * Create an instance of {@link GetPllClockResponse }
     *
     */
    public GetPllClockResponse createGetPllClockResponse() {
        return new GetPllClockResponse();
    }

    /**
     * Create an instance of {@link StartConfigFpgaByFile }
     *
     */
    public StartConfigFpgaByFile createStartConfigFpgaByFile() {
        return new StartConfigFpgaByFile();
    }

    /**
     * Create an instance of {@link StartConfigFpgaByFileResponse }
     *
     */
    public StartConfigFpgaByFileResponse createStartConfigFpgaByFileResponse() {
        return new StartConfigFpgaByFileResponse();
    }

    /**
     * Create an instance of {@link StartConfigFpga }
     *
     */
    public StartConfigFpga createStartConfigFpga() {
        return new StartConfigFpga();
    }

    /**
     * Create an instance of {@link FpgaConfigObject }
     *
     */
    public FpgaConfigObject createFpgaConfigObject() {
        return new FpgaConfigObject();
    }

    /**
     * Create an instance of {@link StartConfigFpgaResponse }
     *
     */
    public StartConfigFpgaResponse createStartConfigFpgaResponse() {
        return new StartConfigFpgaResponse();
    }

    /**
     * Create an instance of {@link GetFpgaConfigStatus }
     *
     */
    public GetFpgaConfigStatus createGetFpgaConfigStatus() {
        return new GetFpgaConfigStatus();
    }

    /**
     * Create an instance of {@link GetFpgaConfigStatusResponse }
     *
     */
    public GetFpgaConfigStatusResponse createGetFpgaConfigStatusResponse() {
        return new GetFpgaConfigStatusResponse();
    }

    /**
     * Create an instance of {@link GetFpgaConfigProgress }
     *
     */
    public GetFpgaConfigProgress createGetFpgaConfigProgress() {
        return new GetFpgaConfigProgress();
    }

    /**
     * Create an instance of {@link GetFpgaConfigProgressResponse }
     *
     */
    public GetFpgaConfigProgressResponse createGetFpgaConfigProgressResponse() {
        return new GetFpgaConfigProgressResponse();
    }

    /**
     * Create an instance of {@link ReConfigFpga }
     *
     */
    public ReConfigFpga createReConfigFpga() {
        return new ReConfigFpga();
    }

    /**
     * Create an instance of {@link ReConfigFpgaResponse }
     *
     */
    public ReConfigFpgaResponse createReConfigFpgaResponse() {
        return new ReConfigFpgaResponse();
    }

    /**
     * Create an instance of {@link RegRead }
     *
     */
    public RegRead createRegRead() {
        return new RegRead();
    }

    /**
     * Create an instance of {@link RegReadResponse }
     *
     */
    public RegReadResponse createRegReadResponse() {
        return new RegReadResponse();
    }

    /**
     * Create an instance of {@link RegWrite }
     *
     */
    public RegWrite createRegWrite() {
        return new RegWrite();
    }

    /**
     * Create an instance of {@link RegWriteResponse }
     *
     */
    public RegWriteResponse createRegWriteResponse() {
        return new RegWriteResponse();
    }

    /**
     * Create an instance of {@link RegBatRead }
     *
     */
    public RegBatRead createRegBatRead() {
        return new RegBatRead();
    }

    /**
     * Create an instance of {@link ObjectFpgaBatchAccess }
     *
     */
    public ObjectFpgaBatchAccess createObjectFpgaBatchAccess() {
        return new ObjectFpgaBatchAccess();
    }

    /**
     * Create an instance of {@link RegBatReadResponse }
     *
     */
    public RegBatReadResponse createRegBatReadResponse() {
        return new RegBatReadResponse();
    }

    /**
     * Create an instance of {@link RegBatWrite }
     *
     */
    public RegBatWrite createRegBatWrite() {
        return new RegBatWrite();
    }

    /**
     * Create an instance of {@link RegBatWriteResponse }
     *
     */
    public RegBatWriteResponse createRegBatWriteResponse() {
        return new RegBatWriteResponse();
    }

    /**
     * Create an instance of {@link SelfTest }
     *
     */
    public SelfTest createSelfTest() {
        return new SelfTest();
    }

    /**
     * Create an instance of {@link ArrayOfstring }
     *
     */
    public ArrayOfstring createArrayOfstring() {
        return new ArrayOfstring();
    }

    /**
     * Create an instance of {@link SelfTestResponse }
     *
     */
    public SelfTestResponse createSelfTestResponse() {
        return new SelfTestResponse();
    }

    /**
     * Create an instance of {@link InterconnTestStart }
     *
     */
    public InterconnTestStart createInterconnTestStart() {
        return new InterconnTestStart();
    }

    /**
     * Create an instance of {@link ArrayOfDeviceSlotInfo }
     *
     */
    public ArrayOfDeviceSlotInfo createArrayOfDeviceSlotInfo() {
        return new ArrayOfDeviceSlotInfo();
    }

    /**
     * Create an instance of {@link InterconnTestStartResponse }
     *
     */
    public InterconnTestStartResponse createInterconnTestStartResponse() {
        return new InterconnTestStartResponse();
    }

    /**
     * Create an instance of {@link InterconnGetProcess }
     *
     */
    public InterconnGetProcess createInterconnGetProcess() {
        return new InterconnGetProcess();
    }

    /**
     * Create an instance of {@link InterConnTestState }
     *
     */
    public InterConnTestState createInterConnTestState() {
        return new InterConnTestState();
    }

    /**
     * Create an instance of {@link InterconnGetProcessResponse }
     *
     */
    public InterconnGetProcessResponse createInterconnGetProcessResponse() {
        return new InterconnGetProcessResponse();
    }

    /**
     * Create an instance of {@link InterconnGetResult }
     *
     */
    public InterconnGetResult createInterconnGetResult() {
        return new InterconnGetResult();
    }

    /**
     * Create an instance of {@link ArrayOfint }
     *
     */
    public ArrayOfint createArrayOfint() {
        return new ArrayOfint();
    }

    /**
     * Create an instance of {@link InterconnGetResultResponse }
     *
     */
    public InterconnGetResultResponse createInterconnGetResultResponse() {
        return new InterconnGetResultResponse();
    }

    /**
     * Create an instance of {@link ArrayOfboolean }
     *
     */
    public ArrayOfboolean createArrayOfboolean() {
        return new ArrayOfboolean();
    }

    /**
     * Create an instance of {@link ArrayOffloat }
     *
     */
    public ArrayOffloat createArrayOffloat() {
        return new ArrayOffloat();
    }

    /**
     * Create an instance of {@link DeviceConnState }
     *
     */
    public DeviceConnState createDeviceConnState() {
        return new DeviceConnState();
    }

    /**
     * Create an instance of {@link ArrayOfObjectFpgaAccess }
     *
     */
    public ArrayOfObjectFpgaAccess createArrayOfObjectFpgaAccess() {
        return new ArrayOfObjectFpgaAccess();
    }

    /**
     * Create an instance of {@link ObjectFpgaAccess }
     *
     */
    public ObjectFpgaAccess createObjectFpgaAccess() {
        return new ObjectFpgaAccess();
    }

    /**
     * Create an instance of {@link DeviceSlotInfo }
     *
     */
    public DeviceSlotInfo createDeviceSlotInfo() {
        return new DeviceSlotInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfstring")
    public JAXBElement<ArrayOfstring> createArrayOfstring(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ArrayOfstring_QNAME, ArrayOfstring.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfint")
    public JAXBElement<ArrayOfint> createArrayOfint(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_ArrayOfint_QNAME, ArrayOfint.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfboolean }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfboolean")
    public JAXBElement<ArrayOfboolean> createArrayOfboolean(ArrayOfboolean value) {
        return new JAXBElement<ArrayOfboolean>(_ArrayOfboolean_QNAME, ArrayOfboolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOffloat }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOffloat")
    public JAXBElement<ArrayOffloat> createArrayOffloat(ArrayOffloat value) {
        return new JAXBElement<ArrayOffloat>(_ArrayOffloat_QNAME, ArrayOffloat.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BoardInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "BoardInfo")
    public JAXBElement<BoardInfo> createBoardInfo(BoardInfo value) {
        return new JAXBElement<BoardInfo>(_BoardInfo_QNAME, BoardInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BoardType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "BoardType")
    public JAXBElement<BoardType> createBoardType(BoardType value) {
        return new JAXBElement<BoardType>(_BoardType_QNAME, BoardType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChipType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ChipType")
    public JAXBElement<ChipType> createChipType(ChipType value) {
        return new JAXBElement<ChipType>(_ChipType_QNAME, ChipType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RunningStatus }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "RunningStatus")
    public JAXBElement<RunningStatus> createRunningStatus(RunningStatus value) {
        return new JAXBElement<RunningStatus>(_RunningStatus_QNAME, RunningStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeviceInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "DeviceInfo")
    public JAXBElement<DeviceInfo> createDeviceInfo(DeviceInfo value) {
        return new JAXBElement<DeviceInfo>(_DeviceInfo_QNAME, DeviceInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConnectionStatus }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ConnectionStatus")
    public JAXBElement<ConnectionStatus> createConnectionStatus(ConnectionStatus value) {
        return new JAXBElement<ConnectionStatus>(_ConnectionStatus_QNAME, ConnectionStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OnlineStatus }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "OnlineStatus")
    public JAXBElement<OnlineStatus> createOnlineStatus(OnlineStatus value) {
        return new JAXBElement<OnlineStatus>(_OnlineStatus_QNAME, OnlineStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OnLineDeviceInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "OnLineDeviceInfo")
    public JAXBElement<OnLineDeviceInfo> createOnLineDeviceInfo(OnLineDeviceInfo value) {
        return new JAXBElement<OnLineDeviceInfo>(_OnLineDeviceInfo_QNAME, OnLineDeviceInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDeviceConnState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ArrayOfDeviceConnState")
    public JAXBElement<ArrayOfDeviceConnState> createArrayOfDeviceConnState(ArrayOfDeviceConnState value) {
        return new JAXBElement<ArrayOfDeviceConnState>(_ArrayOfDeviceConnState_QNAME, ArrayOfDeviceConnState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeviceConnState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "DeviceConnState")
    public JAXBElement<DeviceConnState> createDeviceConnState(DeviceConnState value) {
        return new JAXBElement<DeviceConnState>(_DeviceConnState_QNAME, DeviceConnState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConnectionResult }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ConnectionResult")
    public JAXBElement<ConnectionResult> createConnectionResult(ConnectionResult value) {
        return new JAXBElement<ConnectionResult>(_ConnectionResult_QNAME, ConnectionResult.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "FmcVoltageObject")
    public JAXBElement<FmcVoltageObject> createFmcVoltageObject(FmcVoltageObject value) {
        return new JAXBElement<FmcVoltageObject>(_FmcVoltageObject_QNAME, FmcVoltageObject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PllClockObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "PllClockObject")
    public JAXBElement<PllClockObject> createPllClockObject(PllClockObject value) {
        return new JAXBElement<PllClockObject>(_PllClockObject_QNAME, PllClockObject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FpgaConfigObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "FpgaConfigObject")
    public JAXBElement<FpgaConfigObject> createFpgaConfigObject(FpgaConfigObject value) {
        return new JAXBElement<FpgaConfigObject>(_FpgaConfigObject_QNAME, FpgaConfigObject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FpgaControlBit }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "FpgaControlBit")
    public JAXBElement<FpgaControlBit> createFpgaControlBit(FpgaControlBit value) {
        return new JAXBElement<FpgaControlBit>(_FpgaControlBit_QNAME, FpgaControlBit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConfigStatus }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ConfigStatus")
    public JAXBElement<ConfigStatus> createConfigStatus(ConfigStatus value) {
        return new JAXBElement<ConfigStatus>(_ConfigStatus_QNAME, ConfigStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectFpgaBatchAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "Object_FpgaBatchAccess")
    public JAXBElement<ObjectFpgaBatchAccess> createObjectFpgaBatchAccess(ObjectFpgaBatchAccess value) {
        return new JAXBElement<ObjectFpgaBatchAccess>(_ObjectFpgaBatchAccess_QNAME, ObjectFpgaBatchAccess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfObjectFpgaAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ArrayOfObject_FpgaAccess")
    public JAXBElement<ArrayOfObjectFpgaAccess> createArrayOfObjectFpgaAccess(ArrayOfObjectFpgaAccess value) {
        return new JAXBElement<ArrayOfObjectFpgaAccess>(_ArrayOfObjectFpgaAccess_QNAME, ArrayOfObjectFpgaAccess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectFpgaAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "Object_FpgaAccess")
    public JAXBElement<ObjectFpgaAccess> createObjectFpgaAccess(ObjectFpgaAccess value) {
        return new JAXBElement<ObjectFpgaAccess>(_ObjectFpgaAccess_QNAME, ObjectFpgaAccess.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDeviceSlotInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ArrayOfDeviceSlotInfo")
    public JAXBElement<ArrayOfDeviceSlotInfo> createArrayOfDeviceSlotInfo(ArrayOfDeviceSlotInfo value) {
        return new JAXBElement<ArrayOfDeviceSlotInfo>(_ArrayOfDeviceSlotInfo_QNAME, ArrayOfDeviceSlotInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeviceSlotInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "DeviceSlotInfo")
    public JAXBElement<DeviceSlotInfo> createDeviceSlotInfo(DeviceSlotInfo value) {
        return new JAXBElement<DeviceSlotInfo>(_DeviceSlotInfo_QNAME, DeviceSlotInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InterConnTestState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "InterConnTestState")
    public JAXBElement<InterConnTestState> createInterConnTestState(InterConnTestState value) {
        return new JAXBElement<InterConnTestState>(_InterConnTestState_QNAME, InterConnTestState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InterConnTestItem }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "InterConnTestItem")
    public JAXBElement<InterConnTestItem> createInterConnTestItem(InterConnTestItem value) {
        return new JAXBElement<InterConnTestItem>(_InterConnTestItem_QNAME, InterConnTestItem.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BoardInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "boardInfo", scope = AddBoard.class)
    public JAXBElement<BoardInfo> createAddBoardBoardInfo(BoardInfo value) {
        return new JAXBElement<BoardInfo>(_AddBoardBoardInfo_QNAME, BoardInfo.class, AddBoard.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "boardId", scope = RemoveBoard.class)
    public JAXBElement<String> createRemoveBoardBoardId(String value) {
        return new JAXBElement<String>(_RemoveBoardBoardId_QNAME, String.class, RemoveBoard.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeviceInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceInfo", scope = AddDevice.class)
    public JAXBElement<DeviceInfo> createAddDeviceDeviceInfo(DeviceInfo value) {
        return new JAXBElement<DeviceInfo>(_AddDeviceDeviceInfo_QNAME, DeviceInfo.class, AddDevice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = RemoveDevice.class)
    public JAXBElement<String> createRemoveDeviceDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, RemoveDevice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = OnlineDevice.class)
    public JAXBElement<String> createOnlineDeviceDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, OnlineDevice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OnLineDeviceInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "OnlineInfo", scope = OnlineDeviceAll.class)
    public JAXBElement<OnLineDeviceInfo> createOnlineDeviceAllOnlineInfo(OnLineDeviceInfo value) {
        return new JAXBElement<OnLineDeviceInfo>(_OnlineDeviceAllOnlineInfo_QNAME, OnLineDeviceInfo.class, OnlineDeviceAll.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OnLineDeviceInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "OnlineInfo", scope = OnlineDeviceAllResponse.class)
    public JAXBElement<OnLineDeviceInfo> createOnlineDeviceAllResponseOnlineInfo(OnLineDeviceInfo value) {
        return new JAXBElement<OnLineDeviceInfo>(_OnlineDeviceAllOnlineInfo_QNAME, OnLineDeviceInfo.class, OnlineDeviceAllResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = OfflineDevice.class)
    public JAXBElement<String> createOfflineDeviceDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, OfflineDevice.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDeviceConnState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceConnStates", scope = GetCurrentConnStatus.class)
    public JAXBElement<ArrayOfDeviceConnState> createGetCurrentConnStatusDeviceConnStates(ArrayOfDeviceConnState value) {
        return new JAXBElement<ArrayOfDeviceConnState>(_GetCurrentConnStatusDeviceConnStates_QNAME, ArrayOfDeviceConnState.class, GetCurrentConnStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDeviceConnState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceConnStates", scope = GetCurrentConnStatusResponse.class)
    public JAXBElement<ArrayOfDeviceConnState> createGetCurrentConnStatusResponseDeviceConnStates(ArrayOfDeviceConnState value) {
        return new JAXBElement<ArrayOfDeviceConnState>(_GetCurrentConnStatusDeviceConnStates_QNAME, ArrayOfDeviceConnState.class, GetCurrentConnStatusResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = OpenConnection.class)
    public JAXBElement<String> createOpenConnectionDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, OpenConnection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = CloseConnection.class)
    public JAXBElement<String> createCloseConnectionDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, CloseConnection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = SetFmcVoltageByFile.class)
    public JAXBElement<String> createSetFmcVoltageByFileDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, SetFmcVoltageByFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "configFilePath", scope = SetFmcVoltageByFile.class)
    public JAXBElement<String> createSetFmcVoltageByFileConfigFilePath(String value) {
        return new JAXBElement<String>(_SetFmcVoltageByFileConfigFilePath_QNAME, String.class, SetFmcVoltageByFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fmcVoltageObject", scope = SetFmcVoltage.class)
    public JAXBElement<FmcVoltageObject> createSetFmcVoltageFmcVoltageObject(FmcVoltageObject value) {
        return new JAXBElement<FmcVoltageObject>(_SetFmcVoltageFmcVoltageObject_QNAME, FmcVoltageObject.class, SetFmcVoltage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fmcVoltageObject", scope = GetFmcVoltage.class)
    public JAXBElement<FmcVoltageObject> createGetFmcVoltageFmcVoltageObject(FmcVoltageObject value) {
        return new JAXBElement<FmcVoltageObject>(_SetFmcVoltageFmcVoltageObject_QNAME, FmcVoltageObject.class, GetFmcVoltage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FmcVoltageObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fmcVoltageObject", scope = GetFmcVoltageResponse.class)
    public JAXBElement<FmcVoltageObject> createGetFmcVoltageResponseFmcVoltageObject(FmcVoltageObject value) {
        return new JAXBElement<FmcVoltageObject>(_SetFmcVoltageFmcVoltageObject_QNAME, FmcVoltageObject.class, GetFmcVoltageResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = SetPllClockByFile.class)
    public JAXBElement<String> createSetPllClockByFileDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, SetPllClockByFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "configFilePath", scope = SetPllClockByFile.class)
    public JAXBElement<String> createSetPllClockByFileConfigFilePath(String value) {
        return new JAXBElement<String>(_SetFmcVoltageByFileConfigFilePath_QNAME, String.class, SetPllClockByFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PllClockObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pllClockObject", scope = SetPllClock.class)
    public JAXBElement<PllClockObject> createSetPllClockPllClockObject(PllClockObject value) {
        return new JAXBElement<PllClockObject>(_SetPllClockPllClockObject_QNAME, PllClockObject.class, SetPllClock.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PllClockObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pllClockObject", scope = GetPllClock.class)
    public JAXBElement<PllClockObject> createGetPllClockPllClockObject(PllClockObject value) {
        return new JAXBElement<PllClockObject>(_SetPllClockPllClockObject_QNAME, PllClockObject.class, GetPllClock.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PllClockObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pllClockObject", scope = GetPllClockResponse.class)
    public JAXBElement<PllClockObject> createGetPllClockResponsePllClockObject(PllClockObject value) {
        return new JAXBElement<PllClockObject>(_SetPllClockPllClockObject_QNAME, PllClockObject.class, GetPllClockResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = StartConfigFpgaByFile.class)
    public JAXBElement<String> createStartConfigFpgaByFileDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, StartConfigFpgaByFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "configFilePath", scope = StartConfigFpgaByFile.class)
    public JAXBElement<String> createStartConfigFpgaByFileConfigFilePath(String value) {
        return new JAXBElement<String>(_SetFmcVoltageByFileConfigFilePath_QNAME, String.class, StartConfigFpgaByFile.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FpgaConfigObject }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fpgaConfigObject", scope = StartConfigFpga.class)
    public JAXBElement<FpgaConfigObject> createStartConfigFpgaFpgaConfigObject(FpgaConfigObject value) {
        return new JAXBElement<FpgaConfigObject>(_StartConfigFpgaFpgaConfigObject_QNAME, FpgaConfigObject.class, StartConfigFpga.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = GetFpgaConfigStatus.class)
    public JAXBElement<String> createGetFpgaConfigStatusDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, GetFpgaConfigStatus.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = GetFpgaConfigProgress.class)
    public JAXBElement<String> createGetFpgaConfigProgressDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, GetFpgaConfigProgress.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = ReConfigFpga.class)
    public JAXBElement<String> createReConfigFpgaDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, ReConfigFpga.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = RegRead.class)
    public JAXBElement<String> createRegReadDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, RegRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = RegWrite.class)
    public JAXBElement<String> createRegWriteDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, RegWrite.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = RegBatRead.class)
    public JAXBElement<String> createRegBatReadDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, RegBatRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectFpgaBatchAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fpgaBatchAccessObject", scope = RegBatRead.class)
    public JAXBElement<ObjectFpgaBatchAccess> createRegBatReadFpgaBatchAccessObject(ObjectFpgaBatchAccess value) {
        return new JAXBElement<ObjectFpgaBatchAccess>(_RegBatReadFpgaBatchAccessObject_QNAME, ObjectFpgaBatchAccess.class, RegBatRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectFpgaBatchAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fpgaBatchAccessObject", scope = RegBatReadResponse.class)
    public JAXBElement<ObjectFpgaBatchAccess> createRegBatReadResponseFpgaBatchAccessObject(ObjectFpgaBatchAccess value) {
        return new JAXBElement<ObjectFpgaBatchAccess>(_RegBatReadFpgaBatchAccessObject_QNAME, ObjectFpgaBatchAccess.class, RegBatReadResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = RegBatWrite.class)
    public JAXBElement<String> createRegBatWriteDeviceId(String value) {
        return new JAXBElement<String>(_RemoveDeviceDeviceId_QNAME, String.class, RegBatWrite.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectFpgaBatchAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fpgaBatchAccessObject", scope = RegBatWrite.class)
    public JAXBElement<ObjectFpgaBatchAccess> createRegBatWriteFpgaBatchAccessObject(ObjectFpgaBatchAccess value) {
        return new JAXBElement<ObjectFpgaBatchAccess>(_RegBatReadFpgaBatchAccessObject_QNAME, ObjectFpgaBatchAccess.class, RegBatWrite.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectFpgaBatchAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "fpgaBatchAccessObject", scope = RegBatWriteResponse.class)
    public JAXBElement<ObjectFpgaBatchAccess> createRegBatWriteResponseFpgaBatchAccessObject(ObjectFpgaBatchAccess value) {
        return new JAXBElement<ObjectFpgaBatchAccess>(_RegBatReadFpgaBatchAccessObject_QNAME, ObjectFpgaBatchAccess.class, RegBatWriteResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = SelfTest.class)
    public JAXBElement<ArrayOfstring> createSelfTestDeviceId(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_RemoveDeviceDeviceId_QNAME, ArrayOfstring.class, SelfTest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "filepath", scope = SelfTest.class)
    public JAXBElement<String> createSelfTestFilepath(String value) {
        return new JAXBElement<String>(_SelfTestFilepath_QNAME, String.class, SelfTest.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDeviceSlotInfo }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceSlotInfo", scope = InterconnTestStart.class)
    public JAXBElement<ArrayOfDeviceSlotInfo> createInterconnTestStartDeviceSlotInfo(ArrayOfDeviceSlotInfo value) {
        return new JAXBElement<ArrayOfDeviceSlotInfo>(_InterconnTestStartDeviceSlotInfo_QNAME, ArrayOfDeviceSlotInfo.class, InterconnTestStart.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "FilePath", scope = InterconnTestStart.class)
    public JAXBElement<String> createInterconnTestStartFilePath(String value) {
        return new JAXBElement<String>(_InterconnTestStartFilePath_QNAME, String.class, InterconnTestStart.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InterConnTestState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "CurrTestState", scope = InterconnGetProcess.class)
    public JAXBElement<InterConnTestState> createInterconnGetProcessCurrTestState(InterConnTestState value) {
        return new JAXBElement<InterConnTestState>(_InterconnGetProcessCurrTestState_QNAME, InterConnTestState.class, InterconnGetProcess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InterConnTestState }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "CurrTestState", scope = InterconnGetProcessResponse.class)
    public JAXBElement<InterConnTestState> createInterconnGetProcessResponseCurrTestState(InterConnTestState value) {
        return new JAXBElement<InterConnTestState>(_InterconnGetProcessCurrTestState_QNAME, InterConnTestState.class, InterconnGetProcessResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "InterConnStatus", scope = InterconnGetResult.class)
    public JAXBElement<ArrayOfint> createInterconnGetResultInterConnStatus(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_InterconnGetResultInterConnStatus_QNAME, ArrayOfint.class, InterconnGetResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ResultFilePath", scope = InterconnGetResult.class)
    public JAXBElement<String> createInterconnGetResultResultFilePath(String value) {
        return new JAXBElement<String>(_InterconnGetResultResultFilePath_QNAME, String.class, InterconnGetResult.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "InterConnStatus", scope = InterconnGetResultResponse.class)
    public JAXBElement<ArrayOfint> createInterconnGetResultResponseInterConnStatus(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_InterconnGetResultInterConnStatus_QNAME, ArrayOfint.class, InterconnGetResultResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ResultFilePath", scope = InterconnGetResultResponse.class)
    public JAXBElement<String> createInterconnGetResultResponseResultFilePath(String value) {
        return new JAXBElement<String>(_InterconnGetResultResultFilePath_QNAME, String.class, InterconnGetResultResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "DeviceId", scope = DeviceSlotInfo.class)
    public JAXBElement<String> createDeviceSlotInfoDeviceId(String value) {
        return new JAXBElement<String>(_DeviceSlotInfoDeviceId_QNAME, String.class, DeviceSlotInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "deviceId", scope = DeviceConnState.class)
    public JAXBElement<String> createDeviceConnStateDeviceId(String value) {
        return new JAXBElement<String>(_DeviceConnStateDeviceId_QNAME, String.class, DeviceConnState.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfObjectFpgaAccess }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "AccessObjects", scope = ObjectFpgaBatchAccess.class)
    public JAXBElement<ArrayOfObjectFpgaAccess> createObjectFpgaBatchAccessAccessObjects(ArrayOfObjectFpgaAccess value) {
        return new JAXBElement<ArrayOfObjectFpgaAccess>(_ObjectFpgaBatchAccessAccessObjects_QNAME, ArrayOfObjectFpgaAccess.class, ObjectFpgaBatchAccess.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "BinFilePath", scope = FpgaConfigObject.class)
    public JAXBElement<String> createFpgaConfigObjectBinFilePath(String value) {
        return new JAXBElement<String>(_FpgaConfigObjectBinFilePath_QNAME, String.class, FpgaConfigObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "DeviceId", scope = FpgaConfigObject.class)
    public JAXBElement<String> createFpgaConfigObjectDeviceId(String value) {
        return new JAXBElement<String>(_DeviceSlotInfoDeviceId_QNAME, String.class, FpgaConfigObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "DeviceId", scope = PllClockObject.class)
    public JAXBElement<String> createPllClockObjectDeviceId(String value) {
        return new JAXBElement<String>(_DeviceSlotInfoDeviceId_QNAME, String.class, PllClockObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfboolean }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "OutputClockEnables", scope = PllClockObject.class)
    public JAXBElement<ArrayOfboolean> createPllClockObjectOutputClockEnables(ArrayOfboolean value) {
        return new JAXBElement<ArrayOfboolean>(_PllClockObjectOutputClockEnables_QNAME, ArrayOfboolean.class, PllClockObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOffloat }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "OutputClockFrequencys", scope = PllClockObject.class)
    public JAXBElement<ArrayOffloat> createPllClockObjectOutputClockFrequencys(ArrayOffloat value) {
        return new JAXBElement<ArrayOffloat>(_PllClockObjectOutputClockFrequencys_QNAME, ArrayOffloat.class, PllClockObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "DeviceId", scope = FmcVoltageObject.class)
    public JAXBElement<String> createFmcVoltageObjectDeviceId(String value) {
        return new JAXBElement<String>(_DeviceSlotInfoDeviceId_QNAME, String.class, FmcVoltageObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfint }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "OnlineResult", scope = OnLineDeviceInfo.class)
    public JAXBElement<ArrayOfint> createOnLineDeviceInfoOnlineResult(ArrayOfint value) {
        return new JAXBElement<ArrayOfint>(_OnLineDeviceInfoOnlineResult_QNAME, ArrayOfint.class, OnLineDeviceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "deviceId", scope = DeviceInfo.class)
    public JAXBElement<String> createDeviceInfoDeviceId(String value) {
        return new JAXBElement<String>(_DeviceConnStateDeviceId_QNAME, String.class, DeviceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "deviceType", scope = DeviceInfo.class)
    public JAXBElement<String> createDeviceInfoDeviceType(String value) {
        return new JAXBElement<String>(_DeviceInfoDeviceType_QNAME, String.class, DeviceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "deviceUUID", scope = DeviceInfo.class)
    public JAXBElement<String> createDeviceInfoDeviceUUID(String value) {
        return new JAXBElement<String>(_DeviceInfoDeviceUUID_QNAME, String.class, DeviceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ownedBoardIds", scope = DeviceInfo.class)
    public JAXBElement<ArrayOfstring> createDeviceInfoOwnedBoardIds(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_DeviceInfoOwnedBoardIds_QNAME, ArrayOfstring.class, DeviceInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "belongDeviceSlotId", scope = BoardInfo.class)
    public JAXBElement<String> createBoardInfoBelongDeviceSlotId(String value) {
        return new JAXBElement<String>(_BoardInfoBelongDeviceSlotId_QNAME, String.class, BoardInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "boardId", scope = BoardInfo.class)
    public JAXBElement<String> createBoardInfoBoardId(String value) {
        return new JAXBElement<String>(_BoardInfoBoardId_QNAME, String.class, BoardInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "ipAddress", scope = BoardInfo.class)
    public JAXBElement<String> createBoardInfoIpAddress(String value) {
        return new JAXBElement<String>(_BoardInfoIpAddress_QNAME, String.class, BoardInfo.class, value);
    }

}
