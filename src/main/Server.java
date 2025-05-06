package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static ServerSocket server;
    private static final int PORTA = 5959;
    private static int clientConnessi = 0;

    public static void main(String[] args) {

        System.out.println("\n### Apertura porta del server in corso... ###");
        try {
            server = new ServerSocket(PORTA);
        } catch (IOException e) {
            System.out.println("### Impossibile aprire la porta! ###");
            System.exit(1);
        }

        System.out.println("### Porta aperta con successo! ###\n");

        Socket connessione;
        GestoreClient[] clients = new GestoreClient[2];

        System.out.println("### In attesa di 2 client per avviare la chat... ###\n");
        while (clientConnessi != 2) {
            try {
                connessione = server.accept();
                clients[clientConnessi] = new GestoreClient(connessione, clients);
                clientConnessi++;
                System.out.println("### Un client si e' connesso! ###\n");
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        System.out.println("### 2 Client si sono connessi! Chat avviata ###\n");
    }

}
