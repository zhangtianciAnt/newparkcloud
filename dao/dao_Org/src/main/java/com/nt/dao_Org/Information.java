package com.nt.dao_Org;

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

@Document(collection = "information")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Information extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;
    private String occasion;  //场景。1.PC端；2.微信服务号
    private String title;  //标题
    private String type;  //类型。1.新闻；2.政策；3.党建；4.场地租赁；5.会议室租赁；6.公开课；7.定制活动；8.个性空间；9.行政办公；10.创业孵化；11.供应信息；12；求购信息
    private String imagespath; //封面
    private String content; //内容说明
    private String releasestatus; //发布状态。1.已发布；2.已撤销
    private String releaseperson;   //发布人
    private Date releasetime;   //发布时间
    private Activityinfo activityinfo;   //园区活动
    private Fieldmeetinfo fieldmeetinfo;   //场地会议租赁
    private Businessdocking businessdocking;     //业务对接


    @Data
    public static class Activityinfo extends BaseModel {
        private String _id;
        private Date starttime;   //开始时间
        private Date endtime;   //结束时间
        private String address;   //地址
        private String cost;     //费用
        private String contacts;  //联系人
        private String contacttel;  //联系电话
        private String contactemail;   //邮箱
        private Boolean issignup;      //允许报名。true：允许；false不允许（默认）
        private List<Signupinfo>signupinfo; //报名信息

        @Override
        public void preInsert(TokenModel tokenModel){
            super.preInsert(tokenModel);
            if (signupinfo != null && signupinfo.size() > 0) {
                for (Signupinfo tmp : this.signupinfo) {
                    tmp.preInsert(tokenModel);
                }
            }
        }

        @Override
        public void preUpdate(TokenModel tokenModel){
            super.preUpdate(tokenModel);
            if (signupinfo != null && signupinfo.size() > 0) {
                for (Signupinfo tmp : this.signupinfo) {
                    tmp.preUpdate(tokenModel);
                }
            }
        }


    }

    @Data
    public static class Signupinfo extends BaseModel {
        private String _id;
        private String companyname;   //公司名称
        private String name;          //姓名
        private String phonenumber;   //手机号
    }

    @Data
    public static class Fieldmeetinfo extends BaseModel {
        private String _id;
        private String address; //地址
        private String cost;  //费用
        private String area;   //面积
        private String number;   //容纳人数
        private String contacts;  //联系人
        private String contacttel;  //联系电话
        private String contactemail;   //邮箱
    }

    @Data
    public static class Businessdocking extends BaseModel {
        private String _id;
        private String industry;   //行业
        private String transactionplace;   //交易地点
        private String companyname;   //公司名称
        private String contacts;  //联系人
        private String contacttel;  //联系电话
        private String contactemail;   //邮箱
    }

    // region method
    /**
     * @方法名：preInsert
     * @描述：数据插入前，基础字段数据更新
     * @创建日期：2018/11/06
     * @作者：sunxu
     * @参数：[tokenModel]
     * @返回值：void
     */
//    @Override
//    public void preInsert(TokenModel tokenModel){
//        super.preInsert(tokenModel);
//        if (activityinfo != null && activityinfo.size() > 0) {
//            for (Activityinfo tmp : this.activityinfo) {
//                tmp.preInsert(tokenModel);
//            }
//        }
//
//        if (fieldmeetinfo != null && fieldmeetinfo.size() > 0) {
//            for (Fieldmeetinfo tmp : this.fieldmeetinfo) {
//                tmp.preInsert(tokenModel);
//            }
//        }
//
//        if (businessdocking != null && businessdocking.size() > 0) {
//            for (Businessdocking tmp : this.businessdocking) {
//                tmp.preInsert(tokenModel);
//            }
//        }
//    }

    /**
     * @方法名：preUpdate
     * @描述：数据更新前，基础字段数据更新
     * @创建日期：2018/12/06
     * @作者：sunxu
     * @参数：[tokenModel]
     * @返回值：void
     */
//    @Override
//    public void preUpdate(TokenModel tokenModel){
//        super.preUpdate(tokenModel);
//        if (activityinfo != null && activityinfo.size() > 0) {
//            for (Activityinfo tmp : this.activityinfo) {
//                tmp.preUpdate(tokenModel);
//            }
//        }
//
//        if (fieldmeetinfo != null && fieldmeetinfo.size() > 0) {
//            for (Fieldmeetinfo tmp : this.fieldmeetinfo) {
//                tmp.preUpdate(tokenModel);
//            }
//        }
//
//        if (businessdocking != null && businessdocking.size() > 0) {
//            for (Businessdocking tmp : this.businessdocking) {
//                tmp.preUpdate(tokenModel);
//            }
//        }
//    }
    // endregion
}
