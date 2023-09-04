package com.chunlei.bili.search.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.ConnectionPoolDataSource;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MahoutConfig {

    @Bean
    public DataModel dataModel() throws TasteException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("kirito486");
        dataSource.setDatabaseName("bili_searchdb");

        //ConnectionPoolDataSource connectionPool=new ConnectionPoolDataSource(dataSource);

        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource,"member_video_preference","member_id","video_id","score","time");
        ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(dataModel);
        //开启守护线程刷新dataModel
        Thread thread = new Thread(()->{
            try {
                Thread.sleep(50*50*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            model.refresh(null);
        });
        thread.setDaemon(true);
        thread.start();

        return model;

    }
}
