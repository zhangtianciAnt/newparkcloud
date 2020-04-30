package com.nt.dao_AOCHUAN.AOCHUAN0000;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workplan")
public class WorkPlan extends BaseModel {

    //工作日历主键id
    @Id
    @Column(name = "WORKPLAN_ID")
    private String workplan_id;

    //员工id
    @Column(name = "EMP_ID")
    private String emp_id;

    //日期
    @Column(name = "WORK_DATE")
    private Date work_date;

    //时间：From
    @Column(name = "WORK_FTIME")
    private Date work_ftime;

    //时间：To
    @Column(name = "WORK_TTIME")
    private Date work_ttime;

    //内容
    @Column(name = "WORK_REMARKS")
    private String work_remarks;
}
