Vue.use(VueRouter);

const routes = [
    { path: '/', component: RoomList, exact: true },
    { path: '/room/:roomId', component: RoomMessages, props: (route) => ({ roomId: route.params.roomId }) },
    { path: '/admin', component: AdminPage },
    { path: '*', component: RoomList }
];

const router = new VueRouter({
    mode: 'history',
    routes: routes
});

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAdmin)) {
        console.log("Navigating to admin route. isAdmin:", window.appData.isAdmin);
        if (window.appData.isAdmin) {
            next();
        } else {
            next('/');
        }
    } else {
        next();
    }
});
routes.forEach(route => {
    console.log(`Route path: ${route.path}, props: ${route.props}`);
});