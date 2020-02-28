package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: Startprogram
 * @Author: Newtouch
 * @Description: 申请考核表
 * @Date: 2020/1/7 10:21
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "startprogram")
public class Startprogram extends BaseModel {

    //主键
    @Id
    private String startprogramid;

    //培训清单主键
    private String programlistid;

    //培训名称
    private String programname;

    //通知开班人数
    private String informpeople;

    //理论开班时间
    private Date starttheorydate;

    //理论地点
    private String theorysite;

    //实操开班时间
    private Date startoperationdate;

    //实操地点
    private String operationsite;

    //报名截止日期
    private Date expirationdate;

    //培训材料上交截止日期
    private Date materialsexpirationdate;

    //通知人员
    private String informperson;

    //code分类
    private String programcode;

    //负责人
    private String programhard;

    //内部/外部
    private String insideoutside;

    //强制/非强制
    private String mandatory;

    //线上/线下
    private String isonline;

    //培训时长
    private Double thelength;

    //培训有效期（月）
    private Integer validity;

    //培训费用
    private String money;

    //初训/复训
    private String isfirst;

    //报名资料
    private String applydata;

    //报名资料url
    private String applydataurl;

    //合格判定标准（%）
    private Integer standard;

    //出题数量
    private Integer questionnum;

    //开班状态
    private String programtype;

    //实际开班日期（即：确认开班的时间）
    private java.sql.Date actualstartdate;
}
