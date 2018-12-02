'use strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
exports.helloWorld = functions.https.onRequest((request, response) => {
 response.send("Hello from Firebase!");
});

exports.hello = functions.https.onRequest((request, response) => {
 response.send("Hello ");
});


exports.rotateChores = functions.https.onRequest((request, response) => {
	return admin.database().ref('/houses').once("value", (snapshot) => {
 		const houses = snapshot.val()
        var chores = []
        console.log("name", houses[0].name);
        response.send(houses[0].chores);
		// for (var house in houses){
  //           chores = house.chores;

  //           // for (var chore in chores){
  //           //     console.log(chore.name);
  //           // }

  //       }

 	});


});



    // .onCreate((snapshot, context) => {
    //   // Grab the current value of what was written to the Realtime Database.
    //   const original = snapshot.val();
    //   console.log('Uppercasing', context.params.pushId, original);
    //   const uppercase = original.toUpperCase();
    //   // You must return a Promise when performing asynchronous tasks inside a Functions such as
    //   // writing to the Firebase Realtime Database.
    //   // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
    //   return snapshot.ref.parent.child('uppercase').set(uppercase);
    // });