package de.uniulm.omi.cloudiator.visor.sensors.restUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;


/**
 * Created by Dimitris Charilas on 24/02/17.
 */
public class RestUtil {

	private final static Logger logger = LoggerFactory.getLogger(RestUtil.class);

	private static RestTemplate restTemplate;

	/**
	 * Calls a specified RESTful service
	 */
	public static ResponseEntity callRestUrl(String targetURL, HttpMethod httpMethod, Object form) {
        /*
         * Construct headers
         */
        if( restTemplate == null ) restTemplate = new RestTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		requestHeaders.setAcceptCharset(Collections.singletonList(Charset.forName("UTF-8")));
        /*
         * Perform the call. If body exists then send it as JSON
         */
		HttpEntity requestEntity = form != null ? new HttpEntity(form, requestHeaders) : new HttpEntity(requestHeaders);
		return restTemplate.exchange(targetURL, httpMethod, requestEntity, byte[].class);
	}


}
