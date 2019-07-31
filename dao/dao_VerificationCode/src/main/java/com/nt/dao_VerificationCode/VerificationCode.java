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

    private String _id;
    private String phone;
    private String code;
}
