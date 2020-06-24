package com.nt.dao_AOCHUAN.AOCHUAN6000;

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
@Table(name = "dailyfee")
public class Dailyfee extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //主键
    @Id
    @Column(name = "DAILYFEEID")
    private String dailyfeeid;

    //外键
    @Column(name = "REIMBURSEMENT_ID")
    private String reimbursement_id;

    //日期
    @Column(name = "DAILYDATE")
    private Date dailydate;

    //发票项目
    @Column(name = "DAILYPRO")
    private String dailypro;

    //发票金额
    @Column(name = "FPMONEY")
    private String fpmoney;

    //单据张数
    @Column(name = "DAILYNUN")
    private String dailynun;

    //实际用途
    @Column(name = "DAILYUSE")
    private String dailyuse;

    //实际金额
    @Column(name = "ACTUALYMONEY")
    private String actualymoney;

    //差额
    @Column(name = "DIFFMONEY")
    private String diffmoney;

}
