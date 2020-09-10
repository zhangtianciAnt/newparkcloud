package com.nt.dao_Org.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname OrgListVo
 * @Description TODO 类的描述
 * @Date 2020/8/31 11:58
 * @Author skaixx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrgListVo {

    private String value;

    private String label;

    private String departmentemail;

    private String devicemanageremail;

    private String costcenter;
}
