package br.com.rbh.resource;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import javax.ws.rs.core.Response.Status;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;

import br.com.rbh.dominio.Usuario;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class UsuarioResourceTest {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 8080;
		RestAssured.basePath = "/rest-java-ws/api";
	}

	@AfterClass
	public static void destroy() {
	}
	
	@Test
	public void testConsultarPorId() {
		Response response = when()
			.get("/usuarios/{id}", -1)
		.then()
			.extract()
			.response();
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.statusCode());
		
		String json = response.getBody().asString();
			
		Usuario usuario = new Gson().fromJson(json, Usuario.class);
		
		Assert.assertEquals("Renato Herebia", usuario.getNome());
	}

	@Test
	public void testConsultar() {
		Response response = when()
			.get("/usuarios")
		.then()
			.extract()
			.response();
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.statusCode());
		
		String json = response.getBody().asString();
			
		Usuario[] usuarios = new Gson().fromJson(json, Usuario[].class);
		
		Assert.assertEquals(5, usuarios.length);
	}

	@Test
	public void testCriar() {
		Usuario novoUsuario = new Usuario();
		novoUsuario.setNome("Renato Herebia");
		
		Response response = given()
			.contentType(ContentType.JSON)
			.body(new Gson().toJson(novoUsuario))
		.when()
			.post("/usuarios")
		.then()
			.extract()
			.response();
		
		Assert.assertEquals(Status.CREATED.getStatusCode(), response.statusCode());
		String locationNewResource = response.header("Location");
		Assert.assertTrue(locationNewResource.contains("/api/usuarios/"));
		
		String[] uriNewResource = locationNewResource.split("/");
		Long novoId = Long.valueOf(uriNewResource[uriNewResource.length - 1]);
		
		// desfazer criacao
		response = when()
				.delete("/usuarios/{id}", novoId)
			.then()
				.extract()
				.response();
		
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
	}

	@Test
	public void testAtualizar() {
		Response response = when()
				.get("/usuarios/{id}", -1)
			.then()
				.extract()
				.response();
		
		String json = response.getBody().asString();
		
		Usuario usuario = new Gson().fromJson(json, Usuario.class);
		
		Assert.assertNotNull(usuario);
		
		String nomeAntigo = usuario.getNome();
		
		String novoNome = usuario.getNome() + " Novo Sobrenome";
		
		response = given()
			.contentType(ContentType.JSON)
			.body(new Gson().toJson(new Usuario(novoNome)))
		.when()
			.put("/usuarios/{id}", usuario.getId())
		.then()
			.extract()
			.response();
		
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
		
		response = when()
				.get("/usuarios/{id}", usuario.getId())
			.then()
				.extract()
				.response();
		
		json = response.getBody().asString();
		
		usuario = new Gson().fromJson(json, Usuario.class);
		
		Assert.assertEquals(Status.OK.getStatusCode(), response.getStatusCode());
		Assert.assertNotNull(usuario);
		Assert.assertEquals(novoNome, usuario.getNome());
		
		// desfazer alteracao
		response = given()
				.contentType(ContentType.JSON)
				.body(new Gson().toJson(new Usuario(nomeAntigo)))
			.when()
				.put("/usuarios/{id}", usuario.getId())
			.then()
				.extract()
				.response();
			
			Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
	}

	@Test
	public void testRemover() {
		Usuario novoUsuario = new Usuario();
		novoUsuario.setNome("Renato Herebia");
		
		Response response = given()
			.contentType(ContentType.JSON)
			.body(new Gson().toJson(novoUsuario))
		.when()
			.post("/usuarios")
		.then()
			.extract()
			.response();
		
		Assert.assertEquals(Status.CREATED.getStatusCode(), response.statusCode());
		String locationNewResource = response.header("Location");
		Assert.assertTrue(locationNewResource.contains("/api/usuarios/"));
		
		String[] uriNewResource = locationNewResource.split("/");
		Long novoId = Long.valueOf(uriNewResource[uriNewResource.length - 1]);
		
		response = when()
				.delete("/usuarios/{id}", novoId)
			.then()
				.extract()
				.response();
		
		Assert.assertEquals(Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
		
		response = when()
				.get("/usuarios/{id}", novoId)
			.then()
				.extract()
				.response();
		
		Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
	}
}
