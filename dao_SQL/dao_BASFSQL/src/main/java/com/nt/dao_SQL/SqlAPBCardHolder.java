package com.nt.dao_SQL;

import lombok.Data;

@Data
public class SqlAPBCardHolder {
    private String departmentname;
    private String departmentid;
    private String departmentpeid;
    private String personno;
    private String cardid;
    private String username;
    private String apbid;
    private String apbname;
    private String lastapb;
    private String lastapbname;
    private String intime;
    private String lastintime;
    private String rdrdvcid;
    private String lastrdrdvcid;

    private String usertype;

    private int cnt;

    private int outcnt;

    private int apbflg;
}
