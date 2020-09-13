package com.nt.dao_BASF;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.OrderBy;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pimsalarmdetail {
    @Id
    private String id;
    private String pimsalarmid;
    private String alarm;
    @OrderBy("DESC")
    private Date createon;
}
