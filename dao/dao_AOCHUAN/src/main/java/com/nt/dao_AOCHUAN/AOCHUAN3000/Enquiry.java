package com.nt.dao_AOCHUAN.AOCHUAN3000;


import cn.hutool.core.util.StrUtil;
import com.nt.utils.AuthConstants;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.Date;

//询价情况
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="enquiry")
public class Enquiry {

     private String enquiry_id;
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
     private String document;
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
     public void preInsert(){
          this.createon = new Date();
          this.status = AuthConstants.DEL_FLAG_NORMAL;
     }

     public void preInsert(TokenModel tokenModel){
          this.createby = tokenModel.getUserId();
          this.createon = new Date();
          if(StrUtil.isEmpty(this.owner)){
               this.owner = tokenModel.getUserId();
          }
          this.status = AuthConstants.DEL_FLAG_NORMAL;
     }

     public void preUpdate(TokenModel tokenModel){
          this.modifyby = tokenModel.getUserId();
          this.modifyon = new Date();
     }



}
