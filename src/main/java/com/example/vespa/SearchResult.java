package com.example.vespa;

/**
 * Represents a search result from Vespa
 */
public class SearchResult {
    private String id;
    private String title;
    private String content;
    private String category;
    private double relevance;

    public SearchResult() {
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

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    @Override
    public String toString() {
        return String.format("SearchResult{id='%s', title='%s', relevance=%.4f}", 
                id, title, relevance);
    }
}

