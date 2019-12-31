package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.service_pfans.PFANS6000.mapper.CustomerinforMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerinforServiceImpl implements CustomerinforService {

    @Autowired
    private CustomerinforMapper customerinforMapper;

    @Override
    public List<Customerinfor> getcustomerinfor(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        return customerinforMapper.select(customerinfor);
    }

    @Override
    public Customerinfor getcustomerinforApplyOne(String customerinfor_id) throws Exception {
        return customerinforMapper.selectByPrimaryKey(customerinfor_id);
    }

    @Override
    public void updatecustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        customerinforMapper.updateByPrimaryKeySelective(customerinfor);
    }

    @Override
    public void createcustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        customerinfor.preInsert(tokenModel);
        customerinfor.setCustomerinfor_id(UUID.randomUUID().toString());
        customerinforMapper.insert(customerinfor);
    }


//    导入导出
//    @Override
//    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
//    public List<String> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
//        try {
////            创建listVo集合方便存储导入信息
//            List<Customerinfor> listVo = new ArrayList<Customerinfor>();
////            创建Result结果集的集合
//            List<String> Result = new ArrayList<String>();
////            用来接收前台传过来的文件
//            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
////            创建对象f，且为空
//            File f = null;
////            创建临时文件
//            f = File.createTempFile("temp",null);
////            上传文件
//            file.transferTo(f);
////            使用Excel读文件
//            ExcelReader reader = ExcelUtil.getReader(f);
////            创建集合存入读的文件
//            List<List<Object>> list = reader.read();
////            创建集合存入标准模板
//            List<Object> model = new ArrayList<Object>();
////            标准模板
//            model.add("客户名称");
//            model.add("负责人");
//            model.add("项目联络人");
//            model.add("联系电话");
//            model.add("共同事务联络人");
//            model.add("联系电话");
//            model.add("地址");
//            model.add("人员规模");
//            List<Object> key = list.get(0);
////           上传模板与标准模板 校验
//            for(int i = 0; i < key.size(); i++){
//                if(!key.get(i).toString().trim().equals(model.get(i))){
//                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
//                }
//            }
//            return Result;
//        } catch (Exception e) {
//            throw new LogicalException(e.getMessage());
//        }
//    }
//
//
//    @Override
//    public void methodAttendance(TokenModel tokenModel) throws Exception {
//
//    }

}
