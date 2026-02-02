# 🔍 Java Port Scanner

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.java.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Status](https://img.shields.io/badge/Status-Active-success.svg)]()

A lightweight TCP port scanner written in Java for educational purposes. This project demonstrates network programming concepts, socket operations, and cybersecurity reconnaissance techniques.

![Port Scanner Demo](examples/scan_localhost_success.png)

---

## Table of Contents

- [About](#about)
- [Features](#features)
- [Architecture](#architecture)
- [Installation](#installation)
- [Usage](#usage)
- [Examples](#examples)
- [Technical Details](#technical-details)
- [Legal Disclaimer](#legal-disclaimer)
- [Learning Outcomes](#learning-outcomes)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [Author](#author)

---

## About

This port scanner was developed as a hands-on learning project to explore:

- **Network Programming**: Understanding TCP/IP protocols and socket programming
- **Cybersecurity Fundamentals**: Learning reconnaissance techniques used in penetration testing
- **Object-Oriented Design**: Implementing clean architecture with separation of concerns
- **Exception Handling**: Managing network timeouts, connection failures, and edge cases

The scanner uses the **TCP Connect** scanning technique, establishing full three-way handshakes to detect open ports on target systems.

---

## ✨ Features

### Current (v1.0)

- ✅ **TCP Connect Scan**: Full connection establishment to detect open ports
- ✅ **Service Identification**: Automatically identifies 50+ common services (HTTP, SSH, MySQL, etc.)
- ✅ **Configurable Port Ranges**: Scan specific ranges (e.g., 1-1024, 80-443)
- ✅ **Real-time Progress**: Live updates during scanning with progress indicators
- ✅ **Response Time Measurement**: Records connection time for each port
- ✅ **Detailed Statistics**: Comprehensive scan summary with timing and results
- ✅ **Input Validation**: Robust argument parsing with helpful error messages
- ✅ **Timeout Management**: 2-second timeout per port to prevent hanging

### Planned (v2.0+)

- 🔜 **Multi-threaded Scanning**: Parallel port testing for 10-100x speed improvement
- 🔜 **Banner Grabbing**: Service version detection
- 🔜 **Export Formats**: HTML, CSV, and JSON report generation
- 🔜 **Scan Profiles**: Predefined port sets (web servers, databases, etc.)
- 🔜 **GUI Interface**: JavaFX-based graphical interface

---

## Architecture

The project follows clean architecture principles with clear separation of concerns:
```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                   │
│                   (Main.java)                          │
│  • CLI argument parsing                                │
│  • User interaction                                    │
│  • Output formatting                                   │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────┴───────────────────────────────────────┐
│                     Business Logic                      │
│        (PortScanner.java, PortChecker.java)           │
│  • Scan orchestration                                  │
│  • Network connectivity testing                        │
│  • Progress tracking                                   │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────┴───────────────────────────────────────┐
│                      Data Layer                         │
│      (ScanResult.java, ServiceIdentifier.java)        │
│  • Result storage                                      │
│  • Service identification                              │
│  • Data validation                                     │
└─────────────────────────────────────────────────────────┘
```

### Class Responsibilities

| Class | Purpose | Key Methods |
|-------|---------|-------------|
| **Main** | Entry point, CLI handling | `main()`, `validateArguments()` |
| **PortScanner** | Scan orchestration | `scan()`, `displayResults()` |
| **PortChecker** | TCP connection testing | `checkPort()`, `isPortOpen()` |
| **ScanResult** | Result data object | Getters, `toString()` |
| **ServiceIdentifier** | Port-to-service mapping | `identifyService()`, `isValidPort()` |

---

## 🚀 Installation

### Prerequisites

- **Java 8+** installed ([Download Java](https://www.oracle.com/java/technologies/downloads/))
- **Git** (optional, for cloning)

### Setup

1. **Clone the repository**
```bash
   git clone https://github.com/elmoisan/PortScanner.git
   cd PortScanner
```

2. **Compile the source code**
```bash
   cd src
   javac *.java
```

3. **Verify compilation**
```bash
   # No output = successful compilation
   ls *.class
   # Should show: Main.class, PortScanner.class, etc.
```

---

## 📖 Usage

### Basic Syntax
```bash
java Main <host> [port-range]
```

### Arguments

- `<host>`: **Required** - Target hostname or IP address
- `[port-range]`: **Optional** - Port range in format `start-end` (default: 1-1024)

### Command Examples

**Scan default ports (1-1024) on localhost:**
```bash
java Main localhost
```

**Scan specific port range:**
```bash
java Main 192.168.1.1 1-100
```

**Scan common web ports:**
```bash
java Main example.com 80-443
```

**Scan single port:**
```bash
java Main scanme.nmap.org 22-22
```

---

## Examples

### Example 1: Scanning Local Web Server

**Setup:**
```bash
# Terminal 1: Start a local web server
python3 -m http.server 8080
```

**Scan:**
```bash
# Terminal 2: Scan for the server
java Main localhost 8000-9000
```

**Output:**
```
╔════════════════════════════════════╗
║     Simple Port Scanner v1.0      ║
╚════════════════════════════════════╝

⚖️  Legal Warning:
Only scan systems you own or have permission to test.
Unauthorized scanning may be illegal.

Target: localhost
Port range: 8000-9000
Timeout: 2000ms

🔍 Starting scan...

PORT    STATE    SERVICE              TIME
────────────────────────────────────────────────
8080     OPEN     HTTP-Proxy           (5ms)
Progress: 1001/1001 (100%)

════════════════════════════════════════
           SCAN SUMMARY
════════════════════════════════════════
Scan completed in 25 seconds
Total ports scanned: 1001
Open ports found: 1
════════════════════════════════════════
```

![Localhost Scan](examples/scan_localhost_success.png)

---

### Example 2: Scanning Remote Server
```bash
java Main scanme.nmap.org 20-80
```

**Output:**
```
PORT    STATE    SERVICE              TIME
────────────────────────────────────────────────
22       OPEN     SSH                  (245ms)
80       OPEN     HTTP                 (238ms)
```

![Remote Scan](examples/scan_remote_server.png)

---

### Example 3: No Open Ports Found
```bash
java Main localhost 9000-9010
```

**Output:**
```
Progress: 11/11 (100%)

════════════════════════════════════════
           SCAN SUMMARY
════════════════════════════════════════
 Scan completed in 3 seconds
Total ports scanned: 11
Open ports found: 0

 No open ports found in the specified range
════════════════════════════════════════
```

![No Results](examples/scan_no_results.png)

---

### Example 4: Error Handling

**Invalid port range:**
```bash
java Main localhost 100-1
```

**Output:**
```
 Error: Start port must be less than or equal to end port
```

**Missing arguments:**
```bash
java Main
```

**Output:**
```
Usage: java Main <host> [port-range]

Arguments:
  <host>         Target hostname or IP address
  [port-range]   Port range in format: start-end (optional, default: 1-1024)

Examples:
  java Main localhost
  java Main 192.168.1.1 1-100
  java Main scanme.nmap.org 20-25
```

![Error Handling](examples/usage_help.png)

---

## 🔬 Technical Details

### Scanning Technique

This scanner uses the **TCP Connect** scan method:

1. **SYN** packet sent to target port
2. Target responds with **SYN-ACK** (open) or **RST** (closed)
3. Full three-way handshake completed for open ports
4. Connection immediately closed after confirmation
```
Scanner                    Target
   |                          |
   |-------- SYN ----------->|
   |                          |
   |<------- SYN-ACK --------|  (Port OPEN)
   |                          |
   |-------- ACK ----------->|
   |                          |
   |-------- FIN ----------->|
   |                          |
```

### Why TCP Connect?

| Aspect | Advantage | Limitation |
|--------|-----------|------------|
| **Simplicity** | ✅ No raw sockets needed | ❌ Easily detectable |
| **Reliability** | ✅ 100% accurate for open ports | ❌ Slower than SYN scan |
| **Permissions** | ✅ No root/admin required | ❌ Logs full connections |
| **Cross-platform** | ✅ Works everywhere | ❌ Cannot distinguish filtered ports |

### Performance

**Single-threaded (v1.0):**
- ~2 seconds per port (with 2s timeout)
- 100 ports ≈ 3-5 minutes
- 1000 ports ≈ 30-35 minutes

**Planned multi-threaded (v2.0):**
- ~0.02 seconds per port (100 threads)
- 100 ports ≈ 5-10 seconds
- 1000 ports ≈ 30-60 seconds

---

## Legal Disclaimer

### IMPORTANT - READ BEFORE USE

This tool is designed **exclusively for educational purposes** and authorized security testing.

** LEGAL USE:**
- Scanning your own devices
- Scanning systems you own or control
- Authorized penetration testing with written permission
- Educational labs and test environments (e.g., scanme.nmap.org)

** ILLEGAL USE:**
- Scanning systems without explicit authorization
- Reconnaissance for malicious purposes
- Violating terms of service or acceptable use policies
- Any activity that violates local, state, or federal laws

** WARNING:** Unauthorized port scanning may be considered:
- Computer intrusion (CFAA in the US)
- Violation of terms of service
- Network abuse
- Preparation for cyber attacks

**The author assumes NO responsibility for misuse of this tool. Users are solely responsible for compliance with all applicable laws and regulations.**

### Recommended Test Target

For safe, legal testing: **scanme.nmap.org**

This is a publicly available server specifically set up by Nmap for testing scanners.
```bash
java Main scanme.nmap.org 1-1024
```

---

## 🎓 Learning Outcomes

Through building this project, I gained practical experience with:

### Java Programming
- ✅ Object-oriented design patterns (separation of concerns)
- ✅ Exception handling for network operations
- ✅ Java Collections Framework (ArrayList, Maps)
- ✅ Date/Time API (LocalDateTime, DateTimeFormatter)
- ✅ String formatting and manipulation

### Network Programming
- ✅ Socket programming (java.net.Socket)
- ✅ TCP/IP protocol fundamentals
- ✅ Three-way handshake mechanism
- ✅ Timeout and connection management
- ✅ Network exception handling

### Cybersecurity Concepts
- ✅ Port scanning techniques and methodology
- ✅ Service enumeration and fingerprinting
- ✅ Network reconnaissance ethics
- ✅ Detection and evasion considerations
- ✅ Security testing workflows

### Software Engineering
- ✅ Git version control and GitHub workflow
- ✅ Project documentation (README, comments)
- ✅ Command-line interface design
- ✅ Error handling and input validation
- ✅ Code organization and modularity

---

## Roadmap

### Version 1.0 (Current) ✅
- [x] Basic TCP connect scanning
- [x] Service identification
- [x] CLI interface
- [x] Progress indicators
- [x] Error handling

### Version 2.0 (Planned)
- [ ] Multi-threaded scanning (100 concurrent threads)
- [ ] Banner grabbing for version detection
- [ ] HTML report generation
- [ ] Scan profiles (quick, full, web, database)
- [ ] Rate limiting to avoid overwhelming targets

### Version 3.0 (Future)
- [ ] JavaFX GUI interface
- [ ] Scan history and comparison
- [ ] Export to CSV, JSON, XML
- [ ] Advanced service detection
- [ ] Vulnerability suggestions based on open ports

---

## Contributing

This is a personal learning project, but feedback and suggestions are welcome!

If you find bugs or have ideas for improvements:

1. Open an issue describing the problem/suggestion
2. For bugs, include:
   - Java version
   - Operating system
   - Command used
   - Error message or unexpected behavior

---

## Project Structure
```
PortScanner/
│
├── src/                           # Source code
│   ├── Main.java                 # Entry point and CLI
│   ├── PortScanner.java          # Scan orchestrator
│   ├── PortChecker.java          # Port testing logic
│   ├── ScanResult.java           # Result data object
│   ├── ServiceIdentifier.java    # Service mapping
│   └── TestClasses.java          # Unit tests
│
├── examples/                      # Screenshots and examples
│   ├── scan_localhost_success.png
│   ├── scan_remote_server.png
│   ├── scan_no_results.png
│   ├── usage_help.png
│   └── error_invalid_range.png
│
├── docs/                          # Additional documentation
│   └── (future: architecture diagrams, API docs)
│
├── README.md                      # This file
├── .gitignore                     # Git ignore rules
└── LICENSE                        # MIT License
```

---

## Author

**Elodie Moisan**

- GitHub: [@elmoisan](https://github.com/elmoisan)
- Project: Learning Java and cybersecurity fundamentals
- Status: Computer Science Student

---

## License

This project is licensed under the MIT License - see below for details:
```
MIT License

Copyright (c) 2026 Elodie Moisan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

## Acknowledgments

- **Nmap Project**: For inspiration and providing scanme.nmap.org for testing
- **OWASP**: For cybersecurity education resources
- **Java Documentation**: For comprehensive API references

---

## Statistics

![Code Size](https://img.shields.io/github/languages/code-size/elmoisan/PortScanner)
![Lines of Code](https://img.shields.io/tokei/lines/github/elmoisan/PortScanner)
![Last Commit](https://img.shields.io/github/last-commit/elmoisan/PortScanner)

---

** If you found this project helpful for learning, consider giving it a star!**

---

*Built with for learning and cybersecurity education*
