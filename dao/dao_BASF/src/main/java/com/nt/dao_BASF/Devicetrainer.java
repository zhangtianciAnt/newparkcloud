package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "devicetrainer")
public class Devicetrainer extends BaseModel {

    @Id
    private String devicetrainerid;

    /**
     * 培训项目主键
     */
    private String programid;

    /**
     * 用户组主键
     */
    private String usergroupid;

    /**
     * 装置名称
     */
    private String devicename;

    /**
     *培训装置负责人
     */
    private String createby;


}
