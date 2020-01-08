package com.nt.dao_Assets.Vo;

import com.nt.utils.dao.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String bartype;

    private int sum;
}
