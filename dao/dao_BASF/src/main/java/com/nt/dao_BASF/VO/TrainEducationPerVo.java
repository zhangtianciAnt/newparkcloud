package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainEducationPerVo extends BaseModel {

    /**
     * 培训教育人员id
     */
    private String personnelid;
    /**
     * 培训名称
     */
    private String programname;
    /**
     * 时间
     */
    private String actualstartdate;
    /**
     * 通过/未通过
     */
    private String throughtype;
    /**
     * 姓名
     */
    private String customername;
    /**
     * 卡号
     */
    private String documentnumber;
    /**
     * 员工号
     */
    private String jobnumber;

}