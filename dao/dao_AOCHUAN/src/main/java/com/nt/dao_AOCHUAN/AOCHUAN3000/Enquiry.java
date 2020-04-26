package com.nt.dao_AOCHUAN.AOCHUAN3000;

import cn.hutool.core.util.StrUtil;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

//询价情况
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="enquiry")
public class Enquiry {
     //主键
     private String enquiry_id;
     //询报价外键
     private String quotations_id;
     //供应商
     private String supplier;
     //供应商id
     private String supplierid;
     //区分
     private String flag;
     //数量
     private String amount;
     //报关价/采购报价
     private String quotedprice;
     //汇率
     private String exchangerate;
     //关税率
     private String tariffrate;
     //增值税率
     private String addtax;
     //退税率
     private String drawback;
     //利润率
     private String profitpoint;
     //销售报价
     private String salesquotation;
     //质量管理体系
     private String iso;
     //其他客户进展
     private String process;
     //海外官方机构认证
     private String antecedents;
     //文件情况
     private String doc;
     //数量
     private String counts;

     @Transient
     private String[] document;

     //产品状态
     private String productstatus;
     //备注
     private String note;

     /**
      * 状态
      */
     private String status;
     /**
      * 创建时间
      */
     @OrderBy("ASC")
     private Date createon;
     /**
      * 创建者
      */
     private String createby;
     /**
      * 更新时间
      */
     private Date modifyon;
     /**
      * 更新者
      */
     private String modifyby;
     /**
      * 负责人
      */
     private String owner;

     @Transient
     private List<String> owners;

     @Transient
     private Integer currentPage;

     @Transient
     private Integer pageSize;

     @Transient
     private List<String> ids;

     @Transient
     private String httpOriginType;

}
