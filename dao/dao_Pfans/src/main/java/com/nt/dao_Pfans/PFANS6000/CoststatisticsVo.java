package com.nt.dao_Pfans.PFANS6000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CoststatisticsVo")
public class CoststatisticsVo{

    /**
     * 费用统计
     */
    private List<Coststatistics> coststatistics;

}
