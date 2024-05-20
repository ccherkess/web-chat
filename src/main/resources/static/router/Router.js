Vue.use(VueRouter);

const router = new VueRouter({
    mode: 'history',
    routes: [
        { path: '/', component: RoomList, exact: true},
        { path: '/room/:id', component: RoomMessages, props: true }
    ]
});
