package com.example.demo.entities;

import java.util.List;

public class MusicBrainz {
    private String msid;
    private List<Albums> albums;
    private String name;
    private String description;

    public MusicBrainz(String name, String msid, String description, List<Albums> albums){
        this.name = name;
        this.msid = msid;
        this.description = description;
        this.albums = albums;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Albums> getAlbums() {
        return albums;
    }

    public void setMsid(String msid) {
        this.msid = msid;
    }

    public String getMsid() {
        return msid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MusicBrainz{" +
                "msid='" + msid + '\'' +
                ", albums=" + albums +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
