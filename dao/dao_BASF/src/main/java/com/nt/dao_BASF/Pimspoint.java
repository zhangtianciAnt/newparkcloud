package com.nt.dao_BASF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pimspoint")
public class Pimspoint {
    private String id;
    /**
     * 所属装置
     */
    private String equipment;
    /**
     * pims点位名称
     */
    private String pimspointname;
    /**
     * 取值范围
     */
    private String ranges;
    private String unit;
    private String lowlow;
    private String low;
    private String high;
    private String highhigh;
    private String status;
    private String mark;
    private String type;
}
