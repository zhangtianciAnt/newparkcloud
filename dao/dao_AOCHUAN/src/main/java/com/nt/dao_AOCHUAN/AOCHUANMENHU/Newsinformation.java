package com.nt.dao_AOCHUAN.AOCHUANMENHU;

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
@Table(name = "menhunewsinformation")

public class Newsinformation extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "NEWSINFORMATION_ID")
    private String newsinformation_id;

    @Column(name = "NEWSDAY")
    private String newsday;


    @Column(name = "NEWSYEARMON")
    private String newsyearmon;


    @Column(name = "PICTURE")
    private String picture;


    @Column(name = "NEWSTITLE")
    private String newstitle;

    @Column(name = "NEWSCONTENT")
    private String newscontent;





}
