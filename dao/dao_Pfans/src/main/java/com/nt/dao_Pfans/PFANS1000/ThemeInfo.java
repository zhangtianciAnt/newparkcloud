package com.nt.dao_Pfans.PFANS1000;

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
@Table(name = "themeinfo")
public class ThemeInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "THEMEINFO_ID")
    private String themeinfo_id;

    @Column(name = "THEMENAME")
    private String themename;

}
