<template>
    <div class="room-messages">
        <h2 class="room-title">Messages in Room {{ $route.params.roomId }}</h2>
        <div class="messages-container">
            <ul v-if="messages.length > 0">
                <li v-for="message in messages" :key="message.id">
                    <div class="message-content">
                        <span class="message-author"> {{ currentUser.name }}</span>
                        <span v-if="editingMessage !== message.id">{{ message.payload }}</span>
                        <input v-else v-model="editedPayload" @keyup.enter="updateMessage(message)" />
                            <div class="message-actions">
                                <button v-if="editingMessage !== message.id" @click="editMessage(message)">Edit</button>
                                <button v-else @click="updateMessage(message)">Save</button>
                                <button @click="deleteMessage(message)">Delete</button>
                            </div>
                    </div>
                </li>
            </ul>
            <p v-else>No messages available</p>
        </div>
        <form @submit.prevent="sendMessage">
            <input v-model="newMessage" placeholder="Type a message" required />
            <button type="submit">Send</button>
        </form>
    </div>
</template>

<script>
     export default {
            props: ['roomId'],
            data: function() {
                return {
                    messages: [],
                    newMessage: '',
                    editingMessage: null,
                    editedPayload: '',
                    currentUser: {}
                };
            },
            mounted() {
                console.log('Mounted with roomId:', this.$route.params.roomId);
                this.fetchMessages();
                this.fetchUser();
            },
            methods: {
                fetchUser() {
                    fetch('/api/user/current',  {
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
                        this.currentUser = data;
                    })
                    .catch(error => {
                        console.error('Error fetching current user:', error);
                    });
                },
                fetchMessages() {
                    if (this.$route.params.roomId) {
                        fetch(`/api/messages/${this.$route.params.roomId}/0`, {
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
                            this.messages = data;
                        })
                        .catch(error => {
                            console.error('Error fetching messages:', error);
                        });
                    } else {
                        console.error('Room ID is undefined');
                    }
                },
                sendMessage() {
                    fetch('/api/messages/send', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ "roomId": this.$route.params.roomId, "payload": this.newMessage })
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        this.messages.push(data);
                        this.newMessage = '';
                    })
                    .catch(error => {
                        console.error('Error sending message:', error);
                    });
                },
                editMessage(message) {
                    this.editingMessage = message.id;
                    this.editedPayload = message.payload;
                },
                updateMessage(message) {
                    fetch('/api/messages/edit', {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ "id": message.id, "payload": this.editedPayload })
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.json();
                    })
                    .then(data => {
                        const index = this.messages.findIndex(m => m.id === message.id);
                        if (index !== -1) {
                            this.messages.splice(index, 1, data);
                        }
                        this.editingMessage = null;
                        this.editedPayload = '';
                    })
                    .catch(error => {
                        console.error('Error updating message:', error);
                    });
                },
                deleteMessage(message) {
                    fetch('/api/messages/delete', {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ "id": message.id })
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }
                        return response.text();
                    })
                    .then(data => {
                        this.messages = this.messages.filter(m => m.id !== message.id);
                    })
                    .catch(error => {
                        console.error('Error deleting message:', error);
                    });
                }
            }
        }
</script>

<style>

</style>