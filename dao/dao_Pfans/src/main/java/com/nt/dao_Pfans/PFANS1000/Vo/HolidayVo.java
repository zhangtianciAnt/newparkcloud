package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Holiday;
import com.nt.dao_Pfans.PFANS1000.Holidaydetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayVo {
    /**
     * 休日出勤のカード申請
     */
    private Holiday holiday;

    /**
     * 休日出勤のカード申請明细
     */
    private List<Holidaydetail> holidaydetail;
}
