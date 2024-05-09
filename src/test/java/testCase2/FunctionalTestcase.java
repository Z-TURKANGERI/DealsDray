package testCase2;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class FunctionalTestcase {

	public static void main(String[] args) {
		chromeTestCaseOne();
	}

	public static void chromeTestCaseOne() {
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.get("https://demo.dealsdray.com/");

		// User name, Password, submit
		driver.findElement(By.name("username")).sendKeys("prexo.mis@dealsdray.com");
		driver.findElement(By.name("password")).sendKeys("prexo.mis@dealsdray.com");
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		// To validate user loggedin
		Assert.assertEquals(driver.getTitle(), "Prexo");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Actions action = new Actions(driver);
		// JavascriptExecutor jse = (JavascriptExecutor)driver;

		// Pages option
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[text()='Order']"))).click();

		driver.findElement(By.xpath("//span[text()='Orders']")).click();
		driver.findElement(By.xpath("//button[contains(text(),'Add Bulk Orders')]")).click();
		WebElement importButton = driver.findElement(By.id("mui-7"));

		// Click on import button
		action.moveToElement(importButton).click().build().perform();

		// AutoIT script path
		String autoScriptPath = System.getProperty("user.dir") + "\\AutoIT\\autoItScript.exe";

		try {
			// To run autoIT code
			Runtime.getRuntime().exec(autoScriptPath);
			Thread.sleep(2000);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		// After selection of file Import and validate
		driver.findElement(By.xpath("//button[text()='Import']")).click();
		driver.findElement(By.xpath("//button[text()='Validate Data']")).click();

		// Handling alert
		wait.until(ExpectedConditions.alertIsPresent()).accept();
	
		// Taking Screenshot
		File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss a").format(new Date());
		
		String folderPath = System.getProperty("user.dir") + File.separator + "Screenshots";

		// Creating folder and checking if exist or not if not create new folder
		File folder = new File(folderPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}

		// Full path + Screenshot naming as per test case [Screenshot_date_time]
		String destinationScreenshotPath = folderPath + File.separator + "Screenshot" + "_" + timeStamp + ".png";

		try {
			FileHandler.copy(src, new File(destinationScreenshotPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		driver.close();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		
	}

}
