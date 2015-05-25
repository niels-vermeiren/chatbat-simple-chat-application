<%-- 
    Document   : login
    Created on : 27-mrt-2015, 16:07:38
    Author     : niels
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="title.jsp">  
            <jsp:param name="title" value="ChatBat | login" />  
        </jsp:include> 
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>

            <main class="col col-md-10 col-md-offset-1">   

                <div class="row" style="margin: 0 !important;">
                    <div class="col-md-8 col-md-offset-2">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                <div class="panel-title">

                                    <h4>Login</h4>
                                </div>
                            </div>
                            <div class="panel-body">
                            <c:if test="${errors != null && errors.size() > 0}">
                                <div class="alert alert-danger"  role="alert">
                                    <span class="sr-only">Error:</span>
                                    <c:forEach var="error" items="${errors}">
                                        <p class="error">${error}</p>
                                    </c:forEach> 
                                </div>
                            </c:if>  
                            <form class="form form-vertical" method="post" action="ChatController?dispatcher=login">

                                <div class="control-group">
                                    <label>E-mail</label>
                                    <div class="controls">
                                        <c:choose>
                                            <c:when test="${param.email != null}">
                                                <input type="email" name="email" class="form-control" placeholder="Enter your email address" value="<c:out value='${param.email}'/>">
                                            </c:when>
                                            <c:otherwise>
                                                <input type="email" name="email" class="form-control" placeholder="Enter your email address" value="<c:out value='${email}'/>">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>      

                                <div class="control-group">
                                    <label>Password</label>
                                    <div class="controls">
                                        <input type="password" name="password" class="form-control" placeholder="*****" >
                                    </div>
                                </div>

                                <div class="control-group">
                                    <label>Remember me?</label>
                                    <div class="controls">
                                        <input type="checkbox" name="remember" checked="false">
                                    </div>
                                </div>

                                <div class="control-group">
                                    <label></label>
                                    <div class="controls">
                                        <button type="submit" class="btn btn-primary">
                                            Log on
                                        </button>
                                    </div>
                                </div>   

                            </form>


                        </div><!--/panel content-->
                    </div><!--/panel-->
                </div>
            </div>

        </main>
    </body>
</html>
