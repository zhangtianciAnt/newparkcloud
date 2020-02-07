package com.nt.dao_PHINE.Vo;

import com.nt.dao_PHINE.Fileinfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoVo {
    private String projectId;
    private List<Fileinfo> filesInfo;
}
