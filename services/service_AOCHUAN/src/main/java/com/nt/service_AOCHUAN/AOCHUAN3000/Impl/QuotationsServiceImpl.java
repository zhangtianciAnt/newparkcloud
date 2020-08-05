package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.aliyuncs.utils.IOUtils;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Enquiry;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Quotations;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.QuoAndEnq;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.ToDoNotice;
import com.nt.service_AOCHUAN.AOCHUAN3000.Impl.xls.MyXLSTransformer;
import com.nt.service_AOCHUAN.AOCHUAN3000.QuotationsService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ApplicationrecordMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.EnquiryMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.QuotationsMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.ReturngoodsMapper;
import com.nt.service_AOCHUAN.AOCHUAN8000.Impl.ContractNumber;
import com.nt.service_AOCHUAN.AOCHUAN8000.mapper.NumberMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_Org.mapper.TodoNoticeMapper;
import com.nt.utils.*;
import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.extern.log4j.Log4j;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Log4j
public class QuotationsServiceImpl implements QuotationsService {

    @Autowired
    private QuotationsMapper quotationsMapper;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EnquiryMapper enquiryMapper;

    @Autowired
    private ContractNumber contractNumber;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    /**session操作类*/
    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Autowired
    TodoNoticeMapper todoNoticeMapper;

    @Autowired
    private ApplicationrecordMapper applicationrecordMapper;


    @Override
    public List<Quotations> get(Quotations quotations) throws Exception {
        return quotationsMapper.select(quotations);
    }

    @Override
    public Quotations getOne(String id) throws Exception {
        Quotations quotations = new Quotations();
        Enquiry enquiry = new Enquiry();
        enquiry.setQuotations_id(id);
        quotations = quotationsMapper.selectByPrimaryKey(id);
        List<Enquiry> enquiries = enquiryMapper.select(enquiry);
        for (Enquiry _enquiry:
        enquiries) {
         String[] document = _enquiry.getDoc().length() > 0 ? _enquiry.getDoc().split(","):new String[0];
         _enquiry.setDocument(document);
        }
        quotations.setEnquiry(enquiries);
        return quotations;
    }

    @Override
    public List<QuoAndEnq> getForSupplier(String id) throws Exception {
        return quotationsMapper.getForSupplier(id);
    }

    @Override
    public List<Quotations> getForCustomer(String id) throws Exception {
        return quotationsMapper.getForCustomer(id);
    }

    @Override
    public void update(Quotations quotations, TokenModel tokenModel) throws Exception {
              quotations.preUpdate(tokenModel);
              if(quotations.isNotice()){
                   ToDoNotice(tokenModel,quotations);
                   quotations.setType(quotations.getType() + 1);
               }
              quotations.preUpdate(tokenModel);
              quotationsMapper.updateByPrimaryKeySelective(quotations);
        insertEnquiry(quotations.getEnquiry(),quotations.getQuotations_id());
        //ToDoNotice(tokenModel,quotations.getQuotations_id());
    }

    @Override
    public void insert(Quotations quotations, TokenModel tokenModel) throws Exception {
        String number = contractNumber.getContractNumber("PT001001","quotations");
        quotations.setQuotationsno(number);
        quotations.setQuotations_id(UUID.randomUUID().toString());
        quotations.preInsert(tokenModel);
        if(quotations.isNotice()){
            ToDoNotice(tokenModel,quotations);
            quotations.setType(1);
        }
        quotationsMapper.insert(quotations);
        insertEnquiry(quotations.getEnquiry(),quotations.getQuotations_id());
    }

    @Override
    public void delete(String id) throws Exception {
        quotationsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void setExport(HttpServletResponse response, List<Quotations> quotationsList) throws Exception {
        Map<String, Object> beans = new HashMap();

        //业务逻辑
        beans = logicExport(response, quotationsList);
        boolean delete = false;

        //加载excel模板文件
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:excel/quote.xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            try {
                ClassPathResource cpr = new ClassPathResource("excel/quote.xlsx");
                if (cpr.exists()) {
                    InputStream inputStream = cpr.getInputStream();
                    Date now = new Date();
                    file = File.createTempFile(now.getTime() + "_quote", ".xlsx");
                    try {
                        byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
                        FileCopyUtils.copy(bdata, file);
                    } finally {
                        IOUtils.closeQuietly(inputStream);
                    }
                }
                delete = true;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        //配置下载路径

//        String path = "/download/";
//        createDir(new File(path));
        //根据模板生成新的excel
        File excelFile = createNewFile(beans, file);

        //浏览器端下载文件
        downloadFile(response, excelFile);

        //删除服务器生成文件
        deleteFile(excelFile);
        if ( delete ) {
            deleteFile(file);
        }

    }


    /**
     * 业务逻辑把数据存到Map中
     *
     */
    private Map<String, Object> logicExport(HttpServletResponse response, List<Quotations> quotationsList) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for(Quotations quotations : quotationsList){

            String date = simpleDateFormat.format(quotations.getInquirydate());
            quotations.setXjdate(date);

            String producttype = applicationrecordMapper.dictionaryExportList(quotations.getProducttype());
            quotations.setProducttype(producttype);

            String purpose = applicationrecordMapper.dictionaryExportList(quotations.getPurpose());
            quotations.setPurpose(purpose);

        }

        Map<String, Object> beans = new HashMap();
        List<Quotations> xiaoshou = quotationsList;
        List<Quotations> caigou = quotationsList;

        beans.put("xiaoshou", xiaoshou);
        beans.put("caigou", caigou);


        return beans;
    }

    //如果目录不存在创建目录 存在则不创建
    private void createDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 根据excel模板生成新的excel
     *
     * @param beans
     * @param sourceFile
     * @return
     **/
    private File createNewFile(Map<String, Object> beans, File sourceFile) throws IOException {
        XLSTransformer transformer = new MyXLSTransformer();
		File tempFile = File.createTempFile("D3001", ".xlsx");

        try (InputStream in = new BufferedInputStream(new FileInputStream(sourceFile));
             OutputStream out = new FileOutputStream(tempFile)) {
            //poi版本使用3.1.7要不然会报错
			// poi 4.x 对应
            Workbook workbook = transformer.transformXLS(in, beans);
            workbook.write(out);
            out.flush();
            return tempFile;
        } catch (Exception e) {
        	log.error("error with download 3001 Excel file.", e);
            System.out.println(e.getMessage());
        }
        return tempFile;
    }

    /**
     * 将服务器新生成的excel从浏览器下载
     *
     * @param response
     * @param excelFile
     */
    private void downloadFile(HttpServletResponse response, File excelFile) {
        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
        response.setContentType("multipart/form-data");
        /* 设置文件头：最后一个参数是设置下载文件名 */
        response.setHeader("Content-Disposition", "attachment;filename=" + excelFile.getName());
        try (
                InputStream ins = new FileInputStream(excelFile);
                OutputStream os = response.getOutputStream()
        ) {
            byte[] b = new byte[1024];
            int len;
            while ((len = ins.read(b)) > 0) {
                os.write(b, 0, len);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 浏览器下载完成之后删除服务器生成的文件
     * 也可以设置定时任务去删除服务器文件
     *
     * @param excelFile
     */
    private void deleteFile(File excelFile) {

        excelFile.delete();
    }

    //生成代办
    @Async
    public void ToDoNotice(TokenModel tokenModel,Quotations quotations) throws Exception{
        // 创建代办
        if(quotations.getType() == 0){
            List<MembersVo> membersVos =  roleService.getMembers("5eba6f09e52fa718db632696");
            for (MembersVo membersVo:
            membersVos) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("【采购询价】：您有一条询价需要处理。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("询报价编号【" +quotations.getQuotationsno()+"】");
                toDoNotice.setDataid(quotations.getQuotations_id());
                toDoNotice.setUrl("/AOCHUAN3001FormView");
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setOwner(membersVo.getUserid());
                toDoNoticeService.save(toDoNotice);
                toDoNoticeService.updateNoticesStatus(toDoNotice);
            }
        }else if(quotations.getType() == 1){
            ToDoNotice toDoNotice = new ToDoNotice();
            toDoNotice.setTitle("【销售报价】：您有一条询价需要处理。");
            toDoNotice.setInitiator(tokenModel.getUserId());
            toDoNotice.setContent("询报价编号【" +quotations.getQuotationsno()+"】");
            toDoNotice.setDataid(quotations.getQuotations_id());
            toDoNotice.setUrl("/AOCHUAN3001FormView");
            toDoNotice.preInsert(tokenModel);
            toDoNotice.setOwner(quotations.getSaleresponsibility());
            toDoNoticeService.save(toDoNotice);
        }
    }

    private void insertEnquiry(List<Enquiry> enquiryList,String quotationsId){
        Enquiry enquiry = new Enquiry();
        enquiry.setQuotations_id(quotationsId);
        enquiryMapper.delete(enquiry);
         if(enquiryList!= null && enquiryList.size() > 0){
             for (Enquiry _enquiry:
             enquiryList) {
                 _enquiry.setEnquiry_id(UUID.randomUUID().toString());
                 _enquiry.setQuotations_id(quotationsId);
             String document = _enquiry.getDocument().length > 0 ? StringUtils.join(_enquiry.getDocument()) : "";
             _enquiry.setDoc(document);
             }
             enquiryMapper.insertEnquiryList(enquiryList);
         }
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
