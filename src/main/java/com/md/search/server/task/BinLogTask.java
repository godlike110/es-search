package com.md.search.server.task;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import com.md.search.server.bean.TableTask;
import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.util.TableTaskUtil;

/**
 * binlog任务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午5:21:03
 */
public class BinLogTask implements Runnable {

	public static Logger logger = LoggerFactory.getLogger(BinLogTask.class);

	private TableMapEventData tMap;

	public void run() {
		Map<String, Event> event = null;
		Event tableEvent = null;
		tMap = null;
		Event realEvent = null;
		WriteRowsEventData wData = null;
		UpdateRowsEventData uData = null;
		DeleteRowsEventData dData = null;
		List<String> ids = new ArrayList<String>();

		while (true) {

			try {
				event = BinLogListener.getEvMap();
				if (event != null) {
					tableEvent = event.get(EventType.TABLE_MAP.name());
					if (tableEvent != null) {
						tMap = tableEvent.getData();
					}
					// 只处理mdsh库 在配置中配置的表
					if (StringUtils.isNotBlank(tMap.getDatabase())
							&& tMap.getDatabase().equals(
									ApplicationConstants.BINGLOGDB)
							&& ApplicationConstants.BINLOGTABLESET
									.contains(tMap.getTable())) {
						realEvent = event.get("realData");
						if (null != realEvent) {
							switch (realEvent.getHeader().getEventType()) {
							case EXT_DELETE_ROWS:
								dData = realEvent.getData();
								for (Object[] row : dData.getRows()) {
									ids.add((String) row[0]);
								}
								/**
								 * 添加表更新任务
								 */
								TableTaskUtil.addEventTask(
										EventType.EXT_DELETE_ROWS,
										new TableTask(
												EventType.EXT_DELETE_ROWS, ids,
												tMap.getTable(), tableEvent
														.getHeader()));
								break;
							case EXT_UPDATE_ROWS:
								uData = realEvent.getData();
								logger.info(uData.toString());
								for (Map.Entry<Serializable[], Serializable[]> row : uData
										.getRows()) {
									ids.add((String) row.getValue()[0]);
								}
								TableTaskUtil.addEventTask(
										EventType.EXT_UPDATE_ROWS,
										new TableTask(
												EventType.EXT_UPDATE_ROWS, ids,
												tMap.getTable(), tableEvent
														.getHeader()));
								break;
							case EXT_WRITE_ROWS:
								wData = realEvent.getData();
								for (Object[] row : wData.getRows()) {
									ids.add((String) row[0]);
								}
								TableTaskUtil.addEventTask(
										EventType.EXT_WRITE_ROWS,
										new TableTask(EventType.EXT_WRITE_ROWS,
												ids, tMap.getTable(),
												tableEvent.getHeader()));
								break;
							default:
								break;
							}
						}

						logger.info("table:{} has a {} event,ids:{}",
								tMap.getTable(), realEvent.getHeader()
										.getEventType().name(),
								JSONObject.toJSONString(ids));
					}
					// 清空此次处理
					event = null;
					tableEvent = null;
					tMap = null;
					realEvent = null;
					wData = null;
					uData = null;
					dData = null;
					ids = new ArrayList<String>();
				}
			} catch (Exception e) {
				logger.error("binlog task error:", e);
			}
		}
	}
}
