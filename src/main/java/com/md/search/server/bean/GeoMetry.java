package com.md.search.server.bean;

import java.io.Serializable;

import org.elasticsearch.common.geo.builders.ShapeBuilder.GeoShapeType;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * 多边形
 * 
 * @author zhiwei.wen
 * @Date 2015年8月18日 上午10:56:12
 */
public class GeoMetry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 650503445651216416L;

	private GeoShapeType type;

	private Coordinate[] coordinates;

	public GeoShapeType getType() {
		return type;
	}

	public Coordinate[] getCoordinates() {
		return coordinates;
	}

	public void setType(GeoShapeType type) {
		this.type = type;
	}

	public void setCoordinates(Coordinate[] coordinates) {
		this.coordinates = coordinates;
	}

}
