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
 * objects of type <code>ScheduleType</code>.
 * </p>
 *
 * @see ScheduleType
 */
public abstract class ScheduleTypeDaoBase
    extends HibernateDaoSupport
    implements ScheduleTypeDao
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
                "ScheduleType.get - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(ScheduleTypeImpl.class, id);
        return transformEntity(transform, (ScheduleType)entity);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleType get(Long id)
    {
        return (ScheduleType)this.get(TRANSFORM_NONE, id);
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
                "ScheduleType.load - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(ScheduleTypeImpl.class, id);
        return transformEntity(transform, (ScheduleType)entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScheduleType load(Long id)
    {
        return (ScheduleType)this.load(TRANSFORM_NONE, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<ScheduleType> loadAll()
    {
        return (Collection<ScheduleType>) this.loadAll(ScheduleTypeDao.TRANSFORM_NONE);
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
        return this.loadAll(ScheduleTypeDao.TRANSFORM_NONE, pageNumber, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> loadAll(final int transform, final int pageNumber, final int pageSize)
    {
        try
        {
            final Criteria criteria = this.getSession(false).createCriteria(ScheduleTypeImpl.class);
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
    public ScheduleType create(ScheduleType scheduleType)
    {
        return (ScheduleType)this.create(ScheduleTypeDao.TRANSFORM_NONE, scheduleType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final int transform, final ScheduleType scheduleType)
    {
        if (scheduleType == null)
        {
            throw new IllegalArgumentException(
                "ScheduleType.create - 'scheduleType' can not be null");
        }
        this.getHibernateTemplate().save(scheduleType);
        return this.transformEntity(transform, scheduleType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<ScheduleType> create(final Collection<ScheduleType> entities)
    {
        return (Collection<ScheduleType>) create(ScheduleTypeDao.TRANSFORM_NONE, entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> create(final int transform, final Collection<ScheduleType> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "ScheduleType.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (ScheduleType entity : entities)
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
    public ScheduleType create(
        String displayName)
    {
        return (ScheduleType)this.create(ScheduleTypeDao.TRANSFORM_NONE, displayName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(
        final int transform,
        String displayName)
    {
        ScheduleType entity = new ScheduleTypeImpl();
        entity.setDisplayName(displayName);
        return this.create(transform, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(ScheduleType scheduleType)
    {
        if (scheduleType == null)
        {
            throw new IllegalArgumentException(
                "ScheduleType.update - 'scheduleType' can not be null");
        }
        this.getHibernateTemplate().update(scheduleType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Collection<ScheduleType> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "ScheduleType.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (ScheduleType entity : entities)
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
    public void remove(ScheduleType scheduleType)
    {
        if (scheduleType == null)
        {
            throw new IllegalArgumentException(
                "ScheduleType.remove - 'scheduleType' can not be null");
        }
        this.getHibernateTemplate().delete(scheduleType);
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
                "ScheduleType.remove - 'id' can not be null");
        }
        ScheduleType entity = this.get(id);
        if (entity != null)
        {
            this.remove(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Collection<ScheduleType> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "ScheduleType.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }
    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>ScheduleTypeDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link ScheduleTypeDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see ScheduleTypeDao#transformEntity(int, ScheduleType)
     */
    public Object transformEntity(final int transform, final ScheduleType entity)
    {
        Object target = null;
        if (entity != null)
        {
            switch (transform)
            {
                case ScheduleTypeDao.TRANSFORM_NONE : // fall-through
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
            case ScheduleTypeDao.TRANSFORM_NONE : // fall-through
                default:
                // do nothing;
        }
    }

    /**
     * @see ScheduleTypeDao#toEntities(Collection)
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
     * (which result in an array of entities) to {@link ScheduleType}
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
                else if (input instanceof ScheduleType)
                {
                    result = input;
                }
                return result;
            }
        };

    /**
     * @param row
     * @return ScheduleType
     */
    protected ScheduleType toEntity(Object[] row)
    {
        ScheduleType target = null;
        if (row != null)
        {
            final int numberOfObjects = row.length;
            for (int ctr = 0; ctr < numberOfObjects; ctr++)
            {
                final Object object = row[ctr];
                if (object instanceof ScheduleType)
                {
                    target = (ScheduleType)object;
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
                this.getSession(false), ScheduleTypeImpl.class, search);
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
        return this.search(ScheduleTypeDao.TRANSFORM_NONE, pageNumber, pageSize, search);
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
                this.getSession(false), ScheduleTypeImpl.class, search);
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
    public Set<ScheduleType> search(final Search search)
    {
        return (Set<ScheduleType>) this.search(ScheduleTypeDao.TRANSFORM_NONE, search);
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
    public ScheduleType searchUniqueDisplayName(final String displayName)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("displayName",displayName,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<ScheduleType> searchResult=this.search(search);
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
    public Object searchUniqueDisplayName(final int transform, final String displayName)
    {
        final ScheduleType entity=this.searchUniqueDisplayName(displayName);
        if(entity == null)
        {
            return null;
        }
        return transformEntity(transform, entity);
    }

    // spring-hibernate-dao-base merge-point
}