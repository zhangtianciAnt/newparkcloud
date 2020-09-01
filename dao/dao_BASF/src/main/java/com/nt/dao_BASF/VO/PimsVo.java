package com.nt.dao_BASF.VO;

import com.nt.dao_BASF.Deviceinformation;
import com.nt.dao_BASF.Pimsdata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname PimsVo
 * @Description TODO 类的描述
 * @Date 2020/9/1 22:08
 * @Author skaixx
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PimsVo {

    private Pimsdata pimsdata;

    private Deviceinformation deviceinformation;
}
