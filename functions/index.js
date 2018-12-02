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
        		if (currentChores[myChore].participants){
        			var i = 0;
       				for (var pIndex in currentChores[myChore].participants){
       					var participant = currentChores[myChore].participants[pIndex];
       					if (currentChores[myChore].assignee.id === participant.id){
       						if (i+1 < currentChores[myChore].participants.length){
       							console.log("ran if");
       							currentChores[myChore].assignee = currentChores[myChore].participants[i+1];
       							break;
       						} else {
       							console.log("ran else");
       							currentChores[myChore].assignee = currentChores[myChore].participants[0];
       							break;
       						}
       					}
       					i++;
       				}
        		}
        	}
        	if (currentChores){
        		console.log('currentChores:' + currentChores);
        		admin.database().ref('/houses/' + house + '/chores').set(currentChores);
        	}
        }
        response.send(chores);

 	});


});
