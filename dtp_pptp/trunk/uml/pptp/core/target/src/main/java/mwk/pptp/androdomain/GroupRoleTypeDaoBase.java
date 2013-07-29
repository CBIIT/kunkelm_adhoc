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
 * objects of type <code>GroupRoleType</code>.
 * </p>
 *
 * @see GroupRoleType
 */
public abstract class GroupRoleTypeDaoBase
    extends HibernateDaoSupport
    implements GroupRoleTypeDao
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
                "GroupRoleType.get - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(GroupRoleTypeImpl.class, id);
        return transformEntity(transform, (GroupRoleType)entity);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public GroupRoleType get(Long id)
    {
        return (GroupRoleType)this.get(TRANSFORM_NONE, id);
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
                "GroupRoleType.load - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(GroupRoleTypeImpl.class, id);
        return transformEntity(transform, (GroupRoleType)entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GroupRoleType load(Long id)
    {
        return (GroupRoleType)this.load(TRANSFORM_NONE, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<GroupRoleType> loadAll()
    {
        return (Collection<GroupRoleType>) this.loadAll(GroupRoleTypeDao.TRANSFORM_NONE);
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
        return this.loadAll(GroupRoleTypeDao.TRANSFORM_NONE, pageNumber, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> loadAll(final int transform, final int pageNumber, final int pageSize)
    {
        try
        {
            final Criteria criteria = this.getSession(false).createCriteria(GroupRoleTypeImpl.class);
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
    public GroupRoleType create(GroupRoleType groupRoleType)
    {
        return (GroupRoleType)this.create(GroupRoleTypeDao.TRANSFORM_NONE, groupRoleType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final int transform, final GroupRoleType groupRoleType)
    {
        if (groupRoleType == null)
        {
            throw new IllegalArgumentException(
                "GroupRoleType.create - 'groupRoleType' can not be null");
        }
        this.getHibernateTemplate().save(groupRoleType);
        return this.transformEntity(transform, groupRoleType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<GroupRoleType> create(final Collection<GroupRoleType> entities)
    {
        return (Collection<GroupRoleType>) create(GroupRoleTypeDao.TRANSFORM_NONE, entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> create(final int transform, final Collection<GroupRoleType> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "GroupRoleType.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (GroupRoleType entity : entities)
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
    public GroupRoleType create(
        String displayName)
    {
        return (GroupRoleType)this.create(GroupRoleTypeDao.TRANSFORM_NONE, displayName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(
        final int transform,
        String displayName)
    {
        GroupRoleType entity = new GroupRoleTypeImpl();
        entity.setDisplayName(displayName);
        return this.create(transform, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(GroupRoleType groupRoleType)
    {
        if (groupRoleType == null)
        {
            throw new IllegalArgumentException(
                "GroupRoleType.update - 'groupRoleType' can not be null");
        }
        this.getHibernateTemplate().update(groupRoleType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Collection<GroupRoleType> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "GroupRoleType.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (GroupRoleType entity : entities)
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
    public void remove(GroupRoleType groupRoleType)
    {
        if (groupRoleType == null)
        {
            throw new IllegalArgumentException(
                "GroupRoleType.remove - 'groupRoleType' can not be null");
        }
        this.getHibernateTemplate().delete(groupRoleType);
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
                "GroupRoleType.remove - 'id' can not be null");
        }
        GroupRoleType entity = this.get(id);
        if (entity != null)
        {
            this.remove(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Collection<GroupRoleType> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "GroupRoleType.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }
    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>GroupRoleTypeDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link GroupRoleTypeDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see GroupRoleTypeDao#transformEntity(int, GroupRoleType)
     */
    public Object transformEntity(final int transform, final GroupRoleType entity)
    {
        Object target = null;
        if (entity != null)
        {
            switch (transform)
            {
                case GroupRoleTypeDao.TRANSFORM_NONE : // fall-through
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
            case GroupRoleTypeDao.TRANSFORM_NONE : // fall-through
                default:
                // do nothing;
        }
    }

    /**
     * @see GroupRoleTypeDao#toEntities(Collection)
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
     * (which result in an array of entities) to {@link GroupRoleType}
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
                else if (input instanceof GroupRoleType)
                {
                    result = input;
                }
                return result;
            }
        };

    /**
     * @param row
     * @return GroupRoleType
     */
    protected GroupRoleType toEntity(Object[] row)
    {
        GroupRoleType target = null;
        if (row != null)
        {
            final int numberOfObjects = row.length;
            for (int ctr = 0; ctr < numberOfObjects; ctr++)
            {
                final Object object = row[ctr];
                if (object instanceof GroupRoleType)
                {
                    target = (GroupRoleType)object;
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
                this.getSession(false), GroupRoleTypeImpl.class, search);
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
        return this.search(GroupRoleTypeDao.TRANSFORM_NONE, pageNumber, pageSize, search);
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
                this.getSession(false), GroupRoleTypeImpl.class, search);
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
    public Set<GroupRoleType> search(final Search search)
    {
        return (Set<GroupRoleType>) this.search(GroupRoleTypeDao.TRANSFORM_NONE, search);
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
    public GroupRoleType searchUniqueDisplayName(final String displayName)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("displayName",displayName,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<GroupRoleType> searchResult=this.search(search);
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
        final GroupRoleType entity=this.searchUniqueDisplayName(displayName);
        if(entity == null)
        {
            return null;
        }
        return transformEntity(transform, entity);
    }

    // spring-hibernate-dao-base merge-point
}