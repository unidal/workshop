<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
	<head>
		<title>Easy Selling - Registration</title>
		<link rel='stylesheet' href='${model.webapp}/default.css' type='text/css' />
	</head>
	<body>
		<h1>
			${payload.step.description}
		</h1>
		<ul class="tabs">
			<c:forEach var="step" items="${payload.step.values}">
					<li ${payload.step == step ? 'class="selected"' : ''}>${step.description}</li>
			</c:forEach>
		</ul>

		<jsp:doBody />
	</body>
</html>
