//package org.selftest.interview.transaction.Service;
//
//import org.selftest.interview.transaction.annotation.TransactionSelf;
//import org.selftest.interview.transaction.jdbc.SelfJdbcTemplate;
//import org.selftest.interview.transaction.jdbc.SelfTransactionManage;
//import org.selftest.interview.transaction.mapper.UserMapper;
//import org.selftest.interview.transaction.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.TransactionDefinition;
//import org.springframework.transaction.TransactionStatus;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.UUID;
//
///**
// * @author guozhipeng
// * @date 2020/7/11 15:28
// */
//@Service
//public class UserService2Impl implements UserService {
//
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    DataSource dataSource;
//
//    @Autowired
//    SelfJdbcTemplate selfJdbcTemplate;
//
//    @Autowired
//    SelfTransactionManage selfTransactionManage;
//
//    @Autowired
//    DataSourceTransactionManager dataSourceTransactionManager;
//    @Autowired
//    TransactionDefinition transactionDefinition;
//
//
//    @Override
//    public void insertUser(User user) throws SQLException {
//        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);
//
//        try{
//
//
//            selfJdbcTemplate.execute("insert into user_info VALUES ("+user.getId()+",'"+user.getUserName()+"')");
//            selfJdbcTemplate.execute("insert into operate_log values('新增用户："+user.getId()+"') ");
//
//            int i = 1/0;
//
//            dataSourceTransactionManager.commit(transactionStatus);
//
//        }catch (Exception e){
//            e.printStackTrace();
//
//            dataSourceTransactionManager.rollback(transactionStatus);
//
//        }
//
//
//
//    }
//
//    @Override
//    @TransactionSelf
//    public void deleteUser(Long userId) throws SQLException {
//
//        try {
//            String userSql = "delete from user_info where id = "+userId;
//
//            String logSql = "insert into operate_log values('"+ UUID.randomUUID().toString()+"', '删除用户："+userId+"') ";
//
//            jdbcTemplate.execute(userSql);
//            jdbcTemplate.execute(logSql);
//
//            int i = 1/0;
//        }catch (Exception e){
//            System.out.println("出现异常，回滚");
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void deleteUser2(Long userId) throws SQLException {
//
//    }
//}
