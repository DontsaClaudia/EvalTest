package fr.troisIl.evaluation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ProductServiceTest {

    private Database db = null;
    private ProductService productService;

    private int countBefore = 0;

    @Before
    public void setUp() throws SQLException {
        String testDatabaseFileName = "product.db";

        // reset la BDD avant le test
        File file = new File(testDatabaseFileName);
        file.delete();

        db = new Database(testDatabaseFileName);
        db.createBasicSqlTable();

        productService = new ProductService(db);

        countBefore = count();
    }

    /**
     * Compte les produits en BDD
     *
     * @return le nombre de produit en BDD
     */

    private int count() throws SQLException {
        ResultSet resultSet = db.executeSelect("Select count(*) from Product");
        assertNotNull(resultSet);
        return resultSet.getInt(1);
    }

    @Test
    public void testInsert() throws SQLException {

        Product product = new Product("savons", 2);
        Product createdProduct = productService.insert(product);
        assertNotNull(createdProduct.getId());
        assertEquals("savons", createdProduct.getLabel());
        assertEquals(2, createdProduct.getQuantity().intValue());


    }

    @Test
    public void testUpdate() throws SQLException {
    }

    @Test
    public void testFindById() throws SQLException {
    }

    @Test
    public void testDelete() throws SQLException {
        Product product = new Product("test1", 4);
        Product createdProduct = productService.insert(product);
        productService.delete(createdProduct.getId());
    }
    /*

    // lever exception si recherher un produit supprimmer
    @Test
    public void testSearchDelete() throws SQLException {
        Product product = new Product("test1", 4);
        Product createdProduct = productService.insert(product);
        assertThrows(RuntimeException.class, () -> productService.findById(createdProduct.getId()));
    }

    private void assertThrows(Class<RuntimeException> runtimeExceptionClass, Object o) {
    }
*/
    @Test(expected = RuntimeException.class)
    public void testDeleteNotFound() {
        // Supprimer un produit avec un identifiant non existant
        productService.delete(999);

    }

}
