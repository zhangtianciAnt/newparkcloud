package com.nt.dao_BASF;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: user
 * @Description: 人员通讯录数据表
 * @Author: LXY
 * @CreateDate: 2019/11/06
 * @Version: 1.0
 */
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class BASFUser extends BaseModel {
    @Id
    private String userid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 电话号码
     */
    private String phonenumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 证件类别
     */
    private String documenttype;

    /**
     * 证件号
     */
    private String documentnumber;
    /**
     * 性别
     */
    private String sex;

    /**
     * 人员类别
     */
    private String personneltype;

    /**
     * 承包商名称
     */
    private String contractorname;
}