package com.md.search.server.service;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.md.search.server.bean.Updown;
import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.dao.WeixinAccountDao;
import com.md.search.server.enums.UpdownStatus;
import com.md.search.server.util.PropertiesUtil;

/**
 * 小区es服务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月7日 下午8:56:30
 */
@Service
public class UpdownEsServer {

	public static Logger logger = LoggerFactory.getLogger(UpdownEsServer.class);

	@Autowired
	private Client esClient;

	@Autowired
	private WeixinAccountDao weixinAccountDao;

	public static String index = "maydo";
	public static String type = "community";

	/**
	 * 可以跑吗
	 */
	public static volatile boolean runable = true;

	/**
	 * 生成小区es 文档
	 * 
	 * @param updown
	 * @return
	 */
	public static Map<String, Object> updownDocument(Updown updown) {
		Map<String, Object> json = new HashMap<String, Object>();
		json.put("location", new GeoPoint(Double.parseDouble(updown.getLat()),
				Double.parseDouble(updown.getLon())));
		json.put("add_time", updown.getUpdateTime());
		json.put("level_mark", updown.getLevelMark());
		json.put("lon", updown.getLon());
		json.put("lat", updown.getLat());
		json.put("resource_order_count", updown.getResourceOrderCount());
		json.put("address", updown.getAddress());
		json.put("dist", updown.getDist());
		json.put("city", updown.getCity());
		json.put("region", updown.getRegion());
		json.put("table_flx", updown.getTableFlx());
		json.put("name", updown.getName());
		json.put("id", updown.getId());
		return json;
	}

	/**
	 * 构建数据
	 * 
	 * @param fields
	 * @return
	 */
	@SuppressWarnings("finally")
	public XContentBuilder createDate(Map<String, Object> fields) {
		XContentBuilder xContentBuilder = null;
		try {
			xContentBuilder = XContentFactory.jsonBuilder().startObject();
			Iterator<String> keyIterator = fields.keySet().iterator();
			while (keyIterator.hasNext()) {
				String key = keyIterator.next();
				Object value = fields.get(key);
				xContentBuilder.field(key, value);
			}
			xContentBuilder.endObject();
		} catch (Exception e) {
			logger.error("create data error:", e);
		} finally {
			return xContentBuilder;
		}
	}

	/**
	 * 
	 * @param updown
	 * @return
	 */
	public boolean updateOrinsertDoc(Updown updown) {
		if (ifUpdownExsitInEs(updown.getId())) {

			if (updown.getIsDisable() == UpdownStatus.ENABLE.getStatus()) {
				// 更新
				removeEsDoc(updown);
				return insertEsDoc(updown);
			} else {
				// 删除
				return removeEsDoc(updown);
			}
		} else {
			// 插入
			if (updown.getIsDisable() == UpdownStatus.ENABLE.getStatus()) {
				insertEsDoc(updown);
			}
		}
		return false;
	}

	/**
	 * 定时任务处理(会更新小区最近跟新时间和binlog偏移量)
	 * 
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	public void updateUpdownTask() throws ParseException, FileNotFoundException {
		
		if(!ApplicationConstants.UPDOWN_UPDATE_OPEN) {
			return;
		}
		
		
		if (runable) {
			runable = false;
			long start = System.currentTimeMillis();
			logger.info("start run updateUpdown task!");
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date updateTime = null;

			updateTime = sdf.parse(ApplicationConstants.UPDATE_DATE);
			PropertiesUtil.writeUpdownUpdateTime(new Date());

			// updateTime = sdf.parse("2015-08-07 00:00:00");
			List<Updown> updatedUpdowns = weixinAccountDao
					.getLatestUpdateUpdown(updateTime);
			logger.info("need to update updowns:{}", updatedUpdowns.size());
			for (Updown updown : updatedUpdowns) {
				if (null == updown.getLon()) {
					logger.info("weixin_account,id:{} data error!",
							updown.getId());
					continue;
				}
				updateOrinsertDoc(updown);
			}
			// 更新binLog 偏移量(不需要了)
			//PropertiesUtil.writeBinLogPos();
			logger.info("updateUpdown task done!use time:{}",
					System.currentTimeMillis() - start);
			runable = true;
		} else {
			logger.info("there is already a updateupdown task run,this task will close");
		}
	}

	/**
	 * 根据id获取数据
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("finally")
	public boolean ifUpdownExsitInEs(String id) {
		boolean result = false;
		try {
			final TermQueryBuilder idClause = QueryBuilders
					.termQuery("_id", id);

			final QueryBuilder builder = QueryBuilders.boolQuery().must(
					idClause);

			SearchResponse searchResponse = esClient.prepareSearch(index)
					.setTypes(type).setQuery(builder).execute().actionGet();
			SearchHits searchHitsHit = searchResponse.getHits();
			if (searchHitsHit.getHits().length > 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			logger.error("get doc Error from es:", e);
		} finally {
			return result;
		}
	}

	/**
	 * 更新数据
	 * 
	 * @param updown
	 * @return
	 */
	@SuppressWarnings("finally")
	public boolean upateEsDoc(String id, Map<String, Object> fields) {
		boolean result = false;
		try {
			BulkRequestBuilder bulkRequest = esClient.prepareBulk();
			bulkRequest.add(esClient.prepareUpdate(index, type, id).setSource(
					createDate(fields)));
			logger.info("update weixin_account from es success,id:{}", id);
			return true;
		} catch (Exception e) {
			logger.error("update error :", e);
			logger.info("update weixin_account from es failed,id:{}", id);
			return false;
		} finally {
			return result;
		}
	}

	/**
	 * 插入es文档
	 * 
	 * @param updown
	 * @return
	 */
	@SuppressWarnings("finally")
	public boolean insertEsDoc(Updown updown) {
		boolean result = false;
		try {
			Map<String, Object> data = updownDocument(updown);
			esClient.prepareIndex(index, type, updown.getId()).setRefresh(true)
					.setSource(data).setRefresh(true).execute().actionGet();
			logger.info("insert weixin_account from es success,id:{}",
					updown.getId());
			result = true;
		} catch (Exception e) {
			logger.error("insert error:", e);
			logger.info("insert weixin_account from es failed,id:{}",
					updown.getId());
			result = false;
		} finally {
			return result;
		}

	}

	@SuppressWarnings("finally")
	public boolean removeEsDoc(Updown updown) {
		boolean result = false;
		try {

			esClient.prepareDelete(index, type, updown.getId())
					.setRefresh(true).execute().actionGet();
			result = true;
			logger.info("delete weixin_account from es success,id:{}",
					updown.getId());

		} catch (Exception e) {
			logger.error("remove error:", e);
			logger.info("delete weixin_account from es failed,id:{}",
					updown.getId());
			result = false;

		} finally {
			return result;
		}
	}

}
