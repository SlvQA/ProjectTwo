import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        driver.findElement(By.id("ctl00_MainContent_login_button")).click(); // finding the LogIn button and clicking it

        Thread.sleep(500);

        driver.findElement(By.linkText("Order")).click(); // finding the Order button and clicking it

        Thread.sleep(500);

        WebElement quantity = driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtQuantity")); // input of quantity
        quantity.sendKeys("" + (int) Math.abs(1 + Math.random() * 100));
        int quant = Integer.parseInt(quantity.getAttribute("value")); // storing quantity for future use

        Thread.sleep(300);

        driver.findElement(By.xpath("/html/body/form/table/tbody/tr/td[2]/div[2]/table/tbody/tr/td/ol[1]/li[5]/input[2]")).click(); // finding the Calculate button and clicking it

        Thread.sleep(300);

        WebElement total = driver.findElement(By.id("ctl00_MainContent_fmwOrder_txtTotal")); // getting total from the field and checking if it was calculated correctly
        int actualTotal = Integer.parseInt(total.getAttribute("value"));
        int correctTotal;
        if (quant < 10 && quant > 0) {
            correctTotal = quant * 100;
        } else {
            correctTotal = quant * (100 - (100 * 8) / 100);
        }

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
        String zipFive = faker.address().zipCode().substring(0,5);
        zip.sendKeys(zipFive);

        List<Integer> givenList = Arrays.asList(1, 2, 3); // dynamic way to randomly select a credit card and click (may also be used with Strings)
        int card = (int) (1 + Math.random() * givenList.size());

        // int card = (int) (1 + Math.random() * 3); // an easier, but hardcoded way to randomly select a credit card and click
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

        if (card == 1){WebElement cVisa = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6")); // replacing first digit of the cc number
            cVisa.sendKeys(faker.finance().creditCard(CreditCardType.VISA).replaceFirst("^.","4").replaceAll("-",""));
            }
        if (card == 2){WebElement cMastercard = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6"));
            cMastercard.sendKeys(faker.finance().creditCard(CreditCardType.MASTERCARD).replaceFirst("^.","5").replaceAll("-",""));
            }
        if (card == 3){WebElement cAmex = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6"));
            cAmex.sendKeys(faker.finance().creditCard(CreditCardType.AMERICAN_EXPRESS).replaceFirst("^.","3").replaceAll("-",""));
            }

        WebElement cardNumber = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox6"));
        String crdNum = cardNumber.getAttribute("value");

        WebElement expiration = driver.findElement(By.id("ctl00_MainContent_fmwOrder_TextBox1")); // generating random future date
        LocalDate date = LocalDate.now().plusMonths((int)(1+Math.random()*36));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        String dateStr = date.format(formatter);
        expiration.sendKeys(dateStr);

        driver.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();

        Thread.sleep(300);

        String pageSource =  driver.getPageSource(); // getting page Source code
        driver.getPageSource();
        String text = "New order has been successfully added."; // checking if the Source code contains the needed text
        Assert.assertTrue(pageSource.contains(text), "The needed text was not found, this may be a wrong page.");

        driver.findElement(By.linkText("View all orders")).click(); // finding the "View All Orders" link

        LocalDate datePlaced = LocalDate.now();
        List<WebElement> tds = driver.findElements(By.xpath("//table[@class='SampleTable']//tr[2]//td"));
        List<String> firstRow = new ArrayList<>();
        for (WebElement element : tds){
            firstRow.add(element.getText());}

        Assert.assertEquals(firstRow, List.of("", customerName, "MyMoney", quant, datePlaced, street, city, state, zipFive, card, crdNum, expiration, ""));

        driver.findElement(By.id("ctl00_logout")).click();

        Thread.sleep(500);
        driver.close();
        }
    }

