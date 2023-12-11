BEGIN TRANSACTION;

-- USERS WHO MANAGE/CREATE CAMPAIGNS
INSERT INTO users (username,email,password_hash,role) VALUES ('user1','user1@email.com','user1','ROLE_USER'); -- id 1
INSERT INTO users (username,email,password_hash,role) VALUES ('user2','user2@email.com','user2','ROLE_USER'); -- id 2
INSERT INTO users (username,email,password_hash,role) VALUES ('user3','user3@email.com','user3','ROLE_USER'); -- id 3
INSERT INTO users (username,email,password_hash,role) VALUES ('user4','user4@email.com','user4','ROLE_USER'); -- id 4

-- USERS WHO ONLY DONATE/VOTE
INSERT INTO users (username,password_hash,role) VALUES ('user5','user5','ROLE_USER'); -- id 5
INSERT INTO users (username,password_hash,role) VALUES ('user6','user6','ROLE_USER'); -- id 6
INSERT INTO users (username,password_hash,role) VALUES ('user7','user7','ROLE_USER'); -- id 7
INSERT INTO users (username,password_hash,role) VALUES ('user8','user8','ROLE_USER'); -- id 8

-- CAMPAIGN CREATION
INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public) -- id 1
VALUES ('Fancy New Tech', 'We are making some cool new stuff using technology', 1000000, '2024-1-1', '2024-1-21', false, false);
INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public) -- id 2
VALUES ('Fancy New Robot', 'We are making some cool new stuff using a cool robot', 2000000, '2024-1-2', '2024-1-22', false, true);
INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public) -- id 3
VALUES ('Fancy New Computer', 'We are making some cool new stuff using a quantum computer', 3000000, '2024-1-3', '2024-1-23', true, false);
INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public, deleted) -- id 4
VALUES ('Doomed to Fail', 'Why do I even try', 100, '2024-1-3', '2024-1-23', true, false, true);

-- MANAGER ASSIGNMENT TO USERS 1, 2, 3
INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (1, 1, true);
INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (2, 2, true);
INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (3, 3, true);
INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (4, 1, true);

-- MANAGER DONATIONS
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment, anonymous)
VALUES (1, 1, 1000, '2024-1-11', 'Nice stuff with Tech!', true); -- id 1
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment, anonymous)
VALUES (2, 2, 2000, '2024-1-12', 'Go Robots!', true); -- id 2
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment, anonymous)
VALUES (3, 3, 3000, '2024-1-13', 'What the frick does Quantum mean??', false); -- id 3
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment, anonymous)
VALUES (1, 2, 2000, '2024-1-11', 'Not bad!', true); -- id 4

-- NON-MANAGER DONATIONS FOR VOTE TESTING PURPOSES
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment, anonymous)
VALUES (5, 1, 1000, '2024-1-15', 'Nice!', false); -- id 5


-- SPEND REQUESTS FOR CAMPAIGNS 1 AND 2
INSERT INTO spend_request (campaign_id, request_name, request_amount, request_description, request_approved, end_date)
VALUES (1, 'More Money', 5000, 'Extra Awesome Tech Stuff', false, '2024-1-24'); -- id 1
INSERT INTO spend_request (campaign_id, request_name, request_amount, request_description, request_approved, end_date)
VALUES (2, 'All the Money', 10000, 'Robot Arms', false, '2024-1-24'); -- id 2


INSERT INTO vote (donor_id, request_id, vote_approved)
VALUES (1, 2, false);


COMMIT TRANSACTION;
