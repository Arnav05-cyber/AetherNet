# AetherNet Game Engine

A high-performance, asynchronous multiplayer networking engine built with Java and Netty. AetherNet implements a custom binary protocol designed for real-time state synchronization in multiplayer games.

## 🚀 Overview

AetherNet follows a modular architecture to separate core networking logic from game-specific implementations. It uses a Binary Packet System instead of traditional JSON/Text protocols to ensure minimal latency and reduced bandwidth usage.

## 🏗 Architecture

The project is divided into three main Maven modules:

* **aether-common**: The shared dictionary. Contains the `Packet` interface, custom Encoders/Decoders, and all packet definitions (e.g., `WelcomePacket`, `PlayerMovePacket`).
* **aether-server**: The "Brain." Handles multiple concurrent connections, manages the global `GameWorld` state, and broadcasts updates to all connected clients.
* **aether-client**: The "Eyes." Connects to the server, decodes incoming state updates, and handles local player input.

## 🛠 Features

* **Asynchronous I/O**: Powered by Netty's NIO (Non-blocking I/O) `EventLoopGroup`.
* **Custom Binary Protocol**: Packets are serialized/deserialized manually using `ByteBuf` for maximum speed.
* **Broadcasting System**: Real-time relay of player movements to all connected peers.
* **Concurrent World State**: Thread-safe management of player positions using `ConcurrentHashMap`.

## 🚦 Getting Started

### Prerequisites

* Java 17 or higher
* Maven 3.x
* IntelliJ IDEA (Recommended)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/Arnav05-cyber/AetherNet.git
   ```
2. Open the project in your IDE as a Maven project.
3. Run the clean install command:
   ```bash
   mvn clean install
   ```

### Running the Engine

* **Start the Server**: Run `AetherServer.java` (Default port: 40000).
* **Start the Client**: Run `AetherClient.java`. (To test multiplayer, enable "Allow multiple instances" in your IDE and run a second client).

## 📡 Packet Structure

Every packet in AetherNet follows this binary format:

| Field | Type | Description |
| :--- | :--- | :--- |
| Packet ID | int (4 bytes) | Unique identifier for the packet type. |
| Data Payload | variable | The actual content (Strings, Floats, etc). |

## 🗺 Roadmap

* [x] Basic TCP Handshake
* [x] Binary Encoder/Decoder
* [x] Movement Broadcasting
* [ ] Unique Player UUIDs
* [ ] OpenGL/LWJGL Visual Integration
* [ ] Heartbeat/Keep-Alive Packets

## 📄 License

This project is licensed under the MIT License.
