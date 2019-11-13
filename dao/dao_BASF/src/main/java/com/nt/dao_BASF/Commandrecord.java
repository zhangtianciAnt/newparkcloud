package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: Information
 * @Description: 信息
 * @Author: sunxu
 * @CreateDate: 2018/12/06
 * @UpdateUser: sunxu
 * @UpdateDate: 2018/12/06
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

@Document(collection = "commandrecord")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commandrecord extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;
    private String commandrecordno; //指挥单单号
    private String accidentlocation;  //事故地点
    private String alarmpeople;  //报警人
    private String alarmphone;  //回复电话
    private String accidentcommand; //事故指挥时间
    private String firealarmid; //报警单ID
    private String firealarmno; //报警单号


//    private String content; //内容说明
//    private String releasestatus; //发布状态。1.已发布；2.已撤销
//    private String releaseperson;   //发布人
//    private Date releasetime;   //发布时间
//    private Activityinfo activityinfo;   //园区活动
//    private Fieldmeetinfo fieldmeetinfo;   //场地会议租赁
//    private Businessdocking businessdocking;     //业务对接
//    private List<Photo> photo; //业务对接供求图片

//
//    @Data
//    public static class Activityinfo extends BaseModel {
//        private String _id;
//        private Date starttime;   //开始时间
//        private Date endtime;   //结束时间
//        private String address;   //地址
//        private String cost;     //费用
//        private String contacts;  //联系人
//        private String contacttel;  //联系电话
//        private String contactemail;   //邮箱
//        private Boolean issignup;      //允许报名。true：允许；false不允许（默认）
//        private List<Signupinfo> signupinfo; //报名信息
//        private String activityStatus; // 活动状态
//        private Integer count; //个数
//
//        @Override
//        public void preInsert(TokenModel tokenModel) {
//            super.preInsert(tokenModel);
//            if (signupinfo != null && signupinfo.size() > 0) {
//                for (Signupinfo tmp : this.signupinfo) {
//                    tmp.preInsert(tokenModel);
//                }
//            }
//        }
//
//        @Override
//        public void preUpdate(TokenModel tokenModel) {
//            super.preUpdate(tokenModel);
//            if (signupinfo != null && signupinfo.size() > 0) {
//                for (Signupinfo tmp : this.signupinfo) {
//                    tmp.preUpdate(tokenModel);
//                }
//            }
//        }
//
//    }
//
//    @Data
//    public static class Signupinfo extends BaseModel {
//        private String _id;
//        private String companyname;   //公司名称
//        private String name;          //姓名
//        private String phonenumber;   //手机号
//    }
//
//    @Data
//    public static class Fieldmeetinfo extends BaseModel {
//        private String _id;
//        private String address; //地址
//        private String cost;  //费用
//        private String area;   //面积
//        private String number;   //容纳人数
//        private String contacts;  //联系人
//        private String contacttel;  //联系电话
//        private String contactemail;   //邮箱
//    }
//
//    @Data
//    public static class Businessdocking extends BaseModel {
//        private String _id;
//        private String industry;   //行业
//        private String transactionplace;   //交易地点
//        private String companyname;   //公司名称
//        private String contacts;  //联系人
//        private String contacttel;  //联系电话
//        private String contactemail;   //邮箱
//        private List<Signupinfo> signupinfo; //报名信息
//
//        @Override
//        public void preInsert(TokenModel tokenModel) {
//            super.preInsert(tokenModel);
//            if (signupinfo != null && signupinfo.size() > 0) {
//                for (Signupinfo tmp : this.signupinfo) {
//                    tmp.preInsert(tokenModel);
//                }
//            }
//        }
//
//        @Override
//        public void preUpdate(TokenModel tokenModel) {
//            super.preUpdate(tokenModel);
//            if (signupinfo != null && signupinfo.size() > 0) {
//                for (Signupinfo tmp : this.signupinfo) {
//                    tmp.preUpdate(tokenModel);
//                }
//            }
//        }
//    }
//
//    @Data
//    public static class Photo extends BaseModel {
//        private String photospath; //图片
//    }


}
