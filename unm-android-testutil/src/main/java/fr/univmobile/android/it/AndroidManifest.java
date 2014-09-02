package fr.univmobile.android.it;

import net.avcompris.binding.annotation.Namespaces;
import net.avcompris.binding.annotation.XPath;

@Namespaces("xmlns:android=http://schemas.android.com/apk/res/android")
@XPath("/AndroidManifest.xml/manifest")
public interface AndroidManifest {

	@XPath("application/meta-data[@android:name = 'jsonURL']/@android:value")
	String getJsonURL();

	@XPath("@android:versionCode")
	int getVersionCode();

	@XPath("@android:versionName")
	String getVersionName();
}
