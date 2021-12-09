package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Pfans.PFANS1000.Contractapplication;
import com.nt.dao_Pfans.PFANS6000.CompanyStatistics;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.Award;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.CustomerinforPrimary;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.service_pfans.PFANS6000.mapper.CustomerinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.CustomerinforPrimaryMapper;
import com.nt.utils.LogicalException;
//import com.nt.utils.StringUtils;
import com.nt.utils.PageUtil;
import com.nt.utils.dao.TableDataInfo;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerinforServiceImpl implements CustomerinforService {

    @Autowired
    private CustomerinforMapper customerinforMapper;

    @Autowired
    private CustomerinforPrimaryMapper customerinforPrimaryMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Override
    public List<Customerinfor> getcustomerinfor(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        List<Customerinfor> customerinforList = customerinforMapper.select(customerinfor);
//        if (customerinforList.size() > 0) {
//            //创建时间降序排列
//            customerinforList = customerinforList.stream().sorted(Comparator.comparing(Customerinfor::getThedepC).reversed()).collect(Collectors.toList());
//        }
        return customerinforList;
    }

    // add  ml  211206  dialog分页  from
    @Override
    public TableDataInfo getCustomerinfor(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Customerinfor customerinfor = new Customerinfor();
        List<Customerinfor> customerinforList = customerinforMapper.select(customerinfor);
//        if (customerinforList.size() > 0) {
//            //创建时间降序排列
//            customerinforList = customerinforList.stream().sorted(Comparator.comparing(Customerinfor::getThedepC).reversed()).collect(Collectors.toList());
//        }
        Page<Customerinfor> pageFromList = PageUtil.createPageFromList(customerinforList, pageable);
        TableDataInfo taInfo = new TableDataInfo();
        taInfo.setTotal(pageFromList.getTotalElements() > customerinforList.size() ? customerinforList.size() : pageFromList.getTotalElements());
        taInfo.setResultList(pageFromList.getContent());
        return taInfo;
    }
    // add  ml  211206  dialog分页  to

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
//            customerinforList = customerinforList.stream().sorted(Comparator.comparing(Customerinfor::getThedepC)).collect(Collectors.toList());
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


    // region scc upd 21/12/3 导入 from
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception {

//            创建更新子表数据集合
            List<Customerinfor> details = new ArrayList<Customerinfor>();

            //创建更新主表数据集合
            List<CustomerinforPrimary> mainTable = new ArrayList<>();

            List<Customerinfor> flag = new ArrayList<Customerinfor>();

            List<CustomerinforPrimary> flag2 = new ArrayList<>();

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
            model.add("客户名称(中)");
            model.add("客户名称(日)");
            model.add("客户名称(英)");
            model.add("简称");
            model.add("公司法人");
            model.add("所属公司");
            model.add("事业场编码");
            model.add("地域区分");
            model.add("所属部门(中)");
            model.add("所属部门(日)");
            model.add("所属部门(英)");
            model.add("联络人(中)");
            model.add("联络人(日)");
            model.add("联络人(英)");
            model.add("联系电话");
            model.add("电子邮箱");
            model.add("地址(中)");
            model.add("地址(日)");
            model.add("地址(英)");
            model.add("人员规模");
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
            CustomerinforPrimary customerinforPrimary = null;
            HashMap<CustomerinforPrimary, String> k_v = new HashMap<>();//存放对象和关联主键，用于去重
            ArrayList<String> regional = new ArrayList<>();
            List<Dictionary> diclist1 = dictionaryService.getForSelect("BP028");//地域区分
            Map<String, String> result1 = new HashMap<>();
            for (Dictionary d : diclist1) {
                result1.put(d.getValue1(), d.getCode());
                regional.add(d.getValue1());
            }
            for (int i = 1; i < list.size(); i++) {
                Customerinfor customerinfor = new Customerinfor();
                customerinforPrimary = new CustomerinforPrimary();
                List<Object> value = list.get(k);
                k++;

                if (value != null && !value.isEmpty()) {
                    if (value.size() > 0) {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(0)))) {
                            if (Convert.toStr(value.get(0)).length() > 255) {
                                throw new LogicalException("第" + i + "行 客户名称(中) 长度超长，最大长度为255");
                            } else {
                                customerinfor.setCustchinese(Convert.toStr(value.get(0)));
                                customerinforPrimary.setCustchinese(Convert.toStr(value.get(0)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 客户名称(中) 不能为空");
                        }
                    }
                    if (value.size() > 1) {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(1)))) {
                            if (Convert.toStr(value.get(1)).length() > 255) {
                                throw new LogicalException("第" + i + "行 客户名称(日文) 长度超长，最大长度为255");
                            } else {
                                customerinfor.setCustjapanese(Convert.toStr(value.get(1)));
                                customerinforPrimary.setCustjapanese(Convert.toStr(value.get(1)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 客户名称(日) 不能为空");
                        }
                    }
                    if(value.size()>2)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(2)))) {
                            if (Convert.toStr(value.get(2)).length() > 255) {
                                throw new LogicalException("第" + i + "行 客户名称(英) 长度超长，最大长度为255");
                            } else {
                                customerinfor.setCustenglish(Convert.toStr(value.get(2)));
                                customerinforPrimary.setCustenglish(Convert.toStr(value.get(2)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 客户名称(英) 不能为空");
                        }
                    }
                    if(value.size()>3)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(3)))) {
                            if (Convert.toStr(value.get(3)).length() > 255) {
                                throw new LogicalException("第" + i + "行 简称 长度超长，最大长度为255");
                            } else {
                                customerinfor.setAbbreviation(Convert.toStr(value.get(3)));
                                customerinforPrimary.setAbbreviation(Convert.toStr(value.get(3)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 简称 不能为空");
                        }
                    }
                    if(value.size()>4)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(4)))) {
                            if (Convert.toStr(value.get(4)).length() > 255) {
                                throw new LogicalException("第" + i + "行 公司法人 长度超长，最大长度为255");
                            } else {
                                customerinfor.setLiableperson(Convert.toStr(value.get(4)));
                                customerinforPrimary.setLiableperson(Convert.toStr(value.get(4)));
                            }
                        }
                        else {
                            throw new LogicalException("第" + i + "行 公司法人 不能为空");
                        }
                    }
                    if(value.size()>5)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(5)))) {
                            if (Convert.toStr(value.get(5)).length() > 255) {
                                throw new LogicalException("第" + i + "行 所属公司 长度超长，最大长度为255");
                            } else {
                                customerinfor.setThecompany(Convert.toStr(value.get(5)));
                                customerinforPrimary.setThecompany(Convert.toStr(value.get(5)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 所属公司 不能为空");
                        }
                    }
                    if(value.size()>6)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(6)))) {
                            if (Convert.toStr(value.get(6)).length() > 50) {
                                throw new LogicalException("第" + i + "行 事业场编码 长度超长，最大长度为50");
                            } else {
                                customerinfor.setCausecode(Convert.toStr(value.get(6)));
                                customerinforPrimary.setCausecode(Convert.toStr(value.get(6)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 事业场编码 不能为空");
                        }
                    }
                    if(value.size()>7)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(7)))) {
                            if (Convert.toStr(value.get(7)).length() > 255) {
                                throw new LogicalException("第" + i + "行 地域区分 长度超长，最大长度为255");
                            } else {
                                if(!regional.contains(Convert.toStr(value.get(7)).trim())){
                                    throw new LogicalException("第" + i + "行 地域区分是否输入正确，应为“境内”或“境外-日本”或“境外-日本以外”");
                                }
                                    customerinfor.setRegindiff(result1.get(Convert.toStr(value.get(7)).trim()));
                                    customerinforPrimary.setRegindiff(result1.get(Convert.toStr(value.get(7)).trim()));
                                    List<CustomerinforPrimary> res = customerinforPrimaryMapper.select(customerinforPrimary);
                                    if(res.size() > 0){
                                        throw new LogicalException("第" + i + "行 对应客户信息已存在");
                                    }
//                                    customerinforPrimary.setLiableperson(Convert.toStr(value.get(4)));//法人，不参与check,得需要更新主表数据
//                                    customerinforPrimary.setRegindiff(result1.get(Convert.toStr(value.get(7)).trim()));//地域区分，不参与check,得需要更新主表数据
                            }
                        }
                        else {
                            throw new LogicalException("第" + i + "行 地域区分 不能为空");
                        }
                    }
                    if(value.size()>8)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(8)))){
                            if (Convert.toStr(value.get(7)).length() > 255) {
                                throw new LogicalException("第" + i + "行 所属部门(中) 长度超长，最大长度为255");
                            }else{
                                customerinfor.setThedepC(Convert.toStr(value.get(8)));
                            }
                        }
                    }
                    if(value.size()>9)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(9)))){
                            if (Convert.toStr(value.get(9)).length() > 255) {
                                throw new LogicalException("第" + i + "行 所属部门(日) 长度超长，最大长度为255");
                            }else{
                                customerinfor.setThedepJ(Convert.toStr(value.get(9)));
                            }
                        }
                    }
                    if(value.size()>10)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(10)))){
                            if (Convert.toStr(value.get(10)).length() > 255) {
                                throw new LogicalException("第" + i + "行 所属部门(英) 长度超长，最大长度为255");
                            }else{
                                customerinfor.setThedepE(Convert.toStr(value.get(10)));
                            }
                        }
                    }
                    if(value.size()>11)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(11)))){
                            if (Convert.toStr(value.get(11)).length() > 255) {
                                throw new LogicalException("第" + i + "行 联络人(中) 长度超长，最大长度为255");
                            }else{
                                customerinfor.setProchinese(Convert.toStr(value.get(11)));
                            }
                        }
                    }
                    if(value.size()>12)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(12)))){
                            if (Convert.toStr(value.get(12)).length() > 255) {
                                throw new LogicalException("第" + i + "行 联络人(日) 长度超长，最大长度为255");
                            }else{
                                customerinfor.setProjapanese(Convert.toStr(value.get(12)));
                            }
                        }
                    }
                    if(value.size()>13)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(13)))){
                            if (Convert.toStr(value.get(13)).length() > 255) {
                                throw new LogicalException("第" + i + "行 联络人(英) 长度超长，最大长度为255");
                            }else{
                                customerinfor.setProenglish(Convert.toStr(value.get(13)));
                            }
                        }
                    }
                    if(value.size()>14)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(14)))){
                            if (Convert.toStr(value.get(14)).length() > 255) {
                                throw new LogicalException("第" + i + "行 联系电话 长度超长，最大长度为255");
                            }else{
                                customerinfor.setProtelephone(Convert.toStr(value.get(14)));
                            }
                        }
                    }
                    if(value.size()>15)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(15)))){
                            if (Convert.toStr(value.get(15)).length() > 255) {
                                throw new LogicalException("第" + i + "行 邮箱地址 长度超长，最大长度为255");
                            }else{
                                customerinfor.setProtemail(Convert.toStr(value.get(15)));
                            }
                        }
                    }
                    if(value.size()>16)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(16)))) {
                            if (Convert.toStr(value.get(16)).length() > 255) {
                                throw new LogicalException("第" + i + "行 地址(中) 长度超长，最大长度为255");
                            } else {
                                customerinfor.setAddchinese(Convert.toStr(value.get(16)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 地址(中) 不能为空");
                        }
                    }
                    if(value.size()>17)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(17)))) {
                            if (Convert.toStr(value.get(17)).length() > 255) {
                                throw new LogicalException("第" + i + "行 地址(日) 长度超长，最大长度为255");
                            } else {
                                customerinfor.setAddjapanese(Convert.toStr(value.get(17)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 地址(日) 不能为空");
                        }
                    }
                    if(value.size()>18)
                    {
                        if (!StringUtils.isNullOrEmpty(Convert.toStr(value.get(18)))) {
                            if (Convert.toStr(value.get(18)).length() > 255) {
                                throw new LogicalException("第" + i + "行 地址(英) 长度超长，最大长度为255");
                            } else {
                                customerinfor.setAddenglish(Convert.toStr(value.get(18)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 地址(英) 不能为空");
                        }
                    }
                    if(value.size()>19)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(19)))){
                            String person = Convert.toStr(value.get(19));
                            Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
                            Matcher isNum = pattern.matcher(person.substring(1));
                            if (!isNum.matches()) {
                                throw new LogicalException("第" + i + "行 人员规模应为数字");
                            }
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

                    if(value.size()>20)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(20)))){
                            if (Convert.toStr(value.get(20)).length() > 255) {
                                throw new LogicalException("第" + i + "行 网址 长度超长，最大长度为255");
                            }else{
                                customerinfor.setWebsite(Convert.toStr(value.get(20)));
                            }
                        }

                    }
                    if(value.size()>21)
                    {
                        if(!StringUtils.isNullOrEmpty(Convert.toStr(value.get(21)))){
                            if (Convert.toStr(value.get(21)).length() > 255) {
                                throw new LogicalException("第" + i + "行 备注 长度超长，最大长度为255");
                            }else{
                                customerinfor.setRemarks(Convert.toStr(value.get(21)));
                            }
                        }

                    }
                }
                String id = UUID.randomUUID().toString();
                if(!flag2.contains(customerinforPrimary)){
                    CustomerinforPrimary tem = new CustomerinforPrimary();
                    BeanUtils.copyProperties(tem, customerinforPrimary);
                    k_v.put(tem,id);
                    flag2.add(tem);
                    customerinforPrimary.setCustomerinforprimary_id(id);
                    customerinforPrimary.preInsert(tokenModel);
                    mainTable.add(customerinforPrimary);
//                    mainTable = mainTable.stream().collect(
//                            Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(
//                                    o -> o.getCustchinese() + ";" + o.getCustjapanese()+ ";" + o.getCustenglish()+ ";" + o.getAbbreviation()
//                                            + ";" + o.getThecompany()+ ";" + o.getCausecode()))), ArrayList::new));
                }else{
                    customerinfor.setCustomerinfor_id(UUID.randomUUID().toString());
                    customerinfor.setCustomerinforprimary_id(k_v.get(customerinforPrimary));
                    customerinfor.preInsert(tokenModel);
                    details.add(customerinfor);
                    accesscount = accesscount + 1;
                    continue;
                }

//                if(!flag.contains(customerinfor)){
//                    flag.add(customerinfor);
                    customerinfor.setCustomerinfor_id(UUID.randomUUID().toString());
                    customerinfor.setCustomerinforprimary_id(id);
                    customerinfor.preInsert(tokenModel);
                    details.add(customerinfor);
                    accesscount = accesscount + 1;
//                    details = details.stream().collect(
//                            Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(
//                                    o -> o.getCustchinese() + ";" + o.getCustjapanese()+ ";" + o.getCustenglish()+ ";" + o.getAbbreviation()
//                                            + ";" + o.getThecompany()+ ";" + o.getCausecode()))), ArrayList::new));
//                }
            }
            if(mainTable != null && mainTable.size() > 0){
                customerinforPrimaryMapper.insertList(mainTable);
            }
            if(details != null && details.size() > 0){
                customerinforMapper.insertList(details);
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;

    }
    // endregion scc upd 21/12/3 导入 to

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
            List<Dictionary> diclist = dictionaryService.getForSelect("BP007");//人员规模
            Map<String, String> result = new HashMap<>();
            for (Dictionary d : diclist) {
                result.put( d.getCode(), d.getValue1());
            }
            List<Dictionary> diclist1 = dictionaryService.getForSelect("BP028");//地域区分
            Map<String, String> result1 = new HashMap<>();
            for (Dictionary d : diclist1) {
                result1.put( d.getCode(), d.getValue1());
            }
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
                                case 7: row.createCell(j).setCellValue(result1.get(c.getRegindiff()));break;
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
                                case 19: row.createCell(j).setCellValue(result.get(c.getPerscale()));break;
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
