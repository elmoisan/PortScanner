import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScanResult - Data object representing the result of scanning a single port
 * 
 * This class stores all relevant information about the port scan attempt,
 * including the port number, open/closes state, reponse time, and service identifier.
 * 
 * @author Elodie Moisan
 * @version 1.0
 */

public class ScanResult{

    //Attributes 
    private final int port;
    private final boolean isOpen;
    private final long responseTime;
    private final String service;
    private final String timestamp;

    /**
     * Constructor - Creates a new scan result 
     * 
     * @param port The port number that was scanned 
     * @param isOpen true if the port is open, false if closes/filtered
     * @param responseTime Response time in milliseconds
     */

    public ScanResult(int port, boolean isOpen, long responseTime){
        this.port = port;
        this.isOpen = isOpen;
        this.responseTime = responseTime;

        //Identify the service if port is open
        if (isOpen){
            this.service =ServiceIdentifier.identifyService(port);
        } else {
            this.service = "N/A";
        }

        //Record timestamp
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    //Getters

    /**
     * Gets the port number
     * @return the port number
     */

    public int getPort(){
        return port;
    }

    /**
     * Checks if the port is open
     * @return true if open, false if closed/filtered
     */

    public boolean isOpen(){
        return isOpen;
    }

    /**
     * Gets the response time
     * @return Response time in milliseconds
     */

    public long getResponseTime(){
        return responseTime;
    }

    /**
     * Gets the identified service name
     * @return Service name (e.g., "HTTP", "SSH") or "Unknow"
     */

    public String getService(){
        return service;
    }

    /**
     * Gets the timestamp when the scan was performed 
     * @return Timestamp as a formatted string
     */ 

    public String getTimestamp(){
        return timestamp;
    }

    /**
     * Gets the state as a string
     * @return "OPEN" or "CLOSED"
     */

    public String getState(){
        return isOpen ? "OPEN" : "CLOSED";
    }

    /**
     * Formats the result as a readable string
     * Only returns a formatted string if the port is open
     * 
     * @return Formatted string, or null if port is closed
     */

    @Override
    public String toString(){
        if(!isOpen){
            return null; //Don't display closed ports
        }

        return String.format("%-8d %-10s %-20s (%dms)",
            port,
            "OPEN",
            service,
            responseTime
        );
    }

    /**
     * Formats the result with details (including timestamp)
     * 
     * @return Detailed formatted string
     */

    public String toDetailedString(){
        return String.format("Port: %d | State: %s | Service: %s | Time: %dms | Scanned: %s",
            port,
            getState(),
            service,
            responseTime,
            timestamp
        );
    }


}