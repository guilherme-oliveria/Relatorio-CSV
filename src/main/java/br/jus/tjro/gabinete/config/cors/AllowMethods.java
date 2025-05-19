package br.jus.tjro.gabinete.config.cors;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class AllowMethods {

    public static final List values = ImmutableList.of("HEAD","GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS");
}
