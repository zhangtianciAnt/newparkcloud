package com.nt.service_pfans.PFANS6000.Impl;

import ch.qos.logback.core.joran.spi.ElementSelector;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Role;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS6000.*;
import com.nt.service_Org.DictionaryService;
import com.nt.service_pfans.PFANS6000.ExpatriatesinforService;
import com.nt.service_pfans.PFANS6000.mapper.*;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExpatriatesinforServiceImpl implements ExpatriatesinforService {

    @Autowired
    private ExpatriatesinforMapper expatriatesinforMapper;

    @Autowired
    private ExpatriatesinforDetailMapper expatriatesinforDetailMapper;

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

    //系统服务——add_fjl_0717  退场的第二天，账号不可登录  start
    @Scheduled(cron = "0 0 1 * * ?")
    public void updExpatriatesinforStatus() throws Exception {
        SimpleDateFormat st = new SimpleDateFormat("yyyyMMdd");
        int re = Integer.parseInt(st.format(new Date()));
        List<Expatriatesinfor> expatriatesinforList = expatriatesinforMapper.getExpatriatesinforexit();
        if (expatriatesinforList.size() > 0) {
            for (Expatriatesinfor ex : expatriatesinforList) {
                if (ex.getExitime() != null) {
                    if (re > Integer.parseInt(st.format(ex.getExitime()))) {
                        Query query = new Query();
                        query.addCriteria(Criteria.where("_id").is(ex.getAccount()));
                        query.addCriteria(Criteria.where("status").is("0"));
                        UserAccount usount = mongoTemplate.findOne(query, UserAccount.class);
                        if (usount != null) {
                            usount.setStatus("1");
                            mongoTemplate.save(usount);
                        }
                    }
                }
            }
        }
    }
    //系统服务——add_fjl_0717  退场的第二天，账号不可登录  end

    @Override
    public List<Expatriatesinfor> getexpatriatesinfor(Expatriatesinfor expatriatesinfor) throws Exception {
        // ceshi
//        List<Expatriatesinfor> list = new ArrayList<>();
//        list = expatriatesinforMapper.selectAll();
//        list = list.stream().filter(item -> (item.getOperationform() !=null && !item.getOperationform().equals(""))).collect(Collectors.toList());
//        list = list.stream().filter(item -> (item.getOperationform().equals("BP024001"))).collect(Collectors.toList());
//
//        for(Expatriatesinfor expatriatesinfor1: list)
//        {
//            Expatriatesinfor infor = new Expatriatesinfor();
//            infor = expatriatesinforMapper.selectByPrimaryKey(expatriatesinfor1.getExpatriatesinfor_id());
//            ExpatriatesinforDetail e =new ExpatriatesinforDetail();
//            TokenModel tokenModel = new TokenModel();
//            tokenModel.setUserId(infor.getAccount());
//            tokenModel.setExpireDate(new Date());
//            //登录新的履历
//            e.preInsert(tokenModel);
//            e.setExpatriatesinfordetail_id(UUID.randomUUID().toString());
//            e.setExpatriatesinfor_id(expatriatesinfor1.getExpatriatesinfor_id());
//            e.setGroup_id(expatriatesinfor1.getGroup_id());
//            if(expatriatesinfor1.getExitime() == null)
//            {
//                e.setExdatestr(expatriatesinfor1.getAdmissiontime());
//                e.setExdateend(expatriatesinfor1.getExitime());
//            }
//            else
//            {
//                e.setExdatestr(expatriatesinfor1.getAdmissiontime());
//            }
//
//
//            expatriatesinforDetailMapper.insert(e);
//        }

        //ceshi
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
    public List<ExpatriatesinforDetail> getGroupexpDetail(String expatriatesinfor_id) throws Exception {
        ExpatriatesinforDetail expatriatesinforDetail = new ExpatriatesinforDetail();
        expatriatesinforDetail.setExpatriatesinfor_id(expatriatesinfor_id);
        List<ExpatriatesinforDetail> list = new ArrayList<>();
        list = expatriatesinforDetailMapper.select(expatriatesinforDetail);
        list = list.stream().sorted(Comparator.comparing(ExpatriatesinforDetail::getExdatestr)).collect(Collectors.toList());
        return list;
    }

    @Override
    public void updateinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        //add ccm 0717 单价人名和员工信息名字不一致
        Priceset pri = new Priceset();
        pri.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
        List<Priceset> priexpnameList = pricesetMapper.select(pri);
        if (priexpnameList.size() > 0) {
            for (Priceset pr : priexpnameList) {
                pr.setUsername(expatriatesinfor.getExpname());
                pricesetMapper.updateByPrimaryKey(pr);
            }
        }
        //add ccm 0717 单价人名和员工信息名字不一致
        expatriatesinforMapper.updateByPrimaryKey(expatriatesinfor);
    }

    @Override
    public void crAccount(List<Expatriatesinfor> expatriatesinfor, TokenModel tokenModel) throws Exception {

        for (Expatriatesinfor item : expatriatesinfor) {
            if (StrUtil.isEmpty(item.getAccount())) {
                UserAccount userAccount = new UserAccount();
                userAccount.setAccount("KK-" + PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setPassword("KK-" + PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setUsertype("1");

                Query query = new Query();
                query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
                query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                List<UserAccount> list = mongoTemplate.find(query, UserAccount.class);

                if (list.size() > 0) {
                    userAccount = list.get(0);
                }
                query = new Query();
                query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
                query.addCriteria(Criteria.where("rolename").is("外协staff"));
                List<Role> rolss = mongoTemplate.find(query, Role.class);

                userAccount.setRoles(rolss);
                userAccount.preInsert(tokenModel);
                mongoTemplate.save(userAccount);

                item.setAccount(userAccount.get_id());
                expatriatesinforMapper.updateByPrimaryKeySelective(item);
            }
        }

    }
    //ADD CCM 20210317 NT_PFANS_20210309_BUG_182 FR
    public String selectByAccount(String account,String usertype,int count)throws Exception
    {
        String countString = "";
        Query query0 = new Query();
        query0.addCriteria(Criteria.where("account").is(account));
//        query0.addCriteria(Criteria.where("usertype").is(usertype));
        List<UserAccount> list = mongoTemplate.find(query0, UserAccount.class);
        if(list.size()>0)
        {
            countString = "00"+(count+list.size());
            if(list.size()+count>9)
            {
                countString = "0"+(list.size()+count);
            }
            return selectByAccount(account + countString,"0",list.size()+count);
        }
        return account;
    }
    //ADD CCM 20210317 NT_PFANS_20210309_BUG_182 TO
    @Override
    public void crAccount2(List<Expatriatesinfor> expatriatesinfor, TokenModel tokenModel) throws Exception {

        for (Expatriatesinfor item : expatriatesinfor) {
            if (StrUtil.isEmpty(item.getAccount())) {
                UserAccount userAccount = new UserAccount();
                userAccount.setAccount("KK-" + PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setPassword("KK-" + PinyinHelper.convertToPinyinString(item.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                userAccount.setUsertype("1");

                Query query = new Query();
                //UPD CCM 20210317 NT_PFANS_20210309_BUG_182 FR
//                query.addCriteria(Criteria.where("account").regex(userAccount.getAccount()));
////                query.addCriteria(Criteria.where("password").is(userAccount.getPassword()));
//                query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
//                List<UserAccount> list = mongoTemplate.find(query, UserAccount.class);
//
//                if (list.size() > 0) {
//                    userAccount.setAccount(userAccount.getAccount() + Convert.toStr(list.size()));
//                    userAccount.setPassword(userAccount.getAccount());
////                    userAccount = list.get(0);
//                }
                String account = userAccount.getAccount();
                account  = selectByAccount(account,"1",0);
                userAccount.setAccount(account);
                userAccount.setPassword(account);
                //UPD CCM 20210317 NT_PFANS_20210309_BUG_182 FR


                query = new Query();
                query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
                query.addCriteria(Criteria.where("rolename").is("外协员工"));
                List<Role> rolss = mongoTemplate.find(query, Role.class);

                userAccount.setRoles(rolss);
                userAccount.preInsert(tokenModel);
                mongoTemplate.save(userAccount);

                Expatriatesinfor exp = new Expatriatesinfor();
                exp.setExpatriatesinfor_id(item.getExpatriatesinfor_id());
                List<Expatriatesinfor> explist = expatriatesinforMapper.select(exp);
                if (explist.size() > 0) {
                    explist.get(0).setAccountname(userAccount.getAccount());
                    explist.get(0).setAccount(userAccount.get_id());
                    expatriatesinforMapper.updateByPrimaryKeySelective(explist.get(0));
                }
            } else {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(item.getAccount()));
                List<UserAccount> list = mongoTemplate.find(query, UserAccount.class);
                if (list.size() > 0) {
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
        if (!StringUtils.isNullOrEmpty(expatriatesinfor.getEmail())) {
            Expatriatesinfor e = new Expatriatesinfor();
            e.setEmail(expatriatesinfor.getEmail());
            list = expatriatesinforMapper.select(e);
            if (list.size() > 0) {
                if (!list.get(0).getExpname().equals(expatriatesinfor.getExpname())) {
                    throw new LogicalException("邮箱重复,请重新确认！");
                }
            }
        }
        expatriatesinfor.preUpdate(tokenModel);
        //退场时修改卡号
        if (expatriatesinfor.getExits().equals("0")) {
            expatriatesinfor.setNumber("00000");
        }
        //退场时修改卡号
        if (expatriatesinfor.getExits().equals("1")) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(expatriatesinfor.getAccount()));
            query.addCriteria(Criteria.where("status").is("1"));
            UserAccount userAccountInfo = mongoTemplate.findOne(query, UserAccount.class);
            if (userAccountInfo != null) {
                userAccountInfo.setStatus("0");
                mongoTemplate.save(userAccountInfo);
            }
        }

        //add ccm 20200605
        List<Expatriatesinfor> accountlist = new ArrayList<>();
        Expatriatesinfor ef = new Expatriatesinfor();
        if (expatriatesinfor.getAccount() == null || expatriatesinfor.getAccount().equals("")) {
            ef.setAccount("1");
        } else {
            ef.setAccount(expatriatesinfor.getAccount());
        }
        accountlist = expatriatesinforMapper.select(ef);
        if (accountlist.size() > 0) {
            if (!accountlist.get(0).getAccountname().equals(expatriatesinfor.getAccountname())) {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(accountlist.get(0).getAccount()));
                UserAccount userAcount = mongoTemplate.findOne(query, UserAccount.class);
                if (userAcount != null) {
                    Query q = new Query();
                    q.addCriteria(Criteria.where("account").is(expatriatesinfor.getAccountname()));
                    q.addCriteria(Criteria.where("usertype").is("1"));
                    List<UserAccount> list1 = mongoTemplate.find(q, UserAccount.class);
                    if (list1.size() > 1) {
                        userAcount.setAccount(userAcount.getAccount() + Convert.toStr(list1.size()));
                        userAcount.setPassword(userAcount.getAccount());
                    } else {
                        userAcount.setAccount(expatriatesinfor.getAccountname());
                        userAcount.setPassword(expatriatesinfor.getAccountname());
                    }
                    expatriatesinfor.setAccountname(userAcount.getAccount());
                    mongoTemplate.save(userAcount);
                }
            }
        }
        else {
            UserAccount userAccount = new UserAccount();
            userAccount.setAccount(expatriatesinfor.getAccountname());
            userAccount.setPassword(expatriatesinfor.getAccountname());
            userAccount.setUsertype("1");

            Query query = new Query();
            query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
            query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
            List<UserAccount> list1 = mongoTemplate.find(query, UserAccount.class);

            if (list1.size() > 1) {
                userAccount.setAccount(userAccount.getAccount() + Convert.toStr(list1.size()));
                userAccount.setPassword(userAccount.getAccount());
            }

            query = new Query();
            query.addCriteria(Criteria.where("status").is(AuthConstants.DEL_FLAG_NORMAL));
            query.addCriteria(Criteria.where("rolename").is("外协员工"));
            List<Role> rolss = mongoTemplate.find(query, Role.class);

            userAccount.setRoles(rolss);
            userAccount.preInsert(tokenModel);
            mongoTemplate.save(userAccount);

            expatriatesinfor.setAccountname(userAccount.getAccount());
            expatriatesinfor.setAccount(userAccount.get_id());
        }
        //add ccm 20200605
        if (expatriatesinfor.getGroup_id() == null || expatriatesinfor.getGroup_id().equals("")) {
            expatriatesinfor.setGroup_id(expatriatesinfor.getInterviewdep());
        }

        //add ccm 20201212
        //部门转移,上个月单价复制到新部门 ztc 1118 start
        //取上个月 lastmonthAntStr
        String thisDate = DateUtil.format(new Date(), "yyyy-MM-dd");
        LocalDate monthAntDate = LocalDate.now();
        monthAntDate = monthAntDate.minusMonths(1);
        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM");
        String lastmonthAntStr = formatters.format(monthAntDate);
        //当月 nowmonthAntStr
        String nowmonthAntStr = formatters.format(LocalDate.now());

        //外驻退场时间 admissiontime
        SimpleDateFormat sdfAnt = new SimpleDateFormat("yyyy-MM");
        String admissiontime = sdfAnt.format(expatriatesinfor.getAdmissiontime());

        //外驻人员旧数据
        Expatriatesinfor expatriatesOld = expatriatesinforMapper.selectByPrimaryKey(expatriatesinfor.getExpatriatesinfor_id());


        //if判断条件：1.部门不同（必要条件）2.在职外驻。
        // //3.入场月份不是当月（入场月份是当月必定没上月单价）
        if (!expatriatesinfor.getGroup_id().equals(expatriatesOld.getGroup_id())
                && expatriatesinfor.getExits().equals("1")
//                && !admissiontime.equals(nowmonthAntStr)
            )
        {
            //查询上月PricesetGroup主键 pricesetgroupid
            PricesetGroup pricesetGroup = new PricesetGroup();
            pricesetGroup.setPd_date(lastmonthAntStr);
            List<PricesetGroup> pricesetGroupList = pricesetGroupMapper.select(pricesetGroup);
            //上个月单价月份id
            String pricesetgroupid = pricesetGroupList.get(0).getPricesetgroup_id();

            pricesetGroup.setPd_date(nowmonthAntStr);
            List<PricesetGroup> pricesetGroupList2 = pricesetGroupMapper.select(pricesetGroup);
            //本月单价月份id
            String pricesetgroupidnew = pricesetGroupList2.get(0).getPricesetgroup_id();

            //查询旧部门上个月该员工设定的单价 pricesetListOld
            Priceset priceset = new Priceset();
            priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            priceset.setPricesetgroup_id(pricesetgroupid);
            priceset.setGroup_id(expatriatesOld.getGroup_id());
            List<Priceset> pricesetListOld = pricesetMapper.select(priceset);

            //查询旧部门本月该员工设定的单价 pricesetListnew2
            Priceset priceset2 = new Priceset();
            priceset2.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            priceset2.setPricesetgroup_id(pricesetgroupidnew);
            priceset2.setGroup_id(expatriatesOld.getGroup_id());
            List<Priceset> pricesetListnew2 = pricesetMapper.select(priceset2);

            //查询新部门上个月员工设定的单价 pricesetListNew
            Priceset priceset1 = new Priceset();
            priceset1.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            priceset1.setPricesetgroup_id(pricesetgroupid);
            priceset1.setGroup_id(expatriatesinfor.getGroup_id());
            List<Priceset> pricesetListNew = pricesetMapper.select(priceset1);

            //查询新部门本月员工设定的单价 pricesetListNewing
            Priceset pricesetnewing = new Priceset();
            pricesetnewing.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            pricesetnewing.setPricesetgroup_id(pricesetgroupidnew);
            pricesetnewing.setGroup_id(expatriatesinfor.getGroup_id());
            List<Priceset> pricesetListNewing = pricesetMapper.select(pricesetnewing);

            //当月新部门单价不存在
            Priceset pricesetnew = new Priceset();
            if(pricesetListNewing.size()==0)
            {
                //当月旧部门单价不存在
                if(pricesetListnew2.size()==0)
                {
                    //上月新部门单价不存在
                    if(pricesetListNew.size() == 0)
                    {
                        //上月旧部门单价不存在
                        if(pricesetListOld.size() == 0)
                        {
                            pricesetnew.preInsert(tokenModel);
                            pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                            pricesetnew.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
                            //当月新部门
                            pricesetnew.setGroup_id(expatriatesinfor.getGroup_id());
                            pricesetnew.setGraduation(expatriatesinfor.getGraduation_year());
                            pricesetnew.setCompany(expatriatesinfor.getSuppliername());
                            pricesetnew.setAssesstime(thisDate);
                            pricesetnew.setUsername(expatriatesinfor.getExpname());
                            pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                            pricesetnew.setStatus("0");
                            pricesetMapper.insert(pricesetnew);

                            //当月旧部门
                            pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                            pricesetnew.setGroup_id(expatriatesOld.getGroup_id());
                            pricesetMapper.insert(pricesetnew);
                        }
                        else
                        {
                            pricesetnew = pricesetListOld.get(0);
                            //新部门
                            pricesetnew.setGroup_id(expatriatesinfor.getGroup_id());
                            pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                            pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                            pricesetMapper.insert(pricesetnew);
                        }

                        pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                        pricesetnew.setPricesetgroup_id(pricesetgroupid);
                        pricesetMapper.insert(pricesetnew);
                    }
                    else
                    {
                        //当月新部门
                        pricesetnew = pricesetListNew.get(0);
                        pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                        pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                        pricesetMapper.insert(pricesetnew);

                        pricesetnew = pricesetListNew.get(0);
                        //旧部门 当月
                        pricesetnew.setGroup_id(expatriatesOld.getGroup_id());
                        pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                        pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                        pricesetMapper.insert(pricesetnew);
                    }
                }
                else
                {
                    pricesetnew = pricesetListnew2.get(0);
                    //当月新部门
                    pricesetnew.setGroup_id(expatriatesinfor.getGroup_id());
                    pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                    pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                    pricesetMapper.insert(pricesetnew);

                    //上月新部门
                    if(pricesetListNew.size() == 0)
                    {
                        pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                        pricesetnew.setPricesetgroup_id(pricesetgroupid);
                        pricesetMapper.insert(pricesetnew);
                    }
                }
            }

            //上月新部门单价不存在创建
//            if(pricesetListNew.size()==0)
//            {
//                pricesetListNewing = pricesetMapper.select(pricesetnewing);
//                pricesetnew = pricesetListNewing.get(0);
//                pricesetnew.setPriceset_id(UUID.randomUUID().toString());
//                pricesetnew.setPricesetgroup_id(pricesetgroupid);
//                pricesetMapper.insert(pricesetnew);
//            }

            /*
            if (pricesetListNew.size() == 0) { //新部门上个月单价为0 从旧部门中取
                if (pricesetListnew2.size() == 0) { //该员工旧部门本月没有单价
                    if (pricesetListOld.size() == 0) { //该员工旧部门上个月没有单价 新建
                        pricesetnew.preInsert(tokenModel);
                        pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                        pricesetnew.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
                        //新部门
                        pricesetnew.setGroup_id(expatriatesinfor.getGroup_id());
                        pricesetnew.setGraduation(expatriatesinfor.getGraduation_year());
                        pricesetnew.setCompany(expatriatesinfor.getSuppliername());
                        pricesetnew.setAssesstime(thisDate);
                        pricesetnew.setUsername(expatriatesinfor.getExpname());
                        pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                        pricesetnew.setStatus("0");
                        pricesetMapper.insert(pricesetnew);
                    } else { //旧部门上个月有单价
                        pricesetnew = pricesetListOld.get(0);
                        pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                        //新部门
                        pricesetnew.setGroup_id(expatriatesinfor.getGroup_id());
                        pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                        pricesetMapper.insert(pricesetnew);
                    }
                }else{ //该员工旧部门本月有单价
                    pricesetnew = pricesetListnew2.get(0);
                    pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                    pricesetnew.setGroup_id(expatriatesinfor.getGroup_id());
                    pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                    pricesetMapper.insert(pricesetnew);
                }
            }
            else { //新部门上个月有单价
                pricesetnew = pricesetListNew.get(0);
                pricesetnew.setPriceset_id(UUID.randomUUID().toString());
                pricesetnew.setPricesetgroup_id(pricesetgroupidnew);
                pricesetMapper.insert(pricesetnew);
            }
            */

        }
        else if(expatriatesinfor.getExits().equals("1"))
        {
            PricesetGroup pricesetGroup = new PricesetGroup();
            //查询当月是否存在
            pricesetGroup.setPd_date(nowmonthAntStr);
            List<PricesetGroup> pricesetGroupListafter = pricesetGroupMapper.select(pricesetGroup);
            //查询上月月是否存在
            pricesetGroup.setPd_date(lastmonthAntStr);
            List<PricesetGroup> pricesetGroupListbefore = pricesetGroupMapper.select(pricesetGroup);

            //当月id
            String pricesetgroupidafter = pricesetGroupListafter.get(0).getPricesetgroup_id();
            //上月id
            String pricesetgroupidbefore = pricesetGroupListbefore.get(0).getPricesetgroup_id();

            Priceset priceset = new Priceset();
            //当月当前部门
            priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            priceset.setPricesetgroup_id(pricesetgroupidafter);
            priceset.setGroup_id(expatriatesinfor.getGroup_id());
            List<Priceset> pricesetListafter = pricesetMapper.select(priceset);

            //上月当前部门
            priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
            priceset.setPricesetgroup_id(pricesetgroupidbefore);
            priceset.setGroup_id(expatriatesinfor.getGroup_id());
            List<Priceset> pricesetListbefore = pricesetMapper.select(priceset);

            //if当月当前部门不存在单价
            //else当月当前部门存在单价
            if(pricesetListafter.size()==0)
            {
                //if上月当前部门不存在
                //else上月当前部门存在
                if(pricesetListbefore.size()==0)
                {
                    //当月当前部门
                    priceset.preInsert(tokenModel);
                    priceset.setPriceset_id(UUID.randomUUID().toString());
                    priceset.setUser_id(expatriatesinfor.getExpatriatesinfor_id());
                    priceset.setGroup_id(expatriatesinfor.getGroup_id());
                    priceset.setGraduation(expatriatesinfor.getGraduation_year());
                    priceset.setCompany(expatriatesinfor.getSuppliername());
                    priceset.setAssesstime(thisDate);
                    priceset.setUsername(expatriatesinfor.getExpname());
                    priceset.setPricesetgroup_id(pricesetgroupidafter);
                    priceset.setStatus("0");
                    pricesetMapper.insert(priceset);

                    //上月当前部门
                    priceset.preInsert(tokenModel);
                    priceset.setPriceset_id(UUID.randomUUID().toString());
                    priceset.setPricesetgroup_id(pricesetgroupidbefore);
                    pricesetMapper.insert(priceset);
                }
                else
                {
                    //当月当前部门
                    pricesetListbefore.get(0).preInsert(tokenModel);
                    pricesetListbefore.get(0).setPriceset_id(UUID.randomUUID().toString());
                    pricesetListbefore.get(0).setPricesetgroup_id(pricesetgroupidafter);
                    pricesetMapper.insert(pricesetListbefore.get(0));
                }
            }
            else
            {
                //if上月当前部门不存在
                //else上月当前部门存在
                if(pricesetListbefore.size()==0)
                {
                    //上月当前部门
                    pricesetListafter.get(0).preInsert(tokenModel);
                    pricesetListafter.get(0).setPriceset_id(UUID.randomUUID().toString());
                    pricesetListafter.get(0).setPricesetgroup_id(pricesetgroupidbefore);
                    pricesetMapper.insert(pricesetListafter.get(0));
                }
            }
        }
        //部门转移,上个月单价复制到新部门 ztc 1118 end


        //添加部门履历
        if (!expatriatesinfor.getGroup_id().equals("")) {
            ExpatriatesinforDetail e = new ExpatriatesinforDetail();
            e.setExpatriatesinfor_id(expatriatesinfor.getExpatriatesinfor_id());
            List<ExpatriatesinforDetail> expatriatesinforDetails = new ArrayList<>();
            expatriatesinforDetails = expatriatesinforDetailMapper.select(e);
            if (expatriatesinforDetails.size() == 0) {
                //登录新的履历
                ExpatriatesinforDetail e1 = new ExpatriatesinforDetail();
                e1.preInsert(tokenModel);
                e1.setGroup_id(expatriatesinfor.getGroup_id());
                e1.setExpatriatesinfordetail_id(UUID.randomUUID().toString());
                e1.setExpatriatesinfor_id(expatriatesinfor.getExpatriatesinfor_id());
                e1.setExdatestr(new Date());
                expatriatesinforDetailMapper.insert(e1);

            } else {
                for (ExpatriatesinforDetail expDetail : expatriatesinforDetails) {
                    if (expDetail.getExdateend() == null) {
                        if (!expDetail.getGroup_id().equals(expatriatesinfor.getGroup_id())) {
                            //更新结束时间
                            expDetail.setExdateend(new Date());
                            expDetail.preUpdate(tokenModel);
                            expatriatesinforDetailMapper.updateByPrimaryKey(expDetail);

                            //登录新的履历
                            ExpatriatesinforDetail e2 = new ExpatriatesinforDetail();
                            e2.preInsert(tokenModel);
                            e2.setGroup_id(expatriatesinfor.getGroup_id());
                            e2.setExpatriatesinfordetail_id(UUID.randomUUID().toString());
                            e2.setExpatriatesinfor_id(expDetail.getExpatriatesinfor_id());
                            e2.setExdatestr(new Date());
                            expatriatesinforDetailMapper.insert(e2);
                        }
                    }
                }
            }
        }
        expatriatesinforMapper.updateByPrimaryKey(expatriatesinfor);
    }

    @Override
    public void createexpatriatesinforApply(Expatriatesinfor expatriatesinfor, TokenModel tokenModel) throws Exception {
        expatriatesinfor.preInsert(tokenModel);
        expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
        expatriatesinfor.setExits("1");
        if (expatriatesinfor.getGroup_id() == null || expatriatesinfor.getGroup_id().equals("")) {
            expatriatesinfor.setGroup_id(expatriatesinfor.getInterviewdep());
        }
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
            model.add("供应商中文名称");
            model.add("Rn");
            model.add("邮箱地址");
            model.add("部门");
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
            model.add("更衣柜号");
            List<Object> key = list.get(0);
//           上传模板与标准模板 校验

            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().replace("●", "").trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }

            List<Integer> updint = new ArrayList<Integer>();
            //resultInsUpd true:插入 false:更新
            boolean resultInsUpd = true;
            for (int j = 0; j < key.size(); j++) {
                if (key.get(j).toString().trim().contains("●")) {
                    resultInsUpd = false;
                    updint.add(j);
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            if (resultInsUpd) {
                for (int i = 1; i < list.size(); i++) {
                    Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                    List<Object> value = list.get(k);
                    k++;
                    if (value != null && !value.isEmpty()) {
                        if (value.size() > 12) {
                            if (value.size() > 0) {
                                expatriatesinfor.setExpname(Convert.toStr(value.get(0)));
                                if (expatriatesinfor.getExpname() != null && expatriatesinfor.getExpname().trim().length() > 36) {
                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 长度超长，最大长度为36");
                                }
                                Expatriatesinfor expatriatesinforName = new Expatriatesinfor();
                                expatriatesinforName.setExpname(expatriatesinfor.getExpname().trim());
                                List<Expatriatesinfor> expatriatesinforList = new ArrayList<Expatriatesinfor>();
                                expatriatesinforList = expatriatesinforMapper.select(expatriatesinforName);

                                //UPD CCM 20210317 NT_PFANS_20210309_BUG_182 FR
//                                if (expatriatesinforList.size() > 0) {
//                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
//                                } else {
//                                    UserAccount userAccount = new UserAccount();
//                                    userAccount.setAccount("KK-" + PinyinHelper.convertToPinyinString(expatriatesinfor.getExpname(), "", PinyinFormat.WITHOUT_TONE));
//                                    userAccount.setUsertype("1");
//                                    Query query = new Query();
//                                    query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
//                                    query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
//                                    List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
//                                    if (userAccountlist.size() > 0) {
//                                        throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
//                                    }
//                                }

                                if(expatriatesinforList.size() > 0)
                                {
                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
                                }
                                else
                                {
                                    if(!com.nt.utils.StringUtils.isBase64Encode(expatriatesinforName.getExpname())){
                                        expatriatesinforName.setExpname(Base64.encode(expatriatesinforName.getExpname()));
                                    }
                                    expatriatesinforList = expatriatesinforMapper.select(expatriatesinforName);
                                    if (expatriatesinforList.size() > 0) {
                                        throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
                                    } else {
                                        UserAccount userAccount = new UserAccount();
                                        userAccount.setAccount("KK-" + PinyinHelper.convertToPinyinString(expatriatesinfor.getExpname(), "", PinyinFormat.WITHOUT_TONE));
                                        userAccount.setUsertype("1");
                                        Query query = new Query();
                                        query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                                        query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                                        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                                        if (userAccountlist.size() > 0) {
                                            throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                                        }
                                    }
                                }
                                //UPD CCM 20210317 NT_PFANS_20210309_BUG_182 TO
                            }
                            if (value.size() > 1) {
                                String sex = Convert.toStr(value.get(1));
                                if (sex != null && !sex.isEmpty()) {
                                    Dictionary dictionary = new Dictionary();
                                    dictionary.setValue1(sex.trim());
                                    dictionary.setType("GT");
                                    dictionary.setPcode("PR019");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if (dictionaryList.size() > 0) {
                                        expatriatesinfor.setSex(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if (value.size() > 2) {
                                String birth = Convert.toStr(value.get(2));
                                if (birth != null && birth.length() > 9) {
                                    birth = birth.trim().substring(0, 10);
                                    expatriatesinfor.setBirth(sff.format(sff.parse(birth)));
                                }
                            }
                            if (value.size() > 3) {
                                expatriatesinfor.setGraduateschool(Convert.toStr(value.get(3)));
                            }
                            if (value.size() > 4) {
                                String education = Convert.toStr(value.get(4));
                                if (education != null && !education.isEmpty()) {
                                    Dictionary dictionary = new Dictionary();
                                    dictionary.setValue1(education.trim());
                                    dictionary.setType("RS");
                                    dictionary.setPcode("PR022");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if (dictionaryList.size() > 0) {
                                        expatriatesinfor.setEducation(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if (value.size() > 5) {
                                expatriatesinfor.setContactinformation(Convert.toStr(value.get(5)));
                            }
                            if (value.size() > 6) {
                                String graduation_year = Convert.toStr(value.get(6));
                                if (graduation_year != null && graduation_year.length() > 3) {
                                    graduation_year = graduation_year.trim().substring(0, 4);
                                    expatriatesinfor.setGraduation_year(sf.parse(graduation_year));
                                }
                            }
                            if (value.size() > 7) {
                                String suppliername = Convert.toStr(value.get(7));
                                Supplierinfor supplierinfor = new Supplierinfor();
                                supplierinfor.setSupchinese(suppliername);
                                List<Supplierinfor> supplierinforList = new ArrayList<>();
                                supplierinforList = supplierinforMapper.select(supplierinfor);
                                if (supplierinforList.size() > 0) {
                                    expatriatesinfor.setSuppliername(supplierinforList.get(0).getSupchinese());
                                    expatriatesinfor.setSupplierinfor_id(supplierinforList.get(0).getSupplierinfor_id());
                                } else {
                                    throw new LogicalException("第" + i + "行的供应商名称不存在，请确认。");
                                }
                            }
                            if (value.size() > 8) {
                                String rn = Convert.toStr(value.get(8));
                                if (rn != null && !rn.isEmpty()) {
                                    Dictionary dictionary = new Dictionary();
                                    dictionary.setValue1(rn.trim());
                                    dictionary.setType("RS");
                                    dictionary.setPcode("PR021");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if (dictionaryList.size() > 0) {
                                        expatriatesinfor.setRn(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if (value.size() > 9) {
                                expatriatesinfor.setEmail(Convert.toStr(value.get(9)));
                                if (expatriatesinfor.getEmail() != null && expatriatesinfor.getEmail().length() > 255) {
                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 邮箱 长度超长，最大长度为255");
                                }
                            }
                            if (value.size() > 10) {
                                String groupName = Convert.toStr(value.get(10));
                                if (groupName != null && !groupName.isEmpty()) {
                                    Query query = new Query();
                                    query.addCriteria(Criteria.where("userinfo.centername").is(groupName));
                                    List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                    customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                                    if (customerInfoList.size() > 0) {
                                        expatriatesinfor.setGroup_id(customerInfoList.get(0).getUserinfo().getCenterid());
                                    } else {
                                        throw new LogicalException("第" + i + "行的部门名称不存在，请确认。");
                                    }
                                } else {
                                    throw new LogicalException("第" + i + "行的部门名称不能为空，请确认。");
                                }
                            }
                            if (value.size() > 11) {
                                String operationform = Convert.toStr(value.get(11));
                                if (operationform != null && !operationform.isEmpty()) {
                                    Dictionary dictionary = new Dictionary();
                                    dictionary.setValue1(operationform.trim());
                                    dictionary.setType("BP");
                                    dictionary.setPcode("BP024");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if (dictionaryList.size() > 0) {
                                        expatriatesinfor.setOperationform(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if (value.size() > 12) {
                                expatriatesinfor.setNumber(Convert.toStr(value.get(12)));
                                if (expatriatesinfor.getNumber() == null || expatriatesinfor.getNumber().isEmpty()) {
                                    throw new LogicalException("第" + i + "行 卡号 不能为空，请确认。");
                                }
                                if (expatriatesinfor.getNumber() != null && expatriatesinfor.getNumber().length() > 36) {
                                    throw new LogicalException("第" + i + "行 卡号 长度超长，最大长度为36");
                                }
                                Expatriatesinfor expatriatesinforName = new Expatriatesinfor();
                                expatriatesinforName.setNumber(expatriatesinfor.getNumber().trim());
                                List<Expatriatesinfor> expatriatesinforList = new ArrayList<Expatriatesinfor>();
                                expatriatesinforList = expatriatesinforMapper.select(expatriatesinforName);
                                if (expatriatesinforList.size() > 0) {
                                    throw new LogicalException("第" + i + "行的卡号在外驻人员表中已有人使用，请确认。");
                                }
                            }
                            if (value.size() > 13) {
                                String jobclassification = Convert.toStr(value.get(13));
                                if (jobclassification != null && !jobclassification.isEmpty()) {
                                    Dictionary dictionary = new Dictionary();
                                    dictionary.setValue1(jobclassification.trim());
                                    dictionary.setType("BP");
                                    dictionary.setPcode("BP025");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if (dictionaryList.size() > 0) {
                                        expatriatesinfor.setJobclassification(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if (value.size() > 14) {
                                expatriatesinfor.setSpeciality(Convert.toStr(value.get(14)));
                            }
                            if (value.size() > 15) {
                                String interviewdep = Convert.toStr(value.get(15));
                                if (interviewdep != null && !interviewdep.isEmpty()) {
                                    Query query = new Query();
                                    query.addCriteria(Criteria.where("userinfo.centername").is(interviewdep));
                                    List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                    customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                                    if (customerInfoList.size() > 0) {
                                        expatriatesinfor.setInterviewdep(customerInfoList.get(0).getUserinfo().getCenterid());
                                        if (expatriatesinfor.getGroup_id() == null || expatriatesinfor.getGroup_id().equals("")) {
                                            expatriatesinfor.setGroup_id(customerInfoList.get(0).getUserinfo().getCenterid());
                                        }
                                    } else {
                                        expatriatesinfor.setInterviewdep(interviewdep);
                                        if (expatriatesinfor.getGroup_id() == null || expatriatesinfor.getGroup_id().equals("")) {
                                            expatriatesinfor.setGroup_id(interviewdep);
                                        }
                                    }
                                } else {
                                    throw new LogicalException("第" + i + "行的面试部门不能为空，请确认。");
                                }
                            }
                            if (value.size() > 16) {
                                String interview_date = Convert.toStr(value.get(16));
                                if (interview_date != null && interview_date.length() > 9) {
                                    interview_date = interview_date.trim().substring(0, 10);
                                    expatriatesinfor.setInterview_date(interview_date);
                                }
                            }
                            if (value.size() > 17) {
                                String result = Convert.toStr(value.get(17));
                                if (result != null && !result.isEmpty()) {
                                    Dictionary dictionary = new Dictionary();
                                    dictionary.setValue1(result.trim());
                                    dictionary.setType("BP");
                                    dictionary.setPcode("BP003");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if (dictionaryList.size() > 0) {
                                        expatriatesinfor.setResult(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if (value.size() > 18) {
                                String whetherentry = Convert.toStr(value.get(18));
                                if (whetherentry != null && !whetherentry.isEmpty()) {
                                    Dictionary dictionary = new Dictionary();
                                    dictionary.setValue1(whetherentry.trim());
                                    dictionary.setType("BP");
                                    dictionary.setPcode("BP006");
                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                    if (dictionaryList.size() > 0) {
                                        expatriatesinfor.setWhetherentry(dictionaryList.get(0).getCode());
                                    }
                                }
                            }
                            if (value.size() > 19) {
                                String admissiontime = Convert.toStr(value.get(19));
                                if (admissiontime != null && admissiontime.length() > 9) {
                                    admissiontime = admissiontime.trim().substring(0, 10);
                                    expatriatesinfor.setAdmissiontime(sff.parse(admissiontime));
                                }
                            }
                            if (value.size() > 20) {
                                String exits = Convert.toStr(value.get(20));
                                if (exits != null && !exits.isEmpty()) {
                                    if (exits.trim().equals("是")) {
                                        expatriatesinfor.setExits("0");
                                    } else if (exits.trim().equals("否")) {
                                        expatriatesinfor.setExits("1");
                                    }
                                }
                                if (expatriatesinfor.getExits().equals("0")) {
                                    if (value.size() > 21) {
                                        String exitime = Convert.toStr(value.get(21));
                                        if (exitime != null && exitime.length() > 9) {
                                            exitime = exitime.trim().substring(0, 10);
                                            expatriatesinfor.setExitime(sff.parse(exitime));
                                        }
                                    }
                                    if (value.size() > 22) {
                                        String exitreason = Convert.toStr(value.get(22));
                                        if (exitreason != null && !exitreason.isEmpty()) {
                                            Dictionary dictionary = new Dictionary();
                                            dictionary.setValue1(exitreason.trim());
                                            dictionary.setType("BP");
                                            dictionary.setPcode("BP012");
                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                            if (dictionaryList.size() > 0) {
                                                expatriatesinfor.setExitreason(dictionaryList.get(0).getCode());
                                            }
                                        }
                                    }
                                    if (value.size() > 23) {
                                        String alltechnology = Convert.toStr(value.get(23));
                                        if (alltechnology != null && !alltechnology.isEmpty()) {
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
                                    if (value.size() > 24) {
                                        String sitevaluation = Convert.toStr(value.get(24));
                                        if (sitevaluation != null && !sitevaluation.isEmpty()) {
                                            Dictionary dictionary = new Dictionary();
                                            dictionary.setValue1(sitevaluation.trim());
                                            dictionary.setType("BP");
                                            dictionary.setPcode("BP009");
                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                            if (dictionaryList.size() > 0) {
                                                expatriatesinfor.setSitevaluation(dictionaryList.get(0).getCode());
                                            }
                                        }
                                    }
                                    if (value.size() > 25) {
                                        String businessimpact = Convert.toStr(value.get(25));
                                        if (businessimpact != null && !businessimpact.isEmpty()) {
                                            Dictionary dictionary = new Dictionary();
                                            dictionary.setValue1(businessimpact.trim());
                                            dictionary.setType("BP");
                                            dictionary.setPcode("BP010");
                                            List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                            if (dictionaryList.size() > 0) {
                                                expatriatesinfor.setBusinessimpact(dictionaryList.get(0).getCode());
                                            }
                                        }
                                    }
                                    if (value.size() > 26) {
                                        String countermeasure = Convert.toStr(value.get(26));
                                        if (countermeasure != null && !countermeasure.isEmpty()) {
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
                            if (value.size() > 27) {
                                expatriatesinfor.setRemarks(Convert.toStr(value.get(27)));
                            }
                            if (value.size() > 28) {
                                expatriatesinfor.setLockernumber(Convert.toStr(value.get(28)));
                            }
                        } else {
                            throw new LogicalException("第" + i + "行 卡号 不能为空，请确认。");
                        }
                    }
                    expatriatesinfor.preInsert();
                    expatriatesinfor.setExpatriatesinfor_id(UUID.randomUUID().toString());
                    expatriatesinforMapper.insert(expatriatesinfor);
                    //登录新的履历
                    if (!expatriatesinfor.getGroup_id().isEmpty() && expatriatesinfor.getGroup_id() != null) {
                        ExpatriatesinforDetail e2 = new ExpatriatesinforDetail();
                        e2.preInsert(tokenModel);
                        e2.setGroup_id(expatriatesinfor.getGroup_id());
                        e2.setExpatriatesinfordetail_id(UUID.randomUUID().toString());
                        e2.setExpatriatesinfor_id(expatriatesinfor.getExpatriatesinfor_id());
                        e2.setExdatestr(new Date());
                        expatriatesinforDetailMapper.insert(e2);
                    }
                    listVo.add(expatriatesinfor);
                    accesscount = accesscount + 1;
                }
            } else {
                for (int i = 1; i < list.size(); i++) {
                    Expatriatesinfor expatriatesinfor = new Expatriatesinfor();
                    List<Object> value = list.get(i);
                    if (value != null && !value.isEmpty() && value.size() > 12) {
                        expatriatesinfor.setNumber(Convert.toStr(value.get(12)));
                        List<Expatriatesinfor> expatriatesinforList = new ArrayList<Expatriatesinfor>();
                        expatriatesinforList = expatriatesinforMapper.select(expatriatesinfor);
                        if (expatriatesinforList.size() > 0) {
                            for (int j = 0; j < updint.size(); j++) {
                                if (value.size() > updint.get(j)) {
                                    switch (updint.get(j)) {
                                        case 0:
                                            String name = Convert.toStr(value.get(0));

                                            if (name != null && name.trim().length() > 36) {
                                                throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 长度超长，最大长度为36");
                                            }
                                            if (!(name.equals(expatriatesinforList.get(0).getExpname()))) {
                                                Expatriatesinfor expatriatesinforName = new Expatriatesinfor();
                                                expatriatesinforList.get(0).setExpname(Convert.toStr(value.get(0)));
                                                expatriatesinforName.setExpname(expatriatesinforList.get(0).getExpname().trim());
                                                List<Expatriatesinfor> inforList = new ArrayList<Expatriatesinfor>();
                                                inforList = expatriatesinforMapper.select(expatriatesinforName);
                                                //UPD CCM 20210317 NT_PFANS_20210309_BUG_182 FR
//                                                if (inforList.size() > 0) {
//                                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
//                                                } else {
//                                                    UserAccount userAccount = new UserAccount();
//                                                    userAccount.setAccount("KK-" + PinyinHelper.convertToPinyinString(expatriatesinforList.get(0).getExpname(), "", PinyinFormat.WITHOUT_TONE));
//                                                    userAccount.setUsertype("1");
//                                                    Query query = new Query();
//                                                    query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
//                                                    query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
//                                                    List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
//                                                    if (userAccountlist.size() > 0) {
//                                                        throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
//                                                    }
//                                                }
                                                if(inforList.size() > 0)
                                                {
                                                    throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
                                                }
                                                else
                                                {
                                                    if(!com.nt.utils.StringUtils.isBase64Encode(expatriatesinforName.getExpname())){
                                                        expatriatesinforName.setExpname(Base64.encode(expatriatesinforName.getExpname()));
                                                    }
                                                    inforList = expatriatesinforMapper.select(expatriatesinforName);
                                                    if (inforList.size() > 0) {
                                                        throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在，生成登陆账号时会重复，请确认。");
                                                    } else {
                                                        UserAccount userAccount = new UserAccount();
                                                        userAccount.setAccount("KK-" + PinyinHelper.convertToPinyinString(expatriatesinforList.get(0).getExpname(), "", PinyinFormat.WITHOUT_TONE));
                                                        userAccount.setUsertype("1");
                                                        Query query = new Query();
                                                        query.addCriteria(Criteria.where("account").is(userAccount.getAccount()));
                                                        query.addCriteria(Criteria.where("usertype").is(userAccount.getUsertype()));
                                                        List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                                                        if (userAccountlist.size() > 0) {
                                                            throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 姓名 在外驻人员表中已存在同音的员工，生成登陆账号时会重复，请确认。");
                                                        }
                                                    }
                                                }
                                                //UPD CCM 20210317 NT_PFANS_20210309_BUG_182 TO
                                            }
                                            break;
                                        case 1:
                                            String sex = Convert.toStr(value.get(1));
                                            if (sex != null && !sex.isEmpty()) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(sex.trim());
                                                dictionary.setType("GT");
                                                dictionary.setPcode("PR019");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
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
                                            if (education != null && !education.isEmpty()) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(education.trim());
                                                dictionary.setType("RS");
                                                dictionary.setPcode("PR022");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    expatriatesinforList.get(0).setEducation(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 5:
                                            expatriatesinforList.get(0).setContactinformation(Convert.toStr(value.get(5)));
                                            break;
                                        case 6:
                                            String graduation_year = Convert.toStr(value.get(6));
                                            if (graduation_year != null && graduation_year.length() > 3) {
                                                graduation_year = graduation_year.trim().substring(0, 4);
                                                expatriatesinforList.get(0).setGraduation_year(sf.parse(graduation_year));
                                            }
                                            break;
                                        case 7:
                                            String suppliername = Convert.toStr(value.get(7));
                                            Supplierinfor supplierinfor = new Supplierinfor();
                                            supplierinfor.setSupchinese(suppliername);
                                            List<Supplierinfor> supplierinforList = new ArrayList<>();
                                            supplierinforList = supplierinforMapper.select(supplierinfor);
                                            if (supplierinforList.size() > 0) {
                                                expatriatesinforList.get(0).setSuppliername(supplierinforList.get(0).getSupchinese());
                                                expatriatesinforList.get(0).setSupplierinfor_id(supplierinforList.get(0).getSupplierinfor_id());
                                            } else {
                                                expatriatesinforList.get(0).setSuppliername(suppliername);
                                            }
                                            break;
                                        case 8:
                                            String rn = Convert.toStr(value.get(8));
                                            if (rn != null && !rn.isEmpty()) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(rn.trim());
                                                dictionary.setType("RS");
                                                dictionary.setPcode("PR021");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    expatriatesinforList.get(0).setRn(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 9:
                                            expatriatesinforList.get(0).setEmail(Convert.toStr(value.get(9)));
                                            if (expatriatesinforList.get(0).getEmail() != null && expatriatesinforList.get(0).getEmail().length() > 255) {
                                                throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "对应的 邮箱 长度超长，最大长度为255");
                                            }
                                            break;
                                        case 10:
                                            String groupName = Convert.toStr(value.get(10));
                                            if (groupName != null && !groupName.isEmpty()) {
                                                Query query = new Query();
                                                query.addCriteria(Criteria.where("userinfo.centername").is(groupName));
                                                List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                                customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                                                if (customerInfoList.size() > 0) {
                                                    expatriatesinforList.get(0).setGroup_id(customerInfoList.get(0).getUserinfo().getCenterid());
                                                } else {
                                                    throw new LogicalException("第" + i + "行的部门名称不存在，请确认。");
                                                }
                                            } else {
                                                throw new LogicalException("第" + i + "行的部门名称不能为空，请确认。");
                                            }
                                            break;
                                        case 11:
                                            String operationform = Convert.toStr(value.get(11));
                                            if (operationform != null && !operationform.isEmpty()) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(operationform.trim());
                                                dictionary.setType("BP");
                                                dictionary.setPcode("BP024");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    expatriatesinforList.get(0).setOperationform(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 12:
                                            break;
                                        case 13:
                                            String jobclassification = Convert.toStr(value.get(13));
                                            if (jobclassification != null && !jobclassification.isEmpty()) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(jobclassification.trim());
                                                dictionary.setType("BP");
                                                dictionary.setPcode("BP025");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    expatriatesinforList.get(0).setJobclassification(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 14:
                                            expatriatesinforList.get(0).setSpeciality(Convert.toStr(value.get(14)));
                                            break;
                                        case 15:
                                            String interviewdep = Convert.toStr(value.get(15));

                                            if (interviewdep != null && !interviewdep.isEmpty()) {
                                                Query query = new Query();
                                                query.addCriteria(Criteria.where("userinfo.centername").is(interviewdep));
                                                List<CustomerInfo> customerInfoList = new ArrayList<CustomerInfo>();
                                                customerInfoList = mongoTemplate.find(query, CustomerInfo.class);
                                                if (customerInfoList.size() > 0) {
                                                    expatriatesinforList.get(0).setInterviewdep(customerInfoList.get(0).getUserinfo().getCenterid());
                                                    if (expatriatesinforList.get(0).getGroup_id() == null || expatriatesinforList.get(0).getGroup_id().equals("")) {
                                                        expatriatesinforList.get(0).setGroup_id(customerInfoList.get(0).getUserinfo().getCenterid());
                                                    }
                                                } else {
                                                    expatriatesinforList.get(0).setInterviewdep(interviewdep);
                                                    if (expatriatesinforList.get(0).getGroup_id() == null || expatriatesinforList.get(0).getGroup_id().equals("")) {
                                                        expatriatesinforList.get(0).setGroup_id(interviewdep);
                                                    }
                                                }
                                            } else {
                                                throw new LogicalException("第" + i + "行的面试部门不能为空，请确认。");
                                            }
                                            break;
                                        case 16:
                                            String interview_date = Convert.toStr(value.get(16));
                                            if (interview_date != null && interview_date.length() > 9) {
                                                interview_date = interview_date.trim().substring(0, 10);
                                                expatriatesinforList.get(0).setInterview_date(interview_date);
                                            }
                                            break;
                                        case 17:
                                            String result = Convert.toStr(value.get(17));
                                            if (result != null && !result.isEmpty()) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(result.trim());
                                                dictionary.setType("BP");
                                                dictionary.setPcode("BP003");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    expatriatesinforList.get(0).setResult(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 18:
                                            String whetherentry = Convert.toStr(value.get(18));
                                            if (whetherentry != null && !whetherentry.isEmpty()) {
                                                Dictionary dictionary = new Dictionary();
                                                dictionary.setValue1(whetherentry.trim());
                                                dictionary.setType("BP");
                                                dictionary.setPcode("BP006");
                                                List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                if (dictionaryList.size() > 0) {
                                                    expatriatesinforList.get(0).setWhetherentry(dictionaryList.get(0).getCode());
                                                }
                                            }
                                            break;
                                        case 19:
                                            String admissiontime = Convert.toStr(value.get(19));
                                            if (admissiontime != null && admissiontime.length() > 9) {
                                                admissiontime = admissiontime.trim().substring(0, 10);
                                                expatriatesinforList.get(0).setAdmissiontime(sff.parse(admissiontime));
                                            }
                                            break;
                                        case 20:
                                            String exits = Convert.toStr(value.get(20));
                                            if (exits != null && !exits.isEmpty()) {
                                                if (exits.trim().equals("是")) {
                                                    expatriatesinforList.get(0).setExits("0");
                                                } else if (exits.trim().equals("否")) {
                                                    expatriatesinforList.get(0).setExits("1");
                                                }
                                            }
                                            break;
                                        case 21:
                                            if (expatriatesinforList.get(0).getExits().equals("0")) {
                                                String exitime = Convert.toStr(value.get(21));
                                                if (exitime != null && exitime.length() > 9) {
                                                    exitime = exitime.trim().substring(0, 10);
                                                    expatriatesinforList.get(0).setExitime(sff.parse(exitime));
                                                }
                                            }
                                            break;
                                        case 22:
                                            if (expatriatesinforList.get(0).getExits().equals("0")) {
                                                String exitreason = Convert.toStr(value.get(22));
                                                if (exitreason != null && !exitreason.isEmpty()) {
                                                    Dictionary dictionary = new Dictionary();
                                                    dictionary.setValue1(exitreason.trim());
                                                    dictionary.setType("BP");
                                                    dictionary.setPcode("BP012");
                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                    if (dictionaryList.size() > 0) {
                                                        expatriatesinforList.get(0).setExitreason(dictionaryList.get(0).getCode());
                                                    }
                                                }
                                            }
                                            break;
                                        case 23:
                                            if (expatriatesinforList.get(0).getExits().equals("0")) {
                                                String alltechnology = Convert.toStr(value.get(23));
                                                if (alltechnology != null && !alltechnology.isEmpty()) {
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
                                            if (expatriatesinforList.get(0).getExits().equals("0")) {
                                                String sitevaluation = Convert.toStr(value.get(24));
                                                if (sitevaluation != null && !sitevaluation.isEmpty()) {
                                                    Dictionary dictionary = new Dictionary();
                                                    dictionary.setValue1(sitevaluation.trim());
                                                    dictionary.setType("BP");
                                                    dictionary.setPcode("BP009");
                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                    if (dictionaryList.size() > 0) {
                                                        expatriatesinforList.get(0).setSitevaluation(dictionaryList.get(0).getCode());
                                                    }
                                                }
                                            }
                                            break;
                                        case 25:
                                            if (expatriatesinforList.get(0).getExits().equals("0")) {
                                                String businessimpact = Convert.toStr(value.get(25));
                                                if (businessimpact != null && !businessimpact.isEmpty()) {
                                                    Dictionary dictionary = new Dictionary();
                                                    dictionary.setValue1(businessimpact.trim());
                                                    dictionary.setType("BP");
                                                    dictionary.setPcode("BP010");
                                                    List<Dictionary> dictionaryList = dictionaryService.getDictionaryList(dictionary);
                                                    if (dictionaryList.size() > 0) {
                                                        expatriatesinforList.get(0).setBusinessimpact(dictionaryList.get(0).getCode());
                                                    }
                                                }
                                            }
                                            break;
                                        case 26:
                                            if (expatriatesinforList.get(0).getExits().equals("0")) {
                                                String countermeasure = Convert.toStr(value.get(26));
                                                if (countermeasure != null && !countermeasure.isEmpty()) {
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
                                            expatriatesinforList.get(0).setRemarks(Convert.toStr(value.get(27)));
                                            break;
                                        case 28:
                                            expatriatesinforList.get(0).setLockernumber(Convert.toStr(value.get(28)));
                                            break;
                                    }
                                }
                            }
                            expatriatesinforList.get(0).preUpdate(tokenModel);
                            expatriatesinforMapper.updateByPrimaryKey(expatriatesinforList.get(0));
                            if (!expatriatesinforList.get(0).getGroup_id().isEmpty() && expatriatesinforList.get(0).getGroup_id() != null) {
                                //ccm add
                                ExpatriatesinforDetail e = new ExpatriatesinforDetail();
                                e.setExpatriatesinfor_id(expatriatesinforList.get(0).getExpatriatesinfor_id());
                                List<ExpatriatesinforDetail> expatriatesinforDetails = new ArrayList<>();
                                expatriatesinforDetails = expatriatesinforDetailMapper.select(e);
                                if (expatriatesinforDetails.size() == 0) {
                                    //登录新的履历
                                    ExpatriatesinforDetail e1 = new ExpatriatesinforDetail();
                                    e1.preInsert(tokenModel);
                                    e1.setGroup_id(expatriatesinforList.get(0).getGroup_id());
                                    e1.setExpatriatesinfordetail_id(UUID.randomUUID().toString());
                                    e1.setExpatriatesinfor_id(expatriatesinforList.get(0).getExpatriatesinfor_id());
                                    e1.setExdatestr(new Date());
                                    expatriatesinforDetailMapper.insert(e1);
                                } else {
                                    for (ExpatriatesinforDetail expDetail : expatriatesinforDetails) {
                                        if (expDetail.getExdateend() == null) {
                                            if (!expDetail.getGroup_id().equals(expatriatesinforList.get(0).getGroup_id())) {
                                                //更新结束时间
                                                expDetail.setExdateend(new Date());
                                                expDetail.preUpdate(tokenModel);
                                                expatriatesinforDetailMapper.updateByPrimaryKey(expDetail);

                                                //登录新的履历
                                                ExpatriatesinforDetail e2 = new ExpatriatesinforDetail();
                                                e2.preInsert(tokenModel);
                                                e2.setGroup_id(expatriatesinforList.get(0).getGroup_id());
                                                e2.setExpatriatesinfordetail_id(UUID.randomUUID().toString());
                                                e2.setExpatriatesinfor_id(expDetail.getExpatriatesinfor_id());
                                                e2.setExdatestr(new Date());
                                                expatriatesinforDetailMapper.insert(e2);
                                            }
                                        }
                                    }
                                }
                                //ccm add
                            }
                            accesscount = accesscount + 1;
                        } else {
                            throw new LogicalException("卡号（" + Convert.toStr(value.get(12)) + "）" + "在外驻人员表中不存在，无法更新，请确认。");
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



