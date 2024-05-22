Vue.component('admin-page', {
    data() {
        return {
            rooms: []
        };
    },
    template: `
        <div>
            <h1>Admin Page</h1>
            <table>
                <thead>
                    <tr>
                        <th>Room</th>
                        <th>Users</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="room in rooms" :key="room.id">
                        <td>{{ room.name }}</td>
                        <td>
                            <ul>
                                <li v-for="user in room.chatUsers" :key="user.id">{{ user.name }}</li>
                            </ul>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    `,
    mounted() {
        this.fetchRooms();
    },
    methods: {
        fetchRooms() {
            fetch('/api/room', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
                 })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                const roomPromises = data.map(room =>
                    fetch(`/api/room/info/${room.id}`, {
                        method: 'GET',
                        headers: {
                            'Content-Type': 'application/json'
                        }
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                );

                    Promise.all(roomPromises).then(roomsWithUsers => {
                        this.rooms = roomsWithUsers;
                    });
                });
        }
    }
});

const AdminPage = { template: '<admin-page />' };
