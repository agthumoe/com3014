$(function () {
    (function (window) {
        var TronChallenge = {
            /**
             * The challenge queue URL used for sending commands.
             */
            _challengesQueue: '/queue/game/challenge',
            
            /**
             * The challenge topic URL used for subscriptions.
             */
            _challengesTopic: '/user/{username}/topic/game/challenge',       
            
            /**
             * Reference to the challenge socket
             */
            _challengeSocket: null,
            
            /**
             * A reference to the challenge web socket stomp container.
             */
            _challengeStomp: null,
            
            /**
             * TronChallenge.instance
             * Creates a new instance of the TronChallenge class.
             * 
             * @returns TronChallenge
             */
            instance: function () {
                return Object.create(TronChallenge);
            },
            
            /**
             * TronChallenge.init
             * Initialises the TronChallenge instance.
             * 
             * @returns this
             */
            init: function (username) {
                var that = this;
                this._challengeSocket = new SockJS(this._challengesQueue);
                this._challengeStomp = Stomp.over(this._challengeSocket);
                var challengeStomp = this._challengeStomp;

                challengeStomp.connect({}, function () {
                    challengeStomp.subscribe(that._getSubscriptionURL(username), function (response) {
                        that.handleCommand(response);
                    });
                });
                
                return this;
            },
            
            _getSubscriptionURL: function (username) {
                return this._challengesTopic.replace("{username}", username);
            },
            
            /**
             * TronChallenge.handleCommand
             * Handles a TronChallenge command. These are typically "CHALLENGE.NEW", 
             * "CHALLENGE.DENIED", "CHALLENGE.TIMEOUT". 
             * 
             * @param object response
             * @returns void
             */
            handleCommand: function (response) {
                var body = JSON.parse(response.body);
                
                if (body.command === 'CHALLENGE.NEW') {
                    var $challengeModal = $('#challenge-modal');
                    var $modalBody = $challengeModal.find('[class=modal-body]');
                    $modalBody.html(body.challenger.name + " has challenged you!");
                    $challengeModal.show();
                } else if (body.command === 'CHALLENGE.ACCEPT') {
                    
                } else if (body.command === 'CHALLENGE.DENY') {
                    
                } else if (body.command === 'CHALLENGE.TIMEOUT') {
                    
                }
            },
            
            /**
             * TronChallenge.newChallenge
             * Creates a new challenge request and sends it to the server via a web socket.
             * This is how 1 player challenges another player.
             * 
             * @param int userID
             * @returns this
             */
            newChallenge: function (userID) {
                this._challengeStomp.send(TronChallenge._challengesQueue, {}, 
                    JSON.stringify({
                        command: 'CHALLENGE.NEW',
                        data: {
                            userID: userID
                        }
                    })
                );
            
                return this;
            },

            accept: function (challengeID) {

            },

            decline: function (challengeID) {

            }
        };

        window.TronChallenge = TronChallenge;
    })(window);
});
