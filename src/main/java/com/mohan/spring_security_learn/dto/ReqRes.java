package com.mohan.spring_security_learn.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mohan.spring_security_learn.entity.OurUsers;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ReqRes {

    private Integer statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String name;
    private String email;
    private String city;
    private String role;
    private String password;
    private OurUsers ourUsers;
    private List<OurUsers> ourUsersList;

}

//1. @JsonInclude(JsonInclude.Include.NON_EMPTY)
//This annotation is applied at the class level to control which properties are included in the JSON output when the object is serialized.
//
//JsonInclude.Include.NON_EMPTY means that any field with a "null" or "empty" value (such as an empty list, empty map, or empty string) will be excluded from the JSON output.
//For example, if a field has a null value or if a List field is empty, that field will not appear in the serialized JSON.


//2. @JsonIgnoreProperties(ignoreUnknown = true)
//This annotation is also applied at the class level and affects the deserialization process.
//
//ignoreUnknown = true instructs Jackson to ignore any properties in the JSON input that do not have corresponding fields in the Java class.
//This is particularly helpful when dealing with JSON data that may contain extra fields you don't need or expect, as it prevents Jackson from throwing an exception if it encounters unknown properties.



//Example Use
//Here's an example to illustrate how these annotations work together:
//
//Suppose you have a Java class:
//
//java
//Copy code
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
//@JsonIgnoreProperties(ignoreUnknown = true)
//public class User {
//    private String name;
//    private String email;
//    private List<String> roles;
//    // Getters and Setters
//}
//If you try to serialize an instance of User with an empty or null email and roles, only name will appear in the JSON output. When deserializing, if the JSON contains unexpected fields (e.g., "age"), they will be ignored rather than causing an error.
//
//These annotations together help control JSON output format and make it more robust against unexpected input.































