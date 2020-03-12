package com.nt.service_pfans.PFANS1000.Impl;

import com.mysql.jdbc.StringUtils;
import com.nt.dao_Pfans.PFANS1000.Contracttheme;
import com.nt.service_pfans.PFANS1000.ContractthemeService;
import com.nt.service_pfans.PFANS1000.mapper.ContractthemeMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
        List<Contracttheme> colist = new ArrayList<Contracttheme>();
        colist = contractthemeMapper.select(contracttheme);
        if(colist.size() == 0){
            if(contracttheme.getType().equals("2") || contracttheme.getType().equals("4")){
                colist = contractthemeMapper.getContractthemeList(contracttheme.getType(),contracttheme.getYears());
                if(colist.size() > 0){
                    Contracttheme co = new Contracttheme();
                    co.setType(colist.get(0).getType());
                    co.setYears(colist.get(0).getYears());
                    co.setMonths(colist.get(0).getMonths());
                    colist = contractthemeMapper.select(co);
                }
            }
        }
        colist = colist.stream().sorted(Comparator.comparing(Contracttheme::getRowindex)).collect(Collectors.toList());
        return colist;
    }

    @Override
    public void insert(List<Contracttheme> contracttheme, TokenModel tokenModel) throws Exception {
        if(contracttheme.get(contracttheme.size() - 1).getStatus().equals(AuthConstants.APPROVED_FLAG_YES) &&
                (contracttheme.get(0).getType().equals("2") || contracttheme.get(0).getType().equals("4"))){
            //进行中
            Contracttheme con = new Contracttheme();
            con.setMonths(contracttheme.get(0).getMonths());
            con.setType(contracttheme.get(0).getType());
            //数据库原有数据
            List<Contracttheme> conList = contractthemeMapper.select(con);
            if(conList.size() > 0){
                conList = conList.stream().sorted(Comparator.comparing(Contracttheme::getRowindex)).collect(Collectors.toList());
                if(conList.get(conList.size() - 1).getStatus().equals(AuthConstants.WORK_FLAG_NO)){
                    for(int i = 0; i < contracttheme.size(); i ++){
                        Contracttheme co = contracttheme.get(i);
                        //更新的数据(見通し)审批结束
                        co.preUpdate(tokenModel);
                        co.setStatus(AuthConstants.APPROVED_FLAG_YES);
                        contractthemeMapper.updateByPrimaryKey(co);
                    }
                    return;
                }
            }
            //新添加的数据(見通し)
            int rowindex = 0;
            for(int i = 0; i < contracttheme.size(); i ++){
                rowindex = rowindex + 1;
                Contracttheme co = contracttheme.get(i);
                //新添加的数据(見通し)
                co.preInsert(tokenModel);
                co.setRowindex(String.valueOf(rowindex));
                if(contracttheme.get(i).getContractthemeid().equals("")){
                    co.setStatus(AuthConstants.APPROVED_FLAG_NO);
                }
                else{
                    co.setStatus(AuthConstants.APPROVED_FLAG_YES);
                }
                co.setContractthemeid(UUID.randomUUID().toString());
                contractthemeMapper.insert(co);
            }
            return;
        }

        Contracttheme con = new Contracttheme();
        con.setYears(contracttheme.get(0).getYears());
        con.setType(contracttheme.get(0).getType());
        if(!StringUtils.isNullOrEmpty(contracttheme.get(0).getMonths())){
            con.setMonths(contracttheme.get(0).getMonths());
            con.setType(contracttheme.get(0).getType());
        }

        //数据库原有数据
        List<Contracttheme> conList = contractthemeMapper.select(con);
        int rowindex = 0;
        for(int i = 0; i < contracttheme.size(); i ++){
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
                        co.setStatus(AuthConstants.APPROVED_FLAG_YES);
                        co.setRowindex(String.valueOf(rowindex));
                        contractthemeMapper.insert(co);
                        //下一年计划不为空的场合
                        if(!co.getPersonnel1().equals("") || !co.getAmount1().equals("0")
                            || !co.getPersonnel2().equals("") || !co.getAmount2().equals("0")
                                || !co.getPersonnel3().equals("") || !co.getAmount3().equals("0")){
                            co.preInsert(tokenModel);
                            co.setContractthemeid(UUID.randomUUID().toString());
                            co.setYears(String.valueOf(Integer.valueOf(co.getYears()) + 1));
                            co.setMonths(co.getMonths());
                            contractthemeMapper.insert(co);
                        }
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
                //下一年计划不为空的场合
                if(!co.getPersonnel1().equals("") || !co.getAmount1().equals("0")
                        || !co.getPersonnel2().equals("") || !co.getAmount2().equals("0")
                        || !co.getPersonnel3().equals("") || !co.getAmount3().equals("0")){
                    co.setContractthemeid(UUID.randomUUID().toString());
                    co.setYears(String.valueOf(Integer.valueOf(co.getYears()) + 1));
                    contractthemeMapper.insert(co);
                }
            }
        }
        //删除无用数据
        for(int x = 0; x < conList.size(); x ++){
            Contracttheme ct = conList.get(x);
            contractthemeMapper.delete(ct);
        }
    }
}
