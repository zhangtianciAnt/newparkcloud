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
@Table(name = "emergencytemplate")
public class Emergencytemplate extends BaseModel {

    @Id
    private String templateid;

    /*
     * 模板名称
     */

    private String templatename;

    /*
     * 模板类型
     */

    private String templatetype;


    /*
     * 模板级别
     */

    private String templatelevel;


    /*
     * 创建时间
     */

    private Date templatetim;


    /*
     * 备注
     */

    private String remarks;

    /*
     * 附件
     */

    private String uploadfile;



}
