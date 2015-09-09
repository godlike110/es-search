package com.md.search.server.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * es工具类
 * 
 * @author zhiwei.wen
 * @Date 2015年8月15日 上午10:26:00
 */
public class EsUtil {

	public static Logger logger = LoggerFactory.getLogger(EsUtil.class);

	/**
	 * 对象转换成map
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Map<String, Object> ConvertObjToMap(Object obj)
			throws IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		Map<String, Object> reMap = new HashMap<String, Object>();
		if (obj == null)
			return null;
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field f = obj.getClass().getDeclaredField(fields[i].getName());
			f.setAccessible(true);
			Object o = f.get(obj);
			reMap.put(fields[i].getName(), o);
		}
		return reMap;
	}

	public static XContentBuilder getInsertData(
			Map<String, Object> insertContentMap) throws IOException {
		XContentBuilder xContentBuilder = null;
		xContentBuilder = XContentFactory.jsonBuilder().startObject();

		Iterator<Entry<String, Object>> iterator = insertContentMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, Object> entry = iterator.next();
			String field = entry.getKey();
			Object values = entry.getValue();
			xContentBuilder = xContentBuilder.field(field, values);

		}
		xContentBuilder = xContentBuilder.endObject();
		return xContentBuilder;
	}

	public static void main(String args[]) throws IOException {
		// Coordinate coordinate = new Coordinate(116.2332432, 43.454646);
		// Coordinate[] coordinates =
		// geometryFactory.createPolygon(coordinates);
		// String a =
		// "{\"2d23f008231155f4372ca51dff98090e\":\"{\"node_length\":4,\"node_location\":[{\"w\":39.9649772560349,\"t\":116.41266643920852,\"lng\":116.412666,\"lat\":39.964977},{\"w\":39.96417139932448,\"t\":116.41558468261678,\"lng\":116.415585,\"lat\":39.964171},{\"w\":39.964089168513844,\"t\":116.41249477783163,\"lng\":116.412495,\"lat\":39.964089},{\"w\":39.964089168513844,\"t\":116.41249477783163,\"lng\":116.412495,\"lat\":39.964089}]}\",\"28bb695912fef6c974afa5ccac2c45d4\":\"{\"node_length\":6,\"node_location\":[{\"w\":39.963513550070196,\"t\":116.41251623550426,\"lng\":116.412516,\"lat\":39.963514},{\"w\":39.962921480329285,\"t\":116.41577780166631,\"lng\":116.415778,\"lat\":39.962921},{\"w\":39.96201691943611,\"t\":116.41298830429088,\"lng\":116.412988,\"lat\":39.962017},{\"w\":39.963414872136106,\"t\":116.41191542068498,\"lng\":116.411915,\"lat\":39.963415},{\"w\":39.963414872136106,\"t\":116.41191542068498,\"lng\":116.411915,\"lat\":39.963415},{\"w\":39.96333264041555,\"t\":116.41204416671758,\"lng\":116.412044,\"lat\":39.963333}]}\"}";
		// JSONObject json = JSONObject.parseObject(a);
		// String b =
		// "{\"type\" : \"polygon\",\"coordinates\" : [[ [100.0, 0.0], [101.0, 0.0], [101.0, 1.0], [100.0, 1.0], [100.0, 0.0] ]]}";
		// XContentParser parser = JsonXContent.jsonXContent.createParser(b);
		// GeoShapeFieldMapper geoShapeFieldMapper;
		// Shape polygonShape = ShapeBuilder.parse(parser).build();
		// JSONArray ja = JSONArray.parseArray(a);
		// System.out.println(ja);
	}

}
