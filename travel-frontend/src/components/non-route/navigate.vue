<template>
<div>
    <nav class="navbar navbar-expand-sm bg-dark navbar-dark">
        <!-- Brand/logo -->
        <a class="navbar-brand"><img width="30" class="logo" src="../../assets/logo.jpg"></a>
        <a class="navbar-brand" href="#">Travel!</a>

        <!-- Links -->
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="#" @click="home">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#" @click="search">Search</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#" @click="login" v-show="!isLogin">Login</a>
            </li>
            <li class="nav-item dropdown" v-show="isLogin">
                <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
                    My acount: {{username}}
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="#" @click="upload">Upload</a>
                    <a class="dropdown-item" href="#" @click="myPhotos">My Photos</a>
                    <a class="dropdown-item" href="#" @click="myFavorite">My Favors</a>
                    <a class="dropdown-item" href="#" @click="myFriends">My Friends</a>
                    <a class="dropdown-item" href="#" @click="chatroom">Chatroom</a>
                    <a class="dropdown-item" href="#" @click="logout">Log out</a>
                </div>
            </li>
        </ul>

    </nav>
</div>
</template>

<script>
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
import 'jquery'

export default {
    data() {
        return {
            isLogin: sessionStorage.getItem('username') != undefined,
            username: sessionStorage.getItem('username')
        }
    },
    methods: {
        home() {
            if (this.$route.name == "Index")
                return
            this.$router.push({
                name: "Index"
            })
        },
        browse() {
            if (this.$route.name == "Browse")
                return
            this.$router.push({
                name: "Browse"
            })
        },
        search() {
            if (this.$route.name == "Search")
                return
            this.$router.push({
                name: "Search"
            })
        },
        chatroom() {
            if (this.$route.name == "Chatroom")
                return
            this.$router.push({
                name: "Chatroom"
            })
        },
        login() {
            if (this.$route.name == "Login")
                return
            this.$router.push({
                name: "Login"
            })
        },
        upload() {
            if (this.$route.name == "Upload")
                return
            this.$router.push({
                name: "Upload"
            })
        },
        myPhotos() {
            if (this.$route.name == "MyPhotos")
                return
            this.$router.push({
                name: "MyPhotos"
            })
        },
        myFavorite() {
            if (this.$route.name == "MyFavorite")
                return
            this.$router.push({
                name: "MyFavorite"
            })
        },
        myFriends() {
            if (this.$route.name == "MyFriends")
                return
            this.$router.push({
                name: "MyFriends"
            })
        },
        logout() {
            sessionStorage.removeItem('username')
            sessionStorage.removeItem('uid')
            if (this.$route.name == "Index")
                this.$router.go(0)
            else {
                this.$router.push({
                    name: "Index"
                })
            }
        },
    }
}
</script>