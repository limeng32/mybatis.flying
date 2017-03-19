package indi.mybatis.flying.pojo;

public enum StoryStatus_ {

	s(StoryConfig.STATUS_SKETCH_TEXT), p(StoryConfig.STATUS_PUBLISH_TEXT), c(StoryConfig.STATUS_CANCEL_TEXT);

	private final String text;

	private StoryStatus_(String text) {
		this.text = text;
	}

	public String text() {
		return text;
	}

	@Override
	public String toString() {
		return text;
	}
}
