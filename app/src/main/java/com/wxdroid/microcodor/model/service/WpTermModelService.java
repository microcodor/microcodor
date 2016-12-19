package com.wxdroid.microcodor.model.service;

import com.wxdroid.microcodor.app.MicroCodorApplication;
import com.wxdroid.microcodor.model.WpTermModel;
import com.wxdroid.microcodor.model.dao.WpTermModelDao;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by jinchun on 2016/12/1.
 */

public class WpTermModelService {


    /**
     * 保存、更新分类表
     */
    public static void saveOrUpdateWpTermModelsList(List<WpTermModel> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        WpTermModelDao dao = new WpTermModelDao(MicroCodorApplication.getInstance());
        try {
            dao.saveOrUpdate(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 查询所有
     * */
    public static List<WpTermModel> queryAllWpTermModelList(){
        WpTermModelDao dao = new WpTermModelDao(MicroCodorApplication.getInstance());
        List<WpTermModel> list = null;
        try {
            list = dao.queryAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


}
