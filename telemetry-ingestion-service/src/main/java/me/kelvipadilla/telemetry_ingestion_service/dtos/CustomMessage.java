package me.kelvipadilla.telemetry_ingestion_service.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomMessage {
    private String title;
    private String content;

    public CustomMessage() {}

    public CustomMessage(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
