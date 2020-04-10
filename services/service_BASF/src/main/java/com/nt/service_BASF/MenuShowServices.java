package com.nt.service_BASF;

import com.nt.dao_BASF.MenuShow;
import com.nt.utils.dao.TokenModel;

import java.util.ArrayList;
import java.util.List;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName:BASF10208Services
 * @Author: Wxz
 * @Description:大屏分类管理接口
 * @Date: 2020/04/09 15:22
 * @Version: 1.0
 */
public interface MenuShowServices {
    //用户大屏分类状态列表
    List<MenuShow> list() throws Exception;
    //用户大屏表示更新状态
    void update(MenuShow menuShow, TokenModel tokenModel)throws Exception;
    //删除用户大屏表示
    void del(MenuShow menuShow, TokenModel tokenModel)throws Exception;
    //插入用户大屏表示
    void insert(ArrayList<MenuShow> menuShow, TokenModel tokenModel)throws Exception;
}
