package com.md.search.server.service;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * 操作服务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月15日 下午2:34:23
 */
public interface OperateService {

	/**
	 * 时间处理
	 * 
	 * @param eventType
	 * @param id
	 *            数据主键
	 * @param table
	 *            表
	 * @param offset
	 *            binlog偏移
	 */
	public void eventControl(EventType eventType, String id, String table,
			long offset);

}
