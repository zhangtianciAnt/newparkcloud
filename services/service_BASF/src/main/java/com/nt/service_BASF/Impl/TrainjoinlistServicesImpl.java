package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.Programlist;
import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.Trainjoinlist;
import com.nt.dao_BASF.VO.OverduePersonnelListVo;
import com.nt.dao_BASF.VO.TrainingRecordsExportVo;
import com.nt.dao_BASF.VO.TrainjoinlistVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.service_BASF.TrainjoinlistServices;
import com.nt.service_BASF.mapper.ProgramlistMapper;
import com.nt.service_BASF.mapper.StartprogramMapper;
import com.nt.service_BASF.mapper.TrainingRecordsExportMapper;
import com.nt.service_BASF.mapper.TrainjoinlistMapper;
import com.nt.service_Org.OrgTreeService;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
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
import java.util.*;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: TrainjoinlistServicesImpl
 * @Author: Newtouch
 * @Description: 培训参加名单接口实现类
 * @Date: 2020/1/7 11:14
 * @Version: 1.0
 */
@Service
public class TrainjoinlistServicesImpl implements TrainjoinlistServices {

    @Autowired
    private TrainjoinlistMapper trainjoinlistMapper;

    @Autowired
    private StartprogramMapper startprogramMapper;

    @Autowired
    private ProgramlistMapper programlistMapper;

    @Autowired
    private TrainingRecordsExportMapper trainingRecordsExportMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrgTreeService orgTreeService;

    String updepid = "";
    public String getupDepid (List<OrgTree> orgs, String departmentid) {


        for(int j = 0;j<orgs.size();j++)
        {
            if(orgs.get(j).get_id() !=null)
            {
                if (StringUtils.equals(orgs.get(j).get_id(), departmentid)) {
                    if (StringUtils.isEmpty(orgs.get(j).getUpcompanyid())) {
                        updepid = orgs.get(j).get_id();
                    } else {
                        updepid = orgs.get(j).getUpcompanyid();
                    }
                    break;
                }
                else
                {
                    if(orgs.get(j).getOrgs()!= null && orgs.get(j).getOrgs().size()>0)
                    {
                        updepid = getupDepid(orgs.get(j).getOrgs(),departmentid);
                    }
                }
            }
            else
            {
                if(orgs.get(j).getOrgs()!= null && orgs.get(j).getOrgs().size()>0)
                {
                    updepid = getupDepid(orgs.get(j).getOrgs(),departmentid);
                }
            }

        }
        return updepid;
    }

    //添加培训人员名单
    @Override
    public void insert(TrainjoinlistVo trainjoinlistVo, TokenModel tokenModel) throws Exception {
        if (StringUtils.isNotBlank(trainjoinlistVo.getStartprogramid()) && trainjoinlistVo.getTrainjoinlists() != null) {
            //添加之前先删除之前的
            delete(trainjoinlistVo.getStartprogramid(), tokenModel);
            OrgTree orgTree = new OrgTree();
            List<OrgTree> orglist = orgTreeService.getById(orgTree);
            //循环添加
            for (Trainjoinlist trainjoinlist : trainjoinlistVo.getTrainjoinlists()) {
                if (trainjoinlist.getPersonnelid() != null && trainjoinlist.getPersonnelid().trim() != "") {
                    trainjoinlist.setStartprogramid(trainjoinlistVo.getStartprogramid());
                    trainjoinlist.preInsert(tokenModel);
                    trainjoinlist.setTrainjoinlistid(UUID.randomUUID().toString());
                    trainjoinlist.setNumber(1);

                    String depid = trainjoinlist.getDepartmentid();
                    String updepid =getupDepid(orglist,depid);
                    trainjoinlist.setRootdepid(updepid);
                    trainjoinlistMapper.insert(trainjoinlist);
                }
            }
        }
    }

    //    更新培训人员名单
    @Override
    public void updata(Trainjoinlist trainjoinlist, TokenModel tokenModel) throws Exception {
        trainjoinlist.preUpdate(tokenModel);
        trainjoinlistMapper.updateByPrimaryKeySelective(trainjoinlist);
    }

    //在线培训更新人员
    @Override
    public void updataOnline(Trainjoinlist trainjoinlist, TokenModel tokenModel) throws Exception {
        Trainjoinlist trainjoinlist1 = new Trainjoinlist();
        trainjoinlist1.setTrainjoinlistid(trainjoinlist.getTrainjoinlistid());
        trainjoinlist1 = trainjoinlistMapper.selectOne(trainjoinlist1);
        if (trainjoinlist1.getNumber() == null) {
            trainjoinlist.setNumber(1);
        } else {
            trainjoinlist.setNumber(trainjoinlist1.getNumber() + 1);
        }
        trainjoinlist.preUpdate(tokenModel);
        trainjoinlistMapper.updateByPrimaryKeySelective(trainjoinlist);
    }


    //在线培训添加人员名单
    @Override
    public String onlineInsert(TrainjoinlistVo trainjoinlistVo, TokenModel tokenModel) throws Exception {
        if (StringUtils.isNotBlank(trainjoinlistVo.getStartprogramid()) && trainjoinlistVo.getTrainjoinlists() != null) {
            //循环添加
            for (Trainjoinlist trainjoinlist : trainjoinlistVo.getTrainjoinlists()) {
                if (trainjoinlist.getPersonnelid() != null && trainjoinlist.getPersonnelid().trim() != "") {
                    trainjoinlist.setStartprogramid(trainjoinlistVo.getStartprogramid());
                    Trainjoinlist trainjoinlist1 = new Trainjoinlist();
                    trainjoinlist1.setStartprogramid(trainjoinlistVo.getStartprogramid());
                    trainjoinlist1.setPersonnelid(trainjoinlist.getPersonnelid());
                    trainjoinlist1.setStatus("0");
                    if (trainjoinlistMapper.selectCount(trainjoinlist1) > 0) {
                        Trainjoinlist trainjoinlist2 = trainjoinlistMapper.selectOne(trainjoinlist1);
                        trainjoinlist2.preUpdate(tokenModel);
                        trainjoinlist2.setJointype("正常");
                        trainjoinlistMapper.updateByPrimaryKeySelective(trainjoinlist2);
                        return trainjoinlist2.getTrainjoinlistid();
                    } else {
                        //培训人员添加
                        trainjoinlist.preInsert(tokenModel);
                        trainjoinlist.setTrainjoinlistid(UUID.randomUUID().toString());
                        trainjoinlist.setJointype("正常");
                        trainjoinlist.setNumber(0);
                        trainjoinlistMapper.insert(trainjoinlist);
                        //培训清单培训人数添加
                        try {
                            Startprogram startprogram = new Startprogram();
                            startprogram.setStartprogramid(trainjoinlistVo.getStartprogramid());
                            startprogram = startprogramMapper.selectOne(startprogram);
                            Programlist programlist = new Programlist();
                            programlist.setProgramlistid(startprogram.getProgramlistid());
                            programlist = programlistMapper.selectOne(programlist);
                            programlist.setThispeople(String.valueOf(Integer.parseInt(programlist.getThispeople()) + 1));
                            programlistMapper.updateByPrimaryKeySelective(programlist);
                        } catch (Exception e) {
                            return trainjoinlist.getTrainjoinlistid();
                        }
                        return trainjoinlist.getTrainjoinlistid();
                    }

                }
            }
        }
        return null;
    }

    //根据培训列表id删除参加名单
    @Override
    public void delete(String startprogramid, TokenModel tokenModel) throws Exception {
        if (StringUtils.isNotBlank(startprogramid)) {
            Trainjoinlist trainjoinlist = new Trainjoinlist();
            trainjoinlist.setStartprogramid(startprogramid);
            for (Trainjoinlist trainjoinlist1 : trainjoinlistMapper.select(trainjoinlist)) {
                trainjoinlistMapper.delete(trainjoinlist1);
            }
        }
    }

    //检测是否参加过此培训
    @Override
    public boolean verifyTrai(Trainjoinlist trainjoinlist) throws Exception {
        if (StringUtils.isNotEmpty(trainjoinlist.getPersonnelid()) && StringUtils.isNotEmpty(trainjoinlist.getStartprogramid())) {
            trainjoinlist.setStatus("0");
            int num = trainjoinlistMapper.selectCount(trainjoinlist);
            if (num == 1)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }


    //获取培训申请人员id名单(用户表中的详细信息，用于填充前端表格)
    @Override
    public List<CustomerInfo> joinlist(String startprogramid) throws Exception {
        List<CustomerInfo> customerInfos = new ArrayList<>();
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setStatus("0");
        List<Trainjoinlist> trainjoinlists = trainjoinlistMapper.select(trainjoinlist);
        for (Trainjoinlist trainjoinlist1 : trainjoinlists) {
            Query query = new Query();
            if (StringUtils.isNotEmpty(trainjoinlist1.getPersonnelid())) {
                query.addCriteria(Criteria.where("_id").is(trainjoinlist1.getPersonnelid()));
                customerInfos.add(mongoTemplate.findOne(query, CustomerInfo.class));
            } else {
                break;
            }
        }
        return customerInfos;
    }

    //获取培训申请人员名单
    @Override
    public List<Trainjoinlist> joinlists(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setStatus("0");
        List<Trainjoinlist> trainjoinlists = trainjoinlistMapper.select(trainjoinlist);
        return trainjoinlists;
    }

    //根据人员id获取培训列表id
    @Override
    public List<Trainjoinlist> startprogramidList(String personnelid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setPersonnelid(personnelid);
        trainjoinlist.setStatus("0");
        return trainjoinlistMapper.select(trainjoinlist);
    }

    //根据培训列表主键获取申请人员总数
    @Override
    public int joinnumber(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setStatus("0");
        return trainjoinlistMapper.selectCount(trainjoinlist);
    }

    //根据培训列表主键获取实际参加人数
    @Override
    public int actualjoinnumber(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setJointype("正常");
        trainjoinlist.setStatus("0");
        return trainjoinlistMapper.selectCount(trainjoinlist);
    }

    //根据培训主键获取实际参加通过的人数
    @Override
    public int throughjoinnumber(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setJointype("正常");
        trainjoinlist.setStatus("0");
        trainjoinlist.setThroughtype("通过");
        return trainjoinlistMapper.selectCount(trainjoinlist);
    }

    //在线培训用，通过和不通过人数的和
    @Override
    public int throughAndNoThrough(String startprogramid) throws Exception {
        Trainjoinlist trainjoinlist = new Trainjoinlist();
        trainjoinlist.setStartprogramid(startprogramid);
        trainjoinlist.setJointype("正常");
        trainjoinlist.setStatus("0");
        trainjoinlist.setThroughtype("通过");
        int through = trainjoinlistMapper.selectCount(trainjoinlist);
        trainjoinlist.setThroughtype("未通过");
        int NoThrough = trainjoinlistMapper.selectCount(trainjoinlist);
        return through + NoThrough;
    }

    //excel成绩文档导入
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importexcel(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        //当前的培训参加名单的id
        String trainjoinlistid = "";
        //存放消息
        List<String> result = new ArrayList<>();
        //存放成绩信息list
        HashMap<Trainjoinlist, String> trainHashMap = new HashMap<Trainjoinlist, String>();
        //临时文件
        File f = null;
        //插入成功数
        int successCount = 0;
        //插入失败数
        int errorCount = 0;
        //excel集合
        List<List<Object>> list;
        //当前执行行数
        int k = 1;
        try {
            //转换成文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取并存储为临时文件时发生未知异常！");
            return result;
        }
        try {
            //读取excel临时文件
            ExcelReader reader = ExcelUtil.getReader(f);
            //读取到的excel集合
            list = reader.read();
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取Excel临时文件时发生未知异常！");
            return result;
        }
        try {
            //判断是否是空文件
            if (list == null) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("空文件，请上传正确的成绩文件");
                return result;
            }
            //判断是否是正确格式的成绩文件(方案一：检查行数是否足够)
            if (list.size() < 2) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("成绩文件格式不正确！");
                return result;
            }
            //判断是否是正确格式的成绩文件（方案二：检查表头是否正确）
            if (list.get(0).size() < 12) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("成绩文件表头格式不正确！");
                return result;
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("成绩文件格式不正确！");
            return result;
        }
        try {
            //循环获取成绩信息
            for (int i = 1; i < list.size(); i++) {
                k += 1;
                List<Object> olist = list.get(i);
                //判断这条数据列数是否足够
                try {
                    if (olist.size() < 7) {
                        result.add("成绩表" + k + "行数据异常，导入系统失败！");
                        errorCount += 1;
                        continue;
                    } else if (!StringUtils.isNotBlank(olist.get(0).toString())) {
                        result.add("成绩表" + k + "行数据异常，导入系统失败！");
                        errorCount += 1;
                        continue;
                    } else {
                        if (!StringUtils.isNotBlank(trainjoinlistMapper.selectByPrimaryKey(olist.get(0).toString()).getTrainjoinlistid())) {
                            result.add("成绩表" + k + "行数据异常，导入系统失败！");
                            errorCount += 1;
                            continue;
                        } else {
                            Trainjoinlist trainjoinlist = trainjoinlistMapper.selectByPrimaryKey(olist.get(0).toString());
                            trainjoinlist.setTrainjoinlistid(olist.get(0).toString());
                            //理论成绩
                            try {
                                trainjoinlist.setPerformance(olist.get(5).toString());
                            } catch (Exception e) {
                                trainjoinlist.setPerformance("");
                            }
                            //实操成绩
                            try {
                                trainjoinlist.setActualperformance(olist.get(6).toString());
                            } catch (Exception e) {
                                trainjoinlist.setActualperformance("");
                            }
                            //参加状态
                            trainjoinlist.setJointype(olist.get(7).toString());
                            //通过状态
                            try {
                                if (olist.get(7).toString().trim().equals("正常")) {
                                    if (StringUtils.isEmpty(olist.get(8).toString())) {

                                    } else {
                                        if (!olist.get(8).toString().trim().equals("通过") && !olist.get(8).toString().trim().equals("未通过")) {
                                            result.add("成绩表" + k + "行数据异常，通过状态错误，导入系统失败！");
                                            errorCount += 1;
                                            continue;
                                        } else
                                            trainjoinlist.setThroughtype(olist.get(8).toString());
                                    }
                                }
                            } catch (Exception e) {

                            }
                            //证书编号
                            try {
                                trainjoinlist.setCertificateno(olist.get(9).toString());
                            } catch (Exception e) {
                                trainjoinlist.setCertificateno("");
                            }
                            //发证日期
                            try {
                                trainjoinlist.setIssuedate(olist.get(10).toString());
                            } catch (Exception e) {
                                trainjoinlist.setIssuedate("");
                            }
                            //备注
                            try {
                                trainjoinlist.setRemark(olist.get(11).toString());
                            } catch (Exception e) {
                                trainjoinlist.setRemark("");
                            }
                            trainjoinlist.preUpdate(tokenModel);
                            trainjoinlistMapper.updateByPrimaryKeySelective(trainjoinlist);
                            trainjoinlistid = trainjoinlist.getTrainjoinlistid();
                            successCount += 1;
                        }
                    }
                } catch (Exception e) {
                    result.add("成绩表" + k + "行数据异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }

            }
            result.add("失败数：" + errorCount);
            result.add("成功数：" + successCount);
            upActualProgramlist(trainjoinlistid);
            return result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    //导入excerl后更新培训计划清单的本次培训人数
    private void upActualProgramlist(String trainjoinlistid) throws Exception {
        if (StringUtils.isNotEmpty(trainjoinlistid)) {
            Startprogram startprogram = startprogramMapper.selectByPrimaryKey(trainjoinlistMapper.selectByPrimaryKey(trainjoinlistid).getStartprogramid());
            int actualjoinnumber = actualjoinnumber(startprogram.getStartprogramid());
            Programlist programlist = new Programlist();
            programlist.setProgramlistid(startprogram.getProgramlistid());
            programlist.setThispeople(Integer.toString(actualjoinnumber));
            programlistMapper.updateByPrimaryKeySelective(programlist);
        } else {
            return;
        }
    }

    //即将到期人员列表（前端培训教育大屏用）
    @Override
    public List<OverduePersonnelListVo> overduepersonnellist() throws Exception {
        List<OverduePersonnelListVo> overduePersonnelListVoList = trainjoinlistMapper.OverduePersonnelList();

        for (int i = 0; i < overduePersonnelListVoList.size(); i++) {
            //填充姓名
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(overduePersonnelListVoList.get(i).getPersonnelid()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                if (StringUtils.isNotBlank(customerInfo.getUserinfo().getCustomername())) {
                    overduePersonnelListVoList.get(i).setCustomername(customerInfo.getUserinfo().getCustomername());
                }
            }
        }
        return overduePersonnelListVoList;
    }

    //结果发布判断该培训是否存在正常参加人员通过状态为空
    @Override
    public boolean isNotThroughtype(String startprogramid) throws Exception {
        int count = trainjoinlistMapper.isNotThroughtype(startprogramid);
        if (count > 0) {
            return false;
        } else {
            return true;
        }
    }

    //获取参加培训的人员id们
    @Override
    public List<String> joinPersonnelid() throws Exception {
        return trainjoinlistMapper.joinPersonnelid();
    }

    //获取参加过培训的人员信息（用于培训档案）
    @Override
    public List<CustomerInfo> joinPersonnel(List<String> joinPersonnelid) throws Exception {
        List<CustomerInfo> customerInfos = new ArrayList<>();
        //获取当年年份
        Calendar cal = Calendar.getInstance();
        String year = Integer.toString(cal.get(Calendar.YEAR));

        for (String personnelid : joinPersonnelid) {
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(personnelid));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                Double thelength = startprogramMapper.getTrainThelength(year, personnelid);
                if (thelength != null) {
                    customerInfo.getUserinfo().setAccumulated(Double.toString(thelength));
                } else {
                    customerInfo.getUserinfo().setAccumulated(Double.toString(0.0));
                }
                customerInfos.add(customerInfo);
            }
        }
        return customerInfos;
    }

    //根据用户id填充姓名（结果发布用）
    @Override
    public List<Trainjoinlist> addUserName(List<Trainjoinlist> trainjoinlists) throws Exception {
        for (int i = 0; i < trainjoinlists.size(); i++) {
            //填充姓名
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(trainjoinlists.get(i).getPersonnelid()));
            CustomerInfo customerInfo = mongoTemplate.findOne(query, CustomerInfo.class);
            if (customerInfo != null) {
                if (StringUtils.isNotBlank(customerInfo.getUserinfo().getCustomername())) {
                    trainjoinlists.get(i).setCustomername(customerInfo.getUserinfo().getCustomername());
                }
            }
        }
        return trainjoinlists;
    }

    //培训档案导出
    @Override
    public List<TrainingRecordsExportVo> exportRecordData() throws Exception{
        return trainingRecordsExportMapper.exportRecordData();
    }

    //即将到期培训导出
    @Override
    public List<OverduePersonnelListVo> exportData() throws Exception{
        return trainjoinlistMapper.OverduePersonnelList();
    }
}
