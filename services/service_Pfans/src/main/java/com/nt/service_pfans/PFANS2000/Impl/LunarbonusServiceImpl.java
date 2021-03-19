package com.nt.service_pfans.PFANS2000.Impl;

import cn.hutool.core.convert.Convert;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Auth.Role;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Org.UserAccount;
import com.nt.dao_Pfans.PFANS1000.Vo.OrgTreeVo;
import com.nt.dao_Pfans.PFANS2000.Lunarbasic;
import com.nt.dao_Pfans.PFANS2000.Lunarbonus;
import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.dao_Pfans.PFANS2000.Vo.LunarAllVo;
import com.nt.dao_Pfans.PFANS2000.Vo.LunardetailVo;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS2000.LunarbonusService;
import com.nt.service_pfans.PFANS2000.mapper.ExaminationobjectMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbasicMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbonusMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunardetailMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.bytedeco.javacpp.presets.opencv_core;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class LunarbonusServiceImpl implements LunarbonusService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LunarbonusMapper lunarbonusMapper;

    @Autowired
    private LunardetailMapper lunardetailMapper;

    @Autowired
    private ExaminationobjectMapper examinationobjectMapper;

    @Autowired
    private LunarbasicMapper lunarbasicMapper;

    //获取一览
    @Override
    public List<Lunarbonus> getList(TokenModel tokenModel) throws Exception {
        List<String> lunarbonusList = new ArrayList<>();
        Lunarbonus lunarbonus = new Lunarbonus();
        //        通过查寻树建立表
//        Money4：1为Team层，2为Group层，3为Center层
        OrgTree orgs = orgTreeService.get(new OrgTree());
        List<OrgTreeVo> OrgTreeVolist = new ArrayList<>();
        OrgTreeVo orgtreevo = new OrgTreeVo();
        for (OrgTree org : orgs.getOrgs()) {//center
            orgtreevo = new OrgTreeVo();
            orgtreevo.set_id(org.get_id());//center_id
            orgtreevo.setMoney1("");//group_id
            orgtreevo.setMoney2("");//team_id
            orgtreevo.setMoney3(org.getUser());//负责人
            orgtreevo.setMoney4("3");//center
            OrgTreeVolist.add(orgtreevo);
            if(org.getOrgs() != null){
                for (OrgTree org1 : org.getOrgs()) {//group
                    orgtreevo = new OrgTreeVo();
                    orgtreevo.set_id(org.get_id());//center_id
                    orgtreevo.setMoney1(org1.get_id());//group_id
                    orgtreevo.setMoney2("");//team_id
                    orgtreevo.setMoney3(org1.getUser());//负责人
                    orgtreevo.setMoney4("2");//group
                    OrgTreeVolist.add(orgtreevo);
                    if(org1.getOrgs() != null){
                        for (OrgTree org2 : org1.getOrgs()) {//team
                            orgtreevo = new OrgTreeVo();
                            orgtreevo.set_id(org.get_id());//center_id
                            orgtreevo.setMoney1(org1.get_id());//group_id
                            orgtreevo.setMoney2(org2.get_id());//team_id
                            orgtreevo.setMoney3(org2.getUser());//负责人
                            orgtreevo.setMoney4("1");//team
                            OrgTreeVolist.add(orgtreevo);
                        }
                    }
                }
            }
        }
        Query query = new Query();
        String roles = "";
        query.addCriteria(Criteria.where("_id").is(tokenModel.getUserId()));
        UserAccount account = mongoTemplate.findOne(query,UserAccount.class);
        for(Role role : account.getRoles()){
            roles = roles + role.getDescription();
        }
        String lunarbonus1 = "";
        if (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理")) {
            for (OrgTreeVo orgTreeVo : OrgTreeVolist) {
                if (orgTreeVo.getMoney3().equals(tokenModel.getUserId())) {
                    if (orgTreeVo.getMoney4().equals("1")) {
                        lunarbonus1 = "PJ104001";
                    } else if (orgTreeVo.getMoney4().equals("2")) {
                        lunarbonus1 = "PJ104002";
                    } else if (orgTreeVo.getMoney4().equals("3")) {
                        lunarbonus1 = "PJ104003";
                    }
                    lunarbonusList.add(lunarbonus1);
                }
            }
            List<Lunarbonus> lunarbonus11 = lunarbonusMapper.selectSee(lunarbonusList);
            return lunarbonus11;
        } else {
            return lunarbonusMapper.select(lunarbonus);
        }
    }

    //新建一览和详情
    @Override
    public void insert(LunardetailVo lunardetailVo, TokenModel tokenModel) throws Exception {
        Lunarbonus lunarbonus = new Lunarbonus();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//        upd_fjl_06/10 start --添加先申请一次评价的check
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        String evaluationday = simpleDateFormat.format(lunardetailVo.getEvaluationday());
        Lunarbonus lu = new Lunarbonus();
        lu.setEvaluationday(evaluationday);
        lu.setSubjectmon(lunardetailVo.getSubjectmon());
        List<Lunarbonus> Li = lunarbonusMapper.select(lu);
        if (Li.size() == 0) {
            if (!lunardetailVo.getEvaluatenum().equals("PJ104001")) {
                throw new LogicalException("请先申请【一次評価】");
            }
//            for (Lunarbonus ll : Li) {
//                if (!lunardetailVo.getEvaluatenum().equals("PJ104001")) {
//                    if (!StringUtils.isNullOrEmpty(ll.getEvaluatenum()) && !ll.getEvaluatenum().equals("PJ104001")) {
//                        throw new LogicalException("请先申请【一次評価】");
//                    }
//                }
//            }
        }
//        upd_fjl_06/10 start --添加先申请一次评价的check
        lunarbonus.setEvaluationday(evaluationday);
        lunarbonus.setSubjectmon(lunardetailVo.getSubjectmon());
        lunarbonus.setEvaluatenum(lunardetailVo.getEvaluatenum());

        List<Lunarbonus> List = lunarbonusMapper.select(lunarbonus);
        if (List.size() == 0) {
            lunarbonus.setSubject(lunardetailVo.getSubjectmon());
            lunarbonus.setUser_id(lunardetailVo.getUser_id());
            lunarbonus.preInsert(tokenModel);
            lunarbonus.preUpdate(tokenModel);
            lunarbonus.setLunarbonus_id(UUID.randomUUID().toString());
            lunarbonus.setTenantid("0");
            lunarbonusMapper.insert(lunarbonus);
        } else {
            throw new LogicalException("不能重复评价");
        }

        if (lunardetailVo.getEvaluatenum().equals("PJ104001")) {
            Query query = new Query();
            SimpleDateFormat sff = new SimpleDateFormat("yyyy-MM-dd");
            List<CustomerInfo> CustomerInfoList = mongoTemplate.find(query, CustomerInfo.class);
//            add_fjl_0608 --填在在职人员筛选
            List<CustomerInfo> cust = new ArrayList<>();
            if (CustomerInfoList.size() > 0) {
                for (int i = 0; i < CustomerInfoList.size(); i++) {
                    if (StringUtils.isNullOrEmpty(CustomerInfoList.get(i).getUserinfo().getResignation_date())) {
                        cust.add(CustomerInfoList.get(i));
                    } else if (!StringUtils.isNullOrEmpty(CustomerInfoList.get(i).getUserinfo().getResignation_date()) && sff.parse(CustomerInfoList.get(i).getUserinfo().getResignation_date()).compareTo(new Date()) > 0) {
                        cust.add(CustomerInfoList.get(i));
                    }
                }
            }
//            根据评价日期设置查询出是哪月的出勤率
            String YearM = "202012";
            String Years = "2021";
            String Types = "";
            String Typess = "";
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            if (lunardetailVo.getSubjectmon().equals("PJ103001")) {
                YearM = (Integer.valueOf(sf.format(lunardetailVo.getEvaluationday())) - 1) + "12";
                Years = String.valueOf(Integer.valueOf(sf.format(lunardetailVo.getEvaluationday())) - 1);
                Types = "12";
                Typess = "12";
            } else if (lunardetailVo.getSubjectmon().equals("PJ103002")) {
                YearM = sf.format(lunardetailVo.getEvaluationday()) + "03";
                Years = sf.format(lunardetailVo.getEvaluationday());
                Types = "1";
                Typess = "3";
            } else if (lunardetailVo.getSubjectmon().equals("PJ103003")) {
                YearM = sf.format(lunardetailVo.getEvaluationday()) + "06";
                Years = sf.format(lunardetailVo.getEvaluationday());
                Types = "1";
                Typess = "6";
            } else if (lunardetailVo.getSubjectmon().equals("PJ103004")) {
                YearM = sf.format(lunardetailVo.getEvaluationday()) + "09";
                Years = sf.format(lunardetailVo.getEvaluationday());
                Types = "1";
                Typess = "9";
            }
//            查询出勤率

            List<Lunardetail> detail = lunardetailMapper.selectWorkrate(YearM);

//            前年的赏与记号
            List<Lunardetail> detail1 = lunardetailMapper.selectSign(Years,Types);
//            前回月度评价记号
            List<Lunardetail> detail2 = lunardetailMapper.selectSymbol(Years,Typess);

//            add_fjl_0608 --填在在职人员筛选
            for (CustomerInfo customerInfo : cust) {
                Lunardetail lunardetail = new Lunardetail();
                if (customerInfo != null) {
                    Date date = new Date();
                    String da = sf.format(lunardetailVo.getEvaluationday());
                    DecimalFormat df = new DecimalFormat("######0");
                    //根据出勤率的计算结果设置保存值
                    for (Lunardetail de : detail) {
                        if (de.getUser_id().equals(customerInfo.getUserid())) {
                            if (Double.valueOf(de.getWorkrate()) > 1) {
                                lunardetail.setWorkrate("100%");
                            } else if (Double.valueOf(de.getWorkrate()) < 1) {
                                lunardetail.setWorkrate(df.format(Double.valueOf(de.getWorkrate())*100) + "%");
                            } else if (Double.valueOf(de.getWorkrate()).equals(0.00) || de.getWorkrate() == null || de.getWorkrate().equals("")) {
                                lunardetail.setWorkrate("0%");
                            }
                            break;
                        }
                    }
                    //根据年初等级评定结果设置保存值
                    for (Lunardetail de1 : detail1) {
                        if (de1.getUser_id().equals(customerInfo.getUserid())) {
                            if (de1.getBonussign() == null || de1.getBonussign().equals("")) {
                                lunardetail.setBonussign("-");
                            } else {
                                lunardetail.setBonussign(de1.getBonussign());
                            }
                        }
                    }
                    //根据上次等级评定结果设置保存值
                    for (Lunardetail de2 : detail2) {
                        if (de2.getUser_id().equals(customerInfo.getUserid())) {
                            if (de2.getLastsymbol() == null || de2.getLastsymbol().equals("")) {
                                lunardetail.setLastsymbol("-");
                            } else {
                                lunardetail.setLastsymbol(de2.getLastsymbol());
                            }
                        }
                    }

                    if (lunardetailVo.getSubjectmon().equals("PJ103001")) {
                        lunardetail.setMonth("1");
                    } else if (lunardetailVo.getSubjectmon().equals("PJ103002")) {
                        lunardetail.setMonth("4");
                    } else if (lunardetailVo.getSubjectmon().equals("PJ103003")) {
                        lunardetail.setMonth("7");
                    } else if (lunardetailVo.getSubjectmon().equals("PJ103004")) {
                        lunardetail.setMonth("10");
                    }
                    lunardetail.preInsert(tokenModel);
                    lunardetail.setLunardetail_id(UUID.randomUUID().toString());
                    lunardetail.setSubjectmon(lunarbonus.getSubjectmon());
                    lunardetail.setEvaluatenum(lunarbonus.getEvaluatenum());
                    lunardetail.setLunarbonus_id(lunarbonus.getLunarbonus_id());
                    lunardetail.setExaminationobject_id(lunardetailVo.getExaminationobject_id());
                    lunardetail.setEvaluationday(da);
                    lunardetail.setUser_id(customerInfo.getUserid());
                    lunardetail.setRn(customerInfo.getUserinfo().getRank());
                    lunardetail.setEnterday(customerInfo.getUserinfo().getEnterday());
                    lunardetail.setGroup_id(customerInfo.getUserinfo().getGroupid());
                    lunardetail.setSalary(customerInfo.getUserinfo().getSalary());
                    lunardetail.setTeam_id(customerInfo.getUserinfo().getTeamid());
                    lunardetail.setCenter_id(customerInfo.getUserinfo().getCenterid());
                    if (customerInfo.getUserinfo().getEnddate() != null && customerInfo.getUserinfo().getEnddate().length() > 0) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Calendar rightNow = Calendar.getInstance();
                        String enddate = customerInfo.getUserinfo().getEnddate().substring(0, 10);
                        if (customerInfo.getUserinfo().getEnddate().length() >= 24) {
                            rightNow.setTime(Convert.toDate(enddate));
                            rightNow.add(Calendar.DAY_OF_YEAR, 1);
                            enddate = sdf.format(rightNow.getTime());
                        }
                        String newdate = sdf.format(date);
                        if (sdf.parse(newdate).getTime() > sdf.parse(enddate).getTime()) {
                            lunardetail.setDifference("2");
                        } else {
                            lunardetail.setDifference("1");
                        }
                    }
                    if (lunardetail.getDifference() != null) {
                        if (lunardetail.getDifference().equals("1")) {
                            lunardetail.setPrize("无");
                        } else if (lunardetail.getDifference().equals("2")) {
                            lunardetail.setPrize("有");
                        }
                    }
                    lunardetail.setProcess("0");
                    lunardetail.setOccupationtype(customerInfo.getUserinfo().getOccupationtype());
                    lunardetailMapper.insert(lunardetail);
                }
            }
        }

    }

    //获取详情列表初始数据
    @Override
    public LunarAllVo getOne(String id, TokenModel tokenModel) throws Exception {
        OrgTree orgs = orgTreeService.get(new OrgTree());
        List<OrgTreeVo> OrgTreeVolist = new ArrayList<>();
        OrgTreeVo orgtreevo = new OrgTreeVo();
        for (OrgTree org : orgs.getOrgs()) {//center
            orgtreevo = new OrgTreeVo();
            orgtreevo.set_id(org.get_id());//center_id
            orgtreevo.setMoney1("");//group_id
            orgtreevo.setMoney2("");//team_id
            orgtreevo.setMoney3(org.getUser());//负责人
            orgtreevo.setMoney4("3");//center
            OrgTreeVolist.add(orgtreevo);
            if(org.getOrgs() != null){
                for (OrgTree org1 : org.getOrgs()) {//group
                    orgtreevo = new OrgTreeVo();
                    orgtreevo.set_id(org.get_id());//center_id
                    orgtreevo.setMoney1(org1.get_id());//group_id
                    orgtreevo.setMoney2("");//team_id
                    orgtreevo.setMoney3(org1.getUser());//负责人
                    orgtreevo.setMoney4("2");//group
                    OrgTreeVolist.add(orgtreevo);
                    if(org1.getOrgs() != null){
                        for (OrgTree org2 : org1.getOrgs()) {//team
                            orgtreevo = new OrgTreeVo();
                            orgtreevo.set_id(org.get_id());//center_id
                            orgtreevo.setMoney1(org1.get_id());//group_id
                            orgtreevo.setMoney2(org2.get_id());//team_id
                            orgtreevo.setMoney3(org2.getUser());//负责人
                            orgtreevo.setMoney4("1");//team
                            OrgTreeVolist.add(orgtreevo);
                        }
                    }
                }
            }
        }
        Query query = new Query();
        String roles = "";
        query.addCriteria(Criteria.where("_id").is(tokenModel.getUserId()));
        UserAccount account = mongoTemplate.findOne(query,UserAccount.class);
        for(Role role : account.getRoles()){
            roles = roles + role.getDescription();
        }
        List<String> lunardetailList = new ArrayList<>();
        LunarAllVo LunarAllVo = new LunarAllVo();
        Lunarbonus lunarbonus = lunarbonusMapper.selectByPrimaryKey(id);
        LunarAllVo.setLunarbonus(lunarbonus);
        List<Lunardetail> detal = new ArrayList<>();
        Lunardetail lunardetailCondition = new Lunardetail();
        lunardetailCondition.setLunarbonus_id(id);
        if (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理")) {
            List<String> users = new ArrayList<String>();
            Query cusquery = new Query();
            cusquery.addCriteria(Criteria.where("userid").is(tokenModel.getUserId()));
            CustomerInfo cus = mongoTemplate.findOne(cusquery, CustomerInfo.class);

//            if (cus != null && cus.getUserinfo() != null) {
//                List<CustomerInfo> cuslist = new ArrayList<CustomerInfo>();
//                String teamid = cus.getUserinfo().getTeamid();
//                String groupid = cus.getUserinfo().getGroupid();
//                String centerid = cus.getUserinfo().getCenterid();
            String detailLevel = "";
                if (lunarbonus.getEvaluatenum().equals("PJ104001")) {
                    for (OrgTreeVo orgTreeVo : OrgTreeVolist) {
                        if (orgTreeVo.getMoney3().equals(tokenModel.getUserId()) && orgTreeVo.getMoney4().equals("1")) {
                            detailLevel = orgTreeVo.getMoney2();
                            lunardetailList.add(detailLevel);
                        }
                    }
                    detal = lunardetailMapper.selectTeam(lunardetailList, tokenModel.getUserId(), lunarbonus.getEvaluationday(), lunarbonus.getSubjectmon());
                }
                if (lunarbonus.getEvaluatenum().equals("PJ104002")) {
                    for (OrgTreeVo orgTreeVo : OrgTreeVolist) {
                        if (orgTreeVo.getMoney3().equals(tokenModel.getUserId()) && orgTreeVo.getMoney4().equals("2")) {
                            detailLevel = orgTreeVo.getMoney1();
                            lunardetailList.add(detailLevel);
                        }
                    }
                    detal = lunardetailMapper.selectGroup(lunardetailList, tokenModel.getUserId(), lunarbonus.getEvaluationday(), lunarbonus.getSubjectmon());
                }
                if (lunarbonus.getEvaluatenum().equals("PJ104003")) {
                    for (OrgTreeVo orgTreeVo : OrgTreeVolist) {
                        if (orgTreeVo.getMoney3().equals(tokenModel.getUserId()) && orgTreeVo.getMoney4().equals("3")) {
                            detailLevel = orgTreeVo.get_id();
                            lunardetailList.add(detailLevel);
                        }
                    }
                    detal = lunardetailMapper.selectCenter(lunardetailList, tokenModel.getUserId(), lunarbonus.getEvaluationday(), lunarbonus.getSubjectmon());
                }
//                if (!StringUtils.isNullOrEmpty(teamid)) {
//                    lunardetailCondition.setTeam_id(teamid);
//                } else if (!StringUtils.isNullOrEmpty(groupid)) {
//                    lunardetailCondition.setGroup_id(groupid);
//                } else if (!StringUtils.isNullOrEmpty(centerid)) {
//                    lunardetailCondition.setCenter_id(centerid);
//                }
//            }

        } else {
            String Evanum = "";
            if (lunarbonus.getEvaluatenum().equals("PJ104003")) {
                Evanum = "PJ104001";
            } else if (lunarbonus.getEvaluatenum().equals("PJ104002")) {
                Evanum = "PJ104001";
            } else if (lunarbonus.getEvaluatenum().equals("PJ104001")) {
                Evanum = "PJ104001";
            }
            String Evaday = lunarbonus.getEvaluationday();
            String Submon = lunarbonus.getSubjectmon();
            detal = lunardetailMapper.selectResult(Evanum, Evaday, Submon, tokenModel.getUserId());
        }
        LunarAllVo.setLunardetail(detal);

//        List<Lunardetail> detal = lunardetailMapper.select(lunardetailCondition);
//        if (detal.size() > 0) {
//
//            LunarAllVo.setLunardetail(detal);
//        } else {
//            Lunarbonus con = new Lunarbonus();
//            String Evanum = "";
//            if (lunarbonus.getEvaluatenum().equals("PJ104003")) {
//                Evanum = "PJ104001";
//            } else if (lunarbonus.getEvaluatenum().equals("PJ104002")) {
//                Evanum = "PJ104001";
//            }
//            String Evaday = lunarbonus.getEvaluationday();
//            String Submon = lunarbonus.getSubjectmon();
//            List<Lunarbonus> lunars = lunarbonusMapper.selectResult(Evanum, Evaday, Submon);
//            if (lunars.size() > 0) {
//                lunardetailCondition.setLunarbonus_id(lunars.get(0).getLunarbonus_id());
//                detal = lunardetailMapper.select(lunardetailCondition);
//                for(Lunardetail item:detal){
//                    item.setLunarbonus_id("");
//                    item.setLunardetail_id("");
//                }
//                LunarAllVo.setLunardetail(detal);
//            }else{
//                if(lunarbonus.getEvaluatenum().equals("PJ104003")){
//                    con.setEvaluatenum("PJ104001");
//
//                    lunars = lunarbonusMapper.select(con);
//                    if (lunars.size() > 0) {
//                        lunardetailCondition.setLunarbonus_id(lunars.get(0).getLunarbonus_id());
//                        detal = lunardetailMapper.select(lunardetailCondition);
//                        for(Lunardetail item:detal){
//                            item.setLunarbonus_id("");
//                            item.setLunardetail_id("");
//                        }
//                        LunarAllVo.setLunardetail(detal);
//                    }
//                }
//            }
////            LunarAllVo.setLunardetail();
//        }
        Lunarbasic lunarbasicConditon = new Lunarbasic();
        lunarbasicConditon.setLunarbonus_id(id);
        LunarAllVo.setLunarbasic(lunarbasicMapper.select(lunarbasicConditon).stream().sorted(Comparator.comparing(Lunarbasic::getIndex)).collect(Collectors.toList()));
        return LunarAllVo;
    }

//    人事发起待办
    @Override
    public void createTodonotice(Lunarbonus lunarbonus,TokenModel tokenModel) throws Exception {
//        通过查寻树建立表
//        Money4：1为Team层，2为Group层，3为Center层
        OrgTree orgs = orgTreeService.get(new OrgTree());
        List<OrgTreeVo> OrgTreeVolist = new ArrayList<>();
        OrgTreeVo orgtreevo = new OrgTreeVo();
        for (OrgTree org : orgs.getOrgs()) {//center
            orgtreevo = new OrgTreeVo();
            orgtreevo.set_id(org.get_id());//center_id
            orgtreevo.setMoney1("");//group_id
            orgtreevo.setMoney2("");//team_id
            orgtreevo.setMoney3(org.getUser());//负责人
            orgtreevo.setMoney4("3");//center
            OrgTreeVolist.add(orgtreevo);
            if(org.getOrgs() != null){
                for (OrgTree org1 : org.getOrgs()) {//group
                    orgtreevo = new OrgTreeVo();
                    orgtreevo.set_id(org.get_id());//center_id
                    orgtreevo.setMoney1(org1.get_id());//group_id
                    orgtreevo.setMoney2("");//team_id
                    orgtreevo.setMoney3(org1.getUser());//负责人
                    orgtreevo.setMoney4("2");//group
                    OrgTreeVolist.add(orgtreevo);
                    if(org1.getOrgs() != null){
                        for (OrgTree org2 : org1.getOrgs()) {//team
                            orgtreevo = new OrgTreeVo();
                            orgtreevo.set_id(org.get_id());//center_id
                            orgtreevo.setMoney1(org1.get_id());//group_id
                            orgtreevo.setMoney2(org2.get_id());//team_id
                            orgtreevo.setMoney3(org2.getUser());//负责人
                            orgtreevo.setMoney4("1");//team
                            OrgTreeVolist.add(orgtreevo);
                        }
                    }
                }
            }
        }
        Query query = new Query();
        String roles = "";
        query.addCriteria(Criteria.where("_id").is(tokenModel.getUserId()));
        UserAccount account = mongoTemplate.findOne(query,UserAccount.class);
        for(Role role : account.getRoles()){
            roles = roles + role.getDescription();
        }
        String Grpup_id = "";
//        start 人事给每个TL发送待办进行一次评价 仅人事/薪资操作阶段

        if (roles.contains("人事总务部长") || roles.contains("工资计算担当")) {
            List<OrgTreeVo> TreeVolistTL = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("1"))).collect(Collectors.toList());
            for (OrgTreeVo orgTreeVo0 : TreeVolistTL) {
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice.setDataid(lunarbonus.getLunarbonus_id());
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setUrl("/PFANS2027View");
                toDoNotice.setWorkflowurl("/PFANS2027View");
                toDoNotice.setOwner(orgTreeVo0.getMoney3());
                toDoNoticeService.save(toDoNotice);
            }
            String Process = "1";
            String subjectmon = "";
            lunarbonus.setLunarbonus_id(lunarbonus.getLunarbonus_id());
            if (lunarbonus.getSubjectmon().equals("1-3月(10-12月実際を参考)")) {
                subjectmon = "PJ103001";
            } else if (lunarbonus.getSubjectmon().equals("4-6月(1-3月実際を参考)")) {
                subjectmon = "PJ103002";
            } else if (lunarbonus.getSubjectmon().equals("7-9月(4-6月実際を参考)")) {
                subjectmon = "PJ103003";
            } else if (lunarbonus.getSubjectmon().equals("10-12月(7-9月実際を参考)")) {
                subjectmon = "PJ103004";
            }
            lunardetailMapper.updateProcess1(Process, lunarbonus.getEvaluationday(), subjectmon);
            lunardetailMapper.updateProcess2(Process, lunarbonus.getEvaluationday(), subjectmon);
            String Month = "0";
            if (lunarbonus.getSubjectmon().equals("1-3月(10-12月実際を参考)")) {
                Month = "1";
            } else if (lunarbonus.getSubjectmon().equals("4-6月(1-3月実際を参考)")) {
                Month = "4";
            } else if (lunarbonus.getSubjectmon().equals("7-9月(4-6月実際を参考)")) {
                Month = "7";
            } else if (lunarbonus.getSubjectmon().equals("10-12月(7-9月実際を参考)")) {
                Month = "10";
            }
            Lunardetail lunardetail = new Lunardetail();
            lunardetail.setLunarbonus_id(lunarbonus.getLunarbonus_id());
            List<Lunardetail> LunardetailList = lunardetailMapper.select(lunardetail);
            List<Lunardetail> newdetailList = new ArrayList<>();
            List<Lunardetail> newdetailLists = new ArrayList<>();
            if (LunardetailList.size() > 0) {
                for (Lunardetail lunarlist : LunardetailList) {
                    lunarlist.setLunardetail_id(UUID.randomUUID().toString());
                    lunarlist.setMonth(String.valueOf(Integer.valueOf(Month) + 1));
                    newdetailList.add(lunarlist);
                }
                lunardetailMapper.insertListAllCols(newdetailList);
                for (Lunardetail detail: LunardetailList) {
                    detail.setLunardetail_id(UUID.randomUUID().toString());
                    detail.setMonth(String.valueOf(Integer.valueOf(Month) + 2));
                    newdetailLists.add(detail);
                }
                lunardetailMapper.insertListAllCols(newdetailLists);
            }
            //        end 人事给每个TL发送待办进行一次评价
        } else {
//        start tl给下一级gm发待办  仅tl职操作阶段
            if (lunarbonus.getEvaluatenum().equals("一次評価") && (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理"))) {
                List<OrgTreeVo> TreeVolistTLGroup = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("1") && item.getMoney3().equals(tokenModel.getUserId()))).collect(Collectors.toList());
                for (OrgTreeVo orgTreeVo : TreeVolistTLGroup) {
                    List<OrgTreeVo> TreeVolistGroup = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("2") && item.getMoney1().equals(orgTreeVo.getMoney1()))).collect(Collectors.toList());
                    Grpup_id = TreeVolistGroup.get(0).getMoney3();
                }
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice.setDataid(lunarbonus.getLunarbonus_id());
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setUrl("/PFANS2027View");
                toDoNotice.setWorkflowurl("/PFANS2027View");
                toDoNotice.setOwner(Grpup_id);
                toDoNoticeService.save(toDoNotice);
                String Process = "2";
                String Tmid = TreeVolistTLGroup.get(0).getMoney2();
                lunardetailMapper.updateProcesT(Process, Tmid);
            }
        }
//        end tl给下一级gm发待办
//        start gm 给下一级center长发待办  仅GM操作阶段
        if (lunarbonus.getEvaluatenum().equals("二次評価") && (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理"))) {
            List<OrgTreeVo> TreeVolistGroupCenter = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("2") && item.getMoney3().equals(tokenModel.getUserId()))).collect(Collectors.toList());
            for (OrgTreeVo orgTreeVo: TreeVolistGroupCenter) {
                List<OrgTreeVo> TreeVolistCenter = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("3") && item.get_id().equals(orgTreeVo.get_id()))).collect(Collectors.toList());
                Grpup_id = TreeVolistCenter.get(0).getMoney3();
            }
            ToDoNotice toDoNotice1 = new ToDoNotice();
            toDoNotice1.setTitle("您有月度赏与评价需要确认，请注意查看。");
            toDoNotice1.setInitiator(tokenModel.getUserId());
            toDoNotice1.setContent("您有月度赏与评价需要确认，请注意查看。");
            toDoNotice1.setDataid(lunarbonus.getLunarbonus_id());
            toDoNotice1.preInsert(tokenModel);
            toDoNotice1.setUrl("/PFANS2027View");
            toDoNotice1.setWorkflowurl("/PFANS2027View");
            toDoNotice1.setOwner(Grpup_id);
            toDoNoticeService.save(toDoNotice1);
            String Process = "3";
            String Gpid = TreeVolistGroupCenter.get(0).getMoney1();
            lunardetailMapper.updateProcesG(Process, Gpid);
        }
//        end gm 给下一级center长发待办
//        start center长 给总经理发待办
        if (lunarbonus.getEvaluatenum().equals("最终評価") && (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理"))) {
            List<OrgTreeVo> TreeVolistCenterToZong = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("3") && item.getMoney3().equals(tokenModel.getUserId()))).collect(Collectors.toList());
            ToDoNotice toDoNotice1 = new ToDoNotice();
            toDoNotice1.setTitle("您有月度赏与评价需要确认，请注意查看。");
            toDoNotice1.setInitiator(tokenModel.getUserId());
            toDoNotice1.setContent("您有月度赏与评价需要确认，请注意查看。");
            toDoNotice1.setDataid(lunarbonus.getLunarbonus_id());
            toDoNotice1.preInsert(tokenModel);
            toDoNotice1.setUrl("/PFANS2027View");
            toDoNotice1.setWorkflowurl("/PFANS2027View");
            List<MembersVo> rolelist = roleService.getMembers("5e785fd38f4316308435112d"); //总经理
            if(rolelist.size() > 0)
            {
                toDoNotice1.setOwner(rolelist.get(0).getUserid());
            }
            toDoNoticeService.save(toDoNotice1);
            String Process = "4";
            String Crid = TreeVolistCenterToZong.get(0).get_id();
            lunardetailMapper.updateProcesC(Process, Crid);
        }
//        end center长 给总经理发待办
//        start 总经理给人事发待办
        if (roles.contains("总经理")) {
            ToDoNotice toDoNotice1 = new ToDoNotice();
            toDoNotice1.setTitle("您有月度赏与评价需要确认，请注意查看。");
            toDoNotice1.setInitiator(tokenModel.getUserId());
            toDoNotice1.setContent("可以关闭评价。");
            toDoNotice1.setDataid(lunarbonus.getLunarbonus_id());
            toDoNotice1.preInsert(tokenModel);
            toDoNotice1.setUrl("/PFANS2027View");
            toDoNotice1.setWorkflowurl("/PFANS2027View");
            List<MembersVo> rolelist = roleService.getMembers("5e7861d38f43163084351133"); //人事
            if(rolelist.size() > 0)
            {
                toDoNotice1.setOwner(rolelist.get(0).getUserid());
            }
            toDoNoticeService.save(toDoNotice1);
        }
    }

    @Override
    public void overTodonotice(Lunarbonus lunarbonus,TokenModel tokenModel) throws Exception {
        String Process = "5";
        String subjectmon = "";
        if (lunarbonus.getSubjectmon().equals("1-3月(10-12月実際を参考)")) {
            subjectmon = "PJ103001";
        } else if (lunarbonus.getSubjectmon().equals("4-6月(1-3月実際を参考)")) {
            subjectmon = "PJ103002";
        } else if (lunarbonus.getSubjectmon().equals("7-9月(4-6月実際を参考)")) {
            subjectmon = "PJ103003";
        } else if (lunarbonus.getSubjectmon().equals("10-12月(7-9月実際を参考)")) {
            subjectmon = "PJ103004";
        }
        lunardetailMapper.updateProcess1(Process, lunarbonus.getEvaluationday(), subjectmon);
        lunardetailMapper.updateProcess2(Process, lunarbonus.getEvaluationday(), subjectmon);
    }
}
