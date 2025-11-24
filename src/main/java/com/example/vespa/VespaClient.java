package com.example.vespa;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Client for interacting with Vespa search engine
 */
public class VespaClient {
    // Get Vespa endpoint from environment variable or use default
    private static final String VESPA_ENDPOINT = 
        System.getenv("VESPA_ENDPOINT") != null ? 
        System.getenv("VESPA_ENDPOINT") : "http://localhost:8080";
    private static final String DOCUMENT_API = VESPA_ENDPOINT + "/document/v1/doc/document/docid/";
    private static final String SEARCH_API = VESPA_ENDPOINT + "/search/";
    
    private final CloseableHttpClient httpClient;
    private final Gson gson;

    public VespaClient() {
        this.httpClient = HttpClients.createDefault();
        this.gson = new Gson();
    }

    /**
     * Index a document in Vespa
     */
    public void indexDocument(Document doc) throws IOException {
        JsonObject documentJson = new JsonObject();
        documentJson.addProperty("id", doc.getId());
        documentJson.addProperty("title", doc.getTitle());
        documentJson.addProperty("content", doc.getContent());
        documentJson.addProperty("category", doc.getCategory());
        
        // Convert embedding list to tensor format
        JsonObject embeddingTensor = new JsonObject();
        JsonArray values = new JsonArray();
        for (Float value : doc.getEmbedding()) {
            values.add(value);
        }
        embeddingTensor.add("values", values);
        documentJson.add("embedding", embeddingTensor);

        String url = DOCUMENT_API + doc.getId();
        HttpPost request = new HttpPost(url);
        request.setHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(documentJson.toString(), StandardCharsets.UTF_8));

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                System.out.println("Document indexed successfully: " + doc.getId());
            } else {
                String responseBody = EntityUtils.toString(response.getEntity());
                throw new IOException("Failed to index document. Status: " + statusCode + ", Response: " + responseBody);
            }
        }
    }

    /**
     * Perform hybrid search combining keyword and semantic search
     */
    public List<SearchResult> hybridSearch(String query, List<Float> queryEmbedding, int hits) throws IOException {
        // Build YQL query for hybrid search
        StringBuilder yql = new StringBuilder();
        yql.append("select * from sources * where ");
        yql.append("userQuery()");
        
        if (queryEmbedding != null && !queryEmbedding.isEmpty()) {
            // Add semantic search component
            yql.append(" | ");
            yql.append("({targetHits: ").append(hits).append("}nearestNeighbor(embedding,query_embedding))");
        }

        String url = SEARCH_API + "?yql=" + java.net.URLEncoder.encode(yql.toString(), StandardCharsets.UTF_8)
                + "&query=" + java.net.URLEncoder.encode(query, StandardCharsets.UTF_8)
                + "&ranking=hybrid"
                + "&hits=" + hits;

        // Add query embedding if provided
        if (queryEmbedding != null && !queryEmbedding.isEmpty()) {
            JsonObject embeddingTensor = new JsonObject();
            JsonArray values = new JsonArray();
            for (Float value : queryEmbedding) {
                values.add(value);
            }
            embeddingTensor.add("values", values);
            
            url += "&input.query(query_embedding)=" + java.net.URLEncoder.encode(
                embeddingTensor.toString(), StandardCharsets.UTF_8);
        }

        HttpGet request = new HttpGet(url);
        request.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Search failed. Status: " + response.getStatusLine().getStatusCode() 
                    + ", Response: " + responseBody);
            }

            return parseSearchResults(responseBody);
        }
    }

    /**
     * Perform keyword-only search
     */
    public List<SearchResult> keywordSearch(String query, int hits) throws IOException {
        String yql = "select * from sources * where userQuery()";
        String url = SEARCH_API + "?yql=" + java.net.URLEncoder.encode(yql, StandardCharsets.UTF_8)
                + "&query=" + java.net.URLEncoder.encode(query, StandardCharsets.UTF_8)
                + "&ranking=keyword"
                + "&hits=" + hits;

        HttpGet request = new HttpGet(url);
        request.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Search failed. Status: " + response.getStatusLine().getStatusCode() 
                    + ", Response: " + responseBody);
            }

            return parseSearchResults(responseBody);
        }
    }

    /**
     * Perform semantic-only search
     */
    public List<SearchResult> semanticSearch(List<Float> queryEmbedding, int hits) throws IOException {
        String yql = "select * from sources * where " +
                "{targetHits: " + hits + "}nearestNeighbor(embedding,query_embedding)";
        
        String url = SEARCH_API + "?yql=" + java.net.URLEncoder.encode(yql, StandardCharsets.UTF_8)
                + "&ranking=semantic"
                + "&hits=" + hits;

        JsonObject embeddingTensor = new JsonObject();
        JsonArray values = new JsonArray();
        for (Float value : queryEmbedding) {
            values.add(value);
        }
        embeddingTensor.add("values", values);
        
        url += "&input.query(query_embedding)=" + java.net.URLEncoder.encode(
            embeddingTensor.toString(), StandardCharsets.UTF_8);

        HttpGet request = new HttpGet(url);
        request.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse response = httpClient.execute(request)) {
            HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);
            
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Search failed. Status: " + response.getStatusLine().getStatusCode() 
                    + ", Response: " + responseBody);
            }

            return parseSearchResults(responseBody);
        }
    }

    private List<SearchResult> parseSearchResults(String responseBody) {
        List<SearchResult> results = new ArrayList<>();
        JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
        
        if (jsonResponse.has("root") && jsonResponse.getAsJsonObject("root").has("children")) {
            JsonArray children = jsonResponse.getAsJsonObject("root")
                    .getAsJsonArray("children");
            
            for (int i = 0; i < children.size(); i++) {
                JsonObject hit = children.get(i).getAsJsonObject();
                JsonObject fields = hit.getAsJsonObject("fields");
                
                SearchResult result = new SearchResult();
                if (fields.has("id")) {
                    result.setId(fields.get("id").getAsString());
                }
                if (fields.has("title")) {
                    result.setTitle(fields.get("title").getAsString());
                }
                if (fields.has("content")) {
                    result.setContent(fields.get("content").getAsString());
                }
                if (fields.has("category")) {
                    result.setCategory(fields.get("category").getAsString());
                }
                if (hit.has("relevance")) {
                    result.setRelevance(hit.get("relevance").getAsDouble());
                }
                
                results.add(result);
            }
        }
        
        return results;
    }

    public void close() throws IOException {
        httpClient.close();
    }
}

