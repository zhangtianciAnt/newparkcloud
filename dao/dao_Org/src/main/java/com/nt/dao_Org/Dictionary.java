package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "dictionary")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dictionary extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DICTIONARYID")
    private String dictionaryid;

    @Column(name = "CODE")
    private String code;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "TYPE")
    private String type;

}
