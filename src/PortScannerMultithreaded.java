import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * PortScannerMultithreaded - Multi-threaded port scanner for improved performance
 * 
 * This class uses a thread pool to scan multiple ports concurrently,
 * dramatically improving scan speed compared to sequential scanning.
 * 
 * Performance: ~40-100x faster than PortScanner (sequential version)
 * 
 * @author Elodie Moisan
 * @version 2.0
 */

public class PortScannerMultithreaded {

    //Configuration
    private String targetHost;
    private int startPort;
    private int endPort;
    private int threadPoolSize;

    //Results and timing 
    private List<ScanResult> results;
    private long scanStartTime;
    private long scanEndTime;

    //Thread-safe counter for progress
    private AtomicInteger scannedPorts;
    private AtomicInteger totalPorts;

    //Default thread pool size
    private static final int DEFAULT_THREAD_POOL_SIZE = 100;


    /**
     * Constructor with default thread pool size (100 threads)
     * 
     * @param targetHost the target hostname or IP address
     * @param startPort First port in range to scan 
     * @param endPort Last port in range to scan
     */

    public PortScannerMultithreaded(String targetHost, int startPort, int endPort){
        this.targetHost = targetHost;
        this.startPort = startPort;
        this.endPort = endPort;
        this.threadPoolSize = DEFAULT_THREAD_POOL_SIZE;

        //Thread-safe list for concurrent access
        this.results = new CopyOnWriteArrayList<>();

        //Thread-safe counters
        this.scannedPorts = new AtomicInteger(0);
        this.totalPorts = new AtomicInteger(endPort - startPort + 1);
    }

    /**
     * Constructor with custom thread pool size 
     * 
     * @param targetHost The target hostname or IP address
     * @param startPort First port in range to scan
     * @param endPort Last port in range to scan
     * @param threadPoolSize Number of concurrent threads to use
     */

    public PortScannerMultithreaded(String targetHost, int startPort, int endPort, int threadPoolSize){
        this.targetHost = targetHost;
        this.startPort = startPort;
        this.endPort = endPort;
        this.threadPoolSize = threadPoolSize;

        //Thread-safe list for concurrent access
        this.results = new CopyOnWriteArrayList<>();

        //Thread-safe counters
        this.scannedPorts = new AtomicInteger(0);
        this.totalPorts = new AtomicInteger(endPort - startPort + 1);
    }

    /**
     * Executes the multi-threaded port scan 
     * Creates a thread pool and scans all ports concurrently
     */

    public void scan(){
        //Display scan information
        displayScanInfo();

        //Start timing 
        scanStartTime = System.currentTimeMillis();

        System.out.println("Starting multi-threaded scan...");
        System.out.println(" Using " + threadPoolSize + " concurrent threads\n");
        System.out.println("PORT    STATE    SERVICE              TIME");
        System.out.println("────────────────────────────────────────────────");

        //Create thread pool
        ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);

        //Submit scan tasks for each port
        for (int port = startPort; port <= endPort; port++){
            final int currentPort = port;

            //Submit task to thread pool
            executor.submit(() -> {
                scanPort(currentPort);
            });
        }

        //Shutdown executor (no new tasks accepted)
        executor.shutdown();

        try {
            //Wait for all tasks to complete (max 10 minutes)
            boolean finished = executor.awaitTermination(10, TimeUnit.MINUTES);

            if (!finished) {
                System.err.println("\n Warning: Scan timeout - forcing shutdown");
                executor.shutdown();
            }
        } catch (InterruptedException e) {
            System.err.println("\n Scan Interrupted");
            executor.shutdown();
            Thread.currentThread().interrupt();
        }

        //End timing 
        scanEndTime = System.currentTimeMillis();

        //Display summary
        System.out.println("\n"); //New line after progress
        displaySummary();

    }

    /**
     * Scans a single port (called by worker threads)
     * Thread-safe method for concurrent execution
     * 
     * @param port Port number to scan
     */

    private void scanPort(int port){
        //Check the port
        ScanResult result = PortChecker.checkPort(targetHost, port);

        //If open, store and display
        if (result.isOpen()) {
            results.add(result);

            //Thread-safe display
            synchronized (System.out) {
                System.out.println(result.toString());
            }
        }
        //Update progress (thread-safe)
        int scanned = scannedPorts.incrementAndGet();
        displayProgress(scanned, totalPorts.get());
    }

    /**
     * Displays initial scan configuration
     */
    private void displayScanInfo(){
        System.out.println("\n Target: " + targetHost);
        System.out.println(" Port range: " + startPort + "-" + endPort);
        System.out.println(" Timeout: " + PortChecker.getTimeout() + "ms");
        System.out.println(" Threads: " + threadPoolSize);
        System.out.println();        
    }

    /**
     * Displays scan progress (thread-safe)
     * 
     * @param current Current number of ports scanned
     * @param total Total number of ports to scan
     */

    private synchronized void displayProgress(int current, int total){
        //Update every 10 ports or at completion
        if (current % 10 == 0 || current == total){
            int percentage = (current * 100) / total;

            //ANSI color codes
            String GREEN = "\u001B[32m";
            String BLUE = "\u001B[34m";
            String RESET = "\u001B[0m";

            //Create progress bar 
            int barLength = 40;
            int filled = (barLength * current) / total;
            StringBuilder bar = new StringBuilder("\rProgress: [");

            for(int i = 0; i < barLength; i++){
                if(i < filled){
                    bar.append("█");
                } else {
                    bar.append("░");
                }
            }
            bar.append("]").append(percentage).append("% (")
                .append(current).append("/").append(total).append(")");
            
            System.out.print(bar.toString());
            System.out.flush();
        }
    }

    /**
     * Displays final scan summary with statistics
     */ 

    private void displaySummary(){
        long durationMs = scanEndTime - scanStartTime;
        double durationSec = durationMs / 1000.0;
        int totalScanned = totalPorts.get();
        int openPorts = results.size();

        //Calculate ports per second
        double portsPerSecond = totalScanned / durationSec;

        System.out.println("════════════════════════════════════════");
        System.out.println("           SCAN SUMMARY");
        System.out.println("════════════════════════════════════════");
        System.out.printf(" Scan completed in %.2f seconds%n", durationSec);
        System.out.println(" Total ports scanned: " + totalScanned);
        System.out.println(" Open ports found: " + openPorts);
        System.out.printf("⚡ Speed: %.2f ports/second%n", portsPerSecond);

        if(openPorts == 0) {
            System.out.println("\n No open ports found in the specified range");
        }

        System.out.println("════════════════════════════════════════\n");
    }

    /**
     * Gets the list of scan results (only open ports)
     * 
     * @return List of ScanResult objects for open ports
     */
    public List<ScanResult> getResults() {
        return results;
    }

    /**
     * Gets the scan duration in milliseconds
     * 
     * @return Scan duration, or 0 if scan hasn't completed
     */
    public long getScanDuration(){
        if(scanEndTime > 0 && scanStartTime > 0){
            return scanEndTime - scanStartTime;
        }
        return 0;
    }
}
