<%@ page contentType="text/xml; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<jsp:useBean id="model" class="EbayModel" scope="request" />
<?xml version="1.0" encoding="utf-8"?>

<document author="Frankie Wu">
	<base-font alias="song" name="STSong-Light" encoding="UniGB-UCS2-H" embedded="true" />
	<base-font alias="default" name="Helvetica" encoding="CP1252" embedded="true" />

	<page size="A4" rotate="false" margin-left="0" margin-right="0" margin-top="10">
		<font name="song" size="15">
			<table border="15" widths="150,150">
				<tr>
					<td height="20">Team Name:</td>
					<td height="20">${model.seller.teamName}</td>
				</tr>
				<tr>
					<td height="20">Team Leader:</td>
					<td height="20">${model.seller.teamLeader}</td>
				</tr>
				<tr>
					<td height="20">Team Leader Ext:</td>
					<td height="20">${model.seller.teamLeaderPhone}</td>
				</tr>
				<tr>
					<td height="20">Team Leader Cube:</td>
					<td height="20">${model.seller.teamLeaderCube}</td>
				</tr>
				<tr>
					<td height="20">Shipping ID:</td>
					<td height="20">${model.transaction.shippingTrackingId}</td>
				</tr>
				<tr>
					<td height="20" colspan="2">Notes: incorrect data above may cause delay of your shipment.</td>
				</tr>
			</table>
		</font>

		<font name="default" size="9">
			<table border="0" widths="18,20,200">
				<tr>
					<td height="10" colspan="3"></td>
				</tr>
				<tr>
					<td height="30" colspan="3"><font name="default" size="9"><![CDATA[${model.transaction.buyerAccount}   ${model.transaction.itemTitle}]]></font></td>
				</tr>
				<tr>
					<td>From:</td>
					<td colspan="2"><font name="song"><![CDATA[${model.seller.name}
${model.seller.address}]]></font>
					</td>
				</tr>
				<tr>
					<td height="200" colspan="2"></td>
					<td>
						<font size="15" style="1"><![CDATA[
${model.transaction.buyerName}]]></font>
						<font size="14"><![CDATA[
${model.transaction.shippingAddress}]]></font>
					</td>
				</tr>
			</table>			
		</font>
	</page>
</document>