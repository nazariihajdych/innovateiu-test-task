package ua.innovateiu.test;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc.
 * You can use in Memory collection for store data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {

    public final Map<String, Document> documentsStorage = new HashMap<>();
    private int idGenerator = 1;

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist
     *
     * @param document - document content and author data
     * @return saved document
     */

    public Document save(Document document) {
        if (document.getId() == null) {
            String id = String.valueOf(idGenerator);
            document.setId(id);
        }else if (documentsStorage.containsKey(document.getId())){
            if (documentsStorage.get(document.getId()).equals(document)) {
                return document;
            } else {
                Document existingDocument = documentsStorage.get(document.getId());
                existingDocument.setTitle(document.getTitle());
                existingDocument.setContent(document.getContent());
                existingDocument.setAuthor(document.getAuthor());
                existingDocument.setCreated(document.getCreated());
                return existingDocument;
            }
        }
        documentsStorage.put(document.getId(), document);
        idGenerator++;
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return documentsStorage.values().stream()
                .filter(document -> filterByTitlePrefixes(document, request.getTitlePrefixes()))
                .filter(document -> filterByContainsContents(document, request.getContainsContents()))
                .filter(document -> filterByAuthorIds(document, request.getAuthorIds()))
                .filter(document -> filterByCreatedFrom(document, request.getCreatedFrom()))
                .filter(document -> filterByCreatedTo(document, request.getCreatedTo()))
                .collect(Collectors.toList());
    }

    private boolean filterByTitlePrefixes(Document document, List<String> titlePrefixes) {
        if (titlePrefixes == null || titlePrefixes.isEmpty()) return true;
        for (String prefix : titlePrefixes) {
            if (document.getTitle().startsWith(prefix)) return true;
        }
        return false;
    }

    private boolean filterByContainsContents(Document document, List<String> containsContents) {
        if (containsContents == null || containsContents.isEmpty()) return true;
        for (String content : containsContents) {
            if (document.getContent().contains(content)) return true;
        }
        return false;
    }

    private boolean filterByAuthorIds(Document document, List<String> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) return true;
        return authorIds.contains(document.getAuthor().getId());
    }

    private boolean filterByCreatedFrom(Document document, Instant createdFrom) {
        if (createdFrom == null) return true;
        return document.getCreated().isAfter(createdFrom) || document.getCreated().equals(createdFrom);
    }

    private boolean filterByCreatedTo(Document document, Instant createdTo) {
        if (createdTo == null) return true;
        return document.getCreated().isBefore(createdTo) || document.getCreated().equals(createdTo);
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return Optional.ofNullable(documentsStorage.get(id));
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }
}
