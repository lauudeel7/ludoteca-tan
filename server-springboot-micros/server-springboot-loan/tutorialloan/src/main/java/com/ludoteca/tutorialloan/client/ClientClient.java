package com.ludoteca.tutorialloan.client;

import com.ludoteca.tutorialloan.client.model.ClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "spring-cloud-eureka-client-client")
public interface ClientClient {
    @GetMapping(path = "/client/{id}")
    ClientDto getClient(@PathVariable("id") Long id);
}
