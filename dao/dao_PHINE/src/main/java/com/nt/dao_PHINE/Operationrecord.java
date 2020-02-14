package com.nt.dao_PHINE;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "operationrecord")
public class Operationrecord extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String operationid;

    /**
     * PLL 配置/ FMC 配置/ FPGA Config/ 寄存器访问
     */
    private String title;

    private String content;

    private String operationresult;

    private String projectid;

    private String remarks;

}
