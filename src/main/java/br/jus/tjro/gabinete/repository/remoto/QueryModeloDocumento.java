package br.jus.tjro.gabinete.repository.remoto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryModeloDocumento {

    private final Map<String, List<String>> query;


    public QueryModeloDocumento(HashMap<String, List<String>> query){
        this.query = query;
    }

    public Map<String, List<String>> getMap(){
        query.computeIfAbsent("sistema", k -> List.of("gabinete"));
        return query;
    }
}
