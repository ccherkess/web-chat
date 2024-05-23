<template>
  <div>
    <h1>Admin Page</h1>
    <table>
      <thead>
      <tr>
        <th>Room</th>
        <th>Add User</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="room in rooms" :key="room.roomId">
        <td @click="toggleRoomDetails(room.roomId)" style="cursor: pointer;">
          {{ room.name }}
          <span v-if="room.showDetails">▲</span>
          <span v-else>▼</span>
        </td>
        <td v-if="room.showDetails">
          <ul>
>>>>>>> e127bcd (refactor adminPage)
            <li v-for="user in room.users" :key="user.name">
              {{ user.name }} - Read: {{ user.read ? 'Yes' : 'No' }}, Write: {{ user.write ? 'Yes' : 'No' }}
            </li>
          </ul>
        </td>
        <td v-if="room.showDetails">
          <input v-model="newUserNames[room.roomId]" placeholder="Enter username" />
          <button @click="addUserToRoom(room.roomId)">Add User</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rooms: [],
      newUserNames: {}
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
          .then(data => {
            const roomPromises = data.map(room =>
                fetch(`/api/room/fullInfo/${room.id}`, {
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
              this.rooms = roomsWithUsers.map(room => ({ ...room, showDetails: false }));
              this.rooms.forEach(room => {
                this.$set(this.newUserNames, room.roomId, '');
              });
            });
          });
    },
    toggleRoomDetails(roomId) {
      const room = this.rooms.find(room => room.roomId === roomId);
      if (room) {
        room.showDetails = !room.showDetails;
      }
    },
    addUserToRoom(roomId) {
      const username = this.newUserNames[roomId];
      if (!username) {
        alert('Please enter a username');
        return;
      }

      fetch(`/api/room/user/add/${username}`, {
        method: 'PUT',
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
            console.error('Error adding user to room:', error);
          });
    }
  }
}
</script>

<style>

</style>
