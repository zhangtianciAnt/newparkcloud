package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Homepage;
import com.nt.dao_BASF.Homepagereturn;
import com.nt.service_BASF.HomePageServices;
import com.nt.service_BASF.mapper.HomePageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName:BASF10201ServicesImpl
 * @Author: ZWT
 * @Description: BASF首页左侧菜单控制
 * @Date: 2020/04/09 10:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HomePageServiceImpl implements HomePageServices {

    @Autowired
    private HomePageMapper homepagemapper;

    @Override
    public List<Homepagereturn> getHomepagecontrolshowList(String loginname) throws Exception {
        Homepage homepage = new Homepage();
        homepage.setLoginname(loginname);
        Homepagereturn homepagereturn = new Homepagereturn();

        List<Homepagereturn> homepagereturnList = new ArrayList<Homepagereturn>();

        for (Homepage str : homepagemapper.select(homepage)) {

            if ("1".equals(str.getHomepageshowflg())) {
                //首页
                homepagereturn = setHomepage("首页", "el-icon-platform-eleme", 0);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getFireshowflg())) {
                //火灾消防
                homepagereturn = setHomepage("火灾消防", "el-icon-s-tools", 1);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getSafeshowflg())) {
                //环保安全
                homepagereturn = setHomepage("环保安全", "el-icon-user-solid", 2);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getOnsiteshowflg())) {
                //现场监控
                homepagereturn = setHomepage("现场监控", "el-icon-s-goods", 3);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getPersonnelshowflg())) {
                //人员清点
                homepagereturn = setHomepage("人员清点", "el-icon-question", 4);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getVehicleshowflg())) {
                //车辆定位
                homepagereturn = setHomepage("车辆定位", "el-icon-zoom-in", 5);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getEmergencyshowflg())) {
                homepagereturn = setHomepage("应急档案", "el-icon-s-help", 6);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getTrainingshowflg())) {
                //培训教育
                homepagereturn = setHomepage("培训教育", "el-icon-s-promotion", 7);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getRiskshowflg())) {
                //风险研判
                homepagereturn = setHomepage("风险研判", "el-icon-platform-eleme", 8);
                homepagereturnList.add(homepagereturn);
            }

            if ("1".equals(str.getBackstageflg())) {
                //是否看见后台
                homepagereturn = setHomepage("是否看见后台", "el-icon-backstage", 9);
                homepagereturnList.add(homepagereturn);
            }

        }
        return homepagereturnList;
    }

    /**
     * 返回值设定
     *
     * @param name
     * @param icon
     * @param no
     * @return
     */
    private Homepagereturn setHomepage(String name, String icon, int no) {

        Homepagereturn homepagereturn = new Homepagereturn();
        homepagereturn.setName(name);
        homepagereturn.setIcon(icon);
        homepagereturn.setNo(no);

        return homepagereturn;
    }

}




