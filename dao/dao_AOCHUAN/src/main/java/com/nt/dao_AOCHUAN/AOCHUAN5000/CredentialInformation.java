package com.nt.dao_AOCHUAN.AOCHUAN5000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "crdlinfo")
public class CredentialInformation extends BaseModel {

    //凭证信息id
    @Id
    @Column(name = "CRDLINFO_ID")
    private String crdlinfo_id;

    //凭证编号
    @Column(name = "CRDL_NUM")
    private String crdl_num;
    //金蝶凭证编号
    @Column(name = "CRDLKIS_NUM")
    private String crdlkis_num;
    //凭证类型
    @Column(name = "CRDLTYPE")
    private String crdltype;
    //凭证类型名称
    @Column(name = "CRDLTYPE_NM")
    private String crdltype_nm;
    //凭证字号
    @Column(name = "CRDLWORD")
    private String crdlword;
    //业务日期
    @Column(name = "BUS_DATE")
    private Date bus_date;
    //记账日期
    @Column(name = "ACCT_DATE")
    private Date acct_date;
    //附件数
    @Column(name = "ATTACHMENTS")
    private String attachments;
    //推送状态
    @Column(name = "PUSH_STATUS")
    private String push_status;
    //推送状态名称
    @Column(name = "PUSH_STATUS_NM")
    private String push_status_nm;
}
