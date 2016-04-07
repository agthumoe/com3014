$(function () {
    (function (window) {
        var TronChat = {
            /**
             * A reference to the SocKJS Socket
             */
            _chatSocket: null,

            /**
             * Reference to the StompClient
             */
            _chatStompClient: null,

            /**
             * Reference to the active users socket
             */
            _activeUsersSocket: null,

            /**
             * Reference to the active user stomp object.
             */
            _activeUsersStompClient: null,

            /**
             * jQuery object referencing the input field.
             */
            _input: null,

            /**
             * jQuery object referencing the output area.
             */
            _output: null,

            /**
             * jquery object refrencing the online users wrapper.
             */
            _onlineUsers: null,

            /**
             * The Tracker URL
             */
            _chatTrackerURL: '/queue/chat',

            /**
             * The chat history URL
             */
            _chatURL: '/topic/chat',

            /**
             * URL to connect to to show that we're active
             */
            _activeUsersTrackerURL: '/queue/activeUsers',

            /**
             * URL of the active users
             */
            _activeUsersURL: '/topic/activeUsers',

            /**
             * TronChat.create
             * Creates a new isntance of the TronChat object
             *
             * @return TronChat
             */
            create: function () {
                return Object.create(TronChat);
            },

            /**
             * TronChat.init
             * Initialises the object.
             *
             * @param inputID ID of the entity containing input text
             * @param outputID ID of entity where output will be placed.
             * @param trackerURL URL of to send messages to
             * @param chatHistoryURL URL of the chat history, we subscribe to this with Stomp
             * @return void
             */
            init: function (inputID, outputID, onlineUsersID) {
                var that = this;

                // Wrap the output div in a jquery object
                this._output = $(outputID);

                // Wrap the input ID in a jQuery object.
                this._input = $(inputID);

                // Wrap the online users div in jQuery object.
                this._onlineUsers = $(onlineUsersID);

                // Apply on enter send message.
                this._input.keyup(function (e) {
                    var k = e.which;
                    if (k === 13) {
                        that.send();
                    }
                });

                // Open up a socket
                this._chatSocket = new SockJS(this._chatTrackerURL);
                this._chatStompClient = Stomp.over(this._chatSocket);
                var sc = this._chatStompClient;

                sc.connect({}, function(frame) {
                    sc.subscribe(that._chatURL, function(message){
                        that.displayMessage(message.body);
                    });
                });

                this._activeUsersSocket = new SockJS(this._activeUsersTrackerURL);
                this._activeUsersStompClient = Stomp.over(this._activeUsersSocket);
                var activeUsersStompClient = this._activeUsersStompClient;

                activeUsersStompClient.connect({}, function () {
                    activeUsersStompClient.subscribe(that._activeUsersURL, function (response) {
                        console.log(response);
                    });
                });

                // Register window unload functions so we disconnect properly.
                window.onunload = that.disconnect;
                window.onbeforeunload = that.disconnect;
            },

            /**
             * TronChat.disconnect
             * Disconnect handler
             *
             * @return void
             */
            disconnect: function () {
                if (this._chatStompClient !== null) {
                    this._chatStompClient.disconnect();
                }
            },

            /**
             * TronChat.send
             * Sends a message to the server.
             *
             * @return void
             */
            send: function () {
                this._push(this._input.val());
                this._input.val('');
            },

            /**
             * TronChat._push
             * Pushes a specified message to the server
             *
             * @param m The message to send
             * @return void
             */
            _push: function (m) {
                this._chatStompClient.send(this._chatTrackerURL, {}, JSON.stringify({
                    message: m
                }));
            },

            /**
             * TronChat.displayMessage
             * Displays a message provided by the server
             *
             * @param message The JSON message
             * @return void
             */
            displayMessage: function (message) {
                var m = JSON.parse(message);
                var div = $('<div />').addClass('message');

                div.append($('<span />').addClass('owner').html(m.username + ': '));

                div.append($('<span />').addClass('message-body').html(m.message));

                div.append($('<span />').addClass('time').html(m.currentTime));

                this._output.prepend(div);
            },

            /**
             * TronChat.setMessagesMaxHeight
             * Sets the max-height css attribute to the specified height for messages. This inherently sets
             * overflow to auto.
             *
             * @param height The height to set
             * @return void
             */
            setMessagesMaxHeight: function (height) {
                this._output.css({
                    'overflow': 'auto',
                    'max-height': height
                });
            },
        };

        window.TronChat = TronChat;
    })(window);
});
