package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.SoundRecording;
import com.nt.service_BASF.SoundRecordingServices;
import com.nt.service_BASF.mapper.SoundRecordingMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(rollbackFor = Exception.class)
public class SoundRecordingServicesImpl implements SoundRecordingServices {
    @Autowired
    private SoundRecordingMapper soundRecordingMapper;
    @Override
    public void insert(SoundRecording soundRecording, TokenModel tokenModel) throws Exception {
        soundRecording.preInsert(tokenModel);
        soundRecording.setLuyinID(UUID.randomUUID().toString());
        soundRecordingMapper.insert(soundRecording);
    }
}
