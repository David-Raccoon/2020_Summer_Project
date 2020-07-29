<template>
<div>
    <Navigate />
    <div class="container-fluid">
        <div class="row">
            <div class="col-2 left-sidebar"></div>
            <div class="col-8">
                <div class="card">
                    <div class="card-header bg-info text-light">Photo details: {{title}}</div>
                    <div class="card-body">
                        <h3 class="title">{{title}} (by {{username}})</h3>
                        <h5>{{description}}</h5>
                        <div class="row">
                            <div class="col-6">
                                <img :src="src">
                            </div>
                            <div class="col-6">
                                <div class="card">
                                    <div class="card-header bg-info text-light">Classification</div>
                                    <div class="card-body border title">Content: {{content}}</div>
                                    <div class="card-body border title">Country: {{country}}</div>
                                    <div class="card-body border title">City: {{city}}</div>
                                    <div class="card-body border title">
                                        <div class="row">
                                            <div class="col-6 title">
                                                Current like number: {{likeNumber}}
                                            </div>
                                            <div class="col-6">
                                                <button class="btn btn-success form-control" v-show="!isFavor" @click="addFavorite">Add to my favorite!</button>
                                                <button class="btn btn-danger form-control" v-show="isFavor" @click="removeFavorite">Remove from my favorite</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-2 right-sidebar"></div>
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
            src: "",
            title: "",
            originName: "",
            description: "",
            content: "scenery",
            country: "",
            city: "",
            imageID: 0,
            username: "",
            likeNumber: 0,
            isFavor: false
        }
    },
    methods: {
        setFootPrint(imageID, title) {
            var footprint
            if ((sessionStorage.getItem('footprint') == undefined)) {
                footprint = []
            } else {
                footprint = JSON.parse(sessionStorage.getItem('footprint'))
            }
            for (let i in footprint) {
                if (footprint[i].imageID == imageID) {
                    footprint.splice(i, 1)
                }
            }
            footprint.unshift({
                imageID: imageID,
                title: title
            })
            sessionStorage.setItem('footprint', JSON.stringify(footprint))
            console.log(footprint)
        },
        loadPhotoInfo() {
            var id = this.$route.params.id
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetImgByImageID?id=' + id, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    var image = JSON.parse(httpRequest.responseText)
                    vm.title = image.title
                    vm.description = image.description
                    vm.content = image.content
                    vm.country = image.country
                    vm.city = image.city
                    vm.imageID = image.imageID
                    vm.getUsername(image.uid)
                    vm.likeNumber = image.favorNumber
                    vm.src = img_path + "origin/" + image.path
                    vm.checkFavorite()
                    vm.setFootPrint(image.imageID, image.title)
                }
            };
        },
        getUsername(uid) {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'GetUsernameById?id=' + uid, true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    vm.username = httpRequest.responseText
                }
            };
        },
        checkFavorite() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'CheckFavor?imageID=' + this.imageID + "&uid=" + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'true') {
                        vm.isFavor = true
                    } else {
                        vm.isFavor = false
                    }
                }
            };
        },
        addFavorite() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'CreateFavor?imageID=' + this.imageID + "&uid=" + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        vm.loadPhotoInfo()
                    } else
                        alert(httpRequest.responseText)
                }
            };
        },
        removeFavorite() {
            var httpRequest = new XMLHttpRequest()
            var vm = this
            httpRequest.open('GET', backend_path + 'DeleteFavor?imageID=' + this.imageID + '&uid=' + sessionStorage.getItem('uid'), true)
            httpRequest.send()
            httpRequest.onreadystatechange = function () {
                if (httpRequest.readyState == 4 && httpRequest.status == 200) {
                    if (httpRequest.responseText == 'success') {
                        vm.loadPhotoInfo()
                    } else {
                        alert(httpRequest.responseText)
                    }
                }
            };
        },
    },
    mounted() {
        this.loadPhotoInfo()
    }
}
</script>

<style scoped>
img {
    width: 100%;
}

.title {
    font-family: "italic", "Georgia", "serif";
    font-weight: bold;
}

.left-sidebar {
    background-image: url(../assets/left-sidebar.jpg);
    background-size: 100% 100%;
    width: 100%;
}

.right-sidebar {
    background-image: url(../assets/right-sidebar.jpg);
    background-size: 100% 100%;
    width: 100%;
}
</style>
