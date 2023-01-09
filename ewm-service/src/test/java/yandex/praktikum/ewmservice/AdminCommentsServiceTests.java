package yandex.praktikum.ewmservice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.dto.comment.NewCommentDto;
import yandex.praktikum.ewmservice.repositories.CommentsRepository;
import yandex.praktikum.ewmservice.services.admin.AdminCommentsService;
import yandex.praktikum.ewmservice.services.privateuser.PrivateCommentsService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AdminCommentsServiceTests {

    private final AdminCommentsService adminCommentsService;
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
    public void getAllOfUser() {
        createThreeComments();
        List<CommentDto> commentsOfUser = adminCommentsService.getAllOfUser(10L);
        assertThat(commentsOfUser.size()).isEqualTo(3);
    }

    @Test
    public void delete() {
        Long commentId1 = privateCommentsService.create(8L, 2L, newCommentDto2).getId();
        Long commentId2 = privateCommentsService.create(8L, 2L, newCommentDto1).getId();
        Long commentId3 = privateCommentsService.create(8L, 3L, newCommentDto3).getId();
        assertThat(adminCommentsService.getAllOfUser(8L).size()).isEqualTo(3);
        adminCommentsService.delete(commentId3);
        assertThat(adminCommentsService.getAllOfUser(8L).size()).isEqualTo(2);
        adminCommentsService.delete(commentId2);
        adminCommentsService.delete(commentId1);
        assertThat(adminCommentsService.getAllOfUser(8L).size()).isEqualTo(0);
    }

    @Test
    public void deleteAllOfUser() {
        createThreeComments();
        adminCommentsService.deleteAllOfUser(10L);
        assertThat(adminCommentsService.getAllOfUser(10L)).isEmpty();
    }

    @Test
    public void findByText() {
        createThreeCommentsByDifferentUsers();
        assertThat(adminCommentsService.findByText("stunning").size()).isEqualTo(1);
        assertThat(adminCommentsService.findByText("Ing").size()).isEqualTo(3);
    }

    private void createThreeComments() {
        privateCommentsService.create(10L, 3L, newCommentDto1);
        privateCommentsService.create(10L, 1L, newCommentDto2);
        privateCommentsService.create(10L, 2L, newCommentDto3);
    }

    private void createThreeCommentsByDifferentUsers() {
        privateCommentsService.create(7L, 3L, newCommentDto1);
        privateCommentsService.create(8L, 1L, newCommentDto2);
        privateCommentsService.create(7L, 2L, newCommentDto3);
    }

}
