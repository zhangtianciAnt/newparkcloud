package com.nt.controller.Controller;



import com.nt.dao_Org.WorkOrder;
import com.nt.service_Org.WorkOrderService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/workorder")
public class WorkOrderController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private WorkOrderService workorderService;


    /**
     * @方法名：saveinformation
     * @描述：工单保存
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[information, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/saveworkorder", method = {RequestMethod.POST})
    public ApiResult saveworkorder(@RequestBody WorkOrder workorder, HttpServletRequest request) throws Exception {
        if (workorder == null || StringUtils.isEmpty(workorder)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        if(workorder.getStatus()==null)
        {

            workorder.preInsert(tokenModel);
            workorderService.save(workorder,tokenModel);
        }
        else
        {
            workorder.preUpdate(tokenModel);
            workorderService.save(workorder,tokenModel);
        }
        return ApiResult.success();
    }

    /**
     * @方法名：getinformation
     * @描述：获取工单列表
     * @创建日期：2018/12/13
     * @作者：SUNXU
     * @参数：[request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getworkorder", method = {RequestMethod.POST})
    public ApiResult getworkorder(@RequestBody WorkOrder workorder,HttpServletRequest request) throws Exception {

        TokenModel tokenModel = tokenService.getToken(request);
        if(workorder.getSource().equals("1"))
        {
            workorder.setTenantid(tokenModel.getTenantId());
            workorder.setOwners(tokenModel.getOwnerList());
            workorder.setIds(tokenModel.getIdList());
            workorder.setSource(null);
            return ApiResult.success(workorderService.get(workorder));
        }
        else{
            workorder.setOwner(tokenModel.getUserId());
            return ApiResult.success(workorderService.get(workorder));
        }
    }

    /**
     * @方法名：getInfoById
     * @描述：根据id获取工单信息
     * @创建日期：2018/12/19
     * @作者：SUNXU
     * @参数：[id, request]
     * @返回值：com.nt.utils.ApiResult
     */
    @RequestMapping(value = "/getInfoById", method = {RequestMethod.GET})
    public ApiResult getInfoById(String id, HttpServletRequest request) throws Exception {
        WorkOrder workorder = new WorkOrder();
        return ApiResult.success(workorderService.getInfoById(id));
    }







}
