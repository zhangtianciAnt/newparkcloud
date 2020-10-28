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
@Table(name = "existtrainarchives")
public class Existtrainarchives extends BaseModel {
    @Id
    private String id;

    /**
     * 部门
     */
    private String department;

    /**
     * 姓名
     */
    private String employeename;

    /**
     * 负责人姓名
     */
    private String managername;

    /**
     * 培训项目
     */
    private String trainingprograms;

    /**
     * 证件编号
     */
    private String certificatenumber;

    /**
     * 批准日期
     */
    private Date approvaldate;

    /**
     * 有效日期
     */
    private Date effectivedate;

    /**
     * 备考
     */
    private String remark;

}
