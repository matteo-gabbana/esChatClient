package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private static InetAddress host;
    private static final int PORTA = 5959;

    public static void main(String[] args) {

        hostSetup();
        contattaServer();
    }

    private static void hostSetup() {

        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.out.println("### ID host non trovato... ###");
            System.exit(1);
        }
    }

    private static void contattaServer() {

        try {

            Socket connessione = new Socket(host, PORTA);
            Scanner input = new Scanner(connessione.getInputStream());
            PrintWriter output = new PrintWriter(connessione.getOutputStream(), true);
            Scanner tastiera = new Scanner(System.in);

            Ricezione ricezione = new Ricezione(input);

            while (true) {

                String messaggio = tastiera.nextLine();
                output.println(messaggio);

                if (messaggio.equals("STOP")) {
                    break;
                }
            }

            System.out.println("\n### Chiusura connessione in corso... ###");
            ricezione.interrupt();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("### Connessione chiusa con successo! ###");
        System.exit(0);
    }


}
