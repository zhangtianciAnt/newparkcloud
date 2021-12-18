package com.nt.service_pfans.PFANS4000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.mysql.jdbc.StringUtils;
import com.nt.dao_Org.Dictionary;
import com.nt.dao_Org.Vo.DepartmentVo;
import com.nt.dao_Pfans.PFANS4000.PeoplewareFee;
import com.nt.service_Org.DictionaryService;
import com.nt.service_Org.OrgTreeService;
import com.nt.service_pfans.PFANS4000.mapper.PeoplewareFeeMapper;
import com.nt.service_pfans.PFANS4000.PeoplewareFeeService;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public  class PeoplewareFeeServiceImpl implements PeoplewareFeeService {

    @Autowired
    private PeoplewareFeeMapper peoplewarefeeMapper;

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private OrgTreeService orgTreeService;

    @Override
    public List<PeoplewareFee> getPeopleWareList(PeoplewareFee peoplewareFee) throws Exception {
        List<PeoplewareFee> wareList = peoplewarefeeMapper.select(peoplewareFee);
        return wareList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> importPeopleWare(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        List<String> Result = new ArrayList<String>();
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        File f = null;
        f = File.createTempFile("tmp", null);
        file.transferTo(f);
        ExcelReader reader = ExcelUtil.getReader(f);
        List<Map<String, Object>> readAll = reader.readAll();
        //去重，防止录入两个相同数据
        readAll = readAll.stream().distinct().collect(Collectors.toList());
        boolean resultInsUpd = true;
        if (readAll.size() == 0) {
            throw new LogicalException("表格是否填写数据？请确认。");
        }
        Map<String, Object> key = readAll.get(0);

        if (key.keySet().toString().trim().contains("●")) {
            //只要有一个●就走更新
            resultInsUpd = false;
        }

        int k = 1;
        int accesscount = 0;
        StringBuilder message = new StringBuilder();
        message.delete(0, message.length());
        StringBuilder upd = new StringBuilder();
        upd.delete(0, message.length());
        List<Dictionary> dictionaryRank = dictionaryService.getForSelect("PR021");
        List<DepartmentVo> allDepartment = orgTreeService.getAllDepartment();
        HashMap<String, String> companyid = new HashMap<>();
        List<String> companyens = new ArrayList<>();
        for (DepartmentVo vo : allDepartment) {
            companyens.add(vo.getDepartmentEn());
            companyid.put(vo.getDepartmentEn(), vo.getDepartmentId());
        }
        List<PeoplewareFee> useradd = new ArrayList<>();
        PeoplewareFee peoplewareFee = new PeoplewareFee();
        if (resultInsUpd) {
            for (Map<String, Object> item : readAll) {
                k++;
                peoplewareFee = new PeoplewareFee();
                String uuid = UUID.randomUUID().toString();
                peoplewareFee.setPeoplewareid(uuid);
                PeoplewareFee peoplewareFee1 = new PeoplewareFee();
                if (item.get("部门(简称)") != null && item.get("年度") != null) {
                    peoplewareFee1.setGroupid(companyid.get(item.get("部门(简称)")));
                    peoplewareFee1.setYear(Convert.toStr(item.get("年度")));
                } else if (item.get("部门(简称)") == null) {
                    throw new LogicalException("第" + k + "行 请输入部门(简称)，请确认。");
                } else if (item.get("年度") == null) {
                    throw new LogicalException("第" + k + "行 请输入年度，请确认。");
                }
                //判断填写的部门合法性，为空则为查不出来，赋给groupid
                if (org.springframework.util.StringUtils.isEmpty(peoplewareFee1.getGroupid())) {
                    throw new LogicalException("第" + k + "行 请输入正确的部门(简称)，请确认。");
                }
                List<PeoplewareFee> contans = peoplewarefeeMapper.select(peoplewareFee1);
                if (contans.size() == 0) {
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("员工RANK"))) {
                        List<Dictionary> dictionaryRank1 = dictionaryRank.stream().distinct().filter(items -> (items.getValue1().equals(item.get("员工RANK")))).collect(Collectors.toList());
                        if (dictionaryRank1.size() > 0) {
                            peoplewareFee.setRanks(Convert.toStr(item.get("员工RANK")));
                        } else {
                            throw new LogicalException("第" + k + "行 请输入正确的员工RANK，请确认。");
                        }
                    } else {
                        throw new LogicalException("第" + k + "行 员工RANK 不能为空，请确认。");
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("部门(简称)"))) {
                        List<String> companyens1 = companyens.stream().distinct().filter(items -> (items.equals(item.get("部门(简称)")))).collect(Collectors.toList());
                        if (companyens1.size() > 0) {
                            peoplewareFee.setGroupid(companyid.get(item.get("部门(简称)")));
                        } else {
                            throw new LogicalException("第" + k + "行 请输入正确的部门(简称)，请确认。");
                        }
                    } else {
                        throw new LogicalException("第" + k + "行 部门(简称) 不能为空，请确认。");
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("年度"))) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                        Calendar calendar = Calendar.getInstance();
                        int now_year = 0;
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
//                        if (month >= 1 && month <= 4) {
//                            //时间大于4月10日的，属于新年度，小于10日，属于旧年度
//                            if (day >= 10) {
//                                now_year = calendar.get(Calendar.YEAR);
//                            } else {
//                                now_year = calendar.get(Calendar.YEAR) - 1;
//                            }
//                        } else {
//                            now_year = calendar.get(Calendar.YEAR);
//                        }
                        if(month >= 1 && month < 4) {
                            now_year = calendar.get(Calendar.YEAR) - 1;
                        }
                        else if(month == 4)
                        {
                            //时间大于4月10日的，属于新年度，小于10日，属于旧年度
                            if(day >=10)
                            {
                                now_year = calendar.get(Calendar.YEAR);
                            }
                            else
                            {
                                now_year = calendar.get(Calendar.YEAR) - 1;
                            }
                        }
                        else
                        {
                            now_year = calendar.get(Calendar.YEAR);
                        }
                        int year = Integer.valueOf(Convert.toStr(item.get("年度")));
                        if (year >= now_year) {
                            peoplewareFee.setYear(Convert.toStr(item.get("年度")));
                        } else {
                            throw new LogicalException("第" + k + "行 只能导入当前年度或之后年度，请确认。");
                        }
                    } else {
                        throw new LogicalException("第" + k + "行 年度 不能为空，请确认。");
                    }
                    List<PeoplewareFee> repeatList = useradd.stream().filter(repeat -> (repeat.getGroupid().equals(Convert.toStr(companyid.get(item.get("部门(简称)")))) && repeat.getRanks().equals(Convert.toStr(item.get("员工RANK")))
                            && repeat.getYear().equals(Convert.toStr(item.get("年度"))))).collect(Collectors.toList());
                    if(repeatList.size() > 0){
                        throw new LogicalException("文件中" + item.get("年度")+"年度 " + item.get("部门(简称)") +"部门 " + item.get("员工RANK") + "存在重复数据，请确认。");
                    }
                } else {
                    if (message.toString().contains(Convert.toStr(item.get("年度")))) {
                        message.insert(message.lastIndexOf(Convert.toStr(item.get("年度"))) + 6, Convert.toStr(item.get("部门(简称)")) + ",");
                    } else {
                        message.append(Convert.toStr(item.get("年度")) + "年度" + Convert.toStr(item.get("部门(简称)")) + ",");
                    }
                    continue;
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("1月"))) {
                    peoplewareFee.setMonth1(Convert.toStr(item.get("1月")));
                } else {
                    peoplewareFee.setMonth1("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("2月"))) {
                    peoplewareFee.setMonth2(Convert.toStr(item.get("2月")));
                } else {
                    peoplewareFee.setMonth2("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("3月"))) {
                    peoplewareFee.setMonth3(Convert.toStr(item.get("3月")));
                } else {
                    peoplewareFee.setMonth3("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("4月"))) {
                    peoplewareFee.setMonth4(Convert.toStr(item.get("4月")));
                } else {
                    peoplewareFee.setMonth4("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("5月"))) {
                    peoplewareFee.setMonth5(Convert.toStr(item.get("5月")));
                } else {
                    peoplewareFee.setMonth5("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("6月"))) {
                    peoplewareFee.setMonth6(Convert.toStr(item.get("6月")));
                } else {
                    peoplewareFee.setMonth6("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("7月"))) {
                    peoplewareFee.setMonth7(Convert.toStr(item.get("7月")));
                } else {
                    peoplewareFee.setMonth7("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("8月"))) {
                    peoplewareFee.setMonth8(Convert.toStr(item.get("8月")));
                } else {
                    peoplewareFee.setMonth8("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("9月"))) {
                    peoplewareFee.setMonth9(Convert.toStr(item.get("9月")));
                } else {
                    peoplewareFee.setMonth9("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("10月"))) {
                    peoplewareFee.setMonth10(Convert.toStr(item.get("10月")));
                } else {
                    peoplewareFee.setMonth10("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("11月"))) {
                    peoplewareFee.setMonth11(Convert.toStr(item.get("11月")));
                } else {
                    peoplewareFee.setMonth11("0");
                }
                if (!org.springframework.util.StringUtils.isEmpty(item.get("12月"))) {
                    peoplewareFee.setMonth12(Convert.toStr(item.get("12月")));
                } else {
                    peoplewareFee.setMonth12("0");
                }
                peoplewareFee.preInsert(tokenModel);
                accesscount = accesscount + 1;
                if (!StringUtils.isNullOrEmpty(peoplewareFee.getGroupid())) {
                    useradd.add(peoplewareFee);
                }
            }
            message.append("部门的人件费已存在，已自动跳过!");
            if (useradd.size() > 0) {
                //rank平均值 ztc fr
                useradd.forEach(item ->{
                    item.setAgeprice(item.getSum());
                });
                //rank平均值 ztc to
                List<PeoplewareFee> all = new ArrayList<>(useradd);
                Map<String, Map<String,List<PeoplewareFee>>> collect = useradd.stream().collect(Collectors.groupingBy(PeoplewareFee::getYear,Collectors.groupingBy(PeoplewareFee::getGroupid)));
                collect.forEach((years,vale) -> {
                    vale.forEach((group, value) -> {
                        List<String> ranks = new ArrayList<>();
                        Set<String> yearscount = new HashSet<>();//防止同一部门填写两个rank，yearscount重复添加
                        for (PeoplewareFee peo : value) {
                            ranks.add(peo.getRanks());
                        }
                        List<Dictionary> collects = dictionaryRank.stream().filter(items -> (!ranks.contains(items.getValue1()))).collect(Collectors.toList());
                        PeoplewareFee peomore = null;
                            for (Dictionary dic : collects) {
                                String uuid = UUID.randomUUID().toString();
                                peomore = new PeoplewareFee(uuid, group, years, dic.getValue1(), "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0");
                                peomore.preInsert(tokenModel);
                                all.add(peomore);
                            }
                    });
                });

                peoplewarefeeMapper.insertList(all);
            }
        } else {
            for (Map<String, Object> item : readAll) {
                k++;
                peoplewareFee = new PeoplewareFee();
                if (!org.springframework.util.StringUtils.isEmpty(item.get("员工RANK")) && !org.springframework.util.StringUtils.isEmpty(item.get("年度"))
                        && !org.springframework.util.StringUtils.isEmpty(item.get("部门(简称)"))) {
                    peoplewareFee.setRanks(Convert.toStr(item.get("员工RANK")));
                    peoplewareFee.setYear(Convert.toStr(item.get("年度")));
                    peoplewareFee.setGroupid(companyid.get(item.get("部门(简称)")));
                    //判断填写的部门合法性，为空则为查不出来，赋给groupid
                    if (org.springframework.util.StringUtils.isEmpty(peoplewareFee.getGroupid())) {
                        throw new LogicalException("第" + k + "行 请输入正确的部门(简称)，请确认。");
                    }
                } else {
                    throw new LogicalException("第" + k + "行 请输入部门，员工rank和年度，请确认。");
                }
                List<PeoplewareFee> feeList = peoplewarefeeMapper.select(peoplewareFee);
                if (feeList.size() > 0) {
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("1月●"))) {
                        feeList.get(0).setMonth1(Convert.toStr(item.get("1月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("2月●"))) {
                        feeList.get(0).setMonth2(Convert.toStr(item.get("2月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("3月●"))) {
                        feeList.get(0).setMonth3(Convert.toStr(item.get("3月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("4月●"))) {
                        feeList.get(0).setMonth4(Convert.toStr(item.get("4月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("5月●"))) {
                        feeList.get(0).setMonth5(Convert.toStr(item.get("5月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("6月●"))) {
                        feeList.get(0).setMonth6(Convert.toStr(item.get("6月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("7月●"))) {
                        feeList.get(0).setMonth7(Convert.toStr(item.get("7月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("8月●"))) {
                        feeList.get(0).setMonth8(Convert.toStr(item.get("8月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("9月●"))) {
                        feeList.get(0).setMonth9(Convert.toStr(item.get("9月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("10月●"))) {
                        feeList.get(0).setMonth10(Convert.toStr(item.get("10月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("11月●"))) {
                        feeList.get(0).setMonth11(Convert.toStr(item.get("11月●")));
                    }
                    if (!org.springframework.util.StringUtils.isEmpty(item.get("12月●"))) {
                        feeList.get(0).setMonth12(Convert.toStr(item.get("12月●")));
                    }
                    feeList.get(0).preInsert(tokenModel);
                    accesscount = accesscount + 1;
                    useradd.add(feeList.get(0));
                } else {
                    if (upd.toString().contains(Convert.toStr(item.get("年度")))) {
                        upd.insert(upd.lastIndexOf(Convert.toStr(item.get("年度"))) + 6, Convert.toStr(item.get("部门(简称)")) + ",");
                    } else {
                        upd.append(Convert.toStr(item.get("年度")) + "年度" + Convert.toStr(item.get("部门(简称)")) + ",");
                    }
                    continue;
                }
            }
            upd.append("部门数据不存在，需要导入，不可更新，请确认。");
            if (useradd.size() > 0) {
                //rank平均值 ztc fr
                useradd.forEach(item ->{
                    item.setAgeprice(item.getSum());
                });
                //rank平均值 ztc to
                peoplewarefeeMapper.updateFeeList(useradd);
            } else {
                throw new LogicalException("页面数据为新数据，需要导入，不可更新，请确认。");
            }
        }
        if (message.toString().length() > 16) {
            Result.add("提示：" + message);
        }
        if (upd.toString().length() > 22) {
            Result.add("提示：" + upd);
        }
        Result.add("成功数：" + accesscount);
//        Result.add("成功数人数id" + useradd);
        return Result;

    }
}
