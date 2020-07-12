package org.selftest.interview.transaction.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.selftest.interview.transaction.jdbc.SelfTransactionManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author guozhipeng
 * @date 2020/7/12 10:42
 */
@Aspect
@Component
public class TransactionSelfAspect {

    @Autowired
    SelfTransactionManage selfTransactionManage;

    @Around("@annotation(TransactionSelf)")
    public void doTransaction(ProceedingJoinPoint proceedingJoinPoint) throws SQLException {

        Connection connection = null;

        try {
            connection =selfTransactionManage.getConnection();
            //关闭自动提交
            connection.setAutoCommit(false);

            Object result = proceedingJoinPoint.proceed();

            System.out.println("正常提交");
            connection.commit();
        }catch (Throwable e){
            e.printStackTrace();
            System.out.println("异常回滚");
            connection.rollback();
        }

    }

}
