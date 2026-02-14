import java.util.ArrayList;
import java.util.List;

/**
 * PortScanner - Orchestrates port scanning operations
 * 
 * This class coordinates the scanning of multiple ports on a target host.
 * It manages the scan process, collects results, and displays progress.
 * 
 * @author Elodie Moisan
 * @version 1.0
 */ 

public class PortScanner{

    //Configuration
    private String targetHost;
    private int startPort;
    private int endPort;
    private boolean grabBanners;

    //Results and timing
    private List<ScanResult> results;
    private long scanStartTime;
    private long scanEndTime;

    /**
     * Constructor - Creates a new port scanner 
     * 
     * @param targetHost The target hostname or IP address
     * @param startPort First port in range to scan 
     * @param endPort Last port in range to scan
     */

    public PortScanner(String targetHost, int startPort, int endPort){
        this(targetHost, startPort, endPort, false);
    }

    /**
     * Constructor with banner grabbing option
     * 
     * @param targetHost The target hostname or IP address
     * @param startPort First port in range to scan 
     * @param endPort Last port in range to scan
     * @param grabBanners Whether to attempt banner grabbing
     */

    public PortScanner(String targetHost, int startPort, int endPort, boolean grabBanners){
        this.targetHost = targetHost;
        this.startPort = startPort;
        this.endPort = endPort;
        this.grabBanners = grabBanners;
        this.results = new ArrayList<>();
    }

    /**
     * Executes the port scan
     * Scans all ports in the specified range and displays results in real-time
     */

    public void scan(){
        //Display scan information
        displayScanInfo();

        //Scan timing
        scanStartTime = System.currentTimeMillis();

        //Calculate total ports to scan
        int totalPorts = endPort - startPort + 1;
        int scannedPorts = 0;

        System.out.println("Starting scan...\n");
        System.out.println("PORT    STATE    SERVICE              TIME");
        System.out.println("────────────────────────────────────────────────");

        //Scan each port in the range
        for(int port = startPort; port <= endPort; port++){
            //Check the port 
            ScanResult result = PortChecker.checkPort(targetHost, port, grabBanners);

            //Store and display if open
            if(result.isOpen()){
                results.add(result);
                System.out.println(result.toString());
            }

            //Update progress
            scannedPorts++;
            displayProgress(scannedPorts, totalPorts);
        }

        //End timing
        scanEndTime = System.currentTimeMillis();

        //Display summary
        System.out.println(); //New line after progress
        displaySummary();
    }

    /**
     * Displays initial scan configuration
     */

    private void displayScanInfo(){
        System.out.println("\n Target: " + targetHost);
        System.out.println("Port range: " + startPort + "-" + endPort);
        System.out.println("Timeout: " + PortChecker.getTimeout() + "ms");
        System.out.println();
    }

    /**
     * Displays scan progress
     * 
     * @param current Current number of ports scanned 
     * @param total Total number of ports to scan
     */
    private void displayProgress(int current, int total){
        //Only update every 50 ports or at the end (to avoid too much output)
        if (current % 50 == 0 || current == total){
            int percentage = (current * 100)/total;
            System.out.println("\rProgress: " + current + "/" + total + " (" + percentage + "%)");
            System.out.flush();
        }
    }

    /**
     * Displays final scan summary with statistics
     */

    private void displaySummary(){
        long duration = (scanEndTime - scanStartTime) / 1000;
        int totalScanned = endPort - startPort + 1;
        int openPorts = results.size();

    System.out.println("\n════════════════════════════════════════");
        System.out.println("           SCAN SUMMARY");
        System.out.println("════════════════════════════════════════");
        System.out.println(" Scan completed in " + duration + " seconds");
        System.out.println(" Total ports scanned: " + totalScanned);
        System.out.println(" Open ports found: " + openPorts);

        if (openPorts == 0){
            System.out.println("\n No open ports found in the specified range");
        }

        System.out.println("════════════════════════════════════════\n");
    }

    /**
     * Gets the list of scan results (only open ports)
     * 
     * @return List of ScanResult objects for open ports
     */

    public List<ScanResult> getResults(){
        return results;
    }

    /**
     * Gets the target host being scanned
     * 
     * @return Target hostname or IP address
     * 
     */
    public String getTargetHost(){
        return targetHost;
    }

    /**
     * Gets the start of the port range
     * 
     * @return Starting port number
     */
    public int getEndPort(){
        return endPort;
    }

    /**
     * Gets the total scan duration in milliseconds
     * 
     * @return Scan duration, or 0 if scan hasn't completed
     */
    public long getScanDuration(){
        if (scanEndTime > 0 && scanStartTime > 0){
            return scanEndTime - scanEndTime;
        }
        return 0;
    }
}