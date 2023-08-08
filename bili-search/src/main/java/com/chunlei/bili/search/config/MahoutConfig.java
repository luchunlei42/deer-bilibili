package com.chunlei.bili.search.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MahoutConfig {

    @Bean
    public DataModel dataModel(){
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("kirito486");
        dataSource.setDatabaseName("bili_searchdb");

        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,"member_video_preference","member_id","video_id","score","time");
        return dataModel;

    }
}
