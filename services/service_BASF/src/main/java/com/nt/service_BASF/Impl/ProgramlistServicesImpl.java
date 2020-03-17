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
        //存放消息
        List<String> result = new ArrayList<>();
        //临时文件
        File f = null;
        //插入成功数
        int successCount = 0;
        //插入失败数
        int errorCount = 0;
        //excel集合
        List<List<Object>> list;
        //当前执行行数
        int k = 1;
        try {
            //转换成文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取并存储为临时文件时发生未知异常！");
            return result;
        }
        try {
            //读取excel临时文件
            ExcelReader reader = ExcelUtil.getReader(f);
            //读取到的excel集合
            list = reader.read();
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在读取Excel临时文件时发生未知异常！");
            return result;
        }
        try {
            //判断是否是空文件
            if (list == null) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("空文件，请上传正确的培训清单导入模板文件");
                return result;
            }
            //判断是否是正确格式的成绩文件(方案一：检查行数是否足够)
            if (list.size() < 2) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("培训清单导入模板文件格式不正确！");
                return result;
            }
            //判断是否是正确格式的培训清单导入模板文件（方案二：检查表头是否正确）
            if (list.get(0).size() < 12) {
                errorCount += 1;
                result.add("失败数：" + errorCount);
                result.add("成绩文件表头格式不正确！");
                return result;
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("成绩文件格式不正确！");
            return result;
        }
        try {
            List<Object> model = new ArrayList<Object>();
            model.add("培训名称*");
            model.add("Code分类*");
            model.add("负责人员工号*");
            model.add("内部/外部*");
            model.add("强制/非强制*");
            model.add("线上/线下*");
            model.add("合格判定标准（%）(线上培训用)");
            model.add("出题数量（线上培训用）");
            model.add("培训时长(时)");
            model.add("培训有效期(月)*");
            model.add("培训费用(元)");
            model.add("到期提醒提前时间（月）");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    result.add("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                    errorCount += 1;
                }
            }
            if (errorCount > 0) {
                result.add("失败数：" + errorCount);
                return result;
            }
        } catch (Exception e) {
            errorCount += 1;
            result.add("失败数：" + errorCount);
            result.add("系统在验证列标题时发生未知异常！");
            return result;
        }
        try {
            for (int i = 1; i < list.size(); i++) {
                k += 1;
                Programlist programlist = new Programlist();
                List<Object> value = list.get(i);
                try {
                    //培训名称
                    programlist.setProgramname(value.get(0).toString());
                    Programlist p1 = programlistMapper.selectOne(programlist);
                    if (p1.getProgramlistid() != null) {
                        programlist.setProgramlistid(p1.getProgramlistid());
                    }
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行培训名称异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //code分类
                    programlist.setProgramcode(code("BC038", value.get(1).toString()));
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行Code分类异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    Query query = new Query();
                    query.addCriteria(Criteria.where("jobnumber").is(value.get(2).toString()));
                    List<UserAccount> userAccountlist = mongoTemplate.find(query, UserAccount.class);
                    if (userAccountlist.size() == 1) {
                        String _id = userAccountlist.get(0).get_id();
                        //负责人工号
                        programlist.setProgramhard(_id);
                    } else {
                        result.add("培训清单导入模板" + k + "行负责人员工号对应多人，导入系统失败！");
                        errorCount += 1;
                        continue;
                    }
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行负责人员工号异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //内部/外部
                    programlist.setInsideoutside(code("BC041", value.get(3).toString()));
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行内部/外部异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //强制/非强制
                    programlist.setMandatory(code("BC033", value.get(4).toString()));
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行强制/非强制异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //培训形式（线上/线下）
                    programlist.setIsonline(code("BC032", value.get(5).toString()));
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行线上/线下异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //合格判定标准（%）
                    if (value.get(5).toString().trim().equals("线上")) {
                        programlist.setStandard(Integer.parseInt(value.get(6).toString()));
                    }
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行合格判定标准（%）异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //出题数量
                    if (value.get(5).toString().trim().equals("线上")) {
                        programlist.setQuestionnum(Integer.parseInt(value.get(7).toString()));
                    }
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行出题数量异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //培训时长（时）
                    programlist.setThelength(Double.parseDouble(value.get(8).toString()));
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行培训时长异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //培训有效期
                    programlist.setValidity(value.get(9).toString());
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行培训有效期异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //培训费用
                    programlist.setMoney(value.get(10).toString());
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行培训费用异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                try {
                    //到期提醒提前时间（月）
                    programlist.setRemindtime(Integer.parseInt(value.get(11).toString()));
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行到期提醒提前时间异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
                //累计培训次数
                programlist.setNumber("0");
                //本次培训人数
                programlist.setThispeople("0");
                //累计培训人数
                programlist.setNumberpeople("0");
                //培训状态
                programlist.setProgramtype("BC039001");
                programlist.setApplydata("");
                programlist.setApplydataurl("");
                programlist.setTraindata("");
                programlist.setTraindataurl("");
                try {
                    if (StringUtils.isNotEmpty(programlist.getProgramlistid())) {
                        programlist.preUpdate(tokenModel);
                        programlistMapper.updateByPrimaryKeySelective(programlist);
                    } else {
                        programlist.preInsert(tokenModel);
                        programlist.setProgramlistid(UUID.randomUUID().toString());
                        programlistMapper.insert(programlist);
                    }
                    successCount += 1;
                } catch (Exception e) {
                    result.add("培训清单导入模板" + k + "行存储到数据库异常，导入系统失败！");
                    errorCount += 1;
                    continue;
                }
            }
            result.add("失败数：" + errorCount);
            result.add("成功数：" + successCount);
            return result;
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
