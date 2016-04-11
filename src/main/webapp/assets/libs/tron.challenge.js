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
             * A reference to the challenge modal to accept/dencline invitations.
             */
            _challengeModal: null,
            
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
                
                this._$challengeModal = $('#challenge-modal');
                
                this._challengeSocket = new SockJS(this._challengesQueue);
                this._challengeStomp = Stomp.over(this._challengeSocket);
                var challengeStomp = this._challengeStomp;

                challengeStomp.connect({}, function () {
                    challengeStomp.subscribe(that._getSubscriptionURL(username), function (response) {
                        that.handleCommand(response);
                    });
                });
                
                $('#btn-challenge-accept').on('click', function (e) {
                    that.accept();
                });
                $('#btn-challenge-deny').on('click', function (e) {
                    that.decline();
                });
                
                return this;
            },
            
            /**
             * TronChallenge._getSubscriptionURL
             * @param string username
             * @returns {String}
             */
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
                response = JSON.parse(response.body);
                
                if (response.command === 'CHALLENGE.NEW') {
                    this._commandNewChallenge(response);
                } else if (response.command === 'CHALLENGE.ACCEPT') {
                    this._commandAcceptedChallenge(response);
                } else if (response.command === 'CHALLENGE.DECLINE') {
                    this._commandDeniedChallenge(response);
                } else if (response.command === 'CHALLENGE.TIMEOUT') {
                    this._commandTimeoutChallenge(response);
                }
            },
            
            /**
             * TronChallenge._comandNewChallenge
             * Handles other people's invitations to start a game.
             * 
             * @param Object response
             * @returns this
             */
            _commandNewChallenge: function (response) {
                this._$challengeModal.attr('data-game-id', response.gameID);
                var $modalBody = this._$challengeModal.find('[class=modal-body]');
                $modalBody.html(response.challenger.name + " has challenged you!");
                this._$challengeModal.modal({show: true});
                
                return this;
            },
            
            /**
             * TronChallenge._commandAcceptedChallenge
             * Handles other people accepting challenges this client has made.
             * 
             * @param Object response
             * @returns this
             */
            _commandAcceptedChallenge: function (response) {
                var $notification = $('<div />').addClass('alert alert-success')
                    .html('<strong>' + response.challenged.name + '</strong> accepted your ' +
                        'invitation. Prepare to play!')
                    .css({
                        display: 'none'
                    });
                $('body > div.container').prepend($notification);
                $notification.slideDown();
                setTimeout(function () {
                    $notification.slideUp();
                }, 3000);
                
                return this;
            },
            
            /**
             * TronChallenge._commandDeniedChallenge
             * Handles other clients denying of our challenges.
             * 
             * @param Object response
             * @returns this
             */
            _commandDeniedChallenge: function (response) {
                var $notification = $('<div />').addClass('alert alert-warning')
                    .html('<strong>' + response.challenged.name + '</strong> declined your ' +
                        'invitation.')
                    .css({
                        display: 'none'
                    });
                $('body > div.container').prepend($notification);
                $notification.slideDown();
                setTimeout(function () {
                    $notification.slideUp();
                }, 3000);
                
                return this;
            },
            
            /**
             * TronChallenge._commandTimeoutChallenge
             * Handles timing out of a challenge invitation. 
             * 
             * @param Object response
             * @returns this
             */
            _commandTimeoutChallenge: function (response) {
                var $notification = $('<div />').addClass('alert alert-danger')
                    .css({
                        display: 'none'
                    });
                if (response.challenger.name === User.name) {
                    $notification.html('Your challenge against <strong>' 
                        + response.challenged.name + '</strong> ' + 
                        'timed out.');
                }
                $('body > div.container').prepend($notification);
                $notification.slideDown();
                setTimeout(function () {
                    $notification.slideUp();
                }, 3000);
                
                return this;
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

            /**
             * TronChallenge.accept
             * Handles the acceptance of a challenge invitation
             * 
             * @returns this
             */
            accept: function () {
                var gameID = this._$challengeModal.attr('data-game-id');
                this._challengeStomp.send(TronChallenge._challengesQueue, {}, JSON.stringify({
                    command: 'CHALLENGE.ACCEPT',
                    data: {
                        gameID: gameID
                    }
                }));
                
                this._$challengeModal.modal('hide');
                
                return this;
            },

            /**
             * TronChallenge.decline
             * Handles declining of an invitation.
             * 
             * @returns this
             */
            decline: function () {
                var gameID = this._$challengeModal.attr('data-game-id');
                this._challengeStomp.send(TronChallenge._challengesQueue, {}, JSON.stringify({
                    command: 'CHALLENGE.DECLINE',
                    data: {
                        gameID: gameID
                    }
                }));
                
                return this;
            }
        };

        window.TronChallenge = TronChallenge;
    })(window);
});
