package org.transformers.megatron;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final String name = "Shockwave";
    private final String allegiance = "Decepticon";

    @Value("${transformer.mode:disguised}")
    private String mode;

    @Value("${transformer.disguised:GUN}")
    private String disguised;

    @Value("${transformer.robot:ROBOT}")
    private String robot;

    @GetMapping
    public String transformer() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<h1>");
        stringBuilder.append("Name: ").append(name).append("<br/>");
        stringBuilder.append("Allegiance: ").append(allegiance).append("<br/>");
        stringBuilder.append("Mode: ").append(mode).append("<br/>");
        stringBuilder.append("</h1>");
        if ("robot".equalsIgnoreCase(mode)) {
            stringBuilder.append(robot);
        } else if ("disguised".equalsIgnoreCase(mode)) {
            stringBuilder.append(disguised);
        }

        return stringBuilder.toString();
    }
}