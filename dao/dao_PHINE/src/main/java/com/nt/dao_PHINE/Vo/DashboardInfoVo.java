package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: DashboardInfoVo
 * @Description: 仪表盘图表数据Vo
 * @Author: SKAIXX
 * @CreateDate: 2020/2/19
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardInfoVo {

    /**
     * 总企业数
     */
    private Integer companycnt;

    /**
     * 总项目数
     */
    private Integer projectcnt;

    /**
     * 总用户数
     */
    private Integer usercnt;

    /**
     * 总设备数
     */
    private Integer devicecnt;

    /**
     * 企业/用户数图表
     */
    private List<ChartDataRow> userChartDataRowList;


    /**
     * 企业/设备数图表
     */
    private List<ChartDataRow> deviceChartDataRowList;


    /**
     * 设备状态图表
     */
    private List<ChartDataRow> stateChartDataRowList;
}
