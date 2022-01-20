package com.nt.service_pfans.PFANS1000;

import com.nt.dao_Pfans.PFANS1000.Quotation;
import com.nt.dao_Pfans.PFANS1000.Vo.QuotationVo;
import com.nt.utils.dao.TokenModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface QuotationService {

    List<Quotation> get(Quotation quotation) throws Exception;

    //  add  ml  211130  报价单分页  from
    List<Quotation> getQuotation(Quotation quotation) throws Exception;
    //  add  ml  211130  报价单分页  to

    public QuotationVo selectById(String quotationid) throws Exception;

    void update(QuotationVo quotationVo, TokenModel tokenModel) throws Exception;

//    void download(String quotationid, HttpServletResponse response) throws Exception ;

}
