<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" encoding="UTF-8" doctype-public="html"/>

<xsl:variable name="android-installations"
	select="/android-installation-page/android-installation"/>
	
<xsl:template match="/android-installation-page">

<!-- XML VALIDATION -->

<xsl:variable name="duplicate-dates"
	select="android-installation/@date[. = preceding::android-installation/@date]"/>
<xsl:variable name="duplicate-git-commits"
	select="android-installation/@git-commit[. = preceding::android-installation/@git-commit]"/>

<xsl:if test="$duplicate-dates or $duplicate-git-commits">
<xsl:message terminate="yes">Duplicate elements:
<xsl:for-each select="$duplicate-dates">Date: <xsl:value-of select="."/>
</xsl:for-each>
<xsl:for-each select="$duplicate-git-commits">Git Commit: <xsl:value-of select="."/>
</xsl:for-each>
</xsl:message>
</xsl:if>

<xsl:message>OK - No duplicates in: dates, git commits.</xsl:message>

<html lang="fr" dir="ltr">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Content-Language" content="fr"/>
<title>
UnivMobile Android
</title>
<style type="text/css">
h1,
h2,
body {
	font-family: Arial, Helvetica, sans-serif;
	position: relative;
}
#table-body {
	border-collapse: collapse;
	margin: auto;
}
h1 {
	margin-top: 0.2em;
}
div.nav {
	display: inline-block;
	margin-right: 4em;
}
div.nav a {
	font-family: Arial, Helvetica, sans-serif;
	font-size: small;
	text-decoration: none;
}
table {
	border-collapse: collapse;
}
#table-installations,
#table-installations tr {
	border: 1px solid #000;
}
#table-installations th.date,
#table-installations td.date {
	padding-left: 2em;
}
#table-installations th.git-commit,
#table-installations td.git-commit {
	padding-right: 2em;
}
#table-installations th,
#table-installations td {
	padding-left: 1em;
	padding-right: 1em;
}
#table-installations td {
	padding-top: 0.5em;
	padding-bottom: 0.5em;
}
#table-installations td {
	white-space: nowrap;
}
#table-installations a {
	background-color: #eef;
	text-decoration: none;
	display: block;
}
#table-installations thead {
	background-color: #ddd;
	color: #333;
	font-size: small;
}
tr.cell-bottom {
	display: none;
}
td.git-commit,
div.git-commit,
li.git-commit {
	font-size: small;
}
div.mention {
	display: none;
}
td.build-displayName span.label {
	display: none;
}
td.build-displayName {
	text-align: center;
}
#table-installations {
	width: 100%;
}
#table-installations thead {
	background-color: #ddd;
	color: #333;
	font-size: small;
}
table {
	border-collapse: collapse;
}
#table-installations a {
	xbackground-color: #eef;
	text-decoration: none;
}
#table-installations td {
	vertical-align: top;
}
td.git-commit,
div.git-commit,
li.git-commit {
	font-family: Monaco, 'Courier New', monospace;
}
</style>
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0;"/>
</head>
<body>
<table id="table-body">
<tbody>
<tr>
<td>

<div class="nav">
<a href="http://univmobile.vswip.com/">
	Jenkins — Intégration continue
</a>
</div>

<div class="nav">
<a href="http://univmobile.vswip.com/job/unm-devel-it/lastSuccessfulBuild/artifact/unm-devel-it/target/unm-android-ci-dump.html">
	UnivMobile Android — Intégration continue
</a>
</div>

<div class="nav">
<a href="http://univmobile.vswip.com/job/unm-devel-it/lastSuccessfulBuild/artifact/unm-devel-it/target/unm-android-it-scenarios-dump.html">
	UnivMobile Android — Scénarios
</a>
</div>

<h1>
<span>
UnivMobile Android
</span>
</h1>

<table id="table-installations">
<thead>
<tr>
<th class="date">Date</th>
<th class="build-displayName">Build</th>
<th class="label">Label</th>
<th class="git-commit">Git commit</th>
</tr>
</thead>
<tbody>
<xsl:for-each select="android-installation">
<xsl:sort select="@date" order="descending"/>

<xsl:variable name="git-commit" select="@git-commit"/>

<xsl:variable name="href" select="concat(@git-commit, '/UnivMobile-release.apk')"/>

<tr>

<xsl:call-template name="cells">
<xsl:with-param name="item-element" select="'td'"/>
<xsl:with-param name="href" select="$href"/>
</xsl:call-template>
	
</tr>

</xsl:for-each>
</tbody>
</table>

</td>
</tr>
</tbody>
</table>
</body>
</html>

</xsl:template>

<xsl:template name="cells">
<xsl:param name="item-element" select="'td'"/>
<xsl:param name="href"/>
	
	<xsl:element name="{$item-element}">
	<xsl:attribute name="class">date</xsl:attribute>
		<a href="{$href}">
		<xsl:value-of select="@date"/>
		</a>
	</xsl:element>

	<xsl:element name="{$item-element}">
	<xsl:choose>
	<xsl:when test="@build-displayName">
		<xsl:attribute name="class">build-displayName</xsl:attribute>
		<span class="label">Build </span>
		<xsl:value-of select="@build-displayName"/>
	</xsl:when>
	<xsl:otherwise>
		<xsl:attribute name="class">build-displayName empty</xsl:attribute>
	</xsl:otherwise>
	</xsl:choose>
	</xsl:element>
	
	<xsl:element name="{$item-element}">
	<xsl:attribute name="class">label</xsl:attribute>
		<xsl:value-of select="@label"/>
	</xsl:element>
	
	<xsl:element name="{$item-element}">
	<xsl:attribute name="class">git-commit</xsl:attribute>
		<xsl:value-of select="@git-commit"/>
	</xsl:element>

</xsl:template>

</xsl:stylesheet>