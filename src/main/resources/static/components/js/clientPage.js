
Vue.component('room-list', {
    inject: ['frontendData'],
    data: function() {
       return {
            rooms: []
       };
    },
    template: `
        <div class="room-list" :style="{ backgroundColor: '#f0f0f0' }">
            <h2>List of available rooms:</h2>
                <ul v-if="rooms.length > 0">
                    <li v-for="room in rooms" :key="room.id">
                        <router-link :to="'/room/' + room.id">{{ room.name }}</router-link>
                    </li>
                </ul>
                <p v-else>No rooms available</p>
                <router-link v-if="frontendData.isAdmin" to="/admin">Admin Page</router-link>
        </div>
    `,
    mounted() {
        this.fetchRooms();
        console.log("isAdmin in Vue component:", this.frontendData.isAdmin);
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
                console.log('Response status:', response.status);
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('Data received:', data);
                this.rooms = data;
            })
            .catch(error => {
                console.error('Error fetching chat rooms:', error);
            });
        }
    }
});

const RoomList = {template: '<room-list />'};