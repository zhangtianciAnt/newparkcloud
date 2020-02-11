package com.nt.dao_PHINE.Vo;

import com.nt.dao_PHINE.Project2user;
import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_PHINE.Vo
 * @ClassName: ProjectinfoVo
 * @Author: lxx
 * @Description:
 * @Date: 2020/2/11 1:53 下午
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectinfoVo extends BaseModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String companyid;

    private String projectid;

    private String chipid;

    private String projectname;

    private Date starttime;

    private Date endtime;

    private String projectdescription;

    private String remarks;

    private List<Project2user> project2userList;

}
