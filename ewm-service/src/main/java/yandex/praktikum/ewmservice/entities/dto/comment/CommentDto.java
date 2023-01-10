package yandex.praktikum.ewmservice.entities.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class CommentDto {

    @NotNull
    private Long id;
    @NotBlank
    @Size(max = 512)
    private String text;
    @NotBlank
    private Long event;
    @NotBlank
    private Long user;
    @NotNull
    private boolean edited;

}
