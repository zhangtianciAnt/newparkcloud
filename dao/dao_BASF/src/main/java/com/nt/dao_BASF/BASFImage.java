package com.nt.dao_BASF;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "image")
public class BASFImage extends BaseModel {
    @Id
    private String imageid;

    /**
     * 用户名称
     */
    private String imageurl;

    /**
     * 电话号码
     */
    private String imagetext;
    /**
     * 邮箱
     */
    private String url;
}