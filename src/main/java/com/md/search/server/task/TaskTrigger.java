package com.md.search.server.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.md.search.server.constant.ApplicationConstants;

/**
 * 任务触发器
 * 
 * @author zhiwei.wen
 * @Date 2015年8月15日 下午2:59:34
 */
public class TaskTrigger implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(TaskTrigger.class);

	@Override
	public void run() {
        logger.info("task Triger started!status:{}|{}",ApplicationConstants.ALL_TASK_RUN,ApplicationConstants.HAS_GROBAL_TASK);
		if (ApplicationConstants.ALL_TASK_RUN) {
			if (ApplicationConstants.HAS_GROBAL_TASK) {
				logger.info("start run full data task!");
				new Thread(new FullDataTask()).start();

			}
			while (true) {
				try {
					Thread.sleep(4000l);
					if (!ApplicationConstants.HAS_GROBAL_TASK) {
						logger.info("start run binlog tasks!");
						new Thread(new BinLogDelTask()).start();
						new Thread(new BinLogWriteTask()).start();
						new Thread(new BinLogUpdateTask()).start();
						new Thread(new BinLogListener()).start();
						break;
					}
				} catch (InterruptedException e) {

					logger.error("taskTriger error：", e);
				}

			}

		}

	}

}
