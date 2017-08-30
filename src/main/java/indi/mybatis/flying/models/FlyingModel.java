package indi.mybatis.flying.models;

import indi.mybatis.flying.statics.ActionType;

public class FlyingModel {
	private boolean hasFlyingFeature;
	private ActionType actionType;
	private String ignoreTag;

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
}