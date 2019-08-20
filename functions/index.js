const functions = require('firebase-functions');

const admin = require('firebase-admin');

admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/notifications/{userId}/{notificationsId}')
    .onWrite((snap, context) => {

        let title = snap.after.val().title;
        let message = snap.after.val().message;

        // get the user ID and notificationsId
        let userId = context.params.userId
        let notificationsId = context.params.notificationsId

        return admin.database().ref('/profiles/' + userId).once('value')
            .then(snap => {
                let token = snap.val().instanceId;

                const payload = {
                    data: {
                        title: title,
                        message: message
                    },
                    token: token
                };

                return admin.messaging().send(payload)
                    .then(function(response) {
                        console.log("Successfully sent message:", response);
                    })
                    .catch(function(error) {
                        console.log("Error sending message:", error);
                    });
            });
    });