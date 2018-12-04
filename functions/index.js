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
                                        //Send notification
                                        const getDeviceTokensPromise = admin.database().ref(`/users/${currentChores[myChore].assignee}/notificationTokens`).once('value');
                                        // The snapshot to the user's tokens.
                                        let tokensSnapshot;
                                        // The array containing all the user's tokens.
                                        let tokens;

                                        console.log('Sending notifications.');
                                        // Promise.all([getDeviceTokensPromise]).then(results => {
                                        //         tokensSnapshot = results[0];
                                        //         console.log('Inside function to send notifications.');
                                        //         // Check if there are any device tokens.
                                        //         if (!tokensSnapshot.hasChildren()) {
                                        //           return console.log('There are no notification tokens to send to.');
                                        //         }
                                        //         console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
                                                

                                        //         // Notification details.
                                        //         const payload = {
                                        //           notification: {
                                        //             title: 'You have been assigned to a new chore!',
                                        //             body: `You have been assigned to ${currentChores[myChore].name} .`
                                        //           }
                                        //         };

                                        //         // Listing all tokens as an array.
                                        //         tokens = Object.keys(tokensSnapshot.val());
                                        //         // Send notifications to all tokens.
                                        //         return admin.messaging().sendToDevice(tokens, payload);
                                        //       }).then((response) => {
                                        //         // For each message check if there was an error.
                                        //         const tokensToRemove = [];
                                        //         response.results.forEach((result, index) => {
                                        //           const error = result.error;
                                        //           if (error) {
                                        //             console.error('Failure sending notification to', tokens[index], error);
                                        //             // Cleanup the tokens who are not registered anymore.
                                        //             if (error.code === 'messaging/invalid-registration-token' ||
                                        //                 error.code === 'messaging/registration-token-not-registered') {
                                        //               tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
                                        //             }
                                        //           }
                                        //         });
                                        //         return Promise.all(tokensToRemove);
                                        //       });

	        		}
	        		currentChores[myChore].daysUntilRotation = currentChores[myChore].frequency;
	        		console.log("rotated chore " + currentChores[myChore].name);
	        	} else {
                                //currentChores[myChore].daysUntilRotation = currentChores[myChore].frequency;
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


exports.cleanDaysUntilRotation = functions.https.onRequest((request, response) => {
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

/**
 * Triggers when a user adds or edits a shared supply that is missing and sends a notification.
 *
 */
exports.sendMissingSupplyNotification = functions.database.ref('/houses/{houseId}/supplies/{supplyId}/missing/{missing}')
    .onWrite((change, context) => {
      const houseId = context.params.houseId;
      const supplyId = context.params.supplyId;
      const missing = context.params.missing;
      // If un-follow we exit the function.
      if (!change.after.val()) {
        return console.log('Supply changed in  ', houseId, ' : supply #', supplyId, " status: ", missing);
      }
      console.log('New supply created in house  ', houseId, ' : supply #', supplyId, " status: ", missing);

      return "success";

      // Get the list of device notification tokens.
      // const getDeviceTokensPromise = admin.database()
      //     .ref(`/users/${followedUid}/notificationTokens`).once('value');

      // // Get the follower profile.
      // const getFollowerProfilePromise = admin.auth().getUser(followerUid);

      // // The snapshot to the user's tokens.
      // let tokensSnapshot;

      // // The array containing all the user's tokens.
      // let tokens;

      // return Promise.all([getDeviceTokensPromise, getFollowerProfilePromise]).then(results => {
      //   tokensSnapshot = results[0];
      //   const follower = results[1];

      //   // Check if there are any device tokens.
      //   if (!tokensSnapshot.hasChildren()) {
      //     return console.log('There are no notification tokens to send to.');
      //   }
      //   console.log('There are', tokensSnapshot.numChildren(), 'tokens to send notifications to.');
      //   console.log('Fetched follower profile', follower);

      //   // Notification details.
      //   const payload = {
      //     notification: {
      //       title: 'You have a new follower!',
      //       body: `${follower.displayName} is now following you.`,
      //       icon: follower.photoURL
      //     }
      //   };

      //   // Listing all tokens as an array.
      //   tokens = Object.keys(tokensSnapshot.val());
      //   // Send notifications to all tokens.
      //   return admin.messaging().sendToDevice(tokens, payload);
      // }).then((response) => {
      //   // For each message check if there was an error.
      //   const tokensToRemove = [];
      //   response.results.forEach((result, index) => {
      //     const error = result.error;
      //     if (error) {
      //       console.error('Failure sending notification to', tokens[index], error);
      //       // Cleanup the tokens who are not registered anymore.
      //       if (error.code === 'messaging/invalid-registration-token' ||
      //           error.code === 'messaging/registration-token-not-registered') {
      //         tokensToRemove.push(tokensSnapshot.ref.child(tokens[index]).remove());
      //       }
      //     }
      //   });
      //   return Promise.all(tokensToRemove);
      // });
    });



