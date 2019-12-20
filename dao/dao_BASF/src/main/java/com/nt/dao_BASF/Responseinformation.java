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
 * @ClassName: Responseinformation
 * @Author: 王哲
 * @Description: 应急预案响应信息表
 * @Date: 2019/12/19 15:46
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "responseinformation")
public class Responseinformation extends BaseModel {

    /**
     * 数据主键ID
     */
    @Id
    private String responseinformationid;

    /*
     * 装置CODE值
     */
    private String device;

    /*
     * 负责人
     */
    private String principal;

    /*
     * 专家组
     */
    private String panel;

    /*
     * 工艺处置队
     */
    private String fabricationprocessing;

}
