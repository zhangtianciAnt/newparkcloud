package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chipinfo")
public class Chipinfo extends BaseModel {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 添加时指定，不可重复
     */
    private String chipid;

    /**
     * VU440、KU115等
     */
    private String chiptype;

    /**
     * 所属板卡编号
     */
    private String boardid;

    /**
     * 备注
     */
    private String remarks;

}
