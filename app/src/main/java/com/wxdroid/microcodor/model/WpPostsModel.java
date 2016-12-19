package com.wxdroid.microcodor.model;

import java.io.Serializable;

/**
 * Created by jinchun on 2016/12/2.
 */
//@DatabaseTable(tableName = "wp_posts")
public class WpPostsModel implements Serializable {


    /**
     * id : 1
     * TermId : 5
     * post_author : 1
     * post_date : 2016-03-30 02:05:40
     * post_title : wordpress-开源Blog
     * post_content : 一.域名配置的问题
     * comment_status : open
     * guid : http://www.wxdroid.com/wp/?p=1
     * comment_count : 0,
     * views_count : 18,
     * user:{}
     */

    private long id;
    private long term_id;
    private long post_author;
    private String post_date;
    private String post_title;
    private String post_content;
    private String comment_status;
    private String guid;
    private int comment_count;
    private int views_count;

    private WpUser user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTerm_id() {
        return term_id;
    }

    public void setTerm_id(long term_id) {
        this.term_id = term_id;
    }

    public long getPost_author() {
        return post_author;
    }

    public void setPost_author(long post_author) {
        this.post_author = post_author;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public WpUser getUser() {
        return user;
    }

    public void setUser(WpUser user) {
        this.user = user;
    }

    public int getViews_count() {
        return views_count;
    }

    public void setViews_count(int views_count) {
        this.views_count = views_count;
    }
}
