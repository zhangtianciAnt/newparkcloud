package com.nt.dao_BASF.VO;

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
@Table(name = "fireaccidentrecord")
public class FireaccidentrecordVo extends BaseModel {
    @Id
    private String fireaccidentrecordid;

    /**
     * 事故编号
     */
    private String fireaccidentno;

    /**
     * 消防报警单ID
     */
    private String firealarmid;
    /**
     * 消防报警单单号
     */
    private String firealarmno;
    /**
     * 消防接警单id
     */
    private String commandrecordid;
    /**
     * 消防接警单单号
     */
    private String commandrecordno;
    /**
     * 事故时间
     */
    private Date accidenttime;

    /**
     * 装置名称
     */
    private String indevice;
    /**
     * 事发单位
     */
    private String accunit;

    /**
     * 事故类型
     */
    private String typacc;

    /**
     * 事故完成时间
     */
    private Date firealarmcompletiontime;

}
