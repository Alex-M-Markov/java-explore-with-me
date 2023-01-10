package yandex.praktikum.ewmservice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.repositories.CommentsRepository;
import yandex.praktikum.ewmservice.services.privateuser.PrivateCommentsService;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PrivateCommentsServiceTests {

    private final PrivateCommentsService privateCommentsService;
    private final CommentsRepository commentsRepository;
    private CommentDto commentDto1;
    private CommentDto commentDto2;
    private CommentDto commentDto3;

    @BeforeEach
    void initialize() {
        commentDto1 = CommentDto.builder()
                .withUser(1L)
                .withEvent(10L)
                .withText("It was stunning :-)")
                .build();
        commentDto2 = CommentDto.builder()
                .withUser(5L)
                .withEvent(6L)
                .withText("I've been waiting so long to hear his voice!")
                .build();
        commentDto3 = CommentDto.builder()
                .withUser(10L)
                .withEvent(1L)
                .withText("Boooring...")
                .build();
    }

    @AfterEach
    void clear() {
        commentDto1 = null;
        commentDto2 = null;
        commentDto3 = null;
    }

/*
    @Test
    public void create() {
        privateCommentsService.create(commentDto1);
        assertThat(commentsRepository.getFilmById(4L).getName()).isEqualTo("The Godfather");
    }
*/


}
