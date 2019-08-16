package com.nt.dao_Auth.model;

import com.nt.utils.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SignInInformation")
public class SignInInformation extends BaseModel<SignInInformation> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "No")
    private String no;

    @Column(name = "Date")
    private Date date;
}
