package com.nt.service_AOCHUAN.AOCHUAN6000.Impl;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_AOCHUAN.AOCHUAN6000.Attendance;
import com.nt.service_AOCHUAN.AOCHUAN6000.AttendancesService;
import com.nt.service_AOCHUAN.AOCHUAN6000.mapper.AttendancesMapper;
import com.nt.service_Auth.RoleService;
import com.nt.service_Org.ToDoNoticeService;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(rollbackFor=Exception.class)
public class AttendancesServiceImpl implements AttendancesService {

    @Autowired
    private AttendancesMapper attendanceMapper;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ToDoNoticeService toDoNoticeService;


    @Override
    public List<Attendance> get(Attendance attendance) throws Exception {
        return attendanceMapper.select(attendance);
    }

    @Override
    public List<Attendance> getNow(Attendance attendance) throws Exception {
        return attendanceMapper.select(attendance);
    }

    @Override
    public List<Attendance> getNowMons(String attendancetim) throws Exception {
        return attendanceMapper.getNowMon(attendancetim);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importUser(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("tmp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("日期");
            model.add("姓名");
            model.add("部门");
            model.add("上班时间");
            model.add("下班时间");
            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Attendance attendance = new Attendance();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if (value.get(0).toString().equals("")) {
                        continue;
                    }


                    if (value.size() > 3) {
                        String date = value.get(3).toString();
                        String date1 = value.get(3).toString();
                        date = date.substring(5, 7);
                        date1 = date1.substring(8, 10);
                        if (Integer.parseInt(date1) > 31) {
                            error = error + 1;
                            Result.add("模板第" + (k-1) + "行的日期格式错误，请输入正确的日子，导入失败");
                            continue;
                        }
                        if (Integer.parseInt(date) > 12) {
                            error = error + 1;
                            Result.add("模板第" + (k-1) + "行的日期格式错误，请输入正确的月份，导入失败");
                            continue;
                        }
                    }
                    if (value.size() > 6) {
                        if (value.get(6).toString().length() > 50) {
                            error = error + 1;
                            Result.add("模板第" + (k - 1) + "行的工作备注长度超出范围，请输入长度为50位之内的工作备注，导入失败");
                            continue;
                        }
                    }

                    SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    attendance.preInsert(tokenModel);
                    attendance.setAttendancetim(value.get(0).toString());
                    attendance.setNames(value.get(1).toString());
                    attendance.setDepartment(value.get(2).toString());

                    String workinghours = value.get(3).toString();
                    String offhours = value.get(4).toString();

                    Date working = sf1.parse(workinghours);
                    Date off = sf1.parse(offhours);
                    attendance.setWorkinghours(working);
                    attendance.setOffhours(off);

                    Attendance attendance1 = new Attendance();
                    attendance1.setNames(value.get(1).toString());
                    attendanceMapper.delete(attendance1);

                }
                attendance.setAttendance_id(UUID.randomUUID().toString());

                attendanceMapper.insert(attendance);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;
        } catch (Exception e) {
            throw new LogicalException(e.getMessage());
        }
    }
}
