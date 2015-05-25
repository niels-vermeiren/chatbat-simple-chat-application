/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var xHRObject = new XMLHttpRequest();

//Remove friend ajax call
function removeFriend() {
    console.log("Removing friend...");
    xHRObject.open("POST", "ChatController?dispatcher=removeFriend", true);
    xHRObject.onreadystatechange = getDataRemovedFriend;
    var message = "email=" + document.getElementById("friendEmail").value;
    xHRObject.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xHRObject.send(message);
}

//Friend has been removed
function getDataRemovedFriend() {
    var status = document.getElementById("status");
    status.innerHTML = "";
    var para = document.createElement("p");
    if (xHRObject.readyState === 4) {
        if (xHRObject.status === 200) {
            var response = JSON.parse(xHRObject.responseText);
            
            if (response.success === true) {
                var node = document.createTextNode("Friend has been removed");
                para.appendChild(node);
                status.appendChild(para);
                status.className = "alert alert-success";
            } else {
                var node = document.createTextNode("Friend does not exist");
                para.appendChild(node);
                status.appendChild(para);
                status.className = "alert alert-danger";
            }
        }
    }
}

//Poll top 5 friends 
function pollTopFriends() {
    console.log("Polling top friends...");
    xHRObject.open("GET", "ChatController?dispatcher=topFriends", true);
    xHRObject.onreadystatechange = getDataTopFriends;
    xHRObject.send(null);
}

//Top 5 friends received
function getDataTopFriends() {
    if (xHRObject.readyState === 4) {
        if (xHRObject.status === 200) {
            var response = JSON.parse(xHRObject.responseText);
            
            //empty table
            var topFriends = document.getElementById("topFriends");
            topFriends.innerHTML = "";
            
            for(var i=0; i!==response.friends.length;i++) {
                //Create table
                var tr = document.createElement("tr");
                var tdName = document.createElement("td");
                var tdEmail = document.createElement("td");
                var tdAmount = document.createElement("td");
                var textName = document.createTextNode(response.friends[i].name);
                var textEmail = document.createTextNode(response.friends[i].email);
                var textAmount = document.createTextNode(response.friends[i].amount);
                tdName.appendChild(textName);
                tdEmail.appendChild(textEmail);
                tdAmount.appendChild(textAmount);
                tr.appendChild(tdName);
                tr.appendChild(tdEmail);
                tr.appendChild(tdAmount);
                topFriends.appendChild(tr);
            }

        }
    }
}

//Start polling top friends
window.onload = function() {
    pollTopFriends();
    setInterval(pollTopFriends, 10000);
};


