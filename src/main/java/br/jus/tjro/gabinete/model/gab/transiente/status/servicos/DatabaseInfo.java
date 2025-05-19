package br.jus.tjro.gabinete.model.gab.transiente.status.servicos;

public class DatabaseInfo {
    public String status;
    public String database;
    public Long hello;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Long getHello() {
        return hello;
    }

    public void setHello(Long hello) {
        this.hello = hello;
    }
}
