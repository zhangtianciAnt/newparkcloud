package com.nt.service_Assets.Impl;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Assets.Inventoryplan;
import com.nt.dao_Assets.Vo.InventoryplanVo;
import com.nt.service_Assets.InventoryplanService;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Assets.mapper.InventoryplanMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class InventoryplanServiceImpl implements InventoryplanService{

    @Autowired
    private InventoryplanMapper inventoryplanMapper;
    @Autowired
    private AssetsMapper assetsMapper;

    //列表查询
    @Override
    public List<Inventoryplan> get(Inventoryplan inventoryplan) throws Exception {
        return inventoryplanMapper.select(inventoryplan);
    }


}
