$(function () {
    (function (window) {
        var TronChat = {
            /**
             * A reference to the SocKJS Socket
             */
            _socket: null,

            /**
             * Reference to the StompClient
             */
            _stompClient: null,

            /**
             * jQuery object referencing the input field.
             */
            _input: null,

            /**
             * jQuery object referencing the output area.
             */
            _output: null,

            /**
             * The Tracker URL
             */
            _trackerURL: null,

            /**
             * The chat history URL
             */
            _historyURL: null,

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
            init: function (inputID, outputID, trackerURL, chatHistoryURL) {
                var that = this;

                this._trackerURL = trackerURL;
                this._historyURL = chatHistoryURL;

                // Wrap the output div in a jquery object
                this._output = $(outputID);

                // Wrap the input ID in a jQuery object.
                this._input = $(inputID);

                // Apply on enter send message.
                this._input.keyup(function (e) {
                    var k = e.which;
                    if (k === 13) {
                        that.send();
                    }
                });

                // Open up a socket
                this._socket = new SockJS(trackerURL);

                // Stomp on the socket to make subscribing to new messages easier.
                this._stompClient = Stomp.over(this._socket);
                var sc = this._stompClient;

                sc.connect({}, function(frame) {
                    sc.subscribe(chatHistoryURL, function(message){
                        that.displayMessage(message.body);
                    });

                    that._push('Hi, I just joined the chat');
                });

                // Register window unload functions so we disconnect properly.
                window.onunload = this.disconnect;
                window.onbeforeunload = this.disconnect;
            },

            /**
             * TronChat.disconnect
             * Disconnect handler
             *
             * @return void
             */
            disconnect: function () {
                if (this._stompClient !== null) {
                    this._stompClient.disconnect();
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
                this._stompClient.send(this._trackerURL, {}, JSON.stringify({
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
