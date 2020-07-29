package com.dms.ptp.response;

import java.util.List;

/**
 * ChannelData Class
 */
public class ChannelData {

    private int id;
    private String name;
    private String tag;
    private String no;
    private List<String> genre;
    private String logo;

    public ChannelData(int id, String name, List<String> genre, String logo) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
