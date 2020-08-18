package models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
@Builder
public class TriangleReqDto {
    private String separator;
    private String input;
}
