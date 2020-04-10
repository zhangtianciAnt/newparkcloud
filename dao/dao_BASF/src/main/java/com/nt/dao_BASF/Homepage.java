package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "menushow")
public class Homepage extends BaseModel {

    @Id
    private String menuid;

    /**
     * 角色名
     */
    private String loginname;

    /**
     *首页
     */
    private String homepageshowflg;

    /**
     * 火灾消防
     */
    private String fireshowflg;

    /**
     * 环保安全
     */
    private String safeshowflg;

    /**
     * 现场监控
     */
    private String onsiteshowflg;

    /**
     * 人员清点
     */
    private String personnelshowflg;

    /**
     * 车辆定位
     */
    private String vehicleshowflg;

    /**
     * 应急档案
     */
    private String emergencyshowflg;

    /**
     * 培训教育
     */
    private String trainingshowflg;

    /**
     * 风险研判
     */
    private String riskshowflg;

    /**
     * 是否看见后台
     */
    private String backstageflg;

}