<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="header">
    <h1>ChatBat</h1>
    <ul>
        
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <c:choose>
                    <c:when test="${pageContext.request.getParameter(\"dispatcher\") == \"toSettings\"}">
                        <li><a href="ChatController?dispatcher=toHome">Home</a></li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="ChatController?dispatcher=toSettings">Settings</a></li>
                    </c:otherwise>
                </c:choose>
                <li><a href="ChatController?dispatcher=logout" id="logout">Logout</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="ChatController?dispatcher=toRegister">Register</a></li>
                <li><a href="ChatController?dispatcher=toLogin">Login</a></li>
            </c:otherwise>
        </c:choose>
       
    </ul>
</div>