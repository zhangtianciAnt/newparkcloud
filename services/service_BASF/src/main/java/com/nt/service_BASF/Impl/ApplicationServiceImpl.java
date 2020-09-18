package com.nt.service_BASF.Impl;
/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: ApplicationServiceImpl
 * @Author: LXY
 * @Description: 消防设备申请接口实现类
 * @Date: 2019/11/15
 * @Version: 1.0
 */
import com.nt.dao_BASF.Application;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.VO.DeviceinfomationVo;
import com.nt.dao_Workflow.Workflowinstance;
import com.nt.service_BASF.ApplicationServices;
import com.nt.service_BASF.mapper.ApplicationMapper;
import com.nt.service_BASF.mapper.DeviceinformationMapper;
import com.nt.service_WorkFlow.mapper.WorkflowinstanceMapper;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nt.dao_BASF.VO.ApplicationVo;

import java.util.List;
import java.util.UUID;

@Service
public class ApplicationServiceImpl implements ApplicationServices {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private DeviceinformationMapper deviceinformationMapper;

    @Autowired
    private WorkflowinstanceMapper workflowinstanceMapper;

    @Override
    public List<Application> get(Application application) throws Exception {
        return applicationMapper.getAllInfo(application.getApplicationtype());
    }

    public ApplicationVo getone(Application application) throws Exception{

        Workflowinstance workflowinstance = new Workflowinstance();
        workflowinstance.setDataid(application.getApplicationid());
        List wf =  workflowinstanceMapper.select(workflowinstance);
        String wfname = ((Workflowinstance)wf.get(0)).getWorkflowname();

        ApplicationVo applicationVo = new ApplicationVo();
        List applicationlist = applicationMapper.select(application);
        applicationVo.setApplication((Application) applicationlist.get(0));
        applicationVo.setWorkflowname(wfname);
        return applicationVo;
    }






    public List<ApplicationVo> getList() throws Exception {
        return applicationMapper.selectApplicationVoList();
    }

    @Override
    public void insert(TokenModel tokenModel, Application application) throws Exception {
        application.preInsert(tokenModel);
        application.setApplicationid(UUID.randomUUID().toString());
        application.setApplicationstatus("BC012005");

        applicationMapper.insert(application);

        Deviceinformation deviceinformation = new Deviceinformation();
        deviceinformation.setDeviceinformationid(application.getDeviceinformationid());
        deviceinformation.preUpdate(tokenModel);
        deviceinformation.setDevicestatus("BC011002");
        deviceinformationMapper.updateByPrimaryKeySelective(deviceinformation);
    }

    @Override
    public void update(TokenModel tokenModel, Application application) throws Exception {
        application.preUpdate(tokenModel);
        applicationMapper.updateByPrimaryKeySelective(application);
    }

    @Override
    public void del(TokenModel tokenModel, Application application) throws Exception {
        application.preUpdate(tokenModel);
        applicationMapper.updateByPrimaryKeySelective(application);
    }

    @Override
    public Application one(String applicationid) throws Exception {
        return applicationMapper.selectByPrimaryKey(applicationid);
    }

    //前端大屏道路占用/临时封闭区域列表（审批通过的并且使用时间≤系统时间≤归还时间）
    @Override
    public List<Application> roadClosed() throws Exception{
        return applicationMapper.roadClosed();
    }

    @Override
    public void returnBack(String applicationId) throws Exception {
        // 系统服务自动归还
        if (StringUtils.isEmpty(applicationId)) {
            List<Application> applicationList = applicationMapper.getAllReturnBack();
            applicationList.forEach(this::returnLogic);
        } else {    // 手动归还指定设备
            Application application = applicationMapper.selectByPrimaryKey(applicationId);
            returnLogic(application);
        }
    }

    private void returnLogic(Application application) {
        // 逻辑删除Application数据
        application.setStatus("1");
        applicationMapper.updateByPrimaryKey(application);

        // 重置设备状态
        Deviceinformation deviceinfomation = deviceinformationMapper.selectByPrimaryKey(application.getDeviceinformationid());
        if (deviceinfomation != null) {
            if (deviceinfomation.getDevicename().equals("虚拟路桩")) {
                deviceinfomation.setStatus("1");
            } else {
                deviceinfomation.setDevicestatus("BC011001");
            }
            deviceinformationMapper.updateByPrimaryKey(deviceinfomation);
        }
    }
}
