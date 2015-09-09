package com.md.search.server.factory;

import com.md.search.server.constant.ApplicationConstants;
import com.md.search.server.enums.EsClientType;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * es工厂
 * 
 * @author zhiwei.wen
 * @Date 2015年8月6日 下午5:43:21
 */
public class ElasticSearchClientFactory extends AbstractFactoryBean<Client> {

	private EsClientType typology;

	private String nodes;

	private Client esClient;

	private Node node;

	private String clusName;

	/**
	 * 
	 * @param typology
	 */
	public void setTypology(EsClientType typology) {
		this.typology = typology;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public void setClusName(String clusName) {
		this.clusName = clusName;
	}

	/**
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#getObjectType()
	 */
	@Override
	public Class<Client> getObjectType() {
		return Client.class;
	}

	/**
	 * @see org.springframework.beans.factory.config.AbstractFactoryBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}

	/**
	 * 创建客服端
	 */
	@Override
	protected Client createInstance() throws Exception {

		if (typology == null)
			throw new BeanCreationException("Error creating "
					+ Client.class.getName()
					+ ": 'typology' property is required. Between "
					+ EsClientType.values());

		switch (typology) {

		case local:
			NodeBuilder nodeBuilder = NodeBuilder.nodeBuilder();
			node = nodeBuilder.node();
			esClient = node.client();
			break;

		case remote:

			if (StringUtils.isBlank(nodes)) {
				throw new BeanCreationException(
						"Error creating "
								+ Client.class.getName()
								+ ": 'nodes' property is required if 'remote' typology is set");
			}
			Collection<InetSocketTransportAddress> addresses = fromNodes(nodes);
			Settings settings = ImmutableSettings.settingsBuilder()
					.put("client.transport.sniff", true)
					.put("cluster.name", clusName).build();
			esClient = new TransportClient(settings);
			for (InetSocketTransportAddress address : addresses) {
				((TransportClient) esClient).addTransportAddress(address);
			}
			break;

		}

		return esClient;

	}

	/**
	 * 获取所有节点
	 * 
	 * @param nodes
	 * @return
	 */
	protected Collection<InetSocketTransportAddress> fromNodes(String nodes) {
		String[] nodeArray = nodes.split(ApplicationConstants.ES_IP_SPLIT);
		List<InetSocketTransportAddress> addresses = new ArrayList<InetSocketTransportAddress>();
		for (String ipPortString : nodeArray) {
			String ip = ipPortString.split(":")[0];
			String port = ipPortString.split(":")[1];
			addresses.add(new InetSocketTransportAddress(ip, Integer
					.parseInt(port)));
		}
		return addresses;
	}

	/**
	 * 销毁
	 */
	@Override
	public void destroy() throws Exception {
		super.destroy();
		if (esClient != null) {
			esClient.close();
		}
		if (node != null) {
			node.close();
		}
	}

}
