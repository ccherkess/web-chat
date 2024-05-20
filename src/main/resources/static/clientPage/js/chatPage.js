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
            <h2>Messages in Room {{ roomId }}</h2>
            <ul v-if="messages.length > 0">
                <li v-for="message in messages" :key="message.id">{{ message.text }}</li>
            </ul>
            <p v-else>No messages available</p>
            <form @submit.prevent="sendMessage">
                <input v-model="newMessage" placeholder="Type a message" required />
                <button type="submit">Send</button>
            </form>
        </div>
    `,
     mounted() {
            this.fetchMessages();
     },
     methods: {
     fetchMessages() {
        fetch(`/api/messages/${this.roomId}/`, {
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
        },
        sendMessage() {
            fetch(`/api/messages/${this.roomId}/send`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                 body: JSON.stringify({ "payload": this.newMessage })
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

const RoomMessages = {template: '<room-messages />'};