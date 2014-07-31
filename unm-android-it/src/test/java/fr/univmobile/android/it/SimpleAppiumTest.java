package fr.univmobile.android.it;

import static fr.univmobile.it.commons.AppiumCapabilityType.APP;
import static fr.univmobile.it.commons.AppiumCapabilityType.DEVICE;
import static fr.univmobile.it.commons.AppiumCapabilityType.DEVICE_NAME;
import static fr.univmobile.it.commons.AppiumCapabilityType.PLATFORM_NAME;
import static fr.univmobile.it.commons.AppiumCapabilityType.PLATFORM_VERSION;
import static org.apache.commons.lang3.CharEncoding.UTF_8;
import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SimpleAppiumTest {

	@Test
	public void testAppiumSimple() throws Exception {

		final DesiredCapabilities capabilities = new DesiredCapabilities();

		// Get this .apk file from:
		// http://univmobile.vswip.com/job/Android-UnivMobile
		// /lastSuccessfulBuild/artifact/UnivMobile/bin/UnivMobile-debug.apk

		final File app = new File("apk/UnivMobile-debug.apk");

		capabilities.setCapability(APP, app.getAbsolutePath());

		capabilities.setCapability(PLATFORM_NAME, "Android");
		capabilities.setCapability(PLATFORM_VERSION,
				"4.4 KitKat (API Level 19)");

		capabilities.setCapability(DEVICE, "Android");
		capabilities.setCapability(DEVICE_NAME, "Android Emulator");

		final AppiumDriver driver = new AppiumDriver(new URL(
				"http://localhost:4723/wd/hub"), capabilities);
		try {

			Thread.sleep(2000);

			final File file = // ((TakesScreenshot) augmentedDriver)
			driver.getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(file, //
					new File("target", "testAppiumSimple.png"));

			System.out.println("Deleting: " + file.getCanonicalPath() + "...");

			file.delete();

			final String pageSource = driver.getPageSource();

			FileUtils.write(new File("target", "pageSource.xml"), pageSource,
					UTF_8);

			final WebElement element = driver.findElement(By.name("À propos"));

			element.click();

			Thread.sleep(2000);

			final File file2 = // ((TakesScreenshot) augmentedDriver)
			driver.getScreenshotAs(OutputType.FILE);

			FileUtils.copyFile(file2, //
					new File("target", "testAppiumSimple2.png"));

			System.out.println("Deleting: " + file2.getCanonicalPath() + "...");

			file2.delete();

			final String pageSource2 = driver.getPageSource();

			FileUtils.write(new File("target", "pageSource2.xml"), pageSource2,
					UTF_8);

		} finally {
			driver.quit();
		}
	}
}
