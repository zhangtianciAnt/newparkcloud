package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassingRateVo extends BaseModel {


    /**
     * 数量
     */
    private String cnt;

    /**
     * 通过/未通过
     */
    private String vehicletype;
}