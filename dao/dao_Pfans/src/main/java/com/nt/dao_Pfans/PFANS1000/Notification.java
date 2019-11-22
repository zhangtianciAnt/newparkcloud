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
@Table(name = "notification")
public class Notification extends BaseModel {

	private static final long serialVersionUID = 1L;

    /**
	 * 固定資産ソフトウェア移転届明细
	 */
    @Id
    @Column(name = "NOTIFICATION_ID")
    private String notificationid;

    /**
     * 資産管理番号
     */
    @Column(name = "MANAGEMENT")
    private String management;

    /**
     * 資産名
     */
    @Column(name = "ASSETNAME")
    private String assetname;

    /**
     * 移転前責任者
     */
    @Column(name = "PERSON")
    private String person;

    /**
     * 移転後責任者
     */
    @Column(name = "EAFTER")
    private Date eafter;

    /**
     * 移転原因
     */
    @Column(name = "REASON")
    private String reason;

}
