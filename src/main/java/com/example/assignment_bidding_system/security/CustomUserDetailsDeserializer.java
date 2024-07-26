package com.example.assignment_bidding_system.security;

import com.example.assignment_bidding_system.entity.User;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Set;

public class CustomUserDetailsDeserializer extends JsonDeserializer<CustomUserDetails> {

    private static final TypeReference<Set<SimpleGrantedAuthority>> SIMPLE_GRANTED_AUTHORITY_LIST = new TypeReference<>() {
    };

    @Override
    public CustomUserDetails deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode jsonNode = mapper.readTree(p);

        System.out.println("FUCK YOU");
        System.out.println(jsonNode);

        JsonNode userJsonNode = readJsonNode(jsonNode, "user");

        Set<? extends GrantedAuthority> authorities = mapper.convertValue(
                jsonNode.get("authorities"),
                SIMPLE_GRANTED_AUTHORITY_LIST
        );

        System.out.println(authorities);

        Long id = readJsonNode(userJsonNode, "id").asLong();
        String uuid = readJsonNode(userJsonNode, "uuid").asText();
        String username = readJsonNode(userJsonNode, "username").asText();
        String email = readJsonNode(userJsonNode, "email").asText();
        String password = readJsonNode(userJsonNode, "password").asText();
        boolean isEnabled = readJsonNode(userJsonNode, "isEnabled").asBoolean();
        boolean accountNoExpired = readJsonNode(userJsonNode, "accountNoExpired").asBoolean();
        boolean credentialsNonExpired = readJsonNode(userJsonNode, "credentialsNonExpired").asBoolean();
        boolean accountNonLocked = readJsonNode(userJsonNode, "accountNonLocked").asBoolean();

        String familyName = readJsonNode(userJsonNode, "familyName").asText();
        String givenName = readJsonNode(userJsonNode, "givenName").asText();
        String gender = readJsonNode(userJsonNode, "gender").asText();
//        LocalDate dob = LocalDate.parse(readJsonNode(userJsonNode, "dob").asText());
        String jobTitle = readJsonNode(userJsonNode, "jobTitle").asText();

        User user = User.builder()
                .id(id)
                .uuid(uuid)
                .username(username)
                .email(email)
                .password(password)
                .accountNonExpired(accountNoExpired)
                .accountNonLocked(accountNonLocked)
                .credentialsNonExpired(credentialsNonExpired)
                .isEnabled(isEnabled)
                .familyName(familyName)
                .givenName(givenName)
                .gender(gender)
//                .dob(dob)
                .jobTitle(jobTitle)
                .build();

        return new CustomUserDetails(user);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
