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
@Table(name = "softwaretransfer")
public class Softwaretransfer extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 固定資産
	 */
    @Id
    @Column(name = "SOFTWARETRANSFER_ID")
    private String softwaretransferid;

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
     * 起案者
     */
    @Column(name = "INITIATOR")
    private String initiator;

    /**
     * 内線番号
     */
    @Column(name = "INSIDENUMBER")
    private String insidenumber;

    /**
     * 移動日
     */
    @Column(name = "MOBILEDATE")
    private Date mobiledate;

    /**
     * 譲渡预算单位
     */
    @Column(name = "FERRYBUDGETUNIT")
    private String ferrybudgetunit;

    /**
     * 譲渡センター
     */
    @Column(name = "FERRYCENTER_ID")
    private String ferrycenter_id;

    /**
     * 譲渡グループ
     */
    @Column(name = "FERRYGROUP_ID")
    private String ferrygroup_id;

    /**
     * 譲渡チーム
     */
    @Column(name = "FERRYTEAM_ID")
    private String ferryteam_id;

    /**
     * 移管センター
     */
    @Column(name = "TUBECENTER_ID")
    private String tubecenter_id;

    /**
     * 移管グループ
     */
    @Column(name = "TUBEGROUP_ID")
    private String tubegroup_id;

    /**
     * 移管チーム
     */
    @Column(name = "TUBETEAM_ID")
    private String tubeteam_id;

    /**
     * 移管预算单位
     */
    @Column(name = "TUBEBUDGETUNIT")
    private String tubebudgetunit;

}
