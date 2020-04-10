package com.nt.service_BASF;

import com.nt.dao_BASF.Homepagereturn;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF10201Services
 * @Author: ZWT
 * @Description:首页左侧菜单接口
 * @Date: 2020/04/09 10:36
 * @Version: 1.0
 */
public interface HomePageServices {
    // LeftMenu  首页左侧菜单显示控制
    List<Homepagereturn> getHomepagecontrolshowList(String loginname) throws Exception;
}
