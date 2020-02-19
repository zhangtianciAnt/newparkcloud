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
@Table(name = "highriskareadetailed")
public class Highriskareadetailed extends BaseModel {
    @Id
    private String highriskareadetailedid;

    /**
     * 高风险区域主键
     */
    private String highriskareaid;

    /**
     * 坐标
     */
    private String coordinate;
}