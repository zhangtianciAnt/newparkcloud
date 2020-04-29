package com.nt.dao_AOCHUAN.AOCHUAN6000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveDaysVo {
    //用户ID
    private String names;
    //事假请假天数
    private String thingleave;
    //事假请假天数
    private String sickthing;
    //补假请假天数
    private String compensatory;
    //婚假请假天数
    private String marriage;
    //年假请假天数
    private String annual;
    //产假请假天数
    private String maternity;
    //丧假请假天数
    private String funeral;

}
