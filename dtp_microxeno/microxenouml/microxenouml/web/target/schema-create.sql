
    create table AFFYMETRIX_DATA (
        ID BIGINT not null,
        VALUE DOUBLE PRECISION,
        REPLICATE CHARACTER VARYING(1024),
        PASSAGE CHARACTER VARYING(1024),
        EA_ID CHARACTER VARYING(1024),
        TUMOR_FK BIGINT not null,
        AFFYMETRIX_IDENTIFIER_FK BIGINT not null,
        primary key (ID)
    );

    create table AFFYMETRIX_IDENTIFIER (
        ID BIGINT not null,
        PROBE_SET_ID CHARACTER VARYING(1024) not null unique,
        ACCESSION CHARACTER VARYING(1024),
        GENECARD CHARACTER VARYING(1024),
        primary key (ID)
    );

    create table FLAT_DATA (
        ID BIGINT not null,
        PROBE_SET_ID CHARACTER VARYING(1024) not null,
        ACCESSION CHARACTER VARYING(1024),
        GENECARD CHARACTER VARYING(1024),
        TUMOR_NAME CHARACTER VARYING(1024) not null,
        TUMOR_TYPE CHARACTER VARYING(1024) not null,
        EA_ID CHARACTER VARYING(1024) not null,
        PASSAGE CHARACTER VARYING(1024) not null,
        REPLICATE CHARACTER VARYING(1024) not null,
        VALUE DOUBLE PRECISION,
        primary key (ID)
    );

    create table TUMOR (
        ID BIGINT not null,
        TUMOR_NAME CHARACTER VARYING(1024) not null unique,
        TUMOR_TYPE_FK BIGINT not null,
        primary key (ID)
    );

    create table TUMOR_TYPE (
        ID BIGINT not null,
        TUMOR_TYPE CHARACTER VARYING(1024) not null unique,
        primary key (ID)
    );

    create index ea_id_idx on AFFYMETRIX_DATA (EA_ID);

    create index ad_tum_idx on AFFYMETRIX_DATA (TUMOR_FK);

    create index ad_ai_idx on AFFYMETRIX_DATA (AFFYMETRIX_IDENTIFIER_FK);

    create index passage_idx on AFFYMETRIX_DATA (PASSAGE);

    create index replicate_idx on AFFYMETRIX_DATA (REPLICATE);

    alter table AFFYMETRIX_DATA 
        add constraint AFFYMETRIX_DATA_TUMOR_FKC 
        foreign key (TUMOR_FK) 
        references TUMOR;

    alter table AFFYMETRIX_DATA 
        add constraint AFFYMETRIX_DATA_AFFYMETRIX_IDC 
        foreign key (AFFYMETRIX_IDENTIFIER_FK) 
        references AFFYMETRIX_IDENTIFIER;

    create index accession_idx on AFFYMETRIX_IDENTIFIER (ACCESSION);

    create index probe_set_id_idx on AFFYMETRIX_IDENTIFIER (PROBE_SET_ID);

    create index genecard_idx on AFFYMETRIX_IDENTIFIER (GENECARD);

    create index fd_tumor_name_idx on FLAT_DATA (TUMOR_NAME);

    create index fd_replicate_idx on FLAT_DATA (REPLICATE);

    create index fd_ea_id_idx on FLAT_DATA (EA_ID);

    create index fd_probe_set_id_idx on FLAT_DATA (PROBE_SET_ID);

    create index fd_genecard_idx on FLAT_DATA (GENECARD);

    create index fd_passage_idx on FLAT_DATA (PASSAGE);

    create index fd_tumor_type_idx on FLAT_DATA (TUMOR_TYPE);

    create index fd_accession_idx on FLAT_DATA (ACCESSION);

    alter table TUMOR 
        add constraint TUMOR_TUMOR_TYPE_FKC 
        foreign key (TUMOR_TYPE_FK) 
        references TUMOR_TYPE;

    create sequence hibernate_sequence;
