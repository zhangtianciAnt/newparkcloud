package com.nt.dao_Auth.model;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UserInfo")
public class UserInfo extends BaseModel {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Column(name = "No")
    private String no;

    @Column(name = "Name")
    private String name;

}
