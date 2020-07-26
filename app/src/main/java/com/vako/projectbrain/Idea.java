package com.vako.projectbrain;

import java.util.Date;

public class Idea {

    private String title;
    private String context;
    private String content;
    private Date createDate;

    public Idea(String title, String context, String content, Date createDate) {
        this.title = title;
        this.context = context;
        this.content = content;
        this.createDate = createDate;
    }

    public Idea() {

    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreateDate() { return createDate; }
    public void setCreateDate(Date createDate) { this.createDate = createDate; }

    @Override
    public String toString() {
        return "Idea{" +
                "title='" + title + '\'' +
                ", context='" + context + '\'' +
                ", content='" + content + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
