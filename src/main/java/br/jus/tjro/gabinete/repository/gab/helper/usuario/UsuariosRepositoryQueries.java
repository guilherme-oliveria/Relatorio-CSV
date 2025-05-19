package br.jus.tjro.gabinete.repository.gab.helper.usuario;

import br.jus.tjro.gabinete.model.gab.Usuario;
import br.jus.tjro.gabinete.model.gab.enums.PerfilAcesso;

public interface UsuariosRepositoryQueries {

    PerfilAcesso getPermissoes(Usuario usuario);
}
