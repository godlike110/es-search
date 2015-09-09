package com.md.search.server.service;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceFilterBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.GeoDistanceSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.md.search.server.bean.Updown;

/**
 * 定位服务
 * 
 * @author zhiwei.wen
 * @Date 2015年8月6日 下午7:16:47
 */
@Service
public class LocationService {

	public static Logger logger = LoggerFactory
			.getLogger(LocationService.class);

	@Autowired
	private Client esClient;

	/**
	 * 定位小区
	 * 
	 * @param lon
	 * @param lat
	 * @return
	 */
	public Updown location(double lon, double lat, long distance, String index,
			String type) {

		QueryBuilder queryBuilder = queryBuilder(lon, lat, distance);

		GeoDistanceSortBuilder sortBuilder = sortBuilder(lon, lat);

		SearchResponse searchResponse = response(queryBuilder, sortBuilder,
				index, type);

		if (null == searchResponse
				|| StringUtils.isBlank(searchResponse.toString())) {
			return null;
		}

		SearchHits searchHits = searchResponse.getHits();

		if (searchHits.getHits() == null || searchHits.getHits().length == 0) {
			return null;
		}

		Updown updown = null;

		updown = JSONObject.parseObject(
				searchHits.getHits()[0].getSourceAsString(), Updown.class);
		updown.setTableFlx((String) searchHits.getHits()[0].getSource().get("table_flx"));

		return updown;
	}

	private SearchResponse response(QueryBuilder queryBuilder,
			GeoDistanceSortBuilder sortBuilder, String index, String type) {
		SearchResponse searchResponse = esClient.prepareSearch(index)
				.setTypes(type).setQuery(queryBuilder).addSort(sortBuilder)
				.execute().actionGet();

		return searchResponse;
	}

	private GeoDistanceSortBuilder sortBuilder(double lon, double lat) {
		GeoDistanceSortBuilder sortBuilder = SortBuilders
				.geoDistanceSort("location").point(lat, lon)
				.unit(DistanceUnit.KILOMETERS).order(SortOrder.ASC);
		return sortBuilder;
	}

	private QueryBuilder queryBuilder(double lon, double lat, long distance) {
		GeoDistanceFilterBuilder geoFilterBuilder = FilterBuilders
				.geoDistanceFilter("location").point(lat, lon)
				.distance(distance, DistanceUnit.KILOMETERS);
		return new FilteredQueryBuilder(new MatchAllQueryBuilder(),
				geoFilterBuilder);

	}

}
