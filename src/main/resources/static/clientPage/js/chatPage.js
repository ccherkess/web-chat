/* NOT IN USE */



Vue.component('room-messages', {
    props: ['roomId'],
    data: function() {
        return {
            messages: [],
            newMessage: ''
        };
    },
    template: `
        <div class="room-messages">
            <h2>Messages in Room {{ $route.params.roomId }}</h2>
            <ul v-if="messages.length > 0">
                <li v-for="message in messages" :key="message.id">{{ message.payload }}</li>
            </ul>
            <p v-else>No messages available</p>
            <form @submit.prevent="sendMessage">
                <input v-model="newMessage" placeholder="Type a message" required />
                <button type="submit">Send</button>
            </form>
        </div>
    `,
     mounted() {
            console.log('Mounted with roomId:', this.$route.params.roomId);
            this.fetchMessages();
     },
     methods: {
     fetchMessages() {
         if (this.$route.params.roomId)  {
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
            fetch(`/api/messages/send`, {
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
        }
    }
});

const RoomMessages = {props: ['roomId'], template: '<room-messages />'};