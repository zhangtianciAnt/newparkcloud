package com.nt.dao_PHINE.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: FpgaDataVo
 * @Description: java类作用描述
 * @Author: SKAIXX
 * @CreateDate: 2020/2/26
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FpgaDataVo {

    private int uid;

    private String fileid;

    private String projectid;

    private String filetype;

    private String devicekey;

    private String deviceid;

    private String fpgaid;

    private String filename;

    private String url;
}
