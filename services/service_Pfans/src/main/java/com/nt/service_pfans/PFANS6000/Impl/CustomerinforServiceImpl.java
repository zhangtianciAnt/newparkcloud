package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.CustomerinforPrimary;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.service_pfans.PFANS6000.mapper.CustomerinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.CustomerinforPrimaryMapper;
import com.nt.utils.LogicalException;
//import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerinforServiceImpl implements CustomerinforService {

    @Autowired
    private CustomerinforMapper customerinforMapper;

    @Autowired
    private CustomerinforPrimaryMapper customerinforPrimaryMapper;

    @Override
    public List<Customerinfor> getcustomerinfor(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        List<Customerinfor> customerinforList = customerinforMapper.select(customerinfor);
        if (customerinforList.size() > 0) {
            //创建时间降序排列
            customerinforList = customerinforList.stream().sorted(Comparator.comparing(Customerinfor::getThedepC).reversed()).collect(Collectors.toList());
        }
        return customerinforList;
    }

    @Override
    public List<CustomerinforPrimary> getcustomerinforPrimary(CustomerinforPrimary customerinforprimary, TokenModel tokenModel) throws Exception {
        List<CustomerinforPrimary> customerinforPrimaryList = customerinforPrimaryMapper.select(customerinforprimary);
        if (customerinforPrimaryList.size() > 0) {
            //创建时间降序排列
            customerinforPrimaryList = customerinforPrimaryList.stream().sorted(Comparator.comparing(CustomerinforPrimary::getCreateon).reversed()).collect(Collectors.toList());
        }
        return customerinforPrimaryList;
    }

    @Override
    public List<Customerinfor> getcustomerinforApplyOne(Customerinfor customerinfor) throws Exception {
        List<Customerinfor> customerinforList = customerinforMapper.select(customerinfor);
        if (customerinforList.size() > 0) {
            //所属部门升序排列
            customerinforList = customerinforList.stream().sorted(Comparator.comparing(Customerinfor::getThedepC)).collect(Collectors.toList());
        }
        return customerinforList;
    }

    @Override
    public void updatecustomerinforApply(List<Customerinfor> customerinforList, TokenModel tokenModel) throws Exception {
        List<Customerinfor> customerinforListinsert = new ArrayList<>();
        List<Customerinfor> customerinforListupdate = new ArrayList<>();

        //更新主表
        CustomerinforPrimary customerinforPrimary = new CustomerinforPrimary();
        customerinforPrimary = customerinforPrimaryMapper.selectByPrimaryKey(customerinforList.get(0).getCustomerinforprimary_id());
        customerinforPrimary.setCustchinese(customerinforList.get(0).getCustchinese());
        customerinforPrimary.setCustenglish(customerinforList.get(0).getCustenglish());
        customerinforPrimary.setCustjapanese(customerinforList.get(0).getCustjapanese());
        customerinforPrimary.setAbbreviation(customerinforList.get(0).getAbbreviation());
        customerinforPrimary.setLiableperson(customerinforList.get(0).getLiableperson());
        customerinforPrimary.setThecompany(customerinforList.get(0).getThecompany());
        customerinforPrimary.setCausecode(customerinforList.get(0).getCausecode());
        customerinforPrimary.setRegindiff(customerinforList.get(0).getRegindiff());
        customerinforPrimary.preUpdate(tokenModel);
        customerinforPrimaryMapper.updateByPrimaryKey(customerinforPrimary);

        for(Customerinfor c : customerinforList)
        {
            //存在主键，说明是修改
            //不存在主键，说明是插入
            if(!StringUtils.isNullOrEmpty(c.getCustomerinfor_id()))
            {
                c.preUpdate(tokenModel);
                customerinforListupdate.add(c);
            }
            else
            {
                c.preInsert(tokenModel);
                c.setCustomerinfor_id(UUID.randomUUID().toString());
                customerinforListinsert.add(c);
            }
        }
        if(customerinforListinsert.size() > 0)
        {
            customerinforMapper.insertListAllCols(customerinforListinsert);
        }
        if(customerinforListupdate.size() > 0)
        {
            customerinforMapper.updateCustAll(customerinforListupdate);
        }
    }

    @Override
    public void createcustomerinforApply(List<Customerinfor> customerinforList, TokenModel tokenModel) throws Exception {

        //数据查重检索，条件基本信息的8个字段
        CustomerinforPrimary customerinforPrimary = new CustomerinforPrimary();
        customerinforPrimary.setCustchinese(customerinforList.get(0).getCustchinese());
        customerinforPrimary.setCustenglish(customerinforList.get(0).getCustenglish());
        customerinforPrimary.setCustjapanese(customerinforList.get(0).getCustjapanese());
        customerinforPrimary.setAbbreviation(customerinforList.get(0).getAbbreviation());
        customerinforPrimary.setLiableperson(customerinforList.get(0).getLiableperson());
        customerinforPrimary.setThecompany(customerinforList.get(0).getThecompany());
        customerinforPrimary.setCausecode(customerinforList.get(0).getCausecode());
        customerinforPrimary.setRegindiff(customerinforList.get(0).getRegindiff());
        List<CustomerinforPrimary> customerinforPrimaryList = customerinforPrimaryMapper.select(customerinforPrimary);
        if(customerinforPrimaryList.size()>0)
        {
            throw new LogicalException("当前公司已经存在，请重新输入！");
        }
        else
        {
            //数据不存在时，主表插入数据
            customerinforPrimary.setCustomerinforprimary_id(UUID.randomUUID().toString());
            customerinforPrimary.preInsert(tokenModel);
            customerinforPrimaryMapper.insert(customerinforPrimary);

            //明细表插入数据
            List<Customerinfor> customerinforListinsert = new ArrayList<>();
            for(Customerinfor c : customerinforList)
            {
                c.setCustomerinfor_id(UUID.randomUUID().toString());
                c.setCustomerinforprimary_id(customerinforPrimary.getCustomerinforprimary_id());
                c.preInsert(tokenModel);
            }
            customerinforMapper.insertListAllCols(customerinforList);
        }
    }


    //导入导出
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception {

//            创建listVo集合方便存储导入信息
            List<Customerinfor> listVo = new ArrayList<Customerinfor>();
//            创建Result结果集的集合
            List<String> Result = new ArrayList<String>();
//            用来接收前台传过来的文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            创建对象f，且为空
            File f = null;
//            创建临时文件
            f = File.createTempFile("temp", null);
//            上传文件
            file.transferTo(f);
//            使用Excel读文件
            ExcelReader reader = ExcelUtil.getReader(f);
//            创建集合存入读的文件
            List<List<Object>> list = reader.read();
//            创建集合存入标准模板
            List<Object> model = new ArrayList<Object>();
//            标准模板
            model.add("客户名称(中文)");
            model.add("客户名称(日文)");
            model.add("客户名称(英文)");
            model.add("简称");
            model.add("负责人");
            model.add("项目联络人(中文)");
            model.add("项目联络人(日文)");
            model.add("项目联络人(英文)");
            model.add("联系电话");
            model.add("邮箱地址");
            model.add("共通事务联络人");
            model.add("联系电话");
            model.add("邮箱地址");
            model.add("地址(中文)");
            model.add("地址(日文)");
            model.add("地址(英文)");
            model.add("人员规模");
            model.add("所属公司");
            model.add("事业场编码");
            model.add("网址");
            model.add("备注");
            List<Object> key = list.get(0);
//           上传模板与标准模板 校验
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Customerinfor customerinfor = new Customerinfor();
                List<Object> value = list.get(k);
                k++;

                if (value != null && !value.isEmpty()) {
                    if(value.size()>0)
                    {
                        customerinfor.setCustchinese(Convert.toStr(value.get(0)));
                        if(customerinfor.getCustchinese() != null && customerinfor.getCustchinese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 客户名称(中文) 长度超长，最大长度为255");
                        }
                        if(customerinfor.getCustchinese() != null && !customerinfor.getCustchinese().isEmpty())
                        {
                            Customerinfor customer = new Customerinfor();
                            customer.setCustchinese(customerinfor.getCustchinese().trim());
                            List<Customerinfor> customerList = customerinforMapper.select(customer);
                            if(customerList.size()>0)
                            {
                                throw new LogicalException("第" + i + "行 客户名称(中文) 已经存在，请确认。");
                            }
                        }
                    }
                    if(value.size()>1)
                    {
                        customerinfor.setCustjapanese(Convert.toStr(value.get(1)));
                        if(customerinfor.getCustjapanese()!= null && customerinfor.getCustjapanese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 客户名称(日文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>2)
                    {
                        customerinfor.setCustenglish(Convert.toStr(value.get(2)));
                        if(customerinfor.getCustenglish() !=null && customerinfor.getCustenglish().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 客户名称(英文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>3)
                    {
                        customerinfor.setAbbreviation(Convert.toStr(value.get(3)));
                        if(customerinfor.getAbbreviation()!=null && customerinfor.getAbbreviation().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 简称 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>4)
                    {
                        customerinfor.setLiableperson(Convert.toStr(value.get(4)));
                        if(customerinfor.getLiableperson() !=null && customerinfor.getLiableperson().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 负责人 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>5)
                    {
                        customerinfor.setProchinese(Convert.toStr(value.get(5)));
                        if(customerinfor.getProchinese() !=null && customerinfor.getProchinese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(中文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>6)
                    {
                        customerinfor.setProjapanese(Convert.toStr(value.get(6)));
                        if(customerinfor.getProjapanese()!=null && customerinfor.getProjapanese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(日文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>7)
                    {
                        customerinfor.setProenglish(Convert.toStr(value.get(7)));
                        if(customerinfor.getProenglish() !=null && customerinfor.getProenglish().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(英文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>8)
                    {
                        customerinfor.setProtelephone(Convert.toStr(value.get(8)));
                        if(customerinfor.getProtelephone() !=null && customerinfor.getProtelephone().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 联系电话 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>9)
                    {
                        customerinfor.setProtemail(Convert.toStr(value.get(9)));
                        if(customerinfor.getProtemail() !=null && customerinfor.getProtemail().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 邮箱地址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>10)
                    {
                        customerinfor.setCommontperson(Convert.toStr(value.get(10)));
                        if(customerinfor.getCommontperson()!=null && customerinfor.getCommontperson().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 共通事务联络人 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>11)
                    {
                        customerinfor.setComtelephone(Convert.toStr(value.get(11)));
                        if(customerinfor.getComtelephone()!=null && customerinfor.getComtelephone().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 联系电话 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>12)
                    {
                        customerinfor.setComnemail(Convert.toStr(value.get(12)));
                        if(customerinfor.getComnemail()!=null && customerinfor.getComnemail().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 邮箱地址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>13)
                    {
                        customerinfor.setAddchinese(Convert.toStr(value.get(13)));
                        if(customerinfor.getAddchinese()!=null && customerinfor.getAddchinese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 地址(中文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>14)
                    {
                        customerinfor.setAddjapanese(Convert.toStr(value.get(14)));
                        if(customerinfor.getAddjapanese()!=null && customerinfor.getAddjapanese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 地址(日文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>15)
                    {
                        customerinfor.setAddenglish(Convert.toStr(value.get(15)));
                        if(customerinfor.getAddenglish()!=null && customerinfor.getAddenglish().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 地址(英文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>17)
                    {
                        customerinfor.setThecompany(Convert.toStr(value.get(17)));
                        if(customerinfor.getThecompany()!=null && customerinfor.getThecompany().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 所属公司 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>18)
                    {
                        customerinfor.setCausecode(Convert.toStr(value.get(18)));
                        if(customerinfor.getCausecode()!=null && customerinfor.getCausecode().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 事业场编码 长度超长，最大长度为20");
                        }
                        if(customerinfor.getCustchinese() != null && !customerinfor.getCustchinese().isEmpty())
                        {
                            Customerinfor cust = new Customerinfor();
                            cust.setCausecode(customerinfor.getCausecode().trim());
                            List<Customerinfor> custList = customerinforMapper.select(cust);
                            if(custList.size()>0)
                            {
                                throw new LogicalException("第" + i + "行 事业场编码 已经存在，请确认。");
                            }
                        }
                    }
                    if(value.size()>19)
                    {
                        customerinfor.setWebsite(Convert.toStr(value.get(19)));
                        if(customerinfor.getWebsite()!=null && customerinfor.getWebsite().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 网址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>20)
                    {
                        customerinfor.setRemarks(Convert.toStr(value.get(20)));
                    }
                    if(value.size()>16)
                    {
                        String person=Convert.toStr(value.get(16));
                        if(StrUtil.isNotBlank(person)){
                            person = person.trim().replace("<","").replace(">","").replace("≥","").replace("=","").replace("≤","").replace("-","");
                            if(Integer.parseInt(person)>0 && Integer.parseInt(person)<50){
                                customerinfor.setPerscale("BP007001");  //改数据
                            }
                            if(Integer.parseInt(person)>=50 && Integer.parseInt(person)<100){
                                customerinfor.setPerscale("BP007002");  //改数据
                            } if(Integer.parseInt(person)>=100 && Integer.parseInt(person)<500){
                                customerinfor.setPerscale("BP007003");  //改数据
                            }
                            if(Integer.parseInt(person)>=500){
                                customerinfor.setPerscale("BP007004");  //改数据
                            }
                        }
                    }
                }
                customerinfor.preInsert(tokenModel);
                customerinfor.setCustomerinfor_id(UUID.randomUUID().toString());
                customerinforMapper.insert(customerinfor);
                listVo.add(customerinfor);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;

    }


    //region scc add 人员信息导出 from
    @Override
    public void downloadExcel(List<String> ids, HttpServletRequest request, HttpServletResponse resp) throws LogicalException {
        InputStream in = null;
        //集合判空
        try {
            //表格操作
            in = getClass().getClassLoader().getResourceAsStream("jxls_templates/kehuxinxi.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            this.getReportWork1(workbook.getSheetAt(0), ids);
            OutputStream os = resp.getOutputStream();// 取得输出流
            String fileName = "客户信息";
            resp.setContentType("application/vnd.ms-excel;charset=utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename="
                    + new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
            workbook.write(os);
            workbook.close();
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    private void getReportWork1(XSSFSheet sheet1, List<String> ids) throws LogicalException {
        try {
            if (ids != null && ids.size() > 0) {
                List<Customerinfor> customerinforList = customerinforMapper.export(ids);
                //将数据放入Excel
                if (customerinforList != null && customerinforList.size() > 0) {
                    int i = 0;
                    for (Customerinfor c : customerinforList) {
                        //创建工作表的行
                        XSSFRow row = sheet1.createRow(i + 1);
                        for (int j = 0; j < 23; j++) {
                            switch (j) {
                                case 0: row.createCell(j).setCellValue(c.getCustchinese());break;
                                case 1: row.createCell(j).setCellValue(c.getCustjapanese());break;
                                case 2: row.createCell(j).setCellValue(c.getCustenglish());break;
                                case 3: row.createCell(j).setCellValue(c.getAbbreviation());break;
                                case 4: row.createCell(j).setCellValue(c.getLiableperson());break;
                                case 5: row.createCell(j).setCellValue(c.getThecompany());break;
                                case 6: row.createCell(j).setCellValue(c.getCausecode());break;
                                case 7: row.createCell(j).setCellValue(c.getRegindiff());break;
                                case 8: row.createCell(j).setCellValue(c.getThedepC());break;
                                case 9: row.createCell(j).setCellValue(c.getThedepJ());break;
                                case 10: row.createCell(j).setCellValue(c.getThedepE());break;
                                case 11: row.createCell(j).setCellValue(c.getProchinese());break;
                                case 12: row.createCell(j).setCellValue(c.getProjapanese());break;
                                case 13: row.createCell(j).setCellValue(c.getProenglish());break;
                                case 14: row.createCell(j).setCellValue(c.getProtelephone());break;
                                case 15: row.createCell(j).setCellValue(c.getProtemail());break;
                                case 16: row.createCell(j).setCellValue(c.getAddchinese());break;
                                case 17: row.createCell(j).setCellValue(c.getAddjapanese());break;
                                case 18: row.createCell(j).setCellValue(c.getAddenglish());break;
                                case 19: row.createCell(j).setCellValue(c.getPerscale());break;
                                case 20: row.createCell(j).setCellValue(c.getWebsite());break;
                                case 21: row.createCell(j).setCellValue(c.getRemarks());break;
                            }
                        }
                        i += 1;
                    }
                }
            }
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
    //endregion scc add 人员信息导出 to

}
