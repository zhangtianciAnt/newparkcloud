package com.nt.dao_Pfans.PFANS1000.Vo;

import com.nt.dao_Pfans.PFANS1000.Security;
import com.nt.dao_Pfans.PFANS1000.Securitydetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecurityVo {

    private Security security;

    private List<Securitydetail> securitydetail;
}