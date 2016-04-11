$(function () {
    (function (window) {
        /**
         * TronManager
         * Manages the tron modules.
         */
        var TronManager = {
            /**
             * Object containing all available Tron modules.
             */
            _modules: {
                'game': function () {
                    return window.TronGame;
                },
                
                'chat': function () {
                    return window.TronChat;
                },
                
                'challenge': function () {
                    return window.TronChallenge;
                },
                
                'active-users': function () {
                    return window.TronActiveUsers;
                }
            },
            
            /**
             * Tron.getModule
             * Retrieves a module. This is not an object. The object is created later.
             * 
             * @param string key
             * @returns The module class.
             */
            getModule: function (key) {
                return this._modules[key]();
            },
            
            /**
             * Tron.hasModule
             * Checks to see if a module is registered internally.
             * 
             * @param string key
             * @returns {Boolean}
             */
            hasModule: function (key) {
                return (this._modules[key] !== "undefined");
            }
        };
        
        /**
         * Tron
         * Wrapper object for the Tron game. This accepts 2 arguments where the first is the key
         * of the module and the second is an initialisation function. The initalisation function
         * will have the newly created module injected into it for manipulation.
         * 
         * @apram string key The module key
         * @param function init The initalisation function.
         * @return The newly created module.
         */
        var tronManagerInstance = Object.create(TronManager);
        var Tron = function (key, init) {            
            if (key !== "undefiend") {
                // Do we want to create a module?
                if (typeof key === "string") {
                    if (TronManager.hasModule(key)) {
                        var module = tronManagerInstance.getModule(key).instance();
                        init(module);
                        return module;
                    }
                }
            }
        };
        
        window.Tron = Tron;
    })(window);
});