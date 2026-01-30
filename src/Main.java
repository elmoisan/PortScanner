/**
 * Simple Port Scanner - Main Entry Point
 * 
 * A TCP port scanner for learning networking and cybersecurity concepts.
 * 
 * @author Elodie Moisan
 * @version 1.0
 */

public class Main {
    
    /**
     * Program entry point
     * 
     * @param args Command line arguments: <host> <start-port>-<end-port>
     */
    public static void main(String[] args) {
        // Display banner
        printBanner();
        
        // Display legal disclaimer
        printDisclaimer();
        
        //Validates arguments
        if(!validateArguments(args)){
            printUsage();
            System.exit(1);
        }

        //Parse Arguments
        String host = args[0];
        int startPort = 1;
        int endPort = 1024; //Default: scan well-known ports

        //Parse port range if provided
        if(args.length >= 2){
            try{
                String[] range = args[1].split("-");
                startPort = Integer.parseInt(range[0]);
                endPort = Integer.parseInt(range[1]);

                //Validate port range
                if (!ServiceIdentifier.isValidPort(startPort) ||
                    !ServiceIdentifier.isValidPort(endPort)){
                        System.err.println("Error: Ports must be between 0 and 65535");
                        System.exit(1);
                }

                if (startPort > endPort){
                    System.err.println("Start port must be less than or equal to end port");
                    System.exit(1);
                }

            } catch (Exception e){
                System.err.println(" Error: Invalid port range format");
                System.err.println("   Use format: <start>-<end> (e.g., 1-1000)");
                System.exit(1);
            }    
        }

        //Create and launch scanner
        PortScanner scanner = new PortScanner(host, startPort, endPort);
        scanner.scan();
    }

    /**
     * Validates command line arguments
     * 
     * @param args Command line arguments
     * @return true if valid, false otherwise
     */

    private static boolean validateArguments(String[] args){
        return args.length >= 1 && args.length <= 2;
    }

    /**
     * Display application banner
     */
    private static void printBanner() {
        System.out.println("╔════════════════════════════════════╗");
        System.out.println("║     Simple Port Scanner v1.0      ║");
        System.out.println("╚════════════════════════════════════╝");
        System.out.println();
    }
    
    /**
     * Display legal disclaimer
     */
    private static void printDisclaimer() {
        System.out.println("Legal Warning:");
        System.out.println("Only scan systems you own or have permission to test.");
        System.out.println("Unauthorized scanning may be illegal.\n");
    }

     /**
     * Display usage information
     */
    private static void printUsage() {
        System.out.println("Usage: java Main <host> [port-range]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  <host>         Target hostname or IP address");
        System.out.println("  [port-range]   Port range in format: start-end (optional, default: 1-1024)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java Main localhost");
        System.out.println("  java Main 192.168.1.1 1-100");
        System.out.println("  java Main scanme.nmap.org 20-25");
        System.out.println("  java Main example.com 80-443");
    }
}
