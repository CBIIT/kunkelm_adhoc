// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/SpringHibernateDaoBase.vsl in andromda-spring-cartridge.
//
package mwk.pptp.androdomain;

import java.security.Principal;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import mwk.pptp.PrincipalStore;
import mwk.pptp.PropertySearch;
import mwk.pptp.Search;
import mwk.pptp.SearchParameter;
import org.andromda.spring.PaginationResult;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * <p>
 * Base Spring DAO Class: is able to create, update, remove, load, and find
 * objects of type <code>CellLineMaster</code>.
 * </p>
 *
 * @see CellLineMaster
 */
public abstract class CellLineMasterDaoBase
    extends HibernateDaoSupport
    implements CellLineMasterDao
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int transform, final Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.get - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(CellLineMasterImpl.class, id);
        return transformEntity(transform, (CellLineMaster)entity);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public CellLineMaster get(Long id)
    {
        return (CellLineMaster)this.get(TRANSFORM_NONE, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object load(final int transform, final Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.load - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(CellLineMasterImpl.class, id);
        return transformEntity(transform, (CellLineMaster)entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellLineMaster load(Long id)
    {
        return (CellLineMaster)this.load(TRANSFORM_NONE, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<CellLineMaster> loadAll()
    {
        return (Collection<CellLineMaster>) this.loadAll(CellLineMasterDao.TRANSFORM_NONE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> loadAll(final int transform)
    {
        return this.loadAll(transform, -1, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> loadAll(final int pageNumber, final int pageSize)
    {
        return this.loadAll(CellLineMasterDao.TRANSFORM_NONE, pageNumber, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> loadAll(final int transform, final int pageNumber, final int pageSize)
    {
        try
        {
            final Criteria criteria = this.getSession(false).createCriteria(CellLineMasterImpl.class);
            if (pageNumber > 0 && pageSize > 0)
            {
                criteria.setFirstResult(this.calculateFirstResult(pageNumber, pageSize));
                criteria.setMaxResults(pageSize);
            }
            final Collection<?> results = criteria.list();
            this.transformEntities(transform, results);
            return results;
        }
        catch (HibernateException ex)
        {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * firstResult = (pageNumber - 1) * pageSize
     * @param pageNumber
     * @param pageSize
     * @return firstResult
     */
    protected int calculateFirstResult(int pageNumber, int pageSize)
    {
        int firstResult = 0;
        if (pageNumber > 0)
        {
            firstResult = (pageNumber - 1) * pageSize;
        }
        return firstResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellLineMaster create(CellLineMaster cellLineMaster)
    {
        return (CellLineMaster)this.create(CellLineMasterDao.TRANSFORM_NONE, cellLineMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final int transform, final CellLineMaster cellLineMaster)
    {
        if (cellLineMaster == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.create - 'cellLineMaster' can not be null");
        }
        this.getHibernateTemplate().save(cellLineMaster);
        return this.transformEntity(transform, cellLineMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<CellLineMaster> create(final Collection<CellLineMaster> entities)
    {
        return (Collection<CellLineMaster>) create(CellLineMasterDao.TRANSFORM_NONE, entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> create(final int transform, final Collection<CellLineMaster> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (CellLineMaster entity : entities)
                    {
                        create(transform, entity);
                    }
                    return null;
                }
            });
        return entities;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellLineMaster create(
        String pptpIdentifier,
        String strain,
        String implantSite)
    {
        return (CellLineMaster)this.create(CellLineMasterDao.TRANSFORM_NONE, pptpIdentifier, strain, implantSite);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(
        final int transform,
        String pptpIdentifier,
        String strain,
        String implantSite)
    {
        CellLineMaster entity = new CellLineMasterImpl();
        entity.setPptpIdentifier(pptpIdentifier);
        entity.setStrain(strain);
        entity.setImplantSite(implantSite);
        return this.create(transform, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(CellLineMaster cellLineMaster)
    {
        if (cellLineMaster == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.update - 'cellLineMaster' can not be null");
        }
        this.getHibernateTemplate().update(cellLineMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Collection<CellLineMaster> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (CellLineMaster entity : entities)
                    {
                        update(entity);
                    }
                    return null;
                }
            });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(CellLineMaster cellLineMaster)
    {
        if (cellLineMaster == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.remove - 'cellLineMaster' can not be null");
        }
        this.getHibernateTemplate().delete(cellLineMaster);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Long id)
    {
        if (id == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.remove - 'id' can not be null");
        }
        CellLineMaster entity = this.get(id);
        if (entity != null)
        {
            this.remove(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Collection<CellLineMaster> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "CellLineMaster.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }
    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>CellLineMasterDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link CellLineMasterDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see CellLineMasterDao#transformEntity(int, CellLineMaster)
     */
    public Object transformEntity(final int transform, final CellLineMaster entity)
    {
        Object target = null;
        if (entity != null)
        {
            switch (transform)
            {
                case CellLineMasterDao.TRANSFORM_NONE : // fall-through
                default:
                    target = entity;
            }
        }
        return target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transformEntities(final int transform, final Collection<?> entities)
    {
        switch (transform)
        {
            case CellLineMasterDao.TRANSFORM_NONE : // fall-through
                default:
                // do nothing;
        }
    }

    /**
     * @see CellLineMasterDao#toEntities(Collection)
     */
    public void toEntities(final Collection<?> results)
    {
        if (results != null)
        {
            CollectionUtils.transform(results, this.ENTITYTRANSFORMER);
        }
    }

    /**
     * This anonymous transformer is designed to transform report query results
     * (which result in an array of entities) to {@link CellLineMaster}
     * using the Jakarta Commons-Collections Transformation API.
     */
    private Transformer ENTITYTRANSFORMER =
        new Transformer()
        {
            public Object transform(Object input)
            {
                Object result = null;
                if (input instanceof Object[])
                {
                    result = toEntity((Object[])input);
                }
                else if (input instanceof CellLineMaster)
                {
                    result = input;
                }
                return result;
            }
        };

    /**
     * @param row
     * @return CellLineMaster
     */
    protected CellLineMaster toEntity(Object[] row)
    {
        CellLineMaster target = null;
        if (row != null)
        {
            final int numberOfObjects = row.length;
            for (int ctr = 0; ctr < numberOfObjects; ctr++)
            {
                final Object object = row[ctr];
                if (object instanceof CellLineMaster)
                {
                    target = (CellLineMaster)object;
                    break;
                }
            }
        }
        return target;
    }

    /**
     * Gets the current <code>principal</code> if one has been set,
     * otherwise returns <code>null</code>.
     *
     * @return the current principal
     */
    protected Principal getPrincipal()
    {
        return PrincipalStore.get();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({ "unchecked" })
    public PaginationResult search(final int transform, final int pageNumber, final int pageSize, final Search search)
    {
        try
        {
            search.setPageNumber(pageNumber);
            search.setPageSize(pageSize);
            final PropertySearch propertySearch = new PropertySearch(
                this.getSession(false), CellLineMasterImpl.class, search);
            final List results = propertySearch.executeAsList();
            this.transformEntities(transform, results);
            return new PaginationResult(results.toArray(new Object[results.size()]), propertySearch.getTotalCount());
        }
        catch (HibernateException ex)
        {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaginationResult search(final int pageNumber, final int pageSize, final Search search)
    {
        return this.search(CellLineMasterDao.TRANSFORM_NONE, pageNumber, pageSize, search);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<?> search(final int transform, final Search search)
    {
        try
        {
            final PropertySearch propertySearch = new PropertySearch(
                this.getSession(false), CellLineMasterImpl.class, search);
            final Set<?> results = propertySearch.executeAsSet();
            this.transformEntities(transform, results);
            return results;
        }
        catch (HibernateException ex)
        {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<CellLineMaster> search(final Search search)
    {
        return (Set<CellLineMaster>) this.search(CellLineMasterDao.TRANSFORM_NONE, search);
    }

    /**
     * Executes and returns the given Hibernate queryObject as a {@link PaginationResult} instance.
     * @param queryObject
     * @param transform
     * @param pageNumber
     * @param pageSize
     * @return PaginationResult
     */
    @SuppressWarnings({ "unchecked" })
    protected PaginationResult getPaginationResult(
        final Query queryObject,
        final int transform, int pageNumber, int pageSize)
    {
        try
        {
            final ScrollableResults scrollableResults = queryObject.scroll();
            scrollableResults.last();
            int totalCount = scrollableResults.getRowNumber();
            totalCount = totalCount >= 0 ? totalCount + 1 : 0;
            if (pageNumber > 0 && pageSize > 0)
            {
                queryObject.setFirstResult(this.calculateFirstResult(pageNumber, pageSize));
                queryObject.setMaxResults(pageSize);
            }
            // Unchecked transformation because Set object is reused, cannot be strongly typed.
            Set results = new LinkedHashSet(queryObject.list());
            transformEntities(transform, results);
            return new PaginationResult(results.toArray(new Object[results.size()]), totalCount);
        }
        catch (HibernateException ex)
        {
            throw super.convertHibernateAccessException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellLineMaster searchUniquePptpIdentifier(final String pptpIdentifier)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("pptpIdentifier",pptpIdentifier,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<CellLineMaster> searchResult=this.search(search);
        switch(searchResult.size())
        {
            case 0: return null;
            case 1: return searchResult.iterator().next();
            default: throw new NonUniqueResultException(searchResult.size());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object searchUniquePptpIdentifier(final int transform, final String pptpIdentifier)
    {
        final CellLineMaster entity=this.searchUniquePptpIdentifier(pptpIdentifier);
        if(entity == null)
        {
            return null;
        }
        return transformEntity(transform, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellLineMaster searchUniqueStrain(final String strain)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("strain",strain,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<CellLineMaster> searchResult=this.search(search);
        switch(searchResult.size())
        {
            case 0: return null;
            case 1: return searchResult.iterator().next();
            default: throw new NonUniqueResultException(searchResult.size());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object searchUniqueStrain(final int transform, final String strain)
    {
        final CellLineMaster entity=this.searchUniqueStrain(strain);
        if(entity == null)
        {
            return null;
        }
        return transformEntity(transform, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CellLineMaster searchUniqueImplantSite(final String implantSite)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("implantSite",implantSite,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<CellLineMaster> searchResult=this.search(search);
        switch(searchResult.size())
        {
            case 0: return null;
            case 1: return searchResult.iterator().next();
            default: throw new NonUniqueResultException(searchResult.size());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object searchUniqueImplantSite(final int transform, final String implantSite)
    {
        final CellLineMaster entity=this.searchUniqueImplantSite(implantSite);
        if(entity == null)
        {
            return null;
        }
        return transformEntity(transform, entity);
    }

    // spring-hibernate-dao-base merge-point
}