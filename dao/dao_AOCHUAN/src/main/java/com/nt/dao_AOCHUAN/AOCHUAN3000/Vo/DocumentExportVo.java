package com.nt.dao_AOCHUAN.AOCHUAN3000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentExportVo {

    //订舱时间（提醒）
    private Date bookingtime;

    //起运港
    private String departure;

    //货代公司
    private String forwardingcompany;

    //鉴定费用
    private String appraisalcost;

    //进仓单号
    private String entrynumber;

    //截单时间
    private Date interceptiontime;

    //到仓截止时间
    private Date cutofftime;

    //确认到仓
    private String warehouse;

    //报关情况
    private String bill;

    //提单时间
    private Date billtime;

    //运费
    private String freight;

    //保费
    private String premium;

    //单据担当
    private String billresponsibility;

}
