package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "serverinfo")
public class ServerInfo extends BaseModel {
    /**
     * 总厂/分厂名称
     */
    private String factoryname;

    /**
     * 事件类型：目前火警
     */
    private String eventtype;

    /**
     * 回路号/寄存器地址
     */
    private String devline;

    /**
     * 设备地址/寄存器位
     */
    private String devrow;

    /**
     * 设备地址
     */
    private String devnum;

    /**
     * 设备名称
     */
    private String eventname;



}
