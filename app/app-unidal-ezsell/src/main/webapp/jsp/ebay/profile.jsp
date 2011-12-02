<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="w" uri="/web/core"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" class="EbayModel" scope="request" />

<a:body>

<style>
.section {font-size: 2em}
</style>

<div class="section">Change Password</div>

<form method="post" action="${model.pageUri}">
	<table border="0">
		<tr>
			<td>New Password:</td>
			<td><input type="password" name="password1" size="20"/></td>
		</tr>
		<tr>
			<td>Password Again:</td>
			<td><input type="password" name="password2" size="20"/></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td><input type="submit" name="cp" value="Change Password" /></td>
		</tr>
	</table>
</form>

<c:if test="${model.loginUserId == model.seller.operatorId}">
	<hr>
	
	<div class="section">Seller Profile</div>
	
	<form method="post" action="${model.pageUri}">
		<table border="0">
			<tr>
				<td>eBay Account:</td>
				<td>${model.seller.ebayAccount}</td>
			</tr>
			<tr>
				<td>Name:</td>
				<td><input type="text" name="name" value="${model.seller.name}"/></td>
			</tr>
			<tr>
				<td>Address:</td>
				<td><textarea name="address" rows="3" cols="60">${w:htmlEncode(model.seller.address)}</textarea></td>
			</tr>
			<c:if test="${model.seller.teamAtEbay > 0}">
				<tr>
					<td>Team Name:</td>
					<td><input type="text" name="teamName" value="${model.seller.teamName}"/></td>
				</tr>
				<tr>
					<td>Team Leader:</td>
					<td><input type="text" name="teamLeader" value="${model.seller.teamLeader}"/></td>
				</tr>
				<tr>
					<td>Team Leader Ext:</td>
					<td><input type="text" name="teamLeaderPhone" value="${model.seller.teamLeaderPhone}"/></td>
				</tr>
				<tr>
					<td>Team Leader Cube:</td>
					<td><input type="text" name="teamLeaderCube" value="${model.seller.teamLeaderCube}"/></td>
				</tr>
			</c:if>
			<tr>
				<td>&nbsp;</td>
				<td><input type="submit" name="up" value="Update Profile" /></td>
			</tr>
		</table>
	</form>
</c:if>

</a:body>