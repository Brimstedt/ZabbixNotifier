<?php
/**
 *
 * @package zabbixNotifierServer
 * @version $Id$
 * @copyright (c) 2012 Brimstedt Technology
 * @abstract Sends a message
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
require_once('include/c2dm.php');
require_once('include/db.php');
require_once('include/requestDecoder.php');
header("Content-Type: text/plain");

$c2dm = new c2dm();

// Decode request params
$requestDecoder = new RequestDecoder();
$recipient = $_REQUEST['recipient'];
$deviceId = $requestDecoder->getDeviceIdFromRecipient($recipient);
$serverHash = $requestDecoder->getServerHashFromRecipient($recipient);

// Get device registration id for recipient
$db = new DB();
$db->connect($config);

$deviceRegistrationId = $db->getDeviceRegistrationId($deviceId);


$message = $_REQUEST['message'];


// Send Message
$c2dm = new c2dm();
$c2dm->getAuthToken($config['c2dmUsername'], $config['c2dmPassword']);
echo $c2dm->sendMessage($deviceRegistrationId, $serverHash, $message);

// Increase messagecount
$db->registerSentMessage($deviceId, $serverHash);

?>