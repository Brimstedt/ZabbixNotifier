<?php
require_once 'config/config.php';
require_once 'include/db.php';
@header("Content-Type: text/plain");

global $config;

$db = new db();
$db->connect($config);

if(isset($_REQUEST['registrationId']))
{
	$code = $db->registerDevice($_REQUEST['deviceId'], $_REQUEST['registrationId']);
}

if(isset($_REQUEST['serverHash']))
{
	$code = $db->registerDeviceToServer($_REQUEST['deviceId'], $_REQUEST['serverHash']);
}
echo $code;

?>