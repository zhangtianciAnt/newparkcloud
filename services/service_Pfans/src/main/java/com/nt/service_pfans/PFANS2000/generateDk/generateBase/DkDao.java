package com.nt.service_pfans.PFANS2000.generateDk.generateBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DkDao {

    //出
    public Date start;

    //进
    public Date end;

    //外出时长
    public Double length;
}
