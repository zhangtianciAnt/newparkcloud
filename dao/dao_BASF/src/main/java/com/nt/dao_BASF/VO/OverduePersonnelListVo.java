package com.nt.dao_BASF.VO;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.opencv.tracking.TrackerBoosting;

import java.sql.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: OverduePersonnelListVo
 * @Author: Newtouch
 * @Description: 前端大屏复训/到期人员列表用
 * @Date: 2020/1/21 10:30
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverduePersonnelListVo {

    //    培训参加名单主键
    private String trainjoinlistid;

    //    人员id
    private String personnelid;

    //    姓名
    private String customername;

    //    培训名称
    private String programname;

    //    培训开班日期
    private Date actualstartdate;

    //    培训有效期
    private String validity;

    //    培训到期日
    private Date dueDate;
}
