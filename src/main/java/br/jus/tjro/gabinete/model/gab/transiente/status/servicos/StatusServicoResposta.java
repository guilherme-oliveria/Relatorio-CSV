package br.jus.tjro.gabinete.model.gab.transiente.status.servicos;


import java.util.Map;

public class StatusServicoResposta {
    public String status;
    public DiskSpaceInfo diskSpace;
    public DatabaseInfo db;
    public Map<String,String> metricas;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DiskSpaceInfo getDiskSpace() {
        return diskSpace;
    }

    public void setDiskSpace(DiskSpaceInfo diskSpace) {
        this.diskSpace = diskSpace;
    }

    public DatabaseInfo getDb() {
        return db;
    }

    public void setDb(DatabaseInfo db) {
        this.db = db;
    }

    public Map<String, String> getMetricas() {
        return metricas;
    }

    public void setMetricas(Map<String, String> metricas) {
        this.metricas = metricas;
    }
}
