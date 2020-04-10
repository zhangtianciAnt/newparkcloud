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
@Table(name="menushow")
public class MenuShow extends BaseModel {
    @Id
    private String menuid;

    /**
     *用户名称
     */
    private String loginname;

    /**
     *首页状态
     */
    private String homepageshowflg;

    /**
     *火灾消防状态
     */
    private String fireshowflg;

    /**
     *环保安全状态
     */
    private String safeshowflg;

    /**
     *现场监控状态
     */
    private String onsiteshowflg;

    /**
     *人员清点状态
     */
    private String personnelshowflg;

    /**
     *车辆定位状态
     */
    private String vehicleshowflg;

    /**
     *应急档案状态
     */
    private String emergencyshowflg;

    /**
     *培训教育状态
     */
    private String trainingshowflg;
    /**
     *风险研判状态
     */
    private String riskshowflg;
    /**
     *是否看见后台状态
     */
    private String backstageflg;
}
