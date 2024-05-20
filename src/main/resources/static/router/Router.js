const routes = [
    { path: '/', component: Vue.component('room-list') },
    { path: '/room/:id', component: Vue.component('room-messages'), props: true }
];

const router = new VueRouter({
    routes
});
