package indi.mybatis.flying.models;

import java.util.HashMap;
import java.util.Map;

import indi.mybatis.flying.statics.ActionType;
import indi.mybatis.flying.statics.KeyGeneratorType;
import indi.mybatis.flying.type.KeyHandler;

public class FlyingModel {

	private String id;
	private boolean hasFlyingFeature;
	private ActionType actionType;
	private String ignoreTag;
	private KeyGeneratorType keyGeneratorType;
	private KeyHandler keyHandler;
	private String dataSourceId;
	private String connectionCatalog;
	private String prefix;
	private transient String unstablePrefix;
	private Map<String, FlyingModel> properties = new HashMap<>(2);

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isHasFlyingFeature() {
		return hasFlyingFeature;
	}

	public void setHasFlyingFeature(boolean hasFlyingFeature) {
		this.hasFlyingFeature = hasFlyingFeature;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public String getIgnoreTag() {
		return ignoreTag;
	}

	public void setIgnoreTag(String ignoreTag) {
		this.ignoreTag = ignoreTag;
	}

	public KeyGeneratorType getKeyGeneratorType() {
		return keyGeneratorType;
	}

	public void setKeyGeneratorType(KeyGeneratorType keyGeneratorType) {
		this.keyGeneratorType = keyGeneratorType;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	public void setKeyHandler(KeyHandler keyHandler) {
		this.keyHandler = keyHandler;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getConnectionCatalog() {
		return connectionCatalog;
	}

	public void setConnectionCatalog(String connectionCatalog) {
		this.connectionCatalog = connectionCatalog;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUnstablePrefix() {
		return unstablePrefix;
	}

	public void setUnstablePrefix(String unstablePrefix) {
		this.unstablePrefix = unstablePrefix;
	}

	public Map<String, FlyingModel> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, FlyingModel> properties) {
		this.properties = properties;
	}

}