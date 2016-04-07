$(function () {
    (function (window) {
        var TronActiveUsers = {
            /**
             * Reference to the active users socket
             */
            _activeUsersSocket: null,

            /**
             * Reference to the active user stomp object.
             */
            _activeUsersStompClient: null,

            /**
             * jquery object referencing the online users wrapper.
             */
            _activeUsers: null,

            /**
             * A cache of the usernames that are in the active users list.
             */
            _activeUsersCache: [],

            /**
             * URL to connect to to show that we're active
             */
            _activeUsersTrackerURL: '/queue/activeUsers',

            /**
             * URL of the active users
             */
            _activeUsersURL: '/topic/activeUsers',

            /**
             * Defines the interval at which the manual heart beat will be triggered.
             */
            _heartBeatInterval: 5000,

            /**
             * TronActiveUsers.create
             * Creates a new instance of the TronChat object
             *
             * @return TronChat
             */
            create: function () {
                return Object.create(TronActiveUsers);
            },

            /**
             * TronActiveUsers.init
             * Initialises the object.
             *
             * @param onlineUsersID The ID of the div containing online users.
             * @return void
             */
            init: function (onlineUsersID) {
                var that = this;

                // Wrap the online users div in jQuery object.
                this._activeUsers = $(onlineUsersID);

                this._activeUsersSocket = new SockJS(this._activeUsersTrackerURL);
                this._activeUsersStompClient = Stomp.over(this._activeUsersSocket);
                var activeUsersStompClient = this._activeUsersStompClient;

                activeUsersStompClient.connect({}, function () {
                    activeUsersStompClient.subscribe(that._activeUsersURL, function (response) {
                        that.refreshActiveUsersList(response);
                    });
                });

                setInterval(function () {
                    that._activeUsersStompClient.send(that._activeUsersTrackerURL, {}, "{}");
                }, this._heartBeatInterval);
            },

            /**
             * TronActiveUsers.disconnect
             * Disconnect handler
             *
             * @return void
             */
            disconnect: function () {
                if (this._activeUsersStompClient !== null) {
                    this._activeUsersStompClient.disconnect();
                }
            },

            /**
             * TronActiveUsers.refreshActiveUsersList
             * Displays a message provided by the server
             *
             * @param message The JSON message
             * @return void
             */
            refreshActiveUsersList: function (response) {
                var onlineUsers = JSON.parse(response.body);

                var usersToAdd = this._getUsersToAdd(onlineUsers);
                var usersToRemove = this._getUsersToRemove(onlineUsers);

                console.log(this._activeUsersCache);

                // Add the users to the DOM and to the internal cache so we can track them easily.
                for(var i in usersToAdd) {
                    this._activeUsersCache.push(usersToAdd[i].username);

                    var li = $('<li />')
                        .attr({
                            "data-username": usersToAdd[i].username
                        })
                        .addClass('list-group-item')
                        .append(usersToAdd[i].username)
                        .append(this._getChallengeHTML());
                    this._activeUsers.append(li);
                }

                // Remove the users from the DOM and the internal cache.
                for(var i in usersToRemove) {
                    $('[data-username=' + usersToRemove[i] + ']').remove();
                    var i = this._activeUsersCache.indexOf(usersToRemove[i]);
                    if (i > -1) {
                        this._activeUsersCache.splice(i, 1);
                    }
                }
            },

            /**
             * TronActiveUsers._getUsersToAdd
             * Retrieves a list of users that aren't already in the active users list so they can be added.
             *
             * @param latestActiveUsersList A JSON Object containing the latest active users frmo the server.
             * @return An array of the users to add.
             */
            _getUsersToAdd: function (latestActiveUsersList) {
                var usersToAdd = [];

                for (var i in latestActiveUsersList) {
                    if (this._activeUsersCache.indexOf(latestActiveUsersList[i].username) === -1) {
                        usersToAdd.push(latestActiveUsersList[i]);
                    }
                }

                return usersToAdd;
            },

            /**
             * TronActiveUsers._getUsersToRemove
             * Retrieves a list of users that need removing from the active users list.
             *
             * @param latestActiveUsersList A list of the latest active users from the serve.
             * @return An array of users that need removing from the list.
             */
            _getUsersToRemove: function (latestActiveUsersList) {
                var latestActiveUsersUsernames = [];
                var usersToRemove = [];
                var i;

                // Create an array of usernames from the latest active users list.
                for (i in latestActiveUsersList) {
                    latestActiveUsersUsernames.push(latestActiveUsersList[i].username);
                }

                for (i in this._activeUsersCache) {
                    if (latestActiveUsersUsernames.indexOf(this._activeUsersCache[i]) === -1) {
                        usersToRemove.push(this._activeUsersCache[i]);
                    }
                }

                return usersToRemove;
            },

            /**
             * TronActiveUsers._getChallgeHTML
             * Gets the challenge button HTML
             *
             * @return jQuery object containing the challenge anchor tag
             */
            _getChallengeHTML: function () {
                return $('<a />')
                    .attr({
                        href: '#',
                        role: 'button'
                    })
                    .addClass('btn btn-xs btn-default pull-right')
                    .append($('<i />').addClass('fa fa-bolt'));
            }
        };

        window.TronActiveUsers = TronActiveUsers;
    })(window);
});
