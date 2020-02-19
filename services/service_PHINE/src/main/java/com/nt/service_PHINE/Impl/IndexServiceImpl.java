package com.nt.service_PHINE.Impl;

import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_PHINE.Vo.ChartDataRow;
import com.nt.dao_PHINE.Vo.DashboardInfoVo;
import com.nt.dao_PHINE.Vo.DeviceListVo;
import com.nt.dao_PHINE.Vo.ProjectListVo;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.UserService;
import com.nt.service_PHINE.DeviceinfoService;
import com.nt.service_PHINE.IndexService;
import com.nt.service_PHINE.ProjectinfoService;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.service_PHINE.Impl
 * @ClassName: IndexServiceImpl
 * @Description: 获取仪表盘图表数据
 * @Author: SKAIXXOrgTree
 * @CreateDate: 2020/2/19
 * @Version: 1.0
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private OrgTreeService orgTreeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectinfoService projectinfoService;

    @Autowired
    private DeviceinfoService deviceinfoService;

    /**
     * @return
     * @Method getDashboardInfo
     * @Author SKAIXX
     * @Description 获取仪表盘图表数据
     * @Date 2020/2/19 16:08
     * @Param
     **/
    @Override
    public DashboardInfoVo getDashboardInfo(TokenModel tokenModel) {
        DashboardInfoVo dashboardInfoVo = new DashboardInfoVo();
        // region 总企业数
        List<OrgTree> orgTreeList = new ArrayList<>();
        OrgTree orgTree = new OrgTree();
        orgTree.setTenantid(tokenModel.getTenantId());
        try {
            orgTreeList = orgTreeService.get(orgTree);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int idx = 0;
        for (OrgTree item : orgTreeList) {
            if (item.getType().equals("1")) {
                idx++;
            }
        }
        dashboardInfoVo.setCompanycnt(idx);
        // endregion

        // region 总项目数
        List<ProjectListVo> projectListVoList = projectinfoService.getProjectInfoList("");
        dashboardInfoVo.setProjectcnt(projectListVoList.size());
        // endregion

        // region 总用户数
        List<CustomerInfo> customerInfoList = new ArrayList<>();
        customerInfoList = userService.getAllCustomerInfo();
        dashboardInfoVo.setUsercnt(customerInfoList.size() - 1);
        // endregion

        // region 总设备数
        List<DeviceListVo> deviceListVoList = deviceinfoService.getDeviceInfoList();
        dashboardInfoVo.setDevicecnt(deviceListVoList.size());
        // endregion

        // region 企业/用户数图表
        List<ChartDataRow> chartDataRowList = new ArrayList<>();
        customerInfoList.forEach(item -> {
            ChartDataRow tmp = new ChartDataRow();
            tmp.setName(item.getUserinfo().getCompanyid());
            tmp.setCnt(1);
            chartDataRowList.add(tmp);
        });
        Map<String, List<ChartDataRow>> collect = chartDataRowList.stream().collect(Collectors.groupingBy(ChartDataRow::getName));
        chartDataRowList.clear();
        collect.forEach((item, list) -> {
            if (StringUtils.isNotEmpty(item)) {
                ChartDataRow chartDataRow = new ChartDataRow();
                OrgTree ot = new OrgTree();
                ot.set_id(item);
                try {
                    chartDataRow.setName(orgTreeService.getById(ot).get(0).getCompanyshortname());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                chartDataRow.setCnt(list.size());
                chartDataRowList.add(chartDataRow);
            }
        });
        dashboardInfoVo.setUserChartDataRowList(chartDataRowList);
        // endregion

        // region 企业/设备数图表
        dashboardInfoVo.setDeviceChartDataRowList(deviceinfoService.getDeviceChartInfo());
        // endregion

        // region 设备状态图表
        dashboardInfoVo.setStateChartDataRowList(deviceinfoService.getDeviceStatusChartInfo());
        // endregion

        return dashboardInfoVo;
    }
}
