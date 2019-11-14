package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "environment")
public class Environment extends BaseModel {

    @Id
    private String environmentid;

    /*
     * 监测指标
     */

    private String monitor;

    /*
     * 预警级别
     */

    private String alertlevel;

    /*
     * 预警阈值
     */

    private Integer threshold;


}
