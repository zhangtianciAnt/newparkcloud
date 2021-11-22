package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "personscalemee")
public class PersonScaleMee extends BaseModel {
    private static final long serialVersionUID = 1L;

    //人员规模主表主键
    @Id
    @Column(name = "PERSONSCALEMEE_ID")
    private String personscalemee_id;

    //姓名
    @Column(name = "YEARMONTH")
    private String yearmonth;

    //姓名
    @Column(name = "USER_ID")
    private String user_id;

    //center
    @Column(name = "CENTER_ID")
    private String center_id;

    //group
    @Column(name = "GROUP_ID")
    private String group_id;

    //RANKS
    @Column(name = "RANKS")
    private String ranks;

    //伞下管理所有人数
    @Column(name = "MANGERNUMBER")
    private String mangernumber;

    //项目管理对应分数
    @Column(name = "MANAGESORCE")
    private String managesorce;
}
