package com.nt.dao_AOCHUAN.AOCHUAN6000;

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
@Table(name = "secrecy")
public class Secrecy extends BaseModel {
    @Id
    @Column(name = "SECRECY_ID")
    private String secrecy_id;

    @Column(name = "NO")
    private String no;

    @Column(name = "FILETYPE")
    private String filetype;

    @Column(name = "DESCRIBE1")
    private String describe1;

    @Column(name = "CUSTOMER")
    private String customer;

    @Column(name = "RESPONSIBLE")
    private String responsible;

    @Column(name = "UPLOADDATE")
    private Date uploaddate;

    @Column(name = "DUEDATE")
    private Date duedate;

    @Column(name = "UPLOAD")
    private String upload;

    @Column(name = "TYPE")
    private String type;

}
