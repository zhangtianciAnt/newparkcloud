package com.nt.service_BASF.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_BASF.Programlist;
import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.VO.ProgramlistEnhanceVo;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.UserAccount;
import com.nt.service_BASF.ProgramlistServices;
import com.nt.service_BASF.mapper.ProgramlistMapper;
import com.nt.service_BASF.mapper.StartprogramMapper;
import com.nt.service_Org.mapper.DictionaryMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.StringUtils;
import com.nt.utils.dao.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.Impl
 * @ClassName: BASF21207ServicesImpl
 * @Author:
 * @Description: BASF培训清单模块Controller
 * @Date: 2019/11/4 16:30
 * @Version: 1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProgramlistServicesImpl implements ProgramlistServices {

    private static Logger log = LoggerFactory.getLogger(ProgramlistServicesImpl.class);

    @Autowired
    private ProgramlistMapper programlistMapper;

    @Autowired
    private StartprogramMapper startprogramMapper;

    @Autowired
    private DictionaryMapper dictionaryMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * @param programlist
     * @Method list
     * @Author SUN
     * @Version 1.0
     * @Description
     * @Return java.util.List<Programlist>
     * @Date 2019/11/4
     */
    @Override
    public List<Programlist> list(Programlist programlist) throws Exception {
        return programlistMapper.select(programlist);
    }

    //获取培训计划清单增强
    @Override
    public List<ProgramlistEnhanceVo> listEnhance() throws Exception {
        //这个增强查询，用于查询出该培训计划清单的培训是否已经发送过通知
        List<Programlist> programlists = programlistMapper.select(new Programlist());
        List<ProgramlistEnhanceVo> programlistEnhanceVos = new ArrayList<>();
        for (Programlist programlist : programlists) {
            ProgramlistEnhanceVo programlistEnhanceVo = new ProgramlistEnhanceVo();
            programlistEnhanceVo.setvalue(programlist);
            programlistEnhanceVos.add(programlistEnhanceVo);
        }
        for (ProgramlistEnhanceVo programlistEnhanceVo : programlistEnhanceVos) {
            Startprogram startprogram = new Startprogram();
            startprogram.setProgramlistid(programlistEnhanceVo.getProgramlistid());
            startprogram.setProgramtype("BC039001");//BC039001 可开班
            if (startprogramMapper.selectCount(startprogram) > 0) {
                programlistEnhanceVo.setNotification("已通知");
            } else {
                programlistEnhanceVo.setNotification("未通知");
            }
            startprogram.setProgramtype("BC039002");//BC039002 进行中
            if (startprogramMapper.selectCount(startprogram) > 0) {
                programlistEnhanceVo.setNotification("已通知");
            }

        }
        return programlistEnhanceVos;
    }

    /**
     * @param programlistid
     * @Method one
     * @Author
     * @Version 1.0
     * @Description 获取培训计划清单详情
     * @Return com.nt.dao_BASF.programlist
     * @Date 2019/11/25 14:16
     */
    @Override
    public Programlist one(String programlistid) throws Exception {
        return programlistMapper.selectByPrimaryKey(programlistid);
    }

    /**
     * @param programlist
     * @param tokenModel
     * @Method insert
     * @Author
     * @Version 1.0
     * @Description 创建培训清单
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public String insert(Programlist programlist, TokenModel tokenModel) throws Exception {
        programlist.preInsert(tokenModel);
        programlist.setProgramtype("BC039001");
        String uuid = UUID.randomUUID().toString();
        programlist.setProgramlistid(uuid);
        programlistMapper.insert(programlist);
        return uuid;
    }

    /**
     * @param programlist
     * @param tokenModel
     * @Method update
     * @Author
     * @Version 1.0
     * @Description 更新培训清单
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void update(Programlist programlist, TokenModel tokenModel) throws Exception {
        programlist.preUpdate(tokenModel);
        programlistMapper.updateByPrimaryKeySelective(programlist);
    }

    /**
     * @param programlist
     * @Method Delete
     * @Author
     * @Version 1.0
     * @Description 删除培训清单
     * @Return void
     * @Date 2019/11/25 14:16
     */
    @Override
    public void delete(Programlist programlist) throws Exception {
        programlistMapper.updateByPrimaryKeySelective(programlist);
    }

    /**
     * @param request
     * @param tokenModel
     * @Method insert
     * @Author
     * @Version 1.0
     * @Description execl导入
     * @Return void
     * @Date 2019/12/04
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> insert(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<Programlist> listVo = new ArrayList<Programlist>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("培训名称");
            model.add("Code分类");
            model.add("负责人工号");
            model.add("内部/外部");
            model.add("强制/非强制");
            model.add("线上/线下");
            model.add("培训时长");
            model.add("培训有效期");
            model.add("培训费用");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }

            int accesscount = 0;
            int error = 0;

            for (int i = 1; i < list.size(); i++) {
                Programlist programlist = new Programlist();
                List<Object> value = list.get(i);
                //培训名称
                programlist.setProgramname(value.get(0).toString());
                //code分类
                programlist.setProgramcode(code("BC038", value.get(1).toString()));

                List<Programlist> pl = programlistMapper.select(programlist);

                Query query = new Query();
                query.addCriteria(Criteria.where("userno").is(value.get(2).toString()));
                List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                if (userAccountlist.size() > 0) {
                    String _id = userAccountlist.get(0).get_id();
                    //负责人工号
                    programlist.setProgramhard(_id);
                }
                //内部/外部
                programlist.setInsideoutside(code("BC041", value.get(3).toString()));
                //强制/非强制
                programlist.setMandatory(code("BC033", value.get(4).toString()));
                //培训形式（线上/线下）
                programlist.setIsonline(code("BC032", value.get(5).toString()));
                //培训时长（即课时）
                programlist.setThelength(Double.parseDouble(value.get(6).toString()));
                //培训有效期（例：3个月）
                programlist.setValidity(value.get(7).toString());
                //培训费用
                programlist.setMoney(value.get(8).toString());
                //累计培训次数
                programlist.setNumber("0");
                //本次培训人数
                programlist.setThispeople("0");
                //累计培训人数
                programlist.setNumberpeople("0");
                //培训状态
                programlist.setProgramtype("BC039001");
                programlist.preInsert(tokenModel);
                programlist.setProgramlistid(UUID.randomUUID().toString());
                programlist.setApplydata("");
                programlist.setApplydataurl("");
                programlist.setTraindata("");
                programlist.setTraindataurl("");
                if (pl.size() > 0) {
                    programlist.setProgramlistid(pl.get(0).getProgramlistid());
                    programlistMapper.updateByPrimaryKeySelective(programlist);
                } else {
                    programlistMapper.insert(programlist);
                }

                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }

    public String code(String pcode, String name) {
        String code = "";
        Dictionary dictionary = new Dictionary();
        dictionary.setPcode(pcode);
        dictionary.setValue1(name);
        List<Dictionary> listDictionary = dictionaryMapper.select(dictionary);
        code = listDictionary.get(0).getCode();
        return code;
    }

    //根据培训清单模板id获取培训资料(在线培训用)
    @Override
    public Map<String, List<String>> videoOrPdfFile(String programlistid) throws Exception {
        try {
            Map<String, List<String>> filePath = new HashMap<String, List<String>>();
            Programlist programlist = new Programlist();
            programlist.setProgramlistid(programlistid);
            programlist = programlistMapper.selectOne(programlist);
            String url = programlist.getTraindataurl();
            //存放视频路径
            List<String> video = new ArrayList<>();
            //存放pdf路径
            List<String> pdf = new ArrayList<>();
            if (StringUtils.isNotEmpty(url)) {
                //名字和路径
                String[] nameAndPath = url.substring(0, url.length() - 1).split(";");
                //判断文件类型
                for (String str : nameAndPath) {
                    if (str.indexOf(".mp4") != -1) {
                        video.add(str.split(",")[1]);
                    } else if (str.indexOf(".pdf") != -1) {
                        pdf.add(str.split(",")[1]);
                    }
                }
                filePath.put("video", video);
                filePath.put("pdf", pdf);
                return filePath;
            } else
                return filePath;
        } catch (Exception e) {
            return null;
        }
    }
}
