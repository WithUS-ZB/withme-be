INSERT INTO festival (festival_id, title, img, link, start_dttm, end_dttm, created_dttm, updated_dttm, deleted_dttm)
VALUES
    (1L, 'title1', 'img1', 'link1', DATEADD('DAY', -1, NOW()), DATEADD('DAY', 1, NOW()), NOW(), NOW(), NULL),
    (2L, 'title2', 'img2', 'link2', DATEADD('DAY', -1, NOW()), DATEADD('DAY', 1, NOW()), NOW(), NOW(), NULL);