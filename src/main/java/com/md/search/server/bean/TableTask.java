package com.md.search.server.bean;

import java.io.Serializable;
import java.util.List;

import com.github.shyiko.mysql.binlog.event.EventHeader;
import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * mdsh 表任务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月14日 下午3:51:03
 */
public class TableTask implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7467506750475109324L;

	private EventType eventType;

	private List<String> ids;

	private String table;

	private EventHeader header;

	public TableTask(EventType eventType, List<String> ids, String table,
			EventHeader header) {
		this.eventType = eventType;
		this.ids = ids;
		this.table = table;
		this.header = header;
	}

	public EventType getEventType() {
		return eventType;
	}

	public List<String> getIds() {
		return ids;
	}

	public String getTable() {
		return table;
	}

	public void setEvent(EventType eventType) {
		this.eventType = eventType;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public EventHeader getHeader() {
		return header;
	}

	public void setHeader(EventHeader header) {
		this.header = header;
	}

}
