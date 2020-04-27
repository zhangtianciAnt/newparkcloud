package com.nt.service_BASF.Impl;

import com.nt.dao_BASF.Commandrecord;
import com.nt.dao_BASF.Fireaccidentrecord;
import com.nt.dao_BASF.Firealarm;
import com.nt.dao_Org.Dictionary;
import com.nt.service_BASF.FireaccidentrecordServices;
import com.nt.service_BASF.mapper.FireaccidentrecordMapper;
import com.nt.service_Org.DictionaryService;
import com.nt.utils.ExcelOutPutUtil;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import com.nt.utils.jxlsUtil.JxlsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: BASF10203ServicesImpl
 * @Author: SUN
 * @Description: BASF消防事故记录模块实现类
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FireaccidentrecordServicesImpl implements FireaccidentrecordServices {

    private static Logger log = LoggerFactory.getLogger(FireaccidentrecordServicesImpl.class);

    @Autowired
    private FireaccidentrecordMapper fireaccidentrecordMapper;

    @Autowired
    private DictionaryService dictionaryService;

    /**
     * @param fireaccidentrecord
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<Fireaccidentrecord>
     * @Date 2019/11/4
     */
    @Override
    public List<Fireaccidentrecord> list(Fireaccidentrecord fireaccidentrecord) throws Exception {
        return fireaccidentrecordMapper.select(fireaccidentrecord);
    }

    @Override
    public Fireaccidentrecord selectById(String fireaccidentrecordid) throws Exception {
        return fireaccidentrecordMapper.selectByPrimaryKey(fireaccidentrecordid);
    }

    @Override
    public void insert(TokenModel tokenModel, Fireaccidentrecord fireaccidentrecord) throws Exception {
        fireaccidentrecord.preInsert(tokenModel);
        String id = UUID.randomUUID().toString();
        fireaccidentrecord.setFireaccidentrecordid(id);

        String no = "FAT" + String.format("%09d", fireaccidentrecordMapper.selectCount(new Fireaccidentrecord()));
        fireaccidentrecord.setFireaccidentno(no);
        fireaccidentrecordMapper.insert(fireaccidentrecord);
    }

    @Override
    public void update(TokenModel tokenModel, Fireaccidentrecord fireaccidentrecord) throws Exception {
        fireaccidentrecord.preUpdate(tokenModel);
        fireaccidentrecordMapper.updateByPrimaryKeySelective(fireaccidentrecord);
    }

    @Override
    public void excelexport(Fireaccidentrecord fireaccidentrecord, Firealarm firealarm, Commandrecord commandrecord, HttpServletResponse response) throws Exception {
        Map<String, Object> data = new HashMap<>();
        //报警等级字典转换
        if (firealarm.getAlarmlev() != null) {
            for (Dictionary dictionary : dictionaryService.getForSelect("BC015")) {
                if (dictionary.getCode().equals(firealarm.getAlarmlev())) {
                    firealarm.setAlarmlev(dictionary.getValue1());
                }
            }
        }
        //事故类型字典转换
        if (firealarm.getTypacc() != null) {
            for (Dictionary dictionary : dictionaryService.getForSelect("BC013")) {
                if (dictionary.getCode().equals(firealarm.getTypacc())) {
                    firealarm.setTypacc(dictionary.getValue1());
                }
            }
        }
        //应急预案字典转换
        if (firealarm.getEmplan() != null) {
            for (Dictionary dictionary : dictionaryService.getForSelect("BC014")) {
                if (dictionary.getCode().equals(firealarm.getEmplan())) {
                    firealarm.setEmplan(dictionary.getValue1());
                }
            }
        }
        data.put("firealarm", firealarm);
        data.put("fireaccidentrecord", fireaccidentrecord);
        data.put("commandrecord", commandrecord);
        List<Commandrecord.EmergencyDisposal> emergencyDisposal = commandrecord.getEmergencyDisposal();
        List<Commandrecord.AccidentCommand> accidentCommands = commandrecord.getAccidentCommand();
        List<Commandrecord.Command> command = commandrecord.getCommand();
        data.put("emergencyDisposal", emergencyDisposal);
        data.put("accidentCommands", accidentCommands);
        data.put("command", command);
        data.put("casualties", commandrecord.getCasualties().getNumber());
        String imgname = null;
        if (commandrecord.getGisimage() != null&& StringUtils.isNotEmpty(commandrecord.getGisimage())) {
            byte[] a = DatatypeConverter.parseBase64Binary(commandrecord.getGisimage().split(",")[1]);
            String b = commandrecord.getGisimage().split(",")[1];
            imgname = BASE64CodeToBeImage(b, JxlsConfig.getImageRoot(), "png");
            data.put("gisimage", imgname);
        }
        ExcelOutPutUtil.OutPut(fireaccidentrecord.getFireaccidentno(), "fireaccidentrecord.xlsx", data, response);
        deleteImage(JxlsConfig.getImageRoot() + "/" + imgname);
    }

    public String BASE64CodeToBeImage(String BASE64str, String path, String ext) throws Exception {
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        //文件名称
        String uploadFileName = UUID.randomUUID().toString() + "." + ext;
        File targetFile = new File(path, uploadFileName);
        try (OutputStream out = new FileOutputStream(targetFile)) {
            byte[] b = DatatypeConverter.parseBase64Binary(BASE64str);
            for (int i = 0; i < b.length; i++) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
//            return path + "/" + uploadFileName + "." + ext;
            return uploadFileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteImage(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
        }
    }
}
