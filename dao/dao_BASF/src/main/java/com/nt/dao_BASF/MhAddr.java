package com.nt.dao_BASF;

import lombok.Data;

@Data
public class MhAddr {
    //省。如：黑龙江省
    private String Province;
    //市。如：哈尔滨市
    private String City;
    //地区或县。如：南岗区
    private String District;
    //地址详情。如：黑龙江省牡丹江市阳明区G301东新村西196米东新小区东南436米
    private String Detail;

}
