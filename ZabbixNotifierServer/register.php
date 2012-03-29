<?php
require_once 'include/db.php';

if(isset($_REQUEST['registrationid']))
{
	registerDevice($_REQUEST['deviceid'], $_REQUEST['registrationid']);
}

if(isset($_REQUEST['serverHash']))
{
	registerDeviceToServer($_REQUEST['deviceid'], $_REQUEST['serverHash']);
}

#file_put_contents('data/log.txt', serialize($_REQUEST) . "\n", FILE_APPEND);

?>