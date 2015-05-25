<%-- 
    Document   : index
    Created on : 27-mrt-2015, 21:34:04
    Author     : niels
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="title.jsp">  
            <jsp:param name="title" value="ChatBat | Settings" />  
        </jsp:include> 
        <script type="text/javascript" src="js/settingsFunctions.js"></script>
        <style>
            button {
                margin-top:10px;
            }
            h4 {
                margin-top: 20px;
            }
            table {
                margin-top: 0px;
            }
        </style>
    </head>
    <body >
        <jsp:include page="header.jsp"></jsp:include>

        <main class="col-lg-4 col-lg-offset-4 col col-md-6 col-md-offset-3 col-sm-6 col-sm-offset-3 col-xs-10 col-xs-offset-1">   

            <h2>Settings</h2>
            <h4>Remove friend</h3>
                <div id="status"></div>
                <input id="friendEmail" type="text" name="friend" class="form form-control" placeholder="Type in email address"/>
                <button onclick="removeFriend()" class="btn btn-primary" style="margin-top:10px;">Remove friend</button>

                <h4>Top friends</h4>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Messages received</th>
                            </tr>
                        </thead>
                        <tbody id="topFriends">
                         
                        </tbody>
                    </table>
                </div>
        </main>
    </body>
</html>

