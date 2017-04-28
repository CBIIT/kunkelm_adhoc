
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

    alter table ALIASES2CURATED_NSC_TO_ALIASES 
        drop constraint CURATED_NAME_CURATED_NSC_TO_AC;

    alter table ALIASES2CURATED_NSC_TO_ALIASES 
        drop constraint CURATED_NSC_ALIASES_FKC;

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
        drop constraint CMPD_FRAGMENT_CMPD_FRAGMENT_TC;

    alter table CMPD_FRAGMENT 
        drop constraint CMPD_FRAGMENT_CMPD_FRAGMENT_SC;

    alter table CMPD_FRAGMENT 
        drop constraint CMPD_FRAGMENT_CMPD_FRAGMENT_PC;

    alter table CMPD_LIST_MEMBER 
        drop constraint CMPD_LIST_MEMBER_CMPD_LIST_FKC;

    alter table CMPD_LIST_MEMBER 
        drop constraint CMPD_LIST_MEMBER_CMPD_FKC;

    alter table CMPD_NAMED_SETS2NSC_CMPDS 
        drop constraint CMPD_NAMED_SET_NSC_CMPDS_FKC;

    alter table CMPD_NAMED_SETS2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_NAMED_SETS_FKC;

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

    alter table CMPD_TARGETS2NSC_CMPDS 
        drop constraint CMPD_TARGET_NSC_CMPDS_FKC;

    alter table CMPD_TARGETS2NSC_CMPDS 
        drop constraint NSC_CMPD_CMPD_TARGETS_FKC;

    alter table CURATED_NSC 
        drop constraint CURATED_NSC_ORIGINATOR_FKC;

    alter table CURATED_NSC 
        drop constraint CURATED_NSC_GENERIC_NAME_FKC;

    alter table CURATED_NSC 
        drop constraint CURATED_NSC_PREFERRED_NAME_FKC;

    alter table CURATED_NSC 
        drop constraint CURATED_NSC_PRIMARY_TARGET_FKC;

    alter table CURATED_NSCS2PROJECTS 
        drop constraint CURATED_PROJECT_CURATED_NSCS_C;

    alter table CURATED_NSCS2PROJECTS 
        drop constraint CURATED_NSC_PROJECTS_FKC;

    alter table CURATED_NSC_TO_SECONDARY_TARGE 
        drop constraint CURATED_TARGET_CURATED_NSC_TOC;

    alter table CURATED_NSC_TO_SECONDARY_TARGE 
        drop constraint CURATED_NSC_SECONDARY_TARGETSC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_LEGACY_CMPD_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_INVENTORY_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPDIFKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_SALT_FRAGMENT_FC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_NSC_CMPD_TYPE_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_PARENT_FRAGMENTC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_ANNOTATION_FKC;

    alter table NSC_CMPD 
        drop constraint NSC_CMPD_CMPD_BIO_ASSAY_FKC;

    drop table AD_HOC_CMPD cascade;

    drop table AD_HOC_CMPD_FRAGMENT cascade;

    drop table AD_HOC_CMPD_FRAGMENT_P_CHEM cascade;

    drop table AD_HOC_CMPD_FRAGMENT_STRUCTURE cascade;

    drop table ALIASES2CURATED_NSC_TO_ALIASES cascade;

    drop table CMPD cascade;

    drop table CMPD_ALIAS cascade;

    drop table CMPD_ALIASES2NSC_CMPDS cascade;

    drop table CMPD_ALIAS_TYPE cascade;

    drop table CMPD_ANNOTATION cascade;

    drop table CMPD_BIO_ASSAY cascade;

    drop table CMPD_FRAGMENT cascade;

    drop table CMPD_FRAGMENT_P_CHEM cascade;

    drop table CMPD_FRAGMENT_STRUCTURE cascade;

    drop table CMPD_FRAGMENT_TYPE cascade;

    drop table CMPD_INVENTORY cascade;

    drop table CMPD_KNOWN_SALT cascade;

    drop table CMPD_LEGACY_CMPD cascade;

    drop table CMPD_LIST cascade;

    drop table CMPD_LIST_MEMBER cascade;

    drop table CMPD_NAMED_SET cascade;

    drop table CMPD_NAMED_SETS2NSC_CMPDS cascade;

    drop table CMPD_PLATE cascade;

    drop table CMPD_PLATES2NSC_CMPDS cascade;

    drop table CMPD_PROJECT cascade;

    drop table CMPD_PROJECTS2NSC_CMPDS cascade;

    drop table CMPD_PUB_CHEM_SID cascade;

    drop table CMPD_PUB_CHEM_SIDS2NSC_CMPDS cascade;

    drop table CMPD_RELATED cascade;

    drop table CMPD_RELATION_TYPE cascade;

    drop table CMPD_TABLE cascade;

    drop table CMPD_TARGET cascade;

    drop table CMPD_TARGETS2NSC_CMPDS cascade;

    drop table CURATED_NAME cascade;

    drop table CURATED_NSC cascade;

    drop table CURATED_NSCS2PROJECTS cascade;

    drop table CURATED_NSC_TO_SECONDARY_TARGE cascade;

    drop table CURATED_ORIGINATOR cascade;

    drop table CURATED_PROJECT cascade;

    drop table CURATED_TARGET cascade;

    drop table NSC_CMPD cascade;

    drop table NSC_CMPD_TYPE cascade;

    drop table RDKIT_MOL cascade;

    drop table TANIMOTO_SCORES cascade;

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

    drop sequence CMPD_LEGACY_CMPD_SEQ;

    drop sequence CMPD_LIST_MEMBER_SEQ;

    drop sequence CMPD_LIST_SEQ;

    drop sequence CMPD_NAMED_SET_SEQ;

    drop sequence CMPD_PLATE_SEQ;

    drop sequence CMPD_PROJECT_SEQ;

    drop sequence CMPD_PUB_CHEM_SID_SEQ;

    drop sequence CMPD_RELATED_SEQ;

    drop sequence CMPD_RELATION_TYPE_SEQ;

    drop sequence CMPD_SEQ;

    drop sequence CMPD_TARGET_SEQ;

    drop sequence CURATED_NAME_SEQ;

    drop sequence CURATED_NSC_SEQ;

    drop sequence CURATED_ORIGINATOR_SEQ;

    drop sequence CURATED_PROJECT_SEQ;

    drop sequence CURATED_TARGET_SEQ;

    drop sequence hibernate_sequence;
