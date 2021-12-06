package com.nt.service_pfans.PFANS6000;

import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.utils.LogicalException;
import com.nt.dao_Pfans.PFANS6000.CustomerinforPrimary;
import com.nt.utils.dao.TableDataInfo;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CustomerinforService {

    List<CustomerinforPrimary> getcustomerinforPrimary(CustomerinforPrimary customerinforprimary, TokenModel tokenModel) throws Exception;

    List<Customerinfor> getcustomerinfor(Customerinfor customerinfor, TokenModel tokenModel) throws Exception;

    // add  ml  211206  dialog分页  from
    TableDataInfo getCustomerinfor(int currentPage, int pageSize) throws Exception;
    // add  ml  211206  dialog分页  to

    public List<Customerinfor> getcustomerinforApplyOne(Customerinfor customerinfor) throws Exception;

    public void updatecustomerinforApply(List<Customerinfor> customerinforList, TokenModel tokenModel) throws Exception;

    public void createcustomerinforApply(List<Customerinfor> customerinforList, TokenModel tokenModel) throws Exception;

    List<String> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception;

    //region scc add 人员信息导出 from
    void downloadExcel(List<String> ids,HttpServletRequest request, HttpServletResponse resp) throws LogicalException;
    //endregion scc add 人员信息导出 to

}
