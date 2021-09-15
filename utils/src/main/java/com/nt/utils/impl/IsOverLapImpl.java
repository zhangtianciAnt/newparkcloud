package com.nt.utils.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.nt.utils.dao.TimePair;

import java.time.DateTimeException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


    /**
     * 判断多个时间段是否有重叠（交集）
     * @return 返回是否重叠 true重叠 false不重叠
     */
public class IsOverLapImpl {
    public static boolean isOverlapResult(TimePair[] timePairs, boolean isStrict){
        if(timePairs==null || timePairs.length==0){
            throw new DateTimeException("timePairs不能为空");
        }
        Arrays.sort(timePairs, Comparator.comparingLong(TimePair::getStart));

        for(int i=1;i<timePairs.length;i++){
            if(isStrict){
                if(! (timePairs[i-1].getEnd()<timePairs[i].getStart())){
                    return true;
                }
            }else{
                if(! (timePairs[i-1].getEnd()<=timePairs[i].getStart())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断多个时间段是否有重叠（交集）
     * @param timePairList 时间段列表
     * @param isStrict 是否严格重叠，true 严格，没有任何相交或相等；false 不严格，可以首尾相等，比如2021-05-29到2021-05-31和2021-05-31到2021-06-01，不重叠。
     * @return 返回是否重叠
     */
    public static boolean isOverlap(List<TimePair> timePairList, boolean isStrict){
        if(CollectionUtil.isEmpty(timePairList)){
            throw new DateTimeException("timePairList不能为空");
        }
        TimePair[] timePairs = new TimePair[timePairList.size()];
        timePairList.toArray(timePairs);
        return isOverlapResult(timePairs, isStrict);
    }
}
