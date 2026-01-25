/**
 * Test program for ServiceIdentifier and ScanResult classes
 */

public class TestClasses {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("   Testing ServiceIdentifier class");
        System.out.println("═══════════════════════════════════════\n");
        
        //Test ServiceIdentifier
        testServiceIdentifier();
        
        System.out.println("\n═══════════════════════════════════════");
        System.out.println("   Testing ScanResult class");
        System.out.println("═══════════════════════════════════════\n");

        //Test ScanResult
        testScanResult();
    
    }
    
    private static void testServiceIdentifier(){
        int[] testPort = {21, 22, 80, 443, 3306, 8080, 9999};

        System.out.println("PORT        SERVICE");
        System.out.println("────────────────────────────────────────────────");

        for (int port : testPort){
            String service = ServiceIdentifier.identifyService(port);
            System.out.printf("%-8d %s\n", port, service);
        }

        System.out.println("\nPort validity tests:");
        System.out.println("Port 80 valid? " + ServiceIdentifier.isValidPort(80));
        System.out.println("Port -1 valid? " + ServiceIdentifier.isValidPort(-1));
        System.out.println("Port 70000 valid? " + ServiceIdentifier.isValidPort(70000));
        
        System.out.println("\nPort categories:");
        System.out.println("Port 80: " + ServiceIdentifier.getPortCategory(80));
        System.out.println("Port 8080: " + ServiceIdentifier.getPortCategory(8080));
        System.out.println("Port 50000: " + ServiceIdentifier.getPortCategory(50000));
    }

    private static void testScanResult(){
        //Created test results 
        ScanResult result1 = new ScanResult(80, true, 85);
        ScanResult result2 = new ScanResult(443, true, 120);
        ScanResult result3 = new ScanResult(22, true, 95);
        ScanResult result4 = new ScanResult(9999, false, 2000);
        
        System.out.println("PORT    STATE      SERVICE              TIME");
        System.out.println("────────────────────────────────────────────────");

        //Display open ports 
        System.out.println(result1.toString());
        System.out.println(result2.toString());
        System.out.println(result3.toString());
        
        // Closed port should return null from toString()
        String closedResult = result4.toString();
        if (closedResult == null) {
            System.out.println("\n Closed port correctly returns null (not displayed)");
        }
        
        System.out.println("\nDetailed info for port 80:");
        System.out.println(result1.toDetailedString());
        
        System.out.println("\nGetter tests:");
        System.out.println("Port: " + result1.getPort());
        System.out.println("Is Open: " + result1.isOpen());
        System.out.println("Service: " + result1.getService());
        System.out.println("Response Time: " + result1.getResponseTime() + "ms");
        System.out.println("State: " + result1.getState());
    }

}

