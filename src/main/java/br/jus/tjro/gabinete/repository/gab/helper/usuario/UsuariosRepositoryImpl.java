package br.jus.tjro.gabinete.repository.gab.helper.usuario;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.PerfilAcesso;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

public class UsuariosRepositoryImpl implements UsuariosRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Override
    @Transactional(readOnly = true)
    public PerfilAcesso getPermissoes(Usuario usuario) {

        return manager.createQuery("select u.perfilAcesso from Usuario u where u = :usuario", PerfilAcesso.class)
            .setParameter("usuario", usuario)
            .getSingleResult();
    }

}
