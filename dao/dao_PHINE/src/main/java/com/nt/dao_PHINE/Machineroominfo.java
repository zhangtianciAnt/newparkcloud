package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "machineroominfo")
public class Machineroominfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 机房编号&机柜编号
     */
    private String machineroomid;

    /**
     * 机房名
     */
    private String machineroomname;

    /**
     * 机房地址
     */
    private String machineroomaddress;

    /**
     * 机房负责人ID
     */
    private String machineroommanagerid;

    /**
     * 备注
     */
    private String remarks;

}
