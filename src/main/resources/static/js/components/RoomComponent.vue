<template>
    <div class="room-container">
        <div class="room-content">
             <div class="room-info">
                 <h2>Название: {{room.name}}</h2>
                 <h2>ID: {{room.id}}</h2>
                 <h2>Количество пользователей: {{room.chatUsers.length}}</h2>
             </div>
             <div class="room-actions">
                 <button class="button" @click="$emit('deleteRoom', room)">Удалить</button>
                 <button class="button" @click="showUsers">Пользователи</button>
             </div>
        </div>
         <div class="users-container" ref="users">
            <button class="add-user-button" @click="showAddUserModal">Добавить пользователя</button>
            <h2 v-if="room.chatUsers.length > 0">Список пользователей:</h2>
            <ul>
                <li class="user-item" v-for="user in room.chatUsers">
                    <div class="user-info">
                        <h2>Имя: {{ user.name }}</h2>
                        <h2>ID: {{ user.id }}</h2>
                    </div>
                    <button class="button" @click="deleteUserFromRoom(user)">Удалить из комнаты</button>
                    <button class="button" v-if="user.authorities.length != 0" @click="enableWrite(user, false)">Запретить писать</button>
                    <button class="button" v-else @click="enableWrite(user, true)">Разрешить писать</button>
                </li>
            </ul>
         </div>

          <modal-window ref="addUserModal">
             <template v-slot:body>
                <div class="add-user-container">
                    <h2>Выберите пользователя</h2>
                    <div class="add-user-item" v-for="user in usersNotInRoom" @click="addUserToRoom(user)">
                        <h2>Имя: {{ user.name }}</h2>
                        <h2>ID: {{ user.id }}</h2>
                    </div>
                </div>
             </template>
         </modal-window>
    </div>
</template>

<script>
    import ModalWindow from './ModalWindow.vue'

    export default {
        components: {
           ModalWindow
        },
        props: [
           "room"
        ],
        data() {
          return {
            usersNotInRoom: []
          }
        },
        methods: {
            showModal() {
                this.$refs.addUserModal.show = true
            },
            hideModal() {
                this.$refs.addUserModal.show = false;
                this.userNotInRoom = [];
            },
            editName(name) {
                fetch('/api/room/edit', {
                    method: 'PUT',
                    headers: {
                      'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id: room.id, name: name})
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
                    console.error('Error edit name room:', error);
                });
            },
            showUsers() {
                var users = this.$refs.users;

                if (users.style.display == "block") {
                    users.style.display = "none";
                } else {
                    users.style.display = "block";
                }
            },
            showAddUserModal() {
                this.showModal();
                this.fetchUsersNotInRoom();
            },
            addUserToRoom(user) {
                this.hideModal();
                fetch(`/api/room/user/add/${user.name}`, {
                    method: 'PUT',
                    headers: {
                      'Content-Type': 'application/json'
                },
                    body: JSON.stringify({ id: this.room.id })
                })
                .then(response => {
                    if (!response.ok) {
                         throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(unused => {
                    this.room.chatUsers.push(user);
                })
                .catch(error => {
                    console.error('Error edit name room:', error);
                });
            },
            deleteUserFromRoom(user) {
                 fetch(`/api/room/user/delete/${user.name}`, {
                    method: 'PUT',
                    headers: {
                      'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ id: this.room.id })
                 })
                 .then(response => {
                    if (!response.ok) {
                         throw new Error('Network response was not ok');
                    }
                    return response.text();
                 })
                 .then(unused => {
                    const index = this.room.chatUsers.findIndex(u => u.name === user.name);
                    this.room.chatUsers.splice(index, 1);
                 })
                 .catch(error => {
                    console.error('Error edit name room:', error);
                 });
            },
            fetchUsersNotInRoom() {
                fetch(`/api/user/room/not/${this.room.id}`, {
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
                .then(users => {
                    this.usersNotInRoom = users;
                })
                .catch(error => {
                    console.error('Error edit name room:', error);
                });
            },
            enableWrite(user, enable) {
                fetch(`/api/room/user/${enable ? 'enable' : 'disable'}/write/${user.name}`, {
                    method: 'PUT',
                    headers: {
                      'Content-Type': 'application/json'
                },
                    body: JSON.stringify({ id: this.room.id })
                })
                .then(response => {
                    if (!response.ok) {
                         throw new Error('Network response was not ok');
                    }
                    return response.text();
                })
                .then(unused => {
                    user.authorities = enable ? [0] : [];
                })
                .catch(error => {
                    console.error('Error edit name room:', error);
               })
            }
        }
    }
</script>

<style scoped>
    .room-container {
        width: 100%;
        margin: 2% 0;
        padding: 2%;
        border-radius: 16px;
        border: 2px solid black;
    }
    .room-content {
        display: flex;
    }
    .room-info {
        width: 100%;
    }
    .room-actions {
        display: flex;
        flex-direction: column;
        row-gap: 1vh;
    }
    .button {
         border-radius: 16px;
         border: 2px solid black;
         font-size: 16px;
         padding: 6px;
         margin-right: 1%;
    }
    .users-container {
        display: none;
    }
    .user-item {
        margin: 1vh;
        display: flex;
        vertical-align: baseline;
    }
    .user-info {
        width: 70%;
        display: table;
    }
    .add-user-button {
        width: 100%;
        margin: 2% 0;
        border-radius: 16px;
        border: 2px solid black;
        font-size: 24px;
        padding: 6px;
    }
    .add-user-container {
        overflow-y: auto;
        width: 40vw;
        height: 50vh;
        border-radius: 16px;
        padding: 8px;
        border: 4px solid black;
        background: white;
    }
    .add-user-item {
        width: 100%;
        margin: 2% 0;
        border: 2px solid black;
        font-size: 24px;
        padding: 6px;
    }
</style>
