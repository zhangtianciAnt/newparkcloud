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
public class Citation extends BaseModel {
    private static final long serialVersionUID = 1L;

    @Id
    private String citation_id;
    private String staffexitprocedure_id;
    private String content;
    private String user_id;
    private String remarks;

}
