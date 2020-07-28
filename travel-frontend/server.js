var chatList = [];//记录聊天记录
var WebSocketServer = require('ws').Server;
wss = new WebSocketServer({ port: 8181 }); //8181 与前端相对应
//调用 broadcast 广播，实现数据互通和实时更新

wss.broadcast = function (msg) {
    wss.clients.forEach(function each(client) {
        client.send(msg);
    });
};

wss.on('connection', function (ws) {

    wss.broadcast(JSON.stringify({ func: "init", chat: chatList }));

    ws.on('message', function (e) {
        var resData = JSON.parse(e)
        console.log('接收到来自client的消息：' + resData.msg)
        chatList.push({ userId: resData.userId, targetId: resData.targetId, date: resData.date, msg: resData.msg });
        wss.broadcast(JSON.stringify({ userId: resData.userId, targetId: resData.targetId, date: resData.date, msg: resData.msg }));
    });

})

console.log('服务器创建成功')