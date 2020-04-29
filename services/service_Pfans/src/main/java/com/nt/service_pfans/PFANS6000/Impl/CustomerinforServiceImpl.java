package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.service_pfans.PFANS6000.CustomerinforService;
import com.nt.service_pfans.PFANS6000.mapper.CustomerinforMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerinforServiceImpl implements CustomerinforService {

    @Autowired
    private CustomerinforMapper customerinforMapper;

    @Override
    public List<Customerinfor> getcustomerinfor(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        return customerinforMapper.select(customerinfor);
    }

    @Override
    public Customerinfor getcustomerinforApplyOne(String customerinfor_id) throws Exception {
        return customerinforMapper.selectByPrimaryKey(customerinfor_id);
    }

    @Override
    public void updatecustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        customerinforMapper.updateByPrimaryKeySelective(customerinfor);
    }

    @Override
    public void createcustomerinforApply(Customerinfor customerinfor, TokenModel tokenModel) throws Exception {
        customerinfor.preInsert(tokenModel);
        customerinfor.setCustomerinfor_id(UUID.randomUUID().toString());
        customerinforMapper.insert(customerinfor);
    }


    //导入导出
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> eximport(HttpServletRequest request, TokenModel tokenModel) throws Exception {

//            创建listVo集合方便存储导入信息
            List<Customerinfor> listVo = new ArrayList<Customerinfor>();
//            创建Result结果集的集合
            List<String> Result = new ArrayList<String>();
//            用来接收前台传过来的文件
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
//            创建对象f，且为空
            File f = null;
//            创建临时文件
            f = File.createTempFile("temp", null);
//            上传文件
            file.transferTo(f);
//            使用Excel读文件
            ExcelReader reader = ExcelUtil.getReader(f);
//            创建集合存入读的文件
            List<List<Object>> list = reader.read();
//            创建集合存入标准模板
            List<Object> model = new ArrayList<Object>();
//            标准模板
            model.add("客户名称(中文)");
            model.add("客户名称(日文)");
            model.add("客户名称(英文)");
            model.add("简称");
            model.add("负责人");
            model.add("项目联络人(中文)");
            model.add("项目联络人(日文)");
            model.add("项目联络人(英文)");
            model.add("联系电话");
            model.add("邮箱地址");
            model.add("共通事务联络人");
            model.add("联系电话");
            model.add("邮箱地址");
            model.add("地址(中文)");
            model.add("地址(日文)");
            model.add("地址(英文)");
            model.add("人员规模");
            model.add("所属公司");
            model.add("事业场编码");
            model.add("网址");
            model.add("备注");
            List<Object> key = list.get(0);
//           上传模板与标准模板 校验
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LogicalException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Customerinfor customerinfor = new Customerinfor();
                List<Object> value = list.get(k);
                k++;

                if (value != null && !value.isEmpty()) {
                    if(value.size()>0)
                    {
                        customerinfor.setCustchinese(Convert.toStr(value.get(0)));
                        if(customerinfor.getCustchinese() != null && customerinfor.getCustchinese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 客户名称(中文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>1)
                    {
                        customerinfor.setCustjapanese(Convert.toStr(value.get(1)));
                        if(customerinfor.getCustjapanese()!= null && customerinfor.getCustjapanese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 客户名称(日文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>2)
                    {
                        customerinfor.setCustenglish(Convert.toStr(value.get(2)));
                        if(customerinfor.getCustenglish() !=null && customerinfor.getCustenglish().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 客户名称(英文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>3)
                    {
                        customerinfor.setAbbreviation(Convert.toStr(value.get(3)));
                        if(customerinfor.getAbbreviation()!=null && customerinfor.getAbbreviation().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 简称 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>4)
                    {
                        customerinfor.setLiableperson(Convert.toStr(value.get(4)));
                        if(customerinfor.getLiableperson() !=null && customerinfor.getLiableperson().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 负责人 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>5)
                    {
                        customerinfor.setProchinese(Convert.toStr(value.get(5)));
                        if(customerinfor.getProchinese() !=null && customerinfor.getProchinese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(中文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>6)
                    {
                        customerinfor.setProjapanese(Convert.toStr(value.get(6)));
                        if(customerinfor.getProjapanese()!=null && customerinfor.getProjapanese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(日文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>7)
                    {
                        customerinfor.setProenglish(Convert.toStr(value.get(7)));
                        if(customerinfor.getProenglish() !=null && customerinfor.getProenglish().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(英文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>8)
                    {
                        customerinfor.setProtelephone(Convert.toStr(value.get(8)));
                        if(customerinfor.getProtelephone() !=null && customerinfor.getProtelephone().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 联系电话 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>9)
                    {
                        customerinfor.setProtemail(Convert.toStr(value.get(9)));
                        if(customerinfor.getProtemail() !=null && customerinfor.getProtemail().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 邮箱地址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>10)
                    {
                        customerinfor.setCommontperson(Convert.toStr(value.get(10)));
                        if(customerinfor.getCommontperson()!=null && customerinfor.getCommontperson().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 共通事务联络人 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>11)
                    {
                        customerinfor.setComtelephone(Convert.toStr(value.get(11)));
                        if(customerinfor.getComtelephone()!=null && customerinfor.getComtelephone().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 联系电话 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>12)
                    {
                        customerinfor.setComnemail(Convert.toStr(value.get(12)));
                        if(customerinfor.getComnemail()!=null && customerinfor.getComnemail().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 邮箱地址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>13)
                    {
                        customerinfor.setAddchinese(Convert.toStr(value.get(13)));
                        if(customerinfor.getAddchinese()!=null && customerinfor.getAddchinese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 地址(中文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>14)
                    {
                        customerinfor.setAddjapanese(Convert.toStr(value.get(14)));
                        if(customerinfor.getAddjapanese()!=null && customerinfor.getAddjapanese().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 地址(日文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>15)
                    {
                        customerinfor.setAddenglish(Convert.toStr(value.get(15)));
                        if(customerinfor.getAddenglish()!=null && customerinfor.getAddenglish().length() > 255)
                        {
                            throw new LogicalException("第" + i + "行 地址(英文) 长度超长，最大长度为255");
                        }
                    }
                    if(value.size()>17)
                    {
                        customerinfor.setThecompany(Convert.toStr(value.get(17)));
                        if(customerinfor.getThecompany()!=null && customerinfor.getThecompany().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 所属公司 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>18)
                    {
                        customerinfor.setCausecode(Convert.toStr(value.get(18)));
                        if(customerinfor.getCausecode()!=null && customerinfor.getCausecode().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 事业场编码 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>19)
                    {
                        customerinfor.setWebsite(Convert.toStr(value.get(19)));
                        if(customerinfor.getWebsite()!=null && customerinfor.getWebsite().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 网址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>20)
                    {
                        customerinfor.setRemarks(Convert.toStr(value.get(20)));
                    }
                    if(value.size()>16)
                    {
                        String person=Convert.toStr(value.get(16));
                        if(StrUtil.isNotBlank(person)){
                            person = person.trim().replace("<","").replace(">","").replace("≥","").replace("=","").replace("≤","").replace("-","");
                            if(Integer.parseInt(person)>0 && Integer.parseInt(person)<50){
                                customerinfor.setPerscale("BP007001");  //改数据
                            }
                            if(Integer.parseInt(person)>=50 && Integer.parseInt(person)<100){
                                customerinfor.setPerscale("BP007002");  //改数据
                            } if(Integer.parseInt(person)>=100 && Integer.parseInt(person)<500){
                                customerinfor.setPerscale("BP007003");  //改数据
                            }
                            if(Integer.parseInt(person)>=500){
                                customerinfor.setPerscale("BP007004");  //改数据
                            }
                        }
                    }
                }
                customerinfor.preInsert(tokenModel);
                customerinfor.setCustomerinfor_id(UUID.randomUUID().toString());
                customerinforMapper.insert(customerinfor);
                listVo.add(customerinfor);
                accesscount = accesscount + 1;
            }
            Result.add("失败数：" + error);
            Result.add("成功数：" + accesscount);
            return Result;

    }



}
