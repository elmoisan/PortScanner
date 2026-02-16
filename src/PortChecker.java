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
    private static final int DEFAULT_TIMEOUT = 2000; //2 seconds
    private static int timeout = DEFAULT_TIMEOUT;

    /**
     * Sets the timeout for connection attempts
     * 
     * @param timeoutMs Timeout in milliseconds (min: 100ms, max: 30000ms)
     */
    public static void setTimeout(int timeoutMs) {
        if (timeoutMs < 100) {
            System.err.println("Warning: Timeout too low (minimum 100ms), using 100ms");
            timeout = 100;
        } else if (timeoutMs > 30000) {
            System.err.println("Warning: Timeout too high (maximum 30000ms), using 30000ms");
            timeout = 30000;
        } else {
            timeout = timeoutMs;
        }
    }

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
            socket.connect(new InetSocketAddress(host, port), timeout);
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

    public static ScanResult checkPort(String host, int port){
        long startTime = System.currentTimeMillis();

        try{
            //Create socket with timeout
            Socket socket = new Socket();

            //Attempt connection
            socket.connect(new InetSocketAddress(host, port), timeout);
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
        return timeout;
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

    /**
 * Complete port check with optional banner grabbing
 * 
 * @param host Target hostname or IP address
 * @param port Port number to check
 * @param grabBanner Whether to attempt banner grabbing
 * @return ScanResult object with detailed information
 */
    public static ScanResult checkPort(String host, int port, boolean grabBanner) {
        long startTime = System.currentTimeMillis();
    
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), timeout);
        
            long responseTime = System.currentTimeMillis() - startTime;
            socket.close();
        
            return new ScanResult(port, true, responseTime, grabBanner);
        
        } catch (SocketTimeoutException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            return new ScanResult(port, false, responseTime, false);
        
        } catch (IOException e) {
            long responseTime = System.currentTimeMillis() - startTime;
            return new ScanResult(port, false, responseTime, false);
        }
    }
}
