package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.RevenueForecast;
import com.nt.dao_Pfans.PFANS1000.Vo.RevenueForecastVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * Theme别收入见通(RevenueForecast)表服务接口
 *
 * @author makejava
 * @since 2021-11-18 14:58:47
 */
public interface RevenueForecastService {

    /**
     * 保存信息
     *
     * @param revenueForecastVo 收入预测
     * @param tokenModel        令牌模型
     */
    void saveInfo(RevenueForecastVo revenueForecastVo, TokenModel tokenModel) throws Exception;


    /**
     * 选择信息
     *
     * @param revenueForecast 收入预测
     * @return {@link List}<{@link RevenueForecast}>
     */
    List<RevenueForecast> selectInfo(RevenueForecast revenueForecast);


    /**
     * 获取剩余theme（部门条件筛选以外的theme）
     *
     * @return {@link List}<{@link RevenueForecast}>
     */
    List<RevenueForecast> getThemeOutDepth(RevenueForecast revenueForecast);

}
