/**
 * Simple Port Scanner - Main Entry Point
 * 
 * A TCP port scanner for learning networking and cybersecurity concepts.
 * Supports both sequential (v1) and multi-threaded (v2) scanning.
 * 
 * @author Elodie Moisan
 * @version 2.0
 */

public class Main {
    
    /**
     * Program entry point
     * 
     * @param args Command line arguments: [options] <host> [start-port]-[end-port]
     */
    public static void main(String[] args) {
        //Display banner
        printBanner();

        //Display legal disclaimer
        printDisclaimer();

        //Parse options
        boolean useMultithreading = false;
        int threadCount = 100;
        int argOffset = 0;

        //Check for -t or --threads flag
        if(args.length > 0 && (args[0].equals("-t") || args[0].equals("--threads"))) {
            useMultithreading = true;
            argOffset = 1;

            //Check if thread count is specified
            if(args.length > 1 && args[1].matches("\\d+")) {
                threadCount = Integer.parseInt(args[1]);
                argOffset = 2;

                if(threadCount < 1 || threadCount > 1000) {
                    System.err.println("Error: Thread count must be between 1 and 1000");
                    System.exit(1);
                }
            }
        }

        //Validate remaining arguments
        if(args.length - argOffset < 1) {
            printUsage();
            System.exit(1);
        }

        //Parse arguments
        String host = args[argOffset];
        int startPort = 1;
        int endPort = 1024; //Default: scan well-known ports

        //Parse port range if provided
        if(args.length - argOffset >= 2) {
            try {
                String[] range = args[argOffset + 1].split("-");
                startPort = Integer.parseInt(range[0]);
                endPort = Integer.parseInt(range[1]);

                //Validate port range 
                if(!ServiceIdentifier.isValidPort(startPort) || !ServiceIdentifier.isValidPort(endPort)) {
                    System.err.println("Error: Ports must be between 0 and 65535");
                    System.exit(1);
                } 

                if(startPort > endPort){
                    System.err.println("Error: Start port must be less than or equal to end port");
                    System.exit(1);
                }
            }catch (Exception e){
                System.err.println("Error: Invalid port range format");
                System.err.println("Use format: <start>-<end> (e.g., 1-1000)");
                System.exit(1);
            }
        }
        //Launch appropriate scanner 
        if(useMultithreading) {
            //Version 2.0 - Multi-threaded
            PortScannerMultithreaded scanner = new PortScannerMultithreaded(host, startPort, endPort, threadCount);
            scanner.scan();
        } else {
            //Version 1.0 - Sequential
            PortScanner scanner = new PortScanner(host, startPort, endPort);
            scanner.scan();
        }
    }

    /**
     * Display application banner 
     */
    private static void printBanner(){
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║     Simple Port Scanner v2.0       ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();        
    }
    /**
     * Display legal disclaimer
     */
    private static void printDisclaimer() {
        System.out.println("⚖️  Legal Warning:");
        System.out.println("Only scan systems you own or have permission to test.");
        System.out.println("Unauthorized scanning may be illegal.\n");
    }
    
    /**
     * Display usage information
     */
    private static void printUsage() {
        System.out.println("Usage: java Main [options] <host> [port-range]");
        System.out.println();
        System.out.println("Options:");
        System.out.println("  -t, --threads [N]   Enable multi-threaded scanning (default: 100 threads)");
        System.out.println("                      Specify N to use custom thread count (1-1000)");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  <host>              Target hostname or IP address");
        System.out.println("  [port-range]        Port range in format: start-end (optional, default: 1-1024)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java Main localhost                      # Sequential scan, ports 1-1024");
        System.out.println("  java Main -t localhost 1-100             # Multi-threaded, 100 threads");
        System.out.println("  java Main --threads 50 192.168.1.1 1-100 # Multi-threaded, 50 threads");
        System.out.println("  java Main scanme.nmap.org 20-80          # Sequential scan");
        System.out.println();
        System.out.println("Performance:");
        System.out.println("  Sequential:     ~2 seconds per port");
        System.out.println("  Multi-threaded: ~40-100x faster (recommended for large ranges)");
    }    
}    