package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Programlist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Timer;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: ProgramlistEnhanceVo
 * @Author: Newtouch
 * @Description: 培训计划清单增强
 * @Date: 2020/3/16 14:13
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramlistEnhanceVo extends Programlist {

    //是否已通知
    private String notification;

    public void setvalue(Programlist programlist) {
        this.setProgramlistid(programlist.getProgramlistid());
        this.setProgramname(programlist.getProgramname());
        this.setProgramcode(programlist.getProgramcode());
        this.setProgramhard(programlist.getProgramhard());
        this.setInsideoutside(programlist.getInsideoutside());
        this.setMandatory(programlist.getMandatory());
        this.setIsonline(programlist.getIsonline());
        this.setThelength(programlist.getThelength());
        this.setValidity(programlist.getValidity());
        this.setMoney(programlist.getMoney());
        this.setRemindtime(programlist.getRemindtime());
        this.setLastdate(programlist.getLastdate());
        this.setThisdate(programlist.getThisdate());
        this.setNumberpeople(programlist.getNumberpeople());
        this.setApplydata(programlist.getApplydata());
        this.setApplydataurl(programlist.getApplydataurl());
        this.setTraindata(programlist.getTraindata());
        this.setTraindataurl(programlist.getTraindataurl());
        this.setStandard(programlist.getStandard());
        this.setQuestionnum(programlist.getQuestionnum());
        this.setProgramtype(programlist.getProgramtype());
    }
}
