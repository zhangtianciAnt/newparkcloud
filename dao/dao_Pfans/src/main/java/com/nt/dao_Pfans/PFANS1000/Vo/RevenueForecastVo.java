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
    //新添加的theme数据（theme表中没有需要创建）
    private List<ThemeInfor> themeInforList;
    private List<RevenueForecast> revenueForecastList;
}
