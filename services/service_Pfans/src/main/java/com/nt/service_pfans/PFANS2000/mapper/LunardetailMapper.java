package com.nt.service_pfans.PFANS2000.mapper;

import com.nt.dao_Pfans.PFANS2000.Lunardetail;
import com.nt.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface LunardetailMapper extends MyMapper<Lunardetail>{
    List<Lunardetail> selectWorkrate(@Param("YearM") String YearM);
    List<Lunardetail> selectSign(@Param("Years") String Years, @Param("Types") String Types);
    List<Lunardetail> selectSymbol(@Param("Years") String Years, @Param("Typess") String Typess);
    List<Lunardetail> updateProcesT(@Param("Process") String Process, @Param("Tmid") String Tmid);
    List<Lunardetail> updateProcesG(@Param("Process") String Process, @Param("Gpid") String Gpid);
    List<Lunardetail> updateProcesC(@Param("Process") String Process, @Param("Crid") String Crid);
    List<Lunardetail> updateProcess1(@Param("Process") String Process, @Param("Evaluationday") String Evaluationday, @Param("Subjectmon") String Subjectmon);
    List<Lunardetail> updateProcess2(@Param("Process") String Process, @Param("Evaluationday") String Evaluationday, @Param("Subjectmon") String Subjectmon);
    List<Lunardetail> selectTeam(@Param("lunardetailList") List<String> lunardetailList, @Param("Self") String Self, @Param("Evaluationday") String Evaluationday, @Param("Subjectmon") String Subjectmon);
    List<Lunardetail> selectGroup(@Param("lunardetailList") List<String> lunardetailList, @Param("Self") String Self, @Param("Evaluationday") String Evaluationday, @Param("Subjectmon") String Subjectmon);
    List<Lunardetail> selectCenter(@Param("lunardetailList") List<String> lunardetailList, @Param("Self") String Self, @Param("Evaluationday") String Evaluationday, @Param("Subjectmon") String Subjectmon);
    List<Lunardetail> selectResult(@Param("Evanum") String Evanum, @Param("Evaday") String Evaday, @Param("Submon") String Submon, @Param("Self") String Self);
}
