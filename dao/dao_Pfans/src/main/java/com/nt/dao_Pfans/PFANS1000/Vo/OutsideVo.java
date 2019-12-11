package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Outside;
import com.nt.dao_Pfans.PFANS1000.Outsidedetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutsideVo {

    private Outside outside;

    private List<Outsidedetail> outsidedetail;
}
