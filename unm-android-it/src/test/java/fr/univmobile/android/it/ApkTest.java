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

		assertEquals("http://localhost:8380/unm-backend/json/",
				androidManifest.getJsonURL());
	}
}
