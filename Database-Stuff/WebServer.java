import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;


public final class WebServer {

    public static void main(String argv[]) throws Exception
    {
        System.out.println("Current workingdir: " + System.getProperty("user.dir"));
        // set the port number:
        final int port = 12345;
        // establish the listen socket:
        ServerSocket listenSock = new ServerSocket(port);
        // process HTTP service requests in an infinite loop:
        while (true) {
            // listen for a TCP connection request:
            Socket clientSock = listenSock.accept();
            // construct an object to process the HTTP request message:
            HttpRequest request = new HttpRequest(clientSock);
            // create a new thread to process the request:
            Thread thread = new Thread(request);
            // start the thread:
            thread.start();
        }
    }
}

final class HttpRequest implements Runnable {

    private final static String EOL = "\r\n";
    private final Socket socket;

    // constructor:
    HttpRequest(Socket socket) throws Exception
    {
        this.socket = socket;
    }

    private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
    {
        // construct a 1K buffer to hold bytes on their way to the socket:
        byte[] buffer = new byte[1024];
        int bytes = 0;
        // copy requested file into the socket’s output stream:
        while ((bytes = fis.read(buffer)) != -1)
            os.write(buffer, 0, bytes);
    }

    private static String contentType(final String fileName)
    {
        if (fileName.endsWith(".htm") || fileName.endsWith(".html"))
            return "text/html";
        else if (fileName.endsWith(".JPEG") || fileName.endsWith(".jpeg")
                || fileName.endsWith(".JPG") || fileName.endsWith(".jpg"))
            return "image/jpeg";
        else if (fileName.endsWith(".GIF") || fileName.endsWith(".gif"))
            return "image/gif";
        else
            return "application/octet-stream";
    }

    // implement the run() method of the Runnable interface:
    public void run()
    {
        try {
            processRequest();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void processRequest() throws Exception
    {
        // get a reference to the socket’s input and output streams:
        InputStream is = socket.getInputStream();
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        // Set up input stream filters.
        /* DecoratorPattern at it best! */
        FilterInputStream filteredFis = new BufferedInputStream(is);
        BufferedReader br = new BufferedReader(new InputStreamReader(filteredFis));
        // get the request line of the HTTP request message:
        String requestLine = br.readLine();
        // display the request line:
        System.out.println();
        System.out.println(requestLine);
        // get and display the header lines:
        String headerLine;
        while ((headerLine = br.readLine()).length() != 0) {
            System.out.println(headerLine);
        }


        // extract the filename from the request line:
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken(); // skip over the method, which should be "GET"
        String fileName = tokens.nextToken();
        // prepend a "." so that file request is within the current directory:
        fileName = "." + fileName;
        if (fileName.contains("../")) {
            fileName = "./index.html";
        } else if (fileName.endsWith("/")) {
            fileName = fileName + "index.html";
        }

        // open the requested file:
        FileInputStream fis = null;
        boolean fileExists = true;
        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        // construct the response message:
        String statusLine; // EOF terminated
        String contentTypeLine; // EOF terminated
        String entityBody = null;
        if (fileExists) {
            statusLine = "HTTP/1.0 200 OK" + EOL;
            contentTypeLine = "Content-Type: " +
                    contentType(fileName) + EOL;
        } else {
            statusLine = "HTTP/1.0 404 Not Found" + EOL;
            contentTypeLine = "Content-Type: text/html" + EOL;
            entityBody = "<html>" +
                    "<head><title>Not Found</title></head>" +
                    "<body>Not Found</body></html>";
        }
        // send the status line:
        os.writeBytes(statusLine);
        // send the content type line:
        os.writeBytes(contentTypeLine);
        // send a blank line to indicate the end of the header lines:
        os.writeBytes(EOL);

        // send the entity body:
        if (fileExists) {
            sendBytes(fis, os);
            fis.close();
        } else {
            os.writeBytes(entityBody);
        }

        os.close();
        is.close();
        br.close();
        socket.close();
    }

}


