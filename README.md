# SampleApp-Webhooks-Java
SampleApp-Webhooks-Java

<p>Welcome to the Intuit Developer's Webhooks Java Sample App.</p>
<p>This sample app is meant to provide working examples of how to integrate your app with the Intuit Small Business ecosystem.  Specifically, this sample application demonstrates the following:</p>

<ul>
	<li>Implementing webhooks endpoint to receive event notifications.</li>
	<li>Best practices to be followed while processing the event notifications.</li>
	<li>Sample code using QuickBooks Online SDK to call CDC API to sync data between the app and the QuickBooks Online company.</li>
</ul>

<p>Please note that while these examples work, features not called out above are not intended to be taken and used in production business applications. In other words, this is not a seed project to be taken cart blanche and deployed to your production environment.</p>  

<p>For example, certain concerns are not addressed at all in our samples (e.g. security, privacy, scalability). In our sample apps, we strive to strike a balance between clarity, maintainability, and performance where we can. However, clarity is ultimately the most important quality in a sample app.</p>

<p>Therefore there are certain instances where we might forgo a more complicated implementation (e.g. caching a frequently used value, robust error handling, more generic domain model structure) in favor of code that is easier to read. In that light, we welcome any feedback that makes our samples apps easier to learn from.</p>

## Table of Contents

* [Requirements](#requirements)
* [First Use Instructions](#first-use-instructions)
* [Running the code](#running-the-code)
* [Configuring the endpoint](#configuring-the-endpoint)
* [Project Structure](#project-structure)
* [Reset the App](#reset-the-app)


## Requirements

In order to successfully run this sample app you need a few things:

1. Java 1.8
2. A [developer.intuit.com](http://developer.intuit.com) account
3. An app on [developer.intuit.com](http://developer.intuit.com) and the associated app token, consumer key, and consumer secret.
4. QuickBooks Java SDK (already included in the [`lib`](lib) folder) 
5. Two sandbox companies, connect both companies with your app and generate the oauth tokens.
 
## First Use Instructions

1. Clone the GitHub repo to your computer
2. In [`config.properties`](src/main/resources/config.properties), set oauth.type as 1 or 2 depending on type of app you have. For OAuth2 apps set value as 2.
3. For OAuth2 apps, fill in the [`config.properties`](src/main/resources/config.properties) file values (companyid, oauth2.accessToken).
4. For OAuth1 apps, fill in the [`config.properties`](src/main/resources/config.properties) file values (companyid, app token, consumer key, consumer secret, access token key, access token secret). 
5. Also add webhooks subscribed entities and webhooks verifier token that was generated when you subscribed for webhoooks event.

## Running the code

Once the sample app code is on your computer, you can do the following steps to run the app:

1. cd to the project directory</li>
2. Run the command:`./gradlew bootRun` (Mac OS) or `gradlew.bat bootRun` (Windows)</li>
3. Wait until the terminal output displays the "Started Application in xxx seconds" message.
4. Open your browser and go to http://localhost:8080/companyConfigs - This will list the companies in the repository for which you have subscribed event notification.
5. The webhooks endpoint in the sample app is http://localhost:8080/webhooks
6. Once an event notification is received and processed, you can perform step 4 to see that the last updated timestamp has been updated for the realmId for which notification was received.
7. To run the code on a different port, uncomment and update server.port property in application.properties

## Configuring the endpoint

Webhooks requires your enpoint to be exposed over the internet. The easiest way to do that while you are still developing your code locally is to use [ngrok](https://ngrok.com/). Here are the steps to configure ngrok

1. Download and install ngrok
2. Expose your localhost by running "./ngrok http 8080" on the command line. 
3. You will then get a forwarding url that looks something like this:
    Forwarding     https://cb063e9f.ngrok.io -> localhost:8080  
(Remember to use only https url and not the http url for webhooks)
This will expose localhost:8080 to the Internet. Your endpoint url will now be https://cb063e9f.ngrok.io/webhooks
Copy this url and use it for setting up webhooks on developer.intuit.com for your app. 

## Project Structure
* **Standard Java coding structure is used for the sample app**

* Java code is located in the [`src.main.java`](src/main/java) directory
	*  Controller classes are in under the controller folder:
        - [`WebhooksController.java`](src/main/java/com/intuit/developer/sampleapp/webhooks/controllers/WebhooksController.java)
        - [`CompanyController.java`](src/main/java/com/intuit/developer/sampleapp/webhooks/controllers/CompanyController.java)
    *  Queue implementation and processing classes are in under the service/queue folder:
        - [`QueueService.java`](src/main/java/com/intuit/developer/sampleapp/webhooks/service/queue/QueueService.java)
        - [`QueueProcessor.java`](src/main/java/com/intuit/developer/sampleapp/webhooks/service/queue/QueueProcessor.java)
	*  Encryption and payload validation implementation class is in the service/security folder:
        - [`SecurityService.java`](src/main/java/com/intuit/developer/sampleapp/webhooks/service/security/SecurityService.java)
	*  QBO API Service calls are implemented in the service/qbo folder:
        - [`CDCService.java`](src/main/java/com/intuit/developer/sampleapp/webhooks/service/qbo/CDCService.java)
        - [`QueryService.java`](src/main/java/com/intuit/developer/sampleapp/webhooks/service/qbo/QueryService.java)

* Property files are located in the [`src.main.resources`](src/main/resources) directory
* JUnit test files are located in the [`src.test.java`](src/test/java) directory

## Reset the App

This app uses an in-memory temporary H2 database. The tables are loaded during startup with realmId and oauth tokens. The table is read and updated when webhooks notification is processed. Stopping the server
will delete the records.
The oauth tokens are encrypted and stored in the database. There is a sample encryption implementation provided using fake keys. For production use real keys, this can be updated in application.properties
