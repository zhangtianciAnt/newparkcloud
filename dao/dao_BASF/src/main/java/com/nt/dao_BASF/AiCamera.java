package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.Data;

import javax.persistence.Id;

@Data
public class AiCamera extends BaseModel {
    @Id
    private String id;
    private String deviceinfomationid;
    private String alarm;
}
