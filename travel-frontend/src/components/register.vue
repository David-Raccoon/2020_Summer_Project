<template>
<div>
    <Navigate />
    <div class="container">
        <h2>Register</h2>
        <a href="#" @click="login">Already have an account? Login now!</a>
        <div class="form-group">
            <label for="text">Username:</label>
            <input type="text" class="form-control" v-model="username" placeholder="Enter username">
        </div>
        <div class="alert alert-success" v-show="successMsgUsername!=''">
            {{successMsgUsername}}
        </div>
        <div class="alert alert-danger" v-show="errorMsgUsername!=''">
            {{errorMsgUsername}}
        </div>
        <div class="form-group">
            <label for="pwd">Password:</label>
            <input type="password" class="form-control" v-model="password" placeholder="Enter password">
            <label>Strength</label>
            <div class="progress">
                <div class="progress-bar" :style="width"></div>
            </div>
        </div>
        <div class="alert alert-success" v-show="successMsgPassword!=''">
            {{successMsgPassword}}
        </div>
        <div class="alert alert-danger" v-show="errorMsgPassword!=''">
            {{errorMsgPassword}}
        </div>
        <div class="form-group">
            <label for="pwd">Confirm Password:</label>
            <input type="password" class="form-control" v-model="rePassword" placeholder="Enter re-password">
        </div>
        <div class="alert alert-success" v-show="successMsgRePassword!=''">
            {{successMsgRePassword}}
        </div>
        <div class="alert alert-danger" v-show="errorMsgRePassword!=''">
            {{errorMsgRePassword}}
        </div>
        <div class="form-group">
            <label for="pwd">Email:</label>
            <input type="email" class="form-control" v-model="email" placeholder="Enter email">
        </div>
        <div class="alert alert-success" v-show="successMsgEmail!=''">
            {{successMsgEmail}}
        </div>
        <div class="alert alert-danger" v-show="errorMsgEmail!=''">
            {{errorMsgEmail}}
        </div>
        <input type="submit" class="btn btn-primary" value="register" @click="register" />
        <div class="alert alert-danger" v-show="errorMsg!=''">
            {{errorMsg}}
        </div>
    </div>
    <Footer />
</div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
import 'jquery'
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
            username: '',
            password: '',
            rePassword: '',
            email: '',
            successMsgUsername: '',
            successMsgPassword: '',
            successMsgRePassword: '',
            successMsgEmail: '',
            errorMsgUsername: '',
            errorMsgPassword: '',
            errorMsgRePassword: '',
            errorMsgEmail: '',
            errorMsg: '',

            width: "width:0%"
        }
    },
    methods: {
        login() {
            this.$router.push({
                name: "Login"
            })
        },
        encode: function (input) {
            var _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
            var output = "";
            var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
            var i = 0;
            while (i < input.length) {
                chr1 = input.charCodeAt(i++);
                chr2 = input.charCodeAt(i++);
                chr3 = input.charCodeAt(i++);
                enc1 = chr1 >> 2;
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                enc4 = chr3 & 63;
                if (isNaN(chr2)) {
                    enc3 = enc4 = 64;
                } else if (isNaN(chr3)) {
                    enc4 = 64;
                }
                output = output +
                    _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                    _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
            }
            return output;
        },
        decode: function (input) {
            var _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
            var output = "";
            var chr1, chr2, chr3;
            var enc1, enc2, enc3, enc4;
            var i = 0;
            input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
            while (i < input.length) {
                enc1 = _keyStr.indexOf(input.charAt(i++));
                enc2 = _keyStr.indexOf(input.charAt(i++));
                enc3 = _keyStr.indexOf(input.charAt(i++));
                enc4 = _keyStr.indexOf(input.charAt(i++));
                chr1 = (enc1 << 2) | (enc2 >> 4);
                chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                chr3 = ((enc3 & 3) << 6) | enc4;
                output = output + String.fromCharCode(chr1);
                if (enc3 != 64) {
                    output = output + String.fromCharCode(chr2);
                }
                if (enc4 != 64) {
                    output = output + String.fromCharCode(chr3);
                }
            }
            return output;
        },
        checkPassWord(value) {
            var strength = 0
            // 0： 表示第一个级别 1：表示第二个级别 2：表示第三个级别
            // 3： 表示第四个级别 4：表示第五个级别
            if (value.length > 8) { //最初级别
                strength += 20
            }
            if (/\d/.test(value)) { //如果用户输入的密码 包含了数字
                strength += 20
            }
            if (/[a-z]/.test(value)) { //如果用户输入的密码 包含了小写的a到z
                strength += 20
            }
            if (/[A-Z]/.test(value)) { //如果用户输入的密码 包含了大写的A到Z
                strength += 20
            }
            if (/_/.test(value)) { //如果是非数字 字母 下划线
                strength += 20
            }
            this.width = "width:" + strength + "%"
        },
        register() {
            if (this.errorMsgUsername == '' &&
                this.errorMsgPassword == '' &&
                this.errorMsgRePassword == '' &&
                this.errorMsgEmail == '' &&
                this.username != '' &&
                this.password != '' &&
                this.rePassword != '' &&
                this.email != '') {
                var httpRequest = new XMLHttpRequest()
                var vm = this

                httpRequest.open('POST', backend_path + '/Register', true)
                httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
                httpRequest.send('username=' + this.username + '&password=' + this.encode(this.password) + '&email=' + this.email)
                httpRequest.onreadystatechange = function () {
                    if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                        if (httpRequest.responseText == 'success') {
                            alert('Success!')
                            vm.$router.push({
                                path: "/login"
                            })
                        } else
                            vm.errorMsg = httpRequest.responseText
                    }
                };
            } else {
                this.errorMsg = 'You have not finished the form correctly yet!'
            }
        }
    },
    watch: {
        username: function () {
            var reg = /^[a-zA-Z0-9_.]{4,15}$/;
            if (reg.test(this.username)) {
                var httpRequest = new XMLHttpRequest()
                var vm = this
                httpRequest.open('GET', backend_path + 'Register?checkName=' + this.username, true)
                httpRequest.send()
                httpRequest.onreadystatechange = function () {
                    if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                        if (httpRequest.responseText == "true") {
                            vm.successMsgUsername = 'Vaild username.'
                            vm.errorMsgUsername = ''
                            return
                        } else {
                            vm.errorMsgUsername = 'Username already exists.'
                            vm.successMsgUsername = ''
                            return
                        }
                    }
                };
            } else {
                this.errorMsgUsername = 'The username should be 4-15 upper and lower case letters and Numbers'
                this.successMsgUsername = ''
                return
            }
        },
        password: function () {
            var reg = /^[a-zA-Z0-9_]{6,12}$/;
            if (reg.test(this.password)) {
                this.successMsgPassword = 'Vaild password.'
                this.errorMsgPassword = ''
                this.checkPassWord(this.password)
                return
            } else {
                this.errorMsgPassword = 'The password should be 6-12 upper and lower case letters and Numbers and _'
                this.successMsgPassword = ''
                this.width = "width:0%"
                return
            }
        },
        rePassword: function () {
            if (this.password == this.rePassword) {
                this.successMsgRePassword = 'Correct confirm password.'
                this.errorMsgRePassword = ''
                return
            } else {
                this.errorMsgRePassword = 'The password is inconsistent with the confirmation password.'
                this.successMsgRePassword = ''
                return
            }
        },
        email: function () {
            var reg = /^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;
            if (reg.test(this.email)) {
                this.successMsgEmail = 'Vaild email address.'
                this.errorMsgEmail = ''
                return
            } else {
                this.errorMsgEmail = 'Invalid email address.'
                this.successMsgEmail = ''
                return
            }
        }
    }
}
</script>
