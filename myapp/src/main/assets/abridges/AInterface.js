function AInterface() {}

AInterface.showToastMessage = function(text) {
  ABridgeS.call('Android', arguments);
};

AInterface.getUserInfoFromApp = function(text){
    ABridgeS.callWithCallback('Android', arguments);
};
