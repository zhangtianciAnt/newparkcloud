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
 * @ClassName: emergencyplan
 * @Author: Y
 * @Description: emergencyplan
 * @Date: 2019/11/18 17:22
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emergencyplan")
public class Emergencyplan extends BaseModel {
    @Id
    private String emergencyplanid;

    /**
     *应急预案名称
     */
    private String emergencyplanname;

    /**
     *编制单位
     */
    private String compilationunit;

    /**
     *文件名称
     */
    private String filename;

    /**
     *创建时间
     */
    private Date creationtime;

    /*
     * 备注
     */
    private String remarks;

    /*
     * 模板
     */
    private String affiliatetemplate;

}
