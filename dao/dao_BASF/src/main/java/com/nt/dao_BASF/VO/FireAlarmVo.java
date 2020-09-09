package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Firealarm;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FireAlarmVo extends Firealarm {
    /**
     * 事件类型名称
     */
    private String value1;
}
