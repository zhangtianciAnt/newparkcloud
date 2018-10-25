package com.nt.dao_Org;

import com.nt.utils.BaseModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.InvocationTargetException;

@Document(collection = "user")
@Data
public class User extends BaseModel {
    private String id;
    private String userName;
    private String passWord;
    private String uppassWord;

}
