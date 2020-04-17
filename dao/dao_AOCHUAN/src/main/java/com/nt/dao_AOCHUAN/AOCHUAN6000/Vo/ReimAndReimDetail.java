package com.nt.dao_AOCHUAN.AOCHUAN6000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
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
     * 明细表
     */
    private List<ReimbursementDetail> reimFormList;
}

