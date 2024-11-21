package com.mohan.spring_security_learn.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {

    private SecretKey Key;
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    public JWTUtils() {
        String secretKeyString =
                "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] secretKeyBytes = Base64.getDecoder().decode(secretKeyString.getBytes(StandardCharsets.UTF_8));
        this.Key = new SecretKeySpec(secretKeyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key).build().parseSignedClaims(token).getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}

//The JWTUtils class is a utility for handling JSON Web Tokens (JWTs) in a Spring application. It provides methods to generate tokens (including a refresh token), extract information from tokens, and validate tokens. Let's go through the code in detail:
//
//        1. @Component Annotation
//The @Component annotation indicates that JWTUtils is a Spring-managed bean. When annotated this way, the Spring framework will automatically detect and instantiate this class as a bean, making it available for dependency injection in other parts of the application.
//
//        2. Fields
//private SecretKey Key;
//This field holds the secret key used for signing and verifying JWTs. It uses a SecretKey object with the HMAC-SHA256 algorithm for cryptographic security.
//
//private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
//This constant defines the expiration time of the JWT, set to 24 hours (in milliseconds). The time is calculated as:
//
//        24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 86,400,000 milliseconds
//3. Constructor
//The JWTUtils constructor initializes the Key by:
//
//Defining a String constant, secretKey, with a value of "ssd". This is the base string used for the secret key in this example,
// although it is typically stored securely and is longer and more complex in production.
//Converting secretKey to a byte array by encoding it in UTF-8 and then decoding it with Base64, making it suitable for use as a cryptographic key.
//Creating a SecretKeySpec from the decoded byte array, specifying the algorithm "HmacSHA256" to instantiate the Key object. This SecretKeySpec is then assigned to the Key field.
//4. generateToken(UserDetails userDetails) Method
//This method generates a JWT for a given UserDetails object:
//
//        Jwts.builder(): Starts building a new JWT.
//.subject(userDetails.getUsername()): Sets the subject claim of the token, which is typically the username.
//.issuedAt(new Date(System.currentTimeMillis())): Sets the issuance time of the token as the current time.
//.expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)): Sets the expiration time by adding EXPIRATION_TIME (24 hours) to the current time.
//        .signWith(Key): Signs the JWT using the Key created in the constructor.
//        .compact(): Finalizes the token construction and returns it as a string.
//        5. generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) Method
//This method generates a refresh token. A refresh token typically has a longer expiration time and includes additional claims:
//
//        .claims(claims): Sets custom claims using a HashMap parameter, allowing for more data in the token payload.
//        .subject(userDetails.getUsername()), .issuedAt(...), .expiration(...), .signWith(Key), and .compact(): These are similar to the generateToken method and function in the same way.
//6. extractUsername(String token) Method
//This method extracts the username (subject) from a token:
//
//It calls the extractClaims method, passing token and Claims::getSubject as arguments.
//        Claims::getSubject is a method reference to retrieve the subject claim from the token’s payload.
//        7. extractClaims(String token, Function<Claims, T> claimsTFunction) Method
//This is a generic, private helper method to extract claims from a token. It takes:
//
//token: The JWT from which claims need to be extracted.
//        claimsTFunction: A Function that operates on Claims to extract a specific part of it.
//        Here's how it works:
//
//        Jwts.parser(): Initializes a JWT parser.
//        .verifyWith(Key): Sets the key for verifying the token’s signature.
//        .build().parseSignedClaims(token): Parses the token and extracts its claims (payload) after verifying its signature.
//        claimsTFunction.apply(...): Applies the provided function (claimsTFunction) on the extracted claims to retrieve the desired value.
//        8. isTokenValid(String token, UserDetails userDetails) Method
//This method checks if a given token is valid:
//
//It first calls extractUsername(token) to get the username from the token.
//Then, it compares the extracted username with userDetails.getUsername().
//Additionally, it checks if the token is expired by calling isTokenExpired(token).
//The token is considered valid if the username matches and the token is not expired.
//9. isTokenExpired(String token) Method
//This method checks if a token is expired:
//
//It extracts the expiration claim from the token by calling extractClaims(token, Claims::getExpiration).
//Then, it compares the extracted expiration date with the current date (new Date()). If the expiration date is before the current date, the token is expired and the method returns true; otherwise, it returns false.
//Summary
//The JWTUtils class provides essential utilities for JWT handling:
//
//generateToken and generateRefreshToken to create tokens.
//extractUsername to retrieve the username from a token.
//isTokenValid to check if a token is valid by verifying the username and expiration.
//isTokenExpired to check if a token has expired.
//This setup allows a Spring application to use JWTs for authentication, providing a secure way to validate user identity and issue tokens with limited lifespans.
























































