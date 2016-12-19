package com.wxdroid.microcodor.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by jinchun on 2016/11/29.
 */
@DatabaseTable(tableName = "wp_terms")
public class WpTermModel implements Serializable{
    /**
     * term_id : 1
     * name : 公众号
     * slug : wx_wechat
     * term_group : 0
     */
    @DatabaseField(id = true)
    private long term_id;
    @DatabaseField
    private String name;
    @DatabaseField
    private String slug;
    @DatabaseField
    private long term_group;

    public long getTerm_id() {
        return term_id;
    }

    public void setTerm_id(long term_id) {
        this.term_id = term_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public long getTerm_group() {
        return term_group;
    }

    public void setTerm_group(long term_group) {
        this.term_group = term_group;
    }
}
