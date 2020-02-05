package com.nt.dao_PHINE;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cabinetinfo")
public class Cabinetinfo extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String cabinetid;

    private String machineroomname;

    private String machineroomid;

    private String remarks;
}
