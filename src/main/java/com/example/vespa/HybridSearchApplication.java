package com.example.vespa;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Main application demonstrating Vespa hybrid search capabilities
 * 
 * This application shows how to:
 * 1. Index documents with both text and embeddings
 * 2. Perform hybrid search (combining keyword and semantic search)
 * 3. Compare results from keyword-only, semantic-only, and hybrid search
 */
public class HybridSearchApplication {
    
    public static void main(String[] args) {
        VespaClient client = new VespaClient();
        
        try {
            System.out.println("=== Vespa Hybrid Search Demo ===\n");
            
            // Step 1: Index sample documents
            System.out.println("Step 1: Indexing sample documents...");
            indexSampleDocuments(client);
            System.out.println("Documents indexed successfully!\n");
            
            // Wait a moment for indexing to complete
            Thread.sleep(2000);
            
            // Step 2: Perform different types of searches
            String searchQuery = "machine learning algorithms";
            List<Float> queryEmbedding = EmbeddingGenerator.generateEmbedding(searchQuery);
            
            System.out.println("Step 2: Performing searches for query: \"" + searchQuery + "\"\n");
            
            // Hybrid search (combines keyword + semantic)
            System.out.println("--- Hybrid Search Results (Keyword + Semantic) ---");
            List<SearchResult> hybridResults = client.hybridSearch(searchQuery, queryEmbedding, 5);
            printResults(hybridResults);
            
            System.out.println("\n--- Keyword-Only Search Results ---");
            List<SearchResult> keywordResults = client.keywordSearch(searchQuery, 5);
            printResults(keywordResults);
            
            System.out.println("\n--- Semantic-Only Search Results ---");
            List<SearchResult> semanticResults = client.semanticSearch(queryEmbedding, 5);
            printResults(semanticResults);
            
            // Step 3: Demonstrate another search
            System.out.println("\n\n=== Another Search Example ===\n");
            String query2 = "data science";
            List<Float> embedding2 = EmbeddingGenerator.generateEmbedding(query2);
            
            System.out.println("Query: \"" + query2 + "\"");
            System.out.println("\n--- Hybrid Search Results ---");
            List<SearchResult> results2 = client.hybridSearch(query2, embedding2, 5);
            printResults(results2);
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                System.err.println("Error closing client: " + e.getMessage());
            }
        }
    }
    
    private static void indexSampleDocuments(VespaClient client) throws IOException {
        // Sample documents about technology topics
        List<Document> documents = Arrays.asList(
            new Document(
                "doc1",
                "Introduction to Machine Learning",
                "Machine learning is a subset of artificial intelligence that enables systems to learn and improve from experience without being explicitly programmed. It uses algorithms to analyze data, identify patterns, and make predictions.",
                "AI/ML",
                EmbeddingGenerator.generateEmbedding("Introduction to Machine Learning Machine learning is a subset of artificial intelligence")
            ),
            new Document(
                "doc2",
                "Deep Learning Fundamentals",
                "Deep learning uses neural networks with multiple layers to model and understand complex patterns in data. It has revolutionized fields like computer vision, natural language processing, and speech recognition.",
                "AI/ML",
                EmbeddingGenerator.generateEmbedding("Deep Learning Fundamentals Deep learning uses neural networks with multiple layers")
            ),
            new Document(
                "doc3",
                "Data Science Best Practices",
                "Data science combines statistics, programming, and domain expertise to extract insights from data. Key practices include proper data cleaning, feature engineering, and model validation.",
                "Data Science",
                EmbeddingGenerator.generateEmbedding("Data Science Best Practices Data science combines statistics programming domain expertise")
            ),
            new Document(
                "doc4",
                "Python for Data Analysis",
                "Python is the most popular language for data analysis due to libraries like pandas, numpy, and scikit-learn. It provides powerful tools for data manipulation, analysis, and machine learning.",
                "Programming",
                EmbeddingGenerator.generateEmbedding("Python for Data Analysis Python is the most popular language for data analysis")
            ),
            new Document(
                "doc5",
                "Neural Networks Explained",
                "Neural networks are computing systems inspired by biological neural networks. They consist of interconnected nodes (neurons) that process information and can learn complex patterns through training.",
                "AI/ML",
                EmbeddingGenerator.generateEmbedding("Neural Networks Explained Neural networks are computing systems inspired by biological")
            ),
            new Document(
                "doc6",
                "Big Data Technologies",
                "Big data technologies like Hadoop, Spark, and Kafka enable processing of massive datasets. These tools are essential for handling data at scale in modern applications.",
                "Big Data",
                EmbeddingGenerator.generateEmbedding("Big Data Technologies Big data technologies like Hadoop Spark Kafka enable processing")
            ),
            new Document(
                "doc7",
                "Natural Language Processing",
                "NLP enables computers to understand, interpret, and generate human language. Applications include chatbots, translation services, and sentiment analysis.",
                "AI/ML",
                EmbeddingGenerator.generateEmbedding("Natural Language Processing NLP enables computers to understand interpret generate human language")
            ),
            new Document(
                "doc8",
                "Cloud Computing Architecture",
                "Cloud computing provides on-demand access to computing resources over the internet. Key models include Infrastructure as a Service, Platform as a Service, and Software as a Service.",
                "Cloud",
                EmbeddingGenerator.generateEmbedding("Cloud Computing Architecture Cloud computing provides on-demand access to computing resources")
            ),
            new Document(
                "doc9",
                "Computer Vision Applications",
                "Computer vision enables machines to interpret and understand visual information from the world. It powers applications like facial recognition, autonomous vehicles, medical image analysis, and augmented reality systems.",
                "AI/ML",
                EmbeddingGenerator.generateEmbedding("Computer Vision Applications Computer vision enables machines to interpret understand visual information")
            ),
            new Document(
                "doc10",
                "Reinforcement Learning Basics",
                "Reinforcement learning is a type of machine learning where agents learn to make decisions by interacting with an environment. It uses rewards and penalties to guide learning, making it ideal for game playing, robotics, and autonomous systems.",
                "AI/ML",
                EmbeddingGenerator.generateEmbedding("Reinforcement Learning Basics Reinforcement learning agents learn decisions by interacting with environment")
            ),
            new Document(
                "doc11",
                "Database Design Principles",
                "Effective database design involves normalization, indexing strategies, and query optimization. Relational databases use SQL for structured data, while NoSQL databases handle unstructured and semi-structured data efficiently.",
                "Database",
                EmbeddingGenerator.generateEmbedding("Database Design Principles Effective database design normalization indexing query optimization")
            ),
            new Document(
                "doc12",
                "Modern Web Development",
                "Modern web development uses frameworks like React, Vue, and Angular for frontend, and Node.js, Django, or Spring Boot for backend. RESTful APIs and microservices architecture enable scalable and maintainable applications.",
                "Web Development",
                EmbeddingGenerator.generateEmbedding("Modern Web Development React Vue Angular Node.js Django Spring Boot RESTful APIs microservices")
            ),
            new Document(
                "doc13",
                "Cybersecurity Fundamentals",
                "Cybersecurity protects systems, networks, and data from digital attacks. Key practices include encryption, authentication, access control, and regular security audits. Common threats include malware, phishing, and DDoS attacks.",
                "Security",
                EmbeddingGenerator.generateEmbedding("Cybersecurity Fundamentals Cybersecurity protects systems networks data from digital attacks encryption authentication")
            ),
            new Document(
                "doc14",
                "Software Engineering Best Practices",
                "Good software engineering involves clean code principles, design patterns, version control with Git, automated testing, code reviews, and continuous integration. These practices improve code quality and team productivity.",
                "Software Engineering",
                EmbeddingGenerator.generateEmbedding("Software Engineering Best Practices clean code design patterns Git automated testing code reviews")
            ),
            new Document(
                "doc15",
                "Distributed Systems Architecture",
                "Distributed systems spread components across multiple machines to improve performance and reliability. Key concepts include load balancing, replication, consensus algorithms, and handling network partitions. Examples include distributed databases and microservices.",
                "Distributed Systems",
                EmbeddingGenerator.generateEmbedding("Distributed Systems Architecture components across multiple machines load balancing replication consensus algorithms")
            ),
            new Document(
                "doc16",
                "REST API Design Guidelines",
                "REST APIs follow principles like stateless communication, resource-based URLs, HTTP methods for operations, and JSON for data exchange. Good API design includes versioning, proper error handling, and comprehensive documentation.",
                "API Design",
                EmbeddingGenerator.generateEmbedding("REST API Design Guidelines stateless communication resource-based URLs HTTP methods JSON")
            ),
            new Document(
                "doc17",
                "Microservices Architecture",
                "Microservices break applications into small, independent services that communicate over networks. Benefits include scalability, technology diversity, and independent deployment. Challenges include service coordination and distributed data management.",
                "Architecture",
                EmbeddingGenerator.generateEmbedding("Microservices Architecture small independent services communicate over networks scalability independent deployment")
            ),
            new Document(
                "doc18",
                "DevOps and CI/CD Pipelines",
                "DevOps combines development and operations to streamline software delivery. CI/CD pipelines automate building, testing, and deployment processes. Tools like Jenkins, GitLab CI, and GitHub Actions enable continuous integration and delivery.",
                "DevOps",
                EmbeddingGenerator.generateEmbedding("DevOps CI/CD Pipelines automate building testing deployment Jenkins GitLab CI GitHub Actions")
            ),
            new Document(
                "doc19",
                "Containerization with Docker",
                "Docker containerizes applications with their dependencies, ensuring consistent environments across development, testing, and production. Containers are lightweight, portable, and can be orchestrated with Kubernetes for large-scale deployments.",
                "DevOps",
                EmbeddingGenerator.generateEmbedding("Containerization Docker containerizes applications dependencies consistent environments Kubernetes orchestration")
            ),
            new Document(
                "doc20",
                "GraphQL vs REST APIs",
                "GraphQL provides a flexible query language for APIs, allowing clients to request exactly the data they need. Unlike REST, GraphQL uses a single endpoint and enables efficient data fetching, reducing over-fetching and under-fetching problems.",
                "API Design",
                EmbeddingGenerator.generateEmbedding("GraphQL vs REST APIs flexible query language single endpoint efficient data fetching")
            ),
            new Document(
                "doc21",
                "Blockchain Technology Overview",
                "Blockchain is a distributed ledger technology that maintains a continuously growing list of records secured by cryptography. It enables decentralized systems, smart contracts, and cryptocurrencies like Bitcoin and Ethereum.",
                "Blockchain",
                EmbeddingGenerator.generateEmbedding("Blockchain Technology distributed ledger technology decentralized systems smart contracts cryptocurrencies")
            ),
            new Document(
                "doc22",
                "Agile Software Development",
                "Agile methodology emphasizes iterative development, collaboration, and responding to change. Practices include Scrum, Kanban, sprints, daily standups, and continuous feedback. Agile helps teams deliver value faster and adapt to changing requirements.",
                "Software Engineering",
                EmbeddingGenerator.generateEmbedding("Agile Software Development iterative development collaboration Scrum Kanban sprints continuous feedback")
            ),
            new Document(
                "doc23",
                "Machine Learning Model Evaluation",
                "Evaluating ML models requires metrics like accuracy, precision, recall, F1-score, and ROC-AUC. Cross-validation helps assess model performance. It's crucial to test on unseen data and avoid overfitting to training data.",
                "AI/ML",
                EmbeddingGenerator.generateEmbedding("Machine Learning Model Evaluation accuracy precision recall F1-score ROC-AUC cross-validation overfitting")
            ),
            new Document(
                "doc24",
                "Data Warehousing Concepts",
                "Data warehouses store historical data from multiple sources for business intelligence and analytics. They use ETL processes to extract, transform, and load data. Star and snowflake schemas organize data for efficient querying and reporting.",
                "Data Science",
                EmbeddingGenerator.generateEmbedding("Data Warehousing Concepts historical data business intelligence ETL processes star snowflake schemas")
            )
        );
        
        for (Document doc : documents) {
            client.indexDocument(doc);
        }
    }
    
    private static void printResults(List<SearchResult> results) {
        if (results.isEmpty()) {
            System.out.println("No results found.");
            return;
        }
        
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            System.out.println(String.format("%d. [Relevance: %.4f] %s - %s", 
                i + 1, 
                result.getRelevance(),
                result.getTitle(),
                result.getCategory()));
            System.out.println("   " + truncate(result.getContent(), 100));
            System.out.println();
        }
    }
    
    private static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength) + "...";
    }
}

