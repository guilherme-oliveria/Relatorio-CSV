package br.jus.tjro.gabinete.controller.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@CrossOrigin(maxAge = 3600)
public class ControllerAdviceAccessDeniedHandler {

    private final Logger looger = LoggerFactory.getLogger(ControllerAdviceAccessDeniedHandler.class);

    private String getTraceId(){
        return MDC.get("X-B3-TraceId");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        looger.error("acesso negado",e);
        return ResponseEntity.status(403).header("error", "Acesso Negado! ["+getTraceId()+"]").build();
    }

}
