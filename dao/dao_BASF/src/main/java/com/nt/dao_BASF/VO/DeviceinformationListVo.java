package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceinformationListVo extends BaseModel {

    //设备信息List
    private List<Deviceinformation> deviceinformationlist;

    //符合条件的总条数
    private String total;



}
