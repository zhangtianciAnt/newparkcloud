package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: Record
 * @Author: 王哲
 * @Description: 人员培训记录
 * @Date: 2019/11/25 13:37
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "record")
public class Record extends BaseModel {
    /**
     * 人员培训记录主键
     */
    @Id
    private String recordid;

    /**
     * 人员姓名
     */
    private String username;

    /**
     * 人员类别
     */
    private String personneltype;

    /**
     * 培训项目名称
     */
    private String recordname;

    /**
     * 培训科目
     */
    private String recordclass;

    /**
     * 培训类别
     */
    private String recordtype;

    /**
     * 培训时间
     */
    private Date recordtime;

    /**
     * 培训负责人
     */
    private String recordpeo;

    /**
     * 培训方式
     */
    private String recordmod;

    /**
     * 考核成绩
     */
    private String grade;

    /**
     * 考核结果
     */
    private String result;

    /**
     * 证书
     */
    private String certificate;
}
