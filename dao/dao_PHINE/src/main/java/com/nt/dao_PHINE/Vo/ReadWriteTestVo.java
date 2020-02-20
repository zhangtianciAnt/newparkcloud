package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: ReadWriteTestVo
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/20
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadWriteTestVo {
    /**
     * 设备编号
     */
    private String deviceid;

    /**
     * 测试结果
     */
    private String result;
}
