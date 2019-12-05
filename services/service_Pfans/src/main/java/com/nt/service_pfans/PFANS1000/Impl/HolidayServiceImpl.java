package com.nt.service_pfans.PFANS1000.Impl;

import com.nt.dao_Pfans.PFANS1000.Holiday;
import com.nt.dao_Pfans.PFANS1000.Holidaydetail;
import com.nt.dao_Pfans.PFANS1000.Vo.HolidayVo;
import com.nt.service_pfans.PFANS1000.HolidayService;
import com.nt.service_pfans.PFANS1000.mapper.HolidayMapper;
import com.nt.service_pfans.PFANS1000.mapper.HolidaydetailMapper;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor=Exception.class)
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private HolidayMapper holidayMapper;

    @Autowired
    private HolidaydetailMapper holidaydetailMapper;

    @Override
    public List<Holiday> getHoliday(Holiday holiday)  throws Exception{
        return holidayMapper.select(holiday);
    }

    @Override
    public HolidayVo selectById(String holidayid) throws Exception {
        HolidayVo asseVo = new HolidayVo();
        Holidaydetail holidaydetail = new Holidaydetail();
        holidaydetail.setHolidayid(holidayid);
        List<Holidaydetail> holidaydetaillist = holidaydetailMapper.select(holidaydetail);
        holidaydetaillist = holidaydetaillist.stream().sorted(Comparator.comparing(Holidaydetail::getRowindex)).collect(Collectors.toList());
        Holiday holi = holidayMapper.selectByPrimaryKey(holidayid);
        asseVo.setHoliday(holi);
        asseVo.setHolidaydetail(holidaydetaillist);
        return asseVo;
    }

    @Override
    public void updateHoliday(HolidayVo holidayVo, TokenModel tokenModel) throws Exception {
        Holiday holiday = new Holiday();
        BeanUtils.copyProperties(holidayVo.getHoliday(), holiday);
        holiday.preUpdate(tokenModel);
        holidayMapper.updateByPrimaryKey(holiday);
        String sholidayid = holiday.getHolidayid();
        Holidaydetail tail = new Holidaydetail();
        tail.setHolidayid(sholidayid);
        holidaydetailMapper.delete(tail);
        List<Holidaydetail> holidaydetaillist = holidayVo.getHolidaydetail();
        if (holidaydetaillist != null) {
            int rowindex = 0;
            for (Holidaydetail holidaydetail : holidaydetaillist) {
                rowindex = rowindex + 1;
                holidaydetail.preInsert(tokenModel);
                holidaydetail.setHolidaydetailid(UUID.randomUUID().toString());
                holidaydetail.setHolidayid(sholidayid);
                holidaydetail.setRowindex(rowindex);
                holidaydetailMapper.insertSelective(holidaydetail);
            }
        }
    }
    @Override
    public void insert(HolidayVo holidayVo, TokenModel tokenModel) throws Exception {
        String holidayid = UUID.randomUUID().toString();
        Holiday holiday = new Holiday();
        BeanUtils.copyProperties(holidayVo.getHoliday(), holiday);
        holiday.preInsert(tokenModel);
        holiday.setHolidayid(holidayid);
        holidayMapper.insertSelective(holiday);
        List<Holidaydetail> holidaydetaillist = holidayVo.getHolidaydetail();
        if (holidaydetaillist != null) {
            int rowindex = 0;
            for (Holidaydetail holidaydetail : holidaydetaillist) {
                rowindex = rowindex + 1;
                holidaydetail.preInsert(tokenModel);
                holidaydetail.setHolidaydetailid(UUID.randomUUID().toString());
                holidaydetail.setHolidayid(holidayid);
                holidaydetail.setRowindex(rowindex);
                holidaydetailMapper.insertSelective(holidaydetail);
            }
        }
    }
}
