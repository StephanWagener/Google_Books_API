package com.example.stephan.booklist;

public class Book {
    private String title;
    private String author;
    private String infoUrl;
    private String imageUrl;
    private String publisher;

    public Book(String title, String author, String infoUrl, String imageUrl, String publisher) {

        this.title = title;
        this.author = author;
        this.infoUrl = infoUrl;
        this.imageUrl = imageUrl;
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getInfoUrl() {
        return infoUrl;
    }

    public void setInfoUrl(String infoUrl) {
        this.infoUrl = infoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
