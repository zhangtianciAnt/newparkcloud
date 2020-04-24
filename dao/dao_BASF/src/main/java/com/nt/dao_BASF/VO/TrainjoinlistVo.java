package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Trainjoinlist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF.VO
 * @ClassName: TrainjoinlistVo
 * @Author: 王哲
 * @Description: 培训参加名单Vo，用于批量向数据库中加入参加人员
 * @Date: 2020/1/8 13:17
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainjoinlistVo {
    /**
     * 培训列表id
     */
    private String startprogramid;

    /**
     * 人员id
     */
    private List<Trainjoinlist> trainjoinlists;

    /**
     * 通过状态个数
     */
    private int count;

    /**
     * 部门id
     */
    private String departmentid;
}
