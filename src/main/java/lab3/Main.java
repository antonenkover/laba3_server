package lab3;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Main {
    private static final Logger LOG = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        doTheThing();
    }

    private static void doTheThing() {
        System.out.println("Hello this is laba 3.");
        Socket clientSocket = null;
        BufferedReader reader;
        BufferedReader in = null;
        BufferedWriter out = null;

        while (true) {
            try {
                try {
                    clientSocket = new Socket("localhost", 8080);
                    reader = new BufferedReader(new InputStreamReader(System.in));
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    System.out.println("Input your query:");
                    String word = reader.readLine();

                    out.write(word + "\n");
                    out.flush();

                    if (word.equals("exit")) {
                        break;
                    }

                    StringBuilder response = new StringBuilder();
                    String serverWord = in.readLine();
                    while (!serverWord.isEmpty() && !serverWord.contains("null")) {
                        response.append(serverWord);
                        serverWord = in.readLine();
                        serverWord = "\n" + serverWord;
                    }
                    System.out.println(response);
                } finally {
                    assert clientSocket != null;
                    clientSocket.close();
                    assert in != null;
                    in.close();
                    assert out != null;
                    out.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }
    }
}
