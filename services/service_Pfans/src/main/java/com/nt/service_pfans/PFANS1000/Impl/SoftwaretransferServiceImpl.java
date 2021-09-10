package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Assets.Assets;
import com.nt.dao_Auth.Vo.MembersVo;
import com.nt.dao_Org.CustomerInfo;
import com.nt.dao_Org.OrgTree;
import com.nt.dao_Org.ToDoNotice;
import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Notification;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo;
import com.nt.dao_Pfans.PFANS1000.Vo.SoftwaretransferVo2;
import com.nt.service_Assets.mapper.AssetsMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.service_pfans.PFANS1000.SoftwaretransferService;
import com.nt.service_pfans.PFANS1000.mapper.SoftwaretransferMapper;
import com.nt.service_pfans.PFANS1000.mapper.NotificationMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nt.utils.MongoObject.CustmizeQuery;

@Service
@Transactional(rollbackFor = Exception.class)
public class SoftwaretransferServiceImpl implements SoftwaretransferService {

    @Autowired
    private SoftwaretransferMapper softwaretransferMapper;
    @Autowired
    private AssetsMapper assetsMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private OrgTreeService orgTreeService;
    @Autowired
    private ToDoNoticeService toDoNoticeService;
    @Autowired
    private RoleService roleService;

//    @Override
//    public List<Softwaretransfer> getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception {
//        return softwaretransferMapper.select(softwaretransfer);
//    }

    @Override
    public  List<SoftwaretransferVo2> getSoftwaretransfer(Softwaretransfer softwaretransfer) throws Exception {
        return softwaretransferMapper.getSoftware(softwaretransfer.getOwners()).stream().sorted(Comparator.comparing(SoftwaretransferVo2::getCreateon).reversed()).collect(Collectors.toList());
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
        //region scc add 9/6 资产转移及管理者变更决裁，接收人接受代办通知 from
        ArrayList<String> eafter = new ArrayList<>();//接收人
        //endregion scc add 9/6 资产转移及管理者变更决裁，接收人接受代办通知 to
        if (notificationlist != null) {
            int rowindex = 0;
            for (Notification notification : notificationlist) {
                rowindex = rowindex + 1;
                notification.preInsert(tokenModel);
                notification.setNotificationid(UUID.randomUUID().toString());
                notification.setSoftwaretransferid(ssoftwaretransferid);
                notification.setRowindex(rowindex);
                notificationMapper.insertSelective(notification);
                //PSDCD_PFANS_20210406_BUG_040 ztc 资产转移BUG start
                if(softwaretransfer.getStatus().equals("4")){
                    Assets assets = new Assets();
                    assets.setBarcode(notification.getManagement());
                    List<Assets> assetsList = assetsMapper.select(assets);
                    for(Assets ast : assetsList){
                        ast.setOwner(notification.getEafter());
                        ast.setPrincipal(notification.getEafter());
                        String flgid = softwaretransfer.getTubecenter_id();
                        OrgTree orgs = orgTreeService.get(new OrgTree());
                        OrgTree currentOrg = getCurrentOrg(orgs, flgid);
                        ast.setUsedepartment(currentOrg.getCompanyen());
                        assetsMapper.updateByPrimaryKeySelective(ast);
                    }
                }
                if(softwaretransfer.getStatus().equals("4")) {
                    ToDoNotice toDoNotice = new ToDoNotice();
                    toDoNotice.setTitle("有一个【资产部门间转移及管理者变更决裁】审批已结束，请注意查看！");
                    toDoNotice.setInitiator(softwaretransfer.getUser_id());
                    toDoNotice.setContent("有一个【资产部门间转移及管理者变更决裁】审批已结束，请注意查看！");
                    toDoNotice.setUrl("/PFANS1008FormView");
                    toDoNotice.setWorkflowurl("/PFANS1008FormView");
                    toDoNotice.setDataid(softwaretransfer.getSoftwaretransferid());
                    toDoNotice.preInsert(tokenModel);
                    List<MembersVo> rolelist = roleService.getMembers("606bef4253b22307706e52e7");
                    if (rolelist.size() > 0) {
                        for (int t = 0; t < rolelist.size(); t++) {
                            //尹金顺，王颖不接受通知
                            if (("5f55c9f89729aa16f0014fa9").equals(rolelist.get(t).getUserid())|| ("5e78b23e4e3b194874180fe5").equals(rolelist.get(t).getUserid())) {
                                continue;
                            }else {
                                toDoNotice.setOwner(rolelist.get(t).getUserid());
                                toDoNoticeService.save(toDoNotice);
                            }
                        }
                    }
                    //region scc add 9/6 资产转移及管理者变更决裁，接收人接受代办通知 from
                    eafter.add(notification.getEafter());//接收人
                    //endregion scc add 9/6 资产转移及管理者变更决裁，接收人接受代办通知 to
                }
            }
            //region scc add 9/6 资产转移及管理者变更决裁，接收人接受代办通知 from
            if(eafter.size() > 0){
                List<String> managements = eafter.stream().distinct().collect(Collectors.toList());
                ToDoNotice toDoNotice1 = new ToDoNotice();
                toDoNotice1.setTitle("您有资产需确认！");
                toDoNotice1.setInitiator(softwaretransfer.getUser_id());
                toDoNotice1.setContent("有资产已经转移到您名下，请及时进行资产确认！");
                toDoNotice1.setUrl("/PFANS1008FormView");
                toDoNotice1.setWorkflowurl("/PFANS1008FormView");
                toDoNotice1.setDataid(softwaretransfer.getSoftwaretransferid());
                toDoNotice1.preInsert(tokenModel);
                for(String management : managements){
                    toDoNotice1.setOwner(management);
                    toDoNoticeService.save(toDoNotice1);
                }

            }
            //endregion scc add 9/6 资产转移及管理者变更决裁，接收人接受代办通知 to
        }
    }


    private OrgTree getCurrentOrg(OrgTree org, String orgId) throws Exception {
        if (org.get_id().equals(orgId)) {
            return org;
        } else {
            if (org.getOrgs() != null) {
                for (OrgTree item : org.getOrgs()) {
                    OrgTree or = getCurrentOrg(item, orgId);
                    if (or.get_id().equals(orgId)) {
                        return or;
                    }
                }
            }

        }
        return org;
    }

//PSDCD_PFANS_20210406_BUG_040 ztc 资产转移BUG end

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
