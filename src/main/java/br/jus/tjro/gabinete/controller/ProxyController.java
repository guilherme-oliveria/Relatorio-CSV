package br.jus.tjro.gabinete.controller;

import br.jus.tjro.gabinete.service.remoto.ProxyRemotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("proxy")
public class ProxyController {


    @Autowired
    private ProxyRemotoService proxyRemotoService;

    @GetMapping(produces = "application/json")
    public String get(@RequestParam("url") String url) throws Exception {
        return proxyRemotoService.getBody(url).toString();
    }


}
