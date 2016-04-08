$(function () {
    (function (window) {
        var TronChallenge = {

            _challengesQueue: '/queue/challenge',
            _challengesTopic: '/topic/challenge',

            _challengeSocket: null,

            _challengeStomp: null,

            init: function () {
                TronChallenge._challengeSocket = new SockJS(this._challengesQueue);
                TronChallenge._challengeStomp = Stomp.over(this._challengeSocket);
                var challengeStomp = TronChallenge._challengeStomp;

                challengeStomp.connect({}, function () {
                    challengeStomp.subscribe(TronChallenge._challengesTopic, function (response) {
                        TronChallenge.handleCommand(response);
                    });
                });
            },

            handleCommand: function (response) {
                console.log(response);
            },

            newChallenge: function (userID) {
                TronChallenge._challengeStomp.send(TronChallenge._challengesQueue, {},
                    JSON.stringify({
                        command: 'CHALLENGE.ACCEPT',
                        data: {
                            userID: userID
                        }
                    })
                );
            },

            accept: function (challengeID) {

            },

            decline: function (challengeID) {

            }
        };

        window.TronChallenge = TronChallenge;
    })(window);
});
