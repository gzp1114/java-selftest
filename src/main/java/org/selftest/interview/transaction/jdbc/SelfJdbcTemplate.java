package org.selftest.interview.transaction.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author guozhipeng
 * @date 2020/7/11 17:37
 */
@Component
public class SelfJdbcTemplate {

    @Autowired
    SelfTransactionManage selfTransactionManage;

    public void execute(final String sql) throws SQLException {
        Connection connection = selfTransactionManage.getConnection();
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        statement.execute(sql);

    }

}
