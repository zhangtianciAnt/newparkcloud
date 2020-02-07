package com.nt.dao_PHINE.Vo;

import com.nt.dao_PHINE.Operationdetail;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: OperationRecordVo
 * @Author: lxx
 * @Description:
 * @Date: 2020/2/5 12:26 下午
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationRecordVo extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * PLL 配置/ FMC 配置/ FPGA Config/ 寄存器访问 /文件上传
     */
    private String title;

    private String content;

    private String operationresult;

    private String projectid;

    private String remarks;

    private List<Operationdetail> detailist;

}
