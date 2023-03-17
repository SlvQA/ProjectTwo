import com.github.javafaker.Faker;
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
        quantity.sendKeys("" + (int) (1 + Math.random() * 100));
        int quant = Integer.parseInt(quantity.getAttribute("value")); // storing quantity for future use

        Thread.sleep(300);

        WebElement calculateButton = driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td[2]/div[2]/table/tbody/tr/td/ol[1]/li[5]/input[2]")); // finding the Calculate button
        calculateButton.click(); // clicking

        Thread.sleep(300);

        WebElement total = driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtTotal")); // getting total from the field and checking if it was calculated correctly
        int actualTotal = Integer.parseInt(total.getAttribute("value"));
        int correctTotal;
        if (quant < 10 && quant > 0) {
            correctTotal = quant * 100;
        } else {
            correctTotal = quant * (100 - (100 * 8) / 100);
        }
        ;

        Assert.assertEquals(actualTotal, correctTotal, "Total was calculated incorrectly.");

        Faker faker = new Faker();
        WebElement customerName = driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtName"));
        customerName.sendKeys(faker.name().firstName() + " " + faker.name().lastName());
        WebElement street = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox2"));
        street.sendKeys(faker.address().streetAddress());
        WebElement city = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox3"));
        city.sendKeys(faker.address().city());
        WebElement state = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox4")); // creating email
        state.sendKeys(faker.address().state());
        WebElement zip = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox5"));  // copying as a confirmation of the email
        zip.sendKeys(faker.address().zipCode());

        int card = (int) (1 + Math.random() * 3); // randomly select a credit card and click
        switch (card) {
            case 1:
                WebElement visa = driver.findElement(By.id("ctl00_MainContent_fmwOrder_cardList_0"));
                visa.click();
                break;
            case 2:
                WebElement mastercard = driver.findElement(By.id("ctl00_MainContent_fmwOrder_cardList_1"));
                mastercard.click();
                break;
            case 3:
                WebElement amex = driver.findElement(By.id("ctl00_MainContent_fmwOrder_cardList_2"));
                amex.click();
                break;
        }



            // driver.close();

        }
    }

