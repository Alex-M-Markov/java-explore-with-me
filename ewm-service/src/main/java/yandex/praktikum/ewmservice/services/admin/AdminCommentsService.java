package yandex.praktikum.ewmservice.services.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.mappers.CommentsMapper;
import yandex.praktikum.ewmservice.repositories.CommentsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AdminCommentsService {

    private final CommentsRepository commentsRepository;

    @Transactional
    public List<CommentDto> getAllOfUser(long userId) {
        log.info("Получаем все комментарии пользователя {}", userId);
        List<CommentDto> comments = commentsRepository.getCommentsByUserId(userId).stream()
                .map(CommentsMapper::toCommentDto)
                .collect(Collectors.toList());
        log.info("Все комментарии пользователя {} получены", userId);
        return comments;
    }

    @Transactional
    public List<CommentDto> findByText(String text) {
        log.info("Получаем все комментарии, содержащие {}", text);
        List<CommentDto> comments = commentsRepository.getCommentsByTextContainingIgnoreCase(text).stream()
                .map(CommentsMapper::toCommentDto)
                .collect(Collectors.toList());
        log.info("Осуществлен поиск комментариев, содержащих запрошенный текст");
        return comments;
    }

    @Transactional
    public void delete(long commentId) {
        log.info("Удаляется комментарий {}", commentId);
        commentsRepository.deleteById(commentId);
        log.info("Комментарий {} успешно удален", commentId);
    }

    @Transactional
    public void deleteAllOfUser(long userId) {
        log.info("Удаляются все комментарии пользователя {}", userId);
        commentsRepository.deleteAllByUserId(userId);
        log.info("Все комментарии пользователя {} успешно удалены", userId);
    }

}
