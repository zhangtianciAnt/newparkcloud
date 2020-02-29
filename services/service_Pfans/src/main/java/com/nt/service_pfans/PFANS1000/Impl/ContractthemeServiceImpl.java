package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.service_pfans.PFANS1000.ContractthemeService;
import com.nt.service_pfans.PFANS1000.mapper.ContractthemeMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ContractthemeServiceImpl implements ContractthemeService {

    @Autowired
    private ContractthemeMapper contractthemeMapper;

    @Override
    public List<Contracttheme> get(Contracttheme contracttheme) {
        return contractthemeMapper.select(contracttheme);
    }

    @Override
    public List<Contracttheme> one(Contracttheme contracttheme) throws Exception {
        List<Contracttheme> conList = contractthemeMapper.select(contracttheme);
        conList = conList.stream().sorted(Comparator.comparing(Contracttheme::getRowindex)).collect(Collectors.toList());
        return conList;
    }

    @Override
    public void update(List<Contracttheme> contracttheme, TokenModel tokenModel) throws Exception {
        int rowindex = 0;
        for(int i = 0; i < contracttheme.size(); i ++){
            rowindex = rowindex + 1;
            Contracttheme co = contracttheme.get(i);
            if(!co.getContractthemeid().equals("")){
                co.preUpdate(tokenModel);
                co.setRowindex(String.valueOf(rowindex));
                contractthemeMapper.updateByPrimaryKey(co);
            }
            else{
                co.preInsert(tokenModel);
                co.setContractthemeid(UUID.randomUUID().toString());
                co.setRowindex(String.valueOf(rowindex));
                contractthemeMapper.insert(co);
            }
        }
    }

    @Override
    public void insert(List<Contracttheme> contracttheme, TokenModel tokenModel) throws Exception {
        Contracttheme con = new Contracttheme();
        con.setYears(contracttheme.get(0).getYears());
        List<Contracttheme> conList = contractthemeMapper.select(con);
        int rowindex = 0;
        for(int i = 0; i < contracttheme.size(); i ++){
            rowindex = rowindex + 1;
            Contracttheme co = contracttheme.get(i);
            if(!co.getContractthemeid().equals("")){
                if(conList.size() > 0){
                    for (Contracttheme conlist : conList) {
                        if(co.getContractthemeid().equals(conlist.getContractthemeid())){
                            co.preUpdate(tokenModel);
                            co.setRowindex(String.valueOf(rowindex));
                            contractthemeMapper.updateByPrimaryKey(co);
                        }
                        else{
                            contractthemeMapper.delete(conlist);
                        }
                    }
                }
                else{
                    co.preUpdate(tokenModel);
                    co.setRowindex(String.valueOf(rowindex));
                    contractthemeMapper.updateByPrimaryKey(co);
                }
            }
            else{
                co.preInsert(tokenModel);
                co.setContractthemeid(UUID.randomUUID().toString());
                co.setRowindex(String.valueOf(rowindex));
                contractthemeMapper.insert(co);
            }
        }
    }
}
