package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pimsdata")
public class Pimsdata extends BaseModel {

    @Id
    private String pimsid;

    /*
     * 所属装置code
     */

    private String devicecode;

    /*
     * 监控点
     */

    private String monitoringpoint;

    /*
     * 测量数据
     */
    private BigDecimal pimsdata;

    /*
     * 类型
     */
    private String type;

}
