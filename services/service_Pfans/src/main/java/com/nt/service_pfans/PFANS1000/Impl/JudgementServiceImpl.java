package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Business;
import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.Judgementdetail;
import com.nt.dao_Pfans.PFANS1000.Unusedevice;
import com.nt.dao_Pfans.PFANS1000.Vo.JudgementVo;
import com.nt.dao_Pfans.PFANS3000.Purchase;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.JudgementService;
import com.nt.service_pfans.PFANS1000.mapper.JudgementMapper;
import com.nt.service_pfans.PFANS1000.mapper.JudgementdetailMapper;
import com.nt.service_pfans.PFANS1000.mapper.UnusedeviceMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class JudgementServiceImpl implements JudgementService {

    @Autowired
    private JudgementMapper judgementMapper;

    @Autowired
    private UnusedeviceMapper unusedeviceMapper;

    @Autowired
    private JudgementdetailMapper judgementdetailMapper;

    @Autowired
    private BusinessplanService businessplanService;

    @Override
    public List<Judgement> getJudgement(Judgement judgement) {
        return judgementMapper.select(judgement);
    }

    @Override
    public List<Judgement> selectJudgement() throws Exception {
        return judgementMapper.selectJudgement();
    }

    @Override
    public JudgementVo One(String judgementid) throws Exception {
        JudgementVo judgVo = new JudgementVo();
        //设备
        Unusedevice unusedevice = new Unusedevice();

        Judgementdetail judgementdetail = new Judgementdetail();

        unusedevice.setJudgementid(judgementid);

        judgementdetail.setJudgementid(judgementid);

        Judgement judgement = judgementMapper.selectByPrimaryKey(judgementid);

        List<Judgement> judgementLoAntList = judgementMapper.judgementAnt(judgement.getJudgnumbers().substring(0, 13));

        List<Unusedevice> unusedeviceList = unusedeviceMapper.select(unusedevice);
        unusedeviceList = unusedeviceList.stream().sorted(Comparator.comparing(Unusedevice::getRowindex)).collect(Collectors.toList());

        List<Judgementdetail> judgementdetailList = judgementdetailMapper.select(judgementdetail);
        judgementdetailList = judgementdetailList.stream().sorted(Comparator.comparing(Judgementdetail::getRowindex)).collect(Collectors.toList());

        judgVo.setJudgement(judgement);
        judgVo.setUnusedevice(unusedeviceList);
        judgVo.setJudgementdetail(judgementdetailList);
        judgVo.setJudgementLoAntList(judgementLoAntList);

        return judgVo;
    }

    @Override
    public void updateJudgement(JudgementVo judgementVo, TokenModel tokenModel) throws Exception {
        Judgement judgement = new Judgement();
        BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
        judgement.preUpdate(tokenModel);
        Judgement jude = judgementMapper.selectByPrimaryKey(judgement.getJudgementid());
        if(jude.getCareerplan().equals("1") && !jude.getMoney().equals(judgement.getMoney())){
            //金额不统一 旧：jude 新：judgement
            BigDecimal diffMoney = new BigDecimal(judgement.getMoney()).subtract(new BigDecimal(jude.getMoney()));
            if(judgement.getCareerplan().equals("0") || !jude.getRulingid().equals(judgement.getRulingid())){
                businessplanService.cgTpReRulingInfo(jude.getRulingid(), jude.getMoney(), tokenModel);
                if(judgement.getCareerplan().equals("1")){
                    businessplanService.upRulingInfo(judgement.getRulingid(), judgement.getMoney(), tokenModel);
                }
            }else {
                businessplanService.upRulingInfo(judgement.getRulingid(), diffMoney.toString(), tokenModel);
            }
        }
        if (judgement.getStatus().equals("4") && judgement.getJudgnumbers().contains("_")) {
//            原决裁号
            String oldNumAnt = "";
//            原决裁改为  _n
            String numAnt = "";
            int intAnt = 0;
            List<Judgement> judgementlist = judgementMapper.selectAll();
            //对原决裁进行操作
            Judgement judgementAnt = judgementMapper.selectByPrimaryKey(judgementVo.getJudgement().getOldjudgementid());
            oldNumAnt = judgementAnt.getJudgnumbers();
            for (Judgement jmet : judgementlist) {
                if ((jmet.getJudgnumbers().substring(0, 13)).equals(judgementAnt.getJudgnumbers())) {
                    intAnt++;
                }
            }
            numAnt = judgementAnt.getJudgnumbers() + "_" + (intAnt - 1);
            judgementAnt.setJudgnumbers(numAnt);
            judgementMapper.updateByPrimaryKeySelective(judgementAnt);
            //对tmp进行操作
            judgement.setJudgnumbers(oldNumAnt);
            judgementMapper.updateByPrimaryKeySelective(judgement);
        } else {
            judgementMapper.updateByPrimaryKeySelective(judgement);
            String judgementid = judgement.getJudgementid();
            List<Unusedevice> unusedeviceList = judgementVo.getUnusedevice();
            if (unusedeviceList != null) {
                Unusedevice unusedevice = new Unusedevice();
                unusedevice.setJudgementid(judgementid);
                unusedeviceMapper.delete(unusedevice);
                int rowindex = 0;
                for (Unusedevice unu : unusedeviceList) {
                    rowindex = rowindex + 1;
                    unu.preInsert(tokenModel);
                    unu.setUnusedeviceid(UUID.randomUUID().toString());
                    unu.setJudgementid(judgementid);
                    unu.setRowindex(rowindex);
                    unusedeviceMapper.insertSelective(unu);
                }
            }
        }

    }

    @Override
    public void updateJudgementDetail(JudgementVo judgementVo, TokenModel tokenModel) throws Exception {
        Judgement judgement = new Judgement();
        BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
        judgement.preUpdate(tokenModel);
        if(judgementVo.getJudgementdetail().size() > 0){
            for(Judgementdetail jud : judgementVo.getJudgementdetail()){
                Judgementdetail jud_old = judgementdetailMapper.selectByPrimaryKey(jud.getJudgementdetail_id());
                if(jud_old != null){
                    if(jud_old.getCareerplanM().equals("1") && !jud_old.getAmounttobegivenM().equals(jud.getAmounttobegivenM())){
                        //金额不统一 旧：jud_old 新：jud
                        BigDecimal diffMoney = new BigDecimal(jud.getAmounttobegivenM()).subtract(new BigDecimal(jud_old.getAmounttobegivenM()));
                        if(jud.getCareerplanM().equals("0") || !jud_old.getRulingid().equals(jud.getRulingid())){
                            businessplanService.cgTpReRulingInfo(jud_old.getRulingid(), jud_old.getAmounttobegivenM(), tokenModel);
                            if(jud.getCareerplanM().equals("1")){
                                businessplanService.upRulingInfo(jud.getRulingid(), jud.getAmounttobegivenM(), tokenModel);
                            }
                        }else {
                            businessplanService.upRulingInfo(jud.getRulingid(), diffMoney.toString(), tokenModel);
                        }
                    }
                }
            }
        }

        if (judgement.getStatus().equals("4") && judgement.getJudgnumbers().contains("_")) {
//            原决裁号
            String oldNumAnt = "";
//            原决裁改为  _n
            String numAnt = "";
            int intAnt = 0;
            List<Judgement> judgementlist = judgementMapper.selectAll();
            //对原决裁进行操作
            Judgement judgementAnt = judgementMapper.selectByPrimaryKey(judgementVo.getJudgement().getOldjudgementid());
            oldNumAnt = judgementAnt.getJudgnumbers();
            for (Judgement jmet : judgementlist) {
                if ((jmet.getJudgnumbers().substring(0, 13)).equals(judgementAnt.getJudgnumbers())) {
                    intAnt++;
                }
            }
            numAnt = judgementAnt.getJudgnumbers() + "_" + (intAnt - 1);
            judgementAnt.setJudgnumbers(numAnt);
            judgementMapper.updateByPrimaryKeySelective(judgementAnt);
            //对tmp进行操作
            judgement.setJudgnumbers(oldNumAnt);
            judgementMapper.updateByPrimaryKeySelective(judgement);
            List<Judgementdetail> judgementdetailList = judgementVo.getJudgementdetail();
            if (judgementdetailList != null) {
                int rowindex = 0;
                for (Judgementdetail judge : judgementdetailList) {
                    rowindex = rowindex + 1;
                    judge.preInsert(tokenModel);
                    judge.setJudgementdetail_id(UUID.randomUUID().toString());
                    judge.setJudgementid(judgement.getJudgementid());
                    judge.setRowindex(rowindex);
                    judgementdetailMapper.insertSelective(judge);
                }
            }
        } else {
            judgementMapper.updateByPrimaryKeySelective(judgement);
            String judgementid = judgement.getJudgementid();
            List<Unusedevice> unusedeviceList = judgementVo.getUnusedevice();
            if (unusedeviceList != null) {
                Unusedevice unusedevice = new Unusedevice();
                unusedevice.setJudgementid(judgementid);
                unusedeviceMapper.delete(unusedevice);
                int rowindex = 0;
                for (Unusedevice unu : unusedeviceList) {
                    rowindex = rowindex + 1;
                    unu.preInsert(tokenModel);
                    unu.setUnusedeviceid(UUID.randomUUID().toString());
                    unu.setJudgementid(judgementid);
                    unu.setRowindex(rowindex);
                    unusedeviceMapper.insertSelective(unu);
                }
            }
            List<Judgementdetail> judgementdetailList = judgementVo.getJudgementdetail();
            if (judgementdetailList != null) {
                Judgementdetail judgementdetail = new Judgementdetail();
                judgementdetail.setJudgementid(judgementid);
                judgementdetailMapper.delete(judgementdetail);
                int rowindex = 0;
                for (Judgementdetail judge : judgementdetailList) {
                    rowindex = rowindex + 1;
                    judge.preInsert(tokenModel);
                    judge.setJudgementdetail_id(UUID.randomUUID().toString());
                    judge.setJudgementid(judgementid);
                    judge.setRowindex(rowindex);
                    judgementdetailMapper.insertSelective(judge);
                }
            }
        }
    }

    @Override
    public void insert(JudgementVo judgementVo, TokenModel tokenModel) throws Exception {
        String judgementid = UUID.randomUUID().toString();
        Judgement judgement = new Judgement();
        List<Judgement> judgementlist = judgementMapper.selectAll();
        //补充决裁 ztc 0804
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(judgementVo.getJudgement().getSupplementary()) && judgementVo.getJudgement().getSupplementary().equals("1")) {
            String judgementnumberAnt = judgementVo.getJudgement().getJudgnumbers() + "_" + "tmp";
            BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
            judgement.preInsert(tokenModel);
            judgement.setJudgementid(judgementid);
            judgement.setJudgnumbers(judgementnumberAnt);
            judgement.setSupplementary("");
            //原决裁
            judgement.setOldjudgementid(judgementVo.getJudgement().getJudgementid());
            judgementMapper.insertSelective(judgement);
        } else {
            //add-ws-根据当前年月日从001开始增加决裁编号
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String year = sf1.format(date);
            int number = 0;
            String Numbers = "";
            String no = "";
            if (judgementlist.size() > 0) {
                for (Judgement judge : judgementlist) {
                    if (judge.getJudgnumbers() != "" && judge.getJudgnumbers() != null) {
                        String checknumber = StringUtils.uncapitalize(StringUtils.substring(judge.getJudgnumbers(), 2, 10));
                        if (Integer.valueOf(year).equals(Integer.valueOf(checknumber))) {
                            number = number + 1;
                        }
                    }
                }
                if (number <= 8) {
                    no = "00" + (number + 1);
                } else {
                    no = "0" + (number + 1);
                }
            } else {
                no = "001";
            }
            BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
            if (judgement.getEquipment().equals("1")) {
                Numbers = "WC" + year + no;
            } else {
                Numbers = "JC" + year + no;
            }
            //add-ws-根据当前年月日从001开始增加决裁编号
            judgement.preInsert(tokenModel);
            judgement.setJudgementid(judgementid);
            judgement.setJudgnumbers(Numbers);
//        judgement.setEquipment("0");
            judgementMapper.insertSelective(judgement);
            List<Unusedevice> unusedeviceList = judgementVo.getUnusedevice();
            if (unusedeviceList != null) {
//            judgement.setEquipment("1");
                int rowundex = 0;
                for (Unusedevice unu : unusedeviceList) {
                    rowundex = rowundex + 1;
                    unu.preInsert(tokenModel);
                    unu.setUnusedeviceid(UUID.randomUUID().toString());
                    unu.setJudgementid(judgementid);
                    unu.setRowindex(rowundex);
                    unusedeviceMapper.insertSelective(unu);
                }
            }
        }
        //事业计划余额计算
        if(judgementVo.getJudgement().getCareerplan().equals("1")){
            businessplanService.upRulingInfo(judgementVo.getJudgement().getRulingid(), judgementVo.getJudgement().getMoney(), tokenModel);
        }
    }


    @Override
    public void createJudgementDetail(JudgementVo judgementVo, TokenModel tokenModel) throws Exception {
        String judgementid = UUID.randomUUID().toString();
        Judgement judgement = new Judgement();
        //add-ws-根据当前年月日从001开始增加决裁编号
        List<Judgement> judgementlist = judgementMapper.selectAll();
        if (!com.mysql.jdbc.StringUtils.isNullOrEmpty(judgementVo.getJudgement().getSupplementary()) && judgementVo.getJudgement().getSupplementary().equals("1")) {
            String judgementnumberAnt = judgementVo.getJudgement().getJudgnumbers() + "_" + "tmp";
            BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
            judgement.preInsert(tokenModel);
            judgement.setJudgementid(judgementid);
            judgement.setJudgnumbers(judgementnumberAnt);
            judgement.setSupplementary("");
            judgement.setOldjudgementid(judgementVo.getJudgement().getJudgementid());
            judgementMapper.insertSelective(judgement);
            List<Judgementdetail> judgementdetailList = judgementVo.getJudgementdetail();
            if (judgementdetailList != null) {
                int rowindex = 0;
                for (Judgementdetail judge : judgementdetailList) {
                    rowindex = rowindex + 1;
                    judge.preInsert(tokenModel);
                    judge.setJudgementdetail_id(UUID.randomUUID().toString());
                    judge.setJudgementid(judgementid);
                    judge.setRowindex(rowindex);
                    judgementdetailMapper.insertSelective(judge);
                }
            }
        } else {
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String year = sf1.format(date);
            int number = 0;
            String Numbers = "";
            String no = "";
            if (judgementlist.size() > 0) {
                for (Judgement judge : judgementlist) {
                    if (judge.getJudgnumbers() != "" && judge.getJudgnumbers() != null) {
                        String checknumber = StringUtils.uncapitalize(StringUtils.substring(judge.getJudgnumbers(), 2, 10));
                        if (Integer.valueOf(year).equals(Integer.valueOf(checknumber))) {
                            number = number + 1;
                        }
                    }
                }
                if (number <= 8) {
                    no = "00" + (number + 1);
                } else {
                    no = "0" + (number + 1);
                }
            } else {
                no = "001";
            }
            BeanUtils.copyProperties(judgementVo.getJudgement(), judgement);
            Numbers = "JC" + year + no;
            //add-ws-根据当前年月日从001开始增加决裁编号
            judgement.preInsert(tokenModel);
            judgement.setJudgementid(judgementid);
            judgement.setJudgnumbers(Numbers);
//        judgement.setEquipment("0");
            judgementMapper.insertSelective(judgement);
            List<Judgementdetail> JudgementdetailList = judgementVo.getJudgementdetail();
            if (JudgementdetailList != null) {
                int rowundex = 0;
                for (Judgementdetail judge : JudgementdetailList) {
                    rowundex = rowundex + 1;
                    judge.preInsert(tokenModel);
                    judge.setJudgementdetail_id(UUID.randomUUID().toString());
                    judge.setJudgementid(judgementid);
                    judge.setRowindex(rowundex);
                    judgementdetailMapper.insertSelective(judge);
                    if(judge.getCareerplanM().equals("1")){
                        businessplanService.upRulingInfo(judge.getRulingid(), judge.getAmounttobegivenM(), tokenModel);
                    }
                }
            }
        }
    }

    @Override
    public List<Judgement> getJudgementList(Judgement judgement, HttpServletRequest request) throws Exception {
        return judgementMapper.select(judgement);
    }

    //region scc add 10/28 其他业务决裁逻辑删除 from
    @Override
    public void juddelete(Judgement judgement, TokenModel tokenModel) throws Exception {
        Judgement updateStatus = new Judgement();
        updateStatus.setJudgementid(judgement.getJudgementid());
        updateStatus.setStatus("1");
        judgementMapper.updateByPrimaryKeySelective(updateStatus);
        if("1".equals(judgement.getCareerplan())){
            if(!com.mysql.jdbc.StringUtils.isNullOrEmpty(judgement.getRulingid()) && judgement.getMusectosion().equals("0")){
                businessplanService.cgTpReRulingInfo(judgement.getRulingid(),judgement.getMoney(),tokenModel);
            }else if(judgement != null &&  judgement.getMusectosion().equals("1")){
                Judgementdetail judgementdetail = new Judgementdetail();
                judgementdetail.setJudgementid(judgement.getJudgementid());
                List<Judgementdetail> judgementdetails = judgementdetailMapper.select(judgementdetail);
                if(judgementdetails.size() > 0){
                    for(Judgementdetail jud : judgementdetails){
                        businessplanService.cgTpReRulingInfo(jud.getRulingid(),jud.getAmounttobegivenM(),tokenModel);
                    }
                }
            }
            //businessplanService.cgTpReRulingInfo(judgement.getRulingid(),judgement.getMoney(),tokenModel);
        }else{
            return;
        }
    }
    //endregion scc add 10/28 其他业务决裁逻辑删除 to

    //region   add  ml  220112  检索  from
    @Override
    public List<Judgement> getJudgementSearch(Judgement judgement) throws Exception {
        return judgementMapper.getJudgementSearch(judgement);
    }
    //endregion   add  ml  220112  检索  to

}
