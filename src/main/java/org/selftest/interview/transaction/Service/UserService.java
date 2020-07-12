package org.selftest.interview.transaction.Service;

import org.selftest.interview.transaction.model.User;

import java.sql.SQLException;

/**
 * @author guozhipeng
 * @date 2020/7/11 15:27
 */
public interface UserService {

    public void insertUser(User user) throws SQLException;

    public void deleteUser(Long userId) throws SQLException;

    public void deleteUser2(Long userId) throws SQLException;

}
