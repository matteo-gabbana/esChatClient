package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

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
            System.err.println("### ID host non trovato... ###");
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

            System.out.println(ANSI_BLUE + "\n### Chiusura connessione in corso... ###" + ANSI_RESET);
            ricezione.interrupt();

        } catch (IOException e) {
            System.err.println("### Impossibile contattare il server! ###");
        }

        System.out.println(ANSI_BLUE + "### Connessione chiusa con successo! ###" + ANSI_RESET);
        System.exit(0);
    }

}
