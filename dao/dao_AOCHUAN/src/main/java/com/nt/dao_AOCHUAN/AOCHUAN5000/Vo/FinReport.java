package com.nt.dao_AOCHUAN.AOCHUAN5000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN3000.TransportGood;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinReport{

    //走货
    @Column(name = "PURCHASE_ID")
    private String purchase_id;
}
