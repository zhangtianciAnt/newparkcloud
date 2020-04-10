package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.MenuShow;
import com.nt.service_BASF.MenuShowServices;
import com.nt.service_BASF.mapper.MenuShowMapper;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class MenuShowServicesImpl implements MenuShowServices {
    private static Logger log = LoggerFactory.getLogger(DeviceInformationServicesImpl.class);

    @Autowired
    private MenuShowMapper menushowMapper;

    /**
     * @Method list
     * @Author Wxz
     * @Version 1.0
     * @Description 获取用户大屏显示列表
     * @Return void
     * @Date 2020/04/09 16：17
     */
    @Override
    public List<MenuShow> list() throws Exception {
        MenuShow menuShow = new MenuShow();
        return menushowMapper.select(menuShow);
    }

    /**
     * @param menuShow
     * @param tokenModel
     * @Method update
     * @Author Wxz
     * @Version 1.0
     * @Description 更新车辆状态
     * @Return void
     * @Date 2020/04/09 16：20
     */
    @Override
    public void update(MenuShow menuShow, TokenModel tokenModel) throws Exception {
        menuShow.preUpdate(tokenModel);
        menushowMapper.updateByPrimaryKey(menuShow);
    }

    @Override
    public void insert(ArrayList<MenuShow> menuShowList, TokenModel tokenModel) throws Exception {
        MenuShow menuShow1 = new MenuShow();
        menushowMapper.delete(menuShow1);

//        menuShowList.preInsert(tokenModel);
//        menuShow.setLoginname(UUID.randomUUID().toString());
//        menushowMapper.insert(menuShow);

        for (MenuShow tmp  : menuShowList) {
            tmp.preInsert(tokenModel);
            tmp.setMenuid(UUID.randomUUID().toString());
            menushowMapper.insert(tmp);
        }
    }

    @Override
    public void del(MenuShow menuShow, TokenModel tokenModel) throws Exception {
        menuShow.preUpdate(tokenModel);
        menushowMapper.updateByPrimaryKeySelective(menuShow);
    }
}
