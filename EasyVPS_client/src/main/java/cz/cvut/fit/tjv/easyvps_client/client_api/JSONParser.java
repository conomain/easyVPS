package cz.cvut.fit.tjv.easyvps_client.client_api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public abstract class JSONParser {

    public String getApiMessage(String responseBody) {
        try {

            ObjectMapper objectMapper = new ObjectMapper();

            Map<String, Object> map = objectMapper.readValue(responseBody, new TypeReference<>() {});

            return map.get("message").toString();

        } catch (Exception e) {
            e.getMessage();
            return responseBody;
        }
    }
}
