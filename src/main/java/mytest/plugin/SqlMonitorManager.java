/**
 *    Copyright ${license.git.copyrightYears} the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package mytest.plugin;

import lombok.extern.slf4j.Slf4j;
import mytest.Test;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * 数据库时延监控
 * Created by jianghengwei on 15/12/17.
 */
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
		@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class}),
		@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class,
				CacheKey.class, BoundSql.class})})
@Slf4j
public class SqlMonitorManager implements Interceptor {

	private static int MAX_ALLOW_TIME = 1000;

	private boolean sqlMonitor = true;

	private boolean sqlShow = false;

	private final static String UNKNOWN = "Unknown";

	//超过一定时间答应Error日志
	private int allowTime = MAX_ALLOW_TIME;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		if (!sqlMonitor) {
			return invocation.proceed();
		}

		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

		if (mappedStatement == null) {
			return invocation.proceed();
		}

		Object parameter = null;
		if (invocation.getArgs().length > 1) {
			parameter = invocation.getArgs()[1];
		}
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		Configuration configuration = mappedStatement.getConfiguration();
		String sqlId = mappedStatement.getId();

		String sql = null;

		if (sqlShow) {
			sql = showSql(configuration, boundSql);
		}

		Object returnValue = null;
		int resultCode = 200;
		long start = System.currentTimeMillis();

		try {
			returnValue = invocation.proceed();
		} catch (Exception e) {

			resultCode = -1;
			throw e;

		} finally {
			long end = System.currentTimeMillis();
			long time = end - start;

			if (time >= allowTime) {
				sql = (sql == null ? showSql(configuration, boundSql) : sql);
				log.error(sql + ",code=" + resultCode + ",sql消耗时间=" + time + " 毫秒");
			} else if (time > 30) {
				sql = (sql == null ? showSql(configuration, boundSql) : sql);
				log.warn(sql + ",code=" + resultCode + ",sql消耗时间=" + time + " 毫秒");
			} else {
				log.info(sqlId + ",code=" + resultCode + ",sql消耗时间=" + time + " 毫秒");
			}
		}
		return returnValue;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		if (properties == null) {
			return;
		}
		if (properties.containsKey("sql_monitor")) {
			String value = properties.getProperty("sql_monitor");
			if (Boolean.TRUE.toString().equals(value)) {
				this.sqlMonitor = true;
			}
		}
		if (properties.containsKey("sql_show")) {
			String value = properties.getProperty("sql_show");
			if (Boolean.TRUE.toString().equals(value)) {
				this.sqlShow = true;
			}
		}
		if (properties.containsKey("overtime_print_error")) {
			String value = properties.getProperty("overtime_print_error");

			if (value == null || "".equals(value.trim())) {
				allowTime = MAX_ALLOW_TIME;
			} else {
				try {
					allowTime = Integer.parseInt(value.trim());
				} catch (Exception ex) {
					allowTime = MAX_ALLOW_TIME;
				}
			}
		}
	}

	private String getParameterValue(Object obj) {

		String value = null;

		if (obj instanceof String) {

			value = "'" + obj.toString() + "'";

		} else if (obj instanceof Date) {

			DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
			value = "'" + formatter.format(new Date()) + "'";

		} else {
			if (obj != null) {
				value = obj.toString();
			} else {
				value = "";
			}
		}
		return value;
	}

	private String showSql(Configuration configuration, BoundSql boundSql) {

		try {

			Object parameterObject = boundSql.getParameterObject();
			List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
			String sql = boundSql.getSql().replaceAll("[\\s]+", " ");

			if (parameterMappings.size() > 0 && parameterObject != null) {

				TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();

				if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
					sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));

				} else {
					MetaObject metaObject = configuration.newMetaObject(parameterObject);
					for (ParameterMapping parameterMapping : parameterMappings) {
						String propertyName = parameterMapping.getProperty();
						if (metaObject.hasGetter(propertyName)) {
							Object obj = metaObject.getValue(propertyName);
							sql = sql.replaceFirst("\\?", getParameterValue(obj));
						} else if (boundSql.hasAdditionalParameter(propertyName)) {
							Object obj = boundSql.getAdditionalParameter(propertyName);
							sql = sql.replaceFirst("\\?", getParameterValue(obj));
						}
					}
				}
			}

			return sql;
		} catch (Throwable e) {
			return UNKNOWN;
		}
	}
}
