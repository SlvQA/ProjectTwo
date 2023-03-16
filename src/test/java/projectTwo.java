import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class projectTwo {
    public void test() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        WebDriver driver = new ChromeDriver(options);
        driver.get("http://secure.smartbearsoftware.com/samples/TestComplete12/WebOrders/Login.aspx"); // navigate to the website
        String actualTitle = driver.getTitle(); // returns the Title of the page
        String expectedTitle = "Web Orders Login";
        //driver.close(); // closing the active window
        Assert.assertEquals(actualTitle, expectedTitle, "Titles are not matching, this may be not the desired page.");

        Thread.sleep(500);

        WebElement username = driver.findElement(By.id("ctl00_MainContent_username")); // input of credentials
        username.sendKeys("Tester");
        WebElement logInPass = driver.findElement(By.id("ctl00_MainContent_password"));
        logInPass.sendKeys("test");
        WebElement LogInButton = driver.findElement(By.id("ctl00_MainContent_login_button")); // finding the LogIn button
        LogInButton.click(); // clicking

        Thread.sleep(500);

        WebElement ordersButton = driver.findElement(By.linkText("Order")); // finding the Order button
        ordersButton.click(); // clicking

        Thread.sleep(500);

        WebElement quantity = driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtQuantity")); // input of quantity
        quantity.sendKeys(""+(int)(1 + Math.random()*100));

        Thread.sleep(300);

        WebElement calculateButton = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td[2]/div[2]/table/tbody/tr/td/ol[1]/li[5]/input[2]")); // finding the Calculate button
        calculateButton.click(); // clicking

        // driver.close();

    }
}
