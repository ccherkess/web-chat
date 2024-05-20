

Vue.component('room-list', {
    data: function() {
       return {
            rooms: []
       };
    },
    template: `
        <div class="room-list" :style="{ backgroundColor: '#f0f0f0' }">
            <h2>Список доступных каналов:</h2>
                <ul v-if="rooms.length > 0">
                    <li v-for="room in rooms" :key="room.id">
                        <router-link :to="'/room/' + room.id">{{ room.name }}</router-link>
                    </li>
                </ul>
                <p v-else>No rooms available</p>
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