package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: Riskassessment
 * @Author: 王哲
 * @Description: 风险判研信息表
 * @Date: 2020/02/04 15:46
 * @Version: 1.0
 */

@Document(collection = "riskassessment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Riskassessment extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;

    /**
     * 承诺公告
     */
    private String notice;

    private Synthesize synthesize;

    /**
     * 综合信息
     */
    @Data
    public static class Synthesize {
        /**
         * 装置总数
         */
        private Integer devicenum;

        /**
         * 运行装置数
         */
        private Integer rundevicenum;

        /**
         * 停产装置数
         */
        private Integer stopdevicenum;

        /**
         * 储罐总数
         */
        private Integer vesselnum;

        /**
         * 使用中的储罐数
         */
        private Integer employvesslnum;

        /**
         * 停用中的储罐数
         */
        private Integer stopvesselnum;

        /**
         * 仓库总数
         */
        private Integer warehousenum;

        /**
         * 使用中的仓库数
         */
        private Integer employwarehousenum;

        /**
         * 停用中的仓库数
         */
        private Integer stopwarehousenum;

        /**
         * 特殊动火作业数
         */
        private Integer specialhotworknum;

        /**
         * 一级动火作业数
         */
        private Integer onelevelhotworknum;

        /**
         * 二级动火作业数
         */
        private Integer twolevelhotworknum;

        /**
         * 受限空间作业数
         */
        private Integer confinedspacenum;

        /**
         * 断路作业数
         */
        private Integer roadbreakingnum;

        /**
         * 动土作业数
         */
        private Integer excavationnum;

        /**
         * 盲板抽堵作业数
         */
        private Integer blindplateoperationnum;

        /**
         * 临时用电作业数
         */
        private Integer temporaryelectricitynum;

        /**
         * 高处作业数
         */
        private Integer workatheightnum;

        /**
         * 吊装作业数
         */
        private Integer liftingoperation;
    }
}
