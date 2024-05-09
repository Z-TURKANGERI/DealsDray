package testCases1;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.safari.SafariDriver;

public class ResolutionAndScreenshot {

	public static WebDriver driver;

	public static void main(String[] args) throws InterruptedException {
		// resolutions as per test cases
		int[][] reqResolutions = { { 1920, 1080 }, { 1366, 768 }, { 1536, 864 }, { 360, 640 }, { 414, 896 },
				{ 375, 667 } };

		for (int[] resolution : reqResolutions) {
			runTestOne("chrome", resolution[0], resolution[1]);
		}
	}

	public static void runTestOne(String browserName, int width, int height) throws InterruptedException {
		// Cross browsing
		if (browserName.equalsIgnoreCase("chrome")) {
			driver = new ChromeDriver();
		} else if (browserName.equalsIgnoreCase("firefox")) {
			driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else {
			System.out.println("Unsupported browser " + browserName);
		}

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().setSize(new Dimension(width, height));
		driver.get("https://www.getcalley.com/page-sitemap.xml");

		// All link on the page
		List<WebElement> links = driver.findElements(By.xpath("//table[@id='sitemap']//tbody//a"));

		int count = 0;
		for (WebElement link : links) {
			if (count >= 6) {
				break;
			}
			Actions action = new Actions(driver);
			action.keyDown(Keys.CONTROL).sendKeys(link).build().perform();
			count++;
		}

		// Getting window handles
		Set<String> windowsId = driver.getWindowHandles();

		for (String id : windowsId) {
			driver.switchTo().window(id);
			Thread.sleep(2000);
			if (driver.getTitle().equals("XML Sitemap - Calley Automatic Call Dialer")) {
				continue;
			}

			// Screenshot setup as per test case
			File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss a").format(new Date());

			String deviceName = "";
			try {
				deviceName = java.net.InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}

			// Folder structure as per test case [Screenshot -> browserName -> deviceName ->
			// width x height]
			String folderPath = System.getProperty("user.dir") + File.separator + "Screenshots" + File.separator
					+ browserName + File.separator + deviceName + File.separator + width + "x" + height;

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
		driver.quit();
	}

}
