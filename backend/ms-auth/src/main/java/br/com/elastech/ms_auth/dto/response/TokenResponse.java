package br.com.elastech.ms_auth.dto.response;

public record TokenResponse(
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
