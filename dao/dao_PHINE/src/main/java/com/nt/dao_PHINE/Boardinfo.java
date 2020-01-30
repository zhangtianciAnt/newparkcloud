package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "boardinfo")
public class Boardinfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 添加时指定，不可重复
     */
    private String boardid;

    /**
     * 板卡类型
     */
    private String boardtype;

    /**
     * 板卡IP地址
     */
    private String boardipaddress;

    /**
     * 所属设备编号
     */
    private String deviceid;

    /**
     * 备注
     */
    private String remarks;


}
