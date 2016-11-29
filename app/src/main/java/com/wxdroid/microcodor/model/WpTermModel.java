package com.wxdroid.microcodor.model;

/**
 * Created by jinchun on 2016/11/29.
 */

public class WpTermModel {
    /**
     * term_id : 1
     * name : 公众号
     * slug : wx_wechat
     * term_group : 0
     */

    private String term_id;
    private String name;
    private String slug;
    private String term_group;

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
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

    public String getTerm_group() {
        return term_group;
    }

    public void setTerm_group(String term_group) {
        this.term_group = term_group;
    }
}
