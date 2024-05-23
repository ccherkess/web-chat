<template>
  <div>
    <h1>Admin Page</h1>
    <table>
      <thead>
      <tr>
        <th>Room</th>
        <th>Users</th>
        <th>Add User</th>
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
        <td>
          <input v-model="newUserNames[room.id]" placeholder="Enter username" />
          <button @click="addUserToRoom(room.id)">Add User</button>
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
                this.rooms.forEach(room => {
                  this.$set(this.newUserNames, room.id, '');
                });
              });
            });
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