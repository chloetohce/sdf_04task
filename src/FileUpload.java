
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class FileUpload {
    // Client

    public static void main(String[] args) throws IOException {
        int port = 3000;
        if (args.length > 0) 
            port = Integer.parseInt(args[0]);

        Socket socket = new Socket("localhost", port);

        //String fileName = console.readLine("Enter file to upload to server: "); 
        // Above doesn't work with Windows because of backslash character as File.separator
        // Java strings automatically reads it as a escape character, and deletes it from string, which affected downstream code. 
        // Found that scanner doesn't do this. 
        System.out.print("Enter file to upload: ");
        Scanner s = new Scanner(System.in);
        String fileName = s.nextLine();

        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);
        /* 
         * Insert check if file exists and is file.
         *  if (!(f.exists() && f.isFile())) {
         *      System.err.println(f + "is not a file")
         *      System.exit(-1); 
         * }
         */
        File file = new File(fileName);
        Long fileSize = file.length();

        // Reading file content to send
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);

        // Sending data to server
        dos.writeUTF(fileName);
        dos.writeLong(fileSize);

        byte[] buff = new byte[4 * 1024]; // Creating a 4k buffer; some files can be very big. 
        int bytesRead = 0;
        while ((bytesRead = bis.read(buff)) > 0) { // fill buffer, and tell me how much is read. Will become -1 if there is no more to read
            dos.write(buff, 0, bytesRead); //write to socket, starting from 0 to whatever we have read. 
        }

        s.close();
        dos.flush();
        bis.close();        
        socket.close();
        
    }
}
