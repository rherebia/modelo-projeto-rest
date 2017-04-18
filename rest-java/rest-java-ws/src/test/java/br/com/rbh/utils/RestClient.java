package br.com.rbh.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientResponse;

import com.google.gson.Gson;

public class RestClient {

	public <T extends Object> List<T> consultar(String uri, Map<String, Object> params, Class<T> clazz) {
		Client client = ClientBuilder.newClient();
		
		WebTarget webTarget = client.target(uri);
		
		if (!params.isEmpty()) {
			for (String param : params.keySet()) {
				webTarget.queryParam(param, params.get(param));
			}
		}
		
		ClientResponse clientResponse = webTarget.request(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		
		if (clientResponse.getStatus() != 200) {
			return new ArrayList<>();
		} else {
			Gson gson = new Gson();
			
			return (List<T>) Arrays.asList(gson.fromJson((String) clientResponse.getEntity(), clazz));
		}
	}
}
