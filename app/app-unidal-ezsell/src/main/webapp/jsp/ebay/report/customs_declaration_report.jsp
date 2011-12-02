<%@ page contentType="text/xml; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<jsp:useBean id="model" class="EbayModel" scope="request" />
<?xml version="1.0" encoding="utf-8"?>

<document author="Frankie Wu">
	<base-font alias="song" name="STSong-Light" encoding="UniGB-UCS2-H" embedded="true" />
	<base-font alias="default" name="Helvetica" encoding="CP1252" embedded="true" />

	<page size="A5" rotate="true" margin-left="15" margin-right="0" margin-top="40">
		<font name="default" size="11">
			<table border="0" width="80" widths="65,20,35,80,20">
				<tr>
					<td height="60" colspan="3"><font name="song">${model.seller.name}
${model.seller.address}</font></td>
					<td>

${model.customsDeclaration.trackingNo}</td>
					<td></td>
				</tr>
				<tr>
					<td height="85" colspan="3"><font style="1">${model.customsDeclaration.toName}</font>
${model.customsDeclaration.toAddress}</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td height="80"><![CDATA[${model.customsDeclaration.itemTitle}]]></td>
					<td>${model.customsDeclaration.itemQuantity}</td>
					<td>${model.customsDeclaration.weight}</td>
					<td>${model.customsDeclaration.itemValue}</td>
					<td>${model.customsDeclaration.origin}</td>
				</tr>
				<tr>
					<td height="23" colspan="5"></td>
				</tr>
				<tr>
					<td height="15" colspan="5">V</td>
				</tr>
			</table>
			<br/>
		</font>
	</page>

	<page size="A5" rotate="true" margin-left="15" margin-right="0" margin-top="40">
		<font name="default" size="11">
			<table border="0" width="80" widths="65,20,35,80,20">
				<tr>
					<td height="60" colspan="3"><font name="song">${model.seller.name}
${model.seller.address}</font></td>
					<td>

${model.customsDeclaration.trackingNo}</td>
					<td></td>
				</tr>
				<tr>
					<td height="85" colspan="3"><font style="1">${model.customsDeclaration.toName}</font>
${model.customsDeclaration.toAddress}</td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td height="80"><![CDATA[${model.customsDeclaration.itemTitle}]]></td>
					<td>${model.customsDeclaration.itemQuantity}</td>
					<td>${model.customsDeclaration.weight}</td>
					<td>${model.customsDeclaration.itemValue}</td>
					<td>${model.customsDeclaration.origin}</td>
				</tr>
				<tr>
					<td height="23" colspan="5"></td>
				</tr>
				<tr>
					<td height="15" colspan="5">V</td>
				</tr>
			</table>
			<br/>
		</font>
	</page>
</document>