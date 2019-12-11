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
@Table(name = "routingdetail")
public class Routingdetail extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * ルーティング申請明细副表
     */
    @Column(name = "ROUTINGDETAIL_ID")
    private String routingdetail_id;

    /**
     * ルーティング申請
     */
    @Column(name = "ROUTING_ID")
    private String routing_id;

    /**
     * 通信方向
     */
    @Column(name = "COMMUNICATION")
    private String communication;

    /**
     * 接続元IPグループ
     */
    @Column(name = "SOURCEIPGROUP")
    private String sourceipgroup;

    /**
     * 接続元IPアドレス（PSDCD内）
     */
    @Column(name = "SOURCEIPADDRESS")
    private String sourceipaddress;

    /**
     * 接続先IPグループ
     */
    @Column(name = "DESTINATIONIPGROUP")
    private String destinationipgroup;

    /**
     * 接続先IPアドレス（PSDCD内）
     */
    @Column(name = "DESTINATIONIPADDRESS")
    private String destinationipaddress;

    /**
     * プロトコル又はポート番号
     */
    @Column(name = "PROTOCOL")
    private String protocol;

    @Column(name = "ROWINDEX")
    private Integer rowindex;
}
