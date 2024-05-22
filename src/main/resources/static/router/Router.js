Vue.use(VueRouter);

const routes = [
    { path: '/', component: RoomList, exact: true },
    { path: '/room/:roomId', component: RoomMessages, props: (route) => ({ roomId: route.params.roomId }) }
    { path: '*', component: RoomList }
];

const router = new VueRouter({
    mode: 'history',
    routes: routes
});

routes.forEach(route => {
    console.log(`Route path: ${route.path}, props: ${route.props}`);
});