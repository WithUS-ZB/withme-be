INSERT INTO Member (member_id, email, password, nickName, birthDate, gender, phoneNumber, profileimg, role, signupPath, signupDttm, membership, createdDttm, updatedDttm, deletedDttm)
VALUES
    (1L, 'user1@example.com', 'password1', 'User1', '1990-01-01', 'MALE', '01012345678', 'profile1.jpg', 'ROLE_MEMBER', 'NORMAL', NOW(), 'FREE', NOW(), NOW(), null),
    (2L, 'user2@example.com', 'password2', 'User2', '1995-05-05', 'FEMALE', '01098765432', 'profile2.jpg', 'ROLE_MEMBER', 'NORMAL', NOW(), 'FREE', NOW(), NOW(), null),
    (3L, 'user3@example.com', 'password3', 'User3', '1988-12-25', 'OTHER', '01011112222', 'profile3.jpg', 'ROLE_MEMBER', 'NORMAL', NOW(), 'FREE', NOW(), NOW(), NOW());

INSERT INTO Comment (comment_id, gatheringId, member_id, commentContent, createdDttm, updatedDttm, deletedDttm)
VALUES
    (1L, 1L, 1L, 'comment1', NOW(), NOW(), null),
    (2L, 1L, 2L, 'comment2', NOW(), NOW(), null),
    (3L, 2L, 1L, 'comment3', NOW(), NOW(), null),
    (4L, 1L, 1L, 'comment4', NOW(), NOW(), NOW());
