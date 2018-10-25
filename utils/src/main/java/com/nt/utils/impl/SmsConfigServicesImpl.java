//package com.nt.utils.impl;
//
//import com.nt.dao_Utils.Platformsmsconfig;
//import com.nt.utils.AuthConstants;
//import com.nt.utils.services.SmsConfigServices;
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
//public class SmsConfigServicesImpl implements SmsConfigServices {
//	private static Logger logger = LoggerFactory.getLogger(SmsConfigServicesImpl.class);
//
//	@Autowired
//	private PlatformsmsconfigMapper scMapper;
//
//	@Override
//	public List<Platformsmsconfig> selectSmsConfigList() throws Exception {
//		try{
//			List<Platformsmsconfig> pscList = scMapper.selectPlatformSmsConfigList();
//			return pscList;
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("获取异常！");
//		}
//	}
//
//	@Override
//	public Platformsmsconfig selectSmsConfig(String configId) throws Exception {
//		try{
//			Platformsmsconfig psc = scMapper.selectByPrimaryKey(configId);
//			return psc;
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("获取异常！");
//		}
//	}
//
//	@Override
//	public void insertSmsConfig(Platformsmsconfig psc) throws Exception {
//		try{
//			psc.setPlatformsmsconfigid(UUID.randomUUID().toString());
//			psc.setCreateon(new Date());
//			psc.setStatus(AuthConstants.DEL_FLAG_NORMAL);
//			scMapper.insert(psc);
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("插入异常！");
//		}
//	}
//
//	@Override
//	public void updateSmsConfig(Platformsmsconfig psc, String userId) throws Exception {
//		try{
//			psc.setModifyby(userId);
//			psc.setModifyon(new Date());
//			scMapper.updateByPrimaryKeySelective(psc);
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("更新异常！");
//		}
//	}
//
//	@Override
//	public void updateSmsConfigStatus(String configId, String userId) throws Exception {
//		try{
//			scMapper.updateSmsConfigStatus(configId, userId);
//		} catch (Exception e) {
//			logger.warn(e.getMessage());
//			throw new Exception("更新异常！");
//		}
//	}
//
//}
