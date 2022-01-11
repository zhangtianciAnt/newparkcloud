package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.dao_Pfans.PFANS1000.ThemeInfor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueForecastVo {
    private RevenueForecast revenueForecast;
    private List<RevenueForecast> revenueForecastList;
    private int currentPage;
    private int pageSize;
}
