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
 * objects of type <code>MouseCrosstab</code>.
 * </p>
 *
 * @see MouseCrosstab
 */
public abstract class MouseCrosstabDaoBase
    extends HibernateDaoSupport
    implements MouseCrosstabDao
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
                "MouseCrosstab.get - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(MouseCrosstabImpl.class, id);
        return transformEntity(transform, (MouseCrosstab)entity);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public MouseCrosstab get(Long id)
    {
        return (MouseCrosstab)this.get(TRANSFORM_NONE, id);
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
                "MouseCrosstab.load - 'id' can not be null");
        }
        final Object entity = this.getHibernateTemplate().get(MouseCrosstabImpl.class, id);
        return transformEntity(transform, (MouseCrosstab)entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseCrosstab load(Long id)
    {
        return (MouseCrosstab)this.load(TRANSFORM_NONE, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<MouseCrosstab> loadAll()
    {
        return (Collection<MouseCrosstab>) this.loadAll(MouseCrosstabDao.TRANSFORM_NONE);
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
        return this.loadAll(MouseCrosstabDao.TRANSFORM_NONE, pageNumber, pageSize);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> loadAll(final int transform, final int pageNumber, final int pageSize)
    {
        try
        {
            final Criteria criteria = this.getSession(false).createCriteria(MouseCrosstabImpl.class);
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
    public MouseCrosstab create(MouseCrosstab mouseCrosstab)
    {
        return (MouseCrosstab)this.create(MouseCrosstabDao.TRANSFORM_NONE, mouseCrosstab);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(final int transform, final MouseCrosstab mouseCrosstab)
    {
        if (mouseCrosstab == null)
        {
            throw new IllegalArgumentException(
                "MouseCrosstab.create - 'mouseCrosstab' can not be null");
        }
        this.getHibernateTemplate().save(mouseCrosstab);
        return this.transformEntity(transform, mouseCrosstab);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Collection<MouseCrosstab> create(final Collection<MouseCrosstab> entities)
    {
        return (Collection<MouseCrosstab>) create(MouseCrosstabDao.TRANSFORM_NONE, entities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<?> create(final int transform, final Collection<MouseCrosstab> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "MouseCrosstab.create - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (MouseCrosstab entity : entities)
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
    public MouseCrosstab create(
        Double mouse1,
        Double mouse2,
        Double mouse3,
        Double mouse4,
        Double mouse5,
        Double mouse6,
        Double mouse7,
        Double mouse8,
        Double mouse9,
        Double mouse10)
    {
        return (MouseCrosstab)this.create(MouseCrosstabDao.TRANSFORM_NONE, mouse1, mouse2, mouse3, mouse4, mouse5, mouse6, mouse7, mouse8, mouse9, mouse10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(
        final int transform,
        Double mouse1,
        Double mouse2,
        Double mouse3,
        Double mouse4,
        Double mouse5,
        Double mouse6,
        Double mouse7,
        Double mouse8,
        Double mouse9,
        Double mouse10)
    {
        MouseCrosstab entity = new MouseCrosstabImpl();
        entity.setMouse1(mouse1);
        entity.setMouse2(mouse2);
        entity.setMouse3(mouse3);
        entity.setMouse4(mouse4);
        entity.setMouse5(mouse5);
        entity.setMouse6(mouse6);
        entity.setMouse7(mouse7);
        entity.setMouse8(mouse8);
        entity.setMouse9(mouse9);
        entity.setMouse10(mouse10);
        return this.create(transform, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MouseCrosstab create(
        MouseGroup mouseGroup)
    {
        return (MouseCrosstab)this.create(MouseCrosstabDao.TRANSFORM_NONE, mouseGroup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object create(
        final int transform,
        MouseGroup mouseGroup)
    {
        MouseCrosstab entity = new MouseCrosstabImpl();
        entity.setMouseGroup(mouseGroup);
        return this.create(transform, entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(MouseCrosstab mouseCrosstab)
    {
        if (mouseCrosstab == null)
        {
            throw new IllegalArgumentException(
                "MouseCrosstab.update - 'mouseCrosstab' can not be null");
        }
        this.getHibernateTemplate().update(mouseCrosstab);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final Collection<MouseCrosstab> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "MouseCrosstab.update - 'entities' can not be null");
        }
        this.getHibernateTemplate().executeWithNativeSession(
            new HibernateCallback()
            {
                public Object doInHibernate(Session session)
                    throws HibernateException
                {
                    for (MouseCrosstab entity : entities)
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
    public void remove(MouseCrosstab mouseCrosstab)
    {
        if (mouseCrosstab == null)
        {
            throw new IllegalArgumentException(
                "MouseCrosstab.remove - 'mouseCrosstab' can not be null");
        }
        this.getHibernateTemplate().delete(mouseCrosstab);
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
                "MouseCrosstab.remove - 'id' can not be null");
        }
        MouseCrosstab entity = this.get(id);
        if (entity != null)
        {
            this.remove(entity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(Collection<MouseCrosstab> entities)
    {
        if (entities == null)
        {
            throw new IllegalArgumentException(
                "MouseCrosstab.remove - 'entities' can not be null");
        }
        this.getHibernateTemplate().deleteAll(entities);
    }
    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>MouseCrosstabDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link MouseCrosstabDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see MouseCrosstabDao#transformEntity(int, MouseCrosstab)
     */
    public Object transformEntity(final int transform, final MouseCrosstab entity)
    {
        Object target = null;
        if (entity != null)
        {
            switch (transform)
            {
                case MouseCrosstabDao.TRANSFORM_NONE : // fall-through
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
            case MouseCrosstabDao.TRANSFORM_NONE : // fall-through
                default:
                // do nothing;
        }
    }

    /**
     * @see MouseCrosstabDao#toEntities(Collection)
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
     * (which result in an array of entities) to {@link MouseCrosstab}
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
                else if (input instanceof MouseCrosstab)
                {
                    result = input;
                }
                return result;
            }
        };

    /**
     * @param row
     * @return MouseCrosstab
     */
    protected MouseCrosstab toEntity(Object[] row)
    {
        MouseCrosstab target = null;
        if (row != null)
        {
            final int numberOfObjects = row.length;
            for (int ctr = 0; ctr < numberOfObjects; ctr++)
            {
                final Object object = row[ctr];
                if (object instanceof MouseCrosstab)
                {
                    target = (MouseCrosstab)object;
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
                this.getSession(false), MouseCrosstabImpl.class, search);
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
        return this.search(MouseCrosstabDao.TRANSFORM_NONE, pageNumber, pageSize, search);
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
                this.getSession(false), MouseCrosstabImpl.class, search);
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
    public Set<MouseCrosstab> search(final Search search)
    {
        return (Set<MouseCrosstab>) this.search(MouseCrosstabDao.TRANSFORM_NONE, search);
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
    public MouseCrosstab searchUniqueMouse1(final Double mouse1)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse1",mouse1,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse1(final int transform, final Double mouse1)
    {
        final MouseCrosstab entity=this.searchUniqueMouse1(mouse1);
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
    public MouseCrosstab searchUniqueMouse2(final Double mouse2)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse2",mouse2,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse2(final int transform, final Double mouse2)
    {
        final MouseCrosstab entity=this.searchUniqueMouse2(mouse2);
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
    public MouseCrosstab searchUniqueMouse3(final Double mouse3)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse3",mouse3,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse3(final int transform, final Double mouse3)
    {
        final MouseCrosstab entity=this.searchUniqueMouse3(mouse3);
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
    public MouseCrosstab searchUniqueMouse4(final Double mouse4)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse4",mouse4,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse4(final int transform, final Double mouse4)
    {
        final MouseCrosstab entity=this.searchUniqueMouse4(mouse4);
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
    public MouseCrosstab searchUniqueMouse5(final Double mouse5)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse5",mouse5,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse5(final int transform, final Double mouse5)
    {
        final MouseCrosstab entity=this.searchUniqueMouse5(mouse5);
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
    public MouseCrosstab searchUniqueMouse6(final Double mouse6)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse6",mouse6,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse6(final int transform, final Double mouse6)
    {
        final MouseCrosstab entity=this.searchUniqueMouse6(mouse6);
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
    public MouseCrosstab searchUniqueMouse7(final Double mouse7)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse7",mouse7,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse7(final int transform, final Double mouse7)
    {
        final MouseCrosstab entity=this.searchUniqueMouse7(mouse7);
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
    public MouseCrosstab searchUniqueMouse8(final Double mouse8)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse8",mouse8,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse8(final int transform, final Double mouse8)
    {
        final MouseCrosstab entity=this.searchUniqueMouse8(mouse8);
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
    public MouseCrosstab searchUniqueMouse9(final Double mouse9)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse9",mouse9,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse9(final int transform, final Double mouse9)
    {
        final MouseCrosstab entity=this.searchUniqueMouse9(mouse9);
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
    public MouseCrosstab searchUniqueMouse10(final Double mouse10)
    {
        final Search search=new Search(
            new SearchParameter[]{
                new SearchParameter("mouse10",mouse10,SearchParameter.EQUAL_COMPARATOR)
            }
        );

        final Set<MouseCrosstab> searchResult=this.search(search);
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
    public Object searchUniqueMouse10(final int transform, final Double mouse10)
    {
        final MouseCrosstab entity=this.searchUniqueMouse10(mouse10);
        if(entity == null)
        {
            return null;
        }
        return transformEntity(transform, entity);
    }

    // spring-hibernate-dao-base merge-point
}