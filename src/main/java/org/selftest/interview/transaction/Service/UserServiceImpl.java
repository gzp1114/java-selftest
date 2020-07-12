package org.selftest.interview.transaction.Service;

import org.selftest.interview.transaction.annotation.TransactionSelf;
import org.selftest.interview.transaction.jdbc.SelfJdbcTemplate;
import org.selftest.interview.transaction.jdbc.SelfTransactionManage;
import org.selftest.interview.transaction.mapper.UserMapper;
import org.selftest.interview.transaction.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * @author guozhipeng
 * @date 2020/7/11 15:28
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    SelfJdbcTemplate selfJdbcTemplate;

    @Autowired
    SelfTransactionManage selfTransactionManage;

    @Override
//    @Transactional
    public void insertUser(User user) throws SQLException {
//        Connection connection =dataSource.getConnection();
//        Statement statement = connection.createStatement();
//        statement.execute("insert into user_info VALUES ("+user.getId()+",'"+user.getUserName()+"')");
//        statement.execute("insert into operate_log values("+ UUID.randomUUID().toString()+",'新增用户："+user.getId()+"') ");

//        jdbcTemplate.execute("insert into user_info VALUES ("+user.getId()+",'"+user.getUserName()+"')");
//        jdbcTemplate.execute("insert into operate_log values('新增用户："+user.getId()+"') ");

        Connection connection =selfTransactionManage.getConnection();

        try{
            selfJdbcTemplate.execute("insert into user_info VALUES ("+user.getId()+",'"+user.getUserName()+"')");
            selfJdbcTemplate.execute("insert into operate_log values('新增用户："+user.getId()+"') ");

            int i = 1/0;

            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
            connection.rollback();
        }



    }

    @Override
    public void deleteUser(Long userId) throws SQLException {

        Connection connection =dataSource.getConnection();
        //关闭自动提交
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        try {
            String userSql = "delete from user_info where id = "+userId;

            String logSql = "insert into operate_log values('"+ UUID.randomUUID().toString()+"', '删除用户："+userId+"') ";

            statement.execute(userSql);
            statement.execute(logSql);

//            int i = 1/0;
            System.out.println("操作没问题，提交");
            connection.commit();
        }catch (Exception e){
            System.out.println("出现异常，回滚");
            e.printStackTrace();
            connection.rollback();
        }

    }

    @Override
    @TransactionSelf
    public void deleteUser2(Long userId) throws SQLException {
        String userSql = "delete from user_info where id = "+userId;

        String logSql = "insert into operate_log values('"+ UUID.randomUUID().toString()+"', '删除用户："+userId+"') ";

        selfJdbcTemplate.execute(userSql);
        selfJdbcTemplate.execute(logSql);

        int i = 1/0;

    }
}
