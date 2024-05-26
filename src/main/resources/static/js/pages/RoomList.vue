<template>
    <div class = "container">
         <router-link class = "admin" v-if="frontendData.isAdmin" tag = "button"  to="/admin">
            <h2>Страница админа</h2>
        </router-link>
        <h1 class = "header-label">Список доступных каналов</h1>
        <div v-if="rooms.length > 0">
            <div v-for="room in rooms" :key="room.id" class = "room-item">
                <router-link :to="'/room/' + room.id" tag = "button" class = "room-button">
                    <h2>{{ room.name }}</h2>
                </router-link>
            </div>
        </div>
        <h2 v-else class = "room-not-available">Вы не состоите ни в одном канале :(</h2>
    </div>
</template>

<script>
    export default {
        props: {
           frontendData: {
             type: Object,
             default: () => {{ isAdmin: false }}
           }
        },
        data() {
           return {
                rooms: []
           };
        },
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
    }
</script>

<style scoped>
    .admin {
        width: 100%;
        border: 2px solid black;
        border-radius: 16px;
        text-align: center;
        padding: 1vh;
        margin: 1vh 0;
    }
    .container {
        margin: 5% auto;
        width: 50vw;
        border: 2px solid black;
        border-radius: 16px;
    }
    .header-label {
        margin: 0;
    }
    .room-item {
        padding: 0;
    }
    .room-button {
        height: 4vh;
        width: 100%;
        border: 2px solid black;
        margin-top: 2vh;
    }
    .room-not-available {
        margin: 2vh auto;
        text-align: center;
    }
</style>