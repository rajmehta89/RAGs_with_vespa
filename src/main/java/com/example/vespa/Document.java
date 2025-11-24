package com.example.vespa;

import java.util.List;

/**
 * Represents a document in the Vespa index
 */
public class Document {
    private String id;
    private String title;
    private String content;
    private String category;
    private List<Float> embedding;

    public Document() {
    }

    public Document(String id, String title, String content, String category, List<Float> embedding) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.category = category;
        this.embedding = embedding;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Float> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Float> embedding) {
        this.embedding = embedding;
    }
}

