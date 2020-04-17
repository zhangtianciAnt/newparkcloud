package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.sql.Blob;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: SoundRecording
 * @Description: 录音保存
 * @Author: cui
 * @CreateDate: 2020/04/16
 * @Version: 1.0
 */

@Document(collection = "soundrecording")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoundRecording extends BaseModel {
    @Id
    private String luyinID;
    //录音
    @Transient
    private Blob luyin;
    //url
    private String songurl;
}
