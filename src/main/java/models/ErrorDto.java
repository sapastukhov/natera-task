package models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
@Builder
public class ErrorDto {
    private long timestamp;
    private int status;
    private String error;
    private String exception;
    private String message;
    private String path;
}
