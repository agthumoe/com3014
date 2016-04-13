$(function() {
    (function (window) {
        var TronPreGame = {
            /**
             * Defines the URL to send game updates to.
             */
            _gameQueue: '/queue/game',
            
            /**
             * Defines the subscription URL specific to the logged in user. 
             */
            _gameTopic: '/user/{userID}/topic/game',
            
            /**
             * A reference to the game socket.
             */
            _gameSocket: null,
            
            /**
             * A reference to the STOMP interface being used.
             */
            _gameStomp: null,
            
            /**
             * The unique GAME ID
             */
            _gameID: null,
            
            /**
             * A reference to the game in progress.
             */
            _game: null,
            
            /**
             * Quick reference to determine if the game has been initialised.
             */
            _gameInitialised: false,
            
            /**
             * Indicator of whether this is the challenged or challenger.
             */
            _role: null,
            
            /**
             * #.init
             * Initialises the TronGameFactory instance.
             * 
             * @returns this
             */
            init: function () {
                var that = this;
                
                this._gameSocket = new SockJS(this._gameQueue);
                this._gameStomp = Stomp.over(this._gameSocket);
                var gameStomp = this._gameStomp;
                
                var path = window.location.pathname.split('/');
                this._gameID = path[path.length - 1];
                
                this._gameTopic = this._gameTopic.replace('{userID}', User.username);

                gameStomp.connect({}, function () {
                    gameStomp.subscribe(that._gameTopic, function (response) {
                        that.handle(response);
                    });
                
                    gameStomp.send(that._gameQueue, {}, JSON.stringify({
                        command: 'GAME.READY',
                        data: {
                            gameID: that._gameID,
                            height: $(window).height(),
                            width: $(window).width()
                        }
                    }));
                });
                
                return this;
            },
            
            /**
             * #.instance
             * Creates an instance of the Tron Game Factory
             * 
             * @returns TronGameFactory instance
             */
            instance: function () {
                return Object.create(TronPreGame);
            },
            
            /**
             * #.handle
             * Handles a message from the server before the game starts.
             * 
             * @param json response
             * @returns void
             */
            handle: function (response) {
                response = JSON.parse(response.body);
                
                if (response.command === "GAME.PREP") {
                    
                    this._gameStomp.send(this._gameQueue, {}, JSON.stringify({
                        command: 'GAME.PREP_RECEIVED',
                        data: {
                            gameID: this._gameID
                        }
                    }));
                    
                    this._role = response.role;
                    this.initGame(response.height, response.width, response.role);
                    
                } else if (response.command === 'GAME.START') {
                    this.startGame();
                }
            },
            
            /**
             * #.initGame
             * Initialises the game with the players and canvas size etc.
             * 
             * @returns void
             */
            initGame: function (height, width, role) {
                var that = this;
                
                if (!this._gameInitialised) {
                    this._gameInitialised = true;
                    
                    this._game = TronGame.instance();
                    
                    $('body').append($('<div />').attr({
                        id: 'stage',
                        'tron-height': height,
                        'tron-width': width
                    }));

                    this._game.init('stage', function () {  
                        
                        // Setup required for the challenger player.
                        if (role === 'CHALLENGER') {
                            Crafty.e('CyanPlayer, ControllablePlayer, Challenger').attr({
                                x: 100,
                                y: 100,
                                rotation: 90
                            })
                            .setStomp(that._gameStomp, that._gameID);
                            
                            Crafty.e('OrangePlayer, RemotePlayer, Challenged').attr({
                                x: width - 100,
                                y: height - 100,
                                rotation: -90
                            })
                            .setStomp(that._gameStomp, that._gameTopic);
                        }
                        
                        // Setup required for the challenged player.
                        if (role === 'CHALLENGED') {
                            Crafty.e('CyanPlayer, RemotePlayer, Challenger').attr({
                                x: 100,
                                y: 100,
                                rotation: -90
                            })
                            .setStomp(that._gameStomp, that._gameTopic);
                        
                            Crafty.e('OrangePlayer, ControllablePlayer, Challenged').attr({
                                x: width - 100,
                                y: height - 100,
                                rotation: -90
                            })
                            .setStomp(that._gameStomp, that._gameID);
                        }
                        
                    });
                }
            },
            
            startGame: function (startIn) {
                if (this._role === 'CHALLENGED') {
                    var pos = Crafty('Challenged').get(0).pos();
                    Crafty.log(pos);
                } else {
                    
                }
                
                this._game.start();
            },
            
            endGame: function () {
                
            },
            
            /**
             * #.getGameSocket
             * 
             * @returns The game Socket
             */
            getGameSocket: function () {
                return this._gameSocket;
            },
            
            /**
             * #.getGameStomp
             * 
             * @returns The game STOMP
             */
            getGameStomp: function () {
                return this._gameStomp;
            }
        };
        window.TronPreGame = TronPreGame;
        
        var TronGame = {
            /**
             * Global configuration of TronGame
             */
            _config: {
                /**
                 * Defines the starting scene for the game.
                 */
                startingScene: 'Main',

                /**
                 * Defines the timeout for explosions. Once timed out, explosion will destroy,
                 */
                explosionTimeout: 1500,

                /**
                 * Defines the time a trail stays alive for.
                 */
                trailTimeout: 10000,

                /**
                 * Defines whether logging is enabled or not.
                 */
                logging: true,

                /**
                 * Defines the default paths to look for images and audio files.
                 */
                assetPaths: {
                    audio: "../../assets/audio/",
                    images: "../../assets/images/game/"
                },

                /**
                 * Defines the default loading functions
                 */
                assetLoadHandlers: {
                    onLoad: function () {
                        Crafty.log("Assets loaded");
                    },

                    onProgress: function (obj) {
                        Crafty.log("Assets loaded: " + Math.round(obj.percent) + "%");
                    },

                    onError: function (asset) {
                        Crafty.log("Error loading " + asset);
                    }
                }
            },

            /**
             * A singular instance of TronGame
             */
            _instance: null,

            /**
             * Determines if the assets have been loaded or not.
             */
            _assetsLoaded: false,

            /**
             * Flag to determine if components have been defined or not.
             */
            _componentsDefined: false,
            
            /**
             * Dertermines whether the game has been intiialised.
             */
            _initialised: false,

            /**
             * Determines if the game is paused or not.
             */
            _paused: false,
            
            /**
             * Determines if the game has been staretd or not. Should not be directly accessed.
             */
            _started: false,

            /**
             * A reference to the canvas used to render the game. It's a DOM element, nothing
             * to do with Crafty.
             */
            _canvas: null,

            /**
             * A log of all defined scenes
             */
            _scenes: [],

            /**
             * Defines the assets of TronGame
             */
            _assets: {
                imags: ['logo_nobg.png'],
                sprites: {
                    "bike_cyan.png": {
                        tile: 32,
                        tileh: 32,
                        map: {
                            Sprite_BikeCyan: [0, 0]
                        }
                    },
                    "bike_orange.png": {
                        tile: 32,
                        tileh: 32,
                        map: {
                            Sprite_BikeOrange: [0, 0]
                        }
                    },
                    "explosion.png": {
                        tile: 64,
                        tileh: 64,
                        map: {
                            Sprite_Explosion_0: [0, 0],
                            Sprite_Explosion_1: [1, 0],
                            Sprite_Explosion_2: [2, 0],
                            Sprite_Explosion_3: [3, 0],
                            Sprite_Explosion_4: [4, 0],
                            Sprite_Explosion_5: [5, 0],
                        }
                    },
                    "trail.png": {
                        tile: 32,
                        tileh: 32,
                        map: {
                            Sprite_Trail: [0, 0]
                        }
                    },
                    "speaker.png": {
                        tile: 512,
                        tileh: 512,
                        map: {
                            Sprite_SpeakerActive: [0, 0],
                            Sprite_SpeakerMuted: [1, 0]
                        }
                    }
                },
                audio: {
                    Background: ["run_wild_edited.mp3"],
                    Explosion: ["explosion.mp3"]
                }
            },

            /**
             * TronGame.instance
             * Creates a new instance of the TronGame game. Provides a method for accessing the TronGame
             * signleton as we only ever really want 1 instance of the game in play. Anything else
             * would be silly.
             *
             * Games should be initialised with the init method once retrieved. This handles loading
             * of assets and setting up the canvas.
             */
            instance: function () {
                if (!TronGame._instance) {
                    TronGame._instance = Object.create(TronGame);
                }

                return TronGame._instance;
            },

            /**
             * TronGame.init
             * Initialises the game by creating the game specific objects and
             * defining the map.
             *
             * @returns this
             */
            init: function(canvasID, callback) {
                Crafty.logginEnabled = TronGame._config.logging;

                Crafty.log("Initialising TronGame");

                this._setupCanvas(canvasID);
                this._defineComponents();
                this._loadAssets();
                
                // Wait for the game to have all assets loaded before entering the main scene.
                (function enterMainScene(game, callback) {
                    if (!TronGame._assetsLoaded) {
                        setTimeout(enterMainScene, 100, game, callback);
                        return;
                    }
                    
                    game.enterScene('Main', game);
                    callback();
                    game._initialised = true;
                    game.lockPlayers();
                })(this, callback);

                return this;
            },

            /**
             * TronGame.start
             * Starts the game.
             *
             * @param h Height of the game window
             * @param w Width of the game window
             * @parama element The element to use for the game.
             *
             * @return this
             */
            start: function () {
                
                var that = this;
                
                // Wait for the game to have all assets loaded before entering the main scene.
                (function startGame(attempt, game) {
                    if (attempt > 5) {
                        Crafty.log("Failed to start game");
                        return;
                    } else if (!game._initialised) {
                        Crafty.log("Failed to start game on attempt " + attempt);
                        setTimeout(startGame, 500, ++attempt, game);
                        return;
                    } else {
                        
                        Crafty.log("Starting TronGame attempt " + attempt);
                        // CODE TO START THE GAME.
                        that.unlockPlayers();
                    }
                })(1, this);
                
                return this;
            },

            /**
             * TronGame.stop
             * Stops the game completely and optionally resets the game state.
             *
             * @param bool If true, clears the game state resetting everything.
             * @return this
             */
            unload: function (clearState) {
                Crafty.log("Unloading game");

                // Default clear state to false if not set.
                clearState = (typeof clearState === "undefined" ? false : clearState);

                Crafty.audio.stop();
                Crafty.stop(clearState);

                return this;
            },

            /**
             * TronGame.pause
             * Pauses this game.
             *
             * @returns this
             */
            pause: function () {
                Crafty.log("Pausing game");

                if (!this._paused) {
                    Crafty.pause();
                    this._paused = !this._paused;
                }

                return this;
            },

            /**
             * TronGame.unpause
             * Unpauses a paused game.
             *
             * @returns this
             */
            unpause: function () {
                Crafty.log("Unpausing game");

                if (this._paused) {
                    Crafty.pause();
                    this._paused = !this._paused;
                }

                return this;
            },

            /**
             * TronGame.isPaused
             * Determines whether the game is paused or not.
             *
             * @return bool
             */
            isPaused: function () {
                return this._paused;
            },

            /**
             * Adds a scene to TronGame
             *
             * @param string key
             * @param function scene
             * @returns this
             */
            addScene: function (key, scene) {
                TronGame._scenes.push(key);
                Crafty.defineScene(key, scene);
            },

            /**
             * Enters the defined scene.
             *
             * @param string key
             * @returns void
             */
            enterScene: function (key, data) {
                if (TronGame._scenes.indexOf(key) === -1) {
                    Crafty.error('Scene undefined: ' + key);
                } else {
                    Crafty.log('Entering Scene: ' + key);
                    Crafty.enterScene(key, data);
                }
            },

            /**
             * Sets the global configuration.
             *
             * @param Object config
             * @returns void
             */
            setConfig: function (config) {
                config = config || {};
                for(var i in config) {
                    TronGame._config[i] = config[i];
                }
            },
            
            /**
             * #.lockPlayers
             * Locks all players.
             * 
             * @returns this
             */
            lockPlayers: function () {
                Crafty.log('Locking all players');
                Crafty('Player').each(function (p) {
                    this.lock();
                });
                
                return this;
            },
            
            /**
             * #.unlockPlayers
             * Unlocks all players.
             * 
             * @returns this
             */
            unlockPlayers: function () {
                Crafty.log('Unlocking all players');
                Crafty('Player').each(function (p) {
                    this.unlock();
                });
                
                return this;
            },

            /**
             * TronGame._setupCanvas
             * Creates the canvas and initialises the game. Then pauses the game and waits for
             * it to start.
             *
             * @param string canvas
             * @returns void
             */
            _setupCanvas: function (canvasID) {
                this._canvas = document.getElementById(canvasID);
                var canvas = $(this._canvas);
                
                // Get all the heights and widths to position the canvas in the centre of the 
                // screen
                var height = canvas.attr('tron-height') * 1;
                var width = canvas.attr('tron-width') * 1;
                var windowHeight = $(window).height();
                var windowWidth = $(window).width();
                
                // Calculate required spacing for the canvas from the edges
                marginTop = (windowHeight / 2) - (height / 2);
                marginLeft = (windowWidth / 2) - (width / 2) - 10;
                
                if (this._canvas
                    && typeof this._canvas !== "undefined"
                    && this._canvas.attributes.hasOwnProperty('tron-height')
                    && this._canvas.attributes.hasOwnProperty('tron-width')) {

                    Crafty.log("Setting up canvas with predefined properties");
                    
                    canvas.css({
                        position: 'fixed',
                        'margin-left': marginLeft,
                        'margin-top': marginTop
                    });
                    
                    Crafty.init(width, height, this._canvas);
                } else {
                    Crafty.log("Setting up default canvas");
                    Crafty.init();
                }
            },

            /**
             * Loads all necessary images, sprites and audio files.
             */
            _loadAssets: function (onLoad, onProgress, onError) {
                if (!TronGame._assetsLoaded) {
                    onLoad = onLoad || this._config.assetLoadHandlers.onLoad;
                    onProgress = onProgress || this._config.assetLoadHandlers.onProgress;
                    onError = onError || this._config.assetLoadHandlers.onError;

                    // Apply the configuration for paths.
                    Crafty.paths(TronGame._config.assetPaths);

                    Crafty.load(this._assets, function () {
                        TronGame._assetsLoaded = true;
                        onLoad();
                    }, onProgress, onError);
                }
            },

            /**
             * TronGame._defineComponents
             * Defines the game objects used in TronGame.
             */
            _defineComponents: function () {
                // Ensure components haven't been defined yet.
                if (!TronGame._componentsDefined) {
                    /**
                     * Define a generic square object.
                     */
                    Crafty.c('Square', {
                        required: '2D, Canvas, Color'
                    });

                    /**
                     * Defines a debug point used only when debugging things.
                     *
                     * Relatively useful.
                     */
                    Crafty.c('DebugPoint', {
                        required: 'Square',

                        init: function () {
                            this.h = 2;
                            this.w = 2;
                            this.color('red');
                        }
                    });

                    /**
                     * Defines a bike trail.
                     */
                    Crafty.c('Trail', {
                        /**
                         * Define required components
                         */
                        required: 'Square, Collision, Tween',

                        /**
                         * A reference to the player associated with this trail object.
                         */
                        _player: null,

                        /**
                         * Determines whether this trail object is active or not.
                         */
                        _active: false,

                        /**
                         * Initialise object.
                         */
                        init: function () {
                            this.h = 6;
                            this.w = 1;
                            this.origin('center');
                            this.checkHits('Player');
                            this.z = 5;

                            // Activate the trail after some time  to avoid blowing ourselves up.
                            setTimeout(function (trail) {
                                trail.activate();
                            }, 200, this);
                        },

                        /**
                         * Define events for the trail.
                         */
                        events: {
                            HitOn: function (hit) {
                                var o = hit[0].obj;
                                if (o.has('Player') && this._active) {
                                    o.explode();
                                }
                            }
                        },

                        /**
                         * #.setPlayer
                         * Sets the player associated with this object.
                         *
                         * @param player
                         * @return this
                         */
                        setPlayer: function (player) {
                            this._player = player;
                            return this;
                        },

                        /**
                         * #.getPlayer
                         * Retrieves the player this Trail object is associated with.
                         *
                         * @returns Player
                         */
                        getPlayer: function () {
                            return this._player;
                        },

                        /**
                         * #.isActive
                         * Determines if this object is active or not.
                         *
                         * @returns bool
                         */
                        isActive: function () {
                            return this._active;
                        },

                        /**
                         * #.activate
                         * Activates this object making it collidable
                         *
                         * @returns this
                         */
                        activate: function () {
                            this._active = true;

                            // Schedule time to remove the trail
                            setTimeout(function (trail) {
                                // Tweem the trail out so it's invisible.
                                trail.tween({
                                    alpha: 0
                                }, 500, "easeOutQuad");

                                // Set another event to remove the trail from the map.
                                setTimeout(function (trail) {
                                    trail.getPlayer().removeTrail(trail);
                                    trail.destroy();
                                }, 500, trail);
                            }, TronGame._config.trailTimeout, this);

                            return this;
                        }
                    });

                    /**
                     * Define a player object.
                     */
                    Crafty.c('Player', {
                        /**
                         * Define required components.
                         */
                        required: '2D, Canvas, Collision',

                        /**
                         * Defines the absolute centre point of this object.
                         */
                        _absoluteCentre: {
                            x: 0,
                            y: 0
                        },

                        /**
                         * Defines the movement vector for this object
                         */
                        _vector: new Crafty.math.Vector2D(),

                        /**
                         * Defines the current magnitude of our movement vector.
                         */
                        _magnitude: 1,

                        /**
                         * Minimum required magnitude before a trail appears.
                         */
                        _trailRequiredMagnitude: 150,

                        /**
                         * The trail objects associated with this Player
                         */
                        _trail: [],

                        /**
                         * Defines the colour of this players trail.
                         */
                        _trailColour: 'red',

                        /**
                         * Defines whether this object can be manipulated or not.
                         */
                        _lock: false,                        
                        
                        /**
                         * Status of this object.
                         */
                        _status: 'ACTIVE',

                        /**
                         * Initialises the objects properties.
                         */
                        init: function () {
                            this.origin('center');
                            this.checkHits('Player', 'Trail');
                            this.z = 10;

                            this.collision([
                                10, 0,
                                10, 32,
                                22, 32,
                                22, 0,
                            ]);
                        },

                        /**
                         * Defines events to hook in to.
                         */
                        events: {
                            EnterFrame: function () {
                                // Ensure the player doesn't go flying off the screen never to be
                                // seen again
                                if(this.x > Crafty.viewport.width + this.h) {
                                    this.x = 0-this.h;
                                }

                                if(this.x < 0-this.h) {
                                    this.x = Crafty.viewport.width;
                                }

                                if(this.y > Crafty.viewport.height + this.h) {
                                    this.y = 0-this.h;
                                }

                                if(this.y < 0-this.h) {
                                    this.y = Crafty.viewport.height;
                                }

                                // Calculate the vectors for the direction of the object.
                                this._vector.x = Math.sin(Crafty.math.degToRad(this._rotation));
                                this._vector.y = -Math.cos(Crafty.math.degToRad(this._rotation));
                                this._absoluteCentre.x = this.x + this._origin.x;
                                this._absoluteCentre.y = this.y + this._origin.y;
                            },

                            HitOn: function (collision) {
                                if (collision[0].obj.has('Player')) {
                                    this.explode();
                                }
                            },

                            Move: function (oldPosition) {
                                if (!this.isLocked()) {
                                    // If we're over a configurable magnitude we want to begin creating
                                    // a trail. Place the trail objects right behind the bike by negating
                                    // the bikes vector and giving it a magnitude of 20, then adding
                                    // the vector to the coordinates of the objects centre point.
                                    //
                                    // The centrepoint is tracked on a frame by frame basis.
                                    if (this._magnitude >= this._trailRequiredMagnitude) {
                                        var t = Crafty.e('Trail').attr({
                                            x: (this._absoluteCentre.x),
                                            y: (this._absoluteCentre.y - 2),
                                            rotation: this.rotation
                                        })
                                        .color(this._trailColour)
                                        .setPlayer(this);

                                        this._trail.push(t);
                                    }
                                }
                            }
                        },

                        /**
                         * #.lock
                         * Sets the lock on this object to true.
                         *
                         * @return this
                         */
                        lock: function () {
                            this._lock = true;
                            return this;
                        },
                        
                        /**
                         * #.unlock
                         * Unlocks this player object.
                         * 
                         * @returns this
                         */
                        unlock: function () {
                            this._lock = false;
                            return this;
                        },

                        /**
                         * #.isLocked
                         * Determines if this object has been locked.
                         *
                         * @return bool
                         */
                        isLocked: function () {
                            return this._lock;
                        },

                        /**
                         * #.explode
                         * Explodes this object replacing itself with an explosion object.
                         *
                         * @returns void
                         */
                        explode: function () {
                            if (!this.isLocked()) {
                                // Let everything know we've been muted.
                                this.lock();

                                // Rotate back into the 0d position.
                                this.rotate = 0;
                                
                                // Set the exploded state
                                this._status = 'EXPLODED';

                                // Create a new explision object and offset it by 16 pixels moving it
                                // up and left so it centres ove the top of the bike.
                                //
                                // We offset by 15 because the player is 32x23 and the explosion is
                                // 64x64 so to centre one over the other we must move by 16.
                                var e = Crafty.e('Explosion');
                                e.attr({
                                    x: this.x - 16,
                                    y: this.y - 16
                                });

                                setTimeout(function (player) {
                                    player.destroy();
                                }, 100, this);
                            }
                        },

                        /**
                         * #.removeTrail
                         * Removes a trail object from this Players trail.
                         *
                         * @param Object the Trail object.
                         * @return this
                         */
                        removeTrail: function (trail) {
                            var i = this._trail.indexOf(trail);
                            if (i > -1) {
                                this._trail.splice(i, 1);
                            }

                            return this;
                        }
                    });
                    
                    Crafty.c('Challenger', {});
                    Crafty.c('Challenged', {});

                    /**
                     * Defines an enemy player.
                     */
                    Crafty.c('OrangePlayer', {
                        /**
                         * Define requierd components.
                         */
                        required: 'Player, Sprite_BikeOrange',

                        /**
                         * Initialise the objects parameters.
                         */
                        init: function () {
                            this._trailColour = 'rgb(255, 120, 0)';
                        }
                    });

                    /**
                     * Defines a friendly player.
                     */
                    Crafty.c('CyanPlayer', {
                        /**
                         * Define requierd components.
                         */
                        required: 'Player, Sprite_BikeCyan',

                        /**
                         * Initialise the objects parameters.
                         */
                        init: function () {
                            this._trailColour = 'cyan';
                        }
                    });
                    
                    /**
                     * A remote player updates by the server.
                     */
                    Crafty.c('RemotePlayer', {
                        /**
                         * Required modules.
                         */
                        required: 'Player, Motion',
                        
                        /**
                         * Reference to the STOMP interface to commuicate with the server.
                         */
                        _stomp: null,
                        
                        /**
                         * Defines the remote attributes for this object.
                         */
                        _remoteAttributes: {
                            vx: 0,
                            vy: 0,
                            status: 'ACTIVE',
                        },
                        
                        init: function () {
                            this.origin('center');
                        },
                        
                        events: {
                            EnterFrame: function () {
                                if (!this.isLocked()) {
                                    this.vx = this._remoteAttributes.vx;
                                    this.vy = this._remoteAttributes.vy;
                                    this.rotation = this._remoteAttributes.rotation;
                                    this._magnitude = this._remoteAttributes.magnitude;
                                }
                            }
                        },
                        
                        /**
                         * #.setStomp
                         * Sets the STOMP Interface internally and subscribes for updates.
                         * 
                         * @param STOMP stomp
                         * @param string url
                         * @returns this
                         */
                        setStomp: function (stomp, url) {
                            this._stomp = stomp;
                            this._stomp.debug = null;
                            var that = this;
                            stomp.subscribe(url, function (response) {
                                var body = JSON.parse(response.body);
                                if (body.command === 'GAME.UPDATE') {
                                    // Look at the status and determine if we should blow up or not
                                    if (body.status === 'EXPLODED') {
                                        that.lock();
                                        that.vx = 0;
                                        that.vy = 0;
                                        that.explode();
                                    } else {
                                        that._remoteAttributes = body;
                                    }
                                }
                            });
                            
                            return this;
                        }
                    });

                    /**
                     * Defines a controllable player.
                     *
                     * This must be listed when calling Crafty.e after the playe type.
                     *
                     * @example Crafty.e('CyanPlayer, ControllablePlayer')
                     */
                    Crafty.c('ControllablePlayer', {
                        /**
                         * Object requires motion and keyboard component. Angular motion is handled
                         * manually and so AngularMotion is nto required.
                         */
                        required: "Player, Motion, Keyboard",

                        /**
                         * Defines the magnitude used to multiply the movement vector to denote the
                         * maximum velocity the object can travel in in the x and y planes.
                         */
                        _maxMagnitude: 300,

                        /**
                         * Defines the magnitude to apply to the objects acceleration properties when
                         * manipulating the objects movement vector.
                         */
                        _magnitudeIncrement: 5,

                        /**
                         * Defines the rotational speed of the object.
                         */
                        _rotationSpeed: 7,

                        /**
                         * Defines whether the up key has been pressed or not.
                         */
                        _keysPressed: {
                            UP: false,
                            LEFT: false,
                            RIGHT: false,
                        },
                        
                        /**
                         * A reference to the STOMP interface used to send updates
                         */
                        _stomp: null,
                        
                        /**
                         * A reference to the GAME ID sent in every communication with the server
                         */
                        _gameID: null,

                        /**
                         * Initialiser function to set the objects default properties.
                         */
                        init: function () {
                            this.origin('center');
                        },

                        /**
                         * Binds key down and up events and handles movement caps.
                         */
                        events: {
                            KeyDown: function (e) {
                                // If we're moving, and the key pressed is a directional change then
                                // apply some rotation.
                                if (e.keyCode === Crafty.keys.RIGHT_ARROW) {
                                    this._keysPressed.RIGHT = true;
                                }

                                if (e.keyCode === Crafty.keys.LEFT_ARROW) {
                                    this._keysPressed.LEFT = true;
                                }

                                if (e.keyCode === Crafty.keys.UP_ARROW) {
                                    this._keysPressed.UP = true;
                                }
                            },

                            KeyUp: function (e) {
                                if (e.keyCode === Crafty.keys.RIGHT_ARROW) {
                                    this._keysPressed.RIGHT = false;
                                }

                                if (e.keyCode === Crafty.keys.LEFT_ARROW) {
                                    this._keysPressed.LEFT = false;
                                }

                                if (e.keyCode === Crafty.keys.UP_ARROW) {
                                    this._keysPressed.UP = false;
                                }
                            },

                            EnterFrame: function () {
                                // Ensure the object hasn't been locked before manipulating it.
                                if (!this.isLocked()) {
                                    // Handle rotation of the object. Prevent rotation if both arrows
                                    // are pushed down.
                                    if (!(this._keysPressed.LEFT && this._keysPressed.RIGHT)
                                            && (this._keysPressed.LEFT || this._keysPressed.RIGHT)) {

                                        if (this._keysPressed.LEFT) {
                                            this.rotation-= this._rotationSpeed;
                                        }

                                        if (this._keysPressed.RIGHT) {
                                            this.rotation+= this._rotationSpeed;
                                        }
                                    }

                                    // Does the user want is to move forward?
                                    if (this._keysPressed.UP) {
                                        // Adjust the magnitude ensuring we don't go over the limit.
                                        if (this._magnitude <= this._maxMagnitude) {
                                            this._magnitude+= this._magnitudeIncrement;
                                        }

                                        // Scale the vector to the new magnitude.
                                        this._vector.scaleToMagnitude(this._magnitude);

                                        // Adjust the x and y velocity of the objet accordingly.
                                        this.vx = this._vector.x;
                                        this.vy = this._vector.y;
                                    } else {
                                        // Decrease the magnitude while it's greater than 1
                                        if (this._magnitude > 1) {
                                            this._magnitude-= this._magnitudeIncrement;
                                        }

                                        // If magnitude is less than or equal to 1 we're at the smallest
                                        // magnitude and want to stop the object moving. If not, we want
                                        // to apply the reduced magnitude and set the velocity accordingly.
                                        if (this._magnitude <= 1) {
                                            this._magnitude = 1;
                                            this.vx = 0;
                                            this.vy = 0;
                                        } else {
                                            this._vector.scaleToMagnitude(this._magnitude);
                                            this.vx = this._vector.x;
                                            this.vy = this._vector.y;
                                        }
                                    }
                                    
                                    // Send update to the server
                                    this.sendUpdate();
                                } else {
                                    // Object is locked so set all values to 0
                                    this.vx = 0;
                                    this.vy = 0;
                                }
                            },
                            
                            Remove: function () {
                                // We want to be sure to send 1 last update containing explode
                                // so we know this object blew up on the other client.
                                this.sendUpdate();
                            }
                        },

                        /**
                         * #.isMoving
                         * Determines if this object is moving or not.
                         *
                         * @returns bool
                         */
                        isMoving: function () {
                            var v = this.velocity();
                            return (v.x !== 0 || v.y !== 0);
                        },
                        
                        /**
                         * #.setStomp
                         * Sets the stomp socket so we can push updates to the server about
                         * position and rotation.
                         * 
                         * @param STOMP stomp
                         * @param int gameID
                         * @returns this
                         */
                        setStomp: function (stomp, gameID) {
                            this._stomp = stomp;
                            this._gameID = gameID;
                            this._stomp.debug = null;
                            
                            return this;
                        },
                        
                        /**
                         * #.sendUpdates
                         * Sends an update to the server about position, rotation, etc.
                         * 
                         * @returns void
                         */
                        sendUpdate: function () {
                            // Send information to server about position and rotation.
                            this._stomp.send(TronPreGame._gameQueue, {}, 
                                JSON.stringify({
                                    'command': 'GAME.UPDATE',
                                    'data': {
                                        gameID: this._gameID,
                                        vx: this.vx,
                                        vy: this.vy,
                                        magnitude: this._magnitude,
                                        rotation: this.rotation,
                                        status: this._status
                                    }
                                })
                            );
                        }
                    });

                    /**
                     * Defines an explosion object.
                     */
                    Crafty.c('Explosion', {
                        /**
                         * Define required components.
                         */
                        required: 'Sprite_Explosion_0, Canvas, SpriteAnimation, Tween',

                        /**
                         * Initialise the attributes.
                         */
                        init: function () {
                            this.origin('center');
                            this.z = 20;
                            this.rotation = Crafty.math.randomNumber(0, 359);

                            Crafty.audio.play('Explosion', 1, 0.05);

                            // Create an explosion reel and begin the animation.
                            this.reel('ExplosionAnimation', 250, 0, 0, 6)
                                    .animate('ExplosionAnimation');

                            // Set timeout and tween down the alpha setting for the explosion when,
                            // we're close to removing the explosion.
                            setTimeout(function (explosion) {
                                explosion.tween({
                                    alpha: 0
                                }, 500);
                            }, TronGame._config.explosionTimeout - 500, this);

                            // Remove the explosion after configured time.
                            setTimeout(function (explosion) {
                                explosion.destroy();
                            }, TronGame._config.explosionTimeout, this);
                        }
                    });

                    /**
                     * Speaker control component that allows for muting and unmuting when clicked.
                     */
                    Crafty.c('SpeakerControl', {
                        /**
                         * Define required components
                         */
                        required: '2D, DOM, Sprite_SpeakerActive, Mouse',

                        /**
                         * An ID of the audio track to pause.
                         */
                        _audioID: null,

                        /**
                         * Tracks whether we are in a paused or unpaused state.
                         */
                        _pause: false,

                        /**
                         * Initialise the speaker giving dimensions.
                         */
                        init: function () {
                            this.h = 32;
                            this.w = 32;
                            this.alpha = 0.15;
                        },

                        events: {
                            NewComponent: function () {
                                this.h = 32;
                                this.w = 32;
                            },

                            Click: function (e) {
                                if (this._audioID) {
                                    if (!this._paused) {
                                        this.pause();
                                    } else {
                                        this.unpause();
                                    }
                                } else {
                                    Crafty.log("No audio ID set");
                                }
                            }
                        },

                        /**
                         * Sets the ID of the audio track this object will mute/pause when clicked.
                         *
                         * @param string id
                         * @returns this
                         */
                        setAudioID: function (id) {
                            this._audioID = id;

                            var audioCookie = $.cookie(this._audioID);
                            if (audioCookie !== "undefined") {
                                if (audioCookie === "false") {
                                    this.pause();
                                }
                            }

                            return this;
                        },

                        /**
                         * #.pause
                         * Pauses the specified audio ID
                         */
                        pause: function () {
                            this._paused = true;
                            Crafty.audio.pause(this._audioID);
                            this.removeComponent('Sprite_SpeakerActive')
                                    .addComponent('Sprite_SpeakerMuted');

                            $.cookie(this._audioID, false, {expires: 30});
                        },

                        /**
                         * #.unpause
                         * Unpauses the specified audio ID
                         */
                        unpause: function () {
                            this._paused = false;
                            Crafty.audio.unpause(this._audioID);
                            this.removeComponent('Sprite_SpeakerMuted')
                                .addComponent('Sprite_SpeakerActive');

                            $.cookie(this._audioID, true, {expires: 30});
                        }
                    });

                    // Set components defined flag.
                    TronGame._componentsDefined = true;
                }
            }
        };

        TronGame.addScene('LandingPage', function (game) {
            Crafty.background('rgb(40, 40, 40)');

            Crafty.audio.play('Background', -1, 0.1);

            Crafty.e('SpeakerControl').attr({
                x: Crafty.viewport.width - 64,
                y: 32
            }).setAudioID('Background');

            Crafty.e('2D, Text, DOM').attr({
                h: 50,
                w: 400,
                x: Crafty.viewport.width / 2 - 180,
                y: Crafty.viewport.height / 2 + 80,
            })
            .text('Lets play!')
            .textColor('rgba(255, 255, 255, 0.1)')
            .textFont({
                font: 'Impact, Charcoal, sans-serif',
                size: '30pt',
                weight: 'bold'
            })
            .css({
                'text-align': 'center',
            });

            Crafty.log('Using Logo Measurements: 758, 302');
            Crafty.e('2D, HTML, Mouse').attr({
                x: Crafty.viewport.width / 2 - 379,
                y: Crafty.viewport.height / 2 - 151,
                h: 302,
                w: 758
            })
            .append($('<img />').attr({
                src: TronGame._config.assetPaths.images + 'logo_nobg.png',
            }))
            .bind('Click', function () {
                Crafty.log('Clicked');
                Crafty.audio.stop();
                game.start();
            })
            .bind('MouseOver', function () {
                this.css('cursor', 'pointer');
            });

            var maxParticles = Math.round(
                Crafty.viewport.height / 300 * Crafty.viewport.width / 300 * 6);
            Crafty.e("2D, Canvas, Particles").particles({
                // Maximum number of particles in frame at any one time.
                maxParticles: maxParticles,

                // Size of the particles and change of random size.
                size: 30,
                sizeRandom: 20,

                // Speed of particles and chance of random speed.
                speed: 0.2,
                speedRandom: 0.8,

                // Lifespan in frames
                lifeSpan: 75,
                lifeSpanRandom: 5,

                // Angle is calculated clockwise
                angle: 180,
                angleRandom: 50,

                // Color the particles should start off as and chance of being different.
                startColour: [40, 40, 40, 1],
                startColourRandom: [0, 0, 0, 5],

                // Ending colour of particles and chance of being different.
                endColour: [60, 60, 60, 0],
                endColourRandom: [0, 0, 0, 0],

                // Only applies when fastMode is off, specifies how sharp the gradients are drawn
                sharpness: 80,
                sharpnessRandom: 10,

                // Random spread from origin
                spread: Crafty.viewport.width / 2,

                // How many frames should this last
                duration: -1,

                // Will draw squares instead of circle gradients
                fastMode: true,

                // Defining gravity. 1 is strong gravity.
                gravity: {
                    x: 0,
                    y: 0
                },

                // Jitter movement. Sensible values 1-3
                jitter: 0.5,

                // Offset for the origin of the particles
                originOffset: {
                    x: Crafty.viewport.width / 2,
                    y: Crafty.viewport.height / 2
                },

                // Custom option.
                // Renders particles behind everything else.
                backgroundLayer: true,
            });

            Crafty.e('CyanPlayer').attr({
                x: Crafty.viewport.width / 2 + 150,
                y: Crafty.viewport.height / 2 - 120,
                rotation: 110
            });

            Crafty.e('OrangePlayer').attr({
                x: Crafty.viewport.width / 2 - 150,
                y: Crafty.viewport.height / 2 + 110,
                rotation: 340
            });
        });

        TronGame.addScene('Main', function () {
            Crafty.background('rgb(40, 40, 40) url("/assets/images/game/bg.png") center no-repeat');

            //Crafty.audio.play('Background', -1, 0.05);

            var maxParticles = Math.round(
                Crafty.viewport.height / 300 * Crafty.viewport.width / 300 * 6);
            Crafty.log(maxParticles);
            Crafty.e("2D, Canvas, Particles").particles({
                // Maximum number of particles in frame at any one time.
                maxParticles: maxParticles,

                // Size of the particles and change of random size.
                size: 30,
                sizeRandom: 20,

                // Speed of particles and chance of random speed.
                speed: 0.2,
                speedRandom: 0.8,

                // Lifespan in frames
                lifeSpan: 75,
                lifeSpanRandom: 5,

                // Angle is calculated clockwise
                angle: 180,
                angleRandom: 50,

                // Color the particles should start off as and chance of being different.
                startColour: [40, 40, 40, 1],
                startColourRandom: [0, 0, 0, 5],

                // Ending colour of particles and chance of being different.
                endColour: [60, 60, 60, 0],
                endColourRandom: [0, 0, 0, 0],

                // Only applies when fastMode is off, specifies how sharp the gradients are drawn
                sharpness: 80,
                sharpnessRandom: 10,

                // Random spread from origin
                spread: Crafty.viewport.width / 2,

                // How many frames should this last
                duration: -1,

                // Will draw squares instead of circle gradients
                fastMode: true,

                // Defining gravity. 1 is strong gravity.
                gravity: {
                    x: 0,
                    y: 0
                },

                // Jitter movement. Sensible values 1-3
                jitter: 0.5,

                // Offset for the origin of the particles
                originOffset: {
                    x: Crafty.viewport.width / 2,
                    y: Crafty.viewport.height / 2
                },

                // Custom option.
                // Renders particles behind everything else.
                backgroundLayer: true,
            });
        });

        TronGame.addScene('Debug', function () {
            Crafty.background('rgb(40, 40, 40) url("images/bg.png") center no-repeat');

            var maxParticles = Math.round(
                Crafty.viewport.height / 300 * Crafty.viewport.width / 300 * 6);
            Crafty.e("2D, Canvas, Particles").particles({
                // Maximum number of particles in frame at any one time.
                maxParticles: maxParticles,

                // Size of the particles and change of random size.
                size: 30,
                sizeRandom: 20,

                // Speed of particles and chance of random speed.
                speed: 0.2,
                speedRandom: 0.8,

                // Lifespan in frames
                lifeSpan: 75,
                lifeSpanRandom: 5,

                // Angle is calculated clockwise
                angle: 180,
                angleRandom: 50,

                // Color the particles should start off as and chance of being different.
                startColour: [40, 40, 40, 1],
                startColourRandom: [0, 0, 0, 5],

                // Ending colour of particles and chance of being different.
                endColour: [60, 60, 60, 0],
                endColourRandom: [0, 0, 0, 0],

                // Only applies when fastMode is off, specifies how sharp the gradients are drawn
                sharpness: 80,
                sharpnessRandom: 10,

                // Random spread from origin
                spread: Crafty.viewport.width / 2,

                // How many frames should this last
                duration: -1,

                // Will draw squares instead of circle gradients
                fastMode: true,

                // Defining gravity. 1 is strong gravity.
                gravity: {
                    x: 0,
                    y: 0
                },

                // Jitter movement. Sensible values 1-3
                jitter: 0.5,

                // Offset for the origin of the particles
                originOffset: {
                    x: Crafty.viewport.width / 2,
                    y: Crafty.viewport.height / 2
                },

                // Custom option.
                // Renders particles behind everything else.
                backgroundLayer: true,
            });

            // A controllable player for the user.
            Crafty.e('CyanPlayer, ControllablePlayer').attr({
                x: 100,
                y: 100,
                rotation: 90
            });

            // Define some enemies for testing purposes.
            for (var i = 0; i < 5; i++) {
                Crafty.e('OrangePlayer').attr({
                    x: Crafty.math.randomNumber(150, Crafty.viewport.width),
                    y: Crafty.math.randomNumber(0, Crafty.viewport.height),
                    rotation: Crafty.math.randomNumber(0, 359)
                });
            }
        });

        window.TronGame = TronGame;
    })(window);
});
