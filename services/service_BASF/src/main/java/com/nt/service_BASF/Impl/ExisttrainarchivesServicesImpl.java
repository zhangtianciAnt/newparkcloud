package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.Existtrainarchives;
import com.nt.dao_BASF.VO.ExisttrainarchivesVo;
import com.nt.service_BASF.ExisttrainarchivesServices;
import com.nt.service_BASF.mapper.ExisttrainarchivesMapper;
import com.nt.utils.AuthConstants;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExisttrainarchivesServicesImpl implements ExisttrainarchivesServices {

    private static Logger log = LoggerFactory.getLogger(ExisttrainarchivesServicesImpl.class);

    @Autowired
    private ExisttrainarchivesMapper existtrainarchivesMapper;

    // 获取全部既有的培训档案列表
    @Override
    public List<ExisttrainarchivesVo> getAllList() throws Exception {
        return existtrainarchivesMapper.getAllList();
    }

    @Override
    public ExisttrainarchivesVo getExisttrainarchivesInfoById(String id) throws Exception {
        ExisttrainarchivesVo existtrainarchivesVo = existtrainarchivesMapper.getExisttrainarchivesInfoById(id);
        return existtrainarchivesVo;
    }

    @Override
    public void delete(String id, TokenModel tokenModel) throws Exception {
        Existtrainarchives existtrainarchives = new Existtrainarchives();
        existtrainarchives.setId(id);
        existtrainarchives.setStatus(AuthConstants.DEL_FLAG_DELETE);
        existtrainarchives.preUpdate(tokenModel);
        existtrainarchivesMapper.updateByPrimaryKeySelective(existtrainarchives);
    }

    @Override
    public void update(Existtrainarchives existtrainarchives, TokenModel tokenModel) throws Exception {
        existtrainarchives.preUpdate(tokenModel);
        existtrainarchivesMapper.updateByPrimaryKeySelective(existtrainarchives);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> insert(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        //存放消息
        List<String> result = new ArrayList<>();
        //临时文件
        File f = null;
        //插入成功数
        int successCount = 0;
        //插入失败数
        int errorCount = 0;
        //excel集合
        List<List<Object>> list;
        //当前执行行数
        int k = 1;
        try {
            //转换成文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取并存储为临时文件时发生未知异常！");
            return result;
        }
        try {
            //读取excel临时文件
            ExcelReader reader = ExcelUtil.getReader(f);
            //读取到的excel集合
            list = reader.read();
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取Excel临时文件时发生未知异常！");
            return result;
        }
        //判断是否是空文件
        if (list == null) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("空文件，请上传正确的导入模板文件");
            return result;
        }
        List<Object> model = new ArrayList<Object>();
        model.add("标识符");
        model.add("部门");
        model.add("姓名");
        model.add("负责人");
        model.add("培训项目");
        model.add("证件编号");
        model.add("批准日期");
        model.add("有效日期");
        model.add("备考");
        List<Object> key = list.get(0);
        for (int i = 0; i < key.size(); i++) {
            if (!key.get(i).toString().trim().equals(model.get(i))) {
                result.add("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                errorCount += 1;
            }
        }
        if (errorCount > 0) {
            result.add("失败数：" + errorCount);
            return result;
        }
        try {
            for (int i = 1; i < list.size(); i++) {
                k += 1;
                Existtrainarchives existtrainarchives = new Existtrainarchives();
                SimpleDateFormat formatter = new SimpleDateFormat( "yyyyMMdd");
                List<Object> value = list.get(i);
                //标识
                try {
                    existtrainarchives.setId(value.get(0).toString());
                } catch (Exception e) {
                    existtrainarchives.setId("");
                }
                //部门
                try {
                    existtrainarchives.setDepartment(value.get(1).toString());
                } catch (Exception e) {
                    result.add("导入模板第 " + k + " 行，部门名称数据异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                //姓名
                try {
                    existtrainarchives.setEmployeename(value.get(2).toString());
                } catch (Exception e) {
                    result.add("导入模板第 " + k + "行，姓名数据异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                //负责人
                try {
                    existtrainarchives.setManagername(value.get(3).toString());
                } catch (Exception e) {
                    result.add("导入模板第 " + k + "行，负责人数据异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                //培训项目
                try {
                    existtrainarchives.setTrainingprograms(value.get(4).toString());
                } catch (Exception e) {
                    result.add("导入模板第 " + k + "行，培训项目数据异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                //证件编号
                try {
                    existtrainarchives.setCertificatenumber(value.get(5).toString().trim());
                } catch (Exception e) {
                    existtrainarchives.setCertificatenumber("");
                }
                //批准日期
                try {
                    String approvaldate = value.get(6).toString().trim();
                    existtrainarchives.setApprovaldate(formatter.parse(approvaldate.replace("/","")));
                } catch (Exception e) {
                    existtrainarchives.setApprovaldate(null);
                }
                //有效日期
                try {
                    String effectivedate = value.get(7).toString().trim();
                    existtrainarchives.setEffectivedate(formatter.parse(effectivedate.replace("/","")));
                } catch (Exception e) {
                    existtrainarchives.setEffectivedate(null);
                }
                //备考
                try {
                    existtrainarchives.setRemark(value.get(8).toString().trim());
                } catch (Exception e) {
                    existtrainarchives.setRemark("");
                }
                existtrainarchives.preInsert(tokenModel);
                existtrainarchives.setStatus(AuthConstants.DEL_FLAG_NORMAL);
                try {
                    if (StringUtils.isNotEmpty(existtrainarchives.getId())) {
                        existtrainarchivesMapper.updateByPrimaryKey(existtrainarchives);
                    } else {
                        existtrainarchives.setId(UUID.randomUUID().toString());
                        existtrainarchivesMapper.insert(existtrainarchives);
                    }
                    successCount += 1;
                } catch (Exception e) {
                    result.add("导入模板第 " + k + "行存储到数据库异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
            }
            result.add("失败数：" + errorCount);
            result.add("成功数：" + successCount);
            return result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
