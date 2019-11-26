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
@Table(name = "devicetrainer")
public class Devicetrainer extends BaseModel {

    @Id
    private String devicetrainerid;

    /*
     * 培训项目主键
     */

    private String programid;

    /*
     * 参与培训项目名称
     */

    private String proname;


    /*
     * 装置名称
     */

    private String devicename;


    /*
     * 装置培训负责人
     */

    private String deviceprincipal;


    /*
     * 参与人数
     */

    private Integer participateno;


    /*
     * 培训课程
     */

    private String traincourse;


    /*
     * 培训时间
     */
    private String traintim;


    /*
     * 培训类别
     */
    private String traintype;


    /*
     * 培训人员
     */
    private String trainpeo;


}
