package com.aluracursos.ecomart.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/generador")
public class GeneradorDeProductosController {

    private final ChatClient chatClient;
    //Este codigo se utilizaria para configurar el modelo de Gpt a utilizar en el controlador
    //private final ChatClient chatClientr;
   /* public GeneradorDeProductosController(@Qualifier("gpt-40") ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClient;
    }*/

    public GeneradorDeProductosController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public String generadorDeProductos() {
        var userInput = "Genera 5 productos ecologicos";
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }
}
