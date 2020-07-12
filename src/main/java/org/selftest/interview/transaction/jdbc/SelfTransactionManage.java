package org.selftest.interview.transaction.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author guozhipeng
 * @date 2020/7/11 18:08
 */
@Component
public class SelfTransactionManage {

    private ThreadLocal<Connection> connection = new ThreadLocal<>();

//    private Connection connection;

    @Autowired
    DataSource dataSource;

    public Connection getConnection() throws SQLException {
        if(connection.get() == null){
            connection.set(dataSource.getConnection());
        }
        return connection.get();
    }

//    public Connection getConnection() throws SQLException {
//        if(connection == null){
//            connection = dataSource.getConnection();
//        }
//        return connection;
//    }

}
