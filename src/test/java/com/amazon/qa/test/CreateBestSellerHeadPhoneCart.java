package com.amazon.qa.test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CreateBestSellerHeadPhoneCart {
	public static WebDriver driver;
	
	@BeforeMethod
	public void Initialization() {
	System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
	driver = new ChromeDriver();	
    driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    driver.manage().window().maximize();
    driver.get("https://www.amazon.com");
	}
	
	@Test
	public void addAllBestSellerHeadphoneToCart() throws InterruptedException {
		Reporter.log("Enter the text “Headphones” in the search box.");
		WebDriverWait wait=new WebDriverWait(driver,20);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='twotabsearchtextbox']"))).sendKeys("Headphones");
			
		Reporter.log("Hit enter");
		driver.findElement(By.xpath("//div[@class='nav-search-submit nav-sprite']//input[@class='nav-input']")).click();
		String xpathLink="//*[contains(text(),'Best Seller')]/../../../../../../../../following-sibling::div//a[@class='a-link-normal a-text-normal']";
		
		Reporter.log("Create list of all the best seller headphones");
		List<WebElement> bestSellerHeadPhones=driver.findElements(By.xpath(xpathLink));
		int ExpectedNumOfHeadPhones=bestSellerHeadPhones.size();
		for(int i=0;i<bestSellerHeadPhones.size();i++) 
		{
			if(!bestSellerHeadPhones.get(i).getText().isEmpty())
			{
				bestSellerHeadPhones.get(i).click();
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='add-to-cart-button']"))).click();
				//Added Java wait just for the recording purpose 
				Thread.sleep(3000);
				
				driver.navigate().back();
				//Locating the webelements again to avoid stale element exception
				bestSellerHeadPhones=driver.findElements(By.xpath(xpathLink));
			}
		}
		Reporter.log("Refresh the page to update the cart");
		driver.navigate().refresh();
		WebElement cart=driver.findElement(By.xpath("//span[@id='nav-cart-count']"));
		
		//Added this code to show the updated cart value in top right for video
		((JavascriptExecutor) driver)
	    .executeScript("window.scrollTo(0, -document.body.scrollHeight)");
		
		Reporter.log("Assert Number of headphone(s) in the cart is/are equal to available in the search page");
		Assert.assertEquals(Integer.parseInt(cart.getText()), ExpectedNumOfHeadPhones);

	}
	
	@AfterMethod
	public void TearDown() {
		driver.quit();
	}
}
