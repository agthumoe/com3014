$(function () {
    (function (window) {
        var TronChallenge = {
            /**
             * Defines the URL for a new game.
             */
            _newGameURL: '/game/{gameID}',

            /**
             * The challenge queue URL used for sending commands.
             */
            _challengesQueue: '/queue/challenge',

            /**
             * The challenge topic URL used for subscriptions.
             */
            _challengesTopic: '/user/{username}/topic/challenge',

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
             * The time it takes for a challenge invitation to time out.
             */
            _challengeTimeout: 30000,

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
                this._$challengeModal.modal('show');
                var modal = this._$challengeModal;

                setTimeout(function () {
                    modal.modal('hide');
                }, this._challengeTimeout);

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
                $(".alert").remove();
                var $notification = $('<div />').addClass('alert alert-success')
                    .html('<strong>' + response.challenged.name + '</strong> accepted your ' +
                        'invitation. Prepare to play!')
                    .css({
                        display: 'none'
                    });
                $('body > div.container').prepend($notification);
                $notification.slideDown();
                $notification.remove();

                var that = this;
                setTimeout(function () {
                    window.location.href = that._newGameURL.replace('{gameID}', response.gameID);
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
                $(".alert").remove();
                var $notification = $('<div />').addClass('alert alert-warning')
                    .html('<strong>' + response.challenged.name + '</strong> declined your ' +
                        'invitation.')
                    .css({
                        display: 'none'
                    });
                $('body > div.container').prepend($notification);
                $notification.slideDown();
                console.log("denied");
                setTimeout(function () {
                    $notification.slideUp();
                    $notification.remove();
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
            _handleTimeoutChallenge: function (name) {
                $(".alert").remove();
                var $notification = $('<div />').addClass('alert alert-info')
                    .css({
                        display: 'none'
                    });
                $notification.html('Your challenge against <strong>'
                    + name + '</strong> ' +
                    'timed out.');
                $('body > div.container').prepend($notification);
                $notification.slideDown();
                setTimeout(function () {
                    $notification.slideUp();
                    $notification.remove();
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
            newChallenge: function (userID, name) {
                this._challengeStomp.send(TronChallenge._challengesQueue, {},
                    JSON.stringify({
                        command: 'CHALLENGE.NEW',
                        data: {
                            userID: userID
                        }
                    })
                );

                var that = this;
                setTimeout(function () {
                    that._handleTimeoutChallenge(name);
                }, this._challengeTimeout);

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

                var $notification = $('<div />').addClass('alert alert-success')
                    .html('Prepare to play!')
                    .css({
                        display: 'none'
                    });
                $('body > div.container').prepend($notification);
                $notification.slideDown();

                var that = this;
                setTimeout(function () {
                    window.location.href = that._newGameURL.replace('{gameID}', gameID);
                }, 3000);

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
