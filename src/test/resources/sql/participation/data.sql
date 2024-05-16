INSERT INTO member (member_id, email, password, nick_name, birth_date, gender, phone_number, profile_img, role, signup_path, signup_dttm, membership, created_dttm, updated_dttm, deleted_dttm)
VALUES
    (1L, 'user1@example.com', 'password1', 'User1', '1990-01-01', 'MALE', '01012345678', 'profile1.jpg', 'ROLE_MEMBER', 'NORMAL', NOW(), 'FREE', NOW(), NOW(), NULL),
    (2L, 'user2@example.com', 'password2', 'User2', '1995-05-05', 'FEMALE', '01098765432', 'profile2.jpg', 'ROLE_MEMBER', 'NORMAL', NOW(), 'FREE', NOW(), NOW(), NULL),
    (3L, 'user3@example.com', 'password3', 'User3', '1988-12-25', 'NONE', '01011112222', 'profile3.jpg', 'ROLE_MEMBER', 'NORMAL', NOW(), 'FREE', NOW(), NOW(), NULL),
    (4L, 'user4@example.com', 'password4', 'User4', '1990-10-05', 'MALE', '01022223333', 'profile4.jpg', 'ROLE_MEMBER', 'NORMAL', NOW(), 'FREE', NOW(), NOW(), NULL);

INSERT INTO gathering (gathering_id, member_id, title, content, gathering_type, maximum_participant, day, time, recruitment_start_dt, recruitment_end_dt, category, address, detailed_address, lat, lng, main_img, participants_type, fee, participant_selection_method, status, created_dttm, updated_dttm, deleted_dttm)
VALUES (1L, 1L, 'Title1', 'Content1', 'MEETING', 30, '2024-05-11', '12:00:00', '2024-05-11', '2024-05-12', 'Category1', 'Address1', 'DetailedAddress1', 37.1234, 127.5678, 'MainImage1', 'ADULT', 0, 'FIRST_COME', 'PROGRESS', NOW(), NOW(), NULL),
       (2L, 1L, 'Title2', 'Content2', 'EVENT', 40, '2024-05-12', '13:00:00', '2024-05-11', '2024-05-12', 'Category2', 'Address2', 'DetailedAddress2', 37.2345, 127.6789, 'MainImage2', 'MINOR', 10, 'UNLIMITED_APPLICATION', 'CANCELED', NOW(), NOW(), NULL);

INSERT INTO participation (participation_id, gathering_id, member_id, status, created_dttm, updated_dttm, deleted_dttm)
VALUES
    (1L, 1L, 2L, 'APPROVED', NOW(), NOW(), NULL),
    (2L, 1L, 3L, 'APPROVED', NOW(), NOW(), NULL),
    (3L, 2L, 3L, 'CANCELED', NOW(), NOW(), NULL),
    (4L, 2L, 2L, 'REJECTED', NOW(), NOW(), NULL);