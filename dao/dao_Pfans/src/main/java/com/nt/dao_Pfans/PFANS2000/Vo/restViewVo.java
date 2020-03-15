package com.nt.dao_Pfans.PFANS2000.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class restViewVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userid;

    private String applicationdate;

    private String restdays;
}
