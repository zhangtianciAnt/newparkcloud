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
@Table(name = "program")
public class Program extends BaseModel {

    @Id
    private String programid;

    /*
     * 培训项目名称
     */

    private String programname;

    /*
     * 培训课程
     */

    private String programclass;


    /*
     * 培训时间
     */

    private Date programtime;


    /*
     * 培训负责人
     */

    private String programpeo;


    /*
     * 培训方式
     */

    private String programmod;


    /*
     * 培训类别
     */

    private String programtype;


    /*
     * 强制与否
     */
    private String isforce;


    /*
     * 实际参与人数
     */
    private Integer actualpeo;


    /*
     * 通过率
     */
    private String passrate;


    /*
     * 培训地点
     */
    private String programspace;


    /*
     * 装置培训负责人
     */
    private String devicepeo;


    /*
     * 培训人员
     */
    private String trainers;


}
