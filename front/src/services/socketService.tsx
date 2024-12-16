import SockJS from 'sockjs-client';
import { Client } from "@stomp/stompjs";

export const createWebSocketClient = (setMessages: any) => {
    const client = new Client({
        webSocketFactory: () => new SockJS("http://localhost:4040/api/v1/user/chat"),
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