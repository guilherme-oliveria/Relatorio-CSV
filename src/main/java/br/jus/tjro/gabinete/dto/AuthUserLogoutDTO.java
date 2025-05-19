package br.jus.tjro.gabinete.dto;

public class AuthUserLogoutDTO {
    private String refreshToken;
    private String accessToken;

    public AuthUserLogoutDTO() {
    }
    public AuthUserLogoutDTO(String accessToken,String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

}
