package indi.mybatis.flying.test;

import org.junit.Assert;
import org.junit.Test;

import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.statics.ActionType;
import indi.mybatis.flying.utils.CookOriginalSql;

public class CookOriginalSqlTest {

	@Test
	public void testCook1() {
		String sql = "flying?:select:noPassword";
		FlyingModel flyingModel = CookOriginalSql.fetchFlyingFeature(sql);
		Assert.assertTrue(flyingModel.isHasFlyingFeature());
		Assert.assertEquals(ActionType.select, flyingModel.getActionType());
		Assert.assertEquals("noPassword", flyingModel.getIgnoreTag());
	}

	@Test
	public void testCook2() {
		String sql = "flying?:selectAll:noPassword";
		FlyingModel flyingModel = CookOriginalSql.fetchFlyingFeature(sql);
		Assert.assertTrue(flyingModel.isHasFlyingFeature());
		Assert.assertEquals(ActionType.selectAll, flyingModel.getActionType());
		Assert.assertEquals("noPassword", flyingModel.getIgnoreTag());
	}

}
