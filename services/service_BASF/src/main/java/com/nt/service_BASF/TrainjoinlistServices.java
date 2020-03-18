package com.nt.service_BASF;

import com.nt.dao_BASF.Trainjoinlist;
import com.nt.dao_BASF.VO.OverduePersonnelListVo;
import com.nt.dao_BASF.VO.TrainjoinlistVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletRequest;
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
    void insert(TrainjoinlistVo trainjoinlistVo, TokenModel tokenModel) throws Exception;

    //更新培训人员名单
    void updata(Trainjoinlist trainjoinlist, TokenModel tokenModel) throws Exception;

    //在线培训更新人员
    void updataOnline(Trainjoinlist trainjoinlist, TokenModel tokenModel) throws Exception;

    //在线培训添加培训人员名单
    String onlineInsert(TrainjoinlistVo trainjoinlistVo, TokenModel tokenModel) throws Exception;

    //根据培训列表id删除参加名单
    void delete(String startprogramid, TokenModel tokenModel) throws Exception;

    //检测是否参加过此培训
    boolean verifyTrai(Trainjoinlist trainjoinlist) throws Exception;

    //获取培训申请人员id名单
    ArrayList<String> joinlist(String startprogramid) throws Exception;

    //获取培训申请人员名单
    List<Trainjoinlist> joinlists(String startprogramid) throws Exception;

    //根据人员id获取培训列表id
    List<Trainjoinlist> startprogramidList(String personnelid) throws Exception;

    //根据培训列表主键获取申请人员总数
    int joinnumber(String startprogramid) throws Exception;

    //根据培训列表主键获取实际参加人数
    int actualjoinnumber(String startprogramid) throws Exception;

    //在线培训用，通过和不通过人数的和
    int throughAndNoThrough(String startprogramid) throws Exception;

    //根据培训主键获取实际参加通过的人数
    int throughjoinnumber(String startprogramid) throws Exception;

    //excel文档导入
    List<String> importexcel(HttpServletRequest request, TokenModel tokenModel) throws Exception;

    //即将到期人员列表（前端培训教育大屏用）
    List<OverduePersonnelListVo> overduepersonnellist() throws Exception;

    //结果发布判断该培训是否存在正常参加人员通过状态为空
    boolean isNotThroughtype(String startprogramid) throws Exception;
}
