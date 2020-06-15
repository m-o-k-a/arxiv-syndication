package app.arxivorg.model;

import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AtomReaderTest {



    @Test
    void read() {
         QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addCategory("astro-ph");
        AtomReader atomReader = new AtomReader(queryBuilder.proceed());
        for (int i = 0; i < atomReader.getArticles().getArticles().size() ; i++) {
            assertTrue(atomReader.getArticles().getArticles().get(i) instanceof Article);
        }
        for (Article a: atomReader.getArticles().getArticles()) {
            assertTrue("astro-ph".equals(a.getPrincipaleCategory()) || a.getCategories().contains("astro-ph"));
        }
    }

    @Test
    void getContent() {
         QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addCategory("astro-ph");
        AtomReader atomReader = new AtomReader(queryBuilder.proceed());
        assertEquals(queryBuilder.proceed(), atomReader.getContent());
    }

    @Test
    void getArticles() {
        QueryBuilder queryBuilder = new QueryBuilder();
        queryBuilder.addCategory("astro-ph");
        AtomReader atomReader = new AtomReader(queryBuilder.proceed());
        assertTrue(atomReader.getArticles() instanceof Articles);

    }
}