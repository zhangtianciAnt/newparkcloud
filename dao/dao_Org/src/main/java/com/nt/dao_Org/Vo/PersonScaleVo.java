package com.nt.dao_Org.Vo;


import com.nt.dao_Org.PersonScale;
import com.nt.dao_Org.PersonScaleMee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonScaleVo {

    PersonScaleMee personScaleMee;

    List<PersonScale> personScaleList;
}
