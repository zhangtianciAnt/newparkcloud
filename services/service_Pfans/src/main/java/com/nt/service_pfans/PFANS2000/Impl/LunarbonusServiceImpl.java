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
import com.nt.service_pfans.PFANS2000.mapper.LunarbasicMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunarbonusMapper;
import com.nt.service_pfans.PFANS2000.mapper.LunardetailMapper;
import com.nt.utils.AuthConstants;
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
        roles = roles.replace("副总经理","副总");
        String lunarbonus1 = "";
        if (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理")) {
            List<OrgTreeVo> OrgTreeVolistnew = OrgTreeVolist.stream().filter(coi -> (coi.getMoney3().contains(tokenModel.getUserId()))).collect(Collectors.toList());
            for (OrgTreeVo orgTreeVo : OrgTreeVolistnew) {
                if (orgTreeVo.getMoney4().equals("1")) {
                    lunarbonus1 = "PJ104001";
                } else if (orgTreeVo.getMoney4().equals("2")) {
                    lunarbonus1 = "PJ104002";
                } else if (orgTreeVo.getMoney4().equals("3")) {
                    lunarbonus1 = "PJ104003";
                }
                lunarbonusList.add(lunarbonus1);
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
//        upd_fjl_06/10 start --添加先申请一次评价的check
        lunarbonus.setEvaluationday(evaluationday);
        lunarbonus.setSubjectmon(lunardetailVo.getSubjectmon());

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
        //根据评价日期设置查询出是哪月的出勤率
        String YearM = "";
        String Years = "";
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
        //查询出勤率
        List<Lunardetail> detail = lunardetailMapper.selectWorkrate(YearM);
        //前年的赏与记号
        List<Lunardetail> detail1 = lunardetailMapper.selectSign(Years,Types);
        //前回月度评价记号
        List<Lunardetail> detail2 = lunardetailMapper.selectSymbol(Years,Typess);

//            add_fjl_0608 --填在在职人员筛选
        String Month = "0";
        if (lunardetailVo.getSubjectmon().equals("PJ103001")) {
            Month = "1";
        } else if (lunardetailVo.getSubjectmon().equals("PJ103002")) {
            Month = "4";
        } else if (lunardetailVo.getSubjectmon().equals("PJ103003")) {
            Month = "7";
        } else if (lunardetailVo.getSubjectmon().equals("PJ103004")) {
            Month = "10";
        }
        for (CustomerInfo customerInfo : cust) {
            Lunardetail lunardetail = new Lunardetail();
            if (customerInfo != null) {
                Date date = new Date();
                String da = sf.format(lunardetailVo.getEvaluationday());
                DecimalFormat df = new DecimalFormat("######0");
                //根据出勤率的计算结果设置保存值
                List<Lunardetail> detailcust = detail.stream().filter(coi -> (coi.getUser_id().contains(customerInfo.getUserid()))).collect(Collectors.toList());
                if(detailcust.size() > 0){
                    if (Double.valueOf(detailcust.get(0).getWorkrate()).equals(0.00) || detailcust.get(0).getWorkrate() == null || detailcust.get(0).getWorkrate().equals("")) {
                        lunardetail.setWorkrate("0%");
                    }
                    else{
                        lunardetail.setWorkrate(df.format(Double.valueOf(detailcust.get(0).getWorkrate())*100) + "%");
                    }
                }
                //根据年初等级评定结果设置保存值
                List<Lunardetail> detail1cust = detail1.stream().filter(coi -> (coi.getUser_id().contains(customerInfo.getUserid()))).collect(Collectors.toList());
                if(detail1cust.size() > 0){
                    if (detail1cust.get(0).getBonussign() == null || detail1cust.get(0).getBonussign().equals("")) {
                        lunardetail.setBonussign("-");
                    } else {
                        lunardetail.setBonussign(detail1cust.get(0).getBonussign());
                    }
                }
                //根据上次等级评定结果设置保存值
                List<Lunardetail> detail2cust = detail2.stream().filter(coi -> (coi.getUser_id().contains(customerInfo.getUserid()))).collect(Collectors.toList());
                if(detail2cust.size() > 0){
                    if (detail2cust.get(0).getLastsymbol() == null || detail2cust.get(0).getLastsymbol().equals("")) {
                        lunardetail.setLastsymbol("-");
                    } else {
                        lunardetail.setLastsymbol(detail2cust.get(0).getLastsymbol());
                    }
                }
                lunardetail.setMonth(Month);
                lunardetail.preInsert(tokenModel);
                lunardetail.setLunardetail_id(UUID.randomUUID().toString());
                lunardetail.setSubjectmon(lunarbonus.getSubjectmon());
                lunardetail.setLunarbonus_id(lunarbonus.getLunarbonus_id());
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
                    String enddate = customerInfo.getUserinfo().getEnddate().substring(0, 10).replace("-","/");
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

        //region 新建后两月的数据
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
            for (Lunardetail lunarlist : LunardetailList) {
                lunarlist.setLunardetail_id(UUID.randomUUID().toString());
                lunarlist.setMonth(String.valueOf(Integer.valueOf(Month) + 2));
                newdetailLists.add(lunarlist);
            }
            lunardetailMapper.insertListAllCols(newdetailLists);
        }
        //endregion 新建后两月的数据
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
        roles = roles.replace("副总经理","副总");
        List<String> lunardetailList = new ArrayList<>();
        LunarAllVo LunarAllVo = new LunarAllVo();
        LunarAllVo.setSubmitFlg("0");
        Lunarbonus lunarbonus = lunarbonusMapper.selectByPrimaryKey(id);
        Lunarbonus Lunarbonusal = new Lunarbonus();
        Lunarbonusal.setEvaluationday(lunarbonus.getEvaluationday());
        Lunarbonusal.setSubject(lunarbonus.getSubject());
        List<Lunarbonus> lunarbonusALLList = lunarbonusMapper.select(Lunarbonusal);
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
                String strCenterList = "";//111
                if (lunarbonus.getEvaluatenum().equals("PJ104001")) {
                    for (OrgTreeVo orgTreeVo : OrgTreeVolist) {
                        if (orgTreeVo.getMoney3().equals(tokenModel.getUserId()) && orgTreeVo.getMoney4().equals("1")) {
                            detailLevel = orgTreeVo.getMoney2();
                            lunardetailList.add(detailLevel);
                        }
                    }
                    //detal = lunardetailMapper.selectTeam(lunardetailList, tokenModel.getUserId(), lunarbonus.getEvaluationday(), lunarbonus.getSubject());
                    detal = lunardetailMapper.selectGroup(lunardetailList, tokenModel.getUserId(),lunarbonus.getLunarbonus_id(), lunarbonus.getEvaluationday(), lunarbonus.getSubject());
                    LunarAllVo.setSubmitFlg("1");//可提交
                }
                //center二次评价
                if (lunarbonus.getEvaluatenum().equals("PJ104002")) {
                    List<OrgTreeVo> OrgTreeVolistowner = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney3().equals(tokenModel.getUserId()))).collect(Collectors.toList());
                    OrgTreeVolistowner = OrgTreeVolistowner.stream().distinct().filter(item -> (item.getMoney4().equals("2"))).collect(Collectors.toList());
                    for (OrgTreeVo orgTreeVo : OrgTreeVolistowner) {
                        //center
                        detailLevel = orgTreeVo.getMoney1();
                        lunardetailList.add(detailLevel);
                        //查询一次评价
                        List<Lunarbonus> lunarbonusev = lunarbonusALLList.stream().distinct().filter(item -> (item.getEvaluatenum().equals("PJ104001"))).collect(Collectors.toList());
                        if(lunarbonusev.size() > 0){
                            //获取center下group
                            List<OrgTreeVo> TreeVoGrouplist = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney1().equals(orgTreeVo.getMoney1()))).collect(Collectors.toList());
                            //center下一次评价的group个数
                            List<Lunardetail> detalGroup = lunardetailMapper.selectGroupCount(lunardetailList, tokenModel.getUserId(), lunarbonusev.get(0).getLunarbonus_id(),lunarbonus.getEvaluationday(), lunarbonus.getSubject());
                            //如果相同说明center下所有group已提交一次评价
                            if(TreeVoGrouplist.size() - 1 == detalGroup.size()){
                                LunarAllVo.setSubmitFlg("1");//可提交
                            }
                        }
                    }
                    detal = lunardetailMapper.selectCenter(lunardetailList, tokenModel.getUserId(), lunarbonus.getLunarbonus_id(),lunarbonus.getEvaluationday(), lunarbonus.getSubject());

                }
                if (lunarbonus.getEvaluatenum().equals("PJ104003")) {
                    //副总下一次评价的center个数
                    List<String> lunardetailCenterList = new ArrayList<>();
                    List<OrgTreeVo> OrgTreeVolistowner = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney3().equals(tokenModel.getUserId()))).collect(Collectors.toList());
                    OrgTreeVolistowner = OrgTreeVolistowner.stream().distinct().filter(item -> (item.getMoney4().equals("3"))).collect(Collectors.toList());
                    for (OrgTreeVo orgTreeVo : OrgTreeVolistowner) {
                        detailLevel = orgTreeVo.get_id();
                        lunardetailList.add(detailLevel);
                        //查询一次评价
                        List<Lunarbonus> lunarbonusev = lunarbonusALLList.stream().distinct().filter(item -> (item.getEvaluatenum().equals("PJ104002"))).collect(Collectors.toList());
                        if(lunarbonusev.size() > 0){
                            //获取副总下center
                            List<OrgTreeVo> TreeVoCenterlist = OrgTreeVolist.stream().distinct().filter(item -> (item.get_id().equals(orgTreeVo.get_id()))).collect(Collectors.toList());
                            TreeVoCenterlist = TreeVoCenterlist.stream().distinct().filter(item -> (item.getMoney4().equals("2"))).collect(Collectors.toList());
                            //副总下一次评价的center个数
                            for (OrgTreeVo orgTreecenterVo : TreeVoCenterlist) {
                                detailLevel = orgTreecenterVo.getMoney1();
                                lunardetailCenterList.add(detailLevel);
                            }
                            List<Lunardetail> detalCenter = lunardetailMapper.selectCenterCount(lunardetailCenterList, tokenModel.getUserId(), lunarbonusev.get(0).getLunarbonus_id(),lunarbonus.getEvaluationday(), lunarbonus.getSubject());
                            if(TreeVoCenterlist.size() == detalCenter.size()){//此处无需减1
                                LunarAllVo.setSubmitFlg("1");//可提交
                            }
                        }
                    }
                    detal = lunardetailMapper.selectCenter(lunardetailCenterList, tokenModel.getUserId(), lunarbonus.getLunarbonus_id(),lunarbonus.getEvaluationday(), lunarbonus.getSubjectmon());
                }

        } else {
            String Evaday = lunarbonus.getEvaluationday();
            String Submon = lunarbonus.getSubject();
            detal = lunardetailMapper.selectResult(Evaday, Submon, tokenModel.getUserId());
        }
        LunarAllVo.setLunardetail(detal);
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
            orgtreevo.set_id(org.get_id());//center_id-副总
            orgtreevo.setMoney1("");//group_id-center
            orgtreevo.setMoney2("");//team_id-group
            orgtreevo.setMoney3(org.getUser());//负责人
            orgtreevo.setMoney4("3");//center-副总
            OrgTreeVolist.add(orgtreevo);
            if(org.getOrgs() != null){
                for (OrgTree org1 : org.getOrgs()) {//group-center
                    orgtreevo = new OrgTreeVo();
                    orgtreevo.set_id(org.get_id());//center_id-副总
                    orgtreevo.setMoney1(org1.get_id());//group_id-center
                    orgtreevo.setMoney2("");//team_id-group
                    orgtreevo.setMoney3(org1.getUser());//负责人
                    orgtreevo.setMoney4("2");//group-center
                    OrgTreeVolist.add(orgtreevo);
                    if(org1.getOrgs() != null){
                        for (OrgTree org2 : org1.getOrgs()) {//team
                            orgtreevo = new OrgTreeVo();
                            orgtreevo.set_id(org.get_id());//center_id-副总
                            orgtreevo.setMoney1(org1.get_id());//group_id-center
                            orgtreevo.setMoney2(org2.get_id());//team_id-group
                            orgtreevo.setMoney3(org2.getUser());//负责人
                            orgtreevo.setMoney4("1");//team-group
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
        roles = roles.replace("副总经理","副总");
        String Grpup_id = "";

        if (StringUtils.isNullOrEmpty(lunarbonus.getEvaluatenum())) {

            //region 人事给每个GM发送待办进行一次评价 仅人事/薪资操作阶段
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
                lunarbonus.setEvaluatenum("PJ104001");//一次評価
                lunarbonus.preUpdate(tokenModel);
                lunarbonusMapper.updateByPrimaryKeySelective(lunarbonus);
            }
            //endregion 人事给每个GM发送待办进行一次评价 仅人事/薪资操作阶段

        }
        else{
            //region GM给下一级CENTER发待办  仅GM职操作阶段
            if (lunarbonus.getEvaluatenum().equals("PJ104001") && (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理"))) {
                List<OrgTreeVo> TreeVolistTLGroup = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("1") && item.getMoney3().equals(tokenModel.getUserId()))).collect(Collectors.toList());
                for (OrgTreeVo orgTreeVo : TreeVolistTLGroup) {
                    List<OrgTreeVo> TreeVolistGroup = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("2") && item.getMoney1().equals(orgTreeVo.getMoney1()))).collect(Collectors.toList());
                    Grpup_id = TreeVolistGroup.get(0).getMoney3();
                }

                //复制一次评价的数据给二次评价
                String strLunarbonus_id = UUID.randomUUID().toString();
                Lunarbonus bonus = new Lunarbonus();
                bonus.setLunarbonus_id(lunarbonus.getLunarbonus_id());
                bonus = lunarbonusMapper.select(bonus).get(0);
                Lunarbonus bonusone = new Lunarbonus();
                bonusone.setEvaluationday(bonus.getEvaluationday());
                bonusone.setSubject(bonus.getSubject());
                bonusone.setEvaluatenum("PJ104002");//二次评价
                //查询二次评价是否存在
                List<Lunarbonus> bonustwo = lunarbonusMapper.select(bonusone);
                if (bonustwo.size() == 0) {
                    bonus.setEvaluatenum("PJ104002");//二次评价
                    bonus.setTenantid("0");
                    bonus.preInsert(tokenModel);
                    bonus.setLunarbonus_id(strLunarbonus_id);
                    lunarbonusMapper.insert(bonus);
                }
                else{
                    strLunarbonus_id = bonustwo.get(0).getLunarbonus_id();
                }

                //给group发送代办
                ToDoNotice toDoNotice = new ToDoNotice();
                toDoNotice.setTitle("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice.setInitiator(tokenModel.getUserId());
                toDoNotice.setContent("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice.setDataid(strLunarbonus_id);
                toDoNotice.preInsert(tokenModel);
                toDoNotice.setUrl("/PFANS2027View");
                toDoNotice.setWorkflowurl("/PFANS2027View");
                toDoNotice.setOwner(Grpup_id);
                toDoNoticeService.save(toDoNotice);

                //修改评价详情状态
                String Process = "2";
                String Gpid = TreeVolistTLGroup.get(0).getMoney2();
                lunardetailMapper.updateProcesG(Process, lunarbonus.getLunarbonus_id(),Gpid);

                //变更评价提交人代办状态
                toDoNotice = new ToDoNotice();
                toDoNotice.setDataid(lunarbonus.getLunarbonus_id());
                toDoNotice.setOwner(tokenModel.getUserId());
                List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice);
                if (rst1.size() > 0) {
                    for (ToDoNotice item : rst1) {
                        item.setStatus(AuthConstants.TODO_STATUS_DONE);
                        toDoNoticeService.updateNoticesStatus(item);
                    }
                }
                //复制一次评价详情的值给二次评价
                Lunardetail detail = new Lunardetail();
                detail.setLunarbonus_id(lunarbonus.getLunarbonus_id());
                detail.setGroup_id(Gpid);
                List<Lunardetail> LunardetailList = lunardetailMapper.select(detail);
                if(LunardetailList.size() > 0){
                    for (Lunardetail Lu : LunardetailList) {
                        Lu.setLunarbonus_id(strLunarbonus_id);
                        Lu.setLunardetail_id(UUID.randomUUID().toString());
                        lunardetailMapper.insert(Lu);
                    }
                }
            }
            //endregion GM给下一级CENTER发待办  仅GM职操作阶段

            //region CENTER给下一级副总发待办  仅CENTER职操作阶段
            if (lunarbonus.getEvaluatenum().equals("PJ104002") && (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理"))) {
                List<OrgTreeVo> TreeVolistGroupCenter = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("2") && item.getMoney3().equals(tokenModel.getUserId()))).collect(Collectors.toList());
                for (OrgTreeVo orgTreeVo: TreeVolistGroupCenter) {
                    List<OrgTreeVo> TreeVolistCenter = OrgTreeVolist.stream().distinct().filter(item -> (item.getMoney4().equals("3") && item.get_id().equals(orgTreeVo.get_id()))).collect(Collectors.toList());
                    Grpup_id = TreeVolistCenter.get(0).getMoney3();
                }

                //复制二次评价的数据给三次评价
                String strLunarbonus_id = UUID.randomUUID().toString();
                Lunarbonus bonus = new Lunarbonus();
                bonus.setLunarbonus_id(lunarbonus.getLunarbonus_id());
                bonus = lunarbonusMapper.select(bonus).get(0);
                Lunarbonus bonusone = new Lunarbonus();
                bonusone.setEvaluationday(bonus.getEvaluationday());
                bonusone.setSubject(bonus.getSubject());
                bonusone.setEvaluatenum("PJ104003");//三次评价
                //查询二次评价是否存在
                List<Lunarbonus> bonustwo = lunarbonusMapper.select(bonusone);
                if (bonustwo.size() == 0) {
                    bonus.setEvaluatenum("PJ104003");//三次评价
                    bonus.setTenantid("0");
                    bonus.preInsert(tokenModel);
                    bonus.setLunarbonus_id(strLunarbonus_id);
                    lunarbonusMapper.insert(bonus);
                }
                else{
                    strLunarbonus_id = bonustwo.get(0).getLunarbonus_id();
                }

                ToDoNotice toDoNotice1 = new ToDoNotice();
                toDoNotice1.setTitle("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice1.setInitiator(tokenModel.getUserId());
                toDoNotice1.setContent("您有月度赏与评价需要确认，请注意查看。");
                toDoNotice1.setDataid(strLunarbonus_id);
                toDoNotice1.preInsert(tokenModel);
                toDoNotice1.setUrl("/PFANS2027View");
                toDoNotice1.setWorkflowurl("/PFANS2027View");
                toDoNotice1.setOwner(Grpup_id);
                toDoNoticeService.save(toDoNotice1);

                //修改评价详情状态
                String Process = "3";
                String centerid = TreeVolistGroupCenter.get(0).getMoney1();
                lunardetailMapper.updateProcesC(Process, lunarbonus.getLunarbonus_id(),centerid);

                //变更评价提交人代办状态
                toDoNotice1 = new ToDoNotice();
                toDoNotice1.setDataid(lunarbonus.getLunarbonus_id());
                toDoNotice1.setOwner(tokenModel.getUserId());
                List<ToDoNotice> rst1 = toDoNoticeService.get(toDoNotice1);
                if (rst1.size() > 0) {
                    for (ToDoNotice item : rst1) {
                        item.setStatus(AuthConstants.TODO_STATUS_DONE);
                        toDoNoticeService.updateNoticesStatus(item);
                    }
                }
                //复制二次评价的值给三次评价
                Lunardetail detail = new Lunardetail();
                detail.setLunarbonus_id(lunarbonus.getLunarbonus_id());
                detail.setCenter_id(centerid);
                List<Lunardetail> LunardetailList = lunardetailMapper.select(detail);
                if(LunardetailList.size() > 0){
                    for (Lunardetail Lu : LunardetailList) {
                        Lu.setLunarbonus_id(strLunarbonus_id);
                        Lu.setLunardetail_id(UUID.randomUUID().toString());
                        lunardetailMapper.insert(Lu);
                    }
                }
            }
            //endregion CENTER给下一级副总发待办  仅CENTER职操作阶段

            //region 副总给下一级总经理发待办  仅副总职操作阶段
            if (lunarbonus.getEvaluatenum().equals("PJ104003") && (!roles.contains("人事总务部长") && !roles.contains("工资计算担当") && !roles.contains("总经理"))) {
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
                //lunardetailMapper.updateProcesC(Process, Crid);
            }
            //endregion 副总给下一级总经理发待办  仅副总职操作阶段

        }

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
