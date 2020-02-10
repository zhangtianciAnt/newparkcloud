package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: ProjectListVo
 * @Description: 平台项目列表Vo
 * @Author: SKAIXX
 * @CreateDate: 2020/1/30
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListVo {

    /**
     * 项目id
     */
    private String id;

    /**
     * 项目名称
     */
    private String projectname;

    /**
     * 项目成员数量
     */
    private int usercnt;

    /**
     * 项目设备数量
     */
    private int devicecnt;

    /**
     * 异常设备数量
     */
    private int errorcnt;

    /**
     * 创建者
     */
    private String createby;

    /**
     * 项目ID
     */
    private String projectid;

    /**
     * 芯片ID
     */
    private String chipid;

    /**
     * 芯片ID
     */
    private String companyid;

    /**
     * 项目描述
     */
    private String projectdescription;
}
