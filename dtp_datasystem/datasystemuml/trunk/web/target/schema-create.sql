
    create table AD_HOC_CMPD (
        ID NUMBER(19) not null,
        AD_HOC_CMPD_ID NUMBER(19) unique,
        NAME VARCHAR2(255),
        ORIGINAL_AD_HOC_CMPD_ID NUMBER(19),
        AD_HOC_CMPD_PARENT_FRAGMENT_FK NUMBER(19) unique,
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT (
        ID NUMBER(19) not null,
        STOICHIOMETRY NUMBER(38,15),
        AD_HOC_CMPD_FRAGMENT_P_CHEM_FK NUMBER(19) unique,
        AD_HOC_CMPD_FRAGMENT_STRUCT_FK NUMBER(19) unique,
        AD_HOC_CMPD_FK NUMBER(19),
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT_P_CHEM (
        ID NUMBER(19) not null,
        MOLECULAR_WEIGHT NUMBER(38,15),
        MOLECULAR_FORMULA VARCHAR2(255),
        LOG_D NUMBER(38,15),
        COUNT_HYD_BOND_ACCEPTORS NUMBER(10),
        COUNT_HYD_BOND_DONORS NUMBER(10),
        SURFACE_AREA NUMBER(38,15),
        SOLUBILITY NUMBER(38,15),
        COUNT_RINGS NUMBER(10),
        COUNT_ATOMS NUMBER(10),
        COUNT_BONDS NUMBER(10),
        COUNT_SINGLE_BONDS NUMBER(10),
        COUNT_DOUBLE_BONDS NUMBER(10),
        COUNT_TRIPLE_BONDS NUMBER(10),
        COUNT_ROTATABLE_BONDS NUMBER(10),
        COUNT_HYDROGEN_ATOMS NUMBER(10),
        COUNT_METAL_ATOMS NUMBER(10),
        COUNT_HEAVY_ATOMS NUMBER(10),
        COUNT_POSITIVE_ATOMS NUMBER(10),
        COUNT_NEGATIVE_ATOMS NUMBER(10),
        COUNT_RING_BONDS NUMBER(10),
        COUNT_STEREO_ATOMS NUMBER(10),
        COUNT_STEREO_BONDS NUMBER(10),
        COUNT_RING_ASSEMBLIES NUMBER(10),
        COUNT_AROMATIC_BONDS NUMBER(10),
        COUNT_AROMATIC_RINGS NUMBER(10),
        FORMAL_CHARGE NUMBER(10),
        THE_A_LOG_P NUMBER(38,15),
        primary key (ID)
    );

    create table AD_HOC_CMPD_FRAGMENT_STRUCTURE (
        ID NUMBER(19) not null,
        INCHI CLOB,
        INCHI_AUX CLOB,
        CTAB CLOB,
        CAN_SMI CLOB,
        CAN_TAUT CLOB,
        CAN_TAUT_STRIP_STEREO CLOB,
        primary key (ID)
    );

    create table CMPD (
        ID NUMBER(19) not null,
        CMPD_COMMENT VARCHAR2(255),
        primary key (ID)
    );

    create table CMPD_ALIAS (
        ID NUMBER(19) not null,
        ALIAS VARCHAR2(255) not null,
        CMPD_ALIAS_TYPE_FK NUMBER(19) not null,
        primary key (ID)
    );

    create table CMPD_ALIASES2NSC_CMPDS (
        NSC_CMPDS_FK NUMBER(19) not null,
        CMPD_ALIASES_FK NUMBER(19) not null,
        primary key (NSC_CMPDS_FK, CMPD_ALIASES_FK)
    );

    create table CMPD_ALIAS_TYPE (
        ID NUMBER(19) not null,
        ALIAS_TYPE VARCHAR2(255) not null unique,
        primary key (ID)
    );

    create table CMPD_ANNOTATION (
        ID NUMBER(19) not null,
        GENERAL_COMMENT VARCHAR2(255),
        PURITY_COMMENT VARCHAR2(255),
        STEREOCHEMISTRY_COMMENT VARCHAR2(255),
        MTXT CLOB,
        PSEUDO_ATOMS CLOB,
        primary key (ID)
    );

    create table CMPD_BIO_ASSAY (
        ID NUMBER(19) not null,
        NCI60 NUMBER(10),
        HF NUMBER(10),
        XENO NUMBER(10),
        SARCOMA NUMBER(10),
        primary key (ID)
    );

    create table CMPD_FRAGMENT (
        ID NUMBER(19) not null,
        STOICHIOMETRY NUMBER(38,15),
        CMPD_FRAGMENT_COMMENT VARCHAR2(255),
        CMPD_FRAGMENT_TYPE_FK NUMBER(19) not null,
        CMPD_KNOWN_SALT_FK NUMBER(19),
        CMPD_FRAGMENT_P_CHEM_FK NUMBER(19) unique,
        CMPD_FRAGMENT_STRUCTURE_FK NUMBER(19) unique,
        NSC_CMPD_FK NUMBER(19),
        primary key (ID)
    );

    create table CMPD_FRAGMENT_P_CHEM (
        ID NUMBER(19) not null,
        MOLECULAR_WEIGHT NUMBER(38,15),
        MOLECULAR_FORMULA VARCHAR2(255),
        LOG_D NUMBER(38,15),
        COUNT_HYD_BOND_ACCEPTORS NUMBER(10),
        COUNT_HYD_BOND_DONORS NUMBER(10),
        SURFACE_AREA NUMBER(38,15),
        SOLUBILITY NUMBER(38,15),
        COUNT_RINGS NUMBER(10),
        COUNT_ATOMS NUMBER(10),
        COUNT_BONDS NUMBER(10),
        COUNT_SINGLE_BONDS NUMBER(10),
        COUNT_DOUBLE_BONDS NUMBER(10),
        COUNT_TRIPLE_BONDS NUMBER(10),
        COUNT_ROTATABLE_BONDS NUMBER(10),
        COUNT_HYDROGEN_ATOMS NUMBER(10),
        COUNT_METAL_ATOMS NUMBER(10),
        COUNT_HEAVY_ATOMS NUMBER(10),
        COUNT_POSITIVE_ATOMS NUMBER(10),
        COUNT_NEGATIVE_ATOMS NUMBER(10),
        COUNT_RING_BONDS NUMBER(10),
        COUNT_STEREO_ATOMS NUMBER(10),
        COUNT_STEREO_BONDS NUMBER(10),
        COUNT_RING_ASSEMBLIES NUMBER(10),
        COUNT_AROMATIC_BONDS NUMBER(10),
        COUNT_AROMATIC_RINGS NUMBER(10),
        FORMAL_CHARGE NUMBER(10),
        THE_A_LOG_P NUMBER(38,15),
        primary key (ID)
    );

    create table CMPD_FRAGMENT_STRUCTURE (
        ID NUMBER(19) not null,
        INCHI CLOB,
        INCHI_AUX CLOB,
        CTAB CLOB,
        CAN_SMI CLOB,
        CAN_TAUT CLOB,
        CAN_TAUT_STRIP_STEREO CLOB,
        primary key (ID)
    );

    create table CMPD_FRAGMENT_TYPE (
        ID NUMBER(19) not null,
        FRAGMENT_TYPE VARCHAR2(255) not null unique,
        FRAGMENT_TYPE_DESCRIPTION VARCHAR2(255) not null,
        primary key (ID)
    );

    create table CMPD_INVENTORY (
        ID NUMBER(19) not null,
        INVENTORY NUMBER(38,15),
        primary key (ID)
    );

    create table CMPD_KNOWN_SALT (
        ID NUMBER(19) not null,
        SALT_NAME VARCHAR2(255),
        SALT_MF VARCHAR2(255),
        SALT_MW NUMBER(38,15),
        CAN_SMI CLOB,
        CAN_TAUT CLOB,
        CAN_TAUT_STRIP_STEREO CLOB,
        COUNT_OCCURENCES NUMBER(10),
        SALT_COMMENT VARCHAR2(255) not null,
        primary key (ID)
    );

    create table CMPD_LEGACY_CMPD (
        ID NUMBER(19) not null,
        CTAB CLOB,
        MOLECULAR_WEIGHT NUMBER(38,15),
        MOLECULAR_FORMULA VARCHAR2(255),
        JPG512 BLOB,
        primary key (ID)
    );

    create table CMPD_LIST (
        ID NUMBER(19) not null,
        LIST_NAME VARCHAR2(255) not null,
        DATE_CREATED TIMESTAMP(9) not null,
        LIST_OWNER VARCHAR2(255) not null,
        SHARE_WITH VARCHAR2(255) not null,
        CMPD_LIST_ID NUMBER(19) unique,
        COUNT_LIST_MEMBERS NUMBER(10),
        ANCHOR_SMILES VARCHAR2(255),
        LIST_COMMENT VARCHAR2(255),
        ANCHOR_COMMENT VARCHAR2(255),
        primary key (ID)
    );

    create table CMPD_LIST_MEMBER (
        ID NUMBER(19) not null,
        LIST_MEMBER_COMMENT VARCHAR2(255),
        CMPD_LIST_FK NUMBER(19) not null,
        CMPD_FK NUMBER(19) not null,
        primary key (ID)
    );

    create table CMPD_NAMED_SET (
        ID NUMBER(19) not null,
        SET_NAME VARCHAR2(255) not null unique,
        SET_DESCRIPTION VARCHAR2(255) not null unique,
        primary key (ID)
    );

    create table CMPD_NAMED_SETS2NSC_CMPDS (
        NSC_CMPDS_FK NUMBER(19) not null,
        CMPD_NAMED_SETS_FK NUMBER(19) not null,
        primary key (NSC_CMPDS_FK, CMPD_NAMED_SETS_FK)
    );

    create table CMPD_PLATE (
        ID NUMBER(19) not null,
        PLATE_NAME VARCHAR2(255) not null unique,
        primary key (ID)
    );

    create table CMPD_PLATES2NSC_CMPDS (
        NSC_CMPDS_FK NUMBER(19) not null,
        CMPD_PLATES_FK NUMBER(19) not null,
        primary key (NSC_CMPDS_FK, CMPD_PLATES_FK)
    );

    create table CMPD_PROJECT (
        ID NUMBER(19) not null,
        PROJECT_CODE VARCHAR2(255) not null unique,
        PROJECT_NAME VARCHAR2(255) not null unique,
        primary key (ID)
    );

    create table CMPD_PROJECTS2NSC_CMPDS (
        NSC_CMPDS_FK NUMBER(19) not null,
        CMPD_PROJECTS_FK NUMBER(19) not null,
        primary key (NSC_CMPDS_FK, CMPD_PROJECTS_FK)
    );

    create table CMPD_PUB_CHEM_SID (
        ID NUMBER(19) not null,
        SID NUMBER(19),
        primary key (ID)
    );

    create table CMPD_PUB_CHEM_SIDS2NSC_CMPDS (
        NSC_CMPDS_FK NUMBER(19) not null,
        CMPD_PUB_CHEM_SIDS_FK NUMBER(19) not null,
        primary key (NSC_CMPDS_FK, CMPD_PUB_CHEM_SIDS_FK)
    );

    create table CMPD_RELATED (
        ID NUMBER(19) not null,
        NSC_CMPD_FK NUMBER(19) not null,
        CMPD_RELATION_TYPE_FK NUMBER(19) not null unique,
        primary key (ID)
    );

    create table CMPD_RELATION_TYPE (
        ID NUMBER(19) not null,
        RELATION_TYPE VARCHAR2(255) not null unique,
        primary key (ID)
    );

    create table CMPD_TABLE (
        ID NUMBER(19) not null,
        PREFIX VARCHAR2(255),
        NSC NUMBER(10) unique,
        CAS VARCHAR2(255),
        NAME VARCHAR2(255),
        DISCREET VARCHAR2(255),
        CONF VARCHAR2(255),
        DISTRIBUTION VARCHAR2(255),
        NSC_CMPD_ID NUMBER(19) unique,
        AD_HOC_CMPD_ID NUMBER(19) unique,
        ORIGINAL_AD_HOC_CMPD_ID NUMBER(19),
        INVENTORY NUMBER(38,15),
        NCI60 NUMBER(10),
        HF NUMBER(10),
        XENO NUMBER(10),
        FORMATTED_TARGETS_STRING CLOB,
        FORMATTED_SETS_STRING CLOB,
        FORMATTED_PROJECTS_STRING CLOB,
        FORMATTED_PLATES_STRING CLOB,
        FORMATTED_ALIASES_STRING CLOB,
        FORMATTED_FRAGMENTS_STRING CLOB,
        PSEUDO_ATOMS CLOB,
        MTXT CLOB,
        SALT_SMILES VARCHAR2(255),
        SALT_NAME VARCHAR2(255),
        SALT_MF VARCHAR2(255),
        SALT_MW NUMBER(38,15),
        PARENT_STOICHIOMETRY NUMBER(38,15),
        SALT_STOICHIOMETRY NUMBER(38,15),
        INCHI CLOB,
        INCHI_AUX CLOB,
        CTAB CLOB,
        CAN_SMI CLOB,
        CAN_TAUT CLOB,
        CAN_TAUT_STRIP_STEREO CLOB,
        MOLECULAR_FORMULA VARCHAR2(255),
        LOG_D NUMBER(38,15),
        COUNT_HYD_BOND_ACCEPTORS NUMBER(10),
        COUNT_HYD_BOND_DONORS NUMBER(10),
        SURFACE_AREA NUMBER(38,15),
        SOLUBILITY NUMBER(38,15),
        COUNT_RINGS NUMBER(10),
        COUNT_ATOMS NUMBER(10),
        COUNT_BONDS NUMBER(10),
        COUNT_SINGLE_BONDS NUMBER(10),
        COUNT_DOUBLE_BONDS NUMBER(10),
        COUNT_TRIPLE_BONDS NUMBER(10),
        COUNT_ROTATABLE_BONDS NUMBER(10),
        COUNT_HYDROGEN_ATOMS NUMBER(10),
        COUNT_METAL_ATOMS NUMBER(10),
        COUNT_HEAVY_ATOMS NUMBER(10),
        COUNT_POSITIVE_ATOMS NUMBER(10),
        COUNT_NEGATIVE_ATOMS NUMBER(10),
        COUNT_RING_BONDS NUMBER(10),
        COUNT_STEREO_ATOMS NUMBER(10),
        COUNT_STEREO_BONDS NUMBER(10),
        COUNT_RING_ASSEMBLIES NUMBER(10),
        COUNT_AROMATIC_BONDS NUMBER(10),
        COUNT_AROMATIC_RINGS NUMBER(10),
        FORMAL_CHARGE NUMBER(10),
        THE_A_LOG_P NUMBER(38,15),
        GENERAL_COMMENT VARCHAR2(255),
        PURITY_COMMENT VARCHAR2(255),
        STEREOCHEMISTRY_COMMENT VARCHAR2(255),
        IDENTIFIER_STRING VARCHAR2(255),
        DESCRIPTOR_STRING VARCHAR2(255),
        MOLECULAR_WEIGHT NUMBER(38,15),
        NSC_CMPD_TYPE VARCHAR2(255),
        primary key (ID)
    );

    create table CMPD_TARGET (
        ID NUMBER(19) not null,
        TARGET VARCHAR2(255) not null unique,
        primary key (ID)
    );

    create table CMPD_TARGETS2NSC_CMPDS (
        NSC_CMPDS_FK NUMBER(19) not null,
        CMPD_TARGETS_FK NUMBER(19) not null,
        primary key (NSC_CMPDS_FK, CMPD_TARGETS_FK)
    );

    create table NSC_CMPD (
        ID NUMBER(19) not null,
        NAME VARCHAR2(255),
        NSC_CMPD_ID NUMBER(19) unique,
        PREFIX VARCHAR2(255) not null,
        NSC NUMBER(10) unique,
        CONF VARCHAR2(255) not null,
        DISTRIBUTION VARCHAR2(255) not null,
        CAS VARCHAR2(255),
        COUNT_FRAGMENTS NUMBER(10),
        DISCREET VARCHAR2(255) not null,
        IDENTIFIER_STRING VARCHAR2(255),
        DESCRIPTOR_STRING VARCHAR2(255),
        MOLECULAR_WEIGHT NUMBER(38,15),
        MOLECULAR_FORMULA VARCHAR2(255),
        NSC_CMPD_TYPE_FK NUMBER(19) not null,
        CMPD_PARENT_FRAGMENT_FK NUMBER(19) unique,
        CMPD_BIO_ASSAY_FK NUMBER(19),
        CMPD_INVENTORY_FK NUMBER(19),
        CMPD_ANNOTATION_FK NUMBER(19),
        CMPD_LEGACY_CMPD_FK NUMBER(19) unique,
        primary key (ID)
    );

    create table NSC_CMPD_TYPE (
        ID NUMBER(19) not null,
        NSC_CMPD_TYPE VARCHAR2(255) not null unique,
        CMPD_TYPE_DESCRIPTION VARCHAR2(255) not null,
        primary key (ID)
    );

    create table RDKIT_MOL (
        ID NUMBER(19) not null,
        NSC NUMBER(10),
        MOL VARCHAR2(255) not null,
        MOL_FROM_CTAB VARCHAR2(255) not null,
        primary key (ID)
    );

    create table TANIMOTO_SCORES (
        ID NUMBER(19) not null,
        NSC1 NUMBER(10),
        NSC2 NUMBER(10),
        ATOM_PAIR NUMBER(38,15),
        FEAT_MORGAN NUMBER(38,15),
        LAYERED NUMBER(38,15),
        MACSS NUMBER(38,15),
        MORGAN_BV NUMBER(38,15),
        RDKIT NUMBER(38,15),
        TORSION_BV NUMBER(38,15),
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
