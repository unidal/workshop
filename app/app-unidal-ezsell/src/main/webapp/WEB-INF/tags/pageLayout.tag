<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
	<head>
		<title>Easy Selling - ${model.action.description}</title>
		<link rel='stylesheet' href='${model.webapp}/default.css' type='text/css' />
	</head>
	<body>
		<h1>
			${model.action.description}
		</h1>
		<ul class="tabs">
			<c:forEach var="action" items="${model.action.values}">
				<c:if test="${action.realPage}">
					<li ${model.action == action ? 'class="selected"' : ''}><a href="${model.moduleUri}/${action.name}">${action.description}</a></li>
				</c:if>
			</c:forEach>
		</ul>

		<jsp:doBody />
	</body>
</html>
