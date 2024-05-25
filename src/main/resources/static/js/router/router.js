import Vue from 'vue'
import VueRouter from 'vue-router'
import App from 'pages/App.vue'
import RoomList from 'pages/RoomList.vue'
import RoomMessages from 'pages/RoomMessages.vue'
import AdminPage from "../pages/AdminPage.vue";

Vue.use(VueRouter)

const routes = [
    { path: '/', component: RoomList, exact: true },
    { path: '/room/:roomId', component: RoomMessages, props: (route) => ({ roomId: route.params.roomId}) },
    { path: '*', component: RoomList },
    { path: '/admin', component: AdminPage },
]

export default new VueRouter({
    mode: 'history',
    routes
})