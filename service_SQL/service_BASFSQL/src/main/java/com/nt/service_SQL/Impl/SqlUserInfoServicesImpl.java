package com.nt.service_SQL.Impl;


import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlUserInfo;
import com.nt.dao_SQL.SqlViewDepartment;
import com.nt.service_SQL.SqlUserInfoServices;
import com.nt.service_SQL.sqlMapper.SqlUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class SqlUserInfoServicesImpl implements SqlUserInfoServices {

    @Autowired
    private SqlUserInfoMapper sqlUserInfoMapper;

    @Override
    public List<SqlUserInfo> list( ) throws Exception {
        return sqlUserInfoMapper.userinfolist();
    }

    @Override
    public SqlViewDepartment selectdepartmentid(String recnum) throws Exception {
        return sqlUserInfoMapper.selectdepartmentid(recnum);
    }

    @Override
    public List<SqlAPBCardHolder> selectapbcard( ) throws Exception {
        List apblist = sqlUserInfoMapper.selectapbcard();
        List resultlist = new ArrayList();
        if(apblist.size()>0)
        {
//            resultlist.add(apblist.get(0));
            for(int i = 0;i<apblist.size();i++)
            {
                String lastAPBName = ((SqlAPBCardHolder) apblist.get(i)).getLastapbname();
                String DepartmentID = ((SqlAPBCardHolder) apblist.get(i)).getDepartmentid();
                if(lastAPBName.indexOf("厂外") < 0 )
                {
                    SqlViewDepartment sqlviewdepartment1 = sqlUserInfoMapper.selectdepartmentid(DepartmentID);
                    if(sqlviewdepartment1.getDepartmentpeid().indexOf("-1")==0)
                    {
                        String name1 = sqlviewdepartment1.getName();
                        if("'访客','送货员','临时工作人员','VIP'".indexOf(name1) > 0)
                        {
                            ((SqlAPBCardHolder) apblist.get(i)).setUsertype("访客");
                            resultlist.add(apblist.get(i));
                        }
                        else if("'BACH','BASF','BCH','BACH&BACC','BSC'".indexOf(name1) > 0)
                        {
                            ((SqlAPBCardHolder) apblist.get(i)).setUsertype("员工");
                            resultlist.add(apblist.get(i));
                        }
                        else if("'SCIP','Project','BACH Contractor','BSC Contractor'".indexOf(name1) > 0)
                        {
                            ((SqlAPBCardHolder) apblist.get(i)).setUsertype("承包商");
                            resultlist.add(apblist.get(i));
                        }
                    }
                    else{
                        SqlViewDepartment sqlviewdepartment2 = sqlUserInfoMapper.selectdepartmentid(sqlviewdepartment1.getDepartmentpeid());

                        if(sqlviewdepartment2.getDepartmentpeid().indexOf("-1")==0)
                        {
                            String name2 = sqlviewdepartment2.getName();
                            if("'访客','送货员','临时工作人员','VIP'".indexOf(name2) > 0)
                            {
                                ((SqlAPBCardHolder) apblist.get(i)).setUsertype("访客");
                                resultlist.add(apblist.get(i));
                            }
                            else if("'BACH','BASF','BCH','BACH&BACC','BSC'".indexOf(name2) > 0)
                            {
                                ((SqlAPBCardHolder) apblist.get(i)).setUsertype("员工");
                                resultlist.add(apblist.get(i));
                            }
                            else if("'SCIP','Project','BACH Contractor','BSC Contractor'".indexOf(name2) > 0)
                            {
                                ((SqlAPBCardHolder) apblist.get(i)).setUsertype("承包商");
                                resultlist.add(apblist.get(i));
                            }
                        }
                        else{
                            SqlViewDepartment sqlviewdepartment3 = sqlUserInfoMapper.selectdepartmentid(sqlviewdepartment2.getDepartmentpeid());

                            if(sqlviewdepartment3.getDepartmentpeid().indexOf("-1")==0)
                            {
                                String name3 = sqlviewdepartment3.getName();
                                if("'访客','送货员','临时工作人员','VIP'".indexOf(name3) > 0)
                                {
                                    ((SqlAPBCardHolder) apblist.get(i)).setUsertype("访客");
                                    resultlist.add(apblist.get(i));
                                }
                                else if("'BACH','BASF','BCH','BACH&BACC','BSC'".indexOf(name3) > 0)
                                {
                                    ((SqlAPBCardHolder) apblist.get(i)).setUsertype("员工");
                                    resultlist.add(apblist.get(i));
                                }
                                else if("'SCIP','Project','BACH Contractor','BSC Contractor'".indexOf(name3) > 0)
                                {
                                    ((SqlAPBCardHolder) apblist.get(i)).setUsertype("承包商");
                                    resultlist.add(apblist.get(i));
                                }
                            }
                        }
                    }
                }
            }
        }
        return resultlist;
    }

    @Override
    public List<SqlAPBCardHolder> selectapbcardholder() throws Exception {
        return sqlUserInfoMapper.selectapbcardholder();
    }

    @Override
    public List<SqlViewDepartment> selectdepartment() throws Exception {
        return sqlUserInfoMapper.selectdepartment();
    }
}
