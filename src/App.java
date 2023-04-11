import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    private static final String ipServer = "localhost";
    private static final int portServer = 1234;

    private static Socket socket;
    private static PrintWriter printWriter;

    /**
     * Point d'entrée de l'application
     * 
     * @param args Arguments de la ligne de commande
     */
    public static void main(String[] args) {
        String pseudo = inputUsername();
        connectToServer(pseudo);

        // Créer un objet Runnable pour lire les messages du serveur
        Runnable readMessages = () -> {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = bufferedReader.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        // Démarrer un thread pour lire les messages du serveur
        new Thread(readMessages).start();

        // Créer un objet Runnable pour envoyer les messages de l'utilisateur au serveur
        Runnable sendMessage = () -> {
            try {
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                String message;
                while ((message = userInput.readLine()) != null) {
                    printWriter.println(message);
                    printWriter.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        // Démarrer un thread pour envoyer les messages de l'utilisateur au serveur
        new Thread(sendMessage).start();
    }

    private static String inputUsername() {
        System.out.println("Veuillez entrer votre pseudo :");
        String username = Utils.getUserInput();
        return username;
    }

    private static void connectToServer(String username) {
        try {
            socket = new Socket(ipServer, portServer);
            System.out.println("Connexion établie avec le serveur");

            printWriter = new PrintWriter(socket.getOutputStream());

            printWriter.println(username);
        } catch (IOException e) {
            System.out.println("Impossible de se connecter au serveur");
            e.printStackTrace();
        }
    }
}
