package com.nt.dao_Pfans.PFANS2000;


import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leaveoffice  extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    private String leaveoffice_id;

    private String user_id;
    private String center_id;
    private String group_id;
    private String team_id;
    private String sex_id;
    private Date hiredate_date;
    private Date departure_date;
    private String reason;
    private String address;
    private String fixedtelephone;
    private String mobilephone;
    private String email;


}

