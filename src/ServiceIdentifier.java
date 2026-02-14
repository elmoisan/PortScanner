/**
 * ServiceIdentifier - Identifies network services based on port numbers 
 * 
 * This class maintains a mapping of common port numbers to their associated services.
 * It uses a static approach for simplicity in this learning project.
 * 
 * @author Elodie Moisan
 * @version 1.0
 */

public class ServiceIdentifier{

    /**
    *Identifies the service typically associated with a given port number 
    *
    *@param port The port number to identify (0-65535)
    *@return The service name, or "Unknown" if port is not recognized
    **/

    public static String identifyService(int port){
        return switch (port) {
            //File Transfer
            case 20 -> "FTP-DATA";
            case 21 -> "FTP"; //File Transfer Protocol
            case 22 -> "SSH"; //Secure Shell
            case 23 -> "Telnet";
            
            //Email
            case 25 -> "SMTP";
            case 110 -> "POP3";
            case 143 -> "IMAP";
            case 465 -> "SMTPS";
            case 587 -> "SMTP-Submission";
            case 993 -> "IMAPS";
            case 995 -> "POP3S";

            //Web
            case 80 -> "HTTP";
            case 443 -> "HTTPS";
            case 8080 -> "HTPP-Proxy";
            case 8443 -> "HTTPS-Alt";

            //DNS & Network
            case 53 -> "DNS";
            case 67 -> "DHCP-Server";
            case 68 -> "DHCP-Client";
            case 123 -> "NTP";
            case 161 -> "SNMP";
            case 162 -> "SNMP-Trap";

            //Remote Access
            case 3389 -> "RDP";
            case 5900 -> "VNC";
            case 5901 -> "VNC-1";

            //Databases
            case 1433 -> "MS-SQL";
            case 1521 -> "Oracle";
            case 3306 -> "MySQL";
            case 5432 -> "PostgreSQL";
            case 27017 -> "MongoDB";
            case 6379 -> "Redis";

            //Application Servers
            case 8000 -> "HTTP-Alt";
            case 8888 -> "HTTP-Alt";
            case 9000 -> "HTTP-Alt";
            case 9090 -> "HTTP-Alt";

            //Other Common Services 
            case 137 -> "NetBIOS-NS";
            case 138 -> "NetBIOS-DGM";
            case 139 -> "NetBIOS-SSN";
            case 445 -> "SMB";
            case 514 -> "Syslog";
            case 1194 -> "OpenVPN";
            case 3000 -> "Node.js-Dev";
            case 4444 -> "Metasploit";
            case 5000 -> "UPnP/Flask";
            case 5222 -> "XMPP-Client";
            case 5269 -> "XMPP-Server";
            case 6667 -> "IRC";
            case 8081 -> "HTTP-Alt";
            case 8082 -> "HTTP-Alt";
            case 9200 -> "Elasticsearch";
            case 9300 -> "Elasticsearch-Cluster";

            //Default case
            default -> "Unknown";
        };
    }

    /**
     * Checks if a port number is valid (0-65535)
     * 
     * @param port The port number to validate
     * @return true if valid, false otherwise
    */

    public static boolean isValidPort(int port){
        return port >= 0 && port <= 65535;
    }

    /**
     * Gets the category of the port (Well-known, Registered, or Dynamic)
     * 
     * @param port The port number
     * @return The category as a String
     */

    public static String getPortCategory(int port){
        if(port >= 0 && port <= 1023){
            return "Well-known";
        } else if (port >= 1024 && port <= 49151){
            return "Registered";
        } else if (port >= 49152 && port <= 65535){
            return "Dynamic";
        } else {
            return "Invalid";
        }
    }

}