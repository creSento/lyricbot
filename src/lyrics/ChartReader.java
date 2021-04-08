package lyrics;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ChartReader {
	private String decadeXPath = "/html/body/div/div[3]/div/div/form/div[1]/div/div/div[1]/div[1]/ul/li[2]";
	private String yearXPath = "/html/body/div/div[3]/div/div/form/div[1]/div/div/div[2]/div[1]/ul/li[";
	private String monthXPath = "/html/body/div/div[3]/div/div/form/div[1]/div/div/div[3]/div[1]/ul/li[";
	private String year;
	private String month;
	WebDriver driver;
	CsvWrite csvwriter = new CsvWrite();
	
	public WebDriver getUrl() {
		System.setProperty(Property.DRIVERKEY, Property.DRIVERVALUE);
		driver = new ChromeDriver();
		driver.get(Property.SONGLISTURL);
		term(3);
		driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/ul[1]/li[1]/div/div/button/span")).click();
		term(3);
		return driver;
	}
	
	public void selectMonth(WebDriver driver, int selectYear, int selectMonth) {
		year = yearXPath + String.valueOf(selectYear) + "]";
		month = monthXPath + String.valueOf(selectMonth) + "]";
		
		driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[1]/div/h4[2]/a")).click();
		term(4);
		driver.findElement(By.xpath(decadeXPath)).click();
		term(4);
		// year(li[10]~li[1])
		driver.findElement(By.xpath(year)).click();
		term(4);
		// month(li[1]~li[12])
		driver.findElement(By.xpath(month)).click();
		term(4);
		driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[1]/div/div/div[5]/div[1]/ul/li[2]")).click();
		term(4);
		driver.findElement(By.xpath("/html/body/div/div[3]/div/div/form/div[2]/button/span/span")).click();
		term(4);
	}
	
	public void getSong(WebDriver driver, int selectYear, int selectMonth) throws IOException {
		List<WebElement> list = driver.findElements(By.cssSelector(".input_check"));
		String line = "";
		for(int i = 1; i < 51; i ++) {
			line = "" + selectYear + "," + selectMonth + "," + i + "," + list.get(i).getAttribute("value") + "\n";
			csvwriter.writeFile(Files.SONGFILE, line);
		}
	}
	
	private void term(int second) {
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
