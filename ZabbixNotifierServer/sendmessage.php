<?php
/**
*
* @package c2dm
* @version $Id$
* @copyright (c) 2011 lytsing.org
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

include_once('include/c2dm.php');
include_once('include/db.php');

$deviceId = substr($_REQUEST['recipient'], 31, 99);
$serverHash = substr($_REQUEST['recipient'], 0, 32);
$deviceRegistrationId = getDeviceRegistrationId($deviceId);

$message = $_REQUEST['message'];

// var_dump($deviceId);
// var_dump($serverHash);
// var_dump($deviceRegistrationId);

$c2dm = new c2dm();
$c2dm->getAuthToken("", "");
echo $c2dm->sendMessage($deviceRegistrationId, $serverHash, $message);

?>