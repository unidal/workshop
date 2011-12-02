<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="org.unidal.ezsell.*"%>
<%@ page import="org.unidal.ezsell.user.*"%>
<%@ taglib prefix="a" uri="/WEB-INF/app.tld"%>
<jsp:useBean id="payload" class="EbayPayload" scope="request" />
<jsp:useBean id="model" type="EbayModel" scope="request" />

<a:register>

Congratulations! Your account has been successfully settled. <br/><br/>

Go to <a href="${model.moduleUri}/login">Login</a> page now.

</a:register>