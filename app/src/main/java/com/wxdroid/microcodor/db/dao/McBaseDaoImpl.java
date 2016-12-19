package com.wxdroid.microcodor.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by like on 16/11/16.
 */

public class McBaseDaoImpl<T,D> extends McBaseDao<T,D> {
    private Class<T> clazz;
    private Map<Class<T>,Dao<T,D>> mDaoMap=new HashMap<Class<T>,Dao<T,D>>();
    //缓存泛型Dao
    public McBaseDaoImpl(Context context, Class<T> clazz, String appendName) {
        super(context,appendName);
        this.clazz=clazz;
    }

    @Override
    public Dao<T,D> getDao() throws SQLException {
        Dao<T,D> dao=mDaoMap.get(clazz);
        if (null==dao){
            dao=mDatabaseHelper.getDao(clazz);
            mDaoMap.put(clazz,dao);
        }
        return dao;
    }

}