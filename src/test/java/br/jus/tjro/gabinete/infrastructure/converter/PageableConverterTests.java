package br.jus.tjro.gabinete.infrastructure.converter;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PageableConverterTests {


    @Test
    public void paginaNull(){
        String retorno = new PageableConverter().toQueryString(null);
        assertEquals("",retorno);
    }

    @Test
    public void unpaged(){
        String retorno = new PageableConverter().toQueryString(Pageable.unpaged());
        assertEquals("",retorno);
    }


    @Test
    public void paginaSize10Pagina6(){
        String retorno = new PageableConverter().toQueryString(PageRequest.of(6,10));
        assertEquals("page=6&size=10",retorno);
    }

    @Test
    public void paginaSize10Pagina6SortWindsonDesc(){
        String retorno = new PageableConverter().toQueryString(PageRequest.of(6,10,Sort.by("windson").descending()));
        assertEquals("page=6&size=10&sort=windson,DESC",retorno);
    }


    @Test
    public void paginaSize10Pagina6SortWindson(){
        String retorno = new PageableConverter().toQueryString(PageRequest.of(6,10,Sort.by("windson")));
        assertEquals("page=6&size=10&sort=windson,ASC",retorno);
    }
}
