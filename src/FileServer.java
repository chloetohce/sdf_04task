
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileServer {
    public static void main(String[] args) throws IOException {
        String dbStr = "db";
        int port = 3000;
        if (args.length == 2) {
            port = Integer.parseInt(args[0]);
            dbStr = args[1];
        }
        File db = new File(dbStr);
        if (!db.exists())
            db.mkdir();
        
        // Thread pool
        ExecutorService thrPool = Executors.newFixedThreadPool(2);
        
        // Server 
        ServerSocket server = new ServerSocket(port);

        while (true) { 
            Socket conn = server.accept();
            ConnectionHandler handler = new ConnectionHandler(conn, db);
            thrPool.submit(handler);
        }
    }
}