package fr.univmobile.android.it;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import fr.univmobile.backend.it.TestBackend;
import fr.univmobile.it.commons.AppiumAndroidEnabledTest;
import fr.univmobile.it.commons.DeviceNames;
import fr.univmobile.it.commons.Scenario;
import fr.univmobile.it.commons.Scenarios;

@Scenarios("Scénarios simples")
@DeviceNames({ "Android Emulator" })
public class Scenarios001 extends AppiumAndroidEnabledTest {

	@Before
	public void setUpData() throws Exception {

		// "/tmp/unm-mobileweb/dataDir"
		final String dataDir = TestBackend.readBackendAppDataDir(new File(
				"target", "unm-backend-app/WEB-INF/web.xml"));

		TestBackend.setUpData("001", new File(dataDir));
	}

	private static final int PAUSE = 4000;

	@Scenario("Aller-retour sur la page « À Propos »")
	@Test
	public void sc001() throws Exception {

		takeScreenshot("home.png");

		savePageSource("pageHome.xml");

		elementByName("À propos").shouldBeVisible();

		elementById("org.unpidf.univmobile:id/universitySelected")
				.textShouldEqualTo("Aucune université sélectionnée.");

		elementByName("À propos").click();

		pause(PAUSE);

		takeScreenshot("about.png");

		savePageSource("pageAbout.xml");

		elementByName("A propos, Navigate up").click();

		pause(PAUSE);

		takeScreenshot("home2.png");
	}

	@Scenario("Géocampus")
	@Test
	public void Geocampus_000() throws Exception {

		takeScreenshot("home.png");

		elementById("org.unpidf.univmobile:id/selectGeocampus").click();

		pause(20000);

		takeScreenshot("geocampus_list.png");

		elementByName("Plan").click();

		pause(10000);

		takeScreenshot("geocampus_map.png");

		// elementById("link-poiNav-3792").click(); // Cergy Pointoise

		// pause(2000);

		// takeScreenshot("geocampus_map_infoWindow.png");

		elementById("org.unpidf.univmobile:id/pagerUniversity").click();

		pause(10000);

		takeScreenshot("geocampus_details.png");

		elementById("android:id/up").click();

		pause(10000);

		takeScreenshot("geocampus_back_to_map.png");
	}
}
