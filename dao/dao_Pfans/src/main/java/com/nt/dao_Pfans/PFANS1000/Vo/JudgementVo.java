package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Judgement;
import com.nt.dao_Pfans.PFANS1000.Unusedevice;
import com.nt.dao_Pfans.PFANS1000.Judgementdetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgementVo {

    /**
     * 決裁願
     */
    private Judgement judgement;

    /**
     * 设备名
     */
    private List<Unusedevice> unusedevice;

    /**
     * 多部门
     */
    private List<Judgementdetail> Judgementdetail;

}
