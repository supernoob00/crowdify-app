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
public,
locked
)
VALUES
('Giant Inflatable Hot Dog', 'Join the fun with our Giant Inflatable Hotdog! This whimsical masterpiece brings laughter to any event. Perfect for pool parties, picnics, and more. Help us bring this inflatable joy to life – support our campaign now! 🌭 #InflateTheFun',
300000, '2023-12-11 19:10:25', '2024-01-23 19:10:25', true, true),
('Pick Up Rocks', 'Turn cleanup into a joyous adventure! Back Pick Up Rocks and let''s make tidying up a blast. Your support fuels rock-inspired games, eco-friendly gear, and community events. Join the movement – pledge today and let''s have fun while making a positive impact! 🌟🪨 #RockPickUp',
200000, '2023-06-22 19:10:25', '2024-06-23 19:10:25', true, true),
('World''s Most Burnt Pizza', 'Support our quest for the Worlds Most Burnt Pizza! 🔥🍕 Help us turn culinary mishaps into a triumph. Your contribution fuels our crispy endeavors, and backers enjoy exclusive access to our charred creations. Let''s redefine perfection – back us now! #BurntPizzaMasterpiece',
50000, '2023-06-22 19:10:25', '2024-06-23 19:10:25', true, false),
('Support for Fred''s PHD', 'Help our Fish Get a Degree! Fred, our goldfish, dreams of being a marine biologist. We''re raising funds for an underwater university for Fred. With your support, he''ll dive into a world of knowledge. Let''s make Fred''s aquatic dreams come true! 🐟🎓 #FishyPhD',
5000000, '2023-06-22 19:10:25', '2024-06-23 19:10:25', true, false),
('We need some help!', 'Sike! we dont need help, made you look.',
5000000, '2023-06-22 19:10:25', '2024-06-23 19:10:25', false, false);

INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES
(1, 1, true),
(2, 3, true),
(2, 2, false),
(3, 4, true),
(4, 5, true),
(5, 5, true);

INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment, refunded, anonymous) VALUES
(1, 1, 5000, '2023-11-21 20:00:00','Happy to help!', false, false),
(2, 1, 5000, '2023-11-22 20:00:00','Going all in!', false, true),
(3, 1, 5000, '2023-11-23 20:00:00','Love it!', false, false),
(4, 1, 5000, '2023-11-24 20:00:00','Cool beans!', false, false),
(4, 1, 20000, '2023-12-11 20:00:00','This better take off...', false, false),
(5, 1, 10000, '2023-12-11 20:00:00','', false, false),
(1, 2, 1000, '2023-11-30 20:00:00','Might be interesting...', false, false),
(1, 2, 150000, '2023-11-30 20:00:00','Might be interesting...', false, false),
(4, 2, 5000, '2023-12-11 20:00:00','Rocks Rock!', false, true),
(5, 2, 7500, '2023-12-11 20:00:00','I just love geodude...', false, false),
(3, 3, 1000, '2024-12-13 20:00:00','Pizza!', false, false);

INSERT INTO spend_request (campaign_id, request_amount, request_name, request_description, request_approved, end_date) VALUES
(1, 30000,'I bet a little too much on the ponies...', 'Just need some help with my very normal addiction. The hotdog will come soon enough!', false, '2025-06-22 19:10:25'), --id 1
(2, 100000, 'Design for a new rock picker upper','Rocks 4 lyfe!', false, '2026-06-22 19:10:25'); --id 2

INSERT INTO vote (donor_id, request_id, vote_approved) VALUES
--invalid vote, because this is a manager (1, 1, true),
(2, 1, true),
(3, 1, false),
(4, 1, false),
(5, 1, true),
(1, 2, false),
(4, 2, true),
(5, 2, true);
--invalid vote, because this is not a donor to the campaign (4, 1, true)

COMMIT TRANSACTION;