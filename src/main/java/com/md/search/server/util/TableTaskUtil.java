package com.md.search.server.util;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.md.search.server.bean.TableTask;

/**
 * 表任务工具类
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午4:02:03
 */
public class TableTaskUtil {

	public static Logger logger = LoggerFactory.getLogger(TableTaskUtil.class);

	public static Map<EventType, BlockingQueue<TableTask>> taskMap = new ConcurrentHashMap<EventType, BlockingQueue<TableTask>>();

	public static final int QUEUESIZE = 1000;

	static {
		taskMap.put(EventType.EXT_DELETE_ROWS,
				new LinkedBlockingQueue<TableTask>(QUEUESIZE));
		taskMap.put(EventType.EXT_UPDATE_ROWS,
				new LinkedBlockingQueue<TableTask>(QUEUESIZE));
		taskMap.put(EventType.EXT_WRITE_ROWS,
				new LinkedBlockingQueue<TableTask>(QUEUESIZE));
	}

	/**
	 * 获取任务队列
	 * 
	 * @param event
	 * @return
	 * @throws InterruptedException
	 */
	public static TableTask getEventTask(EventType event)
			throws InterruptedException {
		logger.info("get task !event:{} tabletask queue size:{}", event.name(),
				taskMap.get(event).size());
		return taskMap.get(event).take();
	}

	/**
	 * 添加任务
	 * 
	 * @param event
	 * @return
	 * @throws InterruptedException
	 */
	public static void addEventTask(EventType event, TableTask tableTask)
			throws InterruptedException {
		logger.info("add task !event:{} tabletask queue size:{}", event.name(),
				taskMap.get(event).size());
		taskMap.get(event).put(tableTask);

	}

}
