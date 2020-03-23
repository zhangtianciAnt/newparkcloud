package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.QuestionManage;
import com.nt.utils.MyMapper;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: QuestionManageMapper
 * @Author: Wxz
 * @Description: QuestionManageMapper
 * @Date: 2019/11/20 18:38
 * @Version: 1.0
 */
public interface QuestionManageMapper extends MyMapper<QuestionManage> {

    //题库列表
    List<QuestionManage> selectEnhance() throws Exception;

}
