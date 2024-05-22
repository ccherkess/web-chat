
const app = new Vue({
    data: {
        frontendData: window.frontendData || {}
    },
    router: router,
    provide() {
        return {
            frontendData: this.frontendData
        };
    }
}).$mount('#app');;
