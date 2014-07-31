package fr.univmobile.android.it;

import org.junit.Test;

import fr.univmobile.it.commons.AppiumAndroidEnabledTest;
import fr.univmobile.it.commons.DeviceNames;
import fr.univmobile.it.commons.Scenario;
import fr.univmobile.it.commons.Scenarios;

@Scenarios("Scénarios simples")
@DeviceNames({ "Android Emulator" })
public class Scenarios001 extends AppiumAndroidEnabledTest {

	@Scenario("Aller-retour sur la page « À Propos »")
	@Test
	public void sc001() throws Exception {

		takeScreenshot("home.png");

		savePageSource("pageHome.xml");

		elementByName("À propos").shouldBeVisible();
		
		elementById("org.unpidf.univmobile:id/universitySelected")
				.textShouldEqualTo("Aucune université sélectionnée.");

		elementByName("À propos").click();

		takeScreenshot("about.png");

		savePageSource("pageAbout.xml");
	}
}
