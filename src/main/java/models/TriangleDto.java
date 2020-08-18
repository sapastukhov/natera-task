package models;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@EqualsAndHashCode
@Builder
public class TriangleDto {
    private String id;
    private String firstSide;
    private String secondSide;
    private String thirdSide;
}
