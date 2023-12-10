BEGIN TRANSACTION;

ALTER SEQUENCE users_user_id_seq RESTART WITH 1;
ALTER SEQUENCE campaign_campaign_id_seq RESTART WITH 1;
ALTER SEQUENCE donation_donation_id_seq RESTART WITH 1;
ALTER SEQUENCE spend_request_request_id_seq RESTART WITH 1;

--username: "password"
INSERT INTO users (username, email, password_hash,role) VALUES ('user', 'test@test.com', '$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username, email, password_hash,role) VALUES ('sam', 'sjsomerdin@gmail.com', '$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username, password_hash,role) VALUES ('adi','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username, password_hash,role) VALUES ('chad','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username, password_hash,role) VALUES ('josh','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username, password_hash,role) VALUES ('admin','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_ADMIN');

INSERT INTO campaign
(
campaign_name,
description,
funding_goal,
start_date,
end_date,
locked,
public
)
VALUES
('Giant Inflatable Hot Dog', 'Yum', 30000, '2020-06-22 19:10:25', '2020-06-23 19:10:25', false, false),
('Pick Up Rocks', 'I like rocks', 10000, '2023-06-22 19:10:25', '2027-06-23 19:10:25', true, true),
('World''s Most Burnt Pizza', 'Eat some pizza', 5000, '2023-06-22 19:10:25', '2027-06-23 19:10:25', false, true);

INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES
(1, 1, true),
(1, 2, false),
(2, 3, true),
(3, 1, true);

INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment, refunded, anonymous) VALUES
(1, 1, 5000, '2020-06-22 20:00:00','Happy to help!', false, false),
(1, 2, 500, '2024-06-22 20:00:00','Might be interesting...', false, false),
(2, 1, 5000, '2020-06-22 20:00:00','Going all in!', false, true),
(3, 1, 5000, '2020-06-22 20:00:00','Love it!', false, false),
(4, 2, 5000, '2024-06-22 20:00:00','It''s cool to be able to support in a real way with my money in this fashion!', false, true);

INSERT INTO spend_request (campaign_id, request_amount, request_name, request_description, request_approved, end_date) VALUES
(1, 2000,'I''m going waste this money', 'some monies', false, '2025-06-22 19:10:25'), --id 1
(1, 1500,'Throwin away some more lol', 'more money', false, '2026-06-22 19:10:25'), --id 2
(2, 2100, 'I need a new car','cash', false, '2026-06-22 19:10:25'); --id 3

INSERT INTO vote (donor_id, request_id, vote_approved) VALUES
--invalid vote, because this is a manager (1, 1, true),
(3, 1, true),
(4, 3, true);
--invalid vote, because this is not a donor to the campaign (4, 1, true)

COMMIT TRANSACTION;