package com.nt.dao_AOCHUAN.AOCHUAN7000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_account")
public class Account {

    @Column(name = "FAccountID")
    private String faccountid;

    @Column(name = "FNumber")
    private String fnumber;

    @Column(name = "FName")
    private String fname;

    @Column(name = "FLevel")
    private String flevel;

    @Column(name = "FDetail")
    private String fdetail;

    @Column(name = "FParentID")
    private String FParentID;





}
