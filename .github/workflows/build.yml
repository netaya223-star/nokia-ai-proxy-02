name: Build J2ME JAR

on:
  push:
    branches: [ "main" ]
  workflow_dispatch:

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
    - uses: actions/checkout@v3

    - name: Set up JDK 8
      uses: actions/setup-java@v3
      with:
        java-version: '8'
        distribution: 'temurin'

    - name: Download MicroEmulator Stub Libraries
      run: |
        mkdir -p lib
        curl -sL https://repo1.maven.org/maven2/com/googlecode/microemu/microemu-midp/2.0.4/microemu-midp-2.0.4.jar -o lib/microemu-midp.jar

    - name: Compile Java Class
      run: |
        mkdir -p bin
        javac -cp lib/microemu-midp.jar -d bin ClaudeAI.java

    - name: Create Manifest
      run: |
        echo "MIDlet-1: Claude AI, /icon.png, ClaudeAI" > MANIFEST.MF
        echo "MIDlet-Name: Claude AI" >> MANIFEST.MF
        echo "MIDlet-Vendor: Nokia Dev" >> MANIFEST.MF
        echo "MIDlet-Version: 1.0.0" >> MANIFEST.MF
        echo "MicroEdition-Configuration: CLDC-1.1" >> MANIFEST.MF
        echo "MicroEdition-Profile: MIDP-2.0" >> MANIFEST.MF

    - name: Package JAR
      run: |
        jar cvfm ClaudeAI.jar MANIFEST.MF -C bin .

    - name: Upload JAR Artifact
      uses: actions/upload-artifact@v4
      with:
        name: ClaudeAI-JAR
        path: ClaudeAI.jar
