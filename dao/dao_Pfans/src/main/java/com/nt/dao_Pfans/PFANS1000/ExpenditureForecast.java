package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "expenditure_forecast")
public class ExpenditureForecast extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 数据ID
     */
    @Id
    private String id;
    /**
     * Theme主键ID
     */
    private String themeinforId;
    /**
     * 分类
     */
    private String classIfication;
    /**
     * 保存年月
     */
    private Date saveDate;
    /**
     * 年度
     */
    private String annual;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 四月工数计划
     */
    private String hoursPlan4;
    /**
     * 四月金额计划
     */
    private String moneyPlan4;
    /**
     * 四月工数见通
     */
    private String hoursForecast4;
    /**
     * 四月金额见通
     */
    private String moneyForecast4;
    /**
     * 四月工数实际
     */
    private String hoursActual4;
    /**
     * 四月金额实际
     */
    private String moneyActual4;
    /**
     * 五月工数计划
     */
    private String hoursPlan5;
    /**
     * 五月工数计划
     */
    private String moneyPlan5;
    /**
     * 五月工数计划
     */
    private String hoursForecast5;
    /**
     * 五月工数计划
     */
    private String moneyForecast5;
    /**
     * 五月工数计划
     */
    private String hoursActual5;
    /**
     * 五月工数计划
     */
    private String moneyActual5;
    /**
     * 六月工数计划
     */
    private String hoursPlan6;
    /**
     * 六月工数计划
     */
    private String moneyPlan6;
    /**
     * 六月工数计划
     */
    private String hoursForecast6;
    /**
     * 六月工数计划
     */
    private String moneyForecast6;
    /**
     * 六月工数计划
     */
    private String hoursActual6;
    /**
     * 六月工数计划
     */
    private String moneyActual6;
    /**
     * 七月工数计划
     */
    private String hoursPlan7;
    /**
     * 七月工数计划
     */
    private String moneyPlan7;
    /**
     * 七月工数计划
     */
    private String hoursForecast7;
    /**
     * 七月工数计划
     */
    private String moneyForecast7;
    /**
     * 七月工数计划
     */
    private String hoursActual7;
    /**
     * 七月工数计划
     */
    private String moneyActual7;
    /**
     * 八月工数计划
     */
    private String hoursPlan8;
    /**
     * 八月工数计划
     */
    private String moneyPlan8;
    /**
     * 八月工数计划
     */
    private String hoursForecast8;
    /**
     * 八月工数计划
     */
    private String moneyForecast8;
    /**
     * 八月工数计划
     */
    private String hoursActual8;
    /**
     * 八月工数计划
     */
    private String moneyActual8;
    /**
     * 九月工数计划
     */
    private String hoursPlan9;
    /**
     * 九月工数计划
     */
    private String moneyPlan9;
    /**
     * 九月工数计划
     */
    private String hoursForecast9;
    /**
     * 九月工数计划
     */
    private String moneyForecast9;
    /**
     * 九月工数计划
     */
    private String hoursActual9;
    /**
     * 九月工数计划
     */
    private String moneyActual9;
    /**
     * 十月工数计划
     */
    private String hoursPlan10;
    /**
     * 十月工数计划
     */
    private String moneyPlan10;
    /**
     * 十月工数计划
     */
    private String hoursForecast10;
    /**
     * 十月工数计划
     */
    private String moneyForecast10;
    /**
     * 十月工数计划
     */
    private String hoursActual10;
    /**
     * 十月工数计划
     */
    private String moneyActual10;
    /**
     * 十一月工数计划
     */
    private String hoursPlan11;
    /**
     * 十一月工数计划
     */
    private String moneyPlan11;
    /**
     * 十一月工数计划
     */
    private String hoursForecast11;
    /**
     * 十一月工数计划
     */
    private String moneyForecast11;
    /**
     * 十一月工数计划
     */
    private String hoursActual11;
    /**
     * 十一月工数计划
     */
    private String moneyActual11;
    /**
     * 十二月工数计划
     */
    private String hoursPlan12;
    /**
     * 十二月工数计划
     */
    private String moneyPlan12;
    /**
     * 十二月工数计划
     */
    private String hoursForecast12;
    /**
     * 十二月工数计划
     */
    private String moneyForecast12;
    /**
     * 十二月工数计划
     */
    private String hoursActual12;
    /**
     * 十二月工数计划
     */
    private String moneyActual12;
    /**
     * 一月工数计划
     */
    private String hoursPlan1;
    /**
     * 一月工数计划
     */
    private String moneyPlan1;
    /**
     * 一月工数计划
     */
    private String hoursForecast1;
    /**
     * 一月工数计划
     */
    private String moneyForecast1;
    /**
     * 一月工数计划
     */
    private String hoursActual1;
    /**
     * 一月工数计划
     */
    private String moneyActual1;
    /**
     * 二月工数计划
     */
    private String hoursPlan2;
    /**
     * 二月工数计划
     */
    private String moneyPlan2;
    /**
     * 二月工数计划
     */
    private String hoursForecast2;
    /**
     * 二月工数计划
     */
    private String moneyForecast2;
    /**
     * 二月工数计划
     */
    private String hoursActual2;
    /**
     * 二月工数计划
     */
    private String moneyActual2;
    /**
     * 三月工数计划
     */
    private String hoursPlan3;
    /**
     * 三月工数计划
     */
    private String moneyPlan3;
    /**
     * 三月工数计划
     */
    private String hoursForecast3;
    /**
     * 三月工数计划
     */
    private String moneyForecast3;
    /**
     * 三月工数计划
     */
    private String hoursActual3;
    /**
     * 三月工数计划
     */
    private String moneyActual3;

    @Transient
    private String customerName;//委托元

    @Transient
    private String themeName;//theme名

    @Transient
    private String type;//theme种类

    @Transient
    private String contracttype;//契约形式

}


