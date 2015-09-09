package com.md.search.server.task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.task.BinLogTask;

/**
 * binLog 侦听器
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 上午11:46:34
 */
public class BinLogListener implements Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(BinLogListener.class);

	private static BlockingQueue<Map<String, Event>> eventList = new LinkedBlockingQueue<Map<String, Event>>(
			10000);

	private static Map<String, Event> event = new HashMap<String, Event>();

	private static volatile int flag = 0;

	public static Map<String, Event> getEvMap() throws InterruptedException {
		return eventList.take();
	}

	@Override
	public void run() {

		ExecutorService eService = Executors.newSingleThreadExecutor();
		eService.execute(new BinLogTask());
		BinaryLogClient client = new BinaryLogClient(
				ApplicationConstants.MYSQLHOST,
				Integer.parseInt(ApplicationConstants.MYSQLPORT),
				ApplicationConstants.MYSQLUSER,
				ApplicationConstants.MYSQLPASSWD,
				Long.parseLong(ApplicationConstants.BINLOGPOS));
		client.registerEventListener(new EventListener() {

			@Override
			public void onEvent(Event env) {

				if (env.getHeader().getEventType() == EventType.QUERY
						&& flag == 0) {
					event.put(EventType.QUERY.name(), env);
					flag = 1;
				}
				if (env.getHeader().getEventType() == EventType.TABLE_MAP
						&& flag == 1) {
					event.put(EventType.TABLE_MAP.name(), env);
					flag = 2;
				}
				if ((env.getHeader().getEventType() == EventType.EXT_WRITE_ROWS
						|| env.getHeader().getEventType() == EventType.EXT_UPDATE_ROWS || env
						.getHeader().getEventType() == EventType.EXT_DELETE_ROWS)
						&& flag == 2) {
					event.put("realData", env);
					flag = 3;
				}
				if (env.getHeader().getEventType() == EventType.XID
						&& flag == 3) {
					event.put(EventType.XID.name(), env);
					try {
						eventList.put(event);
						event = new HashMap<String, Event>();
						flag = 0;
					} catch (InterruptedException e) {

						logger.error("binlogTask error:", e);
					}
				}

			}
		});
		try {
			logger.info("binlog listener starting");
			client.connect();
		} catch (IOException e) {
			logger.info("binlog listener started failed!", e);
		}

	}
}
