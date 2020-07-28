<template>
<div>
    <Navigate />
    <div class="container">
        <h2>Login</h2>
        <a href="#" @click="register">Do not have an account? Register now!</a>
        <div class="form-group">
            <label for="text">Username:</label>
            <input type="text" class="form-control" v-model="username" placeholder="Enter username">
        </div>
        <div class="form-group">
            <label for="pwd">Password:</label>
            <input type="password" class="form-control" v-model="password" placeholder="Enter password">
        </div>
        <input type="submit" class="btn btn-primary" value="Login" @click="login" />
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
            errorMsg: ''
        }
    },
    methods: {
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
        login() {
            if (this.username == '' || this.password == '') {
                this.errorMsg = "Please finish the form!"
                return;
            }
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('POST', backend_path + 'Login', true)
            httpRequest.setRequestHeader("Content-type", "application/x-www-form-urlencoded")
            httpRequest.send('username=' + this.username)
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'error: wrong username or password') {
                        vm.errorMsg = httpRequest.responseText
                    } else {
                        if (vm.decode(httpRequest.responseText) == vm.password) {
                            sessionStorage.setItem("username", vm.username)
                            vm.getuid()
                            vm.$router.push({
                                path: "/"
                            })
                        } else {
                            vm.errorMsg = "error: wrong username or password"
                        }
                    }
                }
            };
        },
        register() {
            this.$router.push({
                name: "Register"
            })
        },
        getuid() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetIdByUsername?username=' + this.username, true)
            httpRequest.send('username=' + this.username)
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    sessionStorage.setItem('uid', httpRequest.responseText)
                }
            };
        }
    }
}
</script>
