<template>
    <div class="container">
        <h1 class="header-label">Администрирование пользователей</h1>
        <div class="users-container" id="scroll">
            <div v-for="user in users" class = "user-item">
                <div class="user-info">
                    <h2>ID: {{user.id}}</h2>
                    <h2>Name: {{user.name}}</h2>
                </div>
                <button v-if="user.name !== 'admin'" class="button" @click="deleteUser(user)">Удалить</button>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        data() {
            return {
              users: [],
              scroll: null,
              isUsersNotEnd: false,
              canFetchUsers: true
            };
        },
        mounted() {
            this.scroll = document.getElementById('scroll');
            this.fetchUsers();
            this.scrollListener();
        },
        methods: {
            fetchUsers() {
              fetch('/api/user/all/25', {
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
              .then(data => {this.users = data;})
              .catch(error => {
                  console.error('Error fetch room:', error);
              });
            },
            lazyFetchUsers() {
              fetch(`/api/user/all/25/${this.users[this.users.length - 1].id}`, {
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
                this.isUsersNotEnd = data.length == 0;
                data.forEach(el => this.users.push(el))
              })
              .catch(error => {
                  console.error('Error fetch room:', error);
              });
            },
            scrollListener() {
                this.scroll.addEventListener('scroll', e => {
                    if ((this.scroll.scrollTop + this.scroll.offsetHeight > this.scroll.scrollHeight) && this.canFetchUsers && !this.isUsersNotEnd) {
                        this.lazyFetchUsers();
                        this.canFetchUsers = false;
                        this.scroll.scrollTop -= 10;
                        setTimeout(() => {this.canFetchUsers = true}, 100);
                    }
                });
            },
            deleteUser(user) {
                console.log(user);
                 fetch(`/api/user/${user.name}`, {
                    method: 'DELETE',
                    headers: {
                      'Content-Type': 'application/json'
                    }
                 })
                 .then(response => {
                    if (!response.ok) {
                      throw new Error('Network response was not ok');
                    }
                    return response.text();
                 })
                  .then(data => {
                        const index = this.users.findIndex(u => u.id === user.id);
                        console.log(index)
                        this.users.splice(index, 1);

                        console.log(data)

                        if (this.users.length < 25) this.fetchUsers();
                  })
                  .catch(error => {
                      console.error('Error fetch room:', error);
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
        height: 75vh;
        position:relative;
    }
    .header-label {
        margin-bottom: 2vh;
    }
    .users-container {
         overflow-y: auto;
         margin-top: 2vh;
         width: 100%;
         height: 90%;
         padding: 2%;;
         border-radius: 16px;
         border: 2px solid black;;
     }
     .users-container::-webkit-scrollbar {
         width: 0;
     }
     .user-item {
        border: 2px solid black;
        border-radius: 16px;
        padding: 8px;
        margin: 1%;
        display: flex;
     }
    .button {
        border-radius: 16px;
        border: 2px solid black;
        font-size: 24px;
        padding: 6px;
    }
    .user-info {
        width: 100%;
    }
</style>
