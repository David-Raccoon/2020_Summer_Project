<template>
<div>
    <Navigate />
    <div class="row">
        <div class="col-1 alert alert-info" style="min-height:900px"></div>
        <div class="col-2">
            <div class="card">
                <div class="card-header bg-info text-light">My friends</div>
                <div class="card-body border" v-for="(friendName,index) in friendNames" :key="index">
                    <button class="btn btn-light form-control" @click="targetUsername=friendNames[index];targetUid=friendUid[index];loadChatList(userId,targetUid)">{{friendName}}</button>
                </div>
            </div>
            <div class="card">
                <div class="card-header bg-info text-light">My friends (Limited access)</div>
                <div class="card-body border" v-for="(friendName,index) in limitedNames" :key="index">
                    <button class="btn btn-light form-control" @click="targetUsername=limitedNames[index];targetUid=limitedUid[index];loadChatList(userId,targetUid)">{{friendName}}</button>
                </div>
            </div>
        </div>
        <div class="chat-box col-8">
            <div class="card-header bg-info text-light">Chatting with：{{targetUsername}}</div>
            <div class="card-body border">
                <div v-show="init" class="init-banner">
                    Chat with your friends!
                </div>
                <div v-for="(i,index) in displayMessage" :key="index">
                    <div v-show="i.type=='send'" class="row">
                        <div class="col-7"></div>
                        <div class="alert alert-success col-4">
                            ({{i.date}}): {{i.msg}}
                        </div>
                        <div class="col-1">
                            <img width="50" height="50" src="../assets/profile.jpg" />
                        </div>
                    </div>
                    <div v-show="i.type=='receive'" class="row">
                        <div class="col-1">
                            <img width="50" height="50" src="../assets/profile.jpg" />
                        </div>
                        <div class="alert alert-info col-4">
                            ({{i.date}}): {{i.msg}}
                        </div>
                        <div class="col-7"></div>
                    </div>
                </div>
            </div>
            <div class="card-body border">
                <div class="row">
                    <input type="text" class="form-control col-10" ref="sendMsg" v-model="contentText" />
                    <div class="btn btn-success col-2" @click="sendText()">发送</div>
                </div>
            </div>
        </div>
        <div class="col-1 alert alert-info" style="min-height:900px"></div>
    </div>
</div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
import Vue from 'vue'
import Navigate from './non-route/navigate.vue'
import Footer from './non-route/footer.vue'
import {
    backend_path,
    img_path
} from '../assets/config.js'

export default {
    components: {
        Navigate,
        Footer
    },
    data() {
        return {
            ws: null,
            userId: null,
            username: "",
            list: [],
            contentText: "",
            targetUid: "",
            targetUsername: "",

            friendNames: [],
            friendUid: [],

            limitedNames: [],
            limitedUid: [],

            displayMessage: [],

            init: true
        };
    },
    mounted() {
        this.initWebSocket()
        this.getInfo()
        this.getFriendData()
        this.getLimitedFriendData()
    },
    methods: {
        loadChatList(uid, targetId) {
            this.init = false
            this.displayMessage = []
            for (let chat of this.list) {
                if (chat.userId == uid && chat.targetId == targetId) {
                    this.displayMessage.push({
                        type: "send",
                        date: chat.date,
                        msg: chat.msg,
                    })
                }
                if (chat.userId == targetId && chat.targetId == uid) {
                    this.displayMessage.push({
                        type: "receive",
                        date: chat.date,
                        msg: chat.msg
                    })
                }
            }
        },
        dateFormat: function (fmt, date) {
            let ret;
            const opt = {
                "Y+": date.getFullYear().toString(), // 年
                "m+": (date.getMonth() + 1).toString(), // 月
                "d+": date.getDate().toString(), // 日
                "H+": date.getHours().toString(), // 时
                "M+": date.getMinutes().toString(), // 分
                "S+": date.getSeconds().toString() // 秒
                // 有其他格式化字符需求可以继续添加，必须转化成字符串
            };
            for (let k in opt) {
                ret = new RegExp("(" + k + ")").exec(fmt);
                if (ret) {
                    fmt = fmt.replace(ret[1], (ret[1].length == 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
                };
            };
            return fmt;
        },
        //根据时间戳作为当前用户ID
        getInfo() {
            this.userId = sessionStorage.getItem('uid')
            this.username = sessionStorage.getItem('username')
            this.targetUsername = this.$route.params.targetUsername
        },
        //发送聊天信息
        sendText() {
            if (this.targetUid == "") {
                return
            }
            let _this = this
            _this.$refs["sendMsg"].focus()
            if (!_this.contentText) {
                return;
            }
            let date = new Date()
            let params = {
                userId: _this.userId,
                targetId: _this.targetUid,
                date: _this.dateFormat("YYYY-mm-dd HH:MM", date),
                msg: _this.contentText
            }
            _this.ws.send(JSON.stringify(params)) //调用WebSocket send()发送信息的方法
            _this.contentText = ""
        },
        //进入页面创建websocket连接
        initWebSocket() {
            let _this = this;
            //判断页面有没有存在websocket连接
            if (window.WebSocket) {
                let ws = new WebSocket("ws://localhost:8181")
                _this.ws = ws
                ws.onopen = function (e) {
                    console.log("服务器连接成功")
                }
                ws.onclose = function (e) {
                    console.log("服务器连接关闭")
                }
                ws.onerror = function () {
                    console.log("服务器连接出错")
                }
                ws.onmessage = function (e) {
                    let resData = JSON.parse(e.data)
                    if (resData.func != 'init') {
                        _this.list = [
                            ..._this.list,
                            {
                                userId: resData.userId,
                                targetId: resData.targetId,
                                date: resData.date,
                                msg: resData.msg
                            }
                        ]
                        _this.loadChatList(_this.userId, _this.targetUid)
                    } else {
                        _this.list = resData.chat
                    }
                }
            }
        },
        getFriendData() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetFriendList?state=unlimited&uid=' + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var res = JSON.parse(httpRequest.responseText)
                    vm.friendNames = []
                    vm.friendUid = []
                    for (let key in res) {
                        Vue.set(vm.friendNames, key, res[key].username)
                        Vue.set(vm.friendUid, key, res[key].uid)
                    }
                }
            };
        },
        getLimitedFriendData() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetFriendList?state=limited&uid=' + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var res = JSON.parse(httpRequest.responseText)
                    vm.limitedNames = []
                    vm.limitedUid = []
                    for (let key in res) {
                        Vue.set(vm.limitedNames, key, res[key].username)
                        Vue.set(vm.limitedUid, key, res[key].uid)
                    }
                }
            };
        },
    }
}
</script>

<style scoped>
.init-banner {
    font-size: 50px;
    background-image: url(../assets/chatroom.jpg);
    background-size: 100% 100%;
    width: 100%;
    height: 7em;
    text-align: center;
}
</style>
