package com.example.demo.entities;

public class Albums {
    private String title;
    private String id;
    private String image;
    private String releaseDate;

    public Albums(String id, String title, String imagine, String releaseDate){
        this.id = id;
        this.title = title;
        this.image = imagine;
        this.releaseDate = releaseDate;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Albums{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", image='" + image + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
