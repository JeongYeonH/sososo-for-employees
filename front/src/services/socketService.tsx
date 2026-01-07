import SockJS from 'sockjs-client';
import { Client } from "@stomp/stompjs";

export const createWebSocketClient = (setMessages: any) => {

    const hostname = window.location.hostname;
    const API_BASE_URL = `http://${hostname}:4040`;

    const client = new Client({
        webSocketFactory: () => new SockJS(`${API_BASE_URL}:4040/api/v1/user/chat`),
        connectHeaders: {},
        onConnect: () => {
            client.subscribe('/topic/public', (message: any) => {
                setMessages((prevMessages: any) => [...prevMessages, JSON.parse(message.body)]);
            });
        },
        onStompError: (frame: any) => {
            console.error("STOMP error:", frame);
        },
    });

    client.activate();

    return client;
};

export const deactivateWebSocketClient = (client: any) => {
    if (client.connected) {
        client.deactivate();
    }
};