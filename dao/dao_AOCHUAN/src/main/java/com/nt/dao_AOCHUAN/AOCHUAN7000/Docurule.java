package com.nt.dao_AOCHUAN.AOCHUAN7000;

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
@Table(name = "docurule")
public class Docurule extends BaseModel {
    @Id
    @Column(name = "DOCURULE_ID")
    private String docurule_id;

    @Column(name = "DOCUTYPE")
    private String docutype;

    @Column(name = "DOCUMENT")
    private String document;

    @Column(name = "BUSINESSDAY")
    private String businessday;

    @Column(name = "NOWDAY")
    private String nowday;

    @Column(name = "ANNEXNO")
    private String annexno;

    @Column(name = "CREATEBY")
    private String createby;

    @Column(name = "CREATEON")
    private String createon;
}
