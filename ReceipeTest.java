import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ReceipeTest {
    @Test
    public void testAddArticleIncreasesTotalPrice() {
        Receipe receipe = new Receipe();
        Article bier = new Bier();
        double oldTotal = receipe.getTotalPrice();
        receipe.addArticle(bier);
        assertEquals(oldTotal + bier.getPrice(), receipe.getTotalPrice(), 0.001);
    }

    @Test
    public void testRemoveArticleDecreasesTotalPrice() {
        Receipe receipe = new Receipe();
        Article bier = new Bier();
        receipe.addArticle(bier);
        double afterAdd = receipe.getTotalPrice();
        receipe.removeArticle(bier);
        assertEquals(afterAdd - bier.getPrice(), receipe.getTotalPrice(), 0.001);
    }

    @Test
    public void testArticleListReflectsAddedArticles() {
        Receipe receipe = new Receipe();
        Article bier = new Bier();
        receipe.addArticle(bier);
        List<Article> list = receipe.getArticleList();
        assertTrue(list.contains(bier));
    }
}
