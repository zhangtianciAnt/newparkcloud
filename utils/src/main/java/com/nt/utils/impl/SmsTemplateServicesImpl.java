//package com.nt.utils.impl;
//
//import com.nt.dao_Utils.Platformsmsteplate;
//import com.nt.utils.services.SmsTemplateServices;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@Transactional(rollbackFor=Exception.class)
//public class SmsTemplateServicesImpl implements SmsTemplateServices {
//	private static Logger logger = LoggerFactory.getLogger(SmsTemplateServicesImpl.class);
//
//	@Autowired
//	private PlatformsmsteplateMapper stMapper;
//
//	@Override
//	public List<Platformsmsteplate> selectSmsTemplateList() throws Exception {
//		try{
//			List<Platformsmsteplate> pstList = stMapper.selectPlatformSmsTemplateList();
//			return pstList;
//		} catch (Exception e) {
////			logger.warn(e.getMessage());
//			throw new Exception("获取异常！");
//		}
//	}
//
//	@Override
//	public Platformsmsteplate selectSmsTemplateByTempCode(String tempCode) throws Exception {
//		try{
//			Platformsmsteplate pst = stMapper.selectByTempCode(tempCode);
//			return pst;
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("获取异常！");
//		}
//	}
//
//	@Override
//	public Platformsmsteplate selectSmsTemplate(String tempId) throws Exception {
//		try{
//			Platformsmsteplate pst = stMapper.selectByPrimaryKey(tempId);
//			return pst;
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("获取异常！");
//		}
//	}
//
//	@Override
//	public void insertSmsTemplate(Platformsmsteplate pst) throws Exception {
//		try{
//			pst.setPlatformsmsteplateid(UUID.randomUUID().toString());
//			pst.setCreateon(new Date());
//			pst.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//			stMapper.insert(pst);
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("插入异常！");
//		}
//	}
//
//	@Override
//	public void updateSmsTemplate(Platformsmsteplate pst, String userId) throws Exception {
//		try{
//			pst.setModifyby(userId);
//			pst.setModifyon(new Date());
//			stMapper.updateByPrimaryKeySelective(pst);
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("更新异常！");
//		}
//	}
//
//	@Override
//	public void updateSmsTemplateStatus(String tempId, String userId) throws Exception {
//		try{
//			stMapper.updateSmsTemplateStatus(tempId, userId);
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("更新异常！");
//		}
//	}
//
//}
