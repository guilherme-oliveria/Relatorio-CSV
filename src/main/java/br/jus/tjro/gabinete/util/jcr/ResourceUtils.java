package br.jus.tjro.gabinete.util.jcr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.lang3.StringUtils;

public class ResourceUtils {
    public ResourceUtils() {
    }

    public static long getResourceSize(String uri, HttpClient http) throws HttpException, IOException {
        HeadMethod head = new HeadMethod(uri);

        long var5;
        try {
            int resp = http.executeMethod(head);
            if (resp == 404) {
                long var10 = -1L;
                return var10;
            }

            if (resp != 200) {
                throw new HttpException(String.format("Retorno do servidor inválido %d", resp));
            }

            Header length = head.getResponseHeader("Content-Length");
            var5 = length == null ? -1L : Long.parseLong(length.getValue());
        } finally {
            head.releaseConnection();
        }

        return var5;
    }

    public static int putResource(String uri, byte[] data, Range range, String mimetype, HttpClient http) throws HttpException, IOException {
        ByteArrayRequestEntity entity = new ByteArrayRequestEntity(Arrays.copyOf(data, range.getLength()), StringUtils.defaultString(mimetype, "application/octet-stream"));
        PutMethod put = new PutMethod(uri);
        put.setRequestEntity(entity);
        put.setRequestHeader("Content-Range", range.toContentRange());
        put.setRequestHeader("Content-Length", Long.toString((long)range.getLength()));

        int var7;
        try {
            var7 = http.executeMethod(put);
        } finally {
            put.releaseConnection();
        }

        return var7;
    }

    public static void deleteResource(String uri, HttpClient http) throws HttpException, IOException {
        DeleteMethod delete = new DeleteMethod(uri);

        try {
            http.executeMethod(delete);
        } finally {
            delete.releaseConnection();
        }

    }

    public static InputStream getResource(String uri, HttpClient http) throws HttpException, IOException {
        GetMethod get = new GetMethod(uri);
        int resp = http.executeMethod(get);
        switch (resp) {
            case 200:
                return get.getResponseBodyAsStream();
            case 404:
                throw new HttpException(String.format("Rescurso não encontrado %d", resp));
            case 409:
                throw new HttpException(String.format("Estado do recurso inválido %d", resp));
            case 500:
            case 503:
                throw new HttpException(String.format("Erro interno %d", resp));
            default:
                throw new HttpException(String.format("Código de respo %d", resp));
        }
    }

    public static String getResourceURI(String url, String path) {
        return String.format("%s/%s", url, path);
    }
}
