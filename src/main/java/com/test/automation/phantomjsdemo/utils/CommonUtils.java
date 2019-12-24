package com.test.automation.phantomjsdemo.utils;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

public class CommonUtils {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public String executeTestAutomation(String browser, String url) {
		String executionStatus = "";
		try {
			WebDriver webDriver = getWebDriver(browser);
			webDriver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
			webDriver.manage().window().maximize();
			webDriver.get(url);
			
			WebDriverWait wait = new WebDriverWait(webDriver, 60);
			wait.until(new Function<WebDriver,Boolean>() {
				@Override
				public Boolean apply(WebDriver webDriver) {
					 return ((JavascriptExecutor)webDriver).executeScript("return document.readyState").equals("complete") ;
				}
			});
			
			//Thread.sleep(10000);
			logger.info("Current URL of the browser {}",webDriver.getCurrentUrl());
			logger.info("Current DOM Content of the browser {}",webDriver.getPageSource());
			executionStatus = webDriver.getPageSource();
			webDriver.close();
		} catch (Exception e) {
			executionStatus = e.getMessage();
			e.printStackTrace();
		}
		return executionStatus;
	}

	public WebDriver getWebDriver(String browser) {
		logger.info("Executing getWebDriver(); Browser: {}",browser);
		WebDriver webDriver = null;
		try {
			DesiredCapabilities desiredCapabilities = null;
			Platform currentPlatform = Platform.getCurrent().family();
			switch (browser.toLowerCase()) {
			case "chrome":
				desiredCapabilities = getChromeDesiredCapabilities(currentPlatform);
				webDriver = new ChromeDriver(desiredCapabilities);
				break;
			case "phantomjs":
				desiredCapabilities = getPhantomJSDesiredCapabilities(currentPlatform);
				webDriver = new PhantomJSDriver(desiredCapabilities);
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
		return webDriver;
	}

	private DesiredCapabilities getChromeDesiredCapabilities(Platform currentPlatform) {
		DesiredCapabilities capability;
		boolean isHeadlessModeEnabled = false;
		File file = new File("C:\\Temp\\chromedriver.exe");
		switch (currentPlatform) {
		case WINDOWS:
			isHeadlessModeEnabled = false;
			file = new File("C:\\Temp\\chromedriver.exe");
			break;
		case UNIX:
			isHeadlessModeEnabled = true;
			file = new File("/usr/bin/chromedriver");
			break;
		default:
			break;
		}
		System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
		capability = DesiredCapabilities.chrome();

		capability.setBrowserName("chrome");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-infobars");
		options.addArguments("--start-maximized");
		options.addArguments("-incognito");

		if (isHeadlessModeEnabled) {
			options.addArguments("headless");
			options.addArguments("window-size=1920,1080");
			options.addArguments("--headless");
			options.addArguments("--disable-gpu");
			options.addArguments("--no-sandbox");
		}
		options.addArguments("--start-maximized");
		options.addArguments("--disable-extensions");

		capability.setCapability(ChromeOptions.CAPABILITY, options);
		logger.info("capability: {}"+capability);
		return capability;
	}

	private DesiredCapabilities getPhantomJSDesiredCapabilities(Platform currentPlatform) {
		DesiredCapabilities capability;
		String webDriverPath = "";
		capability = DesiredCapabilities.phantomjs();
		capability.setJavascriptEnabled(true);
		final List<String> argsList = new LinkedList<>();
		argsList.add("--web-security=false");
		argsList.add("--ignore-ssl-errors=yes");
		argsList.add("--ssl-protocol=ANY");
		argsList.add("--load-images=yes");
		capability.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
				argsList.toArray(new String[argsList.size()]));
		capability.setCapability("phantomjs.page.settings.userAgent",
				"Mozilla/5.0 (Unknown; Linux x86_64) AppleWebKit/538.1 (KHTML, like Gecko) PhantomJS/2.1.1 Safari/538.1");
		capability.setCapability("phantomjs.page.customHeaders.SAP-Connectivity-ConsumerAccount", "");

		final LoggingPreferences logPrefs = new LoggingPreferences();
		logPrefs.enable(LogType.BROWSER, Level.OFF);
		capability.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

		logger.info("PLATFORM is {}", currentPlatform);
		switch (currentPlatform) {
		case WINDOWS:
			webDriverPath = "C:\\Temp\\phantomjs.exe";
			capability.setCapability("phantomjs.binary.path", webDriverPath);
			break;
		case UNIX:
			URL url = CommonUtils.class.getProtectionDomain().getCodeSource().getLocation();
			webDriverPath = url.toString().substring(6, url.toString().lastIndexOf("/classes"))
					+ "/classes/WebDriver/phantomjs";
			webDriverPath = webDriverPath.substring(webDriverPath.lastIndexOf("ljs/") + 4, webDriverPath.length());
			File file = new File(webDriverPath);
			file.setExecutable(true);
			capability.setCapability("phantomjs.binary.path", file.getPath());
			break;
		default:
			break;
		}
		logger.info("capability: {}"+capability);
		return capability;
	}

}
