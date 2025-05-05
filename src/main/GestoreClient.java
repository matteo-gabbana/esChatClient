package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GestoreClient extends Thread {

    private Socket client;
    private GestoreClient[] clients;
    private Scanner input;
    private PrintWriter output;
    private int codice;

    public GestoreClient(Socket connessione, GestoreClient[] clients) {

        this.client = connessione;
        this.clients = clients;
        try {
            input = new Scanner(client.getInputStream());
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0; i<2; i++) {
            if (clients[i] == null) {
                codice = i;
                break;
            }
        }

        start();
    }

    public void run() {

        output.println("### Premi Invio per iniziare la chat ###");
        output.println("### Sei connesso - Scrivi \"STOP\" per uscire ###");

        String messaggioRicevuto = "";
        boolean primaVolta = true;
        do {

            if (!primaVolta) {

                while (clients[0] == null || clients[1] == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                messaggioRicevuto = input.nextLine();
//            output.println("ECHO: " + messaggioRicevuto);

                int codiceDestinatario;
                if (codice == 0) {
                    codiceDestinatario = 1;
                } else {
                    codiceDestinatario = 0;
                }
                GestoreClient destinatario = clients[codiceDestinatario];

                destinatario.inviaMessaggio("[" + codice + "] >> " + messaggioRicevuto);

            } else {

                primaVolta = false;

            }

        } while (!messaggioRicevuto.equals("STOP"));

        try {
            System.out.println("\n### Chiusura connessione " + codice + " in corso... ###");
            client.close();
            System.out.println("### Connessione chiusa con successo! ###\n");
        } catch (IOException e) {
            System.out.println("### Impossibile chiudere la connessione! ###");
        }
    }

    private void inviaMessaggio(String messaggio) {
        output.println(messaggio);
    }

}
