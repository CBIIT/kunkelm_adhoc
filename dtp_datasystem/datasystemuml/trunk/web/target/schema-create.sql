
    create table AD_HOC_CMPD (
        ID BIGINT not null,
        AD_HOC_CMPD_ID BIGINT unique,
        NAME CHARACTER VARYING(1024),
        ORIGINAL_AD_HOC_CMPD_ID BIGINT,
        AD_HOC_CMPD_PARENT_FRAGMENT_FK BIGINT unique,
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT (
        ID BIGINT not null,
        STOICHIOMETRY DOUBLE PRECISION,
        AD_HOC_CMPD_FRAGMENT_P_CHEM_FK BIGINT unique,
        AD_HOC_CMPD_FRAGMENT_STRUCT_FK BIGINT unique,
        AD_HOC_CMPD_FK BIGINT,
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT_P_CHEM (
        ID BIGINT not null,
        MOLECULAR_WEIGHT DOUBLE PRECISION,
        MOLECULAR_FORMULA CHARACTER VARYING(1024),
        LOG_D DOUBLE PRECISION,
        COUNT_HYD_BOND_ACCEPTORS INTEGER,
        COUNT_HYD_BOND_DONORS INTEGER,
        SURFACE_AREA DOUBLE PRECISION,
        SOLUBILITY DOUBLE PRECISION,
        COUNT_RINGS INTEGER,
        COUNT_ATOMS INTEGER,
        COUNT_BONDS INTEGER,
        COUNT_SINGLE_BONDS INTEGER,
        COUNT_DOUBLE_BONDS INTEGER,
        COUNT_TRIPLE_BONDS INTEGER,
        COUNT_ROTATABLE_BONDS INTEGER,
        COUNT_HYDROGEN_ATOMS INTEGER,
        COUNT_METAL_ATOMS INTEGER,
        COUNT_HEAVY_ATOMS INTEGER,
        COUNT_POSITIVE_ATOMS INTEGER,
        COUNT_NEGATIVE_ATOMS INTEGER,
        COUNT_RING_BONDS INTEGER,
        COUNT_STEREO_ATOMS INTEGER,
        COUNT_STEREO_BONDS INTEGER,
        COUNT_RING_ASSEMBLIES INTEGER,
        COUNT_AROMATIC_BONDS INTEGER,
        COUNT_AROMATIC_RINGS INTEGER,
        FORMAL_CHARGE INTEGER,
        THE_A_LOG_P DOUBLE PRECISION,
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT_STRUCTURE (
        ID BIGINT not null,
        INCHI TEXT,
        INCHI_AUX TEXT,
        CTAB TEXT,
        CAN_SMI TEXT,
        CAN_TAUT TEXT,
        CAN_TAUT_STRIP_STEREO TEXT,
        primary key (ID)
    );

    create table CMPD (
        ID BIGINT not null,
        COMMENT CHARACTER VARYING(1024),
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

    create table CMPD_ANNOTATION (
        ID BIGINT not null,
        GENERAL_COMMENT CHARACTER VARYING(1024),
        PURITY_COMMENT CHARACTER VARYING(1024),
        STEREOCHEMISTRY_COMMENT CHARACTER VARYING(1024),
        MTXT TEXT,
        PSEUDO_ATOMS TEXT,
        primary key (ID)
    );

    create table CMPD_BIO_ASSAY (
        ID BIGINT not null,
        NCI60 INTEGER,
        HF INTEGER,
        XENO INTEGER,
        SARCOMA INTEGER,
        primary key (ID)
    );

    create table CMPD_FRAGMENT (
        ID BIGINT not null,
        STOICHIOMETRY DOUBLE PRECISION,
        COMMENT CHARACTER VARYING(1024),
        CMPD_FRAGMENT_TYPE_FK BIGINT not null,
        CMPD_KNOWN_SALT_FK BIGINT,
        CMPD_FRAGMENT_P_CHEM_FK BIGINT unique,
        CMPD_FRAGMENT_STRUCTURE_FK BIGINT unique,
        NSC_CMPD_FK BIGINT,
        primary key (ID)
    );

    create table CMPD_FRAGMENT_P_CHEM (
        ID BIGINT not null,
        MOLECULAR_WEIGHT DOUBLE PRECISION,
        MOLECULAR_FORMULA CHARACTER VARYING(1024),
        LOG_D DOUBLE PRECISION,
        COUNT_HYD_BOND_ACCEPTORS INTEGER,
        COUNT_HYD_BOND_DONORS INTEGER,
        SURFACE_AREA DOUBLE PRECISION,
        SOLUBILITY DOUBLE PRECISION,
        COUNT_RINGS INTEGER,
        COUNT_ATOMS INTEGER,
        COUNT_BONDS INTEGER,
        COUNT_SINGLE_BONDS INTEGER,
        COUNT_DOUBLE_BONDS INTEGER,
        COUNT_TRIPLE_BONDS INTEGER,
        COUNT_ROTATABLE_BONDS INTEGER,
        COUNT_HYDROGEN_ATOMS INTEGER,
        COUNT_METAL_ATOMS INTEGER,
        COUNT_HEAVY_ATOMS INTEGER,
        COUNT_POSITIVE_ATOMS INTEGER,
        COUNT_NEGATIVE_ATOMS INTEGER,
        COUNT_RING_BONDS INTEGER,
        COUNT_STEREO_ATOMS INTEGER,
        COUNT_STEREO_BONDS INTEGER,
        COUNT_RING_ASSEMBLIES INTEGER,
        COUNT_AROMATIC_BONDS INTEGER,
        COUNT_AROMATIC_RINGS INTEGER,
        FORMAL_CHARGE INTEGER,
        THE_A_LOG_P DOUBLE PRECISION,
        primary key (ID)
    );

    create table CMPD_FRAGMENT_STRUCTURE (
        ID BIGINT not null,
        INCHI TEXT,
        INCHI_AUX TEXT,
        CTAB TEXT,
        CAN_SMI TEXT,
        CAN_TAUT TEXT,
        CAN_TAUT_STRIP_STEREO TEXT,
        primary key (ID)
    );

    create table CMPD_FRAGMENT_TYPE (
        ID BIGINT not null,
        FRAGMENT_TYPE CHARACTER VARYING(1024) not null unique,
        FRAGMENT_TYPE_DESCRIPTION CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table CMPD_INVENTORY (
        ID BIGINT not null,
        INVENTORY DOUBLE PRECISION,
        primary key (ID)
    );

    create table CMPD_KNOWN_SALT (
        ID BIGINT not null,
        SALT_NAME CHARACTER VARYING(1024),
        SALT_MF CHARACTER VARYING(1024),
        SALT_MW DOUBLE PRECISION,
        CAN_SMI TEXT,
        CAN_TAUT TEXT,
        CAN_TAUT_STRIP_STEREO TEXT,
        COUNT_OCCURENCES INTEGER,
        SALT_COMMENT CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table CMPD_LEGACY_CMPD (
        ID BIGINT not null,
        CTAB TEXT,
        MOLECULAR_WEIGHT DOUBLE PRECISION,
        MOLECULAR_FORMULA CHARACTER VARYING(1024),
        JPG512 BYTEA,
        NSC INTEGER unique,
        INVENTORY DOUBLE PRECISION,
        COUNT_NCI60 INTEGER,
        MTXT CHARACTER VARYING(1024),
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
        ANCHOR_SMILES CHARACTER VARYING(1024),
        LIST_COMMENT CHARACTER VARYING(1024),
        ANCHOR_COMMENT CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table CMPD_LIST_MEMBER (
        ID BIGINT not null,
        LIST_MEMBER_COMMENT CHARACTER VARYING(1024),
        CMPD_LIST_FK BIGINT not null,
        CMPD_FK BIGINT not null,
        primary key (ID)
    );

    create table CMPD_NAMED_SET (
        ID BIGINT not null,
        SET_NAME CHARACTER VARYING(1024) not null unique,
        SET_DESCRIPTION CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_NAMED_SETS2NSC_CMPDS (
        NSC_CMPDS_FK BIGINT not null,
        CMPD_NAMED_SETS_FK BIGINT not null,
        primary key (NSC_CMPDS_FK, CMPD_NAMED_SETS_FK)
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
        NSC_CMPD_FK BIGINT not null,
        CMPD_RELATION_TYPE_FK BIGINT not null unique,
        primary key (ID)
    );

    create table CMPD_RELATION_TYPE (
        ID BIGINT not null,
        RELATION_TYPE CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create table CMPD_TABLE (
        ID BIGINT not null,
        PREFIX CHARACTER VARYING(1024),
        NSC INTEGER unique,
        CAS CHARACTER VARYING(1024),
        NAME CHARACTER VARYING(1024),
        DISCREET CHARACTER VARYING(1024),
        CONF CHARACTER VARYING(1024),
        DISTRIBUTION CHARACTER VARYING(1024),
        NSC_CMPD_ID BIGINT unique,
        AD_HOC_CMPD_ID BIGINT unique,
        ORIGINAL_AD_HOC_CMPD_ID BIGINT,
        INVENTORY DOUBLE PRECISION,
        NCI60 INTEGER,
        HF INTEGER,
        XENO INTEGER,
        FORMATTED_TARGETS_STRING TEXT,
        FORMATTED_SETS_STRING TEXT,
        FORMATTED_PROJECTS_STRING TEXT,
        FORMATTED_PLATES_STRING TEXT,
        FORMATTED_ALIASES_STRING TEXT,
        FORMATTED_FRAGMENTS_STRING TEXT,
        PSEUDO_ATOMS TEXT,
        MTXT TEXT,
        SALT_SMILES CHARACTER VARYING(1024),
        SALT_NAME CHARACTER VARYING(1024),
        SALT_MF CHARACTER VARYING(1024),
        SALT_MW DOUBLE PRECISION,
        PARENT_STOICHIOMETRY DOUBLE PRECISION,
        SALT_STOICHIOMETRY DOUBLE PRECISION,
        INCHI TEXT,
        INCHI_AUX TEXT,
        CTAB TEXT,
        CAN_SMI TEXT,
        CAN_TAUT TEXT,
        CAN_TAUT_STRIP_STEREO TEXT,
        MOLECULAR_FORMULA CHARACTER VARYING(1024),
        LOG_D DOUBLE PRECISION,
        COUNT_HYD_BOND_ACCEPTORS INTEGER,
        COUNT_HYD_BOND_DONORS INTEGER,
        SURFACE_AREA DOUBLE PRECISION,
        SOLUBILITY DOUBLE PRECISION,
        COUNT_RINGS INTEGER,
        COUNT_ATOMS INTEGER,
        COUNT_BONDS INTEGER,
        COUNT_SINGLE_BONDS INTEGER,
        COUNT_DOUBLE_BONDS INTEGER,
        COUNT_TRIPLE_BONDS INTEGER,
        COUNT_ROTATABLE_BONDS INTEGER,
        COUNT_HYDROGEN_ATOMS INTEGER,
        COUNT_METAL_ATOMS INTEGER,
        COUNT_HEAVY_ATOMS INTEGER,
        COUNT_POSITIVE_ATOMS INTEGER,
        COUNT_NEGATIVE_ATOMS INTEGER,
        COUNT_RING_BONDS INTEGER,
        COUNT_STEREO_ATOMS INTEGER,
        COUNT_STEREO_BONDS INTEGER,
        COUNT_RING_ASSEMBLIES INTEGER,
        COUNT_AROMATIC_BONDS INTEGER,
        COUNT_AROMATIC_RINGS INTEGER,
        FORMAL_CHARGE INTEGER,
        THE_A_LOG_P DOUBLE PRECISION,
        GENERAL_COMMENT CHARACTER VARYING(1024),
        PURITY_COMMENT CHARACTER VARYING(1024),
        STEREOCHEMISTRY_COMMENT CHARACTER VARYING(1024),
        IDENTIFIER_STRING CHARACTER VARYING(1024),
        DESCRIPTOR_STRING CHARACTER VARYING(1024),
        MOLECULAR_WEIGHT DOUBLE PRECISION,
        NSC_CMPD_TYPE CHARACTER VARYING(1024),
        primary key (ID)
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

    create table NSC_CMPD (
        ID BIGINT not null,
        NAME CHARACTER VARYING(1024),
        NSC_CMPD_ID BIGINT unique,
        PREFIX CHARACTER VARYING(1024) not null,
        NSC INTEGER unique,
        CONF CHARACTER VARYING(1024) not null,
        DISTRIBUTION CHARACTER VARYING(1024) not null,
        CAS CHARACTER VARYING(1024),
        COUNT_FRAGMENTS INTEGER,
        DISCREET CHARACTER VARYING(1024) not null,
        IDENTIFIER_STRING CHARACTER VARYING(1024),
        DESCRIPTOR_STRING CHARACTER VARYING(1024),
        MOLECULAR_WEIGHT DOUBLE PRECISION,
        MOLECULAR_FORMULA CHARACTER VARYING(1024),
        NSC_CMPD_TYPE_FK BIGINT not null,
        CMPD_PARENT_FRAGMENT_FK BIGINT unique,
        CMPD_BIO_ASSAY_FK BIGINT,
        CMPD_INVENTORY_FK BIGINT,
        CMPD_ANNOTATION_FK BIGINT,
        CMPD_LEGACY_CMPD_FK BIGINT unique,
        primary key (ID)
    );

    create table NSC_CMPD_TYPE (
        ID BIGINT not null,
        NSC_CMPD_TYPE CHARACTER VARYING(1024) not null unique,
        CMPD_TYPE_DESCRIPTION CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table RDKIT_MOL (
        ID BIGINT not null,
        NSC INTEGER,
        MOL CHARACTER VARYING(1024) not null,
        MOL_FROM_CTAB CHARACTER VARYING(1024) not null,
        primary key (ID)
    );

    create table TANIMOTO_SCORES (
        ID BIGINT not null,
        NSC1 INTEGER,
        NSC2 INTEGER,
        ATOM_PAIR DOUBLE PRECISION,
        FEAT_MORGAN DOUBLE PRECISION,
        LAYERED DOUBLE PRECISION,
        MACSS DOUBLE PRECISION,
        MORGAN_BV DOUBLE PRECISION,
        RDKIT DOUBLE PRECISION,
        TORSION_BV DOUBLE PRECISION,
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
        add constraint CMPD_FRAGMENT_CMPD_FRAGMENT_TC 
        foreign key (CMPD_FRAGMENT_TYPE_FK) 
        references CMPD_FRAGMENT_TYPE;

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

    alter table CMPD_NAMED_SETS2NSC_CMPDS 
        add constraint CMPD_NAMED_SET_NSC_CMPDS_FKC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_NAMED_SETS2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_NAMED_SETS_FKC 
        foreign key (CMPD_NAMED_SETS_FK) 
        references CMPD_NAMED_SET;

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

    create index cmpd_table_nsc on CMPD_TABLE (NSC);

    alter table CMPD_TARGETS2NSC_CMPDS 
        add constraint CMPD_TARGET_NSC_CMPDS_FKC 
        foreign key (NSC_CMPDS_FK) 
        references NSC_CMPD;

    alter table CMPD_TARGETS2NSC_CMPDS 
        add constraint NSC_CMPD_CMPD_TARGETS_FKC 
        foreign key (CMPD_TARGETS_FK) 
        references CMPD_TARGET;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_LEGACY_CMPD_FKC 
        foreign key (CMPD_LEGACY_CMPD_FK) 
        references CMPD_LEGACY_CMPD;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_INVENTORY_FKC 
        foreign key (CMPD_INVENTORY_FK) 
        references CMPD_INVENTORY;

    alter table NSC_CMPD 
        add constraint NSC_CMPDIFKC 
        foreign key (ID) 
        references CMPD;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_NSC_CMPD_TYPE_FKC 
        foreign key (NSC_CMPD_TYPE_FK) 
        references NSC_CMPD_TYPE;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_PARENT_FRAGMENTC 
        foreign key (CMPD_PARENT_FRAGMENT_FK) 
        references CMPD_FRAGMENT;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_ANNOTATION_FKC 
        foreign key (CMPD_ANNOTATION_FK) 
        references CMPD_ANNOTATION;

    alter table NSC_CMPD 
        add constraint NSC_CMPD_CMPD_BIO_ASSAY_FKC 
        foreign key (CMPD_BIO_ASSAY_FK) 
        references CMPD_BIO_ASSAY;

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

    create sequence CMPD_LEGACY_CMPD_SEQ;

    create sequence CMPD_LIST_MEMBER_SEQ;

    create sequence CMPD_LIST_SEQ;

    create sequence CMPD_NAMED_SET_SEQ;

    create sequence CMPD_PLATE_SEQ;

    create sequence CMPD_PROJECT_SEQ;

    create sequence CMPD_PUB_CHEM_SID_SEQ;

    create sequence CMPD_RELATED_SEQ;

    create sequence CMPD_RELATION_TYPE_SEQ;

    create sequence CMPD_SEQ;

    create sequence CMPD_TARGET_SEQ;

    create sequence hibernate_sequence;
