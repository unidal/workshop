<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>
		<title>Tracking Analysis System V1.0 - ${payload.action.description}</title>
		<link rel='stylesheet' href='/t/default.css' type='text/css' />
	</head>
	<body>
		<h1>
			${payload.action.description}
		</h1>
		<ul class="tabs">
			<c:forEach var="action" items="${payload.action.values}">
				<c:if test="${action.isRealPage()}">
					<li ${payload.action == action ? 'class="selected"' : ''}>
						<a href="${action.name}">${action.description}</a>
					</li>
				</c:if>
			</c:forEach>
		</ul>

		<jsp:doBody />
	</body>
</html>

