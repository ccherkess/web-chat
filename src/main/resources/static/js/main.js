import Vue from 'vue'
import router from 'router/router'
import App from 'pages/App.vue'

new Vue({
    el: '#app',
    router,
    render: a => a(App)
})