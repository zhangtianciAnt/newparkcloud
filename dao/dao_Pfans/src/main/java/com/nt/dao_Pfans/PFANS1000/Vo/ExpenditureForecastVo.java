package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.ExpenditureForecast;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenditureForecastVo {
    private ExpenditureForecast expenditureForecast;
    private List<ExpenditureForecast> expenditureForecastList;
}