package com.nt.controller.Controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.nt.controller.Controller.WebSocket.WebSocket;
import com.nt.controller.Controller.WebSocket.WebSocketDeviceinfoVo;
import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Firealarm;
import com.nt.dao_BASF.VO.DeviceinformationVo;
import com.nt.dao_Org.Dictionary;
import com.nt.service_BASF.DeviceInformationServices;
import com.nt.service_BASF.FirealarmServices;
import com.nt.service_BASF.HomePageServices;
import com.nt.service_Org.DictionaryService;
import com.nt.utils.*;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private HomePageServices homepageservice;

    @Autowired
    private FirealarmServices firealarmServices;

    @Autowired
    private DeviceInformationServices deviceinFormationServices;

    // websocket消息推送
    private WebSocket ws = new WebSocket();
    private WebSocketDeviceinfoVo webSocketDeviceinfoVo = new WebSocketDeviceinfoVo();


    @RequestMapping(value = "/getForSelect", method = {RequestMethod.GET})
    public ApiResult getForSelect(String code, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(code)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }

        return ApiResult.success(dictionaryService.getForSelect(code));
    }

    @RequestMapping(value = "/all",method={RequestMethod.GET})
    public ApiResult all(HttpServletRequest request) throws Exception {
        //region 推送消防报警单和报警设备
        //获取并立即推送非误报且未完成的消防报警单
        Firealarm firealarmnew = new Firealarm();
        firealarmnew.setCompletesta("0");
        firealarmnew.setMisinformation("0");
        List<Firealarm> firealarms = firealarmServices.list(firealarmnew);
        webSocketDeviceinfoVo.setTopfirealarmList(firealarms);

        //设置剩余的报警层级状态
        List<DeviceinformationVo> list = new ArrayList<>();
        for (Firealarm fi : firealarms) {
            if (StringUtils.isNotEmpty(fi.getDeviceinformationid())) {
                Deviceinformation deviceinformation = deviceinFormationServices.one(fi.getDeviceinformationid());
                if (deviceinformation != null && StringUtils.isNotEmpty(deviceinformation.getMapid())) {
                    //添加报警设备信息和报警单id
                    DeviceinformationVo linkagelistVo = new DeviceinformationVo();
                    linkagelistVo.setFirealarmuuid(fi.getFirealarmid());
                    linkagelistVo.setDeviceinformation(deviceinformation);
                    list.add(linkagelistVo);
                }
            }
        }
        // 推送报警设备信息
        webSocketDeviceinfoVo.setDeviceinformationList(list);
        ws.sendMessageToAll(new TextMessage(JSONObject.toJSONString(webSocketDeviceinfoVo)));
        //endregion
        return ApiResult.success(dictionaryService.getForSelect(""));
    }

//    以下接口用于用户级数据字典页面

    /**
     * @Method bigList
     * @Author 王哲
     * @Version 1.0
     * @Description 查询字典大分类（即PCODE为空,STATUS为不为1的数据）
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/13 10:06
     */
    @RequestMapping(value = "/bigList", method = {RequestMethod.GET})
    public ApiResult bigList(HttpServletRequest request) throws Exception {
        return ApiResult.success(dictionaryService.bigList());
    }

    /**
     * @Method smallAtbig
     * @Author 王哲
     * @Version 1.0
     * @Description 查询字典大分类中的小分类
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/13 10:06
     */
    @RequestMapping(value = "/smallAtbig", method = {RequestMethod.GET})
    public ApiResult bigList(String code, HttpServletRequest request) throws Exception {
        //小特性，当STATUS为空字符串的为系统隐藏字典,但不影响使用
        return ApiResult.success(dictionaryService.smallAtbig(code));
    }

    /**
     * @Method deleteCodes
     * @Author 王哲
     * @Version 1.0
     * @Description 删除小分类字典
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/13 11:37
     */
    @RequestMapping(value = "/deleteCodes", method = {RequestMethod.POST})
    public ApiResult deleteCodes(@RequestBody List<Dictionary> dictionaries, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        dictionaryService.deleteCodes(dictionaries, tokenModel);
        return ApiResult.success();
    }

    /**
     * @Method updataCodes
     * @Author 王哲
     * @Version 1.0
     * @Description 更新小分类字典
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/13 13:25
     */
    @RequestMapping(value = "/updataCodes", method = {RequestMethod.POST})
    public ApiResult updataCodes(@RequestBody List<Dictionary> dictionaries, HttpServletRequest request) throws Exception {
        if (dictionaries == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        dictionaryService.updataCodes(dictionaries, tokenModel);
        return ApiResult.success();
    }

    /**
     * @Method insertCodes
     * @Author 王哲
     * @Version 1.0
     * @Description 新增小分类字典
     * @Return com.nt.utils.ApiResult
     * @Date 2019/12/13 13:26
     */
    @RequestMapping(value = "/insertCodes", method = {RequestMethod.POST})
    public ApiResult insertCodes(@RequestBody List<Dictionary> dictionaries, HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        dictionaryService.insertCodes(dictionaries, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/getHomePage",method={RequestMethod.GET})
    public ApiResult getHomePage(String loginname, HttpServletRequest request) throws Exception {
        if (StrUtil.isEmpty(loginname)) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(homepageservice.getHomepagecontrolshowList(loginname));
    }


}
