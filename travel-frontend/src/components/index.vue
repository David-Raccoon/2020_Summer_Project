<template>
<div>
    <Navigate />
    <Banner />
    <div class="container-fluid">
        <div class="alert alert-success" style="text-align:center">
            Latest-updated image right now!
        </div>
        <div class="row" v-for="i in 2" :key=i>
            <div class="col" v-for="j in 3" :key=j>
                <div class="alert alert-dark">
                    <div class="row">
                        <div class="col-4">
                            <a href="#" @click="details(lateImageID[j-1+3*(i-1)])">
                                <img :src="lateSrc[j-1+3*(i-1)]" style="width:100%" />
                            </a>
                        </div>
                        <div class="col-8">
                            <p>{{lateTitle[j-1+3*(i-1)]}}</p>
                            <p class="description">{{lateDescription[j-1+3*(i-1)]}}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="container-fluid">
        <div class="alert alert-danger" style="text-align:center">Hottest Image Right Now!</div>
        <div class="row">
            <div class="col-2"></div>
            <div id="demo" class="carousel slide col-8" data-ride="carousel">
                <!-- 指示符 -->
                <ul class="carousel-indicators">
                    <li data-target="#demo" data-slide-to="0" class="active"></li>
                    <li data-target="#demo" data-slide-to="1"></li>
                    <li data-target="#demo" data-slide-to="2"></li>
                    <li data-target="#demo" data-slide-to="3"></li>
                    <li data-target="#demo" data-slide-to="4"></li>
                </ul>
                <!-- 轮播图片 -->
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <a href="#" @click="details(hotImageID[0])">
                            <img :src="hotSrc[0]">
                        </a>
                    </div>
                    <div class="carousel-item">
                        <a href="#" @click="details(hotImageID[1])">
                            <img :src="hotSrc[1]">
                        </a>
                    </div>
                    <div class="carousel-item">
                        <a href="#" @click="details(hotImageID[2])">
                            <img :src="hotSrc[2]">
                        </a>
                    </div>
                    <div class="carousel-item">
                        <a href="#" @click="details(hotImageID[3])">
                            <img :src="hotSrc[3]">
                        </a>
                    </div>
                    <div class="carousel-item">
                        <a href="#" @click="details(hotImageID[4])">
                            <img :src="hotSrc[4]">
                        </a>
                    </div>
                </div>
                <!-- 左右切换按钮 -->
                <a class="carousel-control-prev" href="#demo" data-slide="prev">
                    <span class="carousel-control-prev-icon"></span>
                </a>
                <a class="carousel-control-next" href="#demo" data-slide="next">
                    <span class="carousel-control-next-icon"></span>
                </a>
            </div>
            <div class="col-2"></div>
        </div>
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
import Banner from './non-route/banner.vue'
import {
    backend_path,
    img_path
} from '../assets/config.js'

export default {
    components: {
        Navigate,
        Footer,
        Banner
    },
    data() {
        return {
            hotSrc: [],
            hotTitle: [],
            hotDescription: [],
            hotImageID: [],

            lateSrc: [],
            lateTitle: [],
            lateDescription: [],
            lateImageID: [],

            count: 9
        }
    },
    methods: {
        details(imageID) {
            this.$router.push({
                path: "/details",
                name: "Details",
                params: {
                    id: imageID
                }
            })
        },
        getHotImg() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetHottestImg' + '?count=' + 5, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var image = JSON.parse(httpRequest.responseText)
                    for (let i in image) {
                        Vue.set(vm.hotSrc, i, img_path + "origin/" + image[i].path)
                        Vue.set(vm.hotTitle, i, image[i].title)
                        Vue.set(vm.hotDescription, i, image[i].description)
                        Vue.set(vm.hotImageID, i, image[i].imageID)
                    }
                }
            };
        },
        getLateImg() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetLatestImg' + '?count=' + 6, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var image = JSON.parse(httpRequest.responseText)
                    for (let i in image) {
                        Vue.set(vm.lateSrc, i, img_path + "square/" + image[i].path)
                        Vue.set(vm.lateTitle, i, image[i].title)
                        Vue.set(vm.lateDescription, i, image[i].description)
                        Vue.set(vm.lateImageID, i, image[i].imageID)
                    }
                }
            };
        },
        refresh() {
            this.getHotImg()
            this.getLateImg()
        },
    },
    mounted() {
        this.refresh()
    }
}
</script>

<style scoped>
.description {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
}

/* Make the image fully responsive */
.carousel-inner img {
    width: 100%;
    height: 100%;
}
</style>
