package com.nt.service_pfans.PFANS1000.Impl;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import com.nt.dao_Pfans.PFANS1000.Communication;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.service_pfans.PFANS1000.BusinessplanService;
import com.nt.service_pfans.PFANS1000.CommunicationService;
import com.nt.service_pfans.PFANS1000.mapper.CommunicationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.date.DateUtil;
import com.nt.utils.StringUtils;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommunicationServiceImpl implements CommunicationService {

    @Autowired
    private CommunicationMapper communicationMapper;

    @Autowired
    private BusinessplanService businessplanService;

    @Override
    public List<Communication> selectCommunication() throws Exception {
        return communicationMapper.selectCommunication();
    }
    @Override
    public List<Communication> getCommunication(Communication communication) {

        return communicationMapper.select(communication);
    }

    @Override
    public Communication One(String communication_id) throws Exception {

        return communicationMapper.selectByPrimaryKey(communication_id);
    }

    @Override
    public void updateCommunication(Communication communication, TokenModel tokenModel) throws Exception {
        communication.preUpdate(tokenModel);
        communicationMapper.updateByPrimaryKey(communication);
    }

    @Override
    public void insert(Communication communication, TokenModel tokenModel) throws Exception {
        //add-ws-5/27-No.170
        List<Communication> companyProjectslist = communicationMapper.selectAll();
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String year = sf1.format(date);
        int number = 0;
        String Numbers = "";
        String no = "";
        if(companyProjectslist.size()>0){
            for(Communication communi :companyProjectslist){
                if(communi.getNumbercation()!="" && communi.getNumbercation()!=null){
                    String checknumber = StringUtils.uncapitalize(StringUtils.substring(communi.getNumbercation(), 3,11));
                    if(Integer.valueOf(year).equals(Integer.valueOf(checknumber))){
                        number = number+1;
                    }
                }
            }
            if(number<=8){
                no="00"+(number + 1);
            }else{
                no="0"+(number + 1);
            }
        }else{
            no = "001";
        }
        Numbers = "JJF"+year+ no;
        communication.setNumbercation(Numbers);
        //add-ws-5/27-No.170
        communication.preInsert(tokenModel);
        communication.setCommunication_id(UUID.randomUUID().toString()) ;
        communicationMapper.insert(communication);
    }

    //region scc add 10/28 交际费事前决裁逻辑删除 from
    @Override
    public void comdelete(Communication communication, TokenModel tokenModel) throws Exception {
        Communication updateStatus = new Communication();
        updateStatus.setCommunication_id(communication.getCommunication_id());
        updateStatus.setStatus("1");
        communicationMapper.updateByPrimaryKeySelective(updateStatus);
        if("1".equals(communication.getPlan())){
            businessplanService.cgTpReRulingInfo(communication.getRulingid(),communication.getMoneys(),tokenModel);
        }else{
            return;
        }
    }
    //endregion scc add 10/28 交际费事前决裁逻辑删除 to

}
