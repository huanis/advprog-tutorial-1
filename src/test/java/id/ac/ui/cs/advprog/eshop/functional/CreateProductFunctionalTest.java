package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {
    /**
     * The port number assigned to the running application during test execution.
     * Set automatically during each test run by Spring Framework's test context.
     * */
    @LocalServerPort
    private int serverPort;

    /**
     * The base URL for testing. Default to {@code http://localhost}.
     * */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d/product/create", testBaseUrl, serverPort);
    }


    @Test
    void pageTitle_isCorrect(ChromeDriver driver) throws  Exception {
        // Exercise
        driver.get(baseUrl);
        String pageTitle = driver.getTitle();

        // Verify
        assertEquals("Create New Product", pageTitle);
    }

    @Test
    void pageHeader_isCorrect(ChromeDriver driver) throws  Exception {
        // Exercise
        driver.get(baseUrl);
        String pageHeader = driver.findElement(By.tagName("h3"))
                .getText();

        // Verify
        assertEquals("Create New Product", pageHeader);
    }

    @Test
    void createProduct_isCorrect(ChromeDriver driver) throws  Exception {
        // Exercise
        driver.get(baseUrl);

        // Clear field to empty it from any previous data
        WebElement inputName = driver.findElement(By.id("nameInput"));
        inputName.clear();
        //Enter Text
        String name="product";
        inputName.sendKeys(name);

        // Clear field to empty it from any previous data
        WebElement inputQuantity = driver.findElement(By.id("nameInput"));
        inputQuantity.clear();
        //Enter Text
        String quantity = "100";
        inputQuantity.sendKeys(quantity);

        WebElement submitButton = driver.findElement(By.cssSelector(".btn.btn-primary"));
        submitButton.click();

        String pageTitle = driver.getTitle();

        assertEquals("Product List", pageTitle);

    }



}
