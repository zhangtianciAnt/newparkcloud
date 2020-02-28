package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "programlist")
public class Programlist extends BaseModel {
    @Id
    private String programlistid;//培训清单主键

    private String programname;//培训名称

    private String programcode;//code分类

    private String programhard;//负责人

    private String insideoutside;//内部/外部

    private String mandatory;//强制/非强制

    private String isonline;//培训形式（线上/线下）

    private Double thelength;//培训时长（即课时）

    private String validity;//培训有效期（例：3个月）

    private String money;//培训费用

    private Integer remindtime;//到期提醒提前时间（月）

    private Date lastdate;//上次培训日期

    private Date thisdate;//本次培训日期

    private String number;//累计培训次数

    private String thispeople;//本次培训人数

    private String numberpeople;//累计培训人数

    private String applydata;//报名资料

    private String applydataurl;//报名资料url

    private String traindata;//培训资料

    private String traindataurl;//培训资料url

    private Integer standard;//合格判定标准（%）

    private Integer questionnum;//出题数量

    private String programtype;//培训状态
}