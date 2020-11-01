package indi.mybatis.flying.pojo;

import java.util.HashMap;
import java.util.Map;

public enum StoryStatus_ {

	SKETCH(StoryConfig.STATUS_SKETCH_VALUE, StoryConfig.STATUS_SKETCH_TEXT),
	PUBLISH(StoryConfig.STATUS_PUBLISH_VALUE, StoryConfig.STATUS_PUBLISH_TEXT),
	CANCEL(StoryConfig.STATUS_CANCEL_VALUE, StoryConfig.STATUS_CANCEL_TEXT);

	private final String text;

	private final String value;

	private StoryStatus_(String value, String text) {
		this.value = value;
		this.text = text;
	}

	public String text() {
		return text;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return text;
	}

	private static final Map<String, StoryStatus_> map = new HashMap<>();

	static {
		StoryStatus_[] array = StoryStatus_.values();
		for (StoryStatus_ e : array) {
			map.put(e.getValue(), e);
		}
	}

	public static StoryStatus_ forValue(String s) {
		return map.get(s);
	}
}
