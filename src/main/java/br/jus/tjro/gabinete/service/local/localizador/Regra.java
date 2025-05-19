package br.jus.tjro.gabinete.service.local.localizador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.Normalizer;
import java.util.Arrays;

class Regra implements Comparable<Regra> {
    
    private final Logger looger = LoggerFactory.getLogger(Regra.class);
    
    private String parametro;
    private String operador;
    private Object valor;

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getOperador() {
        return operador;
    }

    public void setOperador(String operador) {
        this.operador = operador;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }

    public boolean processa(Object obj) {
        try {

            if (this.operador.equals("=")) {
                if(this.parametro.equals("tags.tag.id")) {
                    var valores = this.valor.toString().split(",");
                    return Arrays.asList(valores).contains(obj.toString());
                }else {
                   return (this.valor.toString().equals(obj.toString()));
                }
            }

            if (this.operador.equals("!=")) {
                if(this.parametro != null && this.parametro.equals("tags.tag.id")) {
                    var valores = this.valor.toString().split(",");
                    return !Arrays.asList(valores).contains(obj.toString());
                }else {
                    return (!this.valor.toString().equals(obj.toString()));
                }
            }
            if (this.operador.equals(">")) {
                if (convertoToLong(obj) > convertoToLong(this.valor)) {
                    return true;
                }
            }
            if (this.operador.equals("<")) {
                if (convertoToLong(obj) < convertoToLong(this.valor)) {
                    return true;
                }
            }
            if (this.operador.equals(">=")) {
                if (convertoToLong(obj) >= convertoToLong(this.valor)) {
                    return true;
                }
            }

            if (this.operador.equals("<=")) {
                if (convertoToLong(obj) <= convertoToLong(this.valor)) {
                    return true;
                }
            }
            if (this.operador.equals("%")) {
                var semAcentos = removerAcentos(this.valor.toString()).toUpperCase();
                var compare = removerAcentos(obj.toString()).toUpperCase();
                return (semAcentos.indexOf(compare) > -1);
            }

            if (this.operador.equals("final")) {
                String retorno = (String) obj;
                if (retorno.length() > 7) {
                    String numero = retorno.substring(6, 7);
                    if (Character.isDigit(((String) this.valor).charAt(0))) {
                        if (numero.equals(this.valor)) {
                            return true;
                        }
                    } else if (this.valor.equals("impar")) {
                        if (Long.parseLong(numero) % 2 != 0) {
                            return true;
                        }
                    } else if (this.valor.equals("par")) {
                        if (Long.parseLong(numero) % 2 == 0) {
                            return true;
                        }
                    }
                }
            }
            if (this.operador.equals("inicial")) {
                String retorno = (String) obj;
                String numero = retorno.substring(0, 1);
                if (Character.isDigit(((String) this.valor).charAt(0))) {
                    if (numero.equals(this.valor)) {
                        return true;

                    }
                } else if (this.valor.equals("impar")) {
                    if (Long.parseLong(numero) % 2 != 0) {
                        return true;
                    }
                } else if (this.valor.equals("par")) {
                    if (Long.parseLong(numero) % 2 == 0) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            looger.error(e.getMessage(),e);
        }
        return false;
    }

    private Long convertoToLong(Object obj) {
        Double d = Double.parseDouble(obj.toString());
        return d.longValue();
    }

    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    @Override
    public int compareTo(Regra o) {
        return 0;
    }
}
