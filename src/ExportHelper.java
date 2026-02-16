import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExportHelper - Handles export of scan results to various formats
 * 
 * Supports exporting scan results to CSV and JSON formats for
 * further analysis or reporting.
 * 
 * @author Elodie Moisan
 * @version 2.2
 */

public class ExportHelper {
    
    private static final DateTimeFormatter dateFormat = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Exports scan results to CSV format
     * 
     * @param results List of ScanResult objects
     * @param targetHost The target host that was scanned
     * @param filename Output filename (without extension)
     * @param scanTime Total scan time in seconds
     * @return true if export successful, false otherwise
     */
    public static boolean exportToCSV(List<ScanResult> results, String targetHost, 
                                       String filename, double scanTime) {
        try {
            String outputFile = filename.endsWith(".csv") ? filename : filename + ".csv";
            FileWriter writer = new FileWriter(outputFile);
            
            // Write header
            writer.write("Target,Port,State,Service,Time(ms),Banner\n");
            
            // Write data rows
            for (ScanResult result : results) {
                if (result.isOpen()) {
                    String banner = result.getBanner() != null ? 
                        "\"" + result.getBanner().replace("\"", "\"\"") + "\"" : "";
                    
                    writer.write(String.format(
                        "%s,%d,OPEN,%s,%d,%s\n",
                        targetHost,
                        result.getPort(),
                        ServiceIdentifier.identifyService(result.getPort()),
                        result.getResponseTime(),
                        banner
                    ));
                }
            }
            
            writer.write("\n# Scan Summary\n");
            writer.write(String.format("# Total ports scanned: %d\n", 
                results.size()));
            writer.write(String.format("# Open ports found: %d\n", 
                countOpen(results)));
            writer.write(String.format("# Scan completed at: %s\n", 
                LocalDateTime.now().format(dateFormat)));
            writer.write(String.format("# Scan duration: %.2f seconds\n", 
                scanTime));
            
            writer.close();
            System.out.println("✅ Results exported to: " + outputFile);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error exporting to CSV: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Exports scan results to JSON format
     * 
     * @param results List of ScanResult objects
     * @param targetHost The target host that was scanned
     * @param filename Output filename (without extension)
     * @param scanTime Total scan time in seconds
     * @return true if export successful, false otherwise
     */
    public static boolean exportToJSON(List<ScanResult> results, String targetHost, 
                                        String filename, double scanTime) {
        try {
            String outputFile = filename.endsWith(".json") ? filename : filename + ".json";
            FileWriter writer = new FileWriter(outputFile);
            
            // Write JSON structure
            writer.write("{\n");
            writer.write("  \"scan_info\": {\n");
            writer.write(String.format("    \"target\": \"%s\",\n", targetHost));
            writer.write(String.format("    \"total_ports_scanned\": %d,\n", results.size()));
            writer.write(String.format("    \"open_ports_found\": %d,\n", countOpen(results)));
            writer.write(String.format("    \"scan_duration_seconds\": %.2f,\n", scanTime));
            writer.write(String.format("    \"timestamp\": \"%s\"\n", 
                LocalDateTime.now().format(dateFormat)));
            writer.write("  },\n");
            
            writer.write("  \"results\": [\n");
            
            List<ScanResult> openPorts = new java.util.ArrayList<>();
            for (ScanResult result : results) {
                if (result.isOpen()) {
                    openPorts.add(result);
                }
            }
            
            for (int i = 0; i < openPorts.size(); i++) {
                ScanResult result = openPorts.get(i);
                writer.write("    {\n");
                writer.write(String.format("      \"port\": %d,\n", result.getPort()));
                writer.write(String.format("      \"state\": \"OPEN\",\n"));
                writer.write(String.format("      \"service\": \"%s\",\n", 
                    ServiceIdentifier.identifyService(result.getPort())));
                writer.write(String.format("      \"response_time_ms\": %d",
                    result.getResponseTime()));
                
                if (result.getBanner() != null && !result.getBanner().isEmpty()) {
                    String banner = result.getBanner()
                        .replace("\\", "\\\\")
                        .replace("\"", "\\\"")
                        .replace("\n", "\\n")
                        .replace("\r", "\\r");
                    writer.write(String.format(",\n      \"banner\": \"%s\"", banner));
                }
                
                writer.write("\n    }");
                
                if (i < openPorts.size() - 1) {
                    writer.write(",");
                }
                writer.write("\n");
            }
            
            writer.write("  ]\n");
            writer.write("}\n");
            
            writer.close();
            System.out.println("✅ Results exported to: " + outputFile);
            return true;
            
        } catch (IOException e) {
            System.err.println("❌ Error exporting to JSON: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Helper method to count open ports
     * 
     * @param results List of scan results
     * @return Count of open ports
     */
    private static int countOpen(List<ScanResult> results) {
        return (int) results.stream()
            .filter(ScanResult::isOpen)
            .count();
    }
    
    /**
     * Determines export format and exports accordingly
     * 
     * @param results List of ScanResult objects
     * @param targetHost The target host that was scanned
     * @param filename Output filename (with or without extension)
     * @param format Export format ("csv" or "json" or "all")
     * @param scanTime Total scan time in seconds
     * @return true if export(s) successful, false otherwise
     */
    public static boolean export(List<ScanResult> results, String targetHost, 
                                  String filename, String format, double scanTime) {
        boolean success = true;
        
        if (format.equalsIgnoreCase("csv")) {
            success = exportToCSV(results, targetHost, filename, scanTime);
        } else if (format.equalsIgnoreCase("json")) {
            success = exportToJSON(results, targetHost, filename, scanTime);
        } else if (format.equalsIgnoreCase("all") || format.equalsIgnoreCase("both")) {
            success = exportToCSV(results, targetHost, filename, scanTime) && 
                     exportToJSON(results, targetHost, filename, scanTime);
        } else {
            System.err.println("❌ Unknown export format: " + format);
            System.err.println("Supported formats: csv, json, all");
            success = false;
        }
        
        return success;
    }
}
