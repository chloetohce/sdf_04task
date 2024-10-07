
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable{
    private final Socket conn;
    private final File db;

    public ConnectionHandler(Socket conn, File db) {
        this.conn = conn;
        this.db = db;
    }
    
    @Override
    public void run() {
        try {
            InputStream is = conn.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            String fTemp = dis.readUTF();
            String fileName = fTemp.substring(fTemp.lastIndexOf(File.separator) + 1);
            Long fileSize = dis.readLong();
            byte[] fileMsg = new byte[fileSize.intValue()]; //Data is read into here. 
            int read = dis.read(fileMsg);

            

            // If file does not exist in db, create
            File f = new File(db.getPath() + File.separator + fileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            // reading the message and copying to db
            FileOutputStream fos = new FileOutputStream(f, false);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            int bytesRead = 0;
            int bytesRecv = 0;
            byte[] buff = new byte[4*1024];
            System.out.println(fileSize);
            while (bytesRecv < fileSize) {
                bytesRead = dis.read(buff);
                bytesRecv += bytesRead;
                bos.write(buff, 0, bytesRead);
                System.out.println("recv: " + bytesRecv);
            }

            bos.flush();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
