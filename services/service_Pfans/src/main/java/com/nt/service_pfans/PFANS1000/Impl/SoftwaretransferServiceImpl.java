package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Notification;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo2;
import com.nt.service_pfans.PFANS1000.SoftwaretransferService;
import com.nt.service_pfans.PFANS1000.mapper.SoftwaretransferMapper;
import com.nt.service_pfans.PFANS1000.mapper.NotificationMapper;
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
public class SoftwaretransferServiceImpl implements SoftwaretransferService {

    @Autowired
    private SoftwaretransferMapper softwaretransferMapper;
    @Autowired
    private NotificationMapper notificationMapper;

//    @Override
//    public List<Softwaretransfer> getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception {
//        return softwaretransferMapper.select(softwaretransfer);
//    }

    @Override
    public  List<SoftwaretransferVo2> getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception {
        return softwaretransferMapper.getSoftware(softwaretransfer.getOwner());
    }

    @Override
    public SoftwaretransferVo selectById(String softwaretransferid) throws Exception {
        SoftwaretransferVo softVo = new SoftwaretransferVo();
        Notification notification = new Notification();
        notification.setSoftwaretransferid(softwaretransferid);
        List<Notification> notificationlist = notificationMapper.select(notification);
        notificationlist = notificationlist.stream().sorted(Comparator.comparing(Notification::getRowindex)).collect(Collectors.toList());
        Softwaretransfer soft = softwaretransferMapper.selectByPrimaryKey(softwaretransferid);
        softVo.setSoftwaretransfer(soft);
        softVo.setNotification(notificationlist);
        return softVo;
    }

    @Override
    public void updateSoftwaretransfer(SoftwaretransferVo softwaretransferVo, TokenModel tokenModel) throws Exception {
        Softwaretransfer softwaretransfer = new Softwaretransfer();
        BeanUtils.copyProperties(softwaretransferVo.getSoftwaretransfer(), softwaretransfer);
        softwaretransfer.preUpdate(tokenModel);
        softwaretransferMapper.updateByPrimaryKey(softwaretransfer);
        String ssoftwaretransferid = softwaretransfer.getSoftwaretransferid();
        Notification not = new Notification();
        not.setSoftwaretransferid(ssoftwaretransferid);
        notificationMapper.delete(not);
        List<Notification> notificationlist = softwaretransferVo.getNotification();
        if (notificationlist != null) {
            int rowindex = 0;
            for (Notification notification : notificationlist) {
                rowindex = rowindex + 1;
                notification.preInsert(tokenModel);
                notification.setNotificationid(UUID.randomUUID().toString());
                notification.setSoftwaretransferid(ssoftwaretransferid);
                notification.setRowindex(rowindex);
                notificationMapper.insertSelective(notification);
            }
        }
    }

    @Override
    public void insert(SoftwaretransferVo softwaretransferVo, TokenModel tokenModel) throws Exception {
        String softwaretransferid = UUID.randomUUID().toString();
        Softwaretransfer softwaretransfer = new Softwaretransfer();
        BeanUtils.copyProperties(softwaretransferVo.getSoftwaretransfer(), softwaretransfer);
        softwaretransfer.preInsert(tokenModel);
        softwaretransfer.setSoftwaretransferid(softwaretransferid);
        softwaretransferMapper.insertSelective(softwaretransfer);
        List<Notification> notificationlist = softwaretransferVo.getNotification();
        if (notificationlist != null) {
            int rowindex = 0;
            for (Notification notification : notificationlist) {
                rowindex = rowindex + 1;
                notification.preInsert(tokenModel);
                notification.setNotificationid(UUID.randomUUID().toString());
                notification.setSoftwaretransferid(softwaretransferid);
                notification.setRowindex(rowindex);
                notificationMapper.insertSelective(notification);
            }
        }
    }
}
