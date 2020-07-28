import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/index'
import Login from '@/components/login'
import Search from '@/components/search'
import Upload from '@/components/upload'
import MyPhotos from '@/components/myPhotos'
import MyFavorite from '@/components/myFavorite'
import MyFriends from '@/components/myFriends'
import Modify from '@/components/modify'
import Details from '@/components/details'
import Register from '@/components/register'
import Chatroom from '@/components/chatroom'

Vue.use(Router)

export const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Index',
      component: Index
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register,
    },
    {
      path: '/search',
      name: 'Search',
      component: Search
    },
    {
      path: '/upload',
      name: 'Upload',
      component: Upload,
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/myPhotos',
      name: 'MyPhotos',
      component: MyPhotos,
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/myFavorite',
      name: 'MyFavorite',
      component: MyFavorite,
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/myFriends',
      name: 'MyFriends',
      component: MyFriends,
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/modify',
      name: 'Modify',
      component: Modify,
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/details',
      name: 'Details',
      component: Details,
      meta: {
        requireAuth: true // 需要登录权限
      }
    },
    {
      path: '/chatroom',
      name: 'Chatroom',
      component: Chatroom,
      meta: {
        requireAuth: true // 需要登录权限
      }
    }
  ]
})

// 前端登录拦截
router.beforeEach(function (to, from, next) {
  if (to.matched.some(record => record.meta.requireAuth))
  //需要普通用户身份才能进入的页面
  {
    if (window.sessionStorage.getItem('username') != null) {
      next()
    }
    else {
      next({
        path: '/login',
      })
    }
  }
  else {
    next()
  }

})