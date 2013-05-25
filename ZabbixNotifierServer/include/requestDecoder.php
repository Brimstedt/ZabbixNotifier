<?php 
/**
 *
 * @package zabbixNotifierServer
 * @version $Id$
 * @copyright (c) 2012 Brimstedt Technology
 * Description: Decodes request parameters
 * 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

class RequestDecoder
{
	
	/**
	 * Extract device id from recipient
	 * 
	 * @param string  $recipient
	 * @return string
	 */
	function getDeviceIdFromRecipient($recipient)
	{
		return substr($recipient, 32, 99);
	}
	
	/**
	 * Extract server hash from recipient
	 * 
	 * @param string $recipient
	 * @return string
	 */
	function getServerHashFromRecipient($recipient)
	{
		return substr($recipient, 0, 32);
	}
}


?>