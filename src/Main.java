/**
 * Simple Port Scanner - Main Entry Point
 * 
 * A TCP port scanner for learning networking and cybersecurity concepts.
 * Supports both sequential (v1) and multi-threaded (v2) scanning.
 * 
 * @author Elodie Moisan
 * @version 2.2
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
        boolean grabBanners = false;
        int timeout = 2000; // Default: 2 seconds
        String exportFormat = null;
        String exportFilename = null;
        int argOffset = 0;

        // Process all flags
        while (argOffset < args.length) {
            String arg = args[argOffset];
            
            if (arg.equals("-t") || arg.equals("--threads")) {
                useMultithreading = true;
                argOffset++;
                
                // Check if next arg is a thread count
                if (argOffset < args.length && args[argOffset].matches("\\d+")) {
                    threadCount = Integer.parseInt(args[argOffset]);
                    argOffset++;
                    
                    if (threadCount < 1 || threadCount > 1000) {
                        System.err.println("Error: Thread count must be between 1 and 1000");
                        System.exit(1);
                    }
                }
            } else if (arg.equals("-b") || arg.equals("--banner")) {
                grabBanners = true;
                argOffset++;
            } else if (arg.equals("-o") || arg.equals("--timeout")) {
                argOffset++;
                if (argOffset >= args.length) {
                    System.err.println("Error: --timeout requires a value in milliseconds");
                    System.exit(1);
                }
                try {
                    timeout = Integer.parseInt(args[argOffset]);
                    if (timeout < 100 || timeout > 30000) {
                        System.err.println("Error: Timeout must be between 100 and 30000 milliseconds");
                        System.exit(1);
                    }
                    argOffset++;
                } catch (NumberFormatException e) {
                    System.err.println("Error: Timeout must be a number");
                    System.exit(1);
                }
            } else if (arg.equals("-e") || arg.equals("--export")) {
                argOffset++;
                if (argOffset >= args.length) {
                    System.err.println("Error: --export requires format and filename");
                    System.err.println("Usage: --export <format> <filename>");
                    System.err.println("Formats: csv, json, all");
                    System.exit(1);
                }
                exportFormat = args[argOffset];
                argOffset++;
                
                if (argOffset >= args.length) {
                    System.err.println("Error: --export requires a filename");
                    System.exit(1);
                }
                exportFilename = args[argOffset];
                argOffset++;
            } else {
                // Not a flag, must be the host
                break;
            }
        }

        //Validate remaining arguments
        if(argOffset >= args.length) {
            printUsage();
            System.exit(1);
        }

        //Parse arguments
        String host = args[argOffset];
        int startPort = 1;
        int endPort = 1024; //Default: scan well-known ports
        argOffset++;

        //Parse port range if provided
        if(argOffset < args.length) {
            try {
                String[] range = args[argOffset].split("-");
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

        // Set timeout in PortChecker
        PortChecker.setTimeout(timeout);

        //Launch appropriate scanner 
        if(useMultithreading) {
            //Version 2.1+ - Multi-threaded
            PortScannerMultithreaded scanner = new PortScannerMultithreaded(host, startPort, endPort, threadCount, grabBanners);
            scanner.scan();
            
            // Export if requested
            if (exportFormat != null) {
                ExportHelper.export(scanner.getResults(), host, exportFilename, exportFormat, scanner.getScanTime());
            }
        } else {
            //Version 2.1+ - Sequential
            PortScanner scanner = new PortScanner(host, startPort, endPort, grabBanners);
            scanner.scan();
            
            // Export if requested
            if (exportFormat != null) {
                ExportHelper.export(scanner.getResults(), host, exportFilename, exportFormat, scanner.getScanTime());
            }
        }
    }

    /**
     * Display application banner 
     */
    private static void printBanner(){
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║     Simple Port Scanner v2.2       ║");
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
        System.out.println("                      Specify N for custom thread count (1-1000)");
        System.out.println("  -b, --banner        Enable banner grabbing for version detection");
        System.out.println("  -o, --timeout [MS]  Set connection timeout in milliseconds (100-30000, default: 2000)");
        System.out.println("  -e, --export [F][N] Export results to file (format: csv|json|all, filename: output)");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  <host>              Target hostname or IP address");
        System.out.println("  [port-range]        Port range in format: start-end (optional, default: 1-1024)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java Main localhost                              # Sequential scan, ports 1-1024");
        System.out.println("  java Main -t localhost 1-100                     # Multi-threaded, 100 threads");
        System.out.println("  java Main -t -b localhost 80-443                 # With banner grabbing");
        System.out.println("  java Main -o 5000 localhost 1-1000               # Custom 5s timeout");
        System.out.println("  java Main -t -e csv results localhost 1-1000     # Export to CSV");
        System.out.println("  java Main -t -e all results localhost 1-1000     # Export CSV and JSON");
        System.out.println("  java Main scanme.nmap.org 20-80                  # Remote server scan");
        System.out.println();
        System.out.println("Performance:");
        System.out.println("  Sequential:     ~2 seconds per port");
        System.out.println("  Multi-threaded: ~40-100x faster (recommended for large ranges)");
    }    
}    