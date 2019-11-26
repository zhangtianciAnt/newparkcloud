package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Softwaretransfer;
import com.nt.dao_Pfans.PFANS1000.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoftwaretransferVo {
    /**
     * 固定資産ソフトウェア移転届
     */
    private Softwaretransfer softwaretransfer;

    /**
     * 固定資産ソフトウェア移転届明细
     */
    private List<Notification> notification;

}
