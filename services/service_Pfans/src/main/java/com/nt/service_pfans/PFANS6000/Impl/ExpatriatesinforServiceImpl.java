package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.mysql.fabric.Response;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.PricesetGroup;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.UserService;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetGroupMapper;
import com.nt.service_pfans.PFANS6000.mapper.PricesetMapper;
import com.nt.service_pfans.PFANS6000.mapper.SupplierinforMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExpatriatesinforServiceImpl implements ExpatriatesinforService {

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private SupplierinforMapper supplierinforMapper;

    @Autowired
    private PricesetMapper pricesetMapper;

    @Autowired
    private PricesetGroupMapper pricesetGroupMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Expatriatesinfor> getexpatriatesinfor(Expatriatesinfor expatriatesinfor) throws Exception {
        return expatriatesinforMapper.select(expatriatesinfor);
    }

    @Override
    public Expatriatesinfor getexpatriatesinforApplyOne(String expatriatesinfor_id) throws Exception {
        Expatriatesinfor infor = new Expatriatesinfor();
        infor = expatriatesinforMapper.selectByPrimaryKey(expatriatesinfor_id);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(infor.getAccount()));
        UserAccount account = mongoTemplate.findOne(query, UserAccount.class);
        if (account != null) {
            infor.setAccountname(account.getAccount());
        }
        return infor;
    }

    @Override
    public void updateinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
    }
    @Override
    public void crAccount(List<Expatriatesinfor> expatriatesinfor, TokenModel tokenModel) throws Exception {

        for(Expatriatesinfor item:expatriatesinfor){
            if(StrUtil.isEmpty(item.getAccount())){
                UserAccount userAccount = new UserAccount();
                userAccount.setAccount("KK-"+PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setPassword("KK-"+PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setUsertype("1");

                Query query = new Query();
                query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
                query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                List<UserAccount> list = mongoTemplate.find(query, UserAccount.class);

                if(list.size() > 0){
                    userAccount = list.get(0);
                }
                query = new Query();
                query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
                query.addCriteria(Criteria.where("rolename").is("外协staff"));
                List<Role>  rolss =  mongoTemplate.find(query, Role.class);

                userAccount.setRoles(rolss);
                userAccount.preInsert(tokenModel);
                mongoTemplate.save(userAccount);

                item.setAccount(userAccount.get_id());
                expatriatesinforMapper.updateByPrimaryKeySelective(item);
            }
        }

    }

    @Override
    public void crAccount2(List<Expatriatesinfor> expatriatesinfor, TokenModel tokenModel) throws Exception {

        for(Expatriatesinfor item:expatriatesinfor){
            if(StrUtil.isEmpty(item.getAccount())){
                UserAccount userAccount = new UserAccount();
                userAccount.setAccount("KK-"+PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setPassword("KK-"+PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setUsertype("1");

                Query query = new Query();
                query.addCriteria(Criteria.where("account").regex(userAccount.getAccount()));
//                query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
                query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                List<UserAccount> list = mongoTemplate.find(query, UserAccount.class);

                if(list.size() > 0){
                    userAccount.setAccount(userAccount.getAccount() + Convert.toStr(list.size()));
//                    userAccount = list.get(0);
                }

                query = new Query();
                query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
                query.addCriteria(Criteria.where("rolename").is("外协员工"));
                List<Role>  rolss =  mongoTemplate.find(query, Role.class);

                userAccount.setRoles(rolss);
                userAccount.preInsert(tokenModel);
                mongoTemplate.save(userAccount);

                Expatriatesinfor exp = new Expatriatesinfor();
                exp.setExpatriatesinfor_id(item.getExpatriatesinfor_id());
                List<Expatriatesinfor> explist = expatriatesinforMapper.select(exp);
                if(explist.size()>0)
                {
                    explist.get(0).setAccountname(userAccount.getAccount());
                    explist.get(0).setAccount(userAccount.get_id());
                    expatriatesinforMapper.updateByPrimaryKeySelective(explist.get(0));
                }
            }else{
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(item.getAccount()));
                List<UserAccount> list = mongoTemplate.find(query, UserAccount.class);
                if(list.size() > 0){
                    Expatriatesinfor explist = expatriatesinforMapper.selectByPrimaryKey(item.getExpatriatesinfor_id());
                    explist.setAccountname(list.get(0).getAccount());
                    expatriatesinforMapper.updateByPrimaryKeySelective(explist);
                }
            }
        }

    }

    @Override
    public void updateexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        if (expatriatesinfor.getOperationform().equals("BP024001")) {
            expatriatesinfor.setDistriobjects("0");
            expatriatesinfor.setVenuetarget("0");
        } else if (expatriatesinfor.getOperationform().equals("BP024002")) {
            expatriatesinfor.setDistriobjects("1");
            expatriatesinfor.setVenuetarget("0");
        } else {
            expatriatesinfor.setDistriobjects("1");
            expatriatesinfor.setVenuetarget("1");
        }
        List<Expatriatesinfor> list = new ArrayList<>();
        String Expname = "";
        if(!StringUtils.isNullOrEmpty(expatriatesinfor.getEmail())){
            Expatriatesinfor e = new Expatriatesinfor();
            e.setEmail(expatriatesinfor.getEmail());
            list = expatriatesinforMapper.select(e);
            if(list.size() > 0){
                if(!list.get(0).getExpname().equals(expatriatesinfor.getExpname())){
                    throw new LogicalException("邮箱重复,请重新确认！");
                }
            }
        }
        expatriatesinfor.preUpdate(tokenModel);
        expatriatesinforMapper.updateByPrimaryKeySelective(expatriatesinfor);
        if (expatriatesinfor.getWhetherentry().equals("BP006001")) {
            Priceset priceset = new Priceset();
            priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            String thisDate = DateUtil.format(new Date(), "yyyy-MM-dd");
            String sDate = DateUtil.format(new Date(), "yyyy-MM");

            List<Priceset> list1 = pricesetMapper.select(priceset);
            if(list1.size() == 0)
            {
                PricesetGroup pricesetGroup = new PricesetGroup();
                pricesetGroup.setPd_date(sDate);
                List<PricesetGroup> pricesetGroupList = pricesetGroupMapper.select(pricesetGroup);
                if(pricesetGroupList.size()>0)
                {
                    priceset.preInsert(tokenModel);
                    priceset.setPriceset_id(UUID.randomUUID().toString());
                    priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
                    priceset.setGraduation(expatriatesinfor.getGraduation_year());
                    priceset.setCompany(expatriatesinfor.getSuppliername());
                    priceset.setAssesstime(thisDate);
                    priceset.setPricesetgroup_id(pricesetGroupList.get(0).getPricesetgroup_id());
                    priceset.setStatus("0");
                    pricesetMapper.insert(priceset);
                }
            }
        }
    }

    @Override
    public void createexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        expatriatesinfor.preInsert(tokenModel);
        expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
        expatriatesinfor.setExits("1");
        expatriatesinforMapper.insert(expatriatesinfor);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> expimport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
//            创建listVo集合方便存储导入信息
            List<Expatriatesinfor> listVo = new ArrayList<Expatriatesinfor>();
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
            model.add("姓名");
            model.add("性别");
            model.add("出生日期");
            model.add("毕业院校");
            model.add("学历");
            model.add("联系方式");
            model.add("毕业年");
            model.add("供应商名称");
            model.add("Rn");
            model.add("邮箱地址");
            model.add("group");
            model.add("作業場所");
            model.add("卡号");
            model.add("作業分類");
            model.add("经验特长");
            model.add("面试部门");
            model.add("面试时间");
            model.add("面试结果");
            model.add("入场与否");
            model.add("入场时间");
            model.add("退场与否");
            model.add("退场时间");
            model.add("退场理由");
            model.add("所有技術");
            model.add("現場評価");
            model.add("业务影响");
            model.add("対策");
            model.add("备注");
            List<Object> key = list.get(0);
//           上传模板与标准模板 校验

            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().replace("●","").trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }

            List<Integer> updint = new  ArrayList<Integer>();
            //resultInsUpd true:插入 false:更新
            boolean resultInsUpd = true;
            for (int j = 0; j < key.size(); j++)
            {
                if(key.get(j).toString().trim().contains("●"))
                {
                    resultInsUpd = false;
                    updint.add(j);
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            if(resultInsUpd)
            {
                for (int i = 1; i < list.size(); i++)
                {
                    Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                    List<Object> value = list.get(k);
                    k++;
                    if (value != null && !value.isEmpty()) {
                        if(value.size()>12)
                        {
                            if(value.size()>0)
                            {
                                expatriatesinfor.setExpname(Convert.toStr(value.get(0)));
                                if(expatriatesinfor.getExpname() != null && expatriatesinfor.getExpname().trim().length() > 36)
                                {
                                    throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 姓名 长度超长，最大长度为36");
                                }
                                Expatriatesinfor expatriatesinforName = new Expatriatesinfor();
                                expatriatesinforName.setExpname(expatriatesinfor.getExpname().trim());
                                List<Expatriatesinfor> expatriatesinforList = new ArrayList<Expatriatesinfor>();
                                expatriatesinforList = expatriatesinforMapper.select(expatriatesinforName);
                                if(expatriatesinforList.size()>0)
                                {
                                    throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
                                }
                                else
                                {
                                    UserAccount userAccount = new UserAccount();
                                    userAccount.setAccount("KK-"+PinyinHelper.convertToPinyinString(expatriatesinfor.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                                    userAccount.setUsertype("1");
                                    Query query = new Query();
                                    query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                                    query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                                    List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                                    if(userAccountlist.size()>0)
                                    {
                                        throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 姓名 在外驻人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                                    }
                                }
                            }
                            if(value.size()>1)
                            {
                                String sex = Convert.toStr(value.get(1));
                                if(sex!=null && !sex.isEmpty())
                                {
                                    Dictionary dictionary =new Dictionary();
                                    dictionary.setValue1(sex.trim());
                                    dictionary.setType("GT");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if(dictionaryList.size()>0)
                                    {
                                        expatriatesinfor.setSex(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if(value.size()>2)
                            {
                                String birth = Convert.toStr(value.get(2));
                                if(birth!=null && birth.length() > 9)
                                {
                                    birth = birth.trim().substring(0,10);
                                    expatriatesinfor.setBirth(sff.format(birth));
                                }
                            }
                            if(value.size()>3)
                            {
                                expatriatesinfor.setGraduateschool(Convert.toStr(value.get(3)));
                            }
                            if(value.size()>4)
                            {
                                String education = Convert.toStr(value.get(4));
                                if(education!=null && !education.isEmpty())
                                {
                                    Dictionary dictionary =new Dictionary();
                                    dictionary.setValue1(education.trim());
                                    dictionary.setType("RS");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if(dictionaryList.size()>0)
                                    {
                                        expatriatesinfor.setEducation(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if(value.size()>5)
                            {
                                expatriatesinfor.setContactinformation(Convert.toStr(value.get(5)));
                            }
                            if(value.size()>6)
                            {
                                String graduation_year = Convert.toStr(value.get(6));
                                if(graduation_year !=null && graduation_year.length()>3)
                                {
                                    graduation_year = graduation_year.trim().substring(0,4);
                                    expatriatesinfor.setGraduation_year(sf.parse(graduation_year));
                                }
                            }
                            if(value.size()>7)
                            {
                                String suppliername = Convert.toStr(value.get(7));
                                Supplierinfor supplierinfor = new Supplierinfor();
                                supplierinfor.setSupchinese(suppliername);
                                List<Supplierinfor> supplierinforList = new ArrayList<>();
                                supplierinforList = supplierinforMapper.select(supplierinfor);
                                if(supplierinforList.size()>0)
                                {
                                    expatriatesinfor.setSuppliername(supplierinforList.get(0).getSupchinese());
                                    expatriatesinfor.setSupplierinfor_id(supplierinforList.get(0).getSupplierinfor_id());
                                }
                                else
                                {
                                    expatriatesinfor.setSuppliername(suppliername);
                                }
                            }
                            if(value.size()>8)
                            {
                                String rn = Convert.toStr(value.get(8));
                                if(rn!=null && !rn.isEmpty())
                                {
                                    Dictionary dictionary =new Dictionary();
                                    dictionary.setValue1(rn.trim());
                                    dictionary.setType("RS");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if(dictionaryList.size()>0)
                                    {
                                        expatriatesinfor.setRn(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if(value.size()>9)
                            {
                                expatriatesinfor.setEmail(Convert.toStr(value.get(9)));
                                if(expatriatesinfor.getEmail() != null && expatriatesinfor.getEmail().length() > 255)
                                {
                                    throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 邮箱 长度超长，最大长度为255");
                                }
                            }
                            if(value.size()>10)
                            {
                                String groupName = Convert.toStr(value.get(10));
                                if(groupName!=null && !groupName.isEmpty())
                                {
                                    Query query = new Query();
                                    query.addCriteria(Criteria.where("userinfo.groupname").is(groupName));
                                    List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                    customerInfoList = mongoTemplate.find(query,CustomerInfo.class);
                                    if(customerInfoList.size()>0)
                                    {
                                        expatriatesinfor.setGroup_id(customerInfoList.get(0).getUserinfo().getGroupid());
                                    }
                                    else
                                    {
                                        expatriatesinfor.setGroup_id(groupName);
                                    }
                                }
                            }
                            if(value.size()>11)
                            {
                                String operationform = Convert.toStr(value.get(11));
                                if(operationform!=null && !operationform.isEmpty())
                                {
                                    Dictionary dictionary =new Dictionary();
                                    dictionary.setValue1(operationform.trim());
                                    dictionary.setType("BP");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if(dictionaryList.size()>0)
                                    {
                                        expatriatesinfor.setOperationform(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if(value.size()>12)
                            {
                                expatriatesinfor.setNumber(Convert.toStr(value.get(12)));
                                if(expatriatesinfor.getNumber() == null || expatriatesinfor.getNumber().isEmpty())
                                {
                                    throw new LogicalException("第" + i + "行 卡号 不能为空，请确认。");
                                }
                                if(expatriatesinfor.getNumber() != null && expatriatesinfor.getNumber().length() > 36)
                                {
                                    throw new LogicalException("第" + i + "行 卡号 长度超长，最大长度为36");
                                }
                                Expatriatesinfor expatriatesinforName = new Expatriatesinfor();
                                expatriatesinforName.setNumber(expatriatesinfor.getNumber().trim());
                                List<Expatriatesinfor> expatriatesinforList = new ArrayList<Expatriatesinfor>();
                                expatriatesinforList = expatriatesinforMapper.select(expatriatesinforName);
                                if(expatriatesinforList.size()>0)
                                {
                                    throw new LogicalException("第" + i + "行的卡号在外驻人员表中已有人使用，请确认。");
                                }
                            }
                            if(value.size()>13)
                            {
                                String jobclassification = Convert.toStr(value.get(13));
                                if(jobclassification!=null && !jobclassification.isEmpty())
                                {
                                    Dictionary dictionary =new Dictionary();
                                    dictionary.setValue1(jobclassification.trim());
                                    dictionary.setType("BP");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if(dictionaryList.size()>0)
                                    {
                                        expatriatesinfor.setJobclassification(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if(value.size()>14)
                            {
                                expatriatesinfor.setSpeciality(Convert.toStr(value.get(14)));
                            }
                            if(value.size()>15)
                            {
                                String interviewdep = Convert.toStr(value.get(15));
                                if(interviewdep!=null && !interviewdep.isEmpty())
                                {
                                    Query query = new Query();
                                    query.addCriteria(Criteria.where("userinfo.groupname").is(interviewdep));
                                    List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                    customerInfoList = mongoTemplate.find(query,CustomerInfo.class);
                                    if(customerInfoList.size()>0)
                                    {
                                        expatriatesinfor.setInterviewdep(customerInfoList.get(0).getUserinfo().getGroupid());
                                    }
                                    else
                                    {
                                        expatriatesinfor.setInterviewdep(interviewdep);
                                    }
                                }
                            }
                            if(value.size()>16)
                            {
                                String interview_date = Convert.toStr(value.get(16));
                                if(interview_date!=null && interview_date.length() > 9)
                                {
                                    interview_date = interview_date.trim().substring(0,10);
                                    expatriatesinfor.setInterview_date(interview_date);
                                }
                            }
                            if(value.size()>17)
                            {
                                String result = Convert.toStr(value.get(17));
                                if(result!=null && !result.isEmpty())
                                {
                                    Dictionary dictionary =new Dictionary();
                                    dictionary.setValue1(result.trim());
                                    dictionary.setType("BP");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if(dictionaryList.size()>0)
                                    {
                                        expatriatesinfor.setResult(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if(value.size()>18)
                            {
                                String whetherentry = Convert.toStr(value.get(18));
                                if(whetherentry!=null && !whetherentry.isEmpty())
                                {
                                    Dictionary dictionary =new Dictionary();
                                    dictionary.setValue1(whetherentry.trim());
                                    dictionary.setType("BP");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if(dictionaryList.size()>0)
                                    {
                                        expatriatesinfor.setWhetherentry(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if(value.size()>19)
                            {
                                String admissiontime = Convert.toStr(value.get(19));
                                if(admissiontime!=null && admissiontime.length() > 9)
                                {
                                    admissiontime = admissiontime.trim().substring(0,10);
                                    expatriatesinfor.setAdmissiontime(sff.parse(admissiontime));
                                }
                            }
                            if(value.size()>20)
                            {
                                String exits = Convert.toStr(value.get(20));
                                if(exits!=null && !exits.isEmpty())
                                {
                                    if(exits.trim().equals("是"))
                                    {
                                        expatriatesinfor.setExits("0");
                                    }
                                    else if(exits.trim().equals("否"))
                                    {
                                        expatriatesinfor.setExits("1");
                                    }
                                }
                                if(expatriatesinfor.getExits().equals("0"))
                                {
                                    if(value.size()>21)
                                    {
                                        String exitime = Convert.toStr(value.get(21));
                                        if(exitime!=null && exitime.length() > 9)
                                        {
                                            exitime = exitime.trim().substring(0,10);
                                            expatriatesinfor.setExitime(sff.parse(exitime));
                                        }
                                    }
                                    if(value.size()>22)
                                    {
                                        String exitreason = Convert.toStr(value.get(22));
                                        if(exitreason!=null && !exitreason.isEmpty())
                                        {
                                            Dictionary dictionary =new Dictionary();
                                            dictionary.setValue1(exitreason.trim());
                                            dictionary.setType("BP");
                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                            if(dictionaryList.size()>0)
                                            {
                                                expatriatesinfor.setExitreason(dictionaryList.get(0).getCode());
                                            }
                                        }
                                    }
                                    if(value.size()>23)
                                    {
                                        String alltechnology = Convert.toStr(value.get(23));
                                        if(alltechnology!=null && !alltechnology.isEmpty())
                                        {
                                            expatriatesinfor.setAlltechnology(alltechnology.trim());
//                                            Dictionary dictionary =new Dictionary();
//                                            dictionary.setValue1(alltechnology.trim());
//                                            dictionary.setType("BP");
//                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
//                                            if(dictionaryList.size()>0)
//                                            {
//                                                expatriatesinfor.setAlltechnology(dictionaryList.get(0).getCode());
//                                            }
                                        }
                                    }
                                    if(value.size()>24)
                                    {
                                        String sitevaluation = Convert.toStr(value.get(24));
                                        if(sitevaluation!=null && !sitevaluation.isEmpty())
                                        {
                                            Dictionary dictionary =new Dictionary();
                                            dictionary.setValue1(sitevaluation.trim());
                                            dictionary.setType("BP");
                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                            if(dictionaryList.size()>0)
                                            {
                                                expatriatesinfor.setSitevaluation(dictionaryList.get(0).getCode());
                                            }
                                        }
                                    }
                                    if(value.size()>25)
                                    {
                                        String businessimpact = Convert.toStr(value.get(25));
                                        if(businessimpact!=null && !businessimpact.isEmpty())
                                        {
                                            Dictionary dictionary =new Dictionary();
                                            dictionary.setValue1(businessimpact.trim());
                                            dictionary.setType("BP");
                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                            if(dictionaryList.size()>0)
                                            {
                                                expatriatesinfor.setBusinessimpact(dictionaryList.get(0).getCode());
                                            }
                                        }
                                    }
                                    if(value.size()>26)
                                    {
                                        String countermeasure = Convert.toStr(value.get(26));
                                        if(countermeasure!=null && !countermeasure.isEmpty())
                                        {
                                            expatriatesinfor.setCountermeasure(countermeasure.trim());
//                                            Dictionary dictionary =new Dictionary();
//                                            dictionary.setValue1(countermeasure.trim());
//                                            dictionary.setType("BP");
//                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
//                                            if(dictionaryList.size()>0)
//                                            {
//                                                expatriatesinfor.setCountermeasure(dictionaryList.get(0).getCode());
//                                            }
                                        }
                                    }
                                }
                            }
                            if(value.size()>27)
                            {
                                expatriatesinfor.setRemarks(Convert.toStr(value.get(27)));
                            }
                        }
                        else
                        {
                            throw new LogicalException("第" + i + "行 卡号 不能为空，请确认。");
                        }
                    }
                    expatriatesinfor.preInsert();
                    expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
                    expatriatesinforMapper.insert(expatriatesinfor);
                    listVo.add(expatriatesinfor);
                    accesscount = accesscount + 1;
                }
            }
            else
            {
                for (int i = 1; i < list.size(); i++)
                {
                    Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                    List<Object> value = list.get(i);
                    if (value != null && !value.isEmpty() && value .size()>12)
                    {
                        expatriatesinfor.setNumber(Convert.toStr(value.get(12)));
                        List<Expatriatesinfor> expatriatesinforList = new ArrayList<Expatriatesinfor>();
                        expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
                        if(expatriatesinforList.size()>0)
                        {
                            for(int j = 0;j < updint.size();j++)
                            {
                                if(value.size() > updint.get(j))
                                {
                                    switch (updint.get(j))
                                    {
                                        case 0:
                                            String name = Convert.toStr(value.get(0));
                                            expatriatesinforList.get(0).setExpname(Convert.toStr(value.get(0)));
                                            if(name != null && name.trim().length() > 36)
                                            {
                                                throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 姓名 长度超长，最大长度为36");
                                            }
                                            if(!(name.equals(expatriatesinforList.get(0).getExpname())))
                                            {
                                                Expatriatesinfor expatriatesinforName = new Expatriatesinfor();
                                                expatriatesinforName.setExpname(expatriatesinforList.get(0).getExpname().trim());
                                                List<Expatriatesinfor> inforList = new ArrayList<Expatriatesinfor>();
                                                inforList = expatriatesinforMapper.select(expatriatesinforName);
                                                if(inforList.size()>0)
                                                {
                                                    throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
                                                }
                                                else
                                                {
                                                    UserAccount userAccount = new UserAccount();
                                                    userAccount.setAccount("KK-"+PinyinHelper.convertToPinyinString(expatriatesinforList.get(0).getExpname(), "", PinyinFormat.WITHOUT_TONE));
                                                    userAccount.setUsertype("1");
                                                    Query query = new Query();
                                                    query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                                                    query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                                                    List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                                                    if(userAccountlist.size()>0)
                                                    {
                                                        throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 姓名 在外驻人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                                                    }
                                                }
                                            }
                                            break;
                                        case 1:
                                            String sex = Convert.toStr(value.get(1));
                                            if(sex!=null && !sex.isEmpty())
                                            {
                                                Dictionary dictionary =new Dictionary();
                                                dictionary.setValue1(sex.trim());
                                                dictionary.setType("GT");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if(dictionaryList.size()>0)
                                                {
                                                    expatriatesinforList.get(0).setSex(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 2:
                                            expatriatesinforList.get(0).setBirth(Convert.toStr(value.get(2)));
                                            break;
                                        case 3:
                                            expatriatesinforList.get(0).setGraduateschool(Convert.toStr(value.get(3)));
                                            break;
                                        case 4:
                                            String education = Convert.toStr(value.get(4));
                                            if(education!=null && !education.isEmpty())
                                            {
                                                Dictionary dictionary =new Dictionary();
                                                dictionary.setValue1(education.trim());
                                                dictionary.setType("RS");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if(dictionaryList.size()>0)
                                                {
                                                    expatriatesinforList.get(0).setEducation(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 5:
                                            expatriatesinfor.setContactinformation(Convert.toStr(value.get(5)));
                                            break;
                                        case 6:
                                            String graduation_year = Convert.toStr(value.get(6));
                                            if(graduation_year !=null && graduation_year.length()>3)
                                            {
                                                graduation_year = graduation_year.trim().substring(0,4);
                                                expatriatesinforList.get(0).setGraduation_year(sf.parse(graduation_year));
                                            }
                                            break;
                                        case 7:
                                            String suppliername = Convert.toStr(value.get(7));
                                            Supplierinfor supplierinfor = new Supplierinfor();
                                            supplierinfor.setSupchinese(suppliername);
                                            List<Supplierinfor> supplierinforList = new ArrayList<>();
                                            supplierinforList = supplierinforMapper.select(supplierinfor);
                                            if(supplierinforList.size()>0)
                                            {
                                                expatriatesinforList.get(0).setSuppliername(supplierinforList.get(0).getSupchinese());
                                                expatriatesinforList.get(0).setSupplierinfor_id(supplierinforList.get(0).getSupplierinfor_id());
                                            }
                                            else
                                            {
                                                expatriatesinforList.get(0).setSuppliername(suppliername);
                                            }
                                            break;
                                        case 8:
                                            String rn = Convert.toStr(value.get(8));
                                            if(rn!=null && !rn.isEmpty())
                                            {
                                                Dictionary dictionary =new Dictionary();
                                                dictionary.setValue1(rn.trim());
                                                dictionary.setType("RS");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if(dictionaryList.size()>0)
                                                {
                                                    expatriatesinforList.get(0).setRn(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 9:
                                            expatriatesinforList.get(0).setEmail(Convert.toStr(value.get(9)));
                                            if(expatriatesinforList.get(0).getEmail() != null && expatriatesinforList.get(0).getEmail().length() > 255)
                                            {
                                                throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "对应的 邮箱 长度超长，最大长度为255");
                                            }
                                            break;
                                        case 10:
                                            String groupName = Convert.toStr(value.get(10));
                                            if(groupName!=null && !groupName.isEmpty())
                                            {
                                                Query query = new Query();
                                                query.addCriteria(Criteria.where("userinfo.groupname").is(groupName));
                                                List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                                customerInfoList = mongoTemplate.find(query,CustomerInfo.class);
                                                if(customerInfoList.size()>0)
                                                {
                                                    expatriatesinforList.get(0).setGroup_id(customerInfoList.get(0).getUserinfo().getGroupid());
                                                }
                                                else
                                                {
                                                    expatriatesinforList.get(0).setGroup_id(groupName);
                                                }
                                            }
                                            break;
                                        case 11:
                                            String operationform = Convert.toStr(value.get(11));
                                            if(operationform!=null && !operationform.isEmpty())
                                            {
                                                Dictionary dictionary =new Dictionary();
                                                dictionary.setValue1(operationform.trim());
                                                dictionary.setType("BP");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if(dictionaryList.size()>0)
                                                {
                                                    expatriatesinforList.get(0).setOperationform(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 12:
                                            break;
                                        case 13:
                                            String jobclassification = Convert.toStr(value.get(13));
                                            if(jobclassification!=null && !jobclassification.isEmpty())
                                            {
                                                Dictionary dictionary =new Dictionary();
                                                dictionary.setValue1(jobclassification.trim());
                                                dictionary.setType("BP");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if(dictionaryList.size()>0)
                                                {
                                                    expatriatesinforList.get(0).setJobclassification(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 14:
                                            expatriatesinforList.get(0).setSpeciality(Convert.toStr(value.get(14)));
                                            break;
                                        case 15:
                                            String interviewdep = Convert.toStr(value.get(15));
                                            if(interviewdep!=null && !interviewdep.isEmpty())
                                            {
                                                Query query = new Query();
                                                query.addCriteria(Criteria.where("userinfo.groupname").is(interviewdep));
                                                List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                                customerInfoList = mongoTemplate.find(query,CustomerInfo.class);
                                                if(customerInfoList.size()>0)
                                                {
                                                    expatriatesinforList.get(0).setInterviewdep(customerInfoList.get(0).getUserinfo().getGroupid());
                                                }
                                                else
                                                {
                                                    expatriatesinforList.get(0).setInterviewdep(interviewdep);
                                                }
                                            }
                                            break;
                                        case 16:
                                            String interview_date = Convert.toStr(value.get(16));
                                            if(interview_date!=null && interview_date.length() > 9)
                                            {
                                                interview_date = interview_date.trim().substring(0,10);
                                                expatriatesinforList.get(0).setInterview_date(interview_date);
                                            }
                                            break;
                                        case 17:
                                            String result = Convert.toStr(value.get(17));
                                            if(result!=null && !result.isEmpty())
                                            {
                                                Dictionary dictionary =new Dictionary();
                                                dictionary.setValue1(result.trim());
                                                dictionary.setType("BP");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if(dictionaryList.size()>0)
                                                {
                                                    expatriatesinfor.setResult(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 18:
                                            String whetherentry = Convert.toStr(value.get(18));
                                            if(whetherentry!=null && !whetherentry.isEmpty())
                                            {
                                                Dictionary dictionary =new Dictionary();
                                                dictionary.setValue1(whetherentry.trim());
                                                dictionary.setType("BP");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if(dictionaryList.size()>0)
                                                {
                                                    expatriatesinfor.setWhetherentry(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 19:
                                            String admissiontime = Convert.toStr(value.get(19));
                                            if(admissiontime!=null && admissiontime.length() > 9)
                                            {
                                                admissiontime = admissiontime.trim().substring(0,10);
                                                expatriatesinforList.get(0).setAdmissiontime(sff.parse(admissiontime));
                                            }
                                            break;
                                        case 20:
                                            String exits = Convert.toStr(value.get(20));
                                            if(exits!=null && !exits.isEmpty())
                                            {
                                                if(exits.trim().equals("是"))
                                                {
                                                    expatriatesinforList.get(0).setExits("0");
                                                }
                                                else if(exits.trim().equals("否"))
                                                {
                                                    expatriatesinforList.get(0).setExits("1");
                                                }
                                            }
                                            break;
                                        case 21:
                                            if(expatriatesinforList.get(0).getExits().equals("0"))
                                            {
                                                String exitime = Convert.toStr(value.get(21));
                                                if(exitime!=null && exitime.length() > 9)
                                                {
                                                    exitime = exitime.trim().substring(0,10);
                                                    expatriatesinforList.get(0).setExitime(sff.parse(exitime));
                                                }
                                            }
                                            break;
                                        case 22:
                                            if(expatriatesinforList.get(0).getExits().equals("0"))
                                            {
                                                String exitreason = Convert.toStr(value.get(22));
                                                if(exitreason!=null && !exitreason.isEmpty())
                                                {
                                                    Dictionary dictionary =new Dictionary();
                                                    dictionary.setValue1(exitreason.trim());
                                                    dictionary.setType("BP");
                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                    if(dictionaryList.size()>0)
                                                    {
                                                        expatriatesinforList.get(0).setExitreason(dictionaryList.get(0).getCode());
                                                    }
                                                }
                                            }
                                            break;
                                        case 23:
                                            if(expatriatesinforList.get(0).getExits().equals("0"))
                                            {
                                                String alltechnology = Convert.toStr(value.get(23));
                                                if(alltechnology!=null && !alltechnology.isEmpty())
                                                {
                                                    expatriatesinforList.get(0).setAlltechnology(alltechnology.trim());
//                                                    Dictionary dictionary =new Dictionary();
//                                                    dictionary.setValue1(alltechnology.trim());
//                                                    dictionary.setType("BP");
//                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
//                                                    if(dictionaryList.size()>0)
//                                                    {
//                                                        expatriatesinforList.get(0).setAlltechnology(dictionaryList.get(0).getCode());
//                                                    }
                                                }
                                            }
                                            break;
                                        case 24:
                                            if(expatriatesinforList.get(0).getExits().equals("0"))
                                            {
                                                String sitevaluation = Convert.toStr(value.get(24));
                                                if(sitevaluation!=null && !sitevaluation.isEmpty())
                                                {
                                                    Dictionary dictionary =new Dictionary();
                                                    dictionary.setValue1(sitevaluation.trim());
                                                    dictionary.setType("BP");
                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                    if(dictionaryList.size()>0)
                                                    {
                                                        expatriatesinforList.get(0).setSitevaluation(dictionaryList.get(0).getCode());
                                                    }
                                                }
                                            }
                                            break;
                                        case 25:
                                            if(expatriatesinforList.get(0).getExits().equals("0"))
                                            {
                                                String businessimpact = Convert.toStr(value.get(25));
                                                if(businessimpact!=null && !businessimpact.isEmpty())
                                                {
                                                    Dictionary dictionary =new Dictionary();
                                                    dictionary.setValue1(businessimpact.trim());
                                                    dictionary.setType("BP");
                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                    if(dictionaryList.size()>0)
                                                    {
                                                        expatriatesinforList.get(0).setBusinessimpact(dictionaryList.get(0).getCode());
                                                    }
                                                }
                                            }
                                            break;
                                        case 26:
                                            if(expatriatesinforList.get(0).getExits().equals("0"))
                                            {
                                                String countermeasure = Convert.toStr(value.get(26));
                                                if(countermeasure!=null && !countermeasure.isEmpty())
                                                {
                                                    expatriatesinforList.get(0).setCountermeasure(countermeasure.trim());
//                                                    Dictionary dictionary =new Dictionary();
//                                                    dictionary.setValue1(countermeasure.trim());
//                                                    dictionary.setType("BP");
//                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
//                                                    if(dictionaryList.size()>0)
//                                                    {
//                                                        expatriatesinforList.get(0).setCountermeasure(dictionaryList.get(0).getCode());
//                                                    }
                                                }
                                            }
                                            break;
                                        case 27:
                                            expatriatesinfor.setRemarks(Convert.toStr(value.get(27)));
                                            break;
                                    }
                                }
                            }
                            expatriatesinforList.get(0).preUpdate(tokenModel);
                            expatriatesinforMapper.updateByPrimaryKey(expatriatesinforList.get(0));
                            accesscount = accesscount + 1;
                        }
                        else
                        {
                            throw new LogicalException("卡号（"+ Convert.toStr(value.get(12)) +"）"  + "在外驻人员表中不存在，无法更新，请确认。");
                        }
                    }
                }
            }

            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}



