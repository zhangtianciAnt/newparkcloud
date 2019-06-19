package com.nt.controller.Controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.nt.dao_Auth.Vo.SignInVo;
import com.nt.dao_Auth.model.SignInInformation;
import com.nt.dao_Auth.model.UserInfo;
import com.nt.dao_Org.ServiceCategory;
import com.nt.service_Auth.SignInInformationSeivice;
import com.nt.service_Auth.mapper.SignInInformationMapper;
import com.nt.service_Auth.mapper.UserInfoMapper;
import com.nt.service_Org.ServiceCategoryService;
import com.nt.utils.ApiResult;
import com.nt.utils.MessageUtil;
import com.nt.utils.MsgConstants;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/SignIn")
public class SignInController {

    @Autowired
    private SignInInformationSeivice signInInformationSeivice;

    @Resource
    private SignInInformationMapper signInInformationMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @RequestMapping(value = "/SignIn", method = {RequestMethod.POST})
    public ApiResult saveservicecategory(String No, HttpServletRequest request) throws Exception {
        SignInInformation signInInformation = new SignInInformation();
        signInInformation.setNo(No) ;
        signInInformation.setDate(new Date());
        signInInformationSeivice.insert(signInInformation);
        return ApiResult.success();
    }

    @RequestMapping(value = "/export", method = {RequestMethod.POST})
    public void export(@RequestBody SignInVo signInVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SignInInformation SsignInInformation = new SignInInformation();
        SsignInInformation.setDate(new Date());
        List<SignInInformation> list = signInInformationMapper.select(SsignInInformation);

        List<SignInVo> rows = new ArrayList<SignInVo>();

        for (SignInInformation item :list
             ) {
            SignInVo row = new SignInVo();
            row.setNo(item.getNo());

            UserInfo userInfo = userInfoMapper.selectByPrimaryKey(item.getNo());
            row.setName(userInfo.getName());
            row.setClassname(signInVo.getClassname());
            row.setTheme(signInVo.getTheme()) ;
            row.setStart(item.getDate());
            row.setZuzhi(signInVo.getZuzhi());
            row.setPlace(signInVo.getPlace());
            row.setTime(signInVo.getTime());
            row.setPeople(signInVo.getPeople());
            row.setSignpeople(signInVo.getPeople());
            row.setSigndate(item.getDate());

            rows.add(row);
        }
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.write(rows);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=test.xls");
        ServletOutputStream out=response.getOutputStream();

        writer.flush(out);
        writer.close();
    }
}
