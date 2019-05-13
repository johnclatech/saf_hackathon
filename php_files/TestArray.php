<?php
		$keyname = "array";
		$array = "2 7 11 15";//"1 2 8 2 10";
		$timestamp = date('Y:m:d h:i:s');
		$processingcode="000000";  //"000100" - ARRAYS //"000200" - Operators 
		$messagetype="0000";
		$channel = "soap";
		
		$parameters = array(
				'array' => $array,
				'timestamp' => $timestamp,
				'processingcode' => $processingcode, 
				'messagetype' => $messagetype,
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
    $client = new SoapClient('http://127.0.0.1:8086/SafHackArrayChallenge/Request/IncomingRequest?wsdl'); // test

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