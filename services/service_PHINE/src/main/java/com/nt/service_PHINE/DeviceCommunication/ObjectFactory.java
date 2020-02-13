
package com.nt.service_PHINE.DeviceCommunication;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the HardwareDeviceService package.
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

    private final static QName _ArrayOfboolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfboolean");
    private final static QName _ArrayOffloat_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOffloat");
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
    private final static QName _ArrayOfDeviceConnState_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ArrayOfDeviceConnState");
    private final static QName _DeviceConnState_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "DeviceConnState");
    private final static QName _ConnectionResult_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "ConnectionResult");
    private final static QName _FmcVoltageObject_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "FmcVoltageObject");
    private final static QName _PllClockObject_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "PllClockObject");
    private final static QName _GetCurrentConnStatusDeviceConnStates_QNAME = new QName("http://tempuri.org/", "deviceConnStates");
    private final static QName _OpenConnectionDeviceId_QNAME = new QName("http://tempuri.org/", "deviceId");
    private final static QName _SetFmcVoltageFmcVoltageObject_QNAME = new QName("http://tempuri.org/", "fmcVoltageObject");
    private final static QName _SetFmcVoltageByFileConfigFilePath_QNAME = new QName("http://tempuri.org/", "configFilePath");
    private final static QName _SetPllClockPllClockObject_QNAME = new QName("http://tempuri.org/", "pllClockObject");
    private final static QName _DeviceConnStateDeviceId_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "deviceId");
    private final static QName _PllClockObjectEnablesOutputClock_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "Enables_OutputClock");
    private final static QName _PllClockObjectFrequencysOutputClock_QNAME = new QName("http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", "Frequencys_OutputClock");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: HardwareDeviceService
     *
     */
    public ObjectFactory() {
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
     * Create an instance of {@link ConfigFpga }
     *
     */
    public ConfigFpga createConfigFpga() {
        return new ConfigFpga();
    }

    /**
     * Create an instance of {@link ConfigFpgaResponse }
     *
     */
    public ConfigFpgaResponse createConfigFpgaResponse() {
        return new ConfigFpgaResponse();
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
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, OpenConnection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = CloseConnection.class)
    public JAXBElement<String> createCloseConnectionDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, CloseConnection.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = SetFmcVoltage.class)
    public JAXBElement<String> createSetFmcVoltageDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, SetFmcVoltage.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = GetFmcVoltage.class)
    public JAXBElement<String> createGetFmcVoltageDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, GetFmcVoltage.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = SetFmcVoltageByFile.class)
    public JAXBElement<String> createSetFmcVoltageByFileDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, SetFmcVoltageByFile.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = SetPllClock.class)
    public JAXBElement<String> createSetPllClockDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, SetPllClock.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = GetPllClock.class)
    public JAXBElement<String> createGetPllClockDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, GetPllClock.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = SetPllClockByFile.class)
    public JAXBElement<String> createSetPllClockByFileDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, SetPllClockByFile.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = ConfigFpga.class)
    public JAXBElement<String> createConfigFpgaDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, ConfigFpga.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "configFilePath", scope = ConfigFpga.class)
    public JAXBElement<String> createConfigFpgaConfigFilePath(String value) {
        return new JAXBElement<String>(_SetFmcVoltageByFileConfigFilePath_QNAME, String.class, ConfigFpga.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = ReConfigFpga.class)
    public JAXBElement<String> createReConfigFpgaDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, ReConfigFpga.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = RegRead.class)
    public JAXBElement<String> createRegReadDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, RegRead.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "deviceId", scope = RegWrite.class)
    public JAXBElement<String> createRegWriteDeviceId(String value) {
        return new JAXBElement<String>(_OpenConnectionDeviceId_QNAME, String.class, RegWrite.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfboolean }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "Enables_OutputClock", scope = PllClockObject.class)
    public JAXBElement<ArrayOfboolean> createPllClockObjectEnablesOutputClock(ArrayOfboolean value) {
        return new JAXBElement<ArrayOfboolean>(_PllClockObjectEnablesOutputClock_QNAME, ArrayOfboolean.class, PllClockObject.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOffloat }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/WcfServiceLib_VerityPlatform", name = "Frequencys_OutputClock", scope = PllClockObject.class)
    public JAXBElement<ArrayOffloat> createPllClockObjectFrequencysOutputClock(ArrayOffloat value) {
        return new JAXBElement<ArrayOffloat>(_PllClockObjectFrequencysOutputClock_QNAME, ArrayOffloat.class, PllClockObject.class, value);
    }

}
