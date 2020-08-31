package com.nt.dao_BASF;

import com.nt.dao_BASF.VO.PimsdataVo;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

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


    @Transient
    private List<PimsdataVo> rows;

}
