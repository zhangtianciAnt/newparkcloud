package com.nt.service_pfans.PFANS6000.Impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.nt.dao_Pfans.PFANS6000.Customerinfor;
import com.nt.dao_Pfans.PFANS6000.Supplierinfor;
import com.nt.service_pfans.PFANS6000.SupplierinforService;
import com.nt.service_pfans.PFANS6000.mapper.SupplierinforMapper;
import com.nt.utils.LogicalException;
import com.nt.utils.dao.TokenModel;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@Transactional(rollbackFor = Exception.class)
public class SupplierinforServiceImpl implements SupplierinforService {

    @Autowired
    private SupplierinforMapper supplierinforMapper;


    @Override
    public List<Supplierinfor> getsupplierinfor(Supplierinfor supplierinfor) throws Exception {
        return supplierinforMapper.select(supplierinfor);
    }

    @Override
    public Supplierinfor getsupplierinforApplyOne(String supplierinfor_id) throws Exception {
        return supplierinforMapper.selectByPrimaryKey(supplierinfor_id);
    }

    @Override
    public void updatesupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception {
        supplierinforMapper.updateByPrimaryKeySelective(supplierinfor);
    }

    @Override
    public void createsupplierinforApply(Supplierinfor supplierinfor, TokenModel tokenModel) throws Exception {
        supplierinfor.preInsert(tokenModel);
        supplierinfor.setSupplierinfor_id(UUID.randomUUID().toString());
        supplierinforMapper.insert(supplierinfor);
    }

    @Override
    public List<Supplierinfor> getSupplierNameList(Supplierinfor supplierinfor, HttpServletRequest request) throws Exception {
        return supplierinforMapper.select(supplierinfor);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<String> supimport(HttpServletRequest request, TokenModel tokenModel) throws Exception {
        try {
            List<Supplierinfor> listVo = new ArrayList<Supplierinfor>();
            List<String> Result = new ArrayList<String>();
            MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
            File f = null;
            f = File.createTempFile("temp", null);
            file.transferTo(f);
            ExcelReader reader = ExcelUtil.getReader(f);
            List<List<Object>> list = reader.read();
            List<Object> model = new ArrayList<Object>();
            model.add("供应商名称(中文)");
            model.add("供应商名称(日文)");
            model.add("供应商名称(英文)");
            model.add("简称");
            model.add("负责人");
            model.add("供应商编码（合同用）");
            model.add("收款方全称");
            model.add("收款方银行账号");
            model.add("收款方开户行");
            model.add("收款方编码");
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
            model.add("网址");
            model.add("备注");

            List<Object> key = list.get(0);
            for (int i = 0; i < key.size(); i++) {
                if (!key.get(i).toString().trim().equals(model.get(i))) {
                    throw new LoginException("第" + (i + 1) + "列标题错误，应为" + model.get(i).toString());
                }
            }
            int k = 1;
            int accesscount = 0;
            int error = 0;
            for (int i = 1; i < list.size(); i++) {
                Supplierinfor supplierinfor = new Supplierinfor();
                List<Object> value = list.get(k);
                k++;
                if (value != null && !value.isEmpty()) {
                    if(value.size()>0)
                    {
                        supplierinfor.setSupchinese(Convert.toStr(value.get(0)));
                        if(supplierinfor.getSupchinese() != null && supplierinfor.getSupchinese().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 供应商名称(中文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>1)
                    {
                        supplierinfor.setSupjapanese(Convert.toStr(value.get(1)));
                        if(supplierinfor.getSupjapanese() != null && supplierinfor.getSupjapanese().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 供应商名称(日文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>2)
                    {
                        supplierinfor.setSupenglish(Convert.toStr(value.get(2)));
                        if(supplierinfor.getSupenglish() != null && supplierinfor.getSupenglish().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 供应商名称(英文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>3)
                    {
                        supplierinfor.setAbbreviation(Convert.toStr(value.get(3)));
                        if(supplierinfor.getAbbreviation() != null && supplierinfor.getAbbreviation().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 简称 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>4)
                    {
                        supplierinfor.setLiableperson(Convert.toStr(value.get(4)));
                        if(supplierinfor.getLiableperson() != null && supplierinfor.getLiableperson().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 负责人 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>5)
                    {
                        supplierinfor.setVendornum(Convert.toStr(value.get(5)));
                        if(supplierinfor.getVendornum() != null && supplierinfor.getVendornum().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 供应商编码（合同用） 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>6)
                    {
                        supplierinfor.setPayeename(Convert.toStr(value.get(6)));
                        if(supplierinfor.getPayeename() != null && supplierinfor.getPayeename().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 收款方全称 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>7)
                    {
                        supplierinfor.setPayeebankaccountnumber(Convert.toStr(value.get(7)));
                        if(supplierinfor.getPayeebankaccountnumber() != null && supplierinfor.getPayeebankaccountnumber().length() > 100)
                        {
                            throw new LogicalException("第" + i + "行 收款方银行账号 长度超长，最大长度为100");
                        }
                    }
                    if(value.size()>8)
                    {
                        supplierinfor.setPayeebankaccount(Convert.toStr(value.get(8)));
                        if(supplierinfor.getPayeebankaccount() != null && supplierinfor.getPayeebankaccount().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 收款方开户行 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>9)
                    {
                        supplierinfor.setSuppliercode(Convert.toStr(value.get(9)));
                        if(supplierinfor.getSuppliercode() != null && supplierinfor.getSuppliercode().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 收款方编码 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>10)
                    {
                        supplierinfor.setProchinese(Convert.toStr(value.get(10)));
                        if(supplierinfor.getProchinese() != null && supplierinfor.getProchinese().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(中文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>11)
                    {
                        supplierinfor.setProjapanese(Convert.toStr(value.get(11)));
                        if(supplierinfor.getProjapanese() != null && supplierinfor.getProjapanese().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(日文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>12)
                    {
                        supplierinfor.setProenglish(Convert.toStr(value.get(12)));
                        if(supplierinfor.getProenglish() != null && supplierinfor.getProenglish().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 项目联络人(英文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>13)
                    {
                        supplierinfor.setProtelephone(Convert.toStr(value.get(13)));
                        if(supplierinfor.getProenglish() != null && supplierinfor.getProenglish().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 联系电话 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>14)
                    {
                        supplierinfor.setProtemail(Convert.toStr(value.get(14)));
                        if(supplierinfor.getProtemail() != null && supplierinfor.getProtemail().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 邮箱地址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>15)
                    {
                        supplierinfor.setCommontperson(Convert.toStr(value.get(15)));
                        if(supplierinfor.getCommontperson() != null && supplierinfor.getCommontperson().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 共通事务联络人 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>16)
                    {
                        supplierinfor.setComtelephone(Convert.toStr(value.get(16)));
                        if(supplierinfor.getComtelephone() != null && supplierinfor.getComtelephone().length() > 20)
                        {
                            throw new LogicalException("第" + i + "行 联系电话 长度超长，最大长度为20");
                        }
                    }
                    if(value.size()>17)
                    {
                        supplierinfor.setComnemail(Convert.toStr(value.get(17)));
                        if(supplierinfor.getComnemail() != null && supplierinfor.getComnemail().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 邮箱地址 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>18)
                    {
                        supplierinfor.setAddchinese(Convert.toStr(value.get(18)));
                        if(supplierinfor.getAddchinese() != null && supplierinfor.getAddchinese().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 地址(中文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>19)
                    {
                        supplierinfor.setAddjapanese(Convert.toStr(value.get(19)));
                        if(supplierinfor.getAddjapanese() != null && supplierinfor.getAddjapanese().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 地址(日文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>20)
                    {
                        supplierinfor.setAddenglish(Convert.toStr(value.get(20)));
                        if(supplierinfor.getAddenglish() != null && supplierinfor.getAddenglish().length() > 50)
                        {
                            throw new LogicalException("第" + i + "行 地址(英文) 长度超长，最大长度为50");
                        }
                    }
                    if(value.size()>22)
                    {
                        supplierinfor.setWebsite(Convert.toStr(value.get(22)));
                        if(supplierinfor.getWebsite() != null && supplierinfor.getWebsite().length() > 200)
                        {
                            throw new LogicalException("第" + i + "行 网址 长度超长，最大长度为200");
                        }
                    }
                    if(value.size()>23)
                    {
                        supplierinfor.setRemarks(Convert.toStr(value.get(23)));
                    }

                    if(value.size()>21)
                    {
                        String person= Convert.toStr(value.get(21));
                        if(StrUtil.isNotBlank(person)){
                            person = person.trim();
                            if(person.contains("≥") || person.contains("<"))
                            {
                                person = person.substring(1);
                            }
                            if(Integer.parseInt(person)>0 && Integer.parseInt(person)<50){
                                supplierinfor.setPerscale("BP007001");  //改数据
                            }
                            if(Integer.parseInt(person)>=50 && Integer.parseInt(person)<100){
                                supplierinfor.setPerscale("BP007002");  //改数据
                            } if(Integer.parseInt(person)>=100 && Integer.parseInt(person)<500){
                                supplierinfor.setPerscale("BP007003");  //改数据
                            }
                            if(Integer.parseInt(person)>500){
                                supplierinfor.setPerscale("BP007004");  //改数据
                            }
                        }
                    }
                }

                supplierinfor.preInsert();
                supplierinfor.setSupplierinfor_id(UUID.randomUUID().toString());
                supplierinforMapper.insert(supplierinfor);
                listVo.add(supplierinfor);
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
