package com.nt.dao_AOCHUAN.AOCHUAN3000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN3000.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportVo {

    /**
     * 走货
     */
    private TransportGood transportgood;

    /**
     * 走货-销售明细子表
     */
    private Saledetails saledetails;

    /**
     * 走货-用款申请记录子表
     */
    private Applicationrecord applicationrecord;
    
}
