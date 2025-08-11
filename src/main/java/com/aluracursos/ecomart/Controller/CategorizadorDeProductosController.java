package com.aluracursos.ecomart.Controller;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.ModelType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.DefaultChatOptionsBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categorizador")
public class CategorizadorDeProductosController {

    private final ChatClient chatClient;

    public CategorizadorDeProductosController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultOptions(ChatOptions
                        .builder()
                        .model("gpt-4o-mini")//Este es el que se configura para tods los metodos si no se especifica dentro del misno.
                        .build())
                .build()
                ;
    }

    public int contadorTokens(String system, String user) {
        var registry = Encodings.newDefaultEncodingRegistry();
        var enc = registry.getEncodingForModel(ModelType.GPT_4O_MINI);
        return enc.countTokens(system + user);

    }

    //Implemetacion de la logica para la seleccion del modelo

    @GetMapping
    public String CategorizadorDeProductos(@RequestParam String producto) {
        var system = """
                Eres un categorizador de productos y debes responder solo el nombre de la categoria
                
                Escoge un acategoria de la siguiente lista:
                 1. Higiene Personal
                 2. Electronicos
                 3. Deportes
                 4. Otros
                 
                 Ejemplo de uso:
                 
                 Producto: Pelota de futbol
                 Respuesta: Deportes
                                
                """;
        var tokens = contadorTokens(system, producto);
        System.out.println(tokens);

        return this.chatClient.prompt()
                .system(system)
                .user(producto)
                .options(ChatOptions.builder()
                        .model("gpt-4o-mini")//Utiliza dentro de este metodo este cuando esta configurado
                        .temperature(0.86).build())
                .advisors(new SimpleLoggerAdvisor())
                .call()
                .content();
    }
}
