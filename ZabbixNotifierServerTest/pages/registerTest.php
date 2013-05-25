<?php

require_once "testsetup.php";
require_once "dbTestCase.php";

class RegisterTest extends dbTestCase
{
	var $db;
	
	public function testRegister_Device()
	{
		$deviceId = 'ABCDEFGHIJKLMNOP';
		$registrationId = '1234567';
		 
		$_REQUEST = array(
				'deviceId' => $deviceId
				,	'registrationId' => $registrationId
		);
		 
		include('register.php');
		 
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Did not get the correct registration id for device');
	}
	
	
	public function testRegister_Device_then_Device_Server()
	{
		$deviceId = 'ABCDEFGHIJKLMNOP';
		$serverHash = '12345678901234567890123456789012';
		$registrationId = '1234567';
	
		$err = $this->db->registerDevice($deviceId, $registrationId);
		$this->assertEquals("", $err, "Expected no error");
		
		$_REQUEST = array(
				'deviceId' => $deviceId
				,	'serverHash' => $serverHash
		);
			
		include('register.php');
			
		$this->assertTrue($this->db->isDeviceRegistererdForServer($deviceId, $serverHash), 'device was not registered with server');
	}
	
	
public function testRegister_Device_Server_without_Device()
	{
		$deviceId = 'ABCDEFGHIJKLMNOP';
		$serverHash = '12345678901234567890123456789012';
	
		$err = $this->db->registerDeviceToServer($deviceId, $serverHash);
	
		$this->assertEquals("", $err, "Expected no error");
		$this->assertTrue($this->db->isDeviceRegistererdForServer($deviceId, $serverHash), 'device was not registered with server');
		$this->assertEquals("", $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
	
	}
	
	public function testRegister_Device_Server_several_times()
	{
		$deviceId = 'Milo';
		$serverHash = 'Beautiful';
		$registrationId = 'Boy';
	
		$err = $this->db->registerDeviceToServer($deviceId, $serverHash);	
		$this->assertEquals("", $err, "Expected no error");

		$err = $this->db->registerDeviceToServer($deviceId, $serverHash);
		$this->assertEquals("", $err, "Expected no error");
		
		$err = $this->db->registerDeviceToServer($deviceId, $serverHash);
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals("", $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');

		
		
		$err = $this->db->registerDevice($deviceId, $registrationId);
		$this->assertEquals("", $err, "Expected no error");
		
		$err = $this->db->registerDeviceToServer($deviceId, $serverHash);
		$this->assertEquals("", $err, "Expected no error");
		
		$this->assertTrue($this->db->isDeviceRegistererdForServer($deviceId, $serverHash), 'device was not registered with server');
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
	
	}
	
	
	public function testRegister_Device_many_times()
	{
		$deviceId = 'ABCDEFGHIJKLMNOP';
		$registrationId = 'xyz';
	

		$err = $this->db->registerDevice($deviceId, "");
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals("", $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
		
		$err = $this->db->registerDevice($deviceId, $registrationId);
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
		
		$err = $this->db->registerDevice($deviceId, "");
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
		
	}
	
	public function testRegister_Device_empty_registrationId_does_not_override_real_registrationId()
	{
		$deviceId = 'ABCDEFGHIJKLMNOP';
		$registrationId = 'xyz';
	
	
		$err = $this->db->registerDevice($deviceId, $registrationId);
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
	
		$err = $this->db->registerDevice($deviceId, "");
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
	
	}
	
	public function testRegister_Device_new_registrationId_overrides_old_registrationId()
	{
		$deviceId = 'Katarina';
		$registrationId = 'Wonderful';
		$registrationIdNew = 'Woman';
	
	
		$err = $this->db->registerDevice($deviceId, $registrationId);
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
	
		$err = $this->db->registerDevice($deviceId, $registrationIdNew);
		$this->assertEquals("", $err, "Expected no error");
		$this->assertEquals($registrationIdNew, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
	
	}
	
	
	public function testRegister_Device_Server_without_Device_then_register_Device()
	{
		$deviceId = 'ABCDEFGHIJKLMNOP';
		$serverHash = '12345678901234567890123456789012';
		$registrationId = '1234567';
		
		$err =  $this->db->registerDeviceToServer($deviceId, $serverHash);
		$this->assertEquals("", $err, "Expected no error");
		
		$this->assertTrue($this->db->isDeviceRegistererdForServer($deviceId, $serverHash), 'device was not registered with server');
		$this->assertEquals("", $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd without registration id');
	
		$err = $this->db->registerDevice($deviceId, $registrationId);
		$this->assertEquals("", $err, 'Expected no registration error');
		
		$this->assertEquals($registrationId, $this->db->getDeviceRegistrationId($deviceId), 'Expected device to be registerd with correct id');
	}
	
	
	public function testRegisterSentMessage()
	{
		$deviceId = 'ABCDEFGHIJKLMNOP';
		$serverHash = '12345678901234567890123456789012';
		$registrationId = '1234567';
		
		$this->db->registerDevice($deviceId, $registrationId);
		$this->db->registerDevicetoServer($deviceId, $serverHash);
		
		$this->assertTrue($this->db->isDeviceRegistererdForServer($deviceId, $serverHash), 'device was not registered with server');
		
		$this->assertTrue($this->db->registerSentMessage($deviceId, $serverHash), 'message count updating failed');
		
		$this->assertEquals(1, $this->db->getMessageCount($deviceId, $serverHash), 'message count was not updated');
		sleep(1);		
		$this->assertTrue($this->db->registerSentMessage($deviceId, $serverHash), 'message count updating failed');
		$this->assertEquals(2, $this->db->getMessageCount($deviceId, $serverHash), 'message count was not updated as expected');
		$this->assertQueryReturnsRows(1, "SELECT 1 FROM device_server WHERE deviceId = '{$deviceId}' AND serverHash = '{$serverHash}' AND registrationDate < lastMessage", 'Expected registrationDate to be earlier than lastmessage');	
		
	}
	
}
?>