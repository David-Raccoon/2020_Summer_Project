<template>
<div>
    <Navigate />
    <div class="row">
        <div class="col-1"></div>
        <div class="col-3">
            <div class="card">
                <!-- 模态框 -->
                <div class="modal fade" id="myModal">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <!-- 模态框头部 -->
                            <div class="modal-header">
                                <h4 class="modal-title">{{modalMessage}}</h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                            </div>
                            <!-- 模态框底部 -->
                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal" v-show='modalMessage!="User does not exist!"' @click="sendRequest">Send</button>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card-header bg-secondary text-light">Find a user!</div>
                <div class="card-footer">
                    <div class="form-group" style="text-align:center">
                        <input type="text" class="form-control" v-model="searchUsername">
                        <br />
                        <button class="btn btn-primary form-control" data-toggle="modal" data-target="#myModal" @click="searchFriend">search by username</button>
                    </div>
                </div>
            </div>
            <div class="card">
                <div class="card-header bg-secondary text-light">My friends</div>
                <div class="card-body" v-for="(friendName,index) in friendNames" :key="index">
                    <a href="#" @click="loadUserInfo(index)">{{friendName}}</a>

                </div>
            </div>
            <div class="card">
                <div class="card-header bg-secondary text-light">My friends (Limited access)</div>
                <div class="card-body" v-for="(friendName,index) in limitedNames" :key="index">
                    <a href="#" @click="loadLimitedUserInfo(index)">{{friendName}}</a>
                </div>
            </div>
            <div class="card">
                <div class="card-header bg-secondary text-light">Friend request I sent to</div>
                <div class="card-body" v-for="(friendName,index) in invitingNames" :key="index">
                    <a href="#" @click="loadInvitingInfo(index)">{{friendName}}</a>
                </div>
            </div>
            <div class="card">
                <div class="card-header bg-secondary text-light">Friend request I received</div>
                <div class="card-body" v-for="(friendName,index) in invitedNames" :key="index">
                    <a href="#" @click="loadInvitedInfo(index)">{{friendName}}</a>
                </div>
            </div>
        </div>
        <div class="col-7">
            <div class="card">
                <div class="card-header bg-secondary text-light">User: {{infoName}}</div>
                <div class="card-body border">
                    <div>id: {{infoUid}}</div>
                    <div>email: {{infoEmail}}</div>
                    <div>sign up date: {{infoDate}}</div>
                    <div>options:
                        <span v-show="optionShow==1">
                            <button class="btn btn-info" @click="limit(friendUid[friendOptionIndex])">
                                limit his/her access to me
                            </button>
                            <button class="btn btn-secondary" @click="unlimit(friendUid[friendOptionIndex])">
                                unlimit his/her access to me
                            </button>
                            <button class="btn btn-danger" @click="deleteFriend(friendUid[friendOptionIndex])">
                                delete this friend
                            </button>
                        </span>
                        <span v-show="optionShow==2">
                            <button class="btn btn-info" @click="limit(limitedUid[limitedOptionIndex])">
                                limit his/her access to me
                            </button>
                            <button class="btn btn-secondary" @click="unlimit(limitedUid[limitedOptionIndex])">
                                unlimit his/her access to me
                            </button>
                            <button class="btn btn-danger" @click="deleteFriend(limitedUid[limitedOptionIndex])">
                                delete this friend
                            </button>
                        </span>
                        <span v-show="optionShow==4">
                            <button class="btn btn-success" @click="accept(invitedUid[invitedOptionIndex])">
                                accept
                            </button>
                            <button class="btn btn-danger" @click="reject(invitedUid[invitedOptionIndex])">
                                reject
                            </button>
                        </span>
                    </div>
                </div>
                <div class="card-header bg-secondary text-light">{{infoName}}'s favors:</div>
                <div class="card-body border" style="text-align:center">
                    <div v-show="resultCount==0&&denied==false" class="row">
                        <div class="col-9">
                            He/She seems to hava no favorite photo yet...
                        </div>
                        <img class="col-3" src="../assets/empty.jpg">
                    </div>
                    <div v-show="denied" class="row">
                        <div class="col-9">
                            Your access was denied due to privacy setting...
                        </div>
                        <img class="col-3" src="../assets/denied.jpg">
                    </div>
                    <div class="row" v-for="i in 3" :key="i">
                        <div class="col-4" v-for="j in 3" :key="j" v-show="j+3*(i-1)+(currentPage-1)*pageSize<=resultCount">
                            <a href="#" @click="details(imageID[3*(i-1)+j-1+pageSize*(currentPage-1)])">
                                <img :src="src[3*(i-1)+j-1+pageSize*(currentPage-1)]" />
                            </a>
                        </div>
                    </div>
                    <div class="btn-group btn-group-sm" style="margin-top:1em">
                        <button type="button" v-show="pageCount>=1" class="btn btn-primary" @click="turnToPage(currentPage-1)">《</button>
                        <button type="button" v-for="i in pageCount" :key="i" :id="i" class="btn btn-primary" @click="turnToPage(i)">{{i}}</button>
                        <button type="button" v-show="pageCount>=1" class="btn btn-primary" @click="turnToPage(currentPage+1)">》</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-1"></div>
    </div>
    <Footer />
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
            searchUsername: "",
            modalMessage: "",
            targetUid: 0,

            friendNames: [],
            friendUid: [],
            friendEmail: [],
            friendSignUpDate: [],
            friendOptionIndex: 0,

            limitedNames: [],
            limitedUid: [],
            limitedEmail: [],
            limitedSignUpDate: [],
            limitedOptionIndex: 0,

            invitingNames: [],
            invitingUid: [],
            invitingEmail: [],
            invitingSignUpDate: [],

            invitedNames: [],
            invitedUid: [],
            invitedEmail: [],
            invitedSignUpDate: [],
            invitedOptionIndex: 0,

            optionShow: 0,

            infoName: "",
            infoEmail: "",
            infoDate: "",
            infoUid: 0,

            src: [],
            imageID: [],
            denied: false,

            pageCount: 1,
            resultCount: 0,
            currentPage: 1,
            pageSize: 9,
            keyword: "",
        }
    },
    methods: {
        loadUserInfo(index) {
            this.optionShow = 1
            this.infoUid = this.friendUid[index]
            this.infoName = this.friendNames[index]
            this.infoEmail = this.friendEmail[index]
            this.infoDate = this.friendSignUpDate[index]
            this.friendOptionIndex = index

            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetFavorImg?uid1=' + sessionStorage.getItem('uid') + "&uid2=" + this.infoUid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    vm.src = []
                    vm.imageID = []
                    vm.denied = false
                    vm.resultCount = 0;
                    if (httpRequest.responseText != "denied") {
                        var res = JSON.parse(httpRequest.responseText)
                        for (let key in res) {
                            Vue.set(vm.src, key, img_path + "square/" + res[key].path)
                            Vue.set(vm.imageID, key, res[key].imageID)
                            vm.resultCount++;
                        }
                    }
                }
            };
        },
        loadLimitedUserInfo(index) {
            this.optionShow = 2
            this.infoUid = this.limitedUid[index]
            this.infoName = this.limitedNames[index]
            this.infoEmail = this.limitedEmail[index]
            this.infoDate = this.limitedSignUpDate[index]
            this.src = []
            this.imageID = []
            this.resultCount = 0
            this.denied = true
            this.limitedOptionIndex = index
        },
        loadInvitingInfo(index) {
            this.optionShow = 3
            this.infoUid = this.invitingUid[index]
            this.infoName = this.invitingNames[index]
            this.infoEmail = this.invitingEmail[index]
            this.infoDate = this.invitingSignUpDate[index]
            this.src = []
            this.imageID = []
            this.resultCount = 0
            this.denied = true
        },
        loadInvitedInfo(index) {
            this.invitedOptionIndex = index
            this.optionShow = 4
            this.infoUid = this.invitedUid[index]
            this.infoName = this.invitedNames[index]
            this.infoEmail = this.invitedEmail[index]
            this.infoDate = this.invitedSignUpDate[index]
            this.src = []
            this.imageID = []
            this.resultCount = 0
            this.denied = true
        },
        setPage() {
            this.resultCount = this.src.length
            this.pageCount = Math.ceil(this.resultCount / this.pageSize)
        },
        turnToPage(num) {
            if (num > this.pageCount || num < 1)
                return
            this.currentPage = num
            for (let i = 1; i <= this.pageCount; i++) {
                if (document.getElementById(String(i)) == null)
                    continue
                if (i == num)
                    document.getElementById(String(i)).setAttribute('class', 'btn btn-success')
                else
                    document.getElementById(String(i)).setAttribute('class', 'btn btn-primary')
            }
        },
        details(imageID) {
            this.$router.push({
                path: "/details",
                name: "Details",
                params: {
                    id: imageID
                }
            })
        },
        chat(uid, username) {
            this.$router.push({
                path: "/chatroom",
                name: "Chatroom",
                params: {
                    targetUid: uid,
                    targetUsername: username
                }
            })
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
                    vm.friendEmail = []
                    vm.friendSignUpDate = []
                    for (let key in res) {
                        Vue.set(vm.friendNames, key, res[key].username)
                        Vue.set(vm.friendUid, key, res[key].uid)
                        Vue.set(vm.friendEmail, key, res[key].email)
                        Vue.set(vm.friendSignUpDate, key, res[key].signUpDate)
                    }
                    if (vm.friendUid != []) {
                        vm.loadUserInfo(0)
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
                    vm.limitedEmail = []
                    vm.limitedSignUpDate = []
                    for (let key in res) {
                        Vue.set(vm.limitedNames, key, res[key].username)
                        Vue.set(vm.limitedUid, key, res[key].uid)
                        Vue.set(vm.limitedEmail, key, res[key].email)
                        Vue.set(vm.limitedSignUpDate, key, res[key].signUpDate)
                    }
                }
            };
        },
        getInvitingData() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetFriendList?state=inviting&uid=' + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var res = JSON.parse(httpRequest.responseText)
                    vm.invitingNames = []
                    vm.invitingUid = []
                    vm.invitingEmail = []
                    vm.invitingSignUpDate = []
                    for (let key in res) {
                        Vue.set(vm.invitingNames, key, res[key].username)
                        Vue.set(vm.invitingUid, key, res[key].uid)
                        Vue.set(vm.invitingEmail, key, res[key].email)
                        Vue.set(vm.invitingSignUpDate, key, res[key].signUpDate)
                    }
                }
            };
        },
        getInvitedData() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetFriendList?state=invited&uid=' + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var res = JSON.parse(httpRequest.responseText)
                    vm.invitedNames = []
                    vm.invitedUid = []
                    vm.invitedEmail = []
                    vm.invitedSignUpDate = []
                    for (let key in res) {
                        Vue.set(vm.invitedNames, key, res[key].username)
                        Vue.set(vm.invitedUid, key, res[key].uid)
                        Vue.set(vm.invitedEmail, key, res[key].email)
                        Vue.set(vm.invitedSignUpDate, key, res[key].signUpDate)
                    }
                }
            };
        },
        refresh() {
            this.getFriendData()
            this.getLimitedFriendData()
            this.getInvitingData()
            this.getInvitedData()
        },
        accept(uid) {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'RequestExecute?action=accept&uid1=' + sessionStorage.getItem('uid') + '&uid2=' + uid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        alert("Success!")
                        vm.refresh()
                    }
                }
            };
        },
        reject(uid) {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'RequestExecute?action=reject&uid1=' + sessionStorage.getItem('uid') + '&uid2=' + uid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        alert("Success!")
                        vm.refresh()
                    }
                }
            };
        },
        limit(uid) {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'FriendExecute?action=limit&uid2=' + sessionStorage.getItem('uid') + '&uid1=' + uid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        alert("Success!")
                        vm.refresh()
                    } else {
                        alert('His/Her access is already limited!')
                    }
                }
            };
        },
        unlimit(uid) {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'FriendExecute?action=unlimit&uid2=' + sessionStorage.getItem('uid') + '&uid1=' + uid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        alert("Success!")
                        vm.refresh()
                    } else {
                        alert('His/Her access is already unlimited!')
                    }
                }
            };
        },
        deleteFriend(uid) {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'FriendExecute?action=delete&uid1=' + sessionStorage.getItem('uid') + '&uid2=' + uid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        alert("Success!")
                        vm.refresh()
                    } else {
                        alert(httpRequest.responseText)
                    }
                }
            };
        },
        searchFriend() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetIdByUsername?username=' + this.searchUsername, true)
            httpRequest.send('username=' + this.username)
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText != 'User does not exist!') {
                        vm.targetUid = httpRequest.responseText
                        vm.modalMessage = 'You are send friend request to ' + vm.searchUsername + '(id: ' + vm.targetUid + ')'
                    } else {
                        vm.modalMessage = 'User does not exist!'
                    }
                }
            };
        },
        sendRequest() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'RequestExecute?action=send&uid1=' + sessionStorage.getItem('uid') + '&uid2=' + this.targetUid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        alert("Success!")
                        vm.refresh()
                    } else {
                        alert(httpRequest.responseText)
                    }
                }
            };
        }
    },
    mounted() {
        this.refresh()
    },
}
</script>
