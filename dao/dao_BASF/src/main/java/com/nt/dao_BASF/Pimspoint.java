package com.nt.dao_BASF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pimspoint")
public class Pimspoint {
    @Id
    private String id;
    /**
     * 所属装置
     */
    private String equipment;
    /**
     * pims点位名称
     */
    private String pimspointname;

    private String devicename;
    /**
     * 取值范围
     */
    private String ranges;
    private String unit;
    private String status;
    private String mark;
    private String type;

    @Transient
    private Deviceinformation deviceinformation;

    @Transient
    private Pimsdata pimsdata;
}
