package com.nt.dao_Pfans.PFANS8000;

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
@Table(name = "information")
public class InformationDelivery extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "INFORMATIONID")
    private String informationid;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "FILETYPE")
    private String filetype;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "AVAILABLESTATE")
    private String availablestate;

    @Column(name = "RICHTEXT")
    private String richtext;

    /**
     * 附件
     */
    @Column(name = "UPLOADFILE")
    private String uploadfile;

}
