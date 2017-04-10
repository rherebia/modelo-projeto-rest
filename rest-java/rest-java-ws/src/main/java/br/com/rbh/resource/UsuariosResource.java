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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import com.google.gson.Gson;

import br.com.rbh.dominio.Usuario;
import br.com.rbh.request.UsuarioRequest;
import br.com.rbh.service.UsuarioListService;
import br.com.rbh.service.UsuarioService;

@Path("usuarios")
public class UsuariosResource {
	
	@EJB
	private UsuarioListService usuarioService;

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("id") Long id) {
		Usuario usuario = usuarioService.get(id);
		
		if (usuario != null) {
			Gson gson = new Gson();
			
			return Response.status(Status.OK).entity(gson.toJson(usuario)).build();
		} else {
			return Response.status(Status.NOT_FOUND).entity("").build();
		}
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response consultar(@QueryParam("limit") Integer limit, @QueryParam("offset") Integer offset) {
		if (limit != null || offset != null) {
			if (limit == null || offset == null) {
				Gson gson = new Gson();
				
				return Response.status(Status.BAD_REQUEST)
						.entity(gson.toJson("{ \"msg\" : \"Os atributos limit e offset devem ser informados juntos\"}"))
						.build();
			}
		}
		
		List<Usuario> usuarios = usuarioService.consultar(limit, offset);
		
		Gson gson = new Gson();
		
		Integer totalUsuarios = 0;
		Integer ultimaPagina = totalUsuarios/limit;
		
		URI first = UriBuilder.fromUri("/usuarios?offset={offset}&limit={limit}")
				.resolveTemplate("offset", 0)
				.resolveTemplate("limit", limit)
				.build();
		
		URI last = UriBuilder.fromUri("/usuarios?offset={offset}&limit={limit}")
				.resolveTemplate("offset", ultimaPagina)
				.resolveTemplate("limit", limit)
				.build();
		
		URI next = UriBuilder.fromUri("/usuarios?offset={offset}&limit={limit}")
				.resolveTemplate("offset", offset < ultimaPagina ? offset + 1 : ultimaPagina)
				.resolveTemplate("limit", limit)
				.build();
		
		URI previous = UriBuilder.fromUri("/usuarios?offset={offset}&limit={limit}")
				.resolveTemplate("offset", offset > 0 ? offset - 1 : 0)
				.resolveTemplate("limit", limit)
				.build();
		
		return Response.status(Status.OK)
				.entity(gson.toJson(usuarios))
				.link(first.toString(), "first")
				.link(last.toString(), "last")
				.link(next.toString(), "next")
				.link(previous.toString(), "previous")
				.build();
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
	public Response atualizar(@PathParam("id") Long id, UsuarioRequest usuarioRequest) {
		Usuario usuario = usuarioRequest.getUsuario();
		
		usuario.setId(id);
		
		usuario = usuarioService.atualizar(usuario);
		
		return Response.noContent().build(); 
	}
	
	@DELETE
	@Path("{id}")
	public Response remover(@PathParam("id") Long id) {
		usuarioService.remover(id);
		
		return Response.noContent().build();
	}
}
