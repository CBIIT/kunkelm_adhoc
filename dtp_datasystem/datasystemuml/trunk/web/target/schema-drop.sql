
    alter table AD_HOC_CMPD 
        drop constraint AD_HOC_CMPDIFKC;

    alter table AD_HOC_CMPD 
        drop constraint AD_HOC_CMPD_AD_HOC_CMPD_PARENC;

    alter table AD_HOC_CMPD_FRAGMENT 
        drop constraint AD_HOC_CMPD_FRAGMENT_AD_HOC_CH;

    alter table AD_HOC_CMPD_FRAGMENT 
        drop constraint AD_HOC_CMPD_FRAGMENT_AD_HOC_CC;

    alter table AD_HOC_CMPD_FRAGMENT 
        drop constraint AD_HOC_CMPD_FRAGMENT_AD_HOC_CD;

    alter table CMPD_ALIAS 
        drop constraint CMPD_ALIAS_CMPD_ALIAS_TYPE_FKC;

    alter table CMPD_ALIASES2NSC_CMPDS 
        drop constraint CMPD_ALIAS_NSC_CMPDS_FKC;

    alter table CMPD_ALIASES2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_ALIASES_FKC;

    alter table CMPD_FRAGMENT 
        drop constraint CMPD_FRAGMENT_NSC_CMPD_FKC;

    alter table CMPD_FRAGMENT 
        drop constraint CMPD_FRAGMENT_CMPD_KNOWN_SALTC;

    alter table CMPD_FRAGMENT 
        drop constraint CMPD_FRAGMENT_CMPD_FRAGMENT_SC;

    alter table CMPD_FRAGMENT 
        drop constraint CMPD_FRAGMENT_CMPD_FRAGMENT_PC;

    alter table CMPD_LISTS2SHARE_WITH_USERS 
        drop constraint DATA_SYSTEM_USER_CMPD_LISTS_FC;

    alter table CMPD_LISTS2SHARE_WITH_USERS 
        drop constraint CMPD_LIST_SHARE_WITH_USERS_FKC;

    alter table CMPD_LIST_MEMBER 
        drop constraint CMPD_LIST_MEMBER_CMPD_LIST_FKC;

    alter table CMPD_LIST_MEMBER 
        drop constraint CMPD_LIST_MEMBER_CMPD_FKC;

    alter table CMPD_PLATES2NSC_CMPDS 
        drop constraint CMPD_PLATE_NSC_CMPDS_FKC;

    alter table CMPD_PLATES2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_PLATES_FKC;

    alter table CMPD_PROJECTS2NSC_CMPDS 
        drop constraint CMPD_PROJECT_NSC_CMPDS_FKC;

    alter table CMPD_PROJECTS2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_PROJECTS_FKC;

    alter table CMPD_PUB_CHEM_SIDS2NSC_CMPDS 
        drop constraint CMPD_PUB_CHEM_SID_NSC_CMPDS_FC;

    alter table CMPD_PUB_CHEM_SIDS2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_PUB_CHEM_SIDS_FC;

    alter table CMPD_RELATED 
        drop constraint CMPD_RELATED_NSC_CMPD_FKM;

    alter table CMPD_RELATED 
        drop constraint CMPD_RELATED_CMPD_RELATION_TYC;

    alter table CMPD_SETS2NSC_CMPDS 
        drop constraint CMPD_SET_NSC_CMPDS_FKC;

    alter table CMPD_SETS2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_SETS_FKC;

    alter table CMPD_TARGETS2NSC_CMPDS 
        drop constraint CMPD_TARGET_NSC_CMPDS_FKC;

    alter table CMPD_TARGETS2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_TARGETS_FKC;

    alter table DATA_SYSTEM_ROLES2DATA_SYSTEM_ 
        drop constraint DATA_SYSTEM_ROLE_DATA_SYSTEM_C;

    alter table DATA_SYSTEM_ROLES2DATA_SYSTEM_ 
        drop constraint DATA_SYSTEM_USER_DATA_SYSTEM_C;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_LEGACY_CMPD_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_ANNOTATIONS_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_INVENTORY_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPDIFKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_NSC_CMPD_TYPE_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_PARENT_FRAGMENTC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_BIO_ASSAY_FKC;

    drop table AD_HOC_CMPD cascade;

    drop table AD_HOC_CMPD_FRAGMENT cascade;

    drop table AD_HOC_CMPD_FRAGMENT_P_CHEM cascade;

    drop table AD_HOC_CMPD_FRAGMENT_STRUCTURE cascade;

    drop table CMPD cascade;

    drop table CMPD_ALIAS cascade;

    drop table CMPD_ALIASES2NSC_CMPDS cascade;

    drop table CMPD_ALIAS_TYPE cascade;

    drop table CMPD_ANNOTATIONS cascade;

    drop table CMPD_BIO_ASSAY cascade;

    drop table CMPD_FRAGMENT cascade;

    drop table CMPD_FRAGMENT_P_CHEM cascade;

    drop table CMPD_FRAGMENT_STRUCTURE cascade;

    drop table CMPD_INVENTORY cascade;

    drop table CMPD_KNOWN_SALT cascade;

    drop table CMPD_LEGACY_CMPD cascade;

    drop table CMPD_LIST cascade;

    drop table CMPD_LISTS2SHARE_WITH_USERS cascade;

    drop table CMPD_LIST_MEMBER cascade;

    drop table CMPD_PLATE cascade;

    drop table CMPD_PLATES2NSC_CMPDS cascade;

    drop table CMPD_PROJECT cascade;

    drop table CMPD_PROJECTS2NSC_CMPDS cascade;

    drop table CMPD_PUB_CHEM_SID cascade;

    drop table CMPD_PUB_CHEM_SIDS2NSC_CMPDS cascade;

    drop table CMPD_RELATED cascade;

    drop table CMPD_RELATION_TYPE cascade;

    drop table CMPD_SET cascade;

    drop table CMPD_SETS2NSC_CMPDS cascade;

    drop table CMPD_TABLE cascade;

    drop table CMPD_TARGET cascade;

    drop table CMPD_TARGETS2NSC_CMPDS cascade;

    drop table DATA_SYSTEM_ROLE cascade;

    drop table DATA_SYSTEM_ROLES2DATA_SYSTEM_ cascade;

    drop table DATA_SYSTEM_USER cascade;

    drop table NSC_CMPD cascade;

    drop table NSC_CMPD_TYPE cascade;

    drop table RDKIT_MOL cascade;

    drop sequence AD_HOC_CMPD_FRAGMENT_P_CHE_SEQ;

    drop sequence AD_HOC_CMPD_FRAGMENT_SEQ;

    drop sequence AD_HOC_CMPD_FRAGMENT_STRUC_SEQ;

    drop sequence CMPD_ALIAS_SEQ;

    drop sequence CMPD_ALIAS_TYPE_SEQ;

    drop sequence CMPD_BIO_ASSAY_SEQ;

    drop sequence CMPD_FRAGMENT_P_CHEM_SEQ;

    drop sequence CMPD_FRAGMENT_SEQ;

    drop sequence CMPD_FRAGMENT_STRUCTURE_SEQ;

    drop sequence CMPD_INVENTORY_SEQ;

    drop sequence CMPD_KNOWN_SALT_SEQ;

    drop sequence CMPD_LIST_MEMBER_SEQ;

    drop sequence CMPD_LIST_SEQ;

    drop sequence CMPD_PLATE_SEQ;

    drop sequence CMPD_PROJECT_SEQ;

    drop sequence CMPD_PUB_CHEM_SID_SEQ;

    drop sequence CMPD_RELATED_SEQ;

    drop sequence CMPD_RELATION_TYPE_SEQ;

    drop sequence CMPD_SEQ;

    drop sequence CMPD_SET_SEQ;

    drop sequence CMPD_TARGET_SEQ;

    drop sequence hibernate_sequence;
