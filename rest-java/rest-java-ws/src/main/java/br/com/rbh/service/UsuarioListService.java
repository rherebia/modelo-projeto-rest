package br.com.rbh.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;

import br.com.rbh.dominio.Usuario;

@Stateful
public class UsuarioListService {

	private List<Usuario> usuarios = new ArrayList<>();
	
	public Usuario get(Long id) {
		Usuario usuario = usuarios
			.stream()
			.filter(u -> id.equals(u.getId()))
			.findAny()
			.orElse(null);
		
		return usuario;
	}
	
	public List<Usuario> consultar(Integer limit, Integer offset) {
		
		Integer fromIndex = offset * limit;
		Integer toIndex = fromIndex + limit;
		
		return usuarios.subList(fromIndex, toIndex);
	}
	
	public Integer count() {
		return usuarios.size();
	}
	
	public Usuario criar(Usuario usuario) {
		usuarios.add(usuario);
		
		return usuario;
	}
	
	public Usuario atualizar(Usuario usuario) {
		Usuario existente = get(usuario.getId());
		
		return usuarios.set(usuarios.indexOf(existente), usuario);
	}
	
	public void remover(Long id) {
		Usuario usuario = get(id);

		if (usuario != null) {
			usuarios.remove(usuario);
		}
	}
}
