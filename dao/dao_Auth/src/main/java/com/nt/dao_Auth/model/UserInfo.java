package com.nt.dao_Auth.model;

import com.nt.utils.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo extends BaseModel<UserInfo> {
    /**
	 *
	 */
	private static final long serialVersionUID = 1L;

    @Column(name = "No")
    private String no;

    @Column(name = "Name")
    private String name;

}
