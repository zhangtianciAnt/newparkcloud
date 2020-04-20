package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "fixedassets")
public class Fixedassets extends BaseModel {
    private static final long serialVersionUID = 1L;

    /**
     * 固定资产贷出修理持出决裁书
     */
    @Id
    @Column(name = "FIXEDASSETS_ID")
    private String fixedassets_id;

    /**
     * 所属センター
     */
    @Column(name = "CENTER_ID")
    private String center_id;

    /**
     * 所属グループ
     */
    @Column(name = "GROUP_ID")
    private String group_id;

    /**
     * 所属チーム
     */
    @Column(name = "TEAM_ID")
    private String team_id;

    /**
     * 申请人
     */
    @Column(name = "USER_ID")
    private String user_id;

    /**
     * RFID资产编号
     */
    @Column(name = "RFID")
    private String rfid;

    /**
     * 申请日期
     */
    @Column(name = "CASEDATE")
    private Date casedate;

    /**
     * 资产类型
     */
    @Column(name = "ASSETTYPE")
    private String assettype;

    /**
     * 是否安装加密软件
     */
    @Column(name = "INSTALLSOFTWARE")
    private String installsoftware;

    /**
     * 是否适合带出
     */
    @Column(name = "SUITABLEBRINGOUT")
    private String suitablebringout;

    /**
     * 輸入日
     */
    @Column(name = "INPUTDATE")
    private Date inputdate;

    /**
     * 監督解除日
     */
    @Column(name = "RELEASEDATE")
    private Date releasedate;

    /**
     * 目的
     */
    @Column(name = "OBJECTIVE")
    private String objective;

    /**
     * 貸出／修理先（住所、電話、社名など）
     */
    @Column(name = "BORROWING")
    private String borrowing;

    /**
     * 貸出／修理／持出期間
     */
    @Column(name = "REPAIR")
    private String repair;

    /**
     * 貸出／修理契約書
     */
    @Column(name = "REPAIRKITS")
    private String repairkits;

    /**
     * 固定資産名称
     */
    @Column(name = "ASSETNAME")
    private String assetname;

    /**
     * 'び付属設備
     */
    @Column(name = "ANCILLARYEQUIPMENT")
    private String ancillaryequipment;

    /**
     * 免税輸入設備
     */
    @Column(name = "DUTYFREEINPUT")
    private String dutyfreeinput;


}
