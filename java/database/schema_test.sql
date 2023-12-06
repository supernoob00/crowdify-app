BEGIN TRANSACTION;

DROP TABLE IF EXISTS users, campaign, campaign_manager, donation, spend_request, vote CASCADE;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,

	CONSTRAINT pk_user PRIMARY KEY (user_id)
);

CREATE TABLE campaign (
    campaign_id SERIAL,
    campaign_name varchar(50) NOT NULL,
    description varchar(500) NOT NULL,
    funding_goal integer NOT NULL,
    start_date timestamp NOT NULL, 
    end_date timestamp NOT NULL,
    locked boolean DEFAULT false NOT NULL, --TODO: what default?
    public boolean DEFAULT false NOT NULL,
    deleted boolean DEFAULT false NOT NULL,

    CONSTRAINT pk_campaign PRIMARY KEY (campaign_id),
    CONSTRAINT valid_funding_goal CHECK (funding_goal >= 100),
    --TODO: maybe modify this constraint
    --end date must be at least 24 hours after start date
    CONSTRAINT valid_dates_end_after_start
        CHECK (EXTRACT (EPOCH FROM end_date) - EXTRACT (EPOCH FROM start_date) >= 86400)
);

CREATE TABLE campaign_manager (
    campaign_id integer,
    manager_id integer,
    creator boolean DEFAULT true NOT NULL, --TODO: add constraint only non-creator managers can be removed, also if creator already exists default FALSE

    CONSTRAINT pk_campaign_manager PRIMARY KEY (campaign_id, manager_id),
    CONSTRAINT fk_campaign_id FOREIGN KEY (campaign_id) REFERENCES campaign (campaign_id),
    CONSTRAINT fk_manager_user_id FOREIGN KEY (manager_id) REFERENCES users (user_id)
);

CREATE TABLE donation (
    donation_id SERIAL,
    donor_id integer,
    campaign_id integer NOT NULL,
    donation_amount integer NOT NULL,
    donation_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    donation_comment varchar(200),
    refunded boolean DEFAULT false NOT NULL, --TODO: pending, approved, rejected statuses? Add constraint that only be these statuses Also what happens if campaign is locked/made private

    CONSTRAINT pk_donation_id PRIMARY KEY (donation_id),
    CONSTRAINT fk_campaign_id FOREIGN KEY (campaign_id) REFERENCES campaign (campaign_id),
    CONSTRAINT fk_donor_user_id FOREIGN KEY (donor_id) REFERENCES users (user_id),
    CONSTRAINT valid_donation_amount CHECK (donation_amount > 0 AND donation_amount <= 50000000)
);

CREATE TABLE spend_request (
    request_id SERIAL,
    campaign_id integer NOT NULL,
    request_amount integer NOT NULL, --TODO: constraint less than current funds - all donations minus all spend requests (should use trigger)
    request_description varchar(500) NOT NULL,
    request_approved boolean DEFAULT false NOT NULL, --TODO: ask Jennifer if manager needs to manually approved spend requests, also add constraint cannot be true if majority has not approved
    end_date timestamp, --TODO: Ask Jennifer is end date needed

    CONSTRAINT pk_request_id PRIMARY KEY (request_id),
    CONSTRAINT fk_campaign_id FOREIGN KEY (campaign_id) REFERENCES campaign (campaign_id),
    CONSTRAINT request_amount_valid CHECK (request_amount > 0)
);

CREATE TABLE vote (
    donor_id integer NOT NULL, --TODO: add constraint only donors for campaign and not a manager
    request_id integer NOT NULL,
    vote_approved boolean, --TODO: ask jennifer is users can reject spend requests, also default null

    CONSTRAINT pk_donor_request_id PRIMARY KEY (donor_id, request_id),
    CONSTRAINT fk_donor_user_id FOREIGN KEY (donor_id) REFERENCES users (user_id),
    CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES spend_request (request_id)
);

COMMIT TRANSACTION;

--- *** ---

DROP FUNCTION IF EXISTS check_campaign_single_creator;
DROP FUNCTION IF EXISTS donation_date_between_start_end_dates;
DROP FUNCTION IF EXISTS spend_request_approved_only_with_majority_vote;
DROP FUNCTION IF EXISTS only_non_manager_donors_vote_spend_request;

CREATE FUNCTION check_campaign_single_creator() RETURNS trigger AS '
	DECLARE
		invalid_counts integer;
    BEGIN
        --check if any campaign has more or less than one creator
        SELECT COUNT (campaign_id)
        FROM
        (SELECT campaign_id, COUNT (creator) AS creator_count FROM campaign_manager WHERE creator GROUP BY campaign_id) as counts
        INTO invalid_counts
        WHERE counts.creator_count != 1;

        IF invalid_counts > 0 THEN
            RAISE EXCEPTION ''Every campaign must have exactly one creator.'';
        END IF;
        RETURN NEW;
    END;' LANGUAGE plpgsql;

--TODO: needs to be tested
CREATE FUNCTION donation_date_between_start_end_dates() RETURNS trigger AS '
	DECLARE
		start_end_dates campaign%ROWTYPE;
    BEGIN
		SELECT (start_date, end_date)
		FROM campaign
		INTO start_end_dates
		WHERE (campaign.campaign_id = NEW.campaign_id);

        IF (NOT (NEW.donation_date > start_end_dates.start_date AND NEW.donation_date < start_end_dates.end_date)) THEN
            RAISE EXCEPTION ''Donation date must be between start and end dates of campaign.'';
        END IF;
        RETURN NEW;
    END;' LANGUAGE plpgsql;

--TODO: needs to be tested
CREATE FUNCTION spend_request_approved_only_with_majority_vote() RETURNS trigger AS '
	DECLARE
		approve_votes integer;
        total_votes integer;
    BEGIN
        SELECT COUNT (donor_id)
        INTO approve_votes
        FROM vote
        WHERE vote.request_id = NEW.request_id AND vote_approved;

        SELECT COUNT (donor_id)
        INTO total_votes
        FROM vote
        WHERE vote.request_id = NEW.request_id;

        IF NEW.approved AND (2 * approve_votes <= total_votes) THEN
            RAISE EXCEPTION ''A spend request cannot be approved if not a majority vote'';
        END IF;
        RETURN NEW;
    END;' LANGUAGE plpgsql;

CREATE FUNCTION only_non_manager_donors_vote_spend_request() RETURNS trigger AS '
	DECLARE
		manager_voter_count integer;
        non_donor_voter_count integer;
    BEGIN
        --count of voters for spend request who are a manager of the associated campaign
        SELECT COUNT (DISTINCT donor_id)
        FROM vote
        INTO manager_voter_count
        INNER JOIN spend_request USING (request_id)
        INNER JOIN campaign_manager
        ON (vote.donor_id = campaign_manager.manager_id)
        AND (spend_request.campaign_id = campaign_manager.campaign_id);

        IF manager_voter_count > 0 THEN
            RAISE EXCEPTION ''A manager cannot vote for a spend request of their own campaign.'';
        END IF;

        --count of voters for spend request who are not a donor
        SELECT COUNT (vote.donor_id)
        FROM vote
        INTO non_donor_voter_count
        INNER JOIN spend_request using (request_id)
        LEFT JOIN donation
        ON (vote.donor_id = donation.donor_id)
        AND (spend_request.campaign_id = donation.campaign_id)
        WHERE (donation.donation_id IS null);

        IF non_donor_voter_count > 0 THEN
            RAISE EXCEPTION ''Only donors to a campaign can vote for that campaign spend requests.'';
        END IF;
        RETURN NEW;
    END;' LANGUAGE plpgsql;

CREATE TRIGGER check_campaign_single_creator BEFORE INSERT OR UPDATE OR DELETE ON campaign_manager
    EXECUTE PROCEDURE check_campaign_single_creator();

CREATE TRIGGER donation_date_between_start_end_dates BEFORE INSERT OR UPDATE ON donation
    EXECUTE PROCEDURE donation_date_between_start_end_dates();

CREATE TRIGGER spend_request_approved_only_with_majority_vote BEFORE INSERT OR UPDATE on spend_request
    EXECUTE PROCEDURE spend_request_approved_only_with_majority_vote();

CREATE TRIGGER only_non_manager_donors_vote_spend_request BEFORE INSERT OR UPDATE on vote
    EXECUTE PROCEDURE only_non_manager_donors_vote_spend_request();


