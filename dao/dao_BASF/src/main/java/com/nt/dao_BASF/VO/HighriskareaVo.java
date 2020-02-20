package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Highriskarea;
import com.nt.dao_BASF.Highriskareadetailed;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HighriskareaVo extends BaseModel {

    @Id
    private String highriskareaid;
    /**
     * 高风险区域名称
     */
    private String highriskareaname;

    /**
     * 详细位置
     */
    private String detailedlocation;

    /**
     * mapid
     */
    private String mapid;

    /**
     * 高风险作业内容
     */
    private String highriskareacontent;

    private ArrayList highriskareadetailedlist;
}