package br.com.rbh.request;

import br.com.rbh.dominio.Usuario;

public class UsuarioRequest {

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Usuario getUsuario() {
		return new Usuario(nome);
	}
}
