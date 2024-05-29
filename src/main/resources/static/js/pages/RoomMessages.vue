<template>
    <div class = "container">
        <h1 class="room-title, header-label">Сообщения в канале {{ $route.params.id }}</h1>

        <div class="messages-container" id="scroll">
            <div v-for="message in messages" :key="message.id">
                <div class="message-container" @click="showModal(message)">
                    <div class = "message-author" >
                      <span v-if="message.user != null"> {{ message.user.name }} </span>
                      <span v-else> Удалённый аккаунт </span>
                    </div>
                    <span class = "message-content">{{ message.payload }}</span>
                    <div class = "timestamp">{{ message.sendAt }}</div>
                </div>
            </div>
        </div>
        <div class = "send-form">
            <form @submit.prevent="sendMessage" v-if="canWrite">
                <input v-model="newMessage" placeholder="Введите сообщение" required class = "input-message"/>
                <button type="submit" class = "send-button">Отправить</button>
            </form>
            <h2 v-else class="no-write">Вы не можете писать в этот канал</h2>
        </div>

        <modal-window ref="editModal">
            <template v-slot:body>
                <div class = "edit-container">
                       <input v-model="selectMessage.payload" required class = "edit-input"/>
                       <div class = "edit-buttons-container">
                           <button type="submit" class="edit-button" @click="updateMessage">Редактировать</button>
                           <button type="submit" class="edit-button" @click="deleteMessage">Удалить</button>
                       </div>
                </div>
            </template>
        </modal-window>
    </div>
</template>

<script>
    import socket from '../util/socket'
    import ModalWindow from '../components/ModalWindow.vue'

     export default {
            props: [
               'frontendData',
               'roomId'
            ],
            components: {
               ModalWindow
            },
            data() {
                return {
                    messages: [],
                    newMessage: '',
                    selectMessage: '',
                    isMessagesNotEnd: false,
                    scroll: null,
                    canWrite: false
                };
            },
            mounted() {
                console.log('Mounted with roomId:', this.$route.params.roomId);
                this.scroll = document.getElementById('scroll');
                this.fetchAuthority();
                this.scrollListener();
                this.fetchMessages();
                socket.subscribe('/room/' + this.$route.params.roomId + '/messages', m => this.handler(JSON.parse(JSON.parse(m.body))));
            },
            methods: {
                showModal(message) {
                    if ((message.user.name !== frontendData.username || !this.canWrite) && !frontendData.isAdmin) {
                        return;
                    }

                    this.selectMessage = message;
                    this.$refs.editModal.show = true
                },
                hideModal() {
                    this.$refs.editModal.show = false;
                },
                handler(event) {
                    console.log(event);

                    const payload = event.payload;

                    switch(event.eventType) {
                        case 'CREATE':
                            this.messages.push(payload);
                            setTimeout(() => this.scrollDown(), 100);
                            break;
                        case 'UPDATE':
                            this.messages.find(m => m.id === payload.id).payload = payload.payload;
                            break;
                        case 'DELETE':
                            this.messages = this.messages.filter(m => m.id !== payload.id);
                            break;
                    }
                },
                scrollDown() {
                    this.scroll.scrollTop = this.scroll.scrollHeight;
                },
                scrollUp() {
                    this.scroll.scrollTop = 1;
                },
                scrollListener() {
                    this.scroll.addEventListener('scroll', e => {
                        if (this.scroll.scrollTop == 0 && !this.isMessagesNotEnd) {
                            this.lazyFetchMessages();
                        }
                    });
                },
                fetchAuthority() {
                    if (this.$route.params.roomId) {
                        fetch(`/api/user/authority/${this.$route.params.roomId}`, {
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
                            this.canWrite = data.canWrite;
                        })
                        .catch(error => {
                            console.error('Error fetching messages:', error);
                        });
                    } else {
                        console.error('Room ID is undefined');
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
                            this.isMessagesNotEnd = data.length == 0;
                            data.forEach(el => this.messages.unshift(el));
                            this.scrollUp();
                        })
                        .catch(error => {
                            console.error('Error fetching messages:', error);
                        });
                    } else {
                        console.error('Room ID is undefined');
                    }
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
                    if (this.newMessage.replace(/^\s+|\s+$/g, '').length < 1) {
                        return;
                    }

                    fetch(`/api/messages/send/${this.$route.params.roomId}`, {
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
                updateMessage() {
                    this.hideModal();
                    fetch(`/api/messages/edit/${this.selectMessage.id}`, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ "id": this.selectMessage.id, "payload": this.selectMessage.payload })
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Network response was not ok');
                        }

                        return response.text();
                    })
                    .catch(error => {
                        console.error('Error updating message:', error);
                    });
                },
                deleteMessage() {
                    this.hideModal();
                    fetch(`/api/messages/delete/${this.selectMessage.id}`, {
                        method: 'DELETE',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ "id": this.selectMessage.id })
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
        border: 2px solid black;
        font-size: 24px;
    }
    .messages-container {
        overflow-y: auto;
        width: 100%;
        height: 85%;
        border: 2px solid black;
        border-radius: 16px;
    }
    .messages-container::-webkit-scrollbar {
      width: 0;
    }
    .message-container {
        width: 96%;
        margin: 2%;
        padding: 1% 2% 2% 2%;
        border-radius: 16px;
        border: 2px solid black;;
    }
    .message-content {
        word-wrap: break-word;
        font-size: 24px;
    }
    .message-author {
        font-size: 20px;
        text-decoration: underline;
    }
    .timestamp {
        font-size: 12px;
        text-align: right;
    }
    .edit-container {
        width: 40vw;
        border-radius: 16px;
        padding: 8px;
        border: 4px solid black;
        background: white;
        text-align: center;
    }
    .edit-input {
         width: 90%;
         height: 4vh;
         border: 2px solid black;
         border-radius: 16px;
         padding: 1vh;
         font-size: 24px;
    }
    .edit-button {
         border-radius: 16px;
         border: 2px solid black;
         font-size: 24px;
         padding: 4px;
    }
    .edit-buttons-container {
        margin-top: 1vh;
    }
    .no-write {
        text-align: center;
    }
</style>