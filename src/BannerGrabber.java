import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * BannerGrabber - Retrieves service banners for version detection
 * 
 * Connects to open ports and attempts to grab service banners
 * which often contain version information useful for fingerprinting.
 * 
 * @author Elodie Moisan
 * @version 2.1
 */

public class BannerGrabber {
    
    private static final int BANNER_TIMEOUT = 3000; // 3 seconds
    private static final int MAX_BANNER_LENGTH = 1024; // 1KB max
    
    /**
     * Attempts to grab a banner from a service
     * 
     * @param host Target hostname or IP
     * @param port Port number
     * @return Banner string, or empty string if unable to grab
     */
    public static String grabBanner(String host, int port) {
        try {
            // Connect to the port
            Socket socket = new Socket(host, port);
            socket.setSoTimeout(BANNER_TIMEOUT);
            
            // Try to read banner
            String banner = readBanner(socket, port);
            
            socket.close();
            return banner;
            
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Reads banner from socket based on port/protocol
     * 
     * @param socket Connected socket
     * @param port Port number (to determine protocol)
     * @return Banner string
     */
    private static String readBanner(Socket socket, int port) throws IOException {
        StringBuilder banner = new StringBuilder();
        
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            
            // Send probe based on port
            sendProbe(out, port);
            
            // Read response
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(in)
            );
            
            String line;
            int lines = 0;
            
            while ((line = reader.readLine()) != null && lines < 5) {
                banner.append(line).append(" ");
                lines++;
                
                if (banner.length() > MAX_BANNER_LENGTH) {
                    break;
                }
            }
            
        } catch (SocketTimeoutException e) {
            // Timeout is normal for some services
        }
        
        return banner.toString().trim();
    }
    
    /**
     * Sends appropriate probe based on service port
     * 
     * @param out Output stream
     * @param port Port number
     */
    private static void sendProbe(OutputStream out, int port) throws IOException {
        switch (port) {
            case 80:
            case 8080:
            case 8000:
            case 8888:
                // HTTP probe
                out.write("HEAD / HTTP/1.0\r\n\r\n".getBytes());
                out.flush();
                break;
                
            case 21:
                // FTP - just wait for banner
                break;
                
            case 25:
            case 587:
                // SMTP - send EHLO
                out.write("EHLO grabber\r\n".getBytes());
                out.flush();
                break;
                
            case 110:
                // POP3 - just wait for banner
                break;
                
            case 22:
                // SSH - just wait for banner
                break;
                
            default:
                // Generic probe - just try to read
                break;
        }
    }
    
    /**
     * Extracts version information from banner
     * 
     * @param banner Raw banner string
     * @return Cleaned version info
     */
    public static String extractVersion(String banner) {
        if (banner == null || banner.isEmpty()) {
            return "";
        }
        
        // Common patterns
        if (banner.contains("Server:")) {
            int start = banner.indexOf("Server:") + 7;
            int end = banner.indexOf("\n", start);
            if (end == -1) end = banner.length();
            return banner.substring(start, end).trim();
        }
        
        if (banner.contains("SSH-")) {
            int start = banner.indexOf("SSH-");
            int end = banner.indexOf(" ", start);
            if (end == -1) end = Math.min(start + 50, banner.length());
            return banner.substring(start, end).trim();
        }
        
        // Return first line if short enough
        String[] lines = banner.split("\\n");
        if (lines.length > 0 && lines[0].length() < 80) {
            return lines[0].trim();
        }
        
        return banner.length() > 50 ? banner.substring(0, 50) + "..." : banner;
    }

    /**
     * CLI entry point for testing banner grabbing
     * 
     * @param args Command line arguments: <host> <port>
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java BannerGrabber <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port;

        try {
            String portArg = args[1].trim();
            if (portArg.contains("-")) {
                portArg = portArg.split("-", 2)[0].trim();
            }
            port = Integer.parseInt(portArg);
        } catch (NumberFormatException e) {
            System.err.println("Error: Port must be a number or a start-end range");
            System.exit(1);
            return;
        }

        String banner = grabBanner(host, port);
        if (banner.isEmpty()) {
            System.out.println("No banner received");
        } else {
            System.out.println("Banner: " + banner);
            String version = extractVersion(banner);
            if (!version.isEmpty()) {
                System.out.println("Version: " + version);
            }
        }
    }
}
