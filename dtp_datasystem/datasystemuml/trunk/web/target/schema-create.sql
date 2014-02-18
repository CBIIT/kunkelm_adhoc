
    create table AD_HOC_CMPD (
        ID BIGINT not null,
        CMPD_OWNER CHARACTER VARYING(1024) not null,
        AD_HOC_CMPD_ID BIGINT unique,
        NAME CHARACTER VARYING(1024),
        ORIGINAL_AD_HOC_CMPD_ID BIGINT,
        AD_HOC_CMPD_PARENT_FRAGMENT_FK BIGINT unique,
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT (
        ID BIGINT not null,
        AD_HOC_CMPD_FRAGMENT_P_CHEM_FK BIGINT unique,
        AD_HOC_CMPD_FRAGMENT_STRUCT_FK BIGINT unique,
        AD_HOC_CMPD_FK BIGINT,
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT_P_CHEM (
        ID BIGINT not null,
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

    create table AD_HOC_CMPD_FRAGMENT_STRUCTURE (
        ID BIGINT not null,
        SMILES CHARACTER VARYING(1024),
        INCHI CHARACTER VARYING(1024),
        MOL CHARACTER VARYING(1024),
        INCHI_AUX CHARACTER VARYING(1024),
        CTAB TEXT,
        primary key (ID)
    );

    create table CMPD (
        ID BIGINT not null,
        primary key (ID)
    );

    create table CMPD_ALIAS (
        ID BIGINT not null,
        ALIAS CHARACTER VARYING(1024) not null,
        CMPD_ALIAS_TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table CMPD_ALIASES2NSC_CMPDS (
        NSC_CMPDS_FK BIGINT not null,
        CMPD_ALIASES_FK BIGINT not null,
        primary key (NSC_CMPDS_FK, CMPD_ALIASES_FK)
    );

    create table CMPD_ALIAS_TYPE (
        ID BIGINT not null,
        ALIAS_TYPE CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_BIO_ASSAY (
        ID BIGINT not null,
        NCI60 INTEGER,
        HF INTEGER,
        XENO INTEGER,
        primary key (ID)
    );

    create table CMPD_FRAGMENT (
        ID BIGINT not null,
        CMPD_FRAGMENT_STRUCTURE_FK BIGINT unique,
        CMPD_KNOWN_SALT_FK BIGINT,
        CMPD_FRAGMENT_P_CHEM_FK BIGINT unique,
        NSC_CMPD_FK BIGINT,
        primary key (ID)
    );

    create table CMPD_FRAGMENT_P_CHEM (
        ID BIGINT not null,
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

    create table CMPD_FRAGMENT_STRUCTURE (
        ID BIGINT not null,
        SMILES CHARACTER VARYING(1024),
        INCHI CHARACTER VARYING(1024),
        MOL CHARACTER VARYING(1024),
        INCHI_AUX CHARACTER VARYING(1024),
        CTAB TEXT,
        primary key (ID)
    );

    create table CMPD_INVENTORY (
        ID BIGINT not null,
        INVENTORY DOUBLE PRECISION,
        primary key (ID)
    );

    create table CMPD_KNOWN_SALT (
        ID BIGINT not null,
        MOL CHARACTER VARYING(1024) not null,
        SMILES CHARACTER VARYING(1024) not null,
        SALT_NAME CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table CMPD_LIST (
        ID BIGINT not null,
        LIST_NAME CHARACTER VARYING(1024) not null,
        DATE_CREATED TIMESTAMP WITHOUT TIME ZONE not null,
        LIST_OWNER CHARACTER VARYING(1024) not null,
        SHARE_WITH CHARACTER VARYING(1024) not null,
        CMPD_LIST_ID BIGINT unique,
        COUNT_LIST_MEMBERS INTEGER,
        primary key (ID)
    );

    create table CMPD_LIST_MEMBER (
        ID BIGINT not null,
        CMPD_FK BIGINT not null,
        CMPD_LIST_FK BIGINT not null,
        primary key (ID)
    );

    create table CMPD_PLATE (
        ID BIGINT not null,
        PLATE_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_PLATES2NSC_CMPDS (
        NSC_CMPDS_FK BIGINT not null,
        CMPD_PLATES_FK BIGINT not null,
        primary key (NSC_CMPDS_FK, CMPD_PLATES_FK)
    );

    create table CMPD_PROJECT (
        ID BIGINT not null,
        PROJECT_CODE CHARACTER VARYING(1024) not null unique,
        PROJECT_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_PROJECTS2NSC_CMPDS (
        NSC_CMPDS_FK BIGINT not null,
        CMPD_PROJECTS_FK BIGINT not null,
        primary key (NSC_CMPDS_FK, CMPD_PROJECTS_FK)
    );

    create table CMPD_PUB_CHEM_SID (
        ID BIGINT not null,
        SID BIGINT,
        primary key (ID)
    );

    create table CMPD_PUB_CHEM_SIDS2NSC_CMPDS (
        NSC_CMPDS_FK BIGINT not null,
        CMPD_PUB_CHEM_SIDS_FK BIGINT not null,
        primary key (NSC_CMPDS_FK, CMPD_PUB_CHEM_SIDS_FK)
    );

    create table CMPD_RELATED (
        ID BIGINT not null,
        NSC_CMPD_FK BIGINT not null unique,
        CMPD_RELATION_TYPE_FK BIGINT not null unique,
        primary key (ID)
    );

    create table CMPD_RELATION_TYPE (
        ID BIGINT not null,
        RELATION_TYPE CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_SET (
        ID BIGINT not null,
        SET_NAME CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_SETS2NSC_CMPDS (
        NSC_CMPDS_FK BIGINT not null,
        CMPD_SETS_FK BIGINT not null,
        primary key (NSC_CMPDS_FK, CMPD_SETS_FK)
    );

    create table CMPD_TARGET (
        ID BIGINT not null,
        TARGET CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_TARGETS2NSC_CMPDS (
        NSC_CMPDS_FK BIGINT not null,
        CMPD_TARGETS_FK BIGINT not null,
        primary key (NSC_CMPDS_FK, CMPD_TARGETS_FK)
    );

    create table CMPD_VIEW (
        ID BIGINT not null,
        MW DOUBLE PRECISION,
        MF CHARACTER VARYING(1024),
        ALOGP DOUBLE PRECISION,
        LOGD DOUBLE PRECISION,
        HBA INTEGER,
        HBD INTEGER,
        SA DOUBLE PRECISION,
        PSA DOUBLE PRECISION,
        SMILES CHARACTER VARYING(1024),
        INCHI CHARACTER VARYING(1024),
        MOL CHARACTER VARYING(1024),
        INCHI_AUX CHARACTER VARYING(1024),
        NAME CHARACTER VARYING(1024),
        NSC_CMPD_ID BIGINT unique,
        PREFIX CHARACTER VARYING(1024),
        NSC INTEGER unique,
        CONF CHARACTER VARYING(1024),
        DISTRIBUTION CHARACTER VARYING(1024),
        CAS CHARACTER VARYING(1024),
        NCI60 INTEGER,
        HF INTEGER,
        XENO INTEGER,
        CMPD_OWNER CHARACTER VARYING(1024),
        AD_HOC_CMPD_ID BIGINT unique,
        FORMATTED_TARGETS_STRING CHARACTER VARYING(1024),
        FORMATTED_SETS_STRING CHARACTER VARYING(1024),
        FORMATTED_PROJECTS_STRING CHARACTER VARYING(1024),
        FORMATTED_PLATES_STRING CHARACTER VARYING(1024),
        FORMATTED_ALIASES_STRING CHARACTER VARYING(1024),
        INVENTORY DOUBLE PRECISION,
        primary key (ID)
    );

    create table NSC_CMPD (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024),
        NSC_CMPD_ID BIGINT unique,
        PREFIX CHARACTER VARYING(1024) not null,
        NSC INTEGER unique,
        CONF CHARACTER VARYING(1024) not null,
        DISTRIBUTION CHARACTER VARYING(1024) not null,
        CAS CHARACTER VARYING(1024),
        PSEUDO_ATOMS CHARACTER VARYING(1024),
        SALT_NAME CHARACTER VARYING(1024),
        SALT_SMILES CHARACTER VARYING(1024),
        SALT_ID BIGINT,
        CMPD_PARENT_FRAGMENT_FK BIGINT unique,
        CMPD_BIO_ASSAY_FK BIGINT unique,
        CMPD_INVENTORY_FK BIGINT unique,
        primary key (ID)
    );

    create table RDKIT_MOL (
        ID BIGINT not null,
        NSC INTEGER,
        MOL CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    alter table AD_HOC_CMPD 
        add constraint AD_HOC_CMPDIFKC 
        foreign key (ID) 
        references CMPD;

    alter table AD_HOC_CMPD 
        add constraint AD_HOC_CMPD_AD_HOC_CMPD_PARENC 
        foreign key (AD_HOC_CMPD_PARENT_FRAGMENT_FK) 
        references AD_HOC_CMPD_FRAGMENT;

    alter table AD_HOC_CMPD_FRAGMENT 
        add constraint AD_HOC_CMPD_FRAGMENT_AD_HOC_CH 
        foreign key (AD_HOC_CMPD_FRAGMENT_STRUCT_FK) 
        references AD_HOC_CMPD_FRAGMENT_STRUCTURE;

    alter table AD_HOC_CMPD_FRAGMENT 
        add constraint AD_HOC_CMPD_FRAGMENT_AD_HOC_CC 
        foreign key (AD_HOC_CMPD_FRAGMENT_P_CHEM_FK) 
        references AD_HOC_CMPD_FRAGMENT_P_CHEM;

    alter table AD_HOC_CMPD_FRAGMENT 
        add constraint AD_HOC_CMPD_FRAGMENT_AD_HOC_CD 
        foreign key (AD_HOC_CMPD_FK) 
        references AD_HOC_CMPD;

    create index cmpd_alias_alias_idx on CMPD_ALIAS (ALIAS);

    alter table CMPD_ALIAS 
        add constraint CMPD_ALIAS_CMPD_ALIAS_TYPE_FKC 
        foreign key (CMPD_ALIAS_TYPE_FK) 
        references CMPD_ALIAS_TYPE;

    alter table CMPD_ALIASES2NSC_CMPDS 
        add constraint CMPD_ALIAS_NSC_CMPDS_FKC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_ALIASES2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_ALIASES_FKC 
        foreign key (CMPD_ALIASES_FK) 
        references CMPD_ALIAS;

    alter table CMPD_FRAGMENT 
        add constraint CMPD_FRAGMENT_NSC_CMPD_FKC 
        foreign key (NSC_CMPD_FK) 
        references NSC_CMPD;

    alter table CMPD_FRAGMENT 
        add constraint CMPD_FRAGMENT_CMPD_KNOWN_SALTC 
        foreign key (CMPD_KNOWN_SALT_FK) 
        references CMPD_KNOWN_SALT;

    alter table CMPD_FRAGMENT 
        add constraint CMPD_FRAGMENT_CMPD_FRAGMENT_SC 
        foreign key (CMPD_FRAGMENT_STRUCTURE_FK) 
        references CMPD_FRAGMENT_STRUCTURE;

    alter table CMPD_FRAGMENT 
        add constraint CMPD_FRAGMENT_CMPD_FRAGMENT_PC 
        foreign key (CMPD_FRAGMENT_P_CHEM_FK) 
        references CMPD_FRAGMENT_P_CHEM;

    alter table CMPD_LIST_MEMBER 
        add constraint CMPD_LIST_MEMBER_CMPD_LIST_FKC 
        foreign key (CMPD_LIST_FK) 
        references CMPD_LIST;

    alter table CMPD_LIST_MEMBER 
        add constraint CMPD_LIST_MEMBER_CMPD_FKC 
        foreign key (CMPD_FK) 
        references CMPD;

    alter table CMPD_PLATES2NSC_CMPDS 
        add constraint CMPD_PLATE_NSC_CMPDS_FKC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_PLATES2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_PLATES_FKC 
        foreign key (CMPD_PLATES_FK) 
        references CMPD_PLATE;

    alter table CMPD_PROJECTS2NSC_CMPDS 
        add constraint CMPD_PROJECT_NSC_CMPDS_FKC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_PROJECTS2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_PROJECTS_FKC 
        foreign key (CMPD_PROJECTS_FK) 
        references CMPD_PROJECT;

    alter table CMPD_PUB_CHEM_SIDS2NSC_CMPDS 
        add constraint CMPD_PUB_CHEM_SID_NSC_CMPDS_FC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_PUB_CHEM_SIDS2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_PUB_CHEM_SIDS_FC 
        foreign key (CMPD_PUB_CHEM_SIDS_FK) 
        references CMPD_PUB_CHEM_SID;

    alter table CMPD_RELATED 
        add constraint CMPD_RELATED_NSC_CMPD_FKM 
        foreign key (NSC_CMPD_FK) 
        references NSC_CMPD;

    alter table CMPD_RELATED 
        add constraint CMPD_RELATED_CMPD_RELATION_TYC 
        foreign key (CMPD_RELATION_TYPE_FK) 
        references CMPD_RELATION_TYPE;

    alter table CMPD_SETS2NSC_CMPDS 
        add constraint CMPD_SET_NSC_CMPDS_FKC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_SETS2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_SETS_FKC 
        foreign key (CMPD_SETS_FK) 
        references CMPD_SET;

    alter table CMPD_TARGETS2NSC_CMPDS 
        add constraint CMPD_TARGET_NSC_CMPDS_FKC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_TARGETS2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_TARGETS_FKC 
        foreign key (CMPD_TARGETS_FK) 
        references CMPD_TARGET;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_INVENTORY_FKC 
        foreign key (CMPD_INVENTORY_FK) 
        references CMPD_INVENTORY;

    alter table NSC_CMPD 
        add constraint NSC_CMPDIFKC 
        foreign key (ID) 
        references CMPD;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_PARENT_FRAGMENTC 
        foreign key (CMPD_PARENT_FRAGMENT_FK) 
        references CMPD_FRAGMENT;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_BIO_ASSAY_FKC 
        foreign key (CMPD_BIO_ASSAY_FK) 
        references CMPD_BIO_ASSAY;

    create index rdkit_mol_nsc_idx on RDKIT_MOL (NSC);

    create sequence AD_HOC_CMPD_FRAGMENT_P_CHE_SEQ;

    create sequence AD_HOC_CMPD_FRAGMENT_SEQ;

    create sequence AD_HOC_CMPD_FRAGMENT_STRUC_SEQ;

    create sequence CMPD_ALIAS_SEQ;

    create sequence CMPD_ALIAS_TYPE_SEQ;

    create sequence CMPD_BIO_ASSAY_SEQ;

    create sequence CMPD_FRAGMENT_P_CHEM_SEQ;

    create sequence CMPD_FRAGMENT_SEQ;

    create sequence CMPD_FRAGMENT_STRUCTURE_SEQ;

    create sequence CMPD_INVENTORY_SEQ;

    create sequence CMPD_KNOWN_SALT_SEQ;

    create sequence CMPD_LIST_MEMBER_SEQ;

    create sequence CMPD_LIST_SEQ;

    create sequence CMPD_PLATE_SEQ;

    create sequence CMPD_PROJECT_SEQ;

    create sequence CMPD_PUB_CHEM_SID_SEQ;

    create sequence CMPD_RELATED_SEQ;

    create sequence CMPD_RELATION_TYPE_SEQ;

    create sequence CMPD_SEQ;

    create sequence CMPD_SET_SEQ;

    create sequence CMPD_TARGET_SEQ;

    create sequence hibernate_sequence;
