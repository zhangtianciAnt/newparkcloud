package com.nt.dao_AOCHUAN.AOCHUAN1000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN1000.Contactperson;
import com.nt.dao_AOCHUAN.AOCHUAN1000.Supplierbaseinfor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "supplierbaseinfor")
public class ContactstitchingVo {

    //供应商
    private Supplierbaseinfor supplier;

    //联系人
    private List<Contactperson> contactperson;

}
