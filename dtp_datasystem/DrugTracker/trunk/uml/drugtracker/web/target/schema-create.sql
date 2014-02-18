
    create table AD_HOC_DRUG_TRACKER (
        ID BIGINT not null,
        STRUCTURE_SOURCE CHARACTER VARYING(1024),
        SMILES CHARACTER VARYING(1024),
        REMOVED_SALTS CHARACTER VARYING(1024),
        MW DOUBLE PRECISION,
        MF CHARACTER VARYING(1024),
        ALOGP DOUBLE PRECISION,
        LOGD DOUBLE PRECISION,
        HBA INTEGER,
        HBD INTEGER,
        SA DOUBLE PRECISION,
        PSA DOUBLE PRECISION,
        primary key (ID)
    );

    create table DRUG_TRACKER (
        ID BIGINT not null,
        AGENT CHARACTER VARYING(1024),
        ORIGINATOR CHARACTER VARYING(1024),
        CAS CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table DRUG_TRACKER_ALIAS (
        ID BIGINT not null,
        ALIAS CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table DRUG_TRACKER_ALIASES2DRUG_TRAC (
        DRUG_TRACKER_ALIASES_FK BIGINT not null,
        DRUG_TRACKERS_FK BIGINT not null,
        primary key (DRUG_TRACKER_ALIASES_FK, DRUG_TRACKERS_FK)
    );

    create table DRUG_TRACKER_PLATE (
        ID BIGINT not null,
        PLATE_NAME CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table DRUG_TRACKER_PLATES2DRUG_TRACK (
        DRUG_TRACKER_PLATES_FK BIGINT not null,
        DRUG_TRACKERS_FK BIGINT not null,
        primary key (DRUG_TRACKER_PLATES_FK, DRUG_TRACKERS_FK)
    );

    create table DRUG_TRACKER_SET (
        ID BIGINT not null,
        SET_NAME CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table DRUG_TRACKER_SETS2DRUG_TRACKER (
        DRUG_TRACKER_SETS_FK BIGINT not null,
        DRUG_TRACKERS_FK BIGINT not null,
        primary key (DRUG_TRACKER_SETS_FK, DRUG_TRACKERS_FK)
    );

    create table DRUG_TRACKER_TARGET (
        ID BIGINT not null,
        TARGET CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table DRUG_TRACKER_TARGETS2DRUG_TRAC (
        DRUG_TRACKER_TARGETS_FK BIGINT not null,
        DRUG_TRACKERS_FK BIGINT not null,
        primary key (DRUG_TRACKER_TARGETS_FK, DRUG_TRACKERS_FK)
    );

    create table NSC_DRUG_TRACKER (
        ID BIGINT not null,
        NSC INTEGER,
        PREFIX CHARACTER VARYING(1024),
        STANDARDIZED_SMILES_FK BIGINT,
        primary key (ID)
    );

    create table RDKIT_MOL (
        ID BIGINT not null,
        NSC INTEGER,
        MOL CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table STANDARDIZED_SMILES (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024),
        NSC INTEGER,
        STRUCTURE_SOURCE CHARACTER VARYING(1024),
        SMILES_FROM_PROD CHARACTER VARYING(1024),
        PROD_CAN_SMI CHARACTER VARYING(1024),
        REMOVED_SALTS CHARACTER VARYING(1024),
        PARENT_CAN_SMI CHARACTER VARYING(1024),
        TAUT_CAN_SMI CHARACTER VARYING(1024),
        TAUT_NOSTEREO_CAN_SMI CHARACTER VARYING(1024),
        XENO INTEGER,
        HF INTEGER,
        NCI60 INTEGER,
        DISTRIBUTION CHARACTER VARYING(1024),
        CONF CHARACTER VARYING(1024),
        INVENTORY DOUBLE PRECISION,
        MW DOUBLE PRECISION,
        MF CHARACTER VARYING(1024),
        ALOGP DOUBLE PRECISION,
        LOGD DOUBLE PRECISION,
        HBA INTEGER,
        HBD INTEGER,
        SA DOUBLE PRECISION,
        PSA DOUBLE PRECISION,
        CAS CHARACTER VARYING(1024),
        PREFIX CHARACTER VARYING(1024) not null,
        COUNT_RELATED INTEGER,
        RELATED CHARACTER VARYING(1024),
        RDKIT_MOL_FK BIGINT unique,
        primary key (ID)
    );

    alter table AD_HOC_DRUG_TRACKER 
        add constraint AD_HOC_DRUG_TRACKERIFKC 
        foreign key (ID) 
        references DRUG_TRACKER;

    alter table DRUG_TRACKER_ALIASES2DRUG_TRAC 
        add constraint DRUG_TRACKER_DRUG_TRACKER_ALIC 
        foreign key (DRUG_TRACKER_ALIASES_FK) 
        references DRUG_TRACKER_ALIAS;

    alter table DRUG_TRACKER_ALIASES2DRUG_TRAC 
        add constraint DRUG_TRACKER_ALIAS_DRUG_TRACKC 
        foreign key (DRUG_TRACKERS_FK) 
        references DRUG_TRACKER;

    alter table DRUG_TRACKER_PLATES2DRUG_TRACK 
        add constraint DRUG_TRACKER_PLATE_DRUG_TRACKC 
        foreign key (DRUG_TRACKERS_FK) 
        references DRUG_TRACKER;

    alter table DRUG_TRACKER_PLATES2DRUG_TRACK 
        add constraint DRUG_TRACKER_DRUG_TRACKER_PLAC 
        foreign key (DRUG_TRACKER_PLATES_FK) 
        references DRUG_TRACKER_PLATE;

    alter table DRUG_TRACKER_SETS2DRUG_TRACKER 
        add constraint DRUG_TRACKER_SET_DRUG_TRACKERC 
        foreign key (DRUG_TRACKERS_FK) 
        references DRUG_TRACKER;

    alter table DRUG_TRACKER_SETS2DRUG_TRACKER 
        add constraint DRUG_TRACKER_DRUG_TRACKER_SETC 
        foreign key (DRUG_TRACKER_SETS_FK) 
        references DRUG_TRACKER_SET;

    alter table DRUG_TRACKER_TARGETS2DRUG_TRAC 
        add constraint DRUG_TRACKER_TARGET_DRUG_TRACC 
        foreign key (DRUG_TRACKERS_FK) 
        references DRUG_TRACKER;

    alter table DRUG_TRACKER_TARGETS2DRUG_TRAC 
        add constraint DRUG_TRACKER_DRUG_TRACKER_TARC 
        foreign key (DRUG_TRACKER_TARGETS_FK) 
        references DRUG_TRACKER_TARGET;

    create index nsc_drug_tracker_nsc on NSC_DRUG_TRACKER (NSC);

    alter table NSC_DRUG_TRACKER 
        add constraint NSC_DRUG_TRACKERIFKC 
        foreign key (ID) 
        references DRUG_TRACKER;

    alter table NSC_DRUG_TRACKER 
        add constraint NSC_DRUG_TRACKER_STANDARDIZEDC 
        foreign key (STANDARDIZED_SMILES_FK) 
        references STANDARDIZED_SMILES;

    create index rdkit_mol_nsc_idx on RDKIT_MOL (NSC);

    create index std_smi_nsc_idx on STANDARDIZED_SMILES (NSC);

    create index std_smi_cas_idx on STANDARDIZED_SMILES (CAS);

    alter table STANDARDIZED_SMILES 
        add constraint STANDARDIZED_SMILES_RDKIT_MOLC 
        foreign key (RDKIT_MOL_FK) 
        references RDKIT_MOL;

    create sequence hibernate_sequence;
