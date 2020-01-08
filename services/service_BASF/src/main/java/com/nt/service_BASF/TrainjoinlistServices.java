package com.nt.service_BASF;

import com.nt.dao_BASF.Trainjoinlist;
import com.nt.utils.dao.TokenModel;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: TrainjoinlistServices
 * @Author: Newtouch
 * @Description: 培训参加名单接口类
 * @Date: 2020/1/7 11:13
 * @Version: 1.0
 */
public interface TrainjoinlistServices {

    //添加培训人员名单
    void insert(Trainjoinlist trainjoinlist, TokenModel tokenModel) throws Exception;

    //根据培训列表删除参加名单
    void delete(String startprogramid, TokenModel tokenModel) throws Exception;

    //获取培训申请人员名单
    ArrayList<String> joinlist(String startprogramid) throws Exception;

    //根据人员id获取培训列表id
    List<Trainjoinlist> startprogramidList(String personnelid) throws Exception;

    //根据培训列表主键获取申请人员总数
    int joinnumber(String startprogramid) throws Exception;

    //根据培训列表主键获取实际参加人数
    int actualjoinnumber(String startprogramid) throws Exception;

}
