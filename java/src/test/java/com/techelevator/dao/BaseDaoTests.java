package com.techelevator.dao;

import com.techelevator.model.Campaign;
import com.techelevator.model.Donation;
import com.techelevator.model.User;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestingDatabaseConfig.class)
public abstract class BaseDaoTests {
    protected static final User USER_1 = new User(1, "user1", "user1", "ROLE_USER");
    protected static final User USER_2 = new User(2, "user2", "user2", "ROLE_USER");
    protected static final User USER_3 = new User(3, "user3", "user3", "ROLE_USER");
    protected static final Donation DONATION_1 = new Donation(1, USER_1, 1,
            "Fancy New Tech", 1000,
            LocalDateTime.of(2024, 1, 11, 0, 0), "Nice stuff with Tech!",
            false, true);
    protected static final Donation DONATION_2 = new Donation(2, USER_2, 2,
            "Fancy New Robot", 2000,
            LocalDateTime.of(2024, 1, 12, 0, 0), "Go Robots!", false, false);
    protected static final Donation DONATION_3 = new Donation(3, USER_3, 3,
            "Fancy New Computer", 3000,
            LocalDateTime.of(2024, 1, 13, 0, 0), "What the frick does Quantum" +
            " mean??", false, false);
    protected static final Donation DONATION_4 = new Donation(4, USER_1, 2,
            "Doomed to Fail", 2000,
            LocalDateTime.of(2024, 1, 11, 0, 0), "Not bad!", false, false);
    protected static final Campaign CAMPAIGN_1 = new Campaign(1, "Fancy New Tech", "We are making some cool new stuff using technology",
            1000000, LocalDateTime.of(2024, 1, 1, 0, 0),
            LocalDateTime.of(2024, 1, 21, 0, 0), false, false, USER_1);
    protected static final Campaign CAMPAIGN_2 = new Campaign(2, "Fancy New Robot", "We are making some cool new stuff using a cool robot",
            2000000, LocalDateTime.of(2024, 1, 2, 0, 0),
            LocalDateTime.of(2024, 1, 22, 0, 0), false, true, USER_2);
    protected static final Campaign CAMPAIGN_3 = new Campaign(3, "Fancy New Computer", "We are making some cool new stuff using a quantum computer",
            3000000, LocalDateTime.of(2024, 1, 3, 0, 0),
            LocalDateTime.of(2024, 1, 23, 0, 0), true, false, USER_3);
    protected static final Campaign CAMPAIGN_4 = new Campaign(4, "Doomed to " +
            "Fail", "Why do I even try", 100, LocalDateTime.of(2024, 1, 3, 0, 0),
            LocalDateTime.of(2024, 1, 23, 0, 0), true, false, USER_1);

    static {
        CAMPAIGN_1.setManagers(List.of(USER_1));
        CAMPAIGN_2.setManagers(List.of(USER_2));
        CAMPAIGN_3.setManagers(List.of(USER_3));
        CAMPAIGN_1.setDonations(List.of(DONATION_1));
        CAMPAIGN_2.setDonations(List.of(DONATION_2, DONATION_4));
        CAMPAIGN_3.setDonations(List.of(DONATION_3));
    }

    @Autowired
    protected DataSource dataSource;

    @After
    public void rollback() throws SQLException {
        dataSource.getConnection().rollback();
    }

}
