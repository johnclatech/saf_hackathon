<?php
		$keyname = "array";
		$vehiclename = "SUZUKI";
		$vehicletype = "OUTBACK"; //Toyota =(WISH, PREMIO, DEMIO, VANGUARD, V8, LEXUS),subaru = (IMPREZZA,LEGACY,FORESTER,OUTBACK)
		$timestamp = date('Y:m:d h:i:s');
		$processingcode="00020";  
		$messagetype="0001";
		$channel = "soap";
		
		$parameters = array(
				'timestamp' => $timestamp,
				'processingcode' => $processingcode, 
				'messagetype' => $messagetype,
				'channel' => $channel,
				'vehiclename' => $vehiclename,
				'car_type' => $vehicletype,
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