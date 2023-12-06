BEGIN TRANSACTION;

INSERT INTO users (username,password_hash,role) VALUES ('user1','user1','ROLE_USER'); -- id 1
INSERT INTO users (username,password_hash,role) VALUES ('user2','user2','ROLE_USER'); -- id 2
INSERT INTO users (username,password_hash,role) VALUES ('user3','user3','ROLE_USER'); -- id 3

INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public) -- id 1
VALUES ('Fancy New Tech', 'We are making some cool new stuff using technology', 1000000, '2024-1-1', '2024-1-21', false, false);
INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public) -- id 2
VALUES ('Fancy New Robot', 'We are making some cool new stuff using a cool robot', 2000000, '2024-1-2', '2024-1-22', false, true);
INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public) -- id 3
VALUES ('Fancy New Computer', 'We are making some cool new stuff using a quantum computer', 3000000, '2024-1-3', '2024-1-23', true, false);
INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, locked, public, deleted) -- id 4
VALUES ('Doomed to Fail', 'Why do I even try', 100, '2024-1-3', '2024-1-23', true, false, true);

INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (1, 1, true);
INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (2, 2, true);
INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (3, 3, true);
INSERT INTO campaign_manager (campaign_id, manager_id, creator) VALUES (4, 1, true);

INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment)
VALUES (1, 1, 1000, '2024-1-11', 'Nice stuff with Tech!'); -- id 1
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment)
VALUES (2, 2, 2000, '2024-1-12', 'Go Robots!'); -- id 2
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment)
VALUES (3, 3, 3000, '2024-1-13', 'What the frick does Quantum mean??'); -- id 3
INSERT INTO donation (donor_id, campaign_id, donation_amount, donation_date, donation_comment)
VALUES (1, 2, 2000, '2024-1-11', 'Not bad!'); -- id 4

COMMIT TRANSACTION;
