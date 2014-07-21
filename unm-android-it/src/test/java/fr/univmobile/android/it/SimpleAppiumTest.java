package fr.univmobile.android.it;

import static fr.univmobile.it.commons.AppiumCapabilityType.*;
import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SimpleAppiumTest {

	@Test
	public void testAppiumSimple() throws Exception {

		final DesiredCapabilities capabilities = new DesiredCapabilities();

		final File app = new File(
				"/Users/dandriana/Documents/workspace/unm-android"
						+ "/UnivMobile/app/build/outputs/apk/app-debug.apk");

		capabilities.setCapability(APP, app.getAbsolutePath());

		// capabilities.setCapability(BROWSER_NAME, "Safari");

		// capabilities.setCapability(PLATFORM, "Mac");
		capabilities.setCapability(PLATFORM_NAME, "Android");
		capabilities.setCapability(PLATFORM_VERSION,
				"4.4 KitKat (API Level 19)");

		capabilities.setCapability(DEVICE, "Android");
		 capabilities.setCapability(DEVICE_NAME, "Android Emulator");
		// "iPhone Retina (4-inch)");
		// "iPhone");
		// "iPhone Retina (3.5-inch)");

		// capabilities.setCapability(APP, "safari");

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
			
		} finally {
			driver.quit();
		}
	}
}
