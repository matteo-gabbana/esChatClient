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
    private boolean mioTurno = false;

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

        if (codice == 0) {
            mioTurno = true;
        }

        start();
    }

    public void run() {

        output.println("### Sei connesso - Scrivi \"STOP\" per uscire ###");

        while (clients[0] == null || clients[1] == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (mioTurno) {
            output.println("### E' il tuo turno, scrivi un messaggio ###");
        } else {
            output.println("### Attendi il messaggio dell'altro client... ###");
        }

        String messaggioRicevuto = "";
        do {

            if (mioTurno) {

                int codiceDestinatario;
                if (codice == 0) {
                    codiceDestinatario = 1;
                } else {
                    codiceDestinatario = 0;
                }

                output.println("Client [" + codiceDestinatario + "] (tu) >> ");
                messaggioRicevuto = input.nextLine();

                GestoreClient destinatario = clients[codiceDestinatario];
                destinatario.inviaMessaggio(messaggioRicevuto);
                this.mioTurno = false;
                destinatario.setTurno(true);

            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } while (!messaggioRicevuto.equals("STOP"));

        try {
            System.out.println("### Chiusura connessione " + codice + " in corso... ###");
            client.close();
            System.out.println("### Connessione chiusa con successo! ###\n");
        } catch (IOException e) {
            System.out.println("### Impossibile chiudere la connessione! ###");
        }
    }

    private void inviaMessaggio(String messaggio) {
        output.println("Client [" + codice + "] << " + messaggio);
    }

    private void setTurno(boolean valore) {
        mioTurno = valore;
    }

}
