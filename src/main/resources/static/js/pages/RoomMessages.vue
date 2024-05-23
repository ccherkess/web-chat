<template>
    <div class = "container">
        <h1 class="room-title, header-label">Сообщения в канале {{ $route.params.id }}</h1>

        <div class="messages-container" id="div">
            <div v-for="message in messages" :key="message.id">
                <div class="message-container">
                    <div class = "message-author">{{ message.user.name }}</div>
                    <span class = "message-content">{{ message.payload }}</span>
                    <div class = "timestamp">{{ message.sendAt }}</div>
                </div>
            </div>
            <div id="scroll">
                <h2>Все сообщения прочитаны</h2>
            </div>
        </div>

        <div class = "send-form">
            <form @submit.prevent="sendMessage">
                <input v-model="newMessage" placeholder="Введите сообщение" required class = "input-message"/>
                <button type="submit" class = "send-button">Отправить</button>
            </form>
        </div>
    </div>
</template>

<script>
    import socket from '../util/socket'

     export default {
            props: ['roomId'],
            data() {
                return {
                    messages: [],
                    newMessage: '',
                    editingMessage: null,
                    editedPayload: '',
                    isMessagesNotEnd: true,
                    currentUser: {}
                };
            },
            mounted() {
                console.log('Mounted with roomId:', this.$route.params.roomId);
                this.fetchMessages();

                document.getElementById('div').addEventListener('scroll', e => {
                    if (document.getElementById('div').scrollTop == 0 && this.isMessagesNotEnd) {
                        this.lazyFetchMessages();
                    }
                });

                socket.subscribe('/room/' + this.$route.params.roomId + '/messages', message => this.handler(JSON.parse(JSON.parse(message.body))));
            },
            methods: {
                handler(message) {
                    console.log(message);

                    switch(message.eventType) {
                        case 'CREATE':
                            this.messages.push(message.message);
                            this.scrollDown();
                            break;
                        case 'DELETE':
                            this.messages = this.messages.filter(m => m.id !== message.message.id);
                            break;
                    }
                },
                lazyFetchMessages() {
                    if (this.$route.params.roomId) {
                        fetch(`/api/messages/${this.$route.params.roomId}/25/` + this.messages[0].id , {
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
                            console.log(data);
                            this.isMessagesNotEnd = data.length > 0;
                            data.forEach(el => this.messages.unshift(el));
                            document.getElementById('div').scrollTop = 10;
                        })
                        .catch(error => {
                            console.error('Error fetching messages:', error);
                        });
                    } else {
                        console.error('Room ID is undefined');
                    }
                },
                scrollDown() {
                    var myElement = document.getElementById('scroll');
                    var topPos = myElement.offsetDown;
                    document.getElementById('div').scrollTop = document.getElementById('div').scrollHeight + 100;
                },
                fetchMessages() {
                    if (this.$route.params.roomId) {
                        fetch(`/api/messages/${this.$route.params.roomId}/25`, {
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
                            data.forEach(el => this.messages.unshift(el));
                        }).then(unused => this.scrollDown())
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
                    .catch(error => {
                        console.error('Error deleting message:', error);
                    });
                }
            }
        }
</script>

<style>
    .container {
        margin: 5% auto;
        width: 50vw;
        border: 2px solid black;
        border-radius: 16px;
        height: 75vh;
        position:relative;
    }
    .header-label {
        margin: 1% auto;
        text-align: center;
    }
    .send-form {
        height: 4vh;
        width: 100%;
        position: absolute;
        bottom: 3vh;
    }
    .input-message {
        width: 75%;
        height: 5vh;
        border: 2px solid black;
        border-radius: 16px;
        padding: 1vh;
        font-size: 24px;
    }
    .send-button {
        width: 20%;
        height: 5vh;
        border-radius: 16px;
        font-size: 24px;
        background: rgba(40, 40, 255, 0.61);
    }
    .messages-container {
        overflow: auto;
        width: 100%;
        height: 85%;
        border: 2px solid black;
        border-radius: 16px;
    }
    .messages-container::-webkit-scrollbar {
      width: 0;
    }
    .message-container {
        width: 50%;
        margin: 2%;
        padding: 1% 2% 2% 2%;
        border-radius: 16px;
        background: rgba(40, 40, 255, 0.61);
    }
    .message-content {
        word-wrap: break-word;
        font-size: 24px;
    }
    .message-author {
        font-size: 20px;
        text-decoration: underline;
    }
    .message-not-available {

    }
    .timestamp {
        font-size: 12px;
        text-align: right;
    }
    #scroll {
        margin: 5vh auto;
        text-align: center;
    }
</style>