package Vo;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * @ProjectName: newparkcloud
 * @Package: com.nt.dao_Org
 * @ClassName: PersonalCenter
 * @Description: 基本信息
 * @Author: FEIJIALIANG
 * @CreateDate: 2018/12/05
 * @Version: 1.0
 */
@Document(collection = "personalCenter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCenter extends BaseModel{
    /**
     * 用户姓名
     */
    private String CUSTOMERNAME;
    /**
     * 用户性别
     */
    private  String SEX;
    /**
     * 办公电话
     */
    private String TEL;
    private String Userid;

}
