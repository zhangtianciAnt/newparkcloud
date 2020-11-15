package com.nt.dao_AOCHUAN.AOCHUAN5000;

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
@Table(name = "kislogin")
public class KisLogin extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "KISLOGINID")
    private String kisloginid;
    //合同号
    @Column(name = "USERNAME")
    private String username;
    //产品中文
    @Column(name = "USERPASSWORD")
    private String userpassword;
    //规格
    @Column(name = "LCID")
    private Integer lcid;

}

