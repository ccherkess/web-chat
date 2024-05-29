<template>
    <div class="container">
        <h1 class="header-label">Администрирование комнат</h1>
        <form @submit.prevent="createRoom">
            <div class = "create-room-container">
                   <input v-model="newRoomName" required placeholder="Введите название комнаты" class = "create-room-input"/>
                   <button type="submit" class="create-room-button">Создать</button>
            </div>
        </form>
        <div class="rooms-container" id="scroll">
            <div v-for="room in rooms" :key="room.id">
                <RoomComponent :room = "room" @deleteRoom="deleteRoom"/>
            </div>
        </div>
    </div>
</template>

<script>
    import RoomComponent from '../components/RoomComponent.vue'

    export default {
        components: {
           RoomComponent
        },
        data() {
            return {
              rooms: [],
              newRoomName: ''
            };
        },
        mounted() {
            this.fetchRooms();
        },
        methods: {
            fetchRooms() {
              fetch('/api/room/info/full', {
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
              .then(data => {this.rooms = data; console.log(data)})
              .catch(error => {
                  console.error('Error fetch room:', error);
              });
            },
            createRoom() {
              const roomName = this.newRoomName;
              this.newRoomName = '';
              if (!roomName) {
                alert('Please enter a room name');
                return;
              }

              fetch('/api/room/create', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify({ name: roomName })
              })
              .then(response => {
                if (!response.ok) {
                  throw new Error('Network response was not ok');
                }
                return response.json();
              })
              .then(room => {
                this.newRoomName = '';
                this.fetchRooms();
              })
              .catch(error => {
                console.error('Error creating room:', error);
              });
            },
            deleteRoom(room) {
              fetch('/api/room/delete', {
                method: 'DELETE',
                headers: {
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify({ id: room.id})
              })
              .then(response => {
                if (!response.ok) {
                  throw new Error('Network response was not ok');
                }
                return response.text();
              })
              .then(unused => {
                const index = this.rooms.findIndex(r => r.id === room.id);
                this.rooms.splice(index, 1);
              })
              .catch(error => {
                console.error('Error deleting room:', error);
              });
            },
        }
    }
</script>

<style scoped>
    .container {
        margin: 5% auto;
        width: 50vw;
        border: 2px solid black;
        border-radius: 16px;
        height: 75vh;
        position:relative;
    }
    .header-label {
        margin-bottom: 2vh;
    }
    .create-room-container {
        width: 100%;
        border-radius: 16px;
        padding: 8px;
        border: 2px solid black;
        text-align: center;
        display: flex;
        justify-content: center;
        position: relative;
    }
    .create-room-input {
         width: 85%;
         height: 4vh;
         border: 2px solid black;
         border-radius: 16px;
         padding: 1vh;
         font-size: 24px;
         margin-right: 1vw;
    }
    .create-room-button {
         width: 15%;
         border-radius: 16px;
         border: 2px solid black;
         font-size: 24px;
         padding: 4px;
    }
    .rooms-container {
         overflow-y: auto;
         margin-top: 2vh;
         width: 100%;
         height: 80%;
         padding: 1% 2% 2% 2%;
         border-radius: 16px;
         border: 2px solid black;;
     }
     .rooms-container::-webkit-scrollbar {
         width: 0;
     }
</style>
