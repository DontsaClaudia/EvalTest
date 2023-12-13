package fr.troisIl.evaluation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.*;

import static org.junit.Assert.*;

public class DatabaseTest {

    private Database db = null;

    @Before
    public void setUp() {
        String testDatabaseFileName = "product.db";

        // reset la BDD avant le test
        File file = new File(testDatabaseFileName);
        file.delete();

        db = new Database(testDatabaseFileName);
    }

    @Test
    public void testDatabaseTable() {
        setUp();
        db.createBasicSqlTable();
        assertEquals(db.showTable(), "Product");
    }


    // Fermer la connexion à la base de données après les tests
    @After
    public  void setDown(){
        try{
            if (db != null)
            {
                Connection connection = db.establishConnections();
                if (connection != null && !connection.isClosed())
                {
                    connection.close();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }


    }

    @Test
    public void testGeneratePrepared() {
        // Utiliser une requête préparée pour insérer des données dans la table
        String insertSql = "INSERT INTO Product (label, quantity) VALUES (?, ?);";
        try (PreparedStatement preparedStatement = db.generatePrepared(insertSql)) {
            preparedStatement.setString(1, "PreparedProduct");
            preparedStatement.setInt(2, 3);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            fail("Exception inattendue: " + e.getMessage());
        }

        // Vérifier que les données ont été insérées avec succès
        String selectSql = "SELECT * FROM Product WHERE label = 'PreparedProduct';";
        try (Statement statement = db.establishConnections().createStatement();
             ResultSet resultSet = statement.executeQuery(selectSql)) {
            assertTrue(resultSet.next());
            assertEquals("PreparedProduct", resultSet.getString("label"));
            assertEquals(3, resultSet.getInt("quantity"));
        } catch (SQLException e) {
            fail("Exception inattendue: " + e.getMessage());
        }
    }


}
