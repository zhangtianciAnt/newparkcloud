package com.nt.service_pfans.PFANS5000;

import com.nt.dao_Pfans.PFANS5000.LogPersonStatistics;
import com.nt.dao_Pfans.PFANS5000.Vo.LogPersonReturnVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

public interface LogPersonStatisticsService {

    //region 工作日 scc 9/13 from
    List<LogPersonStatistics> getLogDataList(LogPersonStatistics logPersonStatistics,String dateOf) throws Exception;
    //endregion 工作日 scc 9/13 to

    //region scc add 9/9 日志人别统计 from
    List<LogPersonReturnVo> getLogPerson(LogPersonStatistics logPersonStatistics,String month, TokenModel tokenModel) throws Exception;
    //endregion scc add 9/9 日志人别统计 to

    //region scc add 9/13 更新数据 from
    void updateByVoId(List<LogPersonReturnVo> logPersonReturnVoList) throws Exception;
    //endregion scc add 9/13 更新数据 to

    //region scc add 21/9/14 报表 from
//    Object getTableinfoReport(String month) throws Exception;
    //endregion scc add 21/9/14 报表 to
}
