package br.com.rbh.resource;

import java.net.URI;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import br.com.rbh.dominio.Usuario;
import br.com.rbh.request.UsuarioRequest;
import br.com.rbh.service.UsuarioService;

@Path("usuarios")
public class UsuariosResource {
	
	@EJB
	private UsuarioService usuarioService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") Long id) {
		Usuario usuario = usuarioService.get(id);
		
		if (usuario != null) {
			Gson gson = new Gson();
			
			return Response.status(200).entity(gson.toJson(usuario)).build();
		} else {
			return Response.status(404).entity("").build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultar() {
		List<Usuario> usuarios = usuarioService.consultar();
		
		Gson gson = new Gson();
		
		return Response.status(200).entity(gson.toJson(usuarios)).build();
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response criar(UsuarioRequest usuarioRequest) throws Exception {
		Usuario usuario = usuarioService.criar(usuarioRequest.getUsuario());
		
		URI uri = new URI("/usuarios/" + usuario.getId());
		
		return Response.created(uri).build();
	}
	
	@PUT
	@Path("{id}")
	public Response atualizar(@PathParam("id") Long id) {
		return null;
	}
	
	@DELETE
	@Path("{id}")
	public Response remover(@PathParam("id") Long id) {
		return null;
	}
}
