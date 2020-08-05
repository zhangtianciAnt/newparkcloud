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
 * @ClassName: chemicalsds
 * @Author: Y
 * @Description: chemicalsds
 * @Date: 2019/11/18 17:22
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chemicalsds")
public class Chemicalsds extends BaseModel {
    @Id
    private String chemicalsdsid;

    /**
     *化学品SDS编号
     */
    private String chemicalsdsnum;

    /**
     *化学品SDS名称
     */
    private String chemicalsdsname;

    /**
     *关键词
     */
    private String keyword;

    /**
     *编制单位
     */
    private String compilationunit;

    /**
     *文件名称
     */
    private String filename;

    /**
     *创建时间
     */
    private Date creationtime;

    /*
     * 备注
     */
    private String remarks;

    /*
     * 化学品SDS级别
     */
    private String planlevel;

    /*
     * 化学品SDS类型
     */
    private String plantype;

    /*
     * 附件
     */
    private String uploadfile;

    /*
     * 展示
     */
    private String downloadpath;

}
