package fr.univmobile.android.it;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ApkUtilsTest {

	@Before
	public void setUp_UnivMobile_0_0_4_20140807_151915_2_apk() throws Exception {

		androidManifest = ApkUtils.loadAndroidManifest(new File(
				"src/test/apk/UnivMobile-0.0.4-20140807.151915-2.apk"));
	}

	private AndroidManifest androidManifest;

	@Test
	public void testJsonURL() {

		assertEquals("http://localhost:8380/unm-backend/json/",
				androidManifest.getJsonURL());
	}

	@Test
	public void testVersionCode() {

		assertEquals(13, androidManifest.getVersionCode());
	}

	@Test
	public void testVersionName() {

		assertEquals("3.0 alpha 1", androidManifest.getVersionName());
	}
}
