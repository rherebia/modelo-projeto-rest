package br.com.rbh.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(schema = "teste")
@SequenceGenerator(name = "usuario_seq_gen", sequenceName = "teste.usuario_seq")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "usuario_seq_gen")
	private Long id;
	
	private String nome;
	
	public Usuario() {
		
	}
	
	public Usuario(String nome) {
		this.nome = nome;
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
