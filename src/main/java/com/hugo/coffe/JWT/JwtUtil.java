package com.hugo.coffe.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // servicio de Spring, para que Spring la maneje como un bean
public class JwtUtil {

    // La clave secreta para firmar el token, debe mantenerse privada
    private SecretKey secret= Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Extrae el nombre de usuario (subject) del token
    public String extractUsername(String token){
        return extractClamis(token,Claims::getSubject);
    }

    // Extrae la fecha de expiración del token
    public Date extractExpiration(String token){
        return extractClamis(token, Claims::getExpiration);
    }

    // Método genérico que extrae información de los claims del token
    public <T> T extractClamis(String token, Function<Claims, T> claimsResolver){
        final Claims claims= extractAllClaims(token); // Extrae todos los claims del token
        return claimsResolver.apply(claims);
    }

    // Extrae todos los claims del token, decodificando el JWT y usando la clave secreta para verificarlo
    public Claims extractAllClaims(String token){
        return Jwts.parser()            // Utiliza el parser para analizar el token
                .setSigningKey(secret)  // Establece la clave secreta que se usó para firmar el token
                .parseClaimsJws(token)  // Analiza el JWT y obtiene los claims
                .getBody();             // Devuelve el cuerpo de los claims del JWT
    }

    // Verifica si el token ha expirado
    private Boolean isTokenExpired(String token){
        // Si la fecha de expiración es anterior a la fecha actual, ha expirado
        return extractExpiration(token).before(new Date());
    }

    // Genera un nuevo token JWT para un usuario, incluyendo el nombre de usuario y el rol en los claims
    public String generateToken(String username, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        return createToken(claims, username);
    }

    // Crea el token JWT, firmándolo con la clave secreta y añadiendo claims, subject, fecha de emisión y expiración
    private String createToken(Map<String,Object> claims, String subject){
        return Jwts.builder()            // Utiliza el builder de JJWT para construir el token
                .setClaims(claims)       // Establece los claims que contiene el token
                .setSubject(subject)     // Establece el subject (nombre de usuario)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Establece la fecha de emisión (hora actual)
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*10)) // Establece la fecha de expiración (10 horas a partir de ahora)
                .signWith(SignatureAlgorithm.HS256, secret)        // Firma el token usando el algoritmo HS256 y la clave secreta
                .compact();              // Compacta el token en formato JWT
    }

    // Valida si un token es válido comparando el nombre de usuario y verificando que el token no haya expirado
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username=extractUsername(token); // Extrae el nombre de usuario del token
        // Devuelve true si el nombre de usuario coincide y el token no ha expirado
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
