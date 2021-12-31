package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * Theme别收入见通(RevenueForecast)实体类
 *
 * @author makejava
 * @since 2021-11-18 14:58:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "revenue_forecast")
public class RevenueForecast extends BaseModel {
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
     * 保存年月
     */
    private Date saveDate;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 年度
     */
    private String annual;
    /**
     * 四月计划
     */
    private String aprilPlan;
    /**
     * 四月见通
     */
    private String aprilForecast;
    /**
     * 四月实际
     */
    private String aprilActual;
    /**
     * 五月计划
     */
    private String mayPlan;
    /**
     * 五月见通
     */
    private String mayForecast;
    /**
     * 五月实际
     */
    private String mayActual;
    /**
     * 六月计划
     */
    private String junePlan;
    /**
     * 六月见通
     */
    private String juneForecast;
    /**
     * 六月实际
     */
    private String juneActual;
    /**
     * 七月计划
     */
    private String julyPlan;
    /**
     * 七月见通
     */
    private String julyForecast;
    /**
     * 七月实际
     */
    private String julyActual;
    /**
     * 八月计划
     */
    private String augustPlan;
    /**
     * 八月见通
     */
    private String augustForecast;
    /**
     * 八月实际
     */
    private String augustActual;
    /**
     * 九月计划
     */
    private String septemberPlan;
    /**
     * 九月见通
     */
    private String septemberForecast;
    /**
     * 九月实际
     */
    private String septemberActual;
    /**
     * 十月计划
     */
    private String octoberPlan;
    /**
     * 十月见通
     */
    private String octoberForecast;
    /**
     * 十月实际
     */
    private String octoberActual;
    /**
     * 十一月计划
     */
    private String novemberPlan;
    /**
     * 十一月见通
     */
    private String novemberForecast;
    /**
     * 十一月实际
     */
    private String novemberActual;
    /**
     * 十二月计划
     */
    private String decemberPlan;
    /**
     * 十二月见通
     */
    private String decemberForecast;
    /**
     * 十二月实际
     */
    private String decemberActual;
    /**
     * 一月计划
     */
    private String januaryPlan;
    /**
     * 一月见通
     */
    private String januaryForecast;
    /**
     * 一月实际
     */
    private String januaryActual;
    /**
     * 二月计划
     */
    private String februaryPlan;
    /**
     * 二月见通
     */
    private String februaryForecast;
    /**
     * 二月实际
     */
    private String februaryActual;
    /**
     * 三月计划
     */
    private String marchPlan;
    /**
     * 三月见通
     */
    private String marchForecast;
    /**
     * 三月实际
     */
    private String marchActual;

    @Transient
    private String customerName;

    @Transient
    private String themeName;

}

