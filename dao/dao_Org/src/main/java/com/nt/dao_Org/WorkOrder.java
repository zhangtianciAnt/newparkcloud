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
 * @ClassName: WorkOrder
 * @Description: 信息
 * @Author: sunxu
 * @CreateDate: 2018/12/14
 * @UpdateUser: sunxu
 * @UpdateDate: 2018/12/14
 * @UpdateRemark:
 * @Version: 1.0
 */

@Document(collection = "workorder")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrder extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;//工单ID
    private String workorderno;  //工单编号
    private String source;  //来源  1.PC后台；2.微信服务号
    private String subject;  //主题
    private String address; //地点
    private String contacts; //联系人
    private String phonenumber; //联系电话
    private String responseperson;   //受理人
    private Date responsetime;   //响应时间
    private String doperson;   //处理人
    private Date dotime;   //处理时间
    private Date requiredtime;   //预约时间
    private String servicecategory;   //服务类目
    private String workorderstatus;   //工单状态  1.新建；2.派单；3.接单；4.开工；5.完成；6.结束
    private String auditstatus;   //审核状态   1.通过；2.驳回
    private List<Replymsg> replymsg; //回复消息


    @Override
    public void preInsert(TokenModel tokenModel){
        super.preInsert(tokenModel);
        if (replymsg != null && replymsg.size() > 0) {
            for (Replymsg tmp : this.replymsg) {
                tmp.preInsert(tokenModel);
            }
        }
    }

    @Override
    public void preUpdate(TokenModel tokenModel){
        super.preUpdate(tokenModel);
        if (replymsg != null && replymsg.size() > 0) {
            for (Replymsg tmp : this.replymsg) {
                tmp.preUpdate(tokenModel);
            }
        }
    }


    @Data
    public static class Replymsg extends BaseModel {
        private String _id;
        private String msg;   //消息内容   支持文字及照片
        private String msgtype;   //消息类型   1.客户方；2.处理方
        private String photo;          //头像
        private String sendperson;   //发送者  处理方记录工号或固定值；客户方表示姓名
    }

}
