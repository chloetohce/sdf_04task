
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
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
            // System.out.println(fileName);
            Long fileSize = dis.readLong();
            // System.out.println(fileSize);
            byte[] fileMsg = new byte[fileSize.intValue()]; //Data is read into here. 
            int read = dis.read(fileMsg);
            // System.out.println(read);

            

            // If file does not exist in db, create
            File f = new File(db.getPath() + File.separator + fileName);
            if (!f.exists()) {
                f.createNewFile();
            }

            // reading the message and copying to db
            FileWriter fw = new FileWriter(f,false);
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write(new String(fileMsg));

            bw.flush();
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
