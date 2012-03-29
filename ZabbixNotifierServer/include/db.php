
<?php
//$dbh = new PDO('mysql:host=localhost;dbname=zabbixnotifier', "root", "");
$dbh = new PDO('mysql:host=mysql.xxx.se;dbname=zabbixnotifier', "", "");



function registerDevice($deviceId, $registrationId)
{
	global $dbh;
	$stmt = $dbh->prepare("
			INSERT INTO device (deviceId, registrationId, registrationDate) 
			VALUES (:deviceId, :registrationId, now())");
	$stmt->bindParam(':deviceId', $deviceId);
	$stmt->bindParam(':registrationId', $registrationId);
	$stmt->execute();
	var_dump($stmt->errorInfo());
	}
	
	function registerDeviceToServer($deviceId, $serverHash)
	{
		global $dbh;
		$stmt = $dbh->prepare("
				INSERT INTO device_server (deviceId, serverHash)
				VALUES (:deviceId, :serverHash)");
		$stmt->bindParam(':deviceId', $deviceId);
		$stmt->bindParam(':serverHash', $serverHash);
		$stmt->execute();
		var_dump($stmt->errorInfo());
	}
	
	
	function getDeviceRegistrationId($deviceId)
	{
		global $dbh;
		$stmt = $dbh->prepare("
				SELECT 	registrationId
				FROM	device d
				WHERE	d.deviceId = :deviceId
				");
		$stmt->bindParam(':deviceId', $deviceId);
		$stmt->execute();
		var_dump($stmt->errorInfo());
		$row = $stmt->fetch();
		var_dump($row);
		return $row['registrationId'];
	}
?>
