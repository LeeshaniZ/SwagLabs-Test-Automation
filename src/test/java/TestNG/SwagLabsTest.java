package TestNG;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * End-to-end checkout test for the public SauceDemo sample application.
 *
 * Run with: mvn test
 * Optional environment variables: SWAG_LABS_URL, SWAG_LABS_USERNAME,
 * SWAG_LABS_PASSWORD, and HEADLESS=true.
 */
public class SwagLabsTest {

    private static final String BASE_URL =
            System.getenv().getOrDefault("SWAG_LABS_URL", "https://www.saucedemo.com/");
    private static final String USERNAME =
            System.getenv().getOrDefault("SWAG_LABS_USERNAME", "standard_user");
    private static final String PASSWORD =
            System.getenv().getOrDefault("SWAG_LABS_PASSWORD", "secret_sauce");

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000");

        if ("true".equalsIgnoreCase(System.getenv("HEADLESS"))) {
            options.addArguments("--headless=new");
        }

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void userCanCompleteCheckout() throws IOException {
        // Sign in.
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='username']"))).sendKeys(USERNAME);
        driver.findElement(By.cssSelector("[data-test='password']")).sendKeys(PASSWORD);
        driver.findElement(By.cssSelector("[data-test='login-button']")).click();

        assertPageTitle("Products");

        // Add one product and verify the cart badge.
        wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("[data-test='add-to-cart-sauce-labs-backpack']"))).click();
        Assert.assertEquals(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector(".shopping_cart_badge"))).getText(),
                "1",
                "The cart should contain one item.");

        // Open the cart and verify its contents.
        driver.findElement(By.cssSelector(".shopping_cart_link")).click();
        assertPageTitle("Your Cart");
        Assert.assertEquals(
                driver.findElement(By.cssSelector(".cart_item .inventory_item_name")).getText(),
                "Sauce Labs Backpack");

        // Enter checkout information.
        driver.findElement(By.cssSelector("[data-test='checkout']")).click();
        assertPageTitle("Checkout: Your Information");

        driver.findElement(By.cssSelector("[data-test='firstName']")).sendKeys("Test");
        driver.findElement(By.cssSelector("[data-test='lastName']")).sendKeys("User");
        driver.findElement(By.cssSelector("[data-test='postalCode']")).sendKeys("12345");
        driver.findElement(By.cssSelector("[data-test='continue']")).click();

        assertPageTitle("Checkout: Overview");

        // Verify the order summary and calculate the displayed total.
        List<WebElement> cartItems = driver.findElements(By.cssSelector(".cart_item"));
        Assert.assertEquals(cartItems.size(), 1, "Checkout should contain exactly one item.");
        Assert.assertEquals(
                driver.findElement(By.cssSelector(".cart_item .inventory_item_name")).getText(),
                "Sauce Labs Backpack");

        double subtotal = amountFrom("[data-test='subtotal-label']");
        double tax = amountFrom("[data-test='tax-label']");
        double total = amountFrom("[data-test='total-label']");
        Assert.assertEquals(total, subtotal + tax, 0.01,
                "The displayed total should equal subtotal plus tax.");

        // Complete the order.
        driver.findElement(By.cssSelector("[data-test='finish']")).click();
        Assert.assertEquals(
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.cssSelector("[data-test='complete-header']"))).getText(),
                "Thank you for your order!");

        saveScreenshot("checkout-complete.png");
    }

    private void assertPageTitle(String expectedTitle) {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("[data-test='title']")));
        wait.until(ExpectedConditions.textToBePresentInElement(title, expectedTitle));
        Assert.assertEquals(title.getText(), expectedTitle,
                "The page title should match the expected checkout step.");
    }

    private double amountFrom(String selector) {
        String label = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(selector))).getText();
        String numericValue = label.replaceAll("[^0-9.]", "");
        return Double.parseDouble(numericValue);
    }

    private void saveScreenshot(String filename) throws IOException {
        Path screenshotDirectory = Path.of("target", "screenshots");
        Files.createDirectories(screenshotDirectory);
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        Files.write(screenshotDirectory.resolve(filename), screenshot);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
