package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PimsdataVo{
    /*
     * 监控点
     */

    private String monitoringpoint;

    /*
     * 测量数据
     */
    private String pimsdata;

    /*
     * 类型
     */
    private String type;

    /*
     *  统计小时
     */
    private String createon;

}
