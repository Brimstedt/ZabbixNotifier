<?php 
/**
 *
 * @package c2dm
 * @version $Id$
 * @copyright (c) 2011 lytsing.org
 * Description: C2DM implementation PHP code
 * refer to: http://stackoverflow.com/questions/4121508/c2dm-implementation-php-code
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

class c2dm {
	var $authString = "";

	/**
	 * Get Google login auth token
	 * @see http://code.google.com/apis/accounts/docs/AuthForInstalledApps.html
	 */
	function getAuthToken($username, $password) {

		// Initialize the curl object
		$curl = curl_init();

		curl_setopt($curl, CURLOPT_URL, "https://www.google.com/accounts/ClientLogin");
		curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true);

		$data = array(
			'Email'         => $username,
			'Passwd'        => $password,
			'accountType'   => 'HOSTED_OR_GOOGLE',
			'source'        => 'BrimstedtTechnology-ZabbixNotifier-1.0',
			'service'       => 'ac2dm');

		curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, 0);
		curl_setopt($curl, CURLOPT_POST, true);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($curl, CURLOPT_POSTFIELDS, $data);

		$response = curl_exec($curl);
		curl_close($curl);

		// Get the Auth string
		preg_match("/Auth=([a-z0-9_\-]+)/i", $response, $matches);
		$this->authString = $matches[1];
	}

	/**
	 * Send HTTP POST data form
	 */
	function sendMessage($deviceRegistrationId, $server, $message) {
		$headers[] = 'Authorization: GoogleLogin auth='.$this->authString;
		$data = array(
			'registration_id' => $deviceRegistrationId,
			'collapse_key' => 0,
			'data.server' => $server,
				'data.message' => $message
		);

		$curl = curl_init();
		  
		curl_setopt($curl, CURLOPT_URL, "https://android.apis.google.com/c2dm/send");
		curl_setopt($curl, CURLOPT_FOLLOWLOCATION, true);
		curl_setopt($curl, CURLOPT_HTTPHEADER, $headers);
		curl_setopt($curl, CURLOPT_HEADER, 0);
		curl_setopt($curl, CURLOPT_POST, true);
		curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
		curl_setopt($curl, CURLOPT_POSTFIELDS, $data);
		curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);

		$respone = curl_exec($curl);
		echo curl_errno($curl);
		echo curl_error($curl);
		curl_close($curl);

		return $respone;
	}
}

?>

