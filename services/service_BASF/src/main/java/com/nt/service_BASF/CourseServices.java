package com.nt.service_BASF;


import com.nt.dao_BASF.Course;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF21201Services
 * @Author: WXL
 * @Description:数据监控管理接口
 * @Date: 2019/11/20 15:50
 * @Version: 1.0
 */
public interface CourseServices {

    //获取课程列表
    List<Course> list() throws Exception;

    //创建课程
    void insert(Course course, TokenModel tokenModel) throws Exception;

    //删除课程
    void delete(Course course) throws Exception;

    //获取课程据详情
    Course one(String courseid) throws Exception;

    //更新课程数据
    void update(Course course, TokenModel tokenModel) throws Exception;

}
