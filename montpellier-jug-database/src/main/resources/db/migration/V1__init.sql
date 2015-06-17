
    create table Event (
        id bigserial,
        capacity integer not null,
        date timestamp,
        description text,
        location text,
        map text,
        open boolean not null,
        registrationURL text,
        report text,
        title text,
        partner_id bigint,
        primary key (id)
    );

    create table EventPartner (
            id bigserial,
            description text,
            logoURL text,
            name text,
            url text,
            primary key (id)
        );

    create table News (
                id bigserial,
                comments boolean not null,
                content text,
                date timestamp,
                title text,
                primary key (id)
            );

    create table Participation (
        id bigserial,
        code text,
        status integer,
        event_id bigint,
        juguser_id bigint,
        primary key (id)
    );

    create table Speaker (
        id bigserial,
        activity text,
        compan text,
        description text,
        fullName text,
        jugmember boolean,
        memberFct text,
        photoUrl text,
        url text,
        email text,
        personalUrl text,
        primary key (id)
    );

    create table Talk (
        id bigserial,
        orderInEvent integer not null,
        teaser text,
        datetime text,
        title text,
        event_id bigint,
        speaker_id bigint,
        links text[],
        primary key (id)
    );

    create table JUGUser (
        id bigserial,
        email text,
        primary key (id)
    );

     create table Tag (
            id bigserial,
            name text,
            primary key (id)
        );

        create table Talk_Tag (
            Talk_id bigint not null,
            tags_id bigint not null,
            primary key (Talk_id, tags_id)
        );

        create table YearPartner (
            id bigserial,
            description text,
            logoURL text,
            name text,
            startDate timestamp,
            stopDate timestamp,
            url text,
            primary key (id)
        );

        create table Poll (
            id bigserial,
            question text not null,
            expiryDate timestamp,
            visible boolean,
            primary key (id)
        );

        create table Answer (
            id bigserial,
            answer text not null,
            votes bigint,
            poll_id bigint not null,
            primary key (id)
        );


    alter table Participation
        add constraint participation_event_id_fk
        foreign key (event_id)
        references Event (id);

    alter table Participation
        add constraint participation_juguser_id_fk
        foreign key (juguser_id)
        references JUGUser (id);

    alter table Talk
        add constraint talk_event_id_fk
        foreign key (event_id)
        references Event (id);

    alter table Talk
        add constraint talk_speaker_id_fk
        foreign key (speaker_id)
        references Speaker (id);

 alter table Event
        add constraint event_partner_id_fk
        foreign key (partner_id)
        references EventPartner (id);

    alter table Talk_Tag
        add constraint talk_tag_tags_id_fk
        foreign key (tags_id)
        references Tag (id);

    alter table Talk_Tag
        add constraint talk_tag_talk_id_fk
        foreign key (Talk_id)
        references Talk (id);

    alter table Answer
        add constraint Poll_Answer
        foreign key (poll_id)
        references Poll (id);

