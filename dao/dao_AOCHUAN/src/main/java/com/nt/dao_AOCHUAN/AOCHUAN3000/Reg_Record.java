package com.nt.dao_AOCHUAN.AOCHUAN3000;

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
@Table(name = "reg_record")
public class Reg_Record extends BaseModel {

    //主键
    @Id
    @Column(name = "RECORD_ID")
    private String record_id;

    //注册id
    @Column(name = "REG_ID")
    private String reg_id;

    //变更日
    @Column(name = "CHANGE_DATE")
    private Date change_date;

    //变更内容
    @Column(name = "CHANGE_CONTENT")
    private String change_content;
}
