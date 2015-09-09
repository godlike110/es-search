package com.md.search.server.service.impl;

import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.md.search.server.service.EsService;
import com.md.search.server.util.EsUtil;
import com.vividsolutions.jts.geom.Coordinate;

@Service
public class EsServiceImpl implements EsService {

	public static Logger logger = LoggerFactory.getLogger(EsServiceImpl.class);

	@Autowired
	public Client esClient;

	@Autowired
	private ObjectMapper jsonMapper;

	@SuppressWarnings("finally")
	@Override
	public boolean upateEsDoc(String index, String type, String id, Object obj) {
		boolean result = false;
		try {
			removeEsDoc(index, type, id);
			upateEsDoc(index, type, id, obj);
			result = true;
		} catch (Exception e) {
			logger.error("update es error:", e);
			result = false;
		} finally {
			return result;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public boolean insertEsDoc(String index, String type, String id, Object obj) {
		boolean result = false;
		try {
			Map<String, Object> data = EsUtil.ConvertObjToMap(obj);
			esClient.prepareIndex(index, type, id).setSource(data)
					.setRefresh(true).execute().actionGet();
			result = true;
		} catch (Exception e) {
			logger.error("insert es error:", e);
			result = false;
		} finally {
			return result;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public boolean insertEsDocByString(String index, String type, String id,
			String obj) {
		boolean result = false;
		try {
			esClient.prepareIndex(index, type, id).setSource(obj)
					.setRefresh(true).execute().actionGet();
			result = true;
		} catch (Exception e) {
			logger.error("insert es error:", e);
			result = false;
		} finally {
			return result;
		}
	}

	@SuppressWarnings("finally")
	@Override
	public boolean removeEsDoc(String index, String type, String id) {

		boolean result = false;
		try {

			esClient.prepareDelete(index, type, id).setRefresh(true).execute()
					.actionGet();
			result = true;

		} catch (Exception e) {
			logger.error("remove es error:", e);
			result = false;

		} finally {
			return result;
		}
	}

	@Override
	public Map<String, Object> getDoc(String index, String type, String id) {
		try {
			GetResponse getResponse = esClient.prepareGet(index, type, id)
					.execute().actionGet();
			return getResponse.getSource();
		} catch (Exception e) {
			logger.error("get Doc error!", e);
		}
		return null;
	}

	// @SuppressWarnings("rawtypes")
	// @Override
	// public <T> T getDocById(String index, String type, String id, Class T) {
	// TermQueryBuilder idClause = QueryBuilders.termQuery("_id", id);
	//
	// QueryBuilder builder = QueryBuilders.boolQuery().must(idClause);
	//
	// try {
	// SearchResponse searchResponse = esClient.prepareSearch(index)
	// .setTypes(type).setQuery(builder).execute().actionGet();
	//
	// if (searchResponse != null) {
	// SearchHits searchHits = searchResponse.getHits();
	// if (searchHits.getHits().length > 0) {
	// byte[] accountAsBytes = searchHits.getAt(0).source();
	// T obj = convert(accountAsBytes, T);
	// return obj;
	// }
	// }
	// } catch (Exception e) {
	// logger.error("getdocbyid error!", e);
	// }
	// return null;
	//
	// }

	@SuppressWarnings({ "rawtypes", "unused" })
	private <T> T convert(byte[] source, Class T) {
		if (source == null || source.length == 0)
			return null;
		try {
			this.jsonMapper.getDeserializationConfig().without(
					DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
			@SuppressWarnings("unchecked")
			final T obj = (T) this.jsonMapper.readValue(source, T);
			return obj;
		} catch (Exception e) {
			logger.error("convert esresponse error!", e);
		}
		return null;
	}

	@Override
	public SearchResponse filteredQuery(String index, String type,
			QueryBuilder queryBuilder) {

		try {
			
			return esClient.prepareSearch(index).setTypes(type)
					.setQuery(queryBuilder).execute().actionGet();
		} catch (Exception e) {
			logger.error("search error!", e);
			return null;
		}

	}

}
