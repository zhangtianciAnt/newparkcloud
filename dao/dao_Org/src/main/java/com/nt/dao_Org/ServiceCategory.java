package com.nt.dao_Org;

import com.nt.utils.dao.BaseModel;
import com.nt.utils.dao.TokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: ServiceCategory
 * @Description: 服务类目
 * @Author: sunxu
 * @CreateDate: 2018/12/14
 * @UpdateUser: sunxu
 * @UpdateDate: 2018/12/14
 * @UpdateRemark:
 * @Version: 1.0
 */

@Document(collection = "servicecategory")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceCategory extends BaseModel {

    /**
     * 数据主键ID
     */
    private String _id;
    private String servicename;  //根节点名称   固定：维修内容分类
    private String type;
    private List<ServiceCategory>categorys; //类目项


    @Override
    public void preInsert(TokenModel tokenModel){
        super.preInsert(tokenModel);
        if (categorys != null && categorys.size() > 0) {
            for (ServiceCategory tmp : this.categorys) {
                tmp.preInsert(tokenModel);
            }
        }
    }

    @Override
    public void preUpdate(TokenModel tokenModel){
        super.preUpdate(tokenModel);
        if (categorys != null && categorys.size() > 0) {
            for (ServiceCategory tmp : this.categorys) {
                tmp.preUpdate(tokenModel);
            }
        }
    }


}
