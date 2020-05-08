package com.nt.dao_BASF;

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
@Table(name = "switchnotifications")
public class Switchnotifications extends BaseModel {
    @Id
    private String switchnotificationsid;

    /**
     * 通知内容
     */
    private String content;





}
