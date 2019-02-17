# NFSee

## Prompt

Taken from the Calgary Hacks 2019 event (https://calgaryhacks-2019.devpost.com/), the challenge is to use IoT/AI to make sustainable smart cities

## Problem Statement
 
In a smart city, information is key.  Information and knowledge both being transmitted and received.  Convenience to access such information is what truly leads a city to become integrated to the internet.  Information connected to convivence is the main idea of our solution.  Being able to both send and receive information with the simple tap of your phone. 
 
Ladies and gentlemen, we present you NFSee, the latest in digital information transmission, and IoT. 

Utilizing NFC technology, we have created a application that is capable of taking a local NFC tag, and converting it into comprehensible data about the surrounding location.  What is so special about this technology is that mostmodern western phones all have NFC built in, and NFC technology is cheap, meaning our product is cheap and easy to mass produce.

## Product Demonstration

During the 24 hours we were given, we have made a functioning app that is able to scan our identified and protected NFC tags.  Once it has scanned it, the app will send information to our server, which communicates with our database.  From that, the server retrieves the info, and sends it back to the app.  The app then displays the information.

For example, say you are new to the new “smart Calgary” and you want to know what Tim Hortons is.  You would have the app, and simply tap the nfc tag at the store.

The app then pulls up our standard food template, with the appropriate info previously entered by the people who set up the NFC tag here.

Once the information has been pulled, we can see the menu, basic information such as the website, phone number, opening hours, social media, and, to help those looking for jobs, any available job listings.

## Technology Used
* Android Studio
* Java
* JavaScript
* NodeJS
* MongoDB
* Express
* HTTP
* NFC
