package indi.mybatis.flying.test;

import static indi.mybatis.flying.utils.FlyingManager.fetchFlyingFeature;

import org.junit.Assert;
import org.junit.Test;

import indi.mybatis.flying.models.FlyingModel;
import indi.mybatis.flying.statics.ActionType;
import indi.mybatis.flying.statics.KeyGeneratorType;

public class CookOriginalSqlTest {

	@Test
	public void testCook1() {
		String sql = "flying?:select:noPassword";
		FlyingModel flyingModel = fetchFlyingFeature(sql);
		Assert.assertTrue(flyingModel.isHasFlyingFeature());
		Assert.assertEquals(ActionType.SELECT, flyingModel.getActionType());
		Assert.assertEquals("noPassword", flyingModel.getIgnoreTag());
	}

	@Test
	public void testCook2() {
		String sql = "flying:selectAll(uuid):noPassword";
		FlyingModel flyingModel = fetchFlyingFeature(sql);
		Assert.assertTrue(flyingModel.isHasFlyingFeature());
		Assert.assertEquals(ActionType.SELECT_ALL, flyingModel.getActionType());
		Assert.assertEquals("noPassword", flyingModel.getIgnoreTag());
		Assert.assertNull(flyingModel.getKeyGeneratorType());
	}

	@Test
	public void testCook3() {
		String sql = "flying:insert(uuid):noPassword";
		FlyingModel flyingModel = fetchFlyingFeature(sql);
		Assert.assertTrue(flyingModel.isHasFlyingFeature());
		Assert.assertEquals(ActionType.INSERT, flyingModel.getActionType());
		Assert.assertEquals("noPassword", flyingModel.getIgnoreTag());
		Assert.assertEquals(KeyGeneratorType.uuid, flyingModel.getKeyGeneratorType());

		String sql2 = "flying:insert():";
		FlyingModel flyingModel2 = fetchFlyingFeature(sql2);
		Assert.assertTrue(flyingModel2.isHasFlyingFeature());
		Assert.assertEquals(ActionType.INSERT, flyingModel2.getActionType());
		Assert.assertEquals("", flyingModel2.getIgnoreTag());
		Assert.assertNull(flyingModel2.getKeyGeneratorType());

		String sql3 = "flying:insert(millisecond)";
		FlyingModel flyingModel3 = fetchFlyingFeature(sql3);
		Assert.assertTrue(flyingModel3.isHasFlyingFeature());
		Assert.assertEquals(ActionType.INSERT, flyingModel3.getActionType());
		Assert.assertNull(flyingModel3.getIgnoreTag());
		Assert.assertEquals(KeyGeneratorType.millisecond, flyingModel3.getKeyGeneratorType());
	}

	@Test
	public void testCook4() {
		String sql = "flying(datasource1:testdb):insert(uuid):noPassword";
		FlyingModel flyingModel = fetchFlyingFeature(sql);
		Assert.assertEquals("datasource1", flyingModel.getDataSourceId());
		Assert.assertEquals("testdb", flyingModel.getConnectionCatalog());
		Assert.assertTrue(flyingModel.isHasFlyingFeature());
		Assert.assertEquals(ActionType.INSERT, flyingModel.getActionType());
		Assert.assertEquals("noPassword", flyingModel.getIgnoreTag());
		Assert.assertEquals(KeyGeneratorType.uuid, flyingModel.getKeyGeneratorType());

		String sql2 = "flying?(datasource2):select:noPassword";
		FlyingModel flyingModel2 = fetchFlyingFeature(sql2);
		Assert.assertNull(flyingModel2.getDataSourceId());
		Assert.assertNull(flyingModel2.getConnectionCatalog());
		Assert.assertTrue(flyingModel2.isHasFlyingFeature());
		Assert.assertEquals(ActionType.SELECT, flyingModel2.getActionType());
		Assert.assertEquals("noPassword", flyingModel2.getIgnoreTag());
	}
}
