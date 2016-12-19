package com.wxdroid.microcodor.model.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.wxdroid.microcodor.db.dao.McBaseDaoImpl;
import com.wxdroid.microcodor.model.WpTermModel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jinchun on 2016/12/1.
 */

public class WpTermModelDao extends McBaseDaoImpl {
    public WpTermModelDao(Context context, Class clazz, String appendName) {
        super(context, clazz, appendName);
    }
    public WpTermModelDao(Context context) {
        super(context, WpTermModel.class, null);
    }

//    public List<WpTermModel> getList(){
//
//        try {
//            Dao<WpTermModel, String> dao = getDao();
//
//            QueryBuilder<WpTermModel, String> qb= dao.queryBuilder();
//            List<WpTermModel> list = qb.query();
//            if(list != null && list.size() > 0){
//                return list;
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
