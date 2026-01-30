import java.net.Socket;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.io.IOException;

/**
 * PortChecker - Test individual ports for connectivity
 * 
 * This class handles the low-level network operations to determine
 * if a specific port on a target is open or closed.
 * Uses TCP connect Scanning technique.
 * 
 * @author Elodie Moisan
 * @version 1.0
 */


public class PortChecker {

    //Timeout for connection attempts (milliseconds)
    private static final int TIMEOUT = 2000; //2 seconds

    /**
     * Simple check if a port is open (returns boolean only)
     * 
     * @param host Target hostname or IP address
     * @param port Port number to check
     * @return true if port is open, false otherwise
     */

    public static boolean isPortOpen(String host, int port){
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), TIMEOUT);
            socket.close();
            return true;

        } catch (IOException e){
            return false;
        }
    }

    /**
     * Complete port check that returns detailed scan result
     * 
     * @param host Target hostname or IP address
     * @param port Port number to check
     * @return ScanResult object with detailed information
     * 
     */

    public static ScanResult chechPort(String host, int port){
        long startTime = System.currentTimeMillis();

        try{
            //Create socket with timeout
            Socket socket = new Socket();

            //Attempt connection
            socket.connect(new InetSocketAddress(host, port), TIMEOUT);
            long responseTime = System.currentTimeMillis() - startTime;
            socket.close();
            return new ScanResult(port, true, responseTime);

        } catch (IOException e){
            //Connection refused = port is closed
            long responseTime = System.currentTimeMillis() - startTime;
            return new ScanResult(port, false, responseTime);
        }
    }

    /**
     * Gets the configured timeout value
     * 
     * @return Timeout in milliseconds
     */

    public static int getTimeout(){
        return TIMEOUT;
    }

    /**
     * Validates if a hostname/IP and port combination is valid before scanning
     * 
     * @param host Hostname or IP address
     * @param port Port number
     * @return true if valid, false otherwise
     */

    public static boolean isValidTarget(String host, int port){
        if (host == null || host.trim().isEmpty()){
            return false;
        }

        if (!ServiceIdentifier.isValidPort(port)){
            return false;
        }

        return true;
    }
}
