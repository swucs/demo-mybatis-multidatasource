package com.hannah.mybatis.datasource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Slf4j
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        log.info("determineCurrentLookupKey : {}", RoutingDatabaseContextHolder.getDataSourceType());
        return RoutingDatabaseContextHolder.getDataSourceType();
    }
}
