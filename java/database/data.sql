BEGIN TRANSACTION;

ALTER SEQUENCE users_user_id_seq RESTART WITH 1;
ALTER SEQUENCE campaign_campaign_id_seq RESTART WITH 1;
ALTER SEQUENCE donation_donation_id_seq RESTART WITH 1;
ALTER SEQUENCE spend_request_request_id_seq RESTART WITH 1;

--username: "password"
INSERT INTO users (username,password_hash,role) VALUES ('user','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('sam','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('adi','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('chad','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('josh','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_USER');
INSERT INTO users (username,password_hash,role) VALUES ('admin','$2a$10$3i4qTmbIul5.SPOo.5oFHOQnSPBcC5ryKiOyekvEYXSuy5HqsYkre','ROLE_ADMIN');

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
(3, 1, false);

INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_status) VALUES
(1, 1, 5000, '2020-06-22 20:00:00', 'sent'),
(1, 2, 500, '2024-06-22 20:00:00', 'sent'),
(2, 1, 5000, '2020-06-22 20:00:00', 'sent'),
(3, 1, 5000, '2020-06-22 20:00:00', 'sent'),
(4, 2, 5000, '2024-06-22 20:00:00', 'sent');

INSERT INTO spend_request (campaign_id, request_amount, request_description, request_approved) VALUES
(1, 100, 'some monies', false),
(1, 50, 'more money', false),
(2, 50, 'cash', false);

INSERT INTO vote (donor_id, request_id, vote_approved) VALUES
--invalid vote, because this is a manager (1, 1, true),
(1, 2, true),
(2, 1, true);
--invalid vote, because this is not a donor to the campaign (4, 1, true)

COMMIT TRANSACTION;