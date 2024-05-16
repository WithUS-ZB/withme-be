package util.objectprovider;

import com.withus.withmebe.comment.dto.request.AddCommentRequest;
import com.withus.withmebe.comment.entity.Comment;
import com.withus.withmebe.member.entity.Member;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class CommentProvider {

  private CommentProvider() {
  }

  public static Comment getStubbedComment(long commentId, long gatheringId, Member writer) {
    return getStubbedCommentWithAddCommentRequest(commentId, gatheringId, writer,
        new AddCommentRequest("댓글내용" + commentId));
  }

  public static Comment getStubbedCommentWithAddCommentRequest(long commentId, long gatheringId,
      Member requester, AddCommentRequest request) {
    Comment comment = Comment.builder()
        .writer(requester)
        .commentContent(request.commentContent())
        .gatheringId(gatheringId)
        .build();
    ReflectionTestUtils.setField(comment, "id", commentId);
    ReflectionTestUtils.setField(comment, "createdDttm", LocalDateTime.now());
    ReflectionTestUtils.setField(comment, "updatedDttm", LocalDateTime.now());
    return comment;
  }
}
