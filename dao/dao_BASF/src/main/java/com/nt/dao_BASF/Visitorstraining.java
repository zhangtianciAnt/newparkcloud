package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ProjectName: BASF应急平台
 * @Package: com.nt.dao_BASF
 * @ClassName: Visitorstraining
 * @Author: 王哲
 * @Description: 访客培训记录
 * @Date: 2019/11/25 10:58
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "visitorstraining")
public class Visitorstraining extends BaseModel {
    /**
     * 访客培训记录主键
     */
    @Id
    private String visitorsid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 证件号
     */
    private String idcard;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 被访人姓名
     */
    private String arevisitorsname;

    /**
     * 培训时间
     */
    private Date visitorstime;

}
