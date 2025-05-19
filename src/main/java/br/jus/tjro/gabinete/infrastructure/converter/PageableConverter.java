package br.jus.tjro.gabinete.infrastructure.converter;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class PageableConverter {

    public String toQueryString(Pageable p){
        if(p == null || p.isUnpaged())
            return "";
        StringBuilder ans = new StringBuilder();
        ans.append("page=");
        ans.append(encodeURLComponent(p.getPageNumber() + ""));

        ans.append("&size=");
        ans.append(encodeURLComponent(p.getPageSize() + ""));

        if (p.getSort() != null) {
            for (Sort.Order o : p.getSort()) {
                ans.append("&sort=");
                ans.append(encodeURLComponent(o.getProperty()));
                ans.append(",");
                ans.append(encodeURLComponent(o.getDirection().name()));
            }
        }
        return ans.toString();
    }

    public String encodeURLComponent(String component){
        return URLEncoder.encode(component, StandardCharsets.UTF_8);
    }
}
