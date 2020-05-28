package com.nt.dao_AOCHUAN.AOCHUAN3000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN3000.FollowUpRecord;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Projects;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Reg_Record;
import com.nt.dao_AOCHUAN.AOCHUAN3000.Registration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAndRegRecord {

    /**
     * 注册表
     */
    private Registration registration;

    /**
     * 记录表
     */
    private List<Reg_Record> reg_records;
}
