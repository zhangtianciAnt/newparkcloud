package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "psdcddetail")
public class Psdcddetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * PSDCD ID申請明细副表
     */
    @Column(name = "PSDCDDETAIL_ID")
    private String psdcddetail_id;

    /**
     * PSDCD ID申請
     */
    @Column(name = "PSDCD_ID")
    private String psdcd_id;

    /**
     * ユーザ種類
     */
    @Column(name = "USERTYPE")
    private String usertype;

    /**
     * ユーザ氏名
     */
    @Column(name = "USERNAME")
    private String username;

    /**
     * 姓
     */
    @Column(name = "SURNAME")
    private String surname;

    /**
     * 名
     */
    @Column(name = "MING")
    private String ming;

    /**
     * 元のメールアカウント
     */
    @Column(name = "ACCOUNT")
    private String account;

    /**
     * 社外送信
     */
    @Column(name = "TRANSMISSION")
    private String transmission;

    /**
     * 期待時間
     */
    @Column(name = "WAITFORTIME")
    private String waitfortime;

    /**
     * 预算单位
     */
    @Column(name = "BUDGETUNIT")
    private String budgetunit;

    /**
     * サイボウズAccount
     */
    @Column(name = "CYBOZU")
    private String cybozu;

    /**
     * A期待時間
     */
    @Column(name = "EXPECTTIME")
    private String expecttime;

    /**
     * Domain Account
     */
    @Column(name = "DOMAINACCOUNT")
    private String domainaccount;

    /**
     * D期待時間
     */
    @Column(name = "FORWARDTIME")
    private String forwardtime;

    /**
     * 備考
     */
    @Column(name = "PREPAREFOR")
    private String preparefor;

    /**
     * 顺序
     */
    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
