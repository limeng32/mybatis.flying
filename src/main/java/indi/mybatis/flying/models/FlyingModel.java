package indi.mybatis.flying.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import indi.mybatis.flying.statics.ActionType;
import indi.mybatis.flying.statics.KeyGeneratorType;
import indi.mybatis.flying.type.KeyHandler;

/**
 * 
 * @date 2019年12月18日 11:56:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 */
public class FlyingModel {

	private String id;
	private boolean hasFlyingFeature;
	private ActionType actionType;
	private String index;
	private String ignoreTag;
	private String whiteListTag;
	private KeyGeneratorType keyGeneratorType;
	private KeyHandler keyHandler;
	private String prefix;
	private String unstablePrefix;
	private Map<String, FlyingModel> properties = new HashMap<>(2);
	private Map<String, Set<AggregateModel>> aggregate = new HashMap<>(2);
	private Set<String> groupBy = new HashSet<>(2);

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

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getIgnoreTag() {
		return ignoreTag;
	}

	public void setIgnoreTag(String ignoreTag) {
		this.ignoreTag = ignoreTag;
	}

	public String getWhiteListTag() {
		return whiteListTag;
	}

	public void setWhiteListTag(String whiteListTag) {
		this.whiteListTag = whiteListTag;
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

	public Map<String, Set<AggregateModel>> getAggregate() {
		return aggregate;
	}

	public void setAggregate(Map<String, Set<AggregateModel>> aggregate) {
		this.aggregate = aggregate;
	}

	public Set<String> getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(Set<String> groupBy) {
		this.groupBy = groupBy;
	}

}