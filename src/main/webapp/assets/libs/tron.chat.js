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
            _chatTrackerURL: '/queue/chat',

            /**
             * The chat history URL
             */
            _chatURL: '/topic/chat',

            /**
             * TronChat.instance
             * Creates a new isntance of the TronChat object
             *
             * @return TronChat
             */
            instance: function () {
                return Object.create(TronChat);
            },

            /**
             * TronChat.init
             * Initialises the object.
             *
             * @param inputID ID of the entity containing input text
             * @param outputID ID of entity where output will be placed.
             * @return void
             */
            init: function (inputID, outputID) {
                var that = this;

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
                this._chatSocket = new SockJS(this._chatTrackerURL);
                this._chatStompClient = Stomp.over(this._chatSocket);
                var sc = this._chatStompClient;

                sc.connect({}, function(frame) {
                    sc.subscribe(that._chatURL, function(message){
                        that.displayMessage(message.body);
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

                div.append($('<span />').addClass('owner').html(m.name + ': '));

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
