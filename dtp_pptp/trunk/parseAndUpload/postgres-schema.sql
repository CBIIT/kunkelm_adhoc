
    create table AFFI_DATA (
        ID BIGINT not null,
        STAT_PAIRS INTEGER not null,
        STAT_PAIRS_USED INTEGER not null,
        SIGNAL DOUBLE PRECISION not null,
        DETECTION CHARACTER VARYING(1024) not null,
        DETECTION_P_VALUE DOUBLE PRECISION not null,
        AFFI_IDENTIFIER_TYPE_FK BIGINT not null,
        CELL_LINE_TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table AFFI_IDENTIFIER_TYPE (
        ID BIGINT not null,
        PROBE_SET_NAME CHARACTER VARYING(1024) not null,
        ACCESSION CHARACTER VARYING(1024),
        GENECARD CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table CELL_LINE_GROUP (
        ID BIGINT not null,
        DOSE DOUBLE PRECISION not null,
        TEST_NUMBER INTEGER not null,
        CELL_LINE_TYPE_FK BIGINT not null,
        MTD_STUDY_FK BIGINT not null,
        SCHEDULE_TYPE_FK BIGINT not null,
        VEHICLE_TYPE_FK BIGINT not null,
        CONCENTRATION_UNIT_TYPE_FK BIGINT not null,
        MOUSE_TYPE_FK BIGINT not null,
        TREATMENT_ROUTE_TYPE_FK BIGINT not null,
        IMPLANT_SITE_TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table CELL_LINE_MASTER (
        ID BIGINT not null,
        PPTP_IDENTIFIER CHARACTER VARYING(1024) not null,
        STRAIN CHARACTER VARYING(1024) not null,
        IMPLANT_SITE CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table CELL_LINE_TYPE (
        ID BIGINT not null,
        PPTP_IDENTIFIER CHARACTER VARYING(1024) not null unique,
        NAME CHARACTER VARYING(1024) not null,
        SECONDARY BOOLEAN not null,
        SOURCE CHARACTER VARYING(1024) not null,
        DAP_IDENTIFIER CHARACTER VARYING(1024) not null,
        SORT_ORDER INTEGER not null,
        CELL_TYPE_FK BIGINT not null,
        PANEL_TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table CELL_TYPE (
        ID BIGINT not null,
        SORT_ORDER INTEGER not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table COMMON (
        ID BIGINT not null,
        DUMMY_STRING CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table COMPOUND_MASTER (
        ID BIGINT not null,
        PPTP_IDENTIFIER CHARACTER VARYING(1024) not null,
        SOLID_DOSE DOUBLE PRECISION not null,
        CONCENTRATION_UNIT CHARACTER VARYING(1024) not null,
        TREATMENT_ROUTE CHARACTER VARYING(1024) not null,
        SCHEDULE CHARACTER VARYING(1024) not null,
        VEHICLE CHARACTER VARYING(1024) not null,
        ALL_DOSE DOUBLE PRECISION,
        primary key (ID)
    );

    create table COMPOUND_TYPE (
        ID BIGINT not null,
        PPTP_IDENTIFIER CHARACTER VARYING(1024) not null unique,
        NAME CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table CONCENTRATION_UNIT_TYPE (
        ID BIGINT not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table GROUP_ROLE_TYPE (
        ID BIGINT not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table IMPLANT_SITE_TYPE (
        ID BIGINT not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table MASTER (
        ID BIGINT not null,
        COMPOUND_IDENTIFIER CHARACTER VARYING(1024) not null,
        DOSE DOUBLE PRECISION not null,
        CONCENTRATION_UNIT CHARACTER VARYING(1024) not null,
        TREATMENT_ROUTE CHARACTER VARYING(1024) not null,
        SCHEDULE CHARACTER VARYING(1024) not null,
        VEHICLE CHARACTER VARYING(1024) not null,
        IMPLANT_SITE CHARACTER VARYING(1024) not null,
        CELL_LINE_IDENTIFIER CHARACTER VARYING(1024) not null,
        MOUSE_TYPE CHARACTER VARYING(1024) not null,
        TEST_NUMBER INTEGER not null,
        COMPOUND_TYPE_KEY BIGINT not null,
        CONCENTRATION_UNIT_TYPE_KEY BIGINT not null,
        TREATMENT_ROUTE_TYPE_KEY BIGINT not null,
        SCHEDULE_TYPE_KEY BIGINT not null,
        VEHICLE_TYPE_KEY BIGINT not null,
        IMPLANT_SITE_TYPE_KEY BIGINT not null,
        CELL_LINE_TYPE_KEY BIGINT not null,
        MOUSE_TYPE_KEY BIGINT not null,
        primary key (ID)
    );

    create table MOUSE (
        ID BIGINT not null,
        MOUSE_NUMBER INTEGER not null,
        TUMOR_VOLUME DOUBLE PRECISION not null,
        LOG_TUMOR_VOLUME DOUBLE PRECISION,
        MOUSE_GROUP_FK BIGINT not null,
        primary key (ID)
    );

    create table MOUSE_CROSSTAB (
        ID BIGINT not null,
        MOUSE1 DOUBLE PRECISION,
        MOUSE2 DOUBLE PRECISION,
        MOUSE3 DOUBLE PRECISION,
        MOUSE4 DOUBLE PRECISION,
        MOUSE5 DOUBLE PRECISION,
        MOUSE6 DOUBLE PRECISION,
        MOUSE7 DOUBLE PRECISION,
        MOUSE8 DOUBLE PRECISION,
        MOUSE9 DOUBLE PRECISION,
        MOUSE10 DOUBLE PRECISION,
        primary key (ID)
    );

    create table MOUSE_EVENT_TIMES (
        ID BIGINT not null,
        MOUSE_NUMBER INTEGER not null,
        EVENT_TIME DOUBLE PRECISION not null,
        MOUSE_GROUP_FK BIGINT not null,
        primary key (ID)
    );

    create table MOUSE_GROUP (
        ID BIGINT not null,
        DAY INTEGER not null,
        GROUP_ROLE_TYPE_FK BIGINT not null,
        CELL_LINE_GROUP_FK BIGINT not null,
        MOUSE_CROSSTAB_FK BIGINT unique,
        primary key (ID)
    );

    create table MOUSE_TYPE (
        ID BIGINT not null,
        STRAIN CHARACTER VARYING(1024) not null unique,
        GENDER CHARACTER VARYING(1024),
        AGE INTEGER,
        SOURCE CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table MTD_STUDY (
        ID BIGINT not null,
        COMPOUND_TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table PANEL_TYPE (
        ID BIGINT not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        SORT_ORDER INTEGER not null,
        primary key (ID)
    );

    create table RETEST_TYPE (
        ID BIGINT not null,
        COMPOUND_IDENTIFIER CHARACTER VARYING(1024) not null,
        DOSE DOUBLE PRECISION not null,
        CELL_LINE_IDENTIFIER CHARACTER VARYING(1024) not null,
        TEST_NUMBER INTEGER not null,
        primary key (ID)
    );

    create table SCHEDULE_TYPE (
        ID BIGINT not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table SUMMARY (
        ID BIGINT not null,
        THE_N1 INTEGER not null,
        THE_D1 INTEGER not null,
        THE_E1 INTEGER not null,
        THE_N2 INTEGER not null,
        COUNT_MOUSE_EVENTS INTEGER not null,
        MEDIAN_DAYS_TO_EVENT DOUBLE PRECISION,
        THE_P_VALUE_FLAG CHARACTER VARYING(1024),
        THE_P_VALUE DOUBLE PRECISION,
        EFS_T_OVER_C_FLAG CHARACTER VARYING(1024),
        EFS_T_OVER_C DOUBLE PRECISION,
        MEDIAN_R_T_V_FLAG CHARACTER VARYING(1024),
        MEDIAN_R_T_V DOUBLE PRECISION,
        T_OVER_C DOUBLE PRECISION,
        DAY_FOR_T_OVER_C INTEGER,
        THE_T_OVER_C_P_VALUE_FLAG CHARACTER VARYING(1024),
        THE_T_OVER_C_P_VALUE DOUBLE PRECISION,
        COUNT_P_D INTEGER not null,
        COUNT_P_D1 INTEGER not null,
        COUNT_P_D2 INTEGER not null,
        COUNT_S_D INTEGER not null,
        COUNT_P_R INTEGER not null,
        COUNT_C_R INTEGER not null,
        COUNT_M_C_R INTEGER not null,
        RESPONSE_MEDIAN_SCORE DOUBLE PRECISION not null,
        OVERALL_GROUP_MEDIAN_RESPONSE CHARACTER VARYING(1024) not null,
        SURVIVING_PERCENT INTEGER,
        AVERAGE_R_T_V_AT_DAY_FOR_T_OVE DOUBLE PRECISION,
        CELL_LINE_GROUP_FK BIGINT not null,
        GROUP_ROLE_TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table TREATMENT_ROUTE_TYPE (
        ID BIGINT not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table VEHICLE_TYPE (
        ID BIGINT not null,
        DISPLAY_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    alter table AFFI_DATA 
        add constraint AFFI_DATA_CELL_LINE_TYPE_FKC 
        foreign key (CELL_LINE_TYPE_FK) 
        references CELL_LINE_TYPE;

    alter table AFFI_DATA 
        add constraint AFFI_DATA_AFFI_IDENTIFIER_TYPC 
        foreign key (AFFI_IDENTIFIER_TYPE_FK) 
        references AFFI_IDENTIFIER_TYPE;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_CELL_LINE_TYPC 
        foreign key (CELL_LINE_TYPE_FK) 
        references CELL_LINE_TYPE;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_MOUSE_TYPE_FKC 
        foreign key (MOUSE_TYPE_FK) 
        references MOUSE_TYPE;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_VEHICLE_TYPE_C 
        foreign key (VEHICLE_TYPE_FK) 
        references VEHICLE_TYPE;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_MTD_STUDY_FKC 
        foreign key (MTD_STUDY_FK) 
        references MTD_STUDY;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_IMPLANT_SITE_C 
        foreign key (IMPLANT_SITE_TYPE_FK) 
        references IMPLANT_SITE_TYPE;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_TREATMENT_ROUC 
        foreign key (TREATMENT_ROUTE_TYPE_FK) 
        references TREATMENT_ROUTE_TYPE;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_CONCENTRATIONC 
        foreign key (CONCENTRATION_UNIT_TYPE_FK) 
        references CONCENTRATION_UNIT_TYPE;

    alter table CELL_LINE_GROUP 
        add constraint CELL_LINE_GROUP_SCHEDULE_TYPEC 
        foreign key (SCHEDULE_TYPE_FK) 
        references SCHEDULE_TYPE;

    alter table CELL_LINE_TYPE 
        add constraint CELL_LINE_TYPE_PANEL_TYPE_FKC 
        foreign key (PANEL_TYPE_FK) 
        references PANEL_TYPE;

    alter table CELL_LINE_TYPE 
        add constraint CELL_LINE_TYPE_CELL_TYPE_FKC 
        foreign key (CELL_TYPE_FK) 
        references CELL_TYPE;

    alter table MOUSE 
        add constraint MOUSE_MOUSE_GROUP_FKC 
        foreign key (MOUSE_GROUP_FK) 
        references MOUSE_GROUP;

    alter table MOUSE_EVENT_TIMES 
        add constraint MOUSE_EVENT_TIMES_MOUSE_GROUPC 
        foreign key (MOUSE_GROUP_FK) 
        references MOUSE_GROUP;

    alter table MOUSE_GROUP 
        add constraint MOUSE_GROUP_MOUSE_CROSSTAB_FKC 
        foreign key (MOUSE_CROSSTAB_FK) 
        references MOUSE_CROSSTAB;

    alter table MOUSE_GROUP 
        add constraint MOUSE_GROUP_CELL_LINE_GROUP_FC 
        foreign key (CELL_LINE_GROUP_FK) 
        references CELL_LINE_GROUP;

    alter table MOUSE_GROUP 
        add constraint MOUSE_GROUP_GROUP_ROLE_TYPE_FC 
        foreign key (GROUP_ROLE_TYPE_FK) 
        references GROUP_ROLE_TYPE;

    alter table MTD_STUDY 
        add constraint MTD_STUDY_COMPOUND_TYPE_FKC 
        foreign key (COMPOUND_TYPE_FK) 
        references COMPOUND_TYPE;

    alter table SUMMARY 
        add constraint SUMMARY_CELL_LINE_GROUP_FKC 
        foreign key (CELL_LINE_GROUP_FK) 
        references CELL_LINE_GROUP;

    alter table SUMMARY 
        add constraint SUMMARY_GROUP_ROLE_TYPE_FKC 
        foreign key (GROUP_ROLE_TYPE_FK) 
        references GROUP_ROLE_TYPE;

    create sequence hibernate_sequence;
