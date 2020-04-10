package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceinformationVo extends BaseModel {

    //设备信息
    private Deviceinformation deviceinformation;

    //报警单编号
    private String firealarmuuid;



}
