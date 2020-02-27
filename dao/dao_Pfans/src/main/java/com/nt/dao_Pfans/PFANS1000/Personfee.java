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
@Table(name = "personfee")
public class Personfee extends BaseModel {

	private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "PERSONFEE_ID")
    private String personfeeid;

    @Id
    @Column(name = "QUOTATION_ID")
    private String quotationid;

    @Column(name = "FUNCTIONSPRICE1")
    private String functionsprice1;

    @Column(name = "FUNCTIONHOUR1")
    private Date functionhour1;

    @Column(name = "FUNCTIONUNIT1")
    private Date functionunit1;

    @Column(name = "FUNCTIONAMOUNT1")
    private Date functionamount1;

    @Column(name = "SYSTEMSPRICE1")
    private Date systemsprice1;

    @Column(name = "SYSTEMHOUR1")
    private String systemhour1;

    @Column(name = "SYSTEMUNIT1")
    private Date systemunit1;

    @Column(name = "SYSTEMAMOUNT1")
    private String systemamount1;

    @Column(name = "DESIGNSPRICE1")
    private Date designsprice1;

    @Column(name = "DESIGNHOUR1")
    private String designhour1;

    @Column(name = "DESIGNUNIT1")
    private Date designunit1;

    @Column(name = "DESIGNAMOUNT1")
    private String designamount1;

    @Column(name = "VERSPRICE1")
    private Date versprice1;

    @Column(name = "VERHOUR1")
    private String verhour1;

    @Column(name = "VERUNIT1")
    private Date verunit1;

    @Column(name = "VERAMOUNT1")
    private String veramount1;

    @Column(name = "IMPLESPRICE1")
    private Date implesprice1;

    @Column(name = "IMPLEHOUR1")
    private String implehour1;

    @Column(name = "IMPLEUNIT1")
    private Date impleunit1;

    @Column(name = "IMPLEAMOUNT1")
    private String impleamount1;

    @Column(name = "DEBUGSPRICE1")
    private Date debugsprice1;

    @Column(name = "DEBUGHOUR1")
    private String debughour1;

    @Column(name = "DEBUGUNIT1")
    private Date debugunit1;

    @Column(name = "DEBUGAMOUNT1")
    private String debugamount1;

    @Column(name = "FUNCTIONSPRICE2")
    private Date functionsprice2;

    @Column(name = "FUNCTIONHOUR2")
    private String functionhour2;

    @Column(name = "FUNCTIONUNIT2")
    private Date functionunit2;

    @Column(name = "FUNCTIONAMOUNT2")
    private String functionamount2;

    @Column(name = "SYSTEMSPRICE2")
    private Date systemsprice2;

    @Column(name = "SYSTEMHOUR2")
    private String systemhour2;

    @Column(name = "SYSTEMUNIT2")
    private Date systemunit2;

    @Column(name = "SYSTEMAMOUNT2")
    private String systemamount2;

    @Column(name = "DESIGNSPRICE2")
    private Date designsprice2;

    @Column(name = "DESIGNHOUR2")
    private String designhour2;

    @Column(name = "DESIGNUNIT2")
    private Date designunit2;

    @Column(name = "DESIGNAMOUNT2")
    private String designamount2;

    @Column(name = "VERSPRICE2")
    private Date versprice2;

    @Column(name = "VERHOUR2")
    private String verhour2;

    @Column(name = "VERUNIT2")
    private Date verunit2;

    @Column(name = "VERAMOUNT2")
    private String veramount2;

    @Column(name = "IMPLESPRICE2")
    private Date implesprice2;

    @Column(name = "IMPLEHOUR2")
    private String implehour2;

    @Column(name = "IMPLEUNIT2")
    private Date impleunit2;

    @Column(name = "IMPLEAMOUNT2")
    private String impleamount2;

    @Column(name = "DEBUGSPRICE2")
    private Date debugsprice2;

    @Column(name = "DEBUGHOUR2")
    private String debughour2;

    @Column(name = "DEBUGUNIT2")
    private Date debugunit2;

    @Column(name = "DEBUGAMOUNT2")
    private String debugamount2;

    @Column(name = "FUNCTIONSPRICE3")
    private Date functionsprice3;

    @Column(name = "FUNCTIONHOUR3")
    private String functionhour3;

    @Column(name = "FUNCTIONUNIT3")
    private Date functionunit3;

    @Column(name = "FUNCTIONAMOUNT3")
    private String functionamount3;

    @Column(name = "SYSTEMSPRICE3")
    private Date systemsprice3;

    @Column(name = "SYSTEMHOUR3")
    private String systemhour3;

    @Column(name = "SYSTEMUNIT3")
    private Date systemunit3;

    @Column(name = "SYSTEMAMOUNT3")
    private String systemamount3;

    @Column(name = "DESIGNSPRICE3")
    private Date designsprice3;

    @Column(name = "DESIGNHOUR3")
    private String designhour3;

    @Column(name = "DESIGNUNIT3")
    private Date designunit3;

    @Column(name = "DESIGNAMOUNT3")
    private String designamount3;

    @Column(name = "VERSPRICE3")
    private Date versprice3;

    @Column(name = "VERHOUR3")
    private String verhour3;

    @Column(name = "VERUNIT3")
    private Date verunit3;

    @Column(name = "VERAMOUNT3")
    private String veramount3;

    @Column(name = "IMPLESPRICE3")
    private Date implesprice3;

    @Column(name = "IMPLEHOUR3")
    private String implehour3;

    @Column(name = "IMPLEUNIT3")
    private Date impleunit3;

    @Column(name = "IMPLEAMOUNT3")
    private String impleamount3;

    @Column(name = "DEBUGSPRICE3")
    private Date debugsprice3;

    @Column(name = "DEBUGHOUR3")
    private String debughour3;

    @Column(name = "DEBUGUNIT3")
    private Date debugunit3;

    @Column(name = "DEBUGAMOUNT3")
    private String debugamount3;

    @Column(name = "ROWINDEX")
    private Integer rowindex;

}
