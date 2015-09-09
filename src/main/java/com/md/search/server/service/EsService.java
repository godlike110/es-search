package com.md.search.server.service;

import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * es服务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月15日 上午10:17:48
 */
public interface EsService {

	/**
	 * 更新索引
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @param fields
	 * @return
	 */
	public boolean upateEsDoc(String index, String type, String id, Object obj);

	/**
	 * 插入
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @param fields
	 * @return
	 */
	public boolean insertEsDoc(String index, String type, String id, Object obj);

	/**
	 * 插入String
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @param obj
	 * @return
	 */
	public boolean insertEsDocByString(String index, String type, String id,
			String obj);

	/**
	 * 删除
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 */
	public boolean removeEsDoc(String index, String type, String id);

	/**
	 * 获取某个文档
	 * 
	 * @param index
	 * @param type
	 * @param id
	 * @return
	 */
	public Map<String, Object> getDoc(String index, String type, String id);

	/**
	 * 搜索
	 * 
	 * @param index
	 * @param type
	 * @param filterQueryBuilder
	 * @return
	 */
	public SearchResponse filteredQuery(String index, String type,
			QueryBuilder queryBuilder);

}
