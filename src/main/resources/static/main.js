$(document).ready(function() {
    $('#resultsTable').DataTable();
} );

$(document).ready(function() {
    $('#webSitesTable').DataTable();
} );

var stompClient = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {

    console.log("connect( ) method if running.");

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnected, onError);
    event.preventDefault();
}

function disconnect(){
    stompClient.disconnect(function(){
        alert("The connection has been closed!");
    });
}

function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/monitor/public', onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/getSomeResponse",
        {},
        JSON.stringify(
            {
                type: 'INFO',
                context: "Connected!",
                websiteName: "No name for now, no website.",
                webSiteCheckResult: {},
                webSiteForMonitoring: {}
            }
        )
    )
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    var type = "";

    if(messageContent && stompClient) {
        var chatMessage = {
            RequestMessage : {
                type: "INFO",
                context: "just a message..",
                webSiteForMonitoring: {
                    name: "websiteName",
                    url: "URL Address",
                    responseDelayBounds: [],
                    sizeBounds: []
                },
            }
        };
        stompClient.send("/app/getSomeResponse", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    if(message.type === 'JOIN') {

    } else if (message.type === 'LEAVE') {

    } else {

    }

}

//websiteForm.addEventListener('submit', connect, true)
//messageForm.addEventListener('submit', sendMessage, true)