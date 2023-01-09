package yandex.praktikum.ewmservice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.dto.comment.NewCommentDto;
import yandex.praktikum.ewmservice.entities.mappers.CommentsMapper;
import yandex.praktikum.ewmservice.exceptions.CommentNotFoundException;
import yandex.praktikum.ewmservice.repositories.CommentsRepository;
import yandex.praktikum.ewmservice.services.privateuser.PrivateCommentsService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PrivateCommentsServiceTests {

    private final PrivateCommentsService privateCommentsService;
    private final CommentsRepository commentsRepository;
    private NewCommentDto newCommentDto1;
    private NewCommentDto newCommentDto2;
    private NewCommentDto newCommentDto3;

    @BeforeEach
    void initialize() {
        newCommentDto1 = NewCommentDto.builder()
                .withText("It was stunning :-)")
                .build();
        newCommentDto2 = NewCommentDto.builder()
                .withText("I've been waiting so long to hear his voice!")
                .build();
        newCommentDto3 = NewCommentDto.builder()
                .withText("Boooring...")
                .build();
        commentsRepository.deleteAll();
    }

    @Test
    public void create() {
        Long commentId = privateCommentsService.create(1L, 3L, newCommentDto1).getId();
        CommentDto commentDtoReturned = CommentsMapper.toCommentDto(commentsRepository.findById(commentId).orElseThrow(()
                -> new CommentNotFoundException(commentId)));
        assertThat(commentDtoReturned.getText()).isEqualTo(newCommentDto1.getText());
        assertThat(commentDtoReturned.getText()).isNotEqualTo("foo");
        assertThat(commentDtoReturned.getUser()).isEqualTo(1L);
        assertThat(commentDtoReturned.getEvent()).isEqualTo(3L);
    }

    @Test
    public void update() {
        Long commentId = privateCommentsService.create(8L, 1L, newCommentDto2).getId();
        CommentDto commentDtoReturned = CommentsMapper.toCommentDto(commentsRepository.findById(commentId).orElseThrow(()
                -> new CommentNotFoundException(commentId)));
        String newText = "Sorry, posted by mistake";
        commentDtoReturned.setText(newText);
        privateCommentsService.update(8L, 1L, commentDtoReturned);
        CommentDto commentDtoReturnedUpdated = CommentsMapper.toCommentDto(commentsRepository.findById(commentId).orElseThrow(()
                -> new CommentNotFoundException(commentId)));
        assertThat(commentDtoReturnedUpdated.getText()).isEqualTo(newText);
        assertThat(commentDtoReturnedUpdated.isEdited()).isTrue();
    }

    @Test
    public void delete() {
        Long commentId = privateCommentsService.create(5L, 2L, newCommentDto3).getId();
        privateCommentsService.delete(5L, 2L, commentId);
        CommentNotFoundException thrown = Assertions.assertThrows(CommentNotFoundException.class, ()
                -> commentsRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId)));
        Assertions.assertEquals("Комментарий " + commentId + " не найден", thrown.getMessage());
    }

}
