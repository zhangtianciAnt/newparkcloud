package com.nt.dao_Assets;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "serveraddress")
public class ServerAddress extends BaseModel {
    private static final long serialVersionUID = 1L;
    /**
     * 服务器地址ID
     */
    @Id
    @Column(name = "SERVERADDRESS_ID")
    private String serveraddress_ID;

    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;

}
