package com.nt.service_BASF;

import com.nt.dao_BASF.SoundRecording;
import com.nt.utils.dao.TokenModel;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: BASF20201Services
 * @Author: SUN
 * @Description: BASF大屏接警指挥
 * @Date: 2020/04/16 14:42
 * @Version: 1.0
 */
public interface SoundRecordingServices {
    //录音保存
    void insert(SoundRecording soundrecording, TokenModel tokenModel) throws Exception;
}
