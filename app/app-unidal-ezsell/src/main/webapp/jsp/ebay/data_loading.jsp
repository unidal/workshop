<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" class="EbayModel" scope="request" />

<a:body>

<form method="post" action="${model.pageUri}" enctype="multipart/form-data">
	<table border="0">
		<tr>
			<td>PayPal Payment File:</td>
			<td><input type="file" name="paymentFile" size="30"/></td>
		</tr>
		<tr>
			<td>Shipping File:</td>
			<td><input type="file" name="shippingFile" size="30"/></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="submit" value="Submit" /></td>
		</tr>
	</table>
</form>

</a:body>