package br.com.rbh.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import br.com.rbh.dominio.Usuario;

@Stateless
public class UsuarioService {

	@PersistenceContext
	private EntityManager em;
	
	public Usuario get(Long id) {
		return em.find(Usuario.class, id);
	}
	
	public List<Usuario> consultar() {
		StringBuilder hql = new StringBuilder();
		hql.append("FROM Usuario");
		
		TypedQuery<Usuario> query = em.createQuery(hql.toString(), Usuario.class);
		
		return query.getResultList();
	}
	
	public Usuario criar(Usuario usuario) {
		em.persist(usuario);
		
		return usuario;
	}
	
	public Usuario atualizar(Usuario usuario) {
		return em.merge(usuario);
	}
	
	public void remover(Usuario usuario) {
		em.remove(usuario);
	}
}
