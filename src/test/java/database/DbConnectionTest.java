package database;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DbConnectionTest {

    @Test
    public void testDbConnectionInit(){
        DbConnection.init();
        assertNotNull(DbConnection.em);
    }
}
