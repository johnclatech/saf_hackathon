<?php
		$keyname = "array";
		$array = "10004324 128933"; //sum - [5,8] divide - [17,3] multiply - [1999999999999999999999999, 3] subtract - [21 ,13]
		$timestamp = date('Y:m:d h:i:s');
		$processingcode="000200"; //"000100" - ARRAYS //"000200" - Operators 
		$messagetype="0000";
		$operation = "subtract"; //sum divide multiply subtract
		$channel = "soap";
		
		$parameters = array(
				'array' => $array,
				'timestamp' => $timestamp,
				'processingcode' => $processingcode, 
				'messagetype' => $messagetype,
				'operation' => $operation,
				'channel' => $channel
				
			); 
			
		$ConfigSalt = "#$%^%^{}#$#?/\|##@!%";
		$hash_method = 'sha512';
		$authkey = hash('sha512', base64_encode($ConfigSalt.$channel.$timestamp.$processingcode));
		$jsonRequest = json_encode($parameters); //  encode to JSON
		// final request format will be the encodedRequest as TranDetails and an AuthKey
		$encodedRequest = base64_encode($jsonRequest);
		$soapRequest = array("key" => $keyname , "value" => $encodedRequest , "Authkey" => $authkey); 	
  
  
		echo 'TranDetails '.$jsonRequest;
		echo '   ';

		echo 'soapRequest array '.$soapRequest; 
		$client = new SoapClient('http://127.0.0.1:8086/SafHackArrayChallenge/Request/IncomingRequest?wsdl');

    
   try {
	  
		var_dump($soapRequest);

      $result = $client->GetIndices($soapRequest);
	  
	  if (is_soap_fault($result)) {
		trigger_error("SOAP Fault: (faultcode: {$result->faultcode}, faultstring: {$result->faultstring})", E_USER_ERROR);
	}
	
	} catch (Exception $ex) {
		echo $ex;
	}
		var_dump($result);
?>