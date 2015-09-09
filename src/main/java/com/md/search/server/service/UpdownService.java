package com.md.search.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.md.search.server.bean.Updown;
import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.dao.UpDownDao;
import com.md.search.server.dao.WeixinAccountDao;

/**
 * 小区操作类
 * 
 * @author zhiwei.wen
 * @Date 2015年8月7日 下午2:27:34
 */
@Service
public class UpdownService {

	public static Logger logger = LoggerFactory.getLogger(UpdownService.class);

	@Autowired
	private UpDownDao upDownDao;

	@Autowired
	private WeixinAccountDao weixinAccountDao;

	@Autowired
	private EsService esService;

	private static int updatecounts = 0;

	private static int insertcounts = 0;

	/**
	 * 根据楼盘城市获取小区
	 * 
	 * @param name
	 * @param city
	 * @return
	 */
	private Updown getUpdownsByNameAndCity(String name, String city) {
		try {
			return weixinAccountDao.getUpdownsByName(name, city);
		} catch (Exception e) {
			logger.error("dao operate err", e);
			return null;
		}
	}

	/**
	 * 扫描整个表 updown
	 * 
	 * @param limit
	 * @param offset
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Updown> getUpdowns(int limit, int offset) {
		try {
			return upDownDao.getUpdowns(offset, limit);
		} catch (Exception e) {
			logger.error("dao operate err", e);
			return null;
		}
	}

	/**
	 * 插入小区
	 * 
	 * @param updown
	 */
	private void insert(Updown updown) {
		try {
			weixinAccountDao.insert(updown);
		} catch (Exception e) {
			logger.error("dao operate err", e);
		}
	}

	/**
	 * 更新小区坐标
	 * 
	 * @param updown
	 */
	private void update(Updown updown) {
		try {
			weixinAccountDao.update(updown);
		} catch (Exception e) {
			logger.error("dao operate err", e);
		}
	}

	/**
	 * 获取总数量
	 * 
	 * @return
	 */
	private int getUpdownCounts() {
		try {
			return upDownDao.getCounts();
		} catch (Exception e) {
			logger.error("db operate err");
			return 0;
		}
	}

	/**
	 * 获取全国总数量
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private int getDbUpdownCounts() {
		try {
			return weixinAccountDao.getCounts();
		} catch (Exception e) {
			logger.error("db operate err");
			return 0;
		}
	}

	/**
	 * 更新或者插入小区数据
	 * 
	 * @param updown
	 */
	public void insertOrUpdateUpdown(Updown updown) {
		Updown dbUpdown = getUpdownsByNameAndCity(updown.getName(),
				updown.getTableFlx());
		if (null == dbUpdown) {
			String id = UUID.randomUUID().toString();
			if (id.length() > 32) {
				id = id.substring(0, 32);
			}
			updown.setId(id);
			insert(updown);
			logger.info("insert :{}", ++UpdownService.insertcounts);
		} else {
			String id = UUID.randomUUID().toString();
			if (id.length() > 32) {
				id = id.substring(0, 32);
			}
			dbUpdown.setId(id);
			dbUpdown.setLat(updown.getLat());
			dbUpdown.setLon(updown.getLon());
			update(dbUpdown);
			logger.info("update :{}", ++UpdownService.updatecounts);
		}
	}

	/**
	 * 搜索某个城市包含name的小区
	 * 
	 * @param name
	 * @param city
	 * @return
	 */
	public List<Updown> searchUpdownByName(String name, String city) {
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.must(QueryBuilders.matchPhrasePrefixQuery("name", name))
				.must(QueryBuilders.termQuery("table_flx", city));
		SearchResponse searchResponse = esService.filteredQuery(
				ApplicationConstants.INDEX, "community", queryBuilder);
		if (null == searchResponse
				|| StringUtils.isBlank(searchResponse.toString())) {
			return null;
		}
		SearchHits searchHits = searchResponse.getHits();
		if (null == searchHits || searchHits.getHits().length == 0) {
			return null;
		}

		List<Updown> updowns = new ArrayList<Updown>();

		for (SearchHit sh : searchHits.getHits()) {
			Updown updown = new Updown();
			updown.setId(sh.getId());
			updown.setTableFlx((String) sh.getSource().get("table_flx"));
			updown.setAddress((String) sh.getSource().get("address"));
			updown.setName((String) sh.getSource().get("name"));
			updown.setLon((String) sh.getSource().get("lon"));
			updown.setLat((String) sh.getSource().get("lat"));
			updown.setLevelMark((String) sh.getSource().get("level_mark"));
			updowns.add(updown);
		}
		return updowns;

	}

	/**
	 * 抓取数据刷新到db
	 */
	public void flushDate() {

		int counts = getUpdownCounts();
		int pagesize = 300;
		int pages = counts / 300 + 1;
		logger.info("pages:{}", pages);
		for (int i = 0; i <= pages; i++) {
			List<Updown> updowns = upDownDao.getUpdowns(i * pagesize, pagesize);
			for (Updown updown : updowns) {
				insertOrUpdateUpdown(updown);
			}
		}
		logger.info("done:{}|{}", updatecounts, insertcounts);

	}

}
