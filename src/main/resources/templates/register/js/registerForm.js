Vue.component('register-form', {
    data: function() {
        return {
            username: '',
            password: ''
        };
    },
    template: `
        <div>
            <h2>Register</h2>
            <form @submit.prevent="register">
                <div class="mb-3">
                    <label for="username">Username: </label>
                    <input type="text" v-model="username" id="username"/><br/>
                </div>
                <div class="mb-3">
                    <label for="password">Password: </label>
                    <input type="password" v-model="password" id="password"/><br/>
                </div>
                <input type="submit" value="Register"/>
            </form>
        </div>
    `,
    methods: {
        register() {
            fetch('/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    username: this.username,
                    password: this.password
                })
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                console.log('User registered:', data);
                window.location.href = '/login';
            })
            .catch(error => {
                console.error('Error registering user:', error);
            });
        }
    }
});

const app = new Vue({
    el: '#app',
    template: '<register-form />'
});
