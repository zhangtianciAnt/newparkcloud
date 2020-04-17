package com.nt.service_SQL.Impl;


import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.VO.DeviceAndSqlUserinfoVo;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_BASF.Impl.DeviceInformationServicesImpl;
import com.nt.dao_SQL.SqlAPBCardHolder;
import com.nt.dao_SQL.SqlUserInfo;
import com.nt.dao_SQL.SqlViewDepartment;
import com.nt.service_SQL.SqlUserInfoServices;
import com.nt.service_SQL.sqlMapper.SqlUserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nt.dao_BASF.PersonnelPermissions;
import com.nt.service_BASF.*;
import java.util.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class SqlUserInfoServicesImpl implements SqlUserInfoServices {

    @Autowired
    private DeviceinformationMapper deviceinformationMapper;

    @Autowired
    private SqlUserInfoMapper sqlUserInfoMapper;

    @Autowired
    private PersonnelPermissionsServices personnelPermissionsServices;

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
        Map<String,String> departmentInfoList=new HashMap<>();
        List departlist = sqlUserInfoMapper.selectdepartment();
        List apblist = sqlUserInfoMapper.selectapbcard();
        List resultlist = new ArrayList();
        List<PersonnelPermissions> personnelPermissionsList = personnelPermissionsServices.list();
        String companystaff="";
        String supplier="";
        String foreignworkers="";


        if (apblist.size() > 0) {
            if (departlist.size() > 0) {
                for (int i = 0; i < departlist.size(); i++) {
                    departmentInfoList.put(((SqlViewDepartment) departlist.get(i)).getRecnum(),((SqlViewDepartment) departlist.get(i)).getDepartmentpeid());
                }
            }
            if (personnelPermissionsList.size() > 0) {
                for (int i = 0; i < personnelPermissionsList.size(); i++) {
                    if("class1".equals(((PersonnelPermissions) personnelPermissionsList.get(i)).getClassname())){
                        if("".equals(companystaff)){
                            companystaff=((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }else{
                            companystaff=companystaff+","+((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }
                    }
                    if("class2".equals(((PersonnelPermissions) personnelPermissionsList.get(i)).getClassname())){
                        if("".equals(foreignworkers)){
                            foreignworkers=((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }else{
                            foreignworkers=foreignworkers+","+((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }
                    }
                    if("class3".equals(((PersonnelPermissions) personnelPermissionsList.get(i)).getClassname())){
                        if("".equals(supplier)){
                            supplier=((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }else{
                            supplier=supplier+","+((PersonnelPermissions) personnelPermissionsList.get(i)).getRecnum();
                        }
                    }
                }
            }
            List<String> companystaffList = Arrays.asList(companystaff.split(","));
            List<String> supplierList = Arrays.asList(supplier.split(","));
            List<String> foreignworkersList = Arrays.asList(foreignworkers.split(","));

            for (int i = 0; i < apblist.size(); i++) {
                String departmentID = ((SqlAPBCardHolder) apblist.get(i)).getDepartmentid();
                for (int j = 1; j<10; j++) {
                    if ("-1".equals(departmentInfoList.get(departmentID))) {
                        for(int z = 0; z<companystaffList.size(); z++) {
                            if(companystaffList.get(z).equals(departmentID)){
                                ((SqlAPBCardHolder) apblist.get(i)).setUsertype("员工");
                                resultlist.add(apblist.get(i));
                            }
                        }
                        for(int z = 0; z<supplierList.size(); z++) {
                            if(supplierList.get(z).equals(departmentID)){
                                ((SqlAPBCardHolder) apblist.get(i)).setUsertype("承包商");
                                resultlist.add(apblist.get(i));
                            }
                        }
                        for(int z = 0; z<foreignworkersList.size(); z++) {
                            if(foreignworkersList.get(z).equals(departmentID)){
                                ((SqlAPBCardHolder) apblist.get(i)).setUsertype("访客");
                                resultlist.add(apblist.get(i));
                            }
                        }
                        j=10;
                    }else {
                        departmentID=departmentInfoList.get(departmentID);
                    }
                }
            }
        }
        return resultlist;
    }

//    紧急集合点
    @Override
    public List<SqlAPBCardHolder> selectapbid(String APBID) throws Exception {

//        DeviceAndSqlUserinfoVo deviceAndSqlUserinfoVo = new DeviceAndSqlUserinfoVo();
//
//        List<SqlAPBCardHolder> departlist = sqlUserInfoMapper.selectapbid();
//        Deviceinformation deviceinformation = new Deviceinformation();
//        List<Deviceinformation> deviceinformationList = deviceinformationMapper.select(deviceinformation);
//        for (int i = 0; i < deviceinformationList.size(); i++) {
//            int cnt = 0;
//            for (int j = 0; j < departlist.size(); j++) {
//                if (deviceinformationList.get(i).getDeviceno() == departlist.get(j).getApbid()) {
//                    cnt++;
//                }
//            }
//            if (cnt != 0) {
//                deviceAndSqlUserinfoVo.setDeviceinformation(deviceinformationList.get(i));
//                deviceAndSqlUserinfoVo.setSqlUserInfoCnt(cnt);
//            }
//        }

        return sqlUserInfoMapper.selectapbid(APBID);
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
