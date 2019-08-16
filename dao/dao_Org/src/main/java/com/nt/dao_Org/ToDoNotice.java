package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "todonotice")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoNotice extends BaseModel {
    /**
     * 数据主键ID
     */
    private String _id;

    /**
     * 类型 1.审批流程代办；2.通知
     */
    private String type;

    /**
     * 代办信息
     */
    private List<ToDoInfos> toDoInfos;

    /**
     * 通知信息
     */
    private List<Notices> notices;

    @Data
    public static class ToDoInfos extends BaseModel {

        /**
         * 数据主键ID
         */
        private String _id;

        /**
         * 标题
         */
        private String title;

        /**
         * 公司名称
         */
        private String companyname;

        /**
         * 发起人
         */
        private String initiator;

        /**
         * 当前节点名
         */
        private String currentnodename;

        /**
         * 发起时间
         */
        private Date launchtime;

        /**
         * 到达时间
         */
        private Date arrivaltime;

    }

    @Data
    public static class Notices extends BaseModel {

        /**
         * 数据主键ID
         */
        private String _id;


        /**
         * 标题
         */
        private String title;

        /**
         * 内容
         */
        private String content;
    }

    // region method

    /**
     * @方法名：preInsert
     * @描述：数据插入前，基础字段数据更新
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[tokenModel]
     * @返回值：void
     */
    @Override
    public void preInsert(TokenModel tokenModel) {
        super.preInsert(tokenModel);
        if (toDoInfos != null && toDoInfos.size() > 0) {
            for (ToDoInfos tmp : this.toDoInfos) {
                tmp.preInsert(tokenModel);
            }
        }

        if (notices != null && notices.size() > 0) {
            for (Notices tmp : this.notices) {
                tmp.preInsert(tokenModel);
            }
        }
    }

    /**
     * @方法名：preUpdate
     * @描述：数据更新前，基础字段数据更新
     * @创建日期：2018/10/25
     * @作者：SKAIXX
     * @参数：[tokenModel]
     * @返回值：void
     */
    @Override
    public void preUpdate(TokenModel tokenModel) {
        super.preUpdate(tokenModel);
        if (toDoInfos != null && toDoInfos.size() > 0) {
            for (ToDoInfos tmp : this.toDoInfos) {
                tmp.preUpdate(tokenModel);
            }
        }

        if (notices != null && notices.size() > 0) {
            for (Notices tmp : this.notices) {
                tmp.preUpdate(tokenModel);
            }
        }
    }
    // endregion
}
