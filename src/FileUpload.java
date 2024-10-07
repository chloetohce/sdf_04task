
import java.io.BufferedReader;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FileUpload {
    // Client

    public static void main(String[] args) throws IOException {
        int port = 5000;
        if (args.length > 0) 
            port = Integer.parseInt(args[0]);

        Socket socket = new Socket("localhost", port);
        Console console = System.console();

        String fileName = console.readLine("Enter file to upload to server: ");

        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

        File file = new File(fileName);
        long fileSize = file.length();
        String fileMsg = "";

        // Reading file content to send
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            fileMsg = fileMsg + line + "\n";
        }
        br.close();
        fr.close();

        // Sending data to server
        dos.writeUTF(fileName);
        dos.writeLong(fileSize);
        dos.write(fileMsg.getBytes());
        dos.flush();
        
        socket.close();
        
    }
}
