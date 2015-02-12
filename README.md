# SoundPlay

## Description
A Java program for voice chat over the internet or a network.

SoundPlay is built and tested in NetBeans IDE 7.4.

## Features
* Optional server hosting with the click of a button
* Auto-find servers on LAN
* Microphone/speaker select
* Message box (text chat)

## To be added
* Encoding/decoding to save bandwidth and possibly CPU. Current bandwidth usage is unacceptably large with 100-200KBps up/down per voice.
* Secure Identity configuration - Server should know who is who, no matter what name they choose.
* Online List / Event updates - to be maintained by server, send to client. Similar to IRC
* Fix Server window's client list - bugs out when multiple connections exist
* Hotkey support for Push-to-Talk through a JNI (Java Native Interface). Currently push-to-talk is only supported inside the config/test window.
Downside to this is that the program will lose portability to all platforms but the one the JNI is used for.

## Challenges Faced and Technologies Used
* Configuring microphones/speakers using Java's Sound API
* Sending/Receiving data through a server using serialization over sockets
* Threading and synchronization
* Autocloseable interface
* Generating sounds for testing purposes
* Mixing multiple unsynchronized received sounds to output a single sound
