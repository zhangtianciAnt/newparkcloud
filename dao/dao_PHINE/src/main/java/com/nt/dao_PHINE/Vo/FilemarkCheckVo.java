package com.nt.dao_PHINE.Vo;

import com.nt.dao_PHINE.Filemark2file;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: FilemarkCheckVo
 * @Description: 文件标记Check用Vo
 * @Author: SKAIXX
 * @CreateDate: 2020/2/11
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilemarkCheckVo {

    /**
     * 标记文件数量
     */
    private int cnt;

    /**
     * 标记文件ID数组
     */
    private List<Filemark2file> filemark2fileList;
}
