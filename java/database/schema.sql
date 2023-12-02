CREATE FUNCTION check_campaign_single_creator() RETURNS trigger AS $check_single_creator$
    BEGIN
        invalid_counts integer;

        --check if any campaign has more or less than one creator
        SELECT COUNT creator
        INTO invalid_counts
        FROM (COUNT creator FROM campaign_manager WHERE creator GROUP BY campaign)
        WHERE creator != 1;

        IF invalid_counts > 0 THEN
            RAISE EXCEPTION 'Every campaign must have exactly one creator.';
        END IF;
        RETURN NEW;
    END;
$check_single_creator$ LANGUAGE plpgsql;

CREATE TRIGGER check_campaign_single_creator BEFORE INSERT OR UPDATE OR DELETE ON campaign_manager
    EXECUTE PROCEDURE check_campaign_single_creator();

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
    campaign_name varchar(50) NOT NULL UNIQUE,
    description varchar(500) NOT NULL,
    funding_goal integer NOT NULL,
    start_date timestamp NOT NULL,
    end_date timestamp NOT NULL,
    locked boolean DEFAULT false NOT NULL, --TODO: what default?
    public boolean DEFAULT false NOT NULL,

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
    --TODO: add constraint for single creator (should use trigger)
);

CREATE TABLE donation (
    donation_id SERIAL,
    donor_id integer,
    campaign_id integer NOT NULL,
    donation_amount integer NOT NULL,
    donation_date timestamp NOT NULL,
    donation_comment varchar(200),
    donation_status varchar(20), --TODO: pending, approved, rejected statuses? Add constraint that only be these statuses Also what happens if campaign is locked/made private

    CONSTRAINT pk_donation_id PRIMARY KEY (donation_id),
    CONSTRAINT fk_campaign_id FOREIGN KEY (campaign_id) REFERENCES campaign (campaign_id),
    CONSTRAINT fk_donor_user_id FOREIGN KEY (donor_id) REFERENCES users (user_id),
    CONSTRAINT valid_donation_amount CHECK (donation_amount > 0 AND donation_amount <= 50000000),
    CONSTRAINT donation_date_between_start_end_dates
        CHECK
        (
        donation_date
        BETWEEN
        (SELECT (start_date, end_date) FROM campaign WHERE campaign.campaign_id = donation.campaign_id)
        )
);

CREATE TABLE spend_request (
    request_id SERIAL,
    campaign_id integer NOT NULL,
    request_amount integer NOT NULL, --TODO: constraint less than current funds - all donations minus all spend requests (should use trigger)
    request_description varchar(500) NOT NULL,
    request_approved boolean DEFAULT false NOT NULL, --TODO: ask Jennifer if manager needs to manually approved spend requests, also add constraint cannot be true if majority has not approved
    end_date date NOT NULL, --TODO: Ask Jennifer is end date needed

    CONSTRAINT pk_request_id PRIMARY KEY (request_id),
    CONSTRAINT fk_campaign_id FOREIGN KEY (campaign_id) REFERENCES campaign (campaign_id),
    CONSTRAINT approved_only_with_majority_vote
        CHECK
        (
        (NOT approved)
        OR
        (SELECT COUNT donor_id FROM vote WHERE vote.request_id = spend_request.request_id AND vote_approved)
        >
        ((SELECT COUNT donor_id FROM vote WHERE vote.request_id = spend_request.request_id) / 2.0)
        )
);

CREATE TABLE vote (
    donor_id integer NOT NULL, --TODO: add constraint only donors for campaign and not a manager
    request_id integer NOT NULL,
    vote_approved boolean, --TODO: ask jennifer is users can reject spend requests, also default null

    CONSTRAINT pk_donor_request_id PRIMARY KEY (donor_id, request_id),
    CONSTRAINT fk_donor_user_id FOREIGN KEY (donor_id) REFERENCES users (user_id),
    CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES spend_request (request_id),
    CONSTRAINT no_manager_vote
        CHECK
        (
        SELECT COUNT donor_id FROM vote INNER JOIN campaign_manager ON (donor_id = manager_id) = 0
        )
);

COMMIT TRANSACTION;


