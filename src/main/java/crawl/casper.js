// init casper
var casper = require('casper').create({
    //verbose: true,
    //logLevel: "debug",
    pageSettings: {
        loadImages: true,
        loadPlugins: true,
        userAgent: 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36',
        onResourceReceived: function(resource,response){
            this.echo(response.url);
        },
    },
});

var x = require('casper').selectXPath;
var mouse = require('mouse').create(casper);
// open the html
casper.start('http://weixin.sogou.com/weixin?query=%E6%89%8B%E6%9C%BA&_sug_type_=&_sug_=n&type=2&page=10&ie=utf8');

// say qiezi
casper.then(function(){
    this.capture('login0.png',{
        top:0,
        left:0,
        width:1200,
        height:700
    })
});

//登录按钮
casper.waitForSelector('a#top_login',function(){
    this.echo('click login button')
    this.click('a#top_login')
})

//截图
casper.then(function(){
    this.capture('login1.png',{
        top:0,
        left:0,
        width:1200,
        height:700
    })
});

//选择账号密码登录
casper.waitForSelector('a#switcher_plogin',function () {
    this.echo('switch to username ')
    this.click('a#switcher_plogin')
});

casper.then(function(){
    this.capture('login2.png',{
        top:0,
        left:0,
        width:1200,
        height:700
    })
});

//填写表单
casper.waitForSelector("form#loginform", function(){
    this.echo('the form who\'s id is form-login is here!!!');

    this.fill("form#loginform", {
        'u': '2609607316',
        'p': '39433956038167',
    }, false)
})

// say qiezi
casper.then(function(){
    this.capture('login3.png',{
        top:0,
        left:0,
        width:1200,
        height:700
    })
});

// 点击提交按钮
casper.then(function(){
    this.click('input#login_button')
});


// say qiezi
casper.then(function(){
    this.capture('login4.png',{
        top:0,
        left:0,
        width:1200,
        height:700
    })
});

casper.run()