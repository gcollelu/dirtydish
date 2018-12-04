'use strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//

exports.rotateChores = functions.https.onRequest((request, response) => {
	return admin.database().ref('/houses').once("value", (snapshot) => {
 		const houses = snapshot.val()
        var chores = []
        for (var house in houses){
        	var currentChores = houses[house].chores;
        	for (var myChore in currentChores){
        		if (!currentChores[myChore].daysUntilRotation){
        			currentChores[myChore].daysUntilRotation = currentChores[myChore].frequency;
        			console.log("set chore daysUntilRotation to " + currentChores[myChore].daysUntilRotation);
        		}
        		if (currentChores[myChore].daysUntilRotation === 1){

	        		if (currentChores[myChore].participants){
	        			var i = 0;
	       				for (var pIndex in currentChores[myChore].participants){
	       					var participant = currentChores[myChore].participants[pIndex];
	       					if (currentChores[myChore].assignee === participant.id){
	       						if (i+1 < currentChores[myChore].participants.length){
	       							currentChores[myChore].assignee = currentChores[myChore].participants[i+1].id;
	       							break;
	       						} else {
	       							currentChores[myChore].assignee = currentChores[myChore].participants[0].id;
	       							break;
	       						}
	       					}
	       					i++;
	       				}
	        		}
	        		currentChores[myChore].daysUntilRotation = currentChores[myChore].frequency;
	        		console.log("rotated chore " + currentChores[myChore].name);
	        	} else {
	        		currentChores[myChore].daysUntilRotation--;
	        	}
        	}
        	if (currentChores){
        		admin.database().ref('/houses/' + house + '/chores').set(currentChores);
        	}
        }
        response.send(chores);

 	});
});

exports.cleanAssignee = functions.https.onRequest((request, response) => {
	return admin.database().ref('/houses').once("value", (snapshot) => {
 		const houses = snapshot.val()
        var chores = []
        for (var house in houses){
        	var currentChores = houses[house].chores;
        	for (var myChore in currentChores){
        		currentChores[myChore].assignee = "";
        	}
        	if (currentChores){
        		admin.database().ref('/houses/' + house + '/chores').set(currentChores);
        	}
        }
        response.send("success");

 	});
});

exports.deleteDatabase = functions.https.onRequest((request, response) => {
	admin.database().ref().remove();
	response.send("deleted database");
});



