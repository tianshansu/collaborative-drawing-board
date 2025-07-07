# Distributed Shared Whiteboard
A real-time collaborative whiteboard system developed using Java RMI, allowing multiple users to draw, type, and interact on a shared canvas from different machines.  
Users can draw shapes, input text, chat in real time, and manage access — making it ideal for remote collaboration, teaching, or brainstorming.


## Features
### Basic
- Multi-user whiteboard shared over a network
- Real-time drawing of lines, rectangles, ovals, triangles
- Freehand drawing and erasing (with size options)
- Text input anywhere on canvas
- Color selection with at least 16 predefined options

### Advanced
- In-app chat window (text-based messaging between users)
- “File” menu with `New`, `Open`, `Save`, `Save As`, and `Close` (manager only)
- Manager can approve/reject join requests
- Manager can kick out any peer
- Display of online user list in real time
- Automatic synchronization of whiteboard state for new joiners
- When a user is drawing, a colored dot appears next to their name in the user list


## Technologies Used

- Java RMI (Remote Method Invocation)
- Java Swing (GUI components)
- Multi-threading for concurrent operations
- Custom protocol for state synchronization and user management

### Start as Manager
java -jar CreateWhiteBoard.jar &lt;serverIP&gt; &lt;port&gt; &lt;username&gt;

### Join as Peer
java -jar JoinWhiteBoard.jar &lt;serverIP&gt; &lt;port&gt; &lt;username&gt;
