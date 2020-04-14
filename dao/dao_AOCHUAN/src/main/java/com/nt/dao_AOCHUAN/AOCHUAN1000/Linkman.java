package com.nt.dao_AOCHUAN.AOCHUAN1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "linkman")
public class Linkman extends BaseModel {
    @Id
    private String linkman_id;

    private String baseinfor_id;

    private String name;

    private String sex;

    private String fixedtelephone;

    private String mobilephone;

    private String position;

    private String email;
}
