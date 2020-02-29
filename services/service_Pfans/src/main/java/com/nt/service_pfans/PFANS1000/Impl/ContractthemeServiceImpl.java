package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
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
    public List<Contracttheme> get(Contracttheme contracttheme) throws Exception {
        List<Contracttheme> conList = contractthemeMapper.select(contracttheme);
        conList = conList.stream().sorted(Comparator.comparing(Contracttheme::getRowindex)).collect(Collectors.toList());
        return conList;
    }

    @Override
    public void insert(List<Contracttheme> contracttheme, TokenModel tokenModel) throws Exception {
        Contracttheme con = new Contracttheme();
        if(!StringUtils.isNullOrEmpty(contracttheme.get(0).getMonths())){
            con.setMonths(contracttheme.get(0).getMonths());
        }
        con.setYears(contracttheme.get(0).getYears());
        //数据库原有数据
        List<Contracttheme> conList = contractthemeMapper.select(con);
        int rowindex = 0;
        for(int i = 0; i < contracttheme.size(); i ++){
            int ddeleteFlg = 0;
            rowindex = rowindex + 1;
            Contracttheme co = contracttheme.get(i);
            if(!co.getContractthemeid().equals("")){
                if(conList.size() > 0){
                    for(int j = 0; j < conList.size(); j ++){
                        Contracttheme ct = conList.get(j);
                        if(co.getContractthemeid().equals(ct.getContractthemeid())){
                            co.preUpdate(tokenModel);
                            co.setRowindex(String.valueOf(rowindex));
                            contractthemeMapper.updateByPrimaryKey(co);
                            conList.remove(j);
                        }
                    }
                }
                else{
                    if(!contracttheme.get(0).getMonths().equals("")){
                        //添加新添加的数据
                        co.preInsert(tokenModel);
                        co.setContractthemeid(UUID.randomUUID().toString());
                        co.setRowindex(String.valueOf(rowindex));
                        contractthemeMapper.insert(co);
                    }
                    else{
                        co.preUpdate(tokenModel);
                        co.setRowindex(String.valueOf(rowindex));
                        contractthemeMapper.updateByPrimaryKey(co);
                    }
                }
            }
            else{
                //添加新添加的数据
                co.preInsert(tokenModel);
                co.setContractthemeid(UUID.randomUUID().toString());
                co.setRowindex(String.valueOf(rowindex));
                contractthemeMapper.insert(co);
            }
        }
        //删除无用数据
        for(int x = 0; x < conList.size(); x ++){
            Contracttheme ct = conList.get(x);
            contractthemeMapper.delete(ct);
        }
    }
}
