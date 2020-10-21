package com.nt.utils;

import lombok.Getter;

@Getter
public enum  KDFormIdEnum {

    MATERIAL("BD_MATERIAL"),//物料
    SUPPLIER("BD_Supplier"),//供应商
    CUSTOMER("BD_Customer"),//客户
    VOUCHER("GL_VOUCHER");//凭证

    private String formid;

    KDFormIdEnum(String formid) {
        this.formid = formid;
    }

}
