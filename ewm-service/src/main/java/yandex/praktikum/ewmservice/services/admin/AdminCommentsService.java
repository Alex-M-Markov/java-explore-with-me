package yandex.praktikum.ewmservice.services.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.Comment;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.mappers.CommentsMapper;
import yandex.praktikum.ewmservice.exceptions.CommentNotFoundException;
import yandex.praktikum.ewmservice.repositories.CommentsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCommentsService {

    private final CommentsRepository commentsRepository;

    @Transactional
    public List<CommentDto> getAllOfUser(Long userId, int from, int size) {
        log.info("Получаем все комментарии пользователя {}", userId);
        List<Comment> comments = commentsRepository.getCommentsByUserId(userId, PageRequest.of(from, size));
        log.info("Все комментарии пользователя {} получены", userId);
        return comments.stream()
                .map(CommentsMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CommentDto> findByText(String text, int from, int size) {
        log.info("Получаем все комментарии, содержащие {}", text);
        List<Comment> comments = commentsRepository.getCommentsByTextContainingIgnoreCase(text,
                PageRequest.of(from, size));
        log.info("Осуществлен поиск комментариев, содержащих запрошенный текст");
        return comments.stream()
                .map(CommentsMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long commentId) {
        log.info("Удаляется комментарий {}", commentId);
        commentsRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(commentId));
        commentsRepository.deleteById(commentId);
        log.info("Комментарий {} успешно удален", commentId);
    }

    @Transactional
    public void deleteAllOfUser(Long userId) {
        log.info("Удаляются все комментарии пользователя {}", userId);
        commentsRepository.deleteAllByUserId(userId);
        log.info("Все комментарии пользователя {} успешно удалены", userId);
    }

}
