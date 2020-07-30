package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "webchatuserinfo")
public class Webchatuserinfo extends BaseModel {
    @Id
    private Integer id;

    /**
     * 登录ID
     */
    private String loginid;

    /**
     * 登录密码
     */
    private String password;

}
