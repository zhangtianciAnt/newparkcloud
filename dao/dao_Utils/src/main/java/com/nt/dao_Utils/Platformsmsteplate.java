package com.nt.dao_Utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class Platformsmsteplate extends BaseModel implements Serializable{
    /**
     * ID
     */
    private String _id;

    /**
     * 模板名
     */
    private String templatename;

    /**
     * 模板CODE
     */
    private String templatecode;

    /**
     * 阿里模板CODE
     */
    private String alitemplatecode;
}