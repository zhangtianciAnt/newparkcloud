package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pimsalarm extends BaseModel {
    @Id
    private String id;
    private String pimspointid;
    private String alarm;

    public Pimsalarm(String id, String pimspointid, String alarm) {
        this.id = id;
        this.pimspointid = pimspointid;
        this.alarm = alarm;
    }

    @Transient
    private Pimspoint pimspoint;

    @Transient
    private List<Pimsalarmdetail> pimsalarmdetails;
}
