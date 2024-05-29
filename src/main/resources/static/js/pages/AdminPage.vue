<template>
    <div class="container">
        <h1>Страница администрирования</h1>
        <router-link to="/admin/room" tag = "button" class = "button">
            <h2>Комнаты</h2>
        </router-link>
         <router-link to="/admin/user" tag = "button" class = "button">
            <h2>Пользователи</h2>
        </router-link>
        <router-link to="/admin/register" tag = "button" class = "button">
            <h2>Заявки</h2>
        </router-link>
    </div>
</template>

<script>
export default {
  data() {
    return {
      rooms: [],
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
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.json();
      })
      .then(data => this.rooms = data)
      .catch(error => {
          console.error('Error fetch room:', error);
      });
    },
    createRoom() {
      const roomName = this.newRoomName;
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
            alert(`Room "${room.name}" created successfully!`);
            this.newRoomName = '';
            this.fetchRooms();
          })
          .catch(error => {
            console.error('Error creating room:', error);
          });
    },
    deleteRoom(roomId) {
      fetch('/api/room/delete', {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ id: roomId })
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.text();
          })
          .then(message => {
            alert(message);
            this.fetchRooms();
          })
          .catch(error => {
            console.error('Error deleting room:', error);
          });
    }
  }
}
</script>

<style scoped>
    .container {
        margin: 5% auto;
        width: 50vw;
        border: 2px solid black;
        border-radius: 16px;
        position:relative;
    }
    .button {
        height: 4vh;
        width: 100%;
        border: 2px solid black;
        margin-top: 2vh;
    }
</style>
