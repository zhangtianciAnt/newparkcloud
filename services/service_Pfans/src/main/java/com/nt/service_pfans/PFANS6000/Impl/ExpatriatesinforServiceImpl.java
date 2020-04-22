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
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS6000.Expatriatesinfor;
import com.nt.dao_Pfans.PFANS6000.Priceset;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.mapper.ExpatriatesinforMapper;
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
    private MongoTemplate mongoTemplate;

    @Override
    public List<Expatriatesinfor> getexpatriatesinfor(Expatriatesinfor expatriatesinfor) throws Exception {
        return expatriatesinforMapper.select(expatriatesinfor);
    }

    @Override
    public Expatriatesinfor getexpatriatesinforApplyOne(String expatriatesinfor_id) throws Exception {
        return expatriatesinforMapper.selectByPrimaryKey(expatriatesinfor_id);
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
                query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
                query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                List<UserAccount> list = mongoTemplate.find(query, UserAccount.class);

                if(list.size() > 0){
                    userAccount = list.get(0);
                }

                query = new Query();
                query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
                query.addCriteria(Criteria.where("rolename").is("外协员工"));
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
            String thisDate = DateUtil.format(new Date(), "yyyy-MM-dd");
            Priceset priceset = new Priceset();
            priceset.preInsert(tokenModel);
            priceset.setPriceset_id(UUID.randomUUID().toString());
            priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            priceset.setGraduation(expatriatesinfor.getGraduation_year());
            priceset.setCompany(expatriatesinfor.getSuppliername());
            priceset.setAssesstime(thisDate);
            priceset.setStatus("0");
            pricesetMapper.insert(priceset);
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
            model.add("毕业院校");
            model.add("学历");
            model.add("毕业年");
            model.add("供应商名称");
            model.add("Rn");
            model.add("邮箱地址");
            model.add("group");
            model.add("作業場所");
            model.add("卡号");
            model.add("作業分類");
            model.add("入场时间");
            model.add("经验特长");
            model.add("退场与否");
            model.add("退场时间");
            model.add("退场理由");
            model.add("所有技術");
            model.add("現場評価");
            model.add("业务影响");
            model.add("対策");
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
            SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            for (int i = 1; i < list.size(); i++) {
                Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if(value.size()>0)
                    {
                        expatriatesinfor.setExpname(Convert.toStr(value.get(0)));
                        if(expatriatesinfor.getExpname() != null && expatriatesinfor.getExpname().trim().length() > 36)
                        {
                            throw new LogicalException("第" + i + "行 姓名 长度超长，最大长度为36");
                        }
                    }
                    if(value.size()>1)
                    {
                        String sex = Convert.toStr(value.get(1));
                        if(sex!=null && !sex.isEmpty())
                        {
                            if(sex.trim().equals("男"))
                            {
                                expatriatesinfor.setSex("PR019001");
                            }
                            else if(sex.trim().equals("女"))
                            {
                                expatriatesinfor.setSex("PR019002");
                            }
                        }
                    }
                    if(value.size()>2)
                    {
                        expatriatesinfor.setGraduateschool(Convert.toStr(value.get(2)));
                    }
                    if(value.size()>3)
                    {
                        String education = Convert.toStr(value.get(3));
                        if(education!=null && !education.isEmpty())
                        {
                            if(education.trim().equals("本科"))
                            {
                                expatriatesinfor.setEducation("PR022001");
                            }
                            else if(education.trim().equals("专科"))
                            {
                                expatriatesinfor.setEducation("PR022002");
                            }
                            else if(education.trim().equals("中专"))
                            {
                                expatriatesinfor.setEducation("PR022003");
                            }
                            else if(education.trim().equals("研究生"))
                            {
                                expatriatesinfor.setEducation("PR022004");
                            }
                            else if(education.trim().equals("博士"))
                            {
                                expatriatesinfor.setEducation("PR022005");
                            }
                        }
                    }
                    if(value.size()>4)
                    {
                        String graduation_year = Convert.toStr(value.get(4));
                        if(graduation_year !=null && graduation_year.length()>3)
                        {
                            graduation_year = graduation_year.trim().substring(0,4);
                            expatriatesinfor.setGraduation_year(sf.parse(graduation_year));
                        }
                    }
                    if(value.size()>5)
                    {
                        String suppliername = Convert.toStr(value.get(5));
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
                    if(value.size()>6)
                    {
                        String rn = Convert.toStr(value.get(6));
                        if(rn!=null && !rn.isEmpty())
                        {
                            if(rn.trim().equals("R3"))
                            {
                                expatriatesinfor.setRn("PR021001");
                            }
                            else if(rn.trim().equals("R4"))
                            {
                                expatriatesinfor.setRn("PR021002");
                            }
                            else if(rn.trim().equals("R5"))
                            {
                                expatriatesinfor.setRn("PR021003");
                            }
                            else if(rn.trim().equals("R6"))
                            {
                                expatriatesinfor.setRn("PR021004");
                            }
                            else if(rn.trim().equals("R7"))
                            {
                                expatriatesinfor.setRn("PR021005");
                            }
                            else if(rn.trim().equals("R8C"))
                            {
                                expatriatesinfor.setRn("PR021006");
                            }
                            else if(rn.trim().equals("R8B"))
                            {
                                expatriatesinfor.setRn("PR021007");
                            }
                            else if(rn.trim().equals("R8A"))
                            {
                                expatriatesinfor.setRn("PR021008");
                            }
                            else if(rn.trim().equals("R9A"))
                            {
                                expatriatesinfor.setRn("PR021009");
                            }
                            else if(rn.trim().equals("R9B"))
                            {
                                expatriatesinfor.setRn("PR021010");
                            }
                            else if(rn.trim().equals("R10"))
                            {
                                expatriatesinfor.setRn("PR021011");
                            }
                            else if(rn.trim().equals("R11A"))
                            {
                                expatriatesinfor.setRn("PR021012");
                            }
                        }
                    }
                    if(value.size()>7)
                    {
                        expatriatesinfor.setEmail(Convert.toStr(value.get(7)));
                        if(expatriatesinfor.getEmail() != null && expatriatesinfor.getEmail().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 邮箱 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>8)
                    {
                        String groupName = Convert.toStr(value.get(8));
                        if(groupName!=null && !groupName.isEmpty())
                        {
                            Query query = new Query();
                            query.addCriteria(Criteria.where("userinfo.groupname").is(groupName));
                            //ObjectMapper objectMapper = new ObjectMapper();
                            //Response response = objectMapper.readValue(query, Response.class);
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
                    if(value.size()>9)
                    {
                        String operationform = Convert.toStr(value.get(9));
                        if(operationform!=null && !operationform.isEmpty())
                        {
                            if(operationform.trim().equals("構内外注"))
                            {
                                expatriatesinfor.setOperationform("BP024001");
                            }
                            else if(operationform.trim().equals("構外外注"))
                            {
                                expatriatesinfor.setOperationform("BP024002");
                            }
                        }
                    }
                    if(value.size()>10)
                    {
                        expatriatesinfor.setNumber(Convert.toStr(value.get(10)));
                        if(expatriatesinfor.getNumber() != null && expatriatesinfor.getNumber().length() > 36)
                        {
                            throw new LogicalException("第" + i + "行 卡号 长度超长，最大长度为36");
                        }
                    }
                    if(value.size()>11)
                    {
                        String jobclassification = Convert.toStr(value.get(11));
                        if(jobclassification!=null && !jobclassification.isEmpty())
                        {
                            if(jobclassification.trim().equals("委任開発"))
                            {
                                expatriatesinfor.setJobclassification("BP025001");
                            }
                            else if(jobclassification.trim().equals("委任テスト"))
                            {
                                expatriatesinfor.setJobclassification("BP025002");
                            }
                            else if(jobclassification.trim().equals("構内請負"))
                            {
                                expatriatesinfor.setJobclassification("BP025003");
                            }
                            else if(jobclassification.trim().equals("構外請負"))
                            {
                                expatriatesinfor.setJobclassification("BP025004");
                            }
                            else if(jobclassification.trim().equals("スタッフ"))
                            {
                                expatriatesinfor.setJobclassification("BP025005");
                            }
                        }
                    }
                    if(value.size()>12)
                    {
                        String admissiontime = Convert.toStr(value.get(12));
                        if(admissiontime!=null && admissiontime.length() > 9)
                        {
                            admissiontime = admissiontime.trim().substring(0,10);
                            expatriatesinfor.setAdmissiontime(sff.parse(admissiontime));
                        }
                    }
                    if(value.size()>13)
                    {
                        expatriatesinfor.setSpeciality(Convert.toStr(value.get(13)));
                    }
                    if(value.size()>14)
                    {
                        String exits = Convert.toStr(value.get(14));
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
                            if(value.size()>15)
                            {
                                String exitime = Convert.toStr(value.get(15));
                                if(exitime!=null && exitime.length() > 9)
                                {
                                    exitime = exitime.trim().substring(0,10);
                                    expatriatesinfor.setExitime(sff.parse(exitime));
                                }
                            }
                            if(value.size()>16)
                            {
                                String exitreason = Convert.toStr(value.get(16));
                                if(exitreason!=null && !exitreason.isEmpty())
                                {
                                    if(exitreason.trim().equals("離職"))
                                    {
                                        expatriatesinfor.setExitreason("BP012001");
                                    }
                                    else if(exitreason.trim().equals("PJ完了"))
                                    {
                                        expatriatesinfor.setExitreason("BP012002");
                                    }
                                }
                            }
                            if(value.size()>17)
                            {
                                String alltechnology = Convert.toStr(value.get(17));
                                if(alltechnology!=null && !alltechnology.isEmpty())
                                {
                                    if(alltechnology.trim().equals("集成电路"))
                                    {
                                        expatriatesinfor.setAlltechnology("BP008001");
                                    }
                                    else if(alltechnology.trim().equals("Java"))
                                    {
                                        expatriatesinfor.setAlltechnology("BP008002");
                                    }
                                }
                            }
                            if(value.size()>18)
                            {
                                String sitevaluation = Convert.toStr(value.get(18));
                                if(sitevaluation!=null && !sitevaluation.isEmpty())
                                {
                                    if(sitevaluation.trim().equals("△"))
                                    {
                                        expatriatesinfor.setSitevaluation("BP009001");
                                    }
                                    else if(sitevaluation.trim().equals("○"))
                                    {
                                        expatriatesinfor.setSitevaluation("BP009002");
                                    }
                                }
                            }
                            if(value.size()>19)
                            {
                                String businessimpact = Convert.toStr(value.get(19));
                                if(businessimpact!=null && !businessimpact.isEmpty())
                                {
                                    if(businessimpact.trim().equals("低い"))
                                    {
                                        expatriatesinfor.setBusinessimpact("BP010001");
                                    }
                                    else if(businessimpact.trim().equals("無し"))
                                    {
                                        expatriatesinfor.setBusinessimpact("BP010002");
                                    }
                                    else if(businessimpact.trim().equals("なし"))
                                    {
                                        expatriatesinfor.setBusinessimpact("BP010003");
                                    }
                                }
                            }
                            if(value.size()>20)
                            {
                                String countermeasure = Convert.toStr(value.get(20));
                                if(countermeasure!=null && !countermeasure.isEmpty())
                                {
                                    if(countermeasure.trim().equals("対策不要"))
                                    {
                                        expatriatesinfor.setCountermeasure("BP011001");
                                    }
                                    else if(countermeasure.trim().equals("引継ぎ不要"))
                                    {
                                        expatriatesinfor.setCountermeasure("BP011002");
                                    }
                                }
                            }
                        }
                    }

                }
                expatriatesinfor.preInsert();
                expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
                expatriatesinforMapper.insert(expatriatesinfor);
                listVo.add(expatriatesinfor);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}



