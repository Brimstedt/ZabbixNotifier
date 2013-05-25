<?php
/**
 *
 * @package zabbixNotifierServer
 * @version $Id$
 * @copyright (c) 2012 Brimstedt Technology
 * @abstract Sets up database
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */


require_once('config/config.php');
require_once('include/db.php');

header("Content-Type: text/plain");

// Set up database
$db = new DB();
$db->connect($config);
if(!$db->isConnected())
{
	die("Unable to connect to db. Check config");
}

$dbVersion = $db->getDbVersion();
print "Database is at version: {$dbVersion}\n";

if($dbVersion >= DB_VERSION)
{
	die("Database is up to date");
}

print "Upgrading from: {$dbVersion} to " . DB_VERSION . "\n";

if($db->initiateDb())
{
	print "Done\n";
	
	$dbVersion = $db->getDbVersion();
	print "Database is at version: {$dbVersion}\n";
	die();
}

$dbVersion = $db->getDbVersion();

print "Failed setting up database :(\n";
print "Database is now at version: {$dbVersion}\n";


