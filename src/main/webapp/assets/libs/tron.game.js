$(function() {
    (function (window) {
        
        //Utility functions
        function waitUntil(condition, run) {
            if (!condition()) {
                setTimeout(waitUntil, 100, condition, run);
                return;
            }
            
            run();
        }
        
        var TronPreGame = {
            /**
             * Defines the URL to send game updates to.
             */
            _gameQueue: '/queue/game',
            
            /**
             * Defines the subscription URL specific to the logged in user. 
             */
            _gameTopic: '/user/{username}/topic/game',
            
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
                
                
                
                // Get the game ID from the URL and replace the subscription URL userID with 
                // the logged in users username.
                var path = window.location.pathname.split('/');
                this._gameID = path[path.length - 1];
                this._gameTopic = this._gameTopic.replace('{username}', User.username);
                
                // Generate a game instance and load all assets.
                var game = TronGame.instance();
                this._game = game;
                this._game.loadAssets(function () {
                    that._onAssetsLoaded();
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
                
                // Go through each possible command and see if we want to handle it.
                if (response.command === 'GAME.PING') {
                    
                    this._gameStomp.send(this._gameQueue, {}, JSON.stringify({
                        command: 'GAME.PONG',
                        data: {
                            gameID: this._gameID
                        }
                    }));
                    
                } else if (response.command === "GAME.PREP") {
                    
                    this._role = response.role;
                    this.initGame(response.height, response.width, response.role);
                    
                    var that = this;
                    var game = this._game;
                    var gameStomp = this._gameStomp;
                    
                    // Wait until the game is initialised and then send a ready message.
                    waitUntil(function () {
                        return game.isInitialised();
                    }, function () {
                        gameStomp.send(that._gameQueue, {}, JSON.stringify({
                            command: 'GAME.READY',
                            data: {
                                gameID: that._gameID
                            }
                        }));
                    });
                    
                } else if (response.command === 'GAME.START') {
                    Crafty.log('GAME.START');
                    this.startGame(response.start_in);
                    
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
                    
                    $('body').append($('<div />').attr({
                        id: 'stage',
                        'tron-height': height,
                        'tron-width': width
                    }));

                    this._game.init('stage', function () {  
                        
                        // Setup required for the challenger player.
                        if (role === 'CHALLENGER') {
                            Crafty.e('Player')
                                .requires('CyanPlayer, LocalPlayer')
                                .attr({
                                    x: 100,
                                    y: 100,
                                    rotation: 90
                                })
                                .setStomp(that._gameStomp, that._gameID);
                            
                            Crafty.e('Player')
                                .requires('OrangePlayer, RemotePlayer')
                                .attr({
                                    x: width - 100,
                                    y: height - 100,
                                    rotation: -90
                                })
                                .setStomp(that._gameStomp, that._gameTopic);
                        }
                        
                        // Setup required for the challenged player.
                        if (role === 'CHALLENGED') {
                            Crafty.e('Player')
                                .requires('CyanPlayer, RemotePlayer')
                                .attr({
                                    x: 100,
                                    y: 100,
                                    rotation: 90
                                })
                                .setStomp(that._gameStomp, that._gameTopic);
                        
                            Crafty.e('Player')
                                .requires('OrangePlayer, LocalPlayer')
                                .attr({
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
                    //var pos = Crafty('Challenged').get(0).pos();
                    //Crafty.log(pos);
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
            },
            
            /**
             * #._onAssetsLoaded
             * Called when the assets have been loaded by Crafty.
             * 
             * @returns void
             */
            _onAssetsLoaded: function () {
                Crafty.log("Assets loaded: now trying to connect the web socket");
                
                this._gameSocket = new SockJS(this._gameQueue);
                this._gameStomp = Stomp.over(this._gameSocket);
                
                // Change the debug function of stomp to filter out any game updates.
//                this._gameStomp.debug = function (s) {
//                    var um = Stomp.Frame.unmarshall(s);
//                    if (um.frames.length > 0 && um.frames[0].body !== "") {
//                        var parsed = JSON.parse(um.frames[0].body);
//                        
//                        if (!String(parsed.command).match("GAME.UPDATE")) {
//                            Crafty.log(s);
//                        }
//                    } else {
//                        Crafty.log(s);
//                    }
//                }
                
                var gameStomp = this._gameStomp;
                var that = this;                
                
                // Connect to the server and subscribe to our personal URL. Then send a message
                // indicating our status as LOADED.
                gameStomp.connect({}, function () {
                    gameStomp.subscribe(that._gameTopic, function (response) {
                        that.handle(JSON.parse(response.body));
                    });
                
                    gameStomp.send(that._gameQueue, {}, JSON.stringify({
                        command: 'GAME.LOADED',
                        data: {
                            gameID: that._gameID,
                            height: $(window).height(),
                            width: $(window).width()
                        }
                    }));
                }, function (err) {
                    Crafty.log("There was an error connecting to the WS");
                    Crafty.log(err);
                });
            },
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
                this.loadAssets();
                
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
                    // Make sure we have an end to the attempts.
                    if (attempt > 5) {
                        Crafty.log("Failed to start game");
                        return;
                    }
                    
                    // Make sure the game is intiialised.
                    if (!game._initialised) {
                        Crafty.log("Failed to start game on attempt " + attempt);
                        setTimeout(startGame, 500, ++attempt, game);
                        return;
                    }
                    
                    // Make sure we haven't already started. We don't want to refresh here.
                    if (game._started) {
                        Crafty.log("Trying to start a game that's arleady been started");
                        return;
                    }
                    
                    Crafty.log("Starting TronGame on attempt " + attempt);
                    game._started = true;
                    Crafty.trigger('TronGameStart');
                    that.unlockPlayers();
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
             * #.isInitialised
             * Determines whether the game is initialised or not.
             * 
             * @return boolean
             */
            isInitialised: function () {
                return this._initialised;
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
                Crafty('Player').each(function () {
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
                Crafty('Player').each(function () {
                    this.unlock();
                });
                
                return this;
            },
            
            /**
             * Loads all necessary images, sprites and audio files.
             */
            loadAssets: function (onLoad, onProgress, onError) {
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
             * TronGame._defineComponents
             * Defines the game objects used in TronGame.
             */
            _defineComponents: function () {
                // Ensure components haven't been defined yet.
                if (!TronGame._componentsDefined) {
                    Crafty.c('Challenger', {});
                    Crafty.c('Challenged', {});
                    
                    Crafty.c('OrangePlayer', {
                        /**
                         * Define requierd components.
                         */
                        required: 'Sprite_BikeOrange, Player',
                        
                        init: function () {
                            Crafty.log("Setting color");
                            this.setBulletColor('rgb(248,148,0)');
                        }
                    });
                    
                    Crafty.c('CyanPlayer', {
                        /**
                         * Define requierd components.
                         */
                        required: 'Sprite_BikeCyan, Player',
                        
                        init: function () {
                            this.setBulletColor('cyan');
                        }
                    });
                    
                    Crafty.c('WrapAroundMap', {
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
                            }
                        }
                    });
                    
                    Crafty.c('Bullet', {
                        
                        required: '2D, Canvas, Color, Motion, Collision, WrapAroundMap',
                        
                        /**
                         * Timeout before the bullet explodes.
                         */
                        _timeout: 1000,
                        
                        /**
                         * Magnitude the bullet should travel at.
                         */
                        _magnitude: 500,
                        
                        /**
                         * Indicator as to whether the bullet is active or not.
                         */
                        _active: false,
                        
                        /**
                         * Default color of a bullet.
                         */
                        _defaultColor: 'white',
                        
                        init: function () {
                            this.h = 4;
                            this.w = 1;
                            this.origin('center');
                            this.checkHits('Player');
                            this.z = 5;
                            this.color(this._defaultColor);
                        },
                        
                        remove: function () {
                            this.explode();
                        },
                        
                        events: {
                            HitOn: function (hit) {
                                var o = hit[0].obj;
                                if (o.has('Player') && this._active) {
                                    o.destroy();
                                }
                            }
                        },
                        
                        /**
                         * Fires the bullet along a given vector.
                         * 
                         * @param Crafty.math.Vector2D vector
                         * @returns void
                         */
                        fire: function (vector) {
                            vector.scaleToMagnitude(this._magnitude);
                            
                            this.vx = vector.x;
                            this.vy = vector.y;
                            
                            var that = this;
                            
                            setTimeout(function () {
                                that._active = true;
                            }, 150);
                            
                            setTimeout(function () {
                                that.destroy();
                            }, this._timeout);
                        },
                        
                        /**
                         * Causes the bullet to blow up.
                         * 
                         * @returns void
                         */
                        explode: function () {
                            Crafty.e('Explosion')
                                .attr({
                                    x: this.x - 16,
                                    y: this.y - 16
                                });
                        }
                    });
                    
                    Crafty.c('Player', {
                        
                        required: '2D, Canvas, Collision, Keyboard, WrapAroundMap',
                        
                        /**
                         * Defines the aboslute centre of this object.
                         */
                        _absoluteCentreX: 0,
                        _absoluteCentreY: 0,

                        /**
                         * Defines the movement vector for this object
                         */
                        _vectorX: 0,
                        _vectorY: 0,

                        /**
                         * Defines the current magnitude of our movement vector.
                         */
                        _magnitude: 1,

                        /**
                         * Defines whether this object can be manipulated or not.
                         */
                        _lock: false,                        
                        
                        /**
                         * Status of this object.
                         */
                        _status: 'ACTIVE',
                        
                        /**
                         * Defines the bullet colour.
                         */
                        _bulletColor: 'white',
                        
                        init: function () {
                            this.origin('center');
                            this.checkHits('Player', 'Trail');
                            this.z = 10;
                            
                            this._Vector = new Crafty.math.Vector2D();

                            this.collision([
                                10, 0,
                                10, 32,
                                22, 32,
                                22, 0,
                            ]);
                        },
                        
                        remove: function (destroy) {
                            this.explode();
                        },
                        
                        events: {
                            EnterFrame: function () {
                                // Calculate the vectors for the direction of the object.
                                this._vectorX = Math.sin(Crafty.math.degToRad(this._rotation));
                                this._vectorY = -Math.cos(Crafty.math.degToRad(this._rotation));
                                this._absoluteCentreX = this.x + this._origin.x;
                                this._absoluteCentreY = this.y + this._origin.y;
                            },

                            HitOn: function (collision) {
                                if (collision[0].obj.has('Player')) {
                                    this.destroy();
                                }
                            }
                        },
                        
                        /**
                         * #.setBulletColor
                         * Sets the colour of this players bullet.
                         * 
                         * @param string color
                         */
                        setBulletColor: function (color) {
                            this._bulletColor = color;
                        },
                        
                        /**
                         * #.fireBullet
                         * Fires a bullet from this player.
                         */
                        fireBullet: function () {
                            Crafty.e('Bullet')
                                .attr({
                                    rotation: this.rotation,
                                    x: this._absoluteCentreX,
                                    y: this._absoluteCentreY
                                })
                                .color(this._bulletColor)
                                .fire(this.getVector());
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
                                Crafty.e('Explosion')
                                    .attr({
                                        x: this.x - 16,
                                        y: this.y - 16
                                    });
                            }
                        },
                        
                        /**
                         * Gets a vector object using the vector arguments for this object.
                         * 
                         * @returns {Crafty.math.Vector2D}
                         */
                        getVector: function () {
                            return new Crafty.math.Vector2D(this._vectorX, this._vectorY);
                        }
                    });

                    Crafty.c('ControllablePlayer', {
                        /**
                         * Object requires motion and keyboard component. Angular motion is handled
                         * manually and so AngularMotion is nto required.
                         */
                        required: "Player, Motion",

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
                        
                        _movementUP: false,
                        _movementLEFT: false,
                        _movementRIGHT: false,
                        
                        init: function () {
                            this.origin('center');
                        },
                        
                        events: {
                            EnterFrame: function () {
                                // Ensure the object hasn't been locked before manipulating it.
                                if (!this.isLocked()) {
                                    // Handle rotation of the object. Prevent rotation if both arrows
                                    // are pushed down.
                                    if (!(this._movementLEFT && this._movementRIGHT)
                                            && (this._movementLEFT || this._movementRIGHT)) {

                                        if (this._movementLEFT) {
                                            this.attr('rotation', 
                                                this.rotation - this._rotationSpeed);
                                        }

                                        if (this._movementRIGHT) {
                                            this.attr('rotation', 
                                                this.rotation + this._rotationSpeed);
                                        }
                                    }
                                    
                                    var vector = this.getVector();

                                    // Does the user want is to move forward?
                                    if (this._movementUP) {
                                        // Adjust the magnitude ensuring we don't go over the limit.
                                        if (this._magnitude <= this._maxMagnitude) {
                                            this.attr('_magnitude', 
                                                this._magnitude + this._magnitudeIncrement);
                                        }

                                        // Scale the vector to the new magnitude.
                                        vector.scaleToMagnitude(this._magnitude);

                                        // Adjust the x and y velocity of the objet accordingly.
                                        this.attr('vx', vector.x);
                                        this.attr('vy', vector.y);
                                    } else {
                                        // Decrease the magnitude while it's greater than 1
                                        if (this._magnitude > 1) {
                                            this.attr('_magnitude', 
                                                this._magnitude - this._magnitudeIncrement);
                                        }

                                        // If magnitude is less than or equal to 1 we're at the smallest
                                        // magnitude and want to stop the object moving. If not, we want
                                        // to apply the reduced magnitude and set the velocity accordingly.
                                        if (this._magnitude <= 1) {
                                            this.attr('_magnitude', 1);
                                            this.attr('vx', 0);
                                            this.attr('vy', 0);
                                        } else {
                                            vector.scaleToMagnitude(this._magnitude);
                                            this.attr('vx', vector.x);
                                            this.attr('vy', vector.y);
                                        }
                                    }
                                } else {
                                    // Object is locked so set all values to 0
                                    this.attr('vx', 0);
                                    this.attr('vy', 0);
                                }
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
                        }
                    });
                    
                    Crafty.c('LocalPlayer', {
                        
                        /**
                         * Requierd components
                         */
                        required: 'ControllablePlayer, Keyboard, Model',
                        
                         /**
                         * A reference to the STOMP interface used to send updates
                         */
                        _stomp: null,
                        
                        /**
                         * A reference to the GAME ID sent in every communication with the server
                         */
                        _gameID: null,
                        
                        remove: function (destroy) {
                            this.sendUpdate();
                        },
                        
                        /**
                         * Events 
                         */
                        events: {
                            KeyDown: function (e) {
                                // If we're moving, and the key pressed is a directional change then
                                // apply some rotation.
                                if (e.keyCode === Crafty.keys.RIGHT_ARROW) {
                                    this.attr('_movementRIGHT', true);
                                }

                                if (e.keyCode === Crafty.keys.LEFT_ARROW) {
                                    this.attr('_movementLEFT', true);
                                }

                                if (e.keyCode === Crafty.keys.UP_ARROW) {
                                    this.attr('_movementUP', true);
                                }
                                
                                if (e.keyCode === Crafty.keys.SPACE) {
                                    this.fireBullet();
                                    this.sendUpdate('status', 'BULLET_FIRED');
                                }
                            },

                            KeyUp: function (e) {
                                if (e.keyCode === Crafty.keys.RIGHT_ARROW) {
                                    this.attr('_movementRIGHT', false);
                                }

                                if (e.keyCode === Crafty.keys.LEFT_ARROW) {
                                    this.attr('_movementLEFT', false);
                                }

                                if (e.keyCode === Crafty.keys.UP_ARROW) {
                                    this.attr('_movementUP', false);
                                }
                            },
                            
                            'Change[_movementUP]': function (state) {
                                this.sendUpdate('_movementUP', state);
                            },
                            'Change[_movementLEFT]': function (state) {
                                this.sendUpdate('_movementLEFT', state);
                            },
                            'Change[_movementRIGHT]': function (state) {
                                this.sendUpdate('_movementRIGHT', state);
                            }
                        },
                        
                        /**
                         * #.sendUpdates
                         * Sends an update to the server about position, rotation, etc.
                         * 
                         * @returns void
                         */
                        sendUpdate: function () {                            
                            data = {
                                gameID: this._gameID,
                                status: this._status,
                                x: this.x,
                                y: this.y,
                                rotation: this.rotation
                            };
                            
                            if (arguments.length === 2) {
                                data[arguments[0]] = arguments[1];
                            }
                            
                            var jsonUpdate = JSON.stringify({
                                command: 'GAME.UPDATE',
                                data: data
                            });
                            
                            this._stomp.send(TronPreGame._gameQueue, {}, jsonUpdate);
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
                            
                            return this;
                        },
                    });
                    
                    /**
                     * A remote player updates by the server.
                     */
                    Crafty.c('RemotePlayer', {
                        /**
                         * Required modules.
                         */
                        required: 'ControllablePlayer',
                        
                        /**
                         * Reference to the STOMP interface to commuicate with the server.
                         */
                        _stomp: null,
                        
                        /**
                         * Initialisation
                         */
                        init: function () {
                            this.origin('center');
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
                            var that = this;
                            
                            stomp.subscribe(url, function (response) {
                                var body = JSON.parse(response.body);
                                if (body.command === 'GAME.UPDATE') {
                                    // Look at the status and determine if we should blow up or not
                                    if (body.status === 'EXPLODED') {
                                        that.lock();
                                        that._movementUP = false;
                                        that._movementLEFT = false;
                                        that._movementRIGHT = false;
                                        that.vx = 0;
                                        that.vy = 0;
                                        that.explode();
                                    } else if (body.status === 'BULLET_FIRED') {
                                        that.fireBullet();
                                    } else {
                                        delete body['gameID'];
                                        delete body['status'];
                                        
                                        for (var i in body) {
                                            that[i] = body[i];
                                        }
                                    }
                                }
                            });
                            
                            return this;
                        }
                    });

                    /**
                     * Defines an explosion object.
                     */
                    Crafty.c('Explosion', {
                        
                        required: 'Sprite_Explosion_0, Canvas, SpriteAnimation, Tween, Collision',

                        init: function () {
                            this.origin('center');
                            
                            this.checkHits('Player');
                            this.z = 20;
                            this.rotation = Crafty.math.randomNumber(0, 359);

                            Crafty.audio.play('Explosion', 1, 0.05);

                            // Create an explosion reel and begin the animation.
                            this.reel('ExplosionAnimation', 250, 0, 0, 6)
                                    .animate('ExplosionAnimation');
                                
                            var that = this;

                            // Set timeout and tween down the alpha setting for the explosion when,
                            // we're close to removing the explosion.
                            setTimeout(function () {
                                that.tween({
                                    alpha: 0
                                }, 500);
                            }, TronGame._config.explosionTimeout - 500);

                            // Remove the explosion after configured time.
                            setTimeout(function () {
                                that.destroy();
                            }, TronGame._config.explosionTimeout);
                        },
                        
                        events: {
                            HitOn: function (hit) {
                                var o = hit[0].obj;
                                if (o.has('Player')) {
                                    o.destroy();
                                }
                            }
                        }
                    });

                    /**
                     * Speaker control component that allows for muting and unmuting when clicked.
                     */
                    Crafty.c('SpeakerControl', {
                        
                        required: '2D, DOM, Sprite_SpeakerActive, Mouse',

                        /**
                         * An ID of the audio track to pause.
                         */
                        _audioID: null,

                        /**
                         * Tracks whether we are in a paused or unpaused state.
                         */
                        _pause: false,

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
