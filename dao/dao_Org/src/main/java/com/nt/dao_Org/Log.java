package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "log")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log extends BaseModel {
    // region properties
    /**
     * 数据主键ID
     */
    private String _id;
    /**
     * 分类
     */
    private String type;
    /**
     * 日志数据
     */
    private List<Logs> logs;

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
        if (logs != null && logs.size() > 0) {
            for (Logs tmp : this.logs) {
                tmp.preInsert(tokenModel);
            }
        }
    }

    // region method

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
        if (logs != null && logs.size() > 0) {
            for (Logs tmp : this.logs) {
                tmp.preUpdate(tokenModel);
            }
        }
    }

    @Data
    public static class Logs extends BaseModel {
        /**
         * 数据主键ID
         */
        private String _id;
        /**
         * 登录IP
         */
        private String ip;
        /**
         * 地区
         */
        private String area;
        /**
         * 登录设备
         */
        private String equipment;

    }
    // endregion

}
