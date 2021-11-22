package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personscale")
public class PersonScale extends BaseModel {
    private static final long serialVersionUID = 1L;

    //人员规模主键
    @Id
    @Column(name = "PERSONSCALE_ID")
    private String personscale_id;

    //年月
    @Column(name = "YEARMONTH")
    private String yearmonth;

    //报告对象
    @Column(name = "REPORTPEOPLE")
    private String reportpeople;

    //区分
    @Column(name = "TYPE")
    private String type;

    //center
    @Column(name = "CENTER_ID")
    private String center_id;

    //group
    @Column(name = "GROUP_ID")
    private String group_id;

    //Rank
    @Column(name = "RANKS")
    private String ranks;


    //项目
    @Column(name = "PROJECT_ID")
    private String project_id;

    //项目
    @Column(name = "PROJECT_NAME")
    private String project_name;

    //日志填写总时长
    @Column(name = "WORKTIME")
    private BigDecimal worktime;

    //比例
    @Column(name = "PROPORTIONS")
    private String proportions;

    //报告者
    @Column(name = "REPORTERS")
    private String reporters;
}
