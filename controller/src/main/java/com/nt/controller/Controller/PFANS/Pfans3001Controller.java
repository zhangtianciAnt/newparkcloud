package com.nt.controller.Controller.PFANS;

import com.nt.dao_Pfans.PFANS3000.Tickets;
import com.nt.dao_Pfans.PFANS3000.Vo.TicketsVo;
import com.nt.service_pfans.PFANS3000.TicketsService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.RequestUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tickets")
public class Pfans3001Controller {

    @Autowired
    private TicketsService ticketsService;

    @Autowired
    private TokenService tokenService;

    @RequestMapping(value = "/insertInfo", method = {RequestMethod.POST})
    public ApiResult insertInfo(@RequestBody Tickets tickets, HttpServletRequest request) throws Exception {
        if (tickets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        ticketsService.insert(tickets, tokenModel);
        return ApiResult.success();
    }

    //11
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public ApiResult insert(@RequestBody TicketsVo ticketsVo, HttpServletRequest request) throws Exception {
        if (ticketsVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        ticketsService.insert1(ticketsVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    public ApiResult list(HttpServletRequest request) throws Exception {
        TokenModel tokenModel = tokenService.getToken(request);
        Tickets tickets = new Tickets();
        tickets.setOwners(tokenModel.getOwnerList());
        return ApiResult.success(ticketsService.list(tickets));
    }

    @RequestMapping(value = "updateInfo", method = {RequestMethod.POST})
    public ApiResult updateInfo(@RequestBody Tickets tickets, HttpServletRequest request) throws Exception {
        if (tickets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        ticketsService.update(tickets, tokenModel);
        return ApiResult.success();
    }

    //11
    @RequestMapping(value = "update", method = {RequestMethod.POST})
    public ApiResult update(@RequestBody TicketsVo ticketsVo, HttpServletRequest request) throws Exception {
        if (ticketsVo == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        TokenModel tokenModel = tokenService.getToken(request);
        ticketsService.update1(ticketsVo, tokenModel);
        return ApiResult.success();
    }

    @RequestMapping(value = "oneInfo", method = {RequestMethod.POST})
    public ApiResult one(@RequestBody Tickets tickets, HttpServletRequest request) throws Exception {
        if (tickets == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(ticketsService.One(tickets.getTickets_id()));
    }

    //11
    @RequestMapping(value = "selectById", method = {RequestMethod.GET})
    public ApiResult selectById(String tickets_id, HttpServletRequest request) throws Exception {
        if (tickets_id == null) {
            return ApiResult.fail(MessageUtil.getMessage(MsgConstants.ERROR_03, RequestUtils.CurrentLocale(request)));
        }
        return ApiResult.success(ticketsService.selectById(tickets_id));
    }

}
