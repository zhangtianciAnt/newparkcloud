package com.nt.service_BASF;

import com.nt.dao_BASF.Startprogram;
import com.nt.dao_BASF.VO.PassingRateVo;
import com.nt.dao_BASF.VO.StartprogramVo;
import com.nt.dao_BASF.VO.TrainEducationPerVo;
import com.nt.utils.dao.TokenModel;

import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF
 * @ClassName: StartprogramServices
 * @Author: 王哲
 * @Description: 申请考核接口类
 * @Date: 2020/1/7 11:06
 * @Version: 1.0
 */
public interface StartprogramServices {

    //获取未开班培训列表
    List<Startprogram> nostart() throws Exception;

    //获取one
    Startprogram one(String startprogramid) throws Exception;

    //添加培训列表
    void insert(Startprogram startprogram, TokenModel tokenModel) throws Exception;

    //更新培训列表
    void update(Startprogram startprogram, TokenModel tokenModel) throws Exception;

    //更新培训清单
    void updateprogramlist(String startprogramid, TokenModel tokenModel) throws Exception;

    //查询培训
    List<Startprogram> select(Startprogram startprogram) throws Exception;

    //查询培训增强
    List<StartprogramVo> selectEnhance(Startprogram startprogram) throws Exception;

    //删除培训
    void delete(Startprogram startprogram, TokenModel tokenModel) throws Exception;

    //by人员id查询培训项目
    List<Startprogram> selectbyuserid(String userid,String selecttype) throws Exception;

    //获取强制的通过/未通过
    List<PassingRateVo>getMandatoryInfo() throws Exception;
    //获取非强制的通过/未通过
    List<PassingRateVo>getIsMandatoryInfo() throws Exception;
    //获取培训教育人员详细
    List<TrainEducationPerVo>getTrainEducationPerInfo() throws Exception;

    //获取未来三个月培训信息推送列表
    List<Startprogram> getFutureProgram() throws Exception;
}
