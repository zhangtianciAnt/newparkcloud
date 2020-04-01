package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "themeinfor")

public class ThemeInfor extends BaseModel {

    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "THEMEINFOR_ID")
    private String themeinfor_id;


    @Column(name = "THEMENAME")
    private String themename;

}