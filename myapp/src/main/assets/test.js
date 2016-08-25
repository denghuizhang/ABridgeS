//APP调用此方法
function getJsUserData() {
    var user = {};
    user['name'] = document.getElementById('name_field').value;
    user['surname'] = document.getElementById('surname_field').value;
    user['age'] = document.getElementById('age_field').value;
    return JSON.stringify(user);
}

//APP主动传递
function setUserInfoFromApp(userJson){
    document.getElementById('src_field').value = userJson;
    var user = JSON.parse(userJson);
    document.getElementById('name_field').value = user.name;
    document.getElementById('surname_field').value = user.surname;
    document.getElementById('age_field').value = user.age;
}

//JS主动获取
function getUserInfo(){
    //调用APP接口获取用户信息
    AInterface.getUserInfoFromApp("获取UserInfo",function(userJson){
        document.getElementById('src_field').value = userJson;
        var user = JSON.parse(userJson);
        document.getElementById('name_field').value = user.name;
        document.getElementById('surname_field').value = user.surname;
        document.getElementById('age_field').value = user.age;
    });
}

document.addEventListener('DOMContentLoaded', function() {
    //调用APP的Toast，JS主动调用
    AInterface.showToastMessage("JS被加载");
}, false);