package fr.univmobile.android.it;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.CharEncoding.UTF_8;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.substringBetween;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import net.avcompris.binding.dom.helper.DomBinderUtils;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

import com.avcompris.lang.NotImplementedException;

import fr.univmobile.testutil.Dumper;
import fr.univmobile.testutil.PropertiesUtils;
import fr.univmobile.testutil.XMLDumper;

public abstract class ApkUtils {

	public static AndroidManifest loadAndroidManifest(final File apkFile)
			throws IOException {

		checkNotNull(apkFile, "apkFile");

		if (!apkFile.isFile()) {
			throw new IllegalStateException("APK file not found: "
					+ apkFile.getPath());
		}

		final Executor executor = new DefaultExecutor();

		final ByteArrayOutputStream bos = new ByteArrayOutputStream();

		executor.setStreamHandler(new PumpStreamHandler(bos, null));

		executor.execute(new CommandLine(new File(PropertiesUtils
				.getTestProperty("aapt"))).addArgument("dump")
				.addArgument("xmltree").addArgument(apkFile.getPath())
				.addArgument("AndroidManifest.xml"));

		final String output = bos.toString(UTF_8);

		final File xmlFile = new File("target", apkFile.getName()
				+ "-AndroidManifest.xml");

		Dumper dumper = XMLDumper.newXMLDumper("AndroidManifest.xml", xmlFile);

		int indent = 0;

		for (String line : split(output, "\r\n")) {

			for (final int lineIndent = getLineIndent(line); lineIndent < indent; indent -= 2) {

				dumper = dumper.close();
			}

			line = line.substring(indent);

			if (line.startsWith("N: ")) { // Namespace
				final String namespace = substringAfter(line, "N: ");
				dumper.addAttribute("xmlns:" + substringBefore(namespace, "="),
						substringAfter(namespace, "="));
				indent += 2;
			}

			else if (line.startsWith("E: ")) { // Element
				final String elementName = substringBetween(line, "E: ", " (");
				dumper = dumper.addElement(elementName);
				indent += 2;
			}

			else if (line.startsWith("A: ")) { // Attribute
				String attributeName = substringBetween(line, "A: ", "=");
				if (attributeName.contains("(")) {
					attributeName = substringBefore(attributeName, "(");
				}
				final Object attributeValue;
				final String v = substringAfter(line, "=");

				if (v.startsWith("@")) {
					continue;
				}

				else if (v.startsWith("(type 0x10)0x")) {
					attributeValue = Integer.parseInt(v.substring(13), 16);
				}

				else if (v.startsWith("\"")) {
					attributeValue = substringBetween(v, "\"");
				}

				else if ("(type 0x12)0xffffffff".equals(v)) {
					attributeValue = Boolean.TRUE;
				}

				else {
					throw new NotImplementedException(attributeName + "=" + v);
				}

				dumper.addAttribute(attributeName, attributeValue);
			}
		}

		while (dumper != null) {

			dumper = dumper.close();
		}

		return DomBinderUtils.xmlContentToJava(xmlFile, AndroidManifest.class);
	}

	private static int getLineIndent(final String line) {

		for (int i = 0; i < line.length(); ++i) {

			if (line.charAt(i) != ' ') {
				return i;
			}
		}

		throw new IllegalArgumentException("Line is blank: \"" + line + "\"");
	}
}
