package com.nt.dao_Pfans.PFANS3000.Vo;

import com.nt.dao_Pfans.PFANS3000.UseCoupon;
import com.nt.dao_Pfans.PFANS3000.JapanCondominium;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JapanCondominiumVo {
    /**
     * 离职申请
     */
    private JapanCondominium japancondominium;
    /**
     * 引継项目
     */
    private List<UseCoupon> usecoupon;


}
