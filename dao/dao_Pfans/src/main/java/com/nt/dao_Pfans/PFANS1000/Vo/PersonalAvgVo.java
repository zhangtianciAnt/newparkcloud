package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Assetinformation;
import com.nt.dao_Pfans.PFANS1000.Salesdetails;
import com.nt.dao_Pfans.PFANS1000.Scrapdetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;
import java.util.stream.DoubleStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalAvgVo {
    /**
     * Code
     */
    private String nextyear;

    /**
     * 人件费平均值
     */
    private Double summerplanpc;

    /**
     * 加班时给
     */
    private Double overtimepay;

    /**
     * 加班小时数
     */
    private Double overtimehour;

    public PersonalAvgVo(String code,String summerplanpc,String overtimepay,String overtimehour){
        this.nextyear = code;
        this.summerplanpc = Double.parseDouble(summerplanpc);
        this.overtimepay = Double.parseDouble(overtimepay);
        this.overtimehour = Double.parseDouble(overtimehour);
    }

}
