package com.nt.dao_PHINE.Vo;

import com.nt.dao_PHINE.Filemark2file;

import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: FilemarkVo
 * @Description: 文件标记Vo
 * @Author: SKAIXX
 * @CreateDate: 2020/2/11
 * @Version: 1.0
 */
public class FilemarkVo {
    private String id;

    private String projectid;

    private String versiondescribtion;

    private String version;

    private String fileid;

    private String remarks;

    private List<Filemark2file> filemark2fileList;
}
