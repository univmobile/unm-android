package fr.univmobile.android.it;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class ApkTest {

	@Test
	public void testAndroidManifest() throws Exception {

		// mvn dependency:copy, see POM File: pom.xml

		final File apkFile = new File("target", "UnivMobile_localhost.apk");

		final AndroidManifest androidManifest = ApkUtils
				.loadAndroidManifest(apkFile);

		assertEquals(
				// "10.0.2.2" is the host loopback interface, as seen from
				// within the Android emulator (itself 10.0.2.15).
				"http://10.0.2.2:8380/unm-backend/json/",
				androidManifest.getJsonURL());
	}
}
