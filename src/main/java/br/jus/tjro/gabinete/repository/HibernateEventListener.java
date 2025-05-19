package br.jus.tjro.gabinete.repository;

import br.jus.tjro.gabinete.model.gab.processo.Processo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.persistence.*;

public class HibernateEventListener {

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyUpdate(Processo processo) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(processo);
            System.out.println("beforeUpdate");
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    private void afterAnyUpdate(Processo processo) {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(processo);
            System.out.println("afterUpdate");
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
