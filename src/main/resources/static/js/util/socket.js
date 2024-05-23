import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'

export default {
    stompClient: null,
    connect() {
        const socket = new SockJS('/ws')
        this.stompClient = Stomp.over(socket)
        this.stompClient.connect({}, frame => console.log('Connected: ' + frame))
    },
    subscribe(url, handler) {
        if (this.stompClient !== null) {
            this.stompClient.subscribe(url, handler)
        }
    },
    disconnect() {
        if (stompClient !== null) {
            stompClient.disconnect()
        }
        console.log("Disconnected")
    }
}