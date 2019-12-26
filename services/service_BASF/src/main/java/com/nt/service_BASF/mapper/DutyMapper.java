package com.nt.service_BASF.mapper;

import com.nt.dao_BASF.Duty;
import com.nt.utils.MyMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.service_BASF.mapper
 * @ClassName: DutyMapper
 * @Author: 王哲
 * @Description: 值班信息表Mapper
 * @Date: 2019/12/25 10:44
 * @Version: 1.0
 */
@Component(value = "DutyMapper")
public interface DutyMapper extends MyMapper<Duty> {

}
