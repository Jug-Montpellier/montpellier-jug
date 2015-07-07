package org.jug.montpellier.jobs;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.montpellierjug.store.jooq.tables.daos.YearpartnerDao;
import org.ow2.chameleon.mail.Mail;
import org.ow2.chameleon.mail.MailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.View;
import org.wisdom.api.annotations.scheduler.Every;
import org.wisdom.api.configuration.ApplicationConfiguration;
import org.wisdom.api.scheduler.Scheduled;
import org.wisdom.api.templates.Template;

import com.google.common.collect.ImmutableMap;

/**
 * Created by fteychene on 17/06/2015.
 */
@Component
@Provides
@Instantiate
public class ExpiringPartnerJob implements Scheduled {

    private static final Long DEFAULT_EXPIRING_TIMESTAMP = 2592000000l;
    private static final String DEFAULT_EMAIL_TO = "jug-leaders-montpellier@googlegroups.com";
    private static final String EXPIRING_MAIL_SUBJECT = "Expiring Partners Alert";
    private static Logger logger = LoggerFactory.getLogger(ExpiringPartnerJob.class);
    @View("mail/expiringPartner")
    private Template expiringTemplate;

    @Requires
    private MailSenderService mailer;

    @Requires
    private YearpartnerDao yearpartnerDao;

    @Requires
    private ApplicationConfiguration configuration;

    @Every(period = 7, unit = TimeUnit.DAYS)
    public void execute() throws Exception {
        logger.info("Expiring partner job start");
        // Configuration of job
        final String emailTo = configuration.getWithDefault("jobs.expiringPartners.emailTo", DEFAULT_EMAIL_TO);
        final Timestamp timestampToCheck = new Timestamp(configuration.get("jobs.expiringPartners.timestampToCheck", Long.class, DEFAULT_EXPIRING_TIMESTAMP));

        // Default values used in search of expiring partners
        final Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        final Timestamp expiryTimestamp = new Timestamp(currentTimestamp.getTime() + timestampToCheck.getTime());

        // Searching the expiring partners
        List<ExpiringPartnerJobItem> expiringPartners =
                yearpartnerDao.findAll().stream()
                        .filter(partner ->
                                partner.getStopdate().after(currentTimestamp) && partner.getStopdate().before(expiryTimestamp))
                        .map(partner -> new ExpiringPartnerJobItem()
                                .setName(partner.getName())
                                .setLogo(partner.getLogourl())
                                        // Compute number of days before the expiration of the partnership
                                .setExpiringTime((int) (partner.getStopdate().getTime() - currentTimestamp.getTime()) / 86400000))
                        .collect(Collectors.toList());

        if (!expiringPartners.isEmpty()) {
            logger.info("Detected expiring partners, send email to inform.");
            mailer.send(
                    new Mail()
                            .to(emailTo).subject(EXPIRING_MAIL_SUBJECT)
                            .body(expiringTemplate.render(new DefaultController(), ImmutableMap.of("partners", expiringPartners)).content().toString()));

        }
        logger.info("Expiring partner job done");
    }

    public class ExpiringPartnerJobItem {

        public String name;
        public String logo;
        public int expiringTime;

        public String getName() {
            return name;
        }

        public ExpiringPartnerJobItem setName(String name) {
            this.name = name;
            return this;
        }

        public String getLogo() {
            return logo;
        }

        public ExpiringPartnerJobItem setLogo(String log) {
            this.logo = log;
            return this;
        }

        public int getExpiringTime() {
            return expiringTime;
        }

        public ExpiringPartnerJobItem setExpiringTime(int expiringTime) {
            this.expiringTime = expiringTime;
            return this;
        }
    }
}
