package br.jus.tjro.gabinete.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminsEnvironment {
    private final List<String> cpfsAdmin;

    @Autowired
    public AdminsEnvironment(@Value("#{'${cpfs.admins:windson}'.split(',')}") List<String> cpfsAdmin) {
        this.cpfsAdmin = cpfsAdmin;
    }

    public Boolean isAdmin(String cpf) {
        return cpfsAdmin.contains(cpf);
    }
}
