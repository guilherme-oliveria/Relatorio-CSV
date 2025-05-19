package br.jus.tjro.gabinete.config.cors;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class AllowHeaders {
    public static final List values = ImmutableList.of("Authorization", "Requestor-Type","Cache-Control",
        "Content-Type", "oj", "cache", "pragma", "traceparent","Keep-Alive","X-CustomHeader",
        "User-Agent","X-Get-Header","Access-Control-Max-Age","Access-Control-Allow-Origin");
}
