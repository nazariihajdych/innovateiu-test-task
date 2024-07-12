package ua.innovateiu.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DocumentManagerTest {

    private DocumentManager documentManager;
    private DocumentManager.Document document;

    @BeforeEach
    void setUp() {
        documentManager = new DocumentManager();

        document = DocumentManager.Document.builder()
                .title("Test Title")
                .content("Test Content")
                .author(DocumentManager.Author.builder().id("1").name("Author One").build())
                .created(Instant.now())
                .build();
    }

    @Test
    void save_successful() {
        DocumentManager.Document savedDocument = documentManager.save(document);

        assertNotNull(savedDocument.getId());
        assertEquals(document.getTitle(), savedDocument.getTitle());
        assertEquals(document.getContent(), savedDocument.getContent());
        assertEquals(document.getAuthor(), savedDocument.getAuthor());
        assertEquals(document.getCreated(), savedDocument.getCreated());
    }

    @Test
    void saveIfIdNotNull_successful() {
        document.setId("2");
        DocumentManager.Document savedDocument = documentManager.save(document);

        assertNotNull(savedDocument.getId());
        assertEquals(document.getTitle(), savedDocument.getTitle());
        assertEquals(document.getContent(), savedDocument.getContent());
        assertEquals(document.getAuthor(), savedDocument.getAuthor());
        assertEquals(document.getCreated(), savedDocument.getCreated());
    }

    @Test
    void saveIfDocumentWithDuplicateId_successful() {
        DocumentManager.Document savedDocument = documentManager.save(document);
        document.setId("1");
        documentManager.save(document);

        assertNotNull(savedDocument.getId());
        assertEquals(document.getTitle(), savedDocument.getTitle());
        assertEquals(document.getContent(), savedDocument.getContent());
        assertEquals(document.getAuthor(), savedDocument.getAuthor());
        assertEquals(document.getCreated(), savedDocument.getCreated());
    }

    @Test
    void search_successful() {
        DocumentManager.Document savedDocument = documentManager.save(document);

        DocumentManager.SearchRequest searchRequest = DocumentManager.SearchRequest.builder()
                .titlePrefixes(List.of("Test"))
                .containsContents(List.of("Test"))
                .authorIds(List.of("1"))
                .build();
        List<DocumentManager.Document> documents = documentManager.search(searchRequest);

        assertNotNull(documents);
        assertEquals(documents.size(), 1);
        assertEquals(documents.get(0).getTitle(), savedDocument.getTitle());
    }

    @Test
    void findById() {
        DocumentManager.Document savedDocument = documentManager.save(document);

        Optional<DocumentManager.Document> byId = documentManager.findById(String.valueOf(1));

        assertTrue(byId.isPresent());
        assertEquals(savedDocument.getTitle(), byId.get().getTitle());
    }
}