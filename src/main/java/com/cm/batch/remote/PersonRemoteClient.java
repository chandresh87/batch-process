package com.cm.batch.remote;

import com.cm.batch.modal.PersonDTO;
import com.cm.batch.modal.PersonEntity;
import com.cm.batch.modal.mapper.PersonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author chandresh.mishra
 */
@Service
public class PersonRemoteClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(PersonRemoteClient.class);
    private final RestTemplate restTemplate;
    private final PersonMapper personMapper;

    public PersonRemoteClient(RestTemplate restTemplate, PersonMapper personMapper) {
        this.restTemplate = restTemplate;
        this.personMapper = personMapper;
    }

    @CircuitBreaker
    public void savePerson(PersonEntity personEntity) {

        final PersonDTO personDTO = personMapper.personEntityToDTO(personEntity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request;

        try {
            request = new HttpEntity<String>(objectMapper.writeValueAsString(personDTO), headers);

            logger.info("posting data {}", personDTO);

            ResponseEntity<String> responseEntityStr =
                    restTemplate.postForEntity("http://localhost:9000/save/person", request, String.class);

            logger.info("response from remote service {} with status code {}", responseEntityStr.getBody(), responseEntityStr.getStatusCode());

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
