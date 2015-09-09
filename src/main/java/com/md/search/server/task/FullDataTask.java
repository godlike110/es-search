package com.md.search.server.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.service.FullDataService;

/**
 * 全量更新任务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月15日 下午2:56:04
 */
public class FullDataTask implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(FullDataTask.class);

	@Autowired
	public static FullDataService fullDataService;

	@Override
	public void run() {

		logger.info("start to run full data task!");
		fullDataService.runFullData();
		logger.info("full data task finish!");
		ApplicationConstants.HAS_GROBAL_TASK = false;

	}

}
