function ABridgeS() {}

ABridgeS.call = function(interfaceName, args) {
    window[interfaceName].call(ABridgeS.getFunctionName(), ABridgeS.argsToJson(args));
}

ABridgeS.callWithCallback = function(interfaceName, args) {
    if (args.length == 0) {
        throw "Function must have at least one argument!"
    }
    var callback = args[args.length - 1];
    if (typeof callback !== "function") {
        throw "Last argument must be a callback function!"
    }

    var correctsArguments = Array.prototype.slice.call(args, 0, args.length - 1);

    var callbackId = ABridgeS.addCallBack(callback);
    window[interfaceName].callWithCallback(ABridgeS.getFunctionName(), ABridgeS.argsToJson(correctsArguments), callbackId);
}

ABridgeS.argsToJson = function(args) {
    var argsArray = [];
    for(var i in args) {
        var item = args[i];
        argsArray.push(item);
    }
    var jsonArguments = JSON.stringify(argsArray);
    return jsonArguments;
}

ABridgeS.getFunctionName = function() {
    var functionLine = (new Error()).stack.split('\n')[3].split(' ')[5];
    var functionCallStack = functionLine.split('.');
    var functionName = functionCallStack[functionCallStack.length - 1];
    return functionName;
};

ABridgeS.bridgeSCallbacks = {};

ABridgeS.addCallBack = function(callback) {
    var callbackId = ABridgeS.generateCallbackId();
    ABridgeS.bridgeSCallbacks[callbackId] = callback;
    return callbackId;
}

ABridgeS.removeCallBack = function(callbackId, response) {
    var callback =  ABridgeS.bridgeSCallbacks[callbackId];
    callback(response);
    delete ABridgeS.bridgeSCallbacks[callbackId];
}

ABridgeS.generateCallbackId = function() {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 5; i++ ){
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
    return text;
}

/*框架被加载时调用，交互APP的onBridgePrepared接口*/
document.addEventListener('DOMContentLoaded', function() {
    window.ABridgeSPrepare.onBridgePrepared();
}, false);