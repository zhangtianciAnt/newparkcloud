package com.nt.controller.Controller.AOCHUAN;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Reimbursement;
import com.nt.dao_AOCHUAN.AOCHUAN6000.ReimbursementDetail;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Vo.ReimAndReimDetail;
import com.nt.service_AOCHUAN.AOCHUAN6000.ReimbursementService;
import com.nt.utils.*;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reimbursement")
public class AOCHUAN6006Controller {

    @Autowired
    private ReimbursementService reimbursementService;

    @Autowired
    private TokenService tokenService;

    /**
     * 获取费用表数据
     */
    @RequestMapping(value = "/getReimbursementList", method = {RequestMethod.POST})
    public ApiResult getReimbursementList(HttpServletRequest request) throws Exception {
        Reimbursement reimbursement = new Reimbursement();
        return ApiResult.success(reimbursementService.getReimbursementList(reimbursement));
    }

    /**
     * 获取费用表
     */
    @RequestMapping(value = "/getForm",method={RequestMethod.GET})
    public ApiResult getForm(@RequestParam String id, HttpServletRequest request) throws Exception {
        return ApiResult.success(reimbursementService.getForm(id));
    }

    /**
     * 获取费用明细表数据
     */
    @RequestMapping(value = "/getReimbursementDetailList", method = {RequestMethod.POST})
    public ApiResult getReimbursementDetailList(@RequestBody ReimbursementDetail reimbursementDetail, HttpServletRequest request) throws Exception {
        return ApiResult.success(reimbursementService.getReimbursementDetailList(reimbursementDetail));
    }

    /**
     * 新建
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody ReimAndReimDetail reimAndReimDetail, HttpServletRequest request) throws Exception {

        if (reimAndReimDetail == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //随机主键
        String id;
        String number;
        Date date = new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyyMMdd" );

        //INSERT:PROJECTS
        Reimbursement reimbursement = reimAndReimDetail.getReimForm();

        id= UUID.randomUUID().toString();
        number ="BX"+ sdf.format(date) + UUID.randomUUID().toString().substring(0,6);

        reimbursement.setReimbursement_id(id);
        reimbursement.setReimbursement_no(number);

        //存在Check
        if (!reimbursementService.existCheck(reimbursement)) {
            //唯一性Check
            if(! reimbursementService.uniqueCheck(reimbursement)){
                reimbursementService.insert(reimbursement, tokenService.getToken(request));
            }
            else {
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //INSERT:FOLLOWUPRECORD
        List<ReimbursementDetail> reimDetailList = reimAndReimDetail.getReimFormList();
        for(ReimbursementDetail reimbursementDetail:reimDetailList){

            id= UUID.randomUUID().toString();
            reimbursementDetail.setReimbursement_detail_id(id);
            reimbursementDetail.setReimbursement_no(reimbursement.getReimbursement_no());

            //存在Check
            if (!reimbursementService.existCheck(reimbursementDetail)) {

                reimbursementDetail.setReimbursement_no(number);
                reimbursementService.insert(reimbursementDetail,tokenService.getToken(request));
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody ReimAndReimDetail reimAndReimDetail, HttpServletRequest request) throws Exception {

        if (reimAndReimDetail == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //UPDATE:PROJECTS
        Reimbursement reimbursement = reimAndReimDetail.getReimForm();
        //存在Check
        if (reimbursementService.existCheck(reimbursement)) {
            //唯一性Check
            if(! reimbursementService.uniqueCheck(reimbursement)) {
                reimbursementService.update(reimbursement, tokenService.getToken(request));

                //UPDATE:FOLLOWUPRECORD
                List<ReimbursementDetail> reimDetailList = reimAndReimDetail.getReimFormList();
                for(ReimbursementDetail reimbursementDetail:reimDetailList){

                    reimbursementDetail.setReimbursement_no(reimbursement.getReimbursement_no());

                    //存在Check
                    if (reimbursementService.existCheck(reimbursementDetail)) {
                        reimbursementService.update(reimbursementDetail, tokenService.getToken(request));
                    }else{
                        String id= UUID.randomUUID().toString();
                        reimbursementDetail.setReimbursement_detail_id(id);
                        reimbursementService.insert(reimbursementDetail,tokenService.getToken(request));
                    }
                }
            }else{
                return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public ApiResult del(@RequestBody ReimAndReimDetail reimAndReimDetail, HttpServletRequest request) throws Exception {

        if (reimAndReimDetail == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //DELETE:PROJECTS
        Reimbursement reimbursement = reimAndReimDetail.getReimForm();
        //存在Check
        if (reimbursementService.existCheck(reimbursement)) {
            reimbursementService.delete(reimbursement, tokenService.getToken(request));

            //DELETE:FOLLOWUPRECORD
            List<ReimbursementDetail> reimDetailList = reimAndReimDetail.getReimFormList();
            for(ReimbursementDetail reimbursementDetail:reimDetailList){

                reimbursementDetail.setReimbursement_no(reimbursement.getReimbursement_no());

                //存在Check
                if (reimbursementService.existCheck(reimbursementDetail)) {
                    reimbursementService.delete(reimbursementDetail, tokenService.getToken(request));
                }
            }
        }else{
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        return ApiResult.success();
    }
}
