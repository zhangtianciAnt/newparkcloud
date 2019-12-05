package com.nt.dao_Pfans.PFANS1000;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "holidaydetail")
public class Holidaydetail extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "HOLIDAYDETAIL_ID")
    private String holidaydetailid;

    @Id
    @Column(name = "HOLIDAY_ID")
    private String holidayid;

    @Column(name = "APPLICATION")
    private String application;

    @Column(name = "KIND")
    private String kind;

    @Column(name = "CARDNUMBER")
    private String cardnumber;

    @Column(name = "ATTENDANCEDATE")
    private Date attendancedate;

    @Column(name = "WORKREASONS")
    private String workreasons;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
