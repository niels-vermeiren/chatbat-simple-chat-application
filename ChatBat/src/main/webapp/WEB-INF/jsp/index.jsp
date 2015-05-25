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
            <jsp:param name="title" value="ChatBat | application" />  
        </jsp:include> 
        <script type="text/javascript" src="js/functions.js"></script>
        <script  type="text/javascript">
                    window.addEventListener("load", function () {
                    connect("${userId}");
            <c:forEach var="msgs" items="${messages}">
                <c:forEach var="message" items="${msgs.value}">
                    isMessageRead("${message.toUser.id}", "${message.fromUser.id}", "${message.isRead}");
                </c:forEach>
            </c:forEach>
                    }, false);        </script>
        <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.25/angular.min.js"></script>

        <script src="js/angularScript.js"></script>
    </head>
    <body >
        <jsp:include page="header.jsp"></jsp:include>
            <main class="col col-md-10 col-md-offset-1">   

                <div class="row" style="margin: 0 !important;">
                    <div id="friendlist" class="col-lg-3 col-md-5 col-sm-5 box">
                        <div class="classWithPad">

                            <a href="#" class="list-group-item active">
                                Menu
                            </a>
                            <div  class="list-group-item" style="border:none;">
                                <span class="glyphicon glyphicon-chevron-right"></span>
                                Status:
                                <select id="changeStatus">
                                    <option value="Online">Online</option>
                                    <option value="Away">Away</option>
                                    <option value="Offline">Offline</option>
                                </select>
                            </div>

                            <a id="addFriend" href="#" class="list-group-item" style="border:none;">
                                <span class="glyphicon glyphicon-chevron-right"></span>
                                Add friend
                            </a>

                            <a href="#" class="list-group-item active">
                                Friends 
                                <div style="float:right;"  ng-app="onlineApp" ng-controller="MainCtrl as ctrl">
                                    <p style="float:left;">({{online}})</p>
                                </div>

                            </a>
                            <input id="filterFriends" type="text" class="form-control" style="border-radius: 0px; border:none;" placeholder="Filter friends"></input>
                            <ul id="friends">

                            </ul>
                        </div>
                    </div>
                    <div id="chat" class="col-lg-9 col-md-7 col-sm-7 box">
                        <div class="classWithPad">
                            <div class="row">
                                <div class="col-lg-10 col-lg-offset-1 col-md-10 col-md-offset-1 col-sm-10 col-sm-offset-1 col-xs-10 col-xs-offset-1">
                                    <div id="conversations">

                                    <c:forEach var="msgs" items="${messages}">
                                        <div data-userid="${msgs.key}" class="messages">
                                            <c:forEach var="message" items="${msgs.value}">
                                                <div class="message">
                                                    <p>
                                                        <b>${message.fromUser.name}: </b> ${message.content}
                                                    </p>

                                                </div>
                                                <hr/>
                                            </c:forEach>
                                        </div>
                                    </c:forEach>


                                </div>
                                <div id="send">
                                    <textarea id="messageToSend" maxlength="500"><= choose a friend to chat with!</textarea>
                                    <button id="sendMessage" class="btn btn-success">Send</button>
                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </main>
    </body>
</html>

