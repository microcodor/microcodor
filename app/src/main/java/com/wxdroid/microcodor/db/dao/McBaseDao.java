package com.wxdroid.microcodor.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.PreparedUpdate;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.support.DatabaseConnection;
import com.wxdroid.microcodor.db.DatabaseHelper;
import com.wxdroid.microcodor.db.config.DBConfig;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by like on 16/11/16.
 */

public abstract class McBaseDao<T, D> {
    protected DatabaseHelper mDatabaseHelper;

    //helper
    protected Context mContext;

    //上下文
    public McBaseDao(Context context, String appendName) {
        if (context == null) {
            throw new IllegalArgumentException("Context can't be null!");
            //如果为空，则扔出非法参数异常
        }
        mContext = context.getApplicationContext();
        //获得单例helper
        mDatabaseHelper = DatabaseHelper.getDBHelper(context, DBConfig.getDatabaseName(appendName));
    }

    /**
     * 抽象方法，重写提供Dao,在BaseDaoImpl里提供了简单的泛型实现，传递实体类Class即可
     *
     * @return Dao类
     * @throws SQLException SQLException异常
     */
    public abstract Dao<T, D> getDao() throws SQLException;

    /**
     * 增，带事务操作
     *
     * @param t 泛型实体类
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int save(T t) throws SQLException {
        Dao<T, D> dao = getDao();

        try {
            int save = dao.create(t);
            return save;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 增或更新，带事务操作
     * @param t 泛型实体类
     * @return Dao.CreateOrUpdateStatus
     * @throws SQLException SQLException异常
     */
    public Dao.CreateOrUpdateStatus saveOrUpdate(T t) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            Dao.CreateOrUpdateStatus orUpdate = dao.createOrUpdate(t);
            return orUpdate;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增或更新，带事务操作
     * @param t 泛型实体类
     * @return Dao.CreateOrUpdateStatus
     * @throws SQLException SQLException异常
     */
    public void saveOrUpdate(List<T> t) throws SQLException {
        Dao<T, D> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            databaseConnection.setSavePoint (t.getClass().getName());
            for (T item : t) {
                dao.createOrUpdate(item);
            }

            dao.commit(databaseConnection);
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.setAutoCommit(databaseConnection, true);
            dao.endThreadConnection(databaseConnection);
        }
    }

    /**
     * 增，带事务操作
     * @param t 泛型实体类集合
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int save(List<T> t) throws SQLException {
        Dao<T, D> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {

            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            databaseConnection.setSavePoint (t.getClass().getName());
            for (T item : t) {
                dao.create(item);
            }
            dao.commit(databaseConnection);
            return t.size();
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.setAutoCommit(databaseConnection, true);
            dao.endThreadConnection(databaseConnection);
        }
        return 0;
    }

    /**
     * 删，带事务操作
     *
     * @param t 泛型实体类
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int delete(T t) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            int delete = dao.delete(t);
            return delete;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删，带事务操作
     *
     * @param list 泛型实体类集合
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int delete(List<T> list) throws SQLException {
        Dao<T, D> dao = getDao();
        DatabaseConnection databaseConnection = null;
        try {
            databaseConnection = dao.startThreadConnection();
            dao.setAutoCommit(databaseConnection, false);
            int delete = dao.delete(list);
            dao.commit(databaseConnection);
            return delete;
        } catch (SQLException e) {
            dao.rollBack(databaseConnection);
            e.printStackTrace();
        } finally {
            dao.setAutoCommit(databaseConnection, true);
            dao.endThreadConnection(databaseConnection);
        }
        return 0;
    }

    /**
     * 删，带事务操作
     *
     * @param columnNames  列名数组
     * @param columnValues 列名对应值数组
     * @return 影响的行数
     * @throws SQLException              SQLException异常
     * @throws InvalidParameterException InvalidParameterException异常
     */
    public int delete(String[] columnNames, Object[] columnValues) throws SQLException, InvalidParameterException {
        List<T> list = query(columnNames, columnValues);
        if (null != list && !list.isEmpty()) {
            Dao<T, D> dao = getDao();
            try {
                int delete = dao.delete(list);
                return delete;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 删，带事务操作
     *
     * @param id id值
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int deleteById(D id) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            int delete = dao.deleteById(id);
            return delete;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 删，带事务操作
     * @param ids id集合
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int deleteByIds(List<D> ids) throws SQLException {
        Dao<T, D> dao = getDao();

        try {
            int delete = dao.deleteIds(ids);
            return delete;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 删，带事务操作
     *
     * @param preparedDelete PreparedDelete类
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int delete(PreparedDelete<T> preparedDelete) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            int delete = dao.delete(preparedDelete);
            return delete;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 改，带事务操作
     *
     * @param t 泛型实体类
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int update(T t) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            int update = dao.update(t);
            return update;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 改，带事务操作
     * @param preparedUpdate PreparedUpdate对象
     * @return 影响的行数
     * @throws SQLException SQLException异常
     */
    public int update(PreparedUpdate<T> preparedUpdate) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            int update = dao.update(preparedUpdate);
            return update;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    /**
     * 查，带事务操作
     *
     * @return 查询结果集合
     * @throws SQLException SQLException异常
     */
    public List<T> queryAll() throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            List<T> query = dao.queryForAll();
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查，带事务操作
     *
     * @param preparedQuery PreparedQuery对象
     * @return 查询结果集合
     * @throws SQLException SQLException异常
     */
    public List<T> query(PreparedQuery<T> preparedQuery) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            List<T> query = dao.query(preparedQuery);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查，带事务操作
     *
     * @param columnName  列名
     * @param columnValue 列名对应值
     * @return 查询结果集合
     * @throws SQLException SQLException异常
     */
    public List<T> query(String columnName, String columnValue) throws SQLException {
        QueryBuilder<T, D> queryBuilder = getDao().queryBuilder();
        queryBuilder.where().eq(columnName, columnValue);
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
        Dao<T, D> dao = getDao();
        try {
            List<T> query = dao.query(preparedQuery);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查，带事务操作
     *
     * @param columnNames
     * @param columnValues
     * @return 查询结果集合
     * @throws SQLException SQLException异常
     */
    public List<T> query(String[] columnNames, Object[] columnValues) throws SQLException {
        if (columnNames.length != columnNames.length) {
            throw new InvalidParameterException("params size is not equal");
        }
        QueryBuilder<T, D> queryBuilder = getDao().queryBuilder();
        Where<T, D> wheres = queryBuilder.where();
        for (int i = 0; i < columnNames.length; i++) {
            if (i==0){
                wheres.eq(columnNames[i], columnValues[i]);
            }else{
                wheres.and().eq(columnNames[i], columnValues[i]);
            }

        }
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();

        Dao<T, D> dao = getDao();
        try {
            List<T> query = dao.query(preparedQuery);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查，带事务操作
     *
     * @param map 列名与值组成的map
     * @return 查询结果集合
     * @throws SQLException SQLException异常
     */
    public List<T> query(Map<String, Object> map) throws SQLException {
        QueryBuilder<T, D> queryBuilder = getDao().queryBuilder();
        if (!map.isEmpty()) {
            Where<T, D> wheres = queryBuilder.where();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            String key = null;
            Object value = null;
            for (int i = 0; iterator.hasNext(); i++) {
                Map.Entry<String, Object> next = iterator.next();
                key = next.getKey();
                value = next.getValue();
                if (i == 0) {
                    wheres.eq(key, value);
                } else {
                    wheres.and().eq(key, value);
                }
            }
        }
        PreparedQuery<T> preparedQuery = queryBuilder.prepare();
        Dao<T, D> dao = getDao();
        try {
            List<T> query = dao.query(preparedQuery);
            return query;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查，带事务操作
     *
     * @param id id值
     * @return 查询结果集合
     * @throws SQLException SQLException异常
     */
    public T queryById(D id) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            T t = dao.queryForId(id);
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断表是否存在
     *
     * @return 表是否存在
     * @throws SQLException SQLException异常
     */
    public boolean isTableExists() throws SQLException {
        return getDao().isTableExists();
    }


    /**
     * 获得记录数
     *
     * @return 记录数
     * @throws SQLException SQLException异常
     */
    public long count() throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            long count = dao.countOf();
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获得记录数
     *
     * @param preparedQuery PreparedQuery类
     * @return 记录数
     * @throws SQLException SQLException异常
     */
    public long count(PreparedQuery<T> preparedQuery) throws SQLException {
        Dao<T, D> dao = getDao();
        try {
            long count = dao.countOf(preparedQuery);
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
