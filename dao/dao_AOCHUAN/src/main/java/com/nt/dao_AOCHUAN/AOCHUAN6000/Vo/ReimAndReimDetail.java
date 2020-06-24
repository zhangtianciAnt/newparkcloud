package com.nt.dao_AOCHUAN.AOCHUAN6000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN6000.Dailyfee;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReimAndReimDetail {

    /**
     * 主表
     */
    private Reimbursement reimForm;

    /**
     * 差旅费
     */
    private List<ReimbursementDetail> reimFormList;


//    日常费
    private List<Dailyfee> reimrcFormList;
}

