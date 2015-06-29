package org.jug.montpellier.jobs;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;
import org.jug.montpellier.models.Yearpartner;
import org.montpellierjug.store.jooq.tables.daos.YearpartnerDao;
import org.montpellierjug.store.jooq.tables.interfaces.IYearpartner;
import org.ow2.chameleon.mail.Mail;
import org.ow2.chameleon.mail.MailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.Controller;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.View;
import org.wisdom.api.annotations.scheduler.Every;
import org.wisdom.api.configuration.ApplicationConfiguration;
import org.wisdom.api.scheduler.Scheduled;
import org.wisdom.api.templates.Template;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by fteychene on 17/06/2015.
 */
@Component
@Provides
@Instantiate
public class ExpiringPartnerJob implements Scheduled {

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
        Timestamp timestampToCheck = new Timestamp(configuration.get("batch.expiringPartners.timestampToCheck", Long.class, 2592000000l));
        Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
        Timestamp expiryTimestamp = new Timestamp(currentTimestamp.getTime() + timestampToCheck.getTime());
        List<ExpiringPartnerJobItem> expiringPartners =
                yearpartnerDao.findAll().stream().filter(partner ->
                            partner.getStopdate().after(currentTimestamp) &&
                                partner.getStopdate().before(expiryTimestamp))
                        .map(partner -> new ExpiringPartnerJobItem()
                                .setName(partner.getName())
                                .setLogo(partner.getLogourl())
                                .setExpiringTime((int)(partner.getStopdate().getTime() - currentTimestamp.getTime()) / 86400000))
                        .collect(Collectors.toList());



        if (!expiringPartners.isEmpty()) {
            mailer.send(
                    new Mail()
                            .to("francois.teychene@gmail.com").subject("Expiring Partners")
                    .body(expiringTemplate.render(new DefaultController(), ImmutableMap.of("partners", expiringPartners)).content().toString()));
            System.out.println(expiringTemplate.render(new DefaultController(), ImmutableMap.of("partners", Lists.partition(expiringPartners, 3))).content().toString());
        }
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
