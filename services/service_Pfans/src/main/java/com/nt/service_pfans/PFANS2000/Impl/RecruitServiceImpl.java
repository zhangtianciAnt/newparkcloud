package com.nt.service_pfans.PFANS2000.Impl;

import com.nt.dao_Pfans.PFANS2000.Recruit;
import com.nt.dao_Pfans.PFANS5000.CompanyProjects;
import com.nt.service_pfans.PFANS2000.RecruitService;
import com.nt.service_pfans.PFANS2000.mapper.RecruitMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Override
    public List<Recruit> getRecruit(Recruit recruit) {
        return recruitMapper.select(recruit);
    }

    @Override
    public Recruit One(String recruitid) throws Exception {

        return recruitMapper.selectByPrimaryKey(recruitid);
    }

    @Override
    public void updateRecruit(Recruit recruit, TokenModel tokenModel) throws Exception {
        recruitMapper.updateByPrimaryKey(recruit);
    }

    @Override
    public void insert(Recruit recruit, TokenModel tokenModel) throws Exception {
//add-ws-8/4-禅道任务296--
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        List<Recruit> recruitlist = recruitMapper.selectAll();
        String year = sf1.format(date);
        int number = 0;
        String Numbers = "";
        String no = "";
        if (recruitlist.size() > 0) {
            for (Recruit recr : recruitlist) {
                if (recr.getNumbers() != "" && recr.getNumbers() != null) {
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(recr.getNumbers(), 2, 10));
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
        Numbers = "ZP" + year + no;
        //add-ws-8/4-禅道任务296--
        recruit.preInsert(tokenModel);
        recruit.setNumbers(Numbers);
        recruit.setRecruitid(UUID.randomUUID().toString());
        recruitMapper.insert(recruit);
    }

    @Override
    public List<Recruit> getRecruitList(Recruit recruit, HttpServletRequest request) throws Exception {

        return recruitMapper.select(recruit);
    }
}
