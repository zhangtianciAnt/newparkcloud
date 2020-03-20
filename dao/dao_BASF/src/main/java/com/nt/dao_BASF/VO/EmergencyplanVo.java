package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Emergencyplan;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmergencyplanVo extends BaseModel {
    private Emergencyplan emergencyplan;
    private String token;
}
