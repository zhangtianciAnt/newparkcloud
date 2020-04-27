package com.nt.dao_AOCHUAN.AOCHUAN6000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsVo {
//工号
    private String jobnumber;
//部门
    private String department;

//姓名
    private String names;
//年假
    private String startannual;
    private String annual;
    private String endannual;
//婚假
    private String startwedding ;
    private String wedding ;
    private String endwedding ;
//产假
    private String startmaternity;
    private String maternity;
    private String endmaternity;
//丧假
    private String startfuneral;
    private String funeral;
    private String endfuneral;
//事假
    private String thingleave;

//病假
    private String sickthing ;

//补假
    private String compensatory;


}

