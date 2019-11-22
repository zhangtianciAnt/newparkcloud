package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Course;
import com.nt.service_BASF.CourseServices;
import com.nt.service_BASF.mapper.CourseMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF21202ServicesImpl
 * @Author: WXL
 * @Description: BASF数据监控模块实现类
 * @Date: 2019/11/18 17:00
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CourseServicesImpl implements CourseServices {

    private static Logger log = LoggerFactory.getLogger(CourseServicesImpl.class);

    @Autowired
    private CourseMapper courseMapper;


    /**
     * @param Course
     * @Method list
     * @Author WXL
     * @Version 1.0
     * @Description 获取课程数据列表
     * @Return java.util.List<Course>
     * @Date 2019/11/20 16:17
     */
    @Override
    public List<Course> list() throws Exception {
        Course course = new Course();
        return courseMapper.select(course);
    }


    /**
     * @param course
     * @param tokenModel
     * @Method insert
     * @Author WXL
     * @Version 1.0
     * @Description 创建课程数据
     * @Return void
     * @Date 2019/11/20 16:12
     */
    @Override
    public void insert(Course course, TokenModel tokenModel) throws Exception {
        course.preInsert(tokenModel);
        course.setCourseid(UUID.randomUUID().toString());
        courseMapper.insert(course);
    }


    /**
     * @param course
     * @Method Delete
     * @Author WXL
     * @Version 1.0
     * @Description 删除课程数据
     * @Return void
     * @Date 2019/11/20 16：12
     */
    @Override
    public void delete(Course course) throws Exception {
        courseMapper.updateByPrimaryKeySelective(course);
    }


    /**
     * @param courseid
     * @Method one
     * @Author WXL
     * @Version 1.0
     * @Description 获取数据课程详情
     * @Return com.nt.dao_BASF.Course
     * @Date 2019/11/18 17：24
     */
    @Override
    public Course one(String courseid) throws Exception {
        return courseMapper.selectByPrimaryKey(courseid);
    }


    /**
     * @param course
     * @param tokenModel
     * @Method update
     * @Author WXL
     * @Version 1.0
     * @Description 更新课程详情
     * @Return void
     * @Date 2019/11/20 16：14
     */
    @Override
    public void update(Course course, TokenModel tokenModel) throws Exception {
        course.preUpdate(tokenModel);
        courseMapper.updateByPrimaryKey(course);
    }

}
