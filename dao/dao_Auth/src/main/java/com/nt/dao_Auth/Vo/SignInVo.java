package com.nt.dao_Auth.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInVo {
    private String no;
    private String name;
    private String classname;
    private String theme;
    private Date start;
    private String zuzhi;
    private String place;
    private String time;
    private String people;
    private String signpeople;
    private Date signdate;
}
