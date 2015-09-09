package com.md.search.server.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.md.search.server.bean.Merchant;
import com.md.search.server.constant.ApplicationConstants;

/**
 * 全量更新任务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月15日 下午3:22:18
 */
@Service
public class FullDataService {

	public static Logger logger = LoggerFactory
			.getLogger(FullDataService.class);

	@Autowired
	private MerchantService merchantService;

	/**
	 * data Resource 全量更新
	 */
	public void runFullData() {
		List<Merchant> merchants = null;
		for (String table : ApplicationConstants.MERCHANTTABLESET) {
			logger.info("run data {}", table);
			try {
				merchants = merchantService.getMerchants(table);
				for (Merchant m : merchants) {
				    boolean result = false;
					List<Merchant> mers = merchantService
							.getMerchantsFromMerchant(m);
					if (null == mers || mers.size()==0) {
						continue;
					}
					for (Merchant obj : mers) {
						result = merchantService.insertMerchantToEs(table + obj.getId()
								+ obj.getWhich(), obj);
					}
					if(result) {
					logger.info(
							"run full data insert merchant success!{}|{}|{}",
							table, m.getId(), m.getWhich());
					} else {
						logger.info(
								"run full data insert merchant error!{}|{}|{}",
								table, m.getId(), m.getWhich());
					}
				}
			} catch (Exception e) {
				logger.error("fulldata error:", e);
				continue;
			}
		}
	}

}
