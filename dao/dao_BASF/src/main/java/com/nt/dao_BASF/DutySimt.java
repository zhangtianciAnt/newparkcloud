package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: DutySimt
 * @Author: WXZ
 * @Description: DutySimt
 * @Date: 2019/12/19 14:46
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "dutysimt")
public class DutySimt extends BaseModel {

    @Id
    private String dutysimtid;

    /**
     *星期一
     */
    private String mon;

    /**
     *星期二
     */
    private String tue;

    /**
     *星期三
     */
    private String web;

    /**
     *星期四
     */
    private String thu;

    /**
     *星期五
     */
    private String fri;

    /**
     *星期六
     */
    private String sat;

    /**
     *星期日
     */
    private String sun;

    /**
     *星期一（备勤）
     */
    private String monbackup;

    /**
     *星期二（备勤）
     */
    private String tuebackup;

    /**
     *星期三（备勤）
     */
    private String webbackup;

    /**
     *星期四（备勤）
     */
    private String thubackup;

    /**
     *星期五（备勤）
     */
    private String firbackup;

    /**
     *星期六（备勤）
     */
    private String satbackup;

    /**
     *星期日（备勤）
     */
    private String sunbackup;

}
