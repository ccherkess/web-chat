import Vue from 'vue'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import router from 'router/router'
import App from 'pages/App.vue'
import socket from 'util/socket'

Vue.use(Vuetify)

socket.connect()

new Vue({
    el: '#app',
    router,
    render: a => a(App)
})