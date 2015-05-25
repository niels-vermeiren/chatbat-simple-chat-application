var ws;
var userId;
var activeConversation;
var requestForFriends = false;
var newMessagesQueue = [];
//WEBSOCKET
function openSocket() {

    //TODO: StatFix url
    var wsUri = "ws://" + document.location.host + "/ChatBat/wsConnect";
    ws = new WebSocket(wsUri);

    ws.onerror = function (msg) {
    };

    ws.onopen = function (msg) {
        console.info("Socket is now open.");
        changeStatus("Online");
        $("#changeStatus").val("Online");
        getFriends();
        hideAllConversations(false);
    };

    ws.onmessage = function (msg) {
        var json = jQuery.parseJSON(msg.data);
        console.log(json.action);
        switch (json.action) {
            case "getFriends":
                populateFriends(json);
                break;
            case "statusChange":
                getFriends();
                break;
            case "messageSent":
                messageReceived(json);
                break;
            case "addedFriend":
                addedFriend(json);
                break;
        }
    };

    ws.onclose = function () {
        console.info("Socket is now closed.");
        changeStatus("Offline");
    };

}
//EVENT LISTENERS
$(document).ready(function () {
    //User clicks logout
    $("#logout").on('click', function () {
        console.log("logging out");
        ws.send(JSON.stringify({
            "action": "changeStatus",
            "userId": userId,
            "status": "Offline"
        }));
    });

    //User changes status manually
    $("#changeStatus").on('change', function () {
        changeStatus($(this).val());
    });

    //Search friends
    $("#filterFriends").keyup(function () {
        getFriends();
    });

    //Empty textarea message on focus
    $("#messageToSend").on('focus', function () {
        var value = $(this).val();
        if (value === "Type your message..." || value === "<= choose a friend to chat with!") {
            $(this).val("");
        }
    });

    //On focusout textarea message set text type your message
    $("#messageToSend").on("focusout", function () {
        var value = $(this).val().trim();
        if (value === "") {
            $(this).val("Type your message...");
        }
    });

    //Open conversation with a particular friend
    $("body").on('click', '.friend', function () {
        var id = $(this).data("id");
        //Remove possible plus signs
        $(this).find(".new").remove();
        //Mark all messages from this user as read
        markMessagesAsRead(id);
        showConversation(id);
    });

    //Send a message
    $("#sendMessage").on('click', function () {
        var message = escapeHtml($("#messageToSend").val().trim());
        if (message !== "" && message !== "Type your message...") {
            sendMessage();
        }
    });

    //Shortkey: to submit a message without clicking the send button, hit ctrl+enter
    $("#messageToSend").keyup(function (event) {
        if (event.keyCode === 13 && event.ctrlKey) {
            $("#sendMessage").click();
        }
    });

    //Add friend button was clicked
    $("#addFriend").on('click', function () {
        bootbox.prompt("Please enter an e-mail address", function (result) {
            if (result !== null) {
                if (isValidEmailAddress(result)) {
                    addFriend(result);
                } else {
                    bootbox.alert("Email was not valid");
                }
            }
        });
    });

});

//FUNCTIONS
//Connect
function connect(id) {
    userId = id;
    openSocket();
}

//Change status user
function changeStatus(status) {
    ws.send(JSON.stringify({
        "action": "changeStatus",
        "userId": userId,
        "status": status
    }));
}

//Get all friends
//Niet met polling geïmplementeerd aangezien ik al een connectie via websockets heb openstaan (heb de code wel nog indien u dit wil zien)
function getFriends() {
    var friendsFilter = $("#filterFriends");
    ws.send(JSON.stringify({
        "action": "getFriends",
        "userId": userId,
        "filterFriends": friendsFilter.val()
    }));
}

//Populate all friends
function populateFriends(json) {
    var friends = $("#friends");
    friends.html("");

    $.each(json.friends, function (key, value) {
        var span = $("<span></span>", {
            "class": "glyphicon glyphicon-chevron-right " + value.state.toLowerCase()
        });
        var wrapper = $("<a></a>", {
            href: "#",
            "class": "list-group-item friend",
            "data-id": value.id
        });
        wrapper.append(span);
        wrapper.append(" " + value.name);
        friends.append(wrapper);
    });
    //If the user received messages while he was offline, add plus signs next to all the names of the friends from whom he got the messages
    //This is dependant on all the friends being loaded first
    if (requestForFriends) {
        for (var i = 0; i !== newMessagesQueue.length; i++) {
            addPlusSign(newMessagesQueue[i]);
        }
    }
    requestForFriends = false;
}

//Hide all conversations
function hideAllConversations(slowing) {
    if(slowing) {
        $(".messages").slideUp('slow');
    } else {
        $(".messages").hide();
    }
}

//Show conversation
function showConversation(userId) {
    $("#messageToSend").val("Type your message...");
    hideAllConversations(true);
    var found = false;
    $(".messages").each(function () {
        var user = $(this).data("userid");
        if (userId === user) {
            found = true;
            $(this).slideDown("slow");
            activeConversation = $(this);
            
        }
    });
    if (!found) {
        activeConversation = createNewConversation(userId);
        
    }
}
//Create a messages div for the first conversation with a new friend
function createNewConversation(userId) {
    var messages = $("<div></div", {
        "class": "messages",
        "data-userid": userId
    });
    $("#conversations").append(messages);
    messages.hide().fadeIn("slow");
    return messages;
}

//Send message over websocket
function sendMessage() {
    if (typeof activeConversation !== 'undefined') {
        ws.send(JSON.stringify({
            "action": "sendMessage",
            "userFrom": userId,
            "userTo": activeConversation.data("userid"),
            "message": escapeHtml($("#messageToSend").val())
        }));
    }
    $("#messageToSend").val("");
}

//Message has been received
function messageReceived(json) {
    var userTo = (parseInt(json.userTo) === parseInt(userId) ? json.userFrom : json.userTo);
    var message = $("<div></div>", {
        'class': "message"
    });
    var paragraph = $("<p></p>");
    paragraph.append("<b>" + json.nameUserFrom + ": </b>" + json.message);
    message.append(paragraph);
    var found = false;
    $(".messages").each(function () {
        if ($(this).data("userid") === userTo) {
            $(this).append(message);
            $(this).append("<hr/>");
            $(this).scrollTop($(this)[0].scrollHeight);
            found = true;

            //If new message was not in the active conversation, add a plus sign next to the name
            if (typeof activeConversation === 'undefined' || activeConversation.data("userid") !== $(this).data("userid")) {
                addPlusSign(userTo);
            }

        }
    });

    //If we can't find an active conversation, create a new one and try again
    if (!found) {
        var messages = createNewConversation(userTo);
        messages.hide();
        messageReceived(json);
    }
}

//Add a plus sign next to the user's from which you got a new message
function addPlusSign(user) {
    $(".friend").each(function () {
        if ($(this).data("id") === user) {
            //if not already a plus sign
            if (!$(this).find(".new").length) {
                var plus = $(" <u class='new'></u>");
                plus.append("(new)");
                $(this).append(" ");
                $(this).append(plus);
            }
        }
    });
}

//Add a plus sign when you got a new message while the current user was not online
//This is added to a queue and will be handled after all the friends have been loaded into the friends list
//This function is called for each message in the db which involves the active user
function isMessageRead(toUser, fromUser, isRead) {
    var read = isRead === "true";
    if (parseInt(userId) === parseInt(toUser) && !read) {
        requestForFriends = true;
        newMessagesQueue = [newMessagesQueue, parseInt(fromUser)];
    }
}

//Mark all messages from a user as being read
//This triggers when you open a conversation with a friend
//FromUser is your friend's id in this case (only the messages being sent FROM him will be marked as read)
function markMessagesAsRead(fromUser) {
    ws.send(JSON.stringify({
        "action": "markMessagesAsRead",
        "fromUser": fromUser,
        "toUser": userId
    }));
}

//Escape message to prevent xss
//It is still possible to xss this application
//I use Jsoup on backend to further safen messages being sent
function escapeHtml(unsafe) {
    return unsafe
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
}

//RFC 2822 compliant regex for checking valid e-mail addresses
function isValidEmailAddress(email) {
    return email.match(/[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/);
}

//Add a friend based on email address
function addFriend(email) {
    ws.send(JSON.stringify({
        "action": "addFriend",
        "email": email
    }));
}

//A friend was added, print result on screen
//If you add a friend, your friend should add you too to see your messages.
//This acts as my kind of confirming mechanism
function addedFriend(json) {
    if (json.found) {
        bootbox.alert("Added friend. If you want your friend to see your messages, your friend should add you too (if not done already).", function () {
            //A reload, this makes sure you synchronously receive the messages already being sent by your newly added friend 
            location.reload();
        });
    } else {
        bootbox.alert("Could not fiend a user with the specified email address");
    }
}