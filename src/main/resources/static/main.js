// DECLARATION

// 1. WebSocket Connection will be set to this variable
var stompClient = null;

// 2. 'Monitoring Results' data table
var checkResults = $('#resultsTable').DataTable();

// 3. 'WebSites for Monitoring' data table
var monitoringWebSites = $('#webSitesTable').DataTable({
	"columnDefs": [ {
		"targets": -1,
		"data": null,
		"defaultContent": "<button>Click!</button>"
	} ]
});


// 'WebSites for Monitoring' - data table - 'Button' - config
$('#webSitesTable tbody').on('click', 'button', function () {
	var data = monitoringWebSites.row( $(this).parents('tr') ).data();
	if(confirm("Are you sure you want to delete the website '"+ data[ 0 ] +"' from monitoring list?")){
		
		var deleteMessage = {
			type: "REMOVE_WEBSITE",
			context: "",
			webSiteForMonitoring: {
				name: data[0],
				url: data[1],
				startDate: data[2]
			}
		};
		
		stompClient.send('/app/deleteWebSite', {}, JSON.stringify(deleteMessage));	
		stompClient.send('/app/getAllWebSites', {}, JSON.stringify(deleteMessage));	
		monitoringWebSites.clear()	
	}
} );

// Total number of Websites added to DB
var numberOfWebSitesOnTrack = 0;

// WebSocket  'Client' < = > 'Server' Connection set up
$(document).ready(function () {
	connect();
});



// Buttons assigned to methods: 
// - Add new WebSite
// - Start/Stop Monitoring
document.getElementById('submitNewWebSite').onclick = sendNewWebSiteForMonitoring;
document.getElementById('monitoringButton').onclick = monitoringButton;



// FUNCTIONS

// 1. WebSocket Connection

// - connect
function connect(event) {
	var socket = new SockJS('/ws');
	stompClient = Stomp.over(socket);

	stompClient.connect({}, onConnected, onError);
	event.preventDefault();
}

// - connection has closed
function onDisconnect() {
	alert("The connection has been closed!");
	document.getElementById('connStatus').className = "glyphicon glyphicon-thumbs-downs";
	document.getElementById('connStatus').style = "color: red";
}

// - successfull connection
function onConnected() {
	stompClient.subscribe('/monitor/public', onMessageReceived);

	var connectedMessage = {
		type: "CONNECTED",
		context: {},
		webSiteForMonitoring: {}
	};

	stompClient.send('/app/connected', {}, JSON.stringify(connectedMessage));

	document.getElementById('connStatus').className = "glyphicon glyphicon-thumbs-up";
	document.getElementById('connStatus').style = "color: green";
}

// - error during connection
function onError(error) {
	alert("The error has occurred!");
	document.getElementById('connStatus').className = "glyphicon glyphicon-thumbs-downs";
	document.getElementById('connStatus').style = "color: red";
}


// - message receiver
function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);

	if (message.type === 'ALL_RESULTS_AND_WEBSITES') {
		fillResultsTable(message.webSitesChecksResults);
		fillWebSitesTable(message.webSitesForMonitoring);
	} else if (message.type === 'NEW_CHECK_RESULT') {
		addNewResult(message.webSiteCheckResult);
	} else if (message.type === 'WEBSITE_ADDED') {
		addNewWebSite(message.webSiteForMonitoring)
	} else if(message.type === 'ALL_WEBSITES'){
		fillWebSitesTable(message.webSiteForMonitoring);
	}
}



// 2. Monitoring Management

// - add new website form (bottom of my html page)
function sendNewWebSiteForMonitoring() {
	var wsName = document.getElementById("wsName").value;
	var wsUrl = document.getElementById("wsURL").value;
	var startDate = new Date().toLocaleString();
	var monitorFrequency = document.getElementById("wsMonitorFrequency").value * 1000;
	var expRespSizeFrom = document.getElementById("wsRespSizeFrom").value;
	var expRespSizeTo = document.getElementById("wsRespSizeTo").value;
	var expRespTimeFrom = document.getElementById("wsRespTimeFrom").value;
	var expRespTimeTo = document.getElementById("wsRespTimeTo").value;

	var addWebSiteMessage = {
		type: "ADD_WEBSITE",
		context: "",
		webSiteForMonitoring: {
			name: wsName,
			url: wsUrl,
			startDate: startDate,
			delayUpBound: expRespTimeTo,
			delayLowBound: expRespTimeFrom,
			sizeUpBound: expRespSizeTo,
			sizeLowBound: expRespSizeFrom,
			monitorFrequency: monitorFrequency
		}
	};

	if(confirm("Are you sure you want to add '"+wsName+"' with URL - '"+wsUrl+
		"', delay bounds - '"+expRespSizeFrom+" - "+expRespSizeTo+
		"' MS , size bounds - "+expRespSizeFrom+" - "+expRespSizeTo+
		" bytes and monitoring frequency of '"+monitorFrequency+"' s to the monitoring?")){

		stompClient.send("/app/addWebSite", {}, JSON.stringify(addWebSiteMessage));
	}
}


// - populate 'MONITORING RESULTS' data table	 
function fillResultsTable(results) {
	for (var i = 0; i < results.length; i++) {
		addNewResult(results[i]);
	}
}


// - add single result to 'MONITORING RESULTS' data table
function addNewResult(result) {
	checkResults.row.add([
		result.name,
		result.url,
		result.responseStatus,
		result.checkTime,
		result.responseTime,
		result.responseCode,
		result.responseSize
	]).draw(false);
}


// - populate 'WEBSITES FOR MONITORING' data table
function fillWebSitesTable(websites) {
	for (var i = 0; i < websites.length; i++) {
		addNewWebSite(websites[i]);
	}

	numberOfWebSitesOnTrack = websites.length;
	document.getElementById('onlineNumber').textContent = numberOfWebSitesOnTrack;
}



// - add single website for monitoring to 'WEBSITES FOR MONITORING' data table
function addNewWebSite(website) {
	monitoringWebSites.row.add([
		website.name,
		website.url,
		website.startDate,
		website.delayLowBound + " - " + website.delayUpBound,
		website.sizeLowBound + " - " + website.sizeUpBound,
		website.monitorFrequency
	]).draw(false);

	numberOfWebSitesOnTrack++;
	document.getElementById('onlineNumber').textContent = numberOfWebSitesOnTrack;
}


// - General Start/Stop Monitoring button 
function monitoringButton() {
	var value = document.getElementById('monitoringButton').value;
	if (value === "Start Monitoring") {
		document.getElementById('monitoringButton').value = "Stop Monitoring";
		document.getElementById('monitoringButton').textContent = "Stop Monitoring";

		var startMessage = {
			type: "START_MONITORING",
			context: {},
			webSiteForMonitoring: {}
		};
		stompClient.send("/app/startMonitoring", {}, JSON.stringify(startMessage));
	} else if (value === "Stop Monitoring") {
		document.getElementById('monitoringButton').value = "Start Monitoring";
		document.getElementById('monitoringButton').textContent = "Start Monitoring";

		var stopMessage = {
			type: "STOP_MONITORING",
			context: {},
			webSiteForMonitoring: {}
		};
		stompClient.send("/app/stopMonitoring", {}, JSON.stringify(stopMessage));
	}
}



// 3. Clock ...

function showTime(){
    var date = new Date();
    var h = date.getHours(); // 0 - 23
    var m = date.getMinutes(); // 0 - 59
    var s = date.getSeconds(); // 0 - 59
    var session = "AM";
    
    if(h == 0){
        h = 12;
    }
    
    if(h > 12){
        h = h - 12;
        session = "PM";
    }
    
    h = (h < 10) ? "0" + h : h;
    m = (m < 10) ? "0" + m : m;
    s = (s < 10) ? "0" + s : s;
    
    var time = h + ":" + m + ":" + s + " " + session;
    document.getElementById("clock").innerText = time;
	document.getElementById("clock").textContent = time;
	
	setTimeout(showTime, 1000);
}

showTime();