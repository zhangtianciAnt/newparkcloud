package com.nt.dao_AOCHUAN.AOCHUAN2000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Contactperson;
import com.nt.dao_AOCHUAN.AOCHUAN2000.Customerbaseinfor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customerbaseinfor")
public class CustomerbaseinforVo {

    //供应商
    private Customerbaseinfor customerbaseinfor;

    //联系人
    private List<Contactperson> contactperson;

}
