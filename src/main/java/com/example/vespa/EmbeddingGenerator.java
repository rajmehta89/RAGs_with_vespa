package com.example.vespa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simple embedding generator for demonstration purposes.
 * In production, you would use a proper embedding model (e.g., sentence-transformers, OpenAI embeddings)
 */
public class EmbeddingGenerator {
    private static final int EMBEDDING_DIMENSION = 384;
    private static final Random random = new Random(42); // Fixed seed for reproducibility

    /**
     * Generate a simple embedding vector for demonstration.
     * In production, use a proper embedding model like:
     * - sentence-transformers (all-MiniLM-L6-v2)
     * - OpenAI text-embedding-ada-002
     * - Cohere embeddings
     */
    public static List<Float> generateEmbedding(String text) {
        // Simple hash-based embedding for demo
        // In production, replace with actual embedding model
        List<Float> embedding = new ArrayList<>(EMBEDDING_DIMENSION);
        int hash = text.hashCode();
        
        for (int i = 0; i < EMBEDDING_DIMENSION; i++) {
            random.setSeed(hash + i);
            float value = (random.nextFloat() - 0.5f) * 2.0f;
            embedding.add(value);
        }
        
        // Normalize the vector
        return normalize(embedding);
    }

    private static List<Float> normalize(List<Float> vector) {
        double sumSquares = 0.0;
        for (Float value : vector) {
            sumSquares += value * value;
        }
        double magnitude = Math.sqrt(sumSquares);
        
        if (magnitude == 0) {
            return vector;
        }
        
        List<Float> normalized = new ArrayList<>(vector.size());
        for (Float value : vector) {
            normalized.add((float) (value / magnitude));
        }
        return normalized;
    }
}

