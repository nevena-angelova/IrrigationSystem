import {Injectable} from '@angular/core';
import {Client, Message} from '@stomp/stompjs';
import {Observable, Subject} from 'rxjs';
import {environment} from '../../environments/environment.development';
import SockJS from 'sockjs-client';
import {Report} from '../model/report.model';

@Injectable({
    providedIn: 'root'
})
export class WebSocketService {
    private readonly stompClient: Client;
    private messageSubject = new Subject<Report>();
    private socketUrl = `${environment.apiUrl}ws`;

    constructor() {
        this.stompClient = new Client({
            webSocketFactory: () => new SockJS(this.socketUrl),
            reconnectDelay: 5000,
            debug: (str) => console.log(str)
        });

        this.stompClient.onConnect = () => {
            // Subscribe to the topic Spring is broadcasting on
            this.stompClient.subscribe('/notification/report', (message: Message) => {
                const report: Report = JSON.parse(message.body);
                this.messageSubject.next(report);
            });
        };

        this.stompClient.onStompError = (frame) => {
            console.error('STOMP error:', frame.headers['message']);
            console.error('Details:', frame.body);
        };

        this.stompClient.activate();
    }

    // Send a message to the backend (Spring listens on /app/...)
    sendMessage(destination: string, message: any) {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.publish({
                destination: `/app/${destination}`,
                body: JSON.stringify(message)
            });
        }
    }

    // Observable stream of incoming messages
    getMessages(): Observable<Report> {
        return this.messageSubject.asObservable();
    }

    // Close the STOMP connection
    closeConnection() {
        if (this.stompClient && this.stompClient.active) {
            this.stompClient.deactivate();
        }
    }
}
