/**
 * 短信实体
 */

package com.nt.dao_VerificationCode;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "verificationCode")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerificationCode extends BaseModel {

    private String _id;     //主键
    private String phone;   // 手机号
    private String code;    // 验证码
}
