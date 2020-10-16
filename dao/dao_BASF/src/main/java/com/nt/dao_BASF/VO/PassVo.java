package com.nt.dao_BASF.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Classname PassVo
 * @Description TODO 类的描述
 * @Date 2020/10/16 14:58
 * @Author skaixx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassVo {

    private String departmentname;

    private String year;

    private BigDecimal pass;
}
