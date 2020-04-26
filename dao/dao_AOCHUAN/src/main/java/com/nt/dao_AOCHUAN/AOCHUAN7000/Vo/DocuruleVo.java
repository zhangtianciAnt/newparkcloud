package com.nt.dao_AOCHUAN.AOCHUAN7000.Vo;

import com.nt.dao_AOCHUAN.AOCHUAN7000.Crerule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Docurule;
import com.nt.dao_AOCHUAN.AOCHUAN7000.Helprule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocuruleVo {
    /*
     * 凭证规则
     * */
    private Docurule docurule;

    /*
     * 分录规则*/
    private List<Crerule> crerules;

    /*
     * 辅助项目*/
    private List<Helprule> helprules;

}
