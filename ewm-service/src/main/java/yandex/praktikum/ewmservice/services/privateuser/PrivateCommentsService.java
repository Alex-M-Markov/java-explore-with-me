package yandex.praktikum.ewmservice.services.privateuser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yandex.praktikum.ewmservice.entities.Comment;
import yandex.praktikum.ewmservice.entities.Event;
import yandex.praktikum.ewmservice.entities.dto.comment.CommentDto;
import yandex.praktikum.ewmservice.entities.dto.comment.NewCommentDto;
import yandex.praktikum.ewmservice.entities.mappers.CommentsMapper;
import yandex.praktikum.ewmservice.exceptions.CommentNotFoundException;
import yandex.praktikum.ewmservice.exceptions.EventNotFoundException;
import yandex.praktikum.ewmservice.exceptions.UserNotFoundException;
import yandex.praktikum.ewmservice.exceptions.WrongUserException;
import yandex.praktikum.ewmservice.repositories.CommentsRepository;
import yandex.praktikum.ewmservice.repositories.EventsRepository;
import yandex.praktikum.ewmservice.repositories.UsersRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentsService {

    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final CommentsRepository commentsRepository;

    @Transactional
    public CommentDto create(long userId, long eventId, NewCommentDto newCommentDto) {
        log.info("Публикуется комментарий к событию {}", eventId);
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException(eventId));
        if (event.getInitiator().getId().equals(userId)) {
            throw new WrongUserException("Нельзя оставлять комментарии на свои события");
        }
        Comment comment = CommentsMapper.fromNewCommentDto(newCommentDto,
                usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId)), event);
        CommentDto commentDtoReturn = CommentsMapper.toCommentDto(commentsRepository.save(comment));
        log.info("Комментарий к событию {} успешно создан", eventId);
        return commentDtoReturn;
    }

    @Transactional
    public CommentDto update(long userId, long eventId, CommentDto commentDto) {
        if (userId != commentDto.getUser()) {
            throw new WrongUserException("Можно изменять только свои комментарии");
        }
        log.info("Обновляется комментарий к событию {}", eventId);
        Comment comment = commentsRepository.findById(commentDto.getId())
                .orElseThrow(() -> new CommentNotFoundException(commentDto.getId()));
        comment.setText(commentDto.getText());
        if (!comment.isEdited()) {
            comment.setEdited(true);
        }
        log.info("Комментарий к событию {} успешно обновлен", eventId);
        return CommentsMapper.toCommentDto(comment);
    }

    @Transactional
    public void delete(long userId, long eventId, long commentId) {
        log.info("Удаляется комментарий к событию {}", eventId);
        Comment comment = commentsRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        if (userId != comment.getUser().getId()) {
            throw new WrongUserException("Можно удалять только свои комментарии");
        }
        commentsRepository.deleteById(commentId);
        log.info("Комментарий к событию {} успешно удален", eventId);
    }

}