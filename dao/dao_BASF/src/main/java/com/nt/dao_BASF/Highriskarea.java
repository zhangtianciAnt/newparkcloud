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
@Table(name = "highriskarea")
public class Highriskarea extends BaseModel {
    @Id
    private String highriskareaid;

    /**
     * 高风险区域名称
     */
    private String highriskareaname;

    private String equipment;

    private String highriskareatype;

    private Date starttime;
    private Date endtime;
    private String person;

    private String remark;

    /**
     * 详细位置
     */
    private String detailedlocation;

    /**
     * mapid
     */
    private String mapid;

    /**
     * 高风险作业内容
     */
    private String highriskareacontent;

    /**
     * 坐标
     */
    private String highriskareadetailedlist;

    private String type;

}