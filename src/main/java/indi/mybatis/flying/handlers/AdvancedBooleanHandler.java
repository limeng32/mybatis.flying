package indi.mybatis.flying.handlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * 
 * @date 2024-03-09 21:50:08
 *
 * @author 李萌
 * @email limeng32@live.cn
 * @since JDK 1.8
 * @description Specify the Advanced BooleanTypeHandler of MappedTypes and
 *              MappedJdbcTypes.
 */
@SuppressWarnings("unused")
@MappedTypes({ Boolean.class })
public class AdvancedBooleanHandler extends BaseTypeHandler<Boolean> implements TypeHandler<Boolean> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType)
            throws SQLException {
        if (parameter) {
            ps.setInt(i, 1);
        } else {
            ps.setInt(i, 0);
        }
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName) == null ? null : rs.getInt(columnName) > 0;
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex) == null ? null : rs.getInt(columnIndex) > 0;
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return cs.getString(columnIndex) == null ? null : cs.getInt(columnIndex) > 0;
    }

}
