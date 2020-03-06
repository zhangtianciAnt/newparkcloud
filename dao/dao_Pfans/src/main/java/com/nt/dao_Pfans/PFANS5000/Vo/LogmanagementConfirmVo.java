package com.nt.dao_Pfans.PFANS5000.Vo;

import com.nt.dao_Pfans.PFANS5000.LogManagement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogmanagementConfirmVo {

    /**
     * 项目id、centerid
     */
    private String projectid;

    /**
     * 项目名称、center
     */
    private String projectname;

    /**
     * 项目编号、月份
     */
    private String numbers;

    /**
     * 确认工时
     */
    private String confirm;

    /**
     * 未确认工时
     */
    private String unconfirm;

}
