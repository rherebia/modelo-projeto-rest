package br.com.rbh.resource;

import java.net.URI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import br.com.rbh.dominio.Usuario;

public class UsuarioResourceTest {

	private static HttpServer httpServer;

	private static WebTarget webTarget;

	@BeforeClass
	public static void setup() {
		ResourceConfig rc = new ResourceConfig().packages("br.com.rbh.resource");

		httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://localhost:7378"), rc);

		Client client = ClientBuilder.newClient();

		webTarget = client.target("http://localhost:7378/usuarios");
	}

	@AfterClass
	public static void destroy() {
		httpServer.shutdownNow();
	}

	@Test
	public void testConsultar() {
		ClientResponse response = webTarget
				.queryParam("offset", 0)
				.queryParam("limit", 5)
				.request()
				.get(ClientResponse.class);

		Assert.assertEquals(Status.OK, response.getStatus());

		Gson gson = new Gson();

		Usuario[] usuarios = gson.fromJson((String) response.getEntity(), Usuario[].class);

		Assert.assertTrue(usuarios.length > 0);
	}

	@Test
	public void testCriar() {
		Usuario novoUsuario = new Usuario();
		novoUsuario.setNome("Renato Herebia");
		
		Gson gson = new Gson();
		
		ClientResponse response = webTarget
				.request()
				.post(Entity.entity(gson.toJson(novoUsuario), MediaType.APPLICATION_JSON), ClientResponse.class);	

		Assert.assertEquals(Status.CREATED, response.getStatus());

		response = webTarget
			.path(response.getHeaderString("Location"))
			.request()
			.get(ClientResponse.class);

		Assert.assertEquals(Status.OK, response.getStatus());
	}

	@Test
	public void testAtualizar() {
		Response response = webTarget.queryParam("offset", 0).queryParam("limit", 5).request().get();

		Assert.assertEquals(Status.OK, response.getStatus());

		Gson gson = new Gson();

		Usuario[] usuarios = gson.fromJson((String) response.getEntity(), Usuario[].class);

		Assert.assertTrue(usuarios.length > 0);
	}

	@Test
	public void testRemover() {
		Response response = webTarget.queryParam("offset", 0).queryParam("limit", 5).request().get();

		Assert.assertEquals(Status.OK, response.getStatus());

		Gson gson = new Gson();

		Usuario[] usuarios = gson.fromJson((String) response.getEntity(), Usuario[].class);

		Assert.assertTrue(usuarios.length > 0);
	}
}
