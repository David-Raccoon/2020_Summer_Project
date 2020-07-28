<template>
<div>
    <Navigate />
    <Banner />
    <div class="container">
        <div class="card">
            <div class="card-header bg-secondary text-light">My Favorite</div>
            <div class="card-body">
                <!-- 模态框 -->
                <div class="modal fade" id="myModal">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <!-- 模态框头部 -->
                            <div class="modal-header">
                                <h4 class="modal-title">Are you sure to remove this favorite?</h4>
                                <button type="button" class="close" data-dismiss="modal">&times;</button>
                            </div>
                            <!-- 模态框主体 -->
                            <div class="modal-body">
                                {{title[deleteIndex]}}
                            </div>
                            <!-- 模态框底部 -->
                            <div class="modal-footer">
                                <button type="button" class="btn btn-danger" data-dismiss="modal" @click="removeFavorite(imageID[deleteIndex])">Confirm</button>
                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
                <div v-show="resultCount==0" class="row">
                    <div class="col-9">
                        You have no favorite photo yet. Try to add some!
                    </div>
                    <img class="col-3" src="../assets/empty.jpg">
                </div>
                <div class="row border" v-for="i in 5" :key="i" v-show="i+5*(currentPage-1)<=resultCount">
                    <div class="col-3 ">
                        <a href="#" @click="details(imageID[i-1+5*(currentPage-1)])">
                            <img :src="src[i-1+5*(currentPage-1)]">
                        </a>
                    </div>
                    <div class="col-6">
                        <p>{{title[i-1+5*(currentPage-1)]}}</p>
                        <p class="description">{{description[i-1+5*(currentPage-1)]}}</p>
                    </div>
                    <div class="col-3">
                        <br /><br />
                        <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#myModal" @click="deleteIndex=i-1+5*(currentPage-1)">
                            Remove from favorite
                        </button>
                    </div>
                </div>
                <br />
                <div style="text-align:center">
                    <div class="btn-group btn-group-sm">
                        <button type="button" v-show="pageCount>=1" class="btn btn-primary" @click="turnToPage(currentPage-1)">《</button>
                        <button type="button" v-for="i in pageCount" :key="i" :id="i" class="btn btn-primary" @click="turnToPage(i)">{{i}}</button>
                        <button type="button" v-show="pageCount>=1" class="btn btn-primary" @click="turnToPage(currentPage+1)">》</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="alert alert-primary" style="text-align:center">
            My Footprint
        </div>
        <div class="alert alert-secondary" style="text-align:center" v-for="i in footprint" :key="i.imageID">
            <a href="#" @click="details(i.imageID)">{{i.title}}</a>
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
            deleteIndex: 0,
            src: [],
            title: [],
            description: [],
            imageID: [],
            pageCount: 1,
            resultCount: 0,
            currentPage: 1,
            pageSize: 5,
            footprint: []
        }
    },
    methods: {
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
        removeFavorite(imageID) {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            var path = this.src[this.deleteIndex].split('/')
            var imgName = path[path.length - 1]
            httpRequest.open('GET', backend_path + 'DeleteFavor?imageID=' + imageID + '&uid=' + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success')
                        vm.refresh()
                    else {
                        console.log(httpRequest.responseText)
                    }
                }
            };
        },
        refresh() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetFavorImg?uid1=' + sessionStorage.getItem('uid') + "&uid2=" + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var res = JSON.parse(httpRequest.responseText)
                    vm.src = []
                    vm.title = []
                    vm.description = []
                    vm.imageID = []
                    for (let key in res) {
                        if (res[key] == '')
                            continue
                        Vue.set(vm.src, key, img_path + 'square/' + res[key].path)
                        Vue.set(vm.title, key, res[key].title)
                        Vue.set(vm.description, key, res[key].description)
                        Vue.set(vm.imageID, key, res[key].imageID)
                    }
                    vm.setPage()
                    vm.turnToPage(1)
                }
            };
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
        upload() {
            if (this.$route.name == "Upload")
                return
            this.$router.push({
                name: "Upload"
            })
        },
    },
    mounted() {
        this.refresh()
        this.footprint = JSON.parse(sessionStorage.getItem('footprint'))
    }
}
</script>

<style scoped>
.description {
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
}
</style>
