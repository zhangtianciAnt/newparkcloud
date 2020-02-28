package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Businessplan;

//import com.nt.dao_Pfans.PFANS1000.Businessplandet;
import com.nt.dao_Pfans.PFANS1000.Pieceworktotal;
import com.nt.dao_Pfans.PFANS1000.Totalplan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessplanVo {
    /**
     * 事业计划
     */
    private Businessplan businessplan;

    /**
     * 人員計画合計
     */
    private List<Totalplan> totalplan;

    /**
     * 人件費計画合計
     */
    private List<Pieceworktotal> pieceworktotal;

    /**
     * 事业计划详细
     */
//    private  List<Businessplandet> businessplandets;

    /**
     * 現時点人員
     */

    /**
     * 新卒・キャリア
     */

    /**
     * 人件費計画合計
     */

    /**
     * 設備投資
     */

    /**
     * 償却合計
     */

    /**
     * 新事业年度
     */

    /**
     * 上一事业年度
     */

    /**
     * 其他
     */

    /**
     * ソフト資
     */

    /**
     * 償却合計
     */

    /**
     * 新事业年度
     */

    /**
     * 上一事业年度
     */

    /**
     * 其他
     */
}
