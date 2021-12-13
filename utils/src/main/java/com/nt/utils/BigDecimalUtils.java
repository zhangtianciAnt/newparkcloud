package com.nt.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {
    public static BigDecimal ifNullSet0_Big(BigDecimal in) {
        if (in != null) {
            return in;
        }
        return BigDecimal.ZERO;
    }
    public static String sum(String ...in){
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i < in.length; i++){
            result = result.add(ifNullSet0(in[i]));
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    private static BigDecimal ifNullSet0(String s) {
        if(s != null){
            return new BigDecimal(s);
        }else{
            return BigDecimal.ZERO;
        }
    }
}
