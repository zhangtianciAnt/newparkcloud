package com.nt.utils;

public class CowBUtils {
    /**
     * 用于判断一个字符串中，是否包含多个字符串中的任意一个（例如：aabbccddeeffgg中是否包含aa,bb,cc其中任意一个，如果包含则返回true，否则false）
     *
     * @param inputString 进行判断的字符串
     * @param words       是否包含的字符串数组
     * @return true 包含，false 不包含
     */
    public static boolean wordsIndexOf(String inputString, String[] words) {
        boolean result = false;
        for (String w : words) {
            if (inputString.indexOf(w) > -1) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * 用于判断一个字符串，是否与数组字符串中的任意一个相等
     *
     * @param inputString 进行判断的字符串
     * @param words       字符串数组
     * @return true 有相同，false 没有相同
     */
    public static boolean wordsEqualOf(String inputString, String[] words) {
        boolean result = false;
        for (String w : words) {
            if (inputString.equals(w)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
