package com.nt.controller.Controller.AOCHUAN;

import com.nt.dao_AOCHUAN.AOCHUAN3000.Reg_Record;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Registration;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Vo.RegistrationAndRegRecord;
import com.nt.service_AOCHUAN.AOCHUAN1000.SupplierbaseinforService;
import com.nt.service_AOCHUAN.AOCHUAN3000.RegistrationService;
import com.nt.service_AOCHUAN.AOCHUAN4000.ProductsService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.apache.el.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/reg")
public class AOCHUAN3010Controller {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private SupplierbaseinforService supplierbaseinforService;
    @Autowired
    private TokenService tokenService;

    /**
     * 获取注册表list
     */
    @RequestMapping(value = "/getRegList", method = {RequestMethod.GET})
    public ApiResult getRegList(HttpServletRequest request) throws Exception {
        return ApiResult.success(registrationService.getRegList());
    }

    /**
     * 根据主键查询
     */
    @RequestMapping(value = "/getReg", method = {RequestMethod.GET})
    public ApiResult getReg(@RequestParam String id, HttpServletRequest request) throws Exception {
        return ApiResult.success(registrationService.getReg(id));
    }

    /**
     * 根据注册表id查询
     */
    @RequestMapping(value = "/getRecordList", method = {RequestMethod.GET})
    public ApiResult getRecordList(@RequestParam String id, HttpServletRequest request) throws Exception {
        return ApiResult.success(registrationService.getRecordList(id));
    }

    /**
     * 获取不在项目表中的产品
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getProdutsExceptUnique", method = {RequestMethod.GET})
    public ApiResult getProdutsExceptUnique(HttpServletRequest request) throws Exception {
        return ApiResult.success(productsService.getProdutsExceptUniqueInReg());
    }

    /**
     * 获取不在项目表中的供应商
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getSuppliersExceptUnique", method = {RequestMethod.GET})
    public ApiResult getSuppliersExceptUnique(HttpServletRequest request) throws Exception {
        return ApiResult.success(supplierbaseinforService.getSuppliersExceptUnique());
    }

    /**
     * 新建
     * @param registrationAndRegRecord
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public  ApiResult insert(@RequestBody RegistrationAndRegRecord registrationAndRegRecord, HttpServletRequest request) throws  Exception{
        TokenModel tokenModel = tokenService.getToken(request);

        if (registrationAndRegRecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //注册表新建
        Registration registration = registrationAndRegRecord.getRegistration();
        if (registration == null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        registration.setReg_id(UUID.randomUUID().toString());
        if(!registrationService.insert(registration,tokenModel)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //记录表新建
        List<Reg_Record> reg_recordList = registrationAndRegRecord.getReg_records();
        if(!reg_recordList.isEmpty()){
            for (Reg_Record item: reg_recordList) {
                item.setRecord_id(UUID.randomUUID().toString());
                item.setReg_id(registration.getReg_id());
                if (!registrationService.insert(item,tokenModel)){
                    return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                }
            }
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 更新
     * @param registrationAndRegRecord
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/update", method = {RequestMethod.POST})
    public  ApiResult update(@RequestBody RegistrationAndRegRecord registrationAndRegRecord, HttpServletRequest request) throws  Exception{

        TokenModel tokenModel = tokenService.getToken(request);

        if (registrationAndRegRecord == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //注册表更新
        Registration registration = registrationAndRegRecord.getRegistration();
        if(registration==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        registrationService.update(registration,tokenModel);

        //记录表更新
        List<Reg_Record> reg_recordList = registrationAndRegRecord.getReg_records();
        //根据注册表id获取已存的记录表
        List<Reg_Record> dbList = registrationService.getRecordList(registration.getReg_id());

        if (reg_recordList.isEmpty()) {
            if(!dbList.isEmpty()){
                //根据注册表id删除
                registrationService.recordDel(registration.getReg_id(),tokenModel);
            }
        } else {
            Map<String, List<Reg_Record>> resultMap = new HashMap<>();

            //获取差异部分
            resultMap = getDiffLst(reg_recordList, dbList);
            List<Reg_Record> diffList = resultMap.get("diff");
            List<Reg_Record> sameList = resultMap.get("same");

            //不同部分
            if (!diffList.isEmpty()) {
                //新建/删除
                for (Reg_Record item : diffList) {
                    Reg_Record reg_record = item;

                    //删除
                    if (registrationService.recordIsExist(reg_record.getRecord_id())) {
                        if(!registrationService.recordDel(reg_record,tokenModel)){
                            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                        }
                    }
                    //新建
                    else {
                        String id = UUID.randomUUID().toString();
                        reg_record.setRecord_id(id);
                        reg_record.setReg_id(registration.getReg_id());
                        if(!registrationService.insert(reg_record, tokenModel)){
                            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                        }
                    }
                }
            }
            //相同部分
            if (!sameList.isEmpty()) {
                //更新
                for (Reg_Record item : sameList) {
                    Reg_Record reg_record = item;
                    reg_record.setReg_id(registration.getReg_id());
                   if(!registrationService.update(reg_record, tokenModel)){
                       return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
                   }
                }
            }
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 删除
     * @param registration
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/del", method = {RequestMethod.POST})
    public  ApiResult del(@RequestBody Registration registration, HttpServletRequest request) throws  Exception{

        TokenModel tokenModel = tokenService.getToken(request);

        //注册表删除/记录表删除
        if (registration==null){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        if(!registrationService.del(registration,tokenModel)){
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        //正常结束
        return ApiResult.success();
    }

    /**
     * 比较list
     *
     * @param list1
     * @param list2
     * @return
     */
    private Map<String, List<Reg_Record>> getDiffLst(List<Reg_Record> list1, List<Reg_Record> list2) {

        List<Reg_Record> maxList = new ArrayList<>();
        List<Reg_Record> minList = new ArrayList<>();
        List<Reg_Record> diffList = new ArrayList<>();
        List<Reg_Record> sameList = new ArrayList<>();


        //区分大小
        if (list1.size() > list2.size()) {
            maxList = list1;
            minList = list2;
        } else {
            minList = list1;
            maxList = list2;
        }

        Map<Reg_Record, Integer> map = new HashMap<Reg_Record, Integer>();

        //参照
        for (Reg_Record item : maxList) {
            map.put(item, 1);
        }

        if(!minList.isEmpty()) {
            //比较
            for (Reg_Record item : minList) {
                if (map.get(item) != null) {
                    map.put(item, 3);
                    continue;
                } else {
                    boolean isSame = false;
                    for (Map.Entry<Reg_Record, Integer> entry : map.entrySet()) {
                        if(StringUtils.isNotBlank(entry.getKey().getRecord_id())){
                            if (entry.getKey().getRecord_id().equals( item.getRecord_id())) {
                                map.put(entry.getKey(), 3);
                                isSame = true;
                                break;
                            }
                        }
                        }
                    if (isSame) {
                        map.put(item, 2);
                        continue;
                    }
                    map.put(item, 1);
                }
            }

            //筛选出差异部分
            for (Map.Entry<Reg_Record, Integer> entry : map.entrySet()) {
                if (entry.getValue() == 1) {
                    diffList.add(entry.getKey());
                } else if (entry.getValue() == 2) {
                    sameList.add(entry.getKey());
                }
            }
        }else{

            //db中取到空
            diffList = maxList;
        }


        Map<String, List<Reg_Record>> resultMap = new HashMap<>();

        resultMap.put("diff", diffList);
        resultMap.put("same", sameList);

        return resultMap;
    }
}
