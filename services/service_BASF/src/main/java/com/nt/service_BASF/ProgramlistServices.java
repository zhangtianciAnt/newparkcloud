package com.nt.service_BASF;

import com.nt.dao_BASF.Programlist;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF21207Services
 * @Author:
 * @Description: BASF培训计划清单模板
 * @Date: 2019/11/4 16:09
 * @Version: 1.0
 */
public interface ProgramlistServices {

    //获取培训计划清单
    List<Programlist>list(Programlist programlist) throws Exception;
    //获取培训计划清单详情
    Programlist one(String programlistid) throws Exception;
    //创建培训计划清单
    void insert(Programlist programlist, TokenModel tokenModel) throws Exception;
    //更新培训计划清单
    void update(Programlist programlist, TokenModel tokenModel) throws Exception;
    //删除培训清单
    void delete(Programlist programlist) throws Exception;
    //execl导入
    List<String> insert(HttpServletRequest request, TokenModel tokenModel) throws Exception;


}
