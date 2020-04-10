package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Homepagereturn extends BaseModel {

    /**
     *首页左侧菜单名称
     */
    private String name;

    /**
     * icon
     */
    private String icon;

    /**
     * no
     */
    private int no;

}