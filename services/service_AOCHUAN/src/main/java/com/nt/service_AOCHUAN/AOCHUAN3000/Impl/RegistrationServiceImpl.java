package com.nt.service_AOCHUAN.AOCHUAN3000.Impl;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Reg_Record;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Registration;
import com.nt.service_AOCHUAN.AOCHUAN3000.RegistrationService;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.Reg_RecordMapper;
import com.nt.service_AOCHUAN.AOCHUAN3000.mapper.RegistrationMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private RegistrationMapper registrationMapper;
    @Autowired
    private Reg_RecordMapper reg_recordMapper;

    /**
     * 获取注册表list
     * @return
     */
    @Override
    public List<Registration> getRegList() {
        Registration registration = new Registration();
        return registrationMapper.select(registration);
    }

    /**
     * 根据主键查询注册表
     * @param id
     * @return
     */
    @Override
    public Registration getReg(String id) {
        return registrationMapper.selectByRegId(id);
    }

    /**
     * 根据注册表id查询记录表
     * @param id
     * @return
     */
    @Override
    public List<Reg_Record> getRecordList(String id) {
        return reg_recordMapper.selectByRegId(id);
    }

    /**
     * 新建
     * @param object
     * @param tokenModel
     * @return
     */
    @Override
    public Boolean insert(Object object, TokenModel tokenModel) throws Exception{

        //注册表新建
        if (object instanceof Registration){
            Registration registration = new Registration();
            BeanUtils.copyProperties(registration,object);

            if (isExist(registration.getReg_id())){
                //已存在
                return false;
            }else{
                registration.preInsert(tokenModel);
                registrationMapper.insert(registration);
            }
        }
        //记录表新建
        else if(object instanceof Reg_Record){
            Reg_Record reg_record = new Reg_Record();
            BeanUtils.copyProperties(reg_record,object);

            if (recordIsExist(reg_record.getRecord_id())){
                //已存在
                return false;
            }else{
                reg_record.preInsert(tokenModel);
                reg_recordMapper.insert(reg_record);
            }
        }

        return true;
    }

    /**
     * 更新
     * @param object
     * @param tokenModel
     * @return
     */
    @Override
    public Boolean update(Object object, TokenModel tokenModel)throws  Exception {

        //注册表更新
        if (object instanceof Registration){
            Registration registration = new Registration();
            BeanUtils.copyProperties(registration,object);

            if (!isExist(registration.getReg_id())){
                //不存在
                return false;
            }else{
                registration.preUpdate(tokenModel);
                registrationMapper.updateByPrimaryKeySelective(registration);
            }
        }
        //记录表更新
        else if(object instanceof Reg_Record){
            Reg_Record reg_record = new Reg_Record();
            BeanUtils.copyProperties(reg_record,object);

            if (!recordIsExist(reg_record.getRecord_id())){
                //不存在
                return false;
            }else{
                reg_record.preUpdate(tokenModel);
                reg_recordMapper.updateByPrimaryKeySelective(reg_record);
            }
        }
        return true;
    }

    /**
     * 删除
     * 注册表直接删除
     * @param registration
     * @param tokenModel
     * @return
     */
    @Override
    public Boolean del(Registration registration, TokenModel tokenModel) {
        if (!isExist(registration.getReg_id())){
            //不存在
            return false;
        }else{
            registration.preUpdate(tokenModel);
            registrationMapper.del(registration.getReg_id(),registration.getModifyby());
            if(recordIsExist(registration.getReg_id())){
                reg_recordMapper.deleteByRegId(registration.getReg_id(),registration.getModifyby());
            }
        }
        return true;
    }

    /**
     * 记录表删除
     * @param object
     * @param tokenModel
     * @return
     */
    @Override
    public Boolean recordDel(Object object, TokenModel tokenModel)throws Exception {

        //根据注册表id删除
        if (object instanceof String) {
            String id = "";
            if(object != null){
                id = object.toString();
            }
            Reg_Record reg_record = new Reg_Record();
            reg_record.setReg_id(id);
            reg_record.preUpdate(tokenModel);
            reg_recordMapper.deleteByRegId(reg_record.getReg_id(), reg_record.getModifyby());
        }
        //根据记录表id删除
        else if(object instanceof Reg_Record){
            Reg_Record reg_record = new Reg_Record();
            BeanUtils.copyProperties(reg_record, object);

            if(!recordIsExist(reg_record.getRecord_id())){
              return false;
            }
            reg_record.preUpdate(tokenModel);
            reg_recordMapper.deleteByRecordId(reg_record.getRecord_id(), reg_record.getModifyby());
        }
        else{
            return false;
        }
        return true;
    }

    /**
     * 注册表存在check
     * @return
     */
    private Boolean isExist(String id){
       if(registrationMapper.existCheck(id)>0){
           return true;
       }
        return false;
    }

    /**
     * 记录表存在check
     * @return
     */
    @Override
    public Boolean recordIsExist(String id){
        if(reg_recordMapper.existCheck(id)>0){
            return true;
        }
        return false;
    }
}
