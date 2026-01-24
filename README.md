
# Simple Port Scanner

A TCP port scanner written in Java for learning networking and cybersecurity concepts.

## About

This project is a simple port scanner that detects open ports on a target machine. It was built as a learning project to understand:
- Network programming in Java (Sockets)
- TCP protocol and port scanning techniques
- Object-oriented programming principles
- Cybersecurity reconnaissance concepts

## Features

- [x] TCP Connect scan for specified port ranges
- [x] Service identification for common ports (HTTP, SSH, FTP, etc.)
- [x] Real-time scan results display
- [x] Scan statistics and summary
- [ ] Multi-threaded scanning (planned for v2)
- [ ] Banner grabbing (planned for v2)
- [ ] HTML report generation (planned for v2)

## Technologies

- **Language:** Java 8+
- **Libraries:** Java standard library only (java.net, java.io)
- **Build Tool:** None (compile with javac)

## 🚀 Installation

### Prerequisites
- Java 8 or higher installed
- Git (to clone the repository)

### Steps

1. Clone the repository:
```bash
git clone https://github.com/elmoisan/PortScanner.git
cd PortScanner
```

2. Compile the source files:
```bash
cd src
javac *.java
```

3. Run the scanner:
```bash
java Main <target-ip> <start-port>-<end-port>
```

## 📖 Usage

### Basic scan
```bash
java Main 192.168.1.1 1-100
```

### Scan common ports
```bash
java Main scanme.nmap.org 1-1024
```

### Scan localhost
```bash
java Main localhost 8000-9000
```

## 📊 Example Output
```
╔════════════════════════════════════╗
║     Simple Port Scanner v1.0      ║
╚════════════════════════════════════╝

Target: 192.168.1.1
Port range: 1-1000
Timeout: 2000ms

Starting scan...

PORT    STATE    SERVICE         
────────────────────────────────────
22      OPEN     SSH             
80      OPEN     HTTP            
443     OPEN     HTTPS           

Scan completed in 45 seconds
3 open ports found out of 1000 scanned
```

## Project Structure
```
PortScanner/
├── src/
│   ├── Main.java                    # Entry point & CLI
│   ├── PortScanner.java             # Scan orchestrator
│   ├── PortChecker.java             # Individual port tester
│   ├── ScanResult.java              # Result data object
│   └── ServiceIdentifier.java       # Service identification
├── examples/
│   └── (screenshots will go here)
├── docs/
│   └── (additional documentation)
├── README.md
└── .gitignore
```

## Legal Disclaimer

**For educational purposes only.**

This tool is designed for learning network programming and cybersecurity concepts. Only scan systems you own or have explicit written permission to test. Unauthorized port scanning may be illegal in your jurisdiction and violate terms of service.

The author is not responsible for misuse of this tool.

## Learning Objectives

This project helped me learn:
- ✅ Socket programming in Java
- ✅ TCP/IP fundamentals and the three-way handshake
- ✅ Exception handling for network operations
- ✅ Object-oriented design patterns
- ✅ Git and GitHub workflow
- ✅ Ethical considerations in cybersecurity

## Contributing

This is a personal learning project, but suggestions are welcome! Feel free to open an issue if you spot bugs or have ideas for improvements.

## License

MIT License - feel free to use this for learning purposes.

## Author

- GitHub: [@elmoisan](https://github.com/elmoisan)
- Project for learning Java and cybersecurity concepts

---

**Status:**Work in Progress - Version 1.0 in development
