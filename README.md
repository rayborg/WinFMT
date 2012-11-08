WinFMT
======

File Metadata Timeline Creator for Windows OS 7, XP, Vista

Notes
=====
To compile from source I used Eclipse Juno and you can use the import function:
File‡import‡ existing projects into workspace‡browse‡find folder and select OK
Checkmark ‡Copy projects into workspace

Setup Installer:
The program is a jar file which can be run be using the command ìjava ñjar FMT.jarî  or double clicking on it.

Execution Dependencies:
The Java Virtual Machine should be installed and updated. http://java.com/en/download/index.jsp
java version "1.7.0_05" Java(TM) SE Runtime Environment (build 1.7.0_05-b05)
You can test by using the command ìjava -versionî and getting the above result.

To compile/run from source code: 
Include the external library Java Native Access version 3.4.1.
It is available here: https://github.com/twall/jna/blob/3.4.1/dist/jna.jar?raw=true
Source code here: https://github.com/twall/jna

You also need to be using JDK version 1.7 (7), available here:
http://www.oracle.com/technetwork/java/javase/downloads/java-se-jdk-7-download-432154.html
If you install JDK version 7 that should include JRE version 7 but just in case.
You also need to be using JRE Environment 7, available here:
http://www.oracle.com/technetwork/java/javase/downloads/java-se-jre-7-download-432155.html
