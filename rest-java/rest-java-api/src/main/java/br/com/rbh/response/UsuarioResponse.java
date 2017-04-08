package br.com.rbh.response;

import br.com.rbh.dominio.Usuario;

public class UsuarioResponse {

	private Long id;
	
	private String nome;
	
	public UsuarioResponse() {
		
	}
	
	public UsuarioResponse(Usuario usuario) {
		this.id = usuario.getId();
		this.nome = usuario.getNome();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
