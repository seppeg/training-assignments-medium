package com.netflix.simianarmy.basic;

import com.netflix.simianarmy.MonkeyConfiguration;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConnectionConfiguration {
    private final String dbDriver;
    private final String dbUser;
    private final String dbPass;
    private final String dbUrl;

    /**
     */
    private DatabaseConnectionConfiguration(String dbDriver, String dbUser, String dbPass, String dbUrl) {
        this.dbDriver = dbDriver;
        this.dbUser = dbUser;
        this.dbPass = dbPass;
        this.dbUrl = dbUrl;
    }

    public HikariDataSource createDatasource(){
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPass);
        dataSource.setMaximumPoolSize(2);
        return dataSource;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public static DatabaseConnectionConfiguration createDatabaseConnnectionConfiguration(MonkeyConfiguration configuration){
        String dbDriver = configuration.getStr("simianarmy.recorder.db.driver");
        String dbUser = configuration.getStr("simianarmy.recorder.db.user");
        String dbPass = configuration.getStr("simianarmy.recorder.db.pass");
        String dbUrl = configuration.getStr("simianarmy.recorder.db.url");
        return new DatabaseConnectionConfiguration(dbDriver, dbUser, dbPass, dbUrl);
    }
}
