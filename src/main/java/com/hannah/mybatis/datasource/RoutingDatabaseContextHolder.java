package com.hannah.mybatis.datasource;

import com.hannah.mybatis.enumeration.DataSourceType;

import java.util.Objects;


/**
 * 사용할 Datasource를 ThreadLocal에 저장
 */
public class RoutingDatabaseContextHolder {
    private static ThreadLocal<DataSourceType> CONTEXT = new ThreadLocal<>();

    public static void set(DataSourceType dataSourceType) {
        Objects.requireNonNull(dataSourceType);
        CONTEXT.set(dataSourceType);
    }

    public static DataSourceType getDataSourceType() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
