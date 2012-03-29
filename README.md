ZabbixNotifier
==============

Overview
--------

ZabbixNotifier is a C2DM (Push) notification system for Zabbix to push messages to Android devices.

It consists of three parts:

*  Android client application
*  Zabbix alert.d script
*  Server application

People who wish to use ZabbixNotifier needs only the alert.d script and the Android client. Configuring your
own server is optional since a public server is available. Using your own server currently requires recompilation of the 
Client.

Android Client
--------------

The client will be available in Play market as soon as version 1.0 is reached.

The client is used to register for C2DM (Push) notifications and display more detailed information when they are received.

When registering the client for a server, you get an id that you need to enter into your Zabbix user account.

No information about the zabbix installation or any personal details are sent to the server, except for the phone's 
unique id and it's key for C2DM messaging. When alerts arise, the alert message is sent as well.

Neither the zabbix server or the ZabbixNotifier client needs to send anything else than hashed id's to the ZabbixNotifierServer.

Agent.d Script
--------------

The agent.d script currently requires wget.

It is used to notify the ZabbixNotifierServer that a message should be sent to a user.

Server application
------------------

The server application ZabbixNotifierServer is written in PHP. It requires a database server, currently only
tested with MySQL, but the SQL used is very basic and most likely supported by any vendor.

Currently only device Id, C2DM key and server hash is saved in the database.

See the database creation script for columns used.
