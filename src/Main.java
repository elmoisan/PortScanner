/**
 * Simple Port Scanner - Main Entry Point
 * 
 * A TCP port scanner for learning networking and cybersecurity concepts.
 * 
 * @author elmoisan
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
        
        // TODO: Validate arguments
        // TODO: Parse host and port range
        // TODO: Create PortScanner instance
        // TODO: Launch scan
        
        System.out.println("\n🚧 Project in development - Coming soon!");
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
}
