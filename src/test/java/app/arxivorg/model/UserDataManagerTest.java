package app.arxivorg.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDataManagerTest {

    @Test
    void setPrefTimeId() {
        UserDataManager userDataManager = new UserDataManager();
        userDataManager.setPrefTimeId(1);
        assertTrue(userDataManager.getPrefTimeId() == 1);
    }

    @Test
    void setPrefMaxArticle() {
        UserDataManager userDataManager = new UserDataManager();
        userDataManager.setPrefMaxArticle(-42);
        assertFalse(userDataManager.getPrefMaxArticle() == -42);
    }
}