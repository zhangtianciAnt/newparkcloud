package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
public class Course extends BaseModel {

    @Id
    private String courseid;

    /*
     * 课程名称
     */

    private String coursename;

    /*
     * 培训课程类别
     */

    private String coursetype;


    /*
     * 有效期限
     */

    private String courselimt;


    /*
     * 时间类型
     */

    private String timetype;


    /*
     * 培训资料
     */

    private String coursefile;


    /*
     * 备注
     */

    private String remarks;


}
