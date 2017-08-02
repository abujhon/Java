import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import Utilities.ExcelUtils;

public class AmazonDataSearch {

	static WebDriver driver;
	WebElement results;
	WebElement search;

	@BeforeClass
	public static void setUp() {
		// System.setProperty("webdriver.chrome.driver"," ")

		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("https://www.amazon.com");
	}

	@Test
	public void searchTest() {
		ExcelUtils.openExcelFile("./src/test/resources/ExcelData/amazon.xlsx", "sheet1");
		int rowsCount = ExcelUtils.getUsedRowsCount();
		for (int rowNum = 1; rowNum < rowsCount; rowNum++) {

			String searchItem = ExcelUtils.getCellData(rowNum, 1);

			searchFor(searchItem);
			String resultText = getSearchResult();
			int resultCount = cleanupSearchResultsCount(resultText);
			ExcelUtils.setCellData(String.valueOf(resultCount), rowNum, 2);
			System.out.println("Search count is : " + resultCount);

			if (resultCount > 0) {
				System.out.println("Search Test pass");
				ExcelUtils.setCellData("pass", rowNum, 3);
			} else {
				System.out.println("Search Test fail");
				ExcelUtils.setCellData("fail", rowNum, 3);
			}
			String now = LocalDateTime.now().toString();
			ExcelUtils.setCellData(now, rowNum, 4);
		}

		// driver.findElement(By.id("twotabsearchtextbox")).sendKeys("Cucumber
		// book"+Keys.ENTER);
		// String results=driver.findElement(By.id("s-result-count")).getText();
		// System.out.println(results);
		// int numberOfResults=cleanupSearchResultsCount(results);
		// if(numberOfResults > 0){
		// System.out.println("PASS");
		// }else{
		// System.out.println("FAIL");
		// }
	}

	public String getSearchResult() {

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {

			e1.printStackTrace();
		}

		try {
			results = driver.findElement(By.id("s-result-count"));
		} catch (Exception e) {
			return "0 result";
		}
		return results.getText();
	}

	public void searchFor(String item) {
		search = driver.findElement(By.id("twotabsearchtextbox"));
		search.clear();
		search.sendKeys(item + Keys.ENTER);
	}

	public int cleanupSearchResultsCount(String searchResult) {
		int resultsCount;
		String longOne = searchResult;
		String[] resultArr = searchResult.split(" ");
		if (longOne.contains("of")) {
			resultsCount = Integer.parseInt(resultArr[2].replace(",", ""));
		} else {
			resultsCount = Integer.parseInt(resultArr[0]);
		}
		return resultsCount;
	}

	 @AfterClass
	 public static void tearDown() {
	 driver.quit();
	 }
}
