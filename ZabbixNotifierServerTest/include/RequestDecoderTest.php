<?php

require_once "testsetup.php";
require_once "include/RequestDecoder.php";

class RequestDecoderTest extends PHPUnit_Framework_TestCase
{
	 
	
    public function testGet_DeviceId_from_request()
    {
		$requestDecoder = new RequestDecoder();
		$recipient = '12345678901234567890123456789012ABCDEFGHIJKL';
		$this->assertEquals('ABCDEFGHIJKL', $requestDecoder->getDeviceIdFromRecipient($recipient), 'Got bad device id from decoder');

    	        
    }
    
    public function testGet_ServerHash_from_request()
    {
    	$requestDecoder = new RequestDecoder();
    	$recipient = '12345678901234567890123456789012ABCDEFGHIJKL';
    	$this->assertEquals('12345678901234567890123456789012', $requestDecoder->getServerHashFromRecipient($recipient), 'Got bad server hash from decoder');
    
    }
}
?>