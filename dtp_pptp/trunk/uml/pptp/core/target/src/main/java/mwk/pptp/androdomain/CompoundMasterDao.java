// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: SpringDao.vsl in andromda-spring-cartridge.
//
package mwk.pptp.androdomain;

import java.util.Collection;
import java.util.Set;
import mwk.pptp.Search;
import org.andromda.spring.PaginationResult;

/**
 *
 * @see CompoundMaster
 */
public interface CompoundMasterDao
{
    /**
     * This constant is used as a transformation flag; entities can be converted automatically into value objects
     * or other types, different methods in a class implementing this interface support this feature: look for
     * an <code>int</code> parameter called <code>transform</code>.
     * <p>
     * This specific flag denotes no transformation will occur.
     */
    public static final int TRANSFORM_NONE = 0;


    /**
     * Transforms the given results to a collection of {@link CompoundMaster}
     * instances (this is useful when the returned results contains a row of data and you want just entities only).
     *
     * @param results the query results.
     */
    public void toEntities(final Collection<?> results);

    /**
     * Gets an instance of CompoundMaster from the persistent store.
     * @param id
     * @return CompoundMaster
     */
    public CompoundMaster get(Long id);

    /**
     * <p>
     * Does the same thing as {@link #get(Long)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined in this class then the result <strong>WILL BE</strong> passed through an operation which can
     * optionally transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     *
     * @param transform flag to determine transformation type.
     * @param id the identifier of the entity to get.
     * @return either the entity or the object transformed from the entity.
     */
    public Object get(int transform, Long id);

    /**
     * Loads an instance of CompoundMaster from the persistent store.
     * @param id
     * @return CompoundMaster
     */
    public CompoundMaster load(Long id);

    /**
     * <p>
     * Does the same thing as {@link #load(Long)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined in this class then the result <strong>WILL BE</strong> passed through an operation which can
     * optionally transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     *
     * @param transform flag to determine transformation type.
     * @param id the identifier of the entity to load.
     * @return either the entity or the object transformed from the entity.
     */
    public Object load(int transform, Long id);

    /**
     * Loads all entities of type {@link CompoundMaster}.
     *
     * @return the loaded entities.
     */
    public Collection<CompoundMaster> loadAll();

    /**
     * <p>
     * Does the same thing as {@link #loadAll()} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     *
     * @param transform the flag indicating what transformation to use.
     * @return the loaded entities.
     */
    public Collection<?> loadAll(final int transform);

    /**
     * <p>
     * Does the same thing as {@link #loadAll()} with an
     * additional two arguments called <code>pageNumber</code> and <code>pageSize</code>. The <code>pageNumber</code>
     * argument allows you to specify the page number when you are paging the results and the pageSize allows you to specify the size of the
     * page retrieved.
     * </p>
     *
     * @param pageNumber the page number to retrieve when paging results.
     * @param pageSize the size of the page to retrieve when paging results.
     * @return the loaded entities.
     */
    public Collection<?> loadAll(final int pageNumber, final int pageSize);

    /**
     * <p>
     * Does the same thing as {@link #loadAll(int)} with an
     * additional two arguments called <code>pageNumber</code> and <code>pageSize</code>. The <code>pageNumber</code>
     * argument allows you to specify the page number when you are paging the results and the pageSize allows you to specify the size of the
     * page retrieved.
     * </p>
     *
     * @param transform the flag indicating what transformation to use.
     * @param pageNumber the page number to retrieve when paging results.
     * @param pageSize the size of the page to retrieve when paging results.
     * @return the loaded entities.
     */
    public Collection<?> loadAll(final int transform, final int pageNumber, final int pageSize);

    /**
     * Creates an instance of CompoundMaster and adds it to the persistent store.
     * @param compoundMaster
     * @return CompoundMaster
     */
    public CompoundMaster create(CompoundMaster compoundMaster);

    /**
     * <p>
     * Does the same thing as {@link #create(CompoundMaster)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     * @param transform
     * @param compoundMaster
     * @return Object
     */
    public Object create(int transform, CompoundMaster compoundMaster);

    /**
     * Creates a new instance of CompoundMaster and adds
     * from the passed in <code>entities</code> collection
     *
     * @param entities the collection of CompoundMaster
     * instances to create.
     *
     * @return the created instances.
     */
    public Collection<CompoundMaster> create(Collection<CompoundMaster> entities);

    /**
     * <p>
     * Does the same thing as {@link #create(CompoundMaster)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     * </p>
     * @param transform
     * @param entities
     * @return Collection
     */
    public Collection<?> create(int transform, Collection<CompoundMaster> entities);

    /**
     * <p>
     * Creates a new <code>CompoundMaster</code>
     * instance from <strong>all</strong> attributes and adds it to
     * the persistent store.
     * </p>
     * @param pptpIdentifier 
     * @param solidDose 
     * @param concentrationUnit 
     * @param treatmentRoute 
     * @param schedule 
     * @param vehicle 
     * @param allDose 
     * @return CompoundMaster
     */
    public CompoundMaster create(
        String pptpIdentifier,
        Double solidDose,
        String concentrationUnit,
        String treatmentRoute,
        String schedule,
        String vehicle,
        Double allDose);

    /**
     * <p>
     * Does the same thing as {@link #create(String, Double, String, String, String, String, Double)} with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * the returned entity will <strong>NOT</strong> be transformed. If this flag is any of the other constants
     * defined here then the result <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entity (into a value object for example). By default, transformation does
     * not occur.
     * </p>
     * @param transform
     * @param pptpIdentifier 
     * @param solidDose 
     * @param concentrationUnit 
     * @param treatmentRoute 
     * @param schedule 
     * @param vehicle 
     * @param allDose 
     * @return CompoundMaster
     */
    public Object create(
        int transform,
        String pptpIdentifier,
        Double solidDose,
        String concentrationUnit,
        String treatmentRoute,
        String schedule,
        String vehicle,
        Double allDose);


    /**
     * Updates the <code>compoundMaster</code> instance in the persistent store.
     * @param compoundMaster
     */
    public void update(CompoundMaster compoundMaster);

    /**
     * Updates all instances in the <code>entities</code> collection in the persistent store.
     * @param entities
     */
    public void update(Collection<CompoundMaster> entities);

    /**
     * Removes the instance of CompoundMaster from the persistent store.
     * @param compoundMaster
     */
    public void remove(CompoundMaster compoundMaster);

    /**
     * Removes the instance of CompoundMaster having the given
     * <code>identifier</code> from the persistent store.
     * @param id
     */
    public void remove(Long id);

    /**
     * Removes all entities in the given <code>entities</code> collection.
     * @param entities
     */
    public void remove(Collection<CompoundMaster> entities);


    /**
     * Does the same thing as {@link #search(int, Search)} but with an
     * additional two flags called <code>pageNumber</code> and <code>pageSize</code>. These flags allow you to
     * limit your data to a specified page number and size.
     *
     * @param transform the transformation flag.
     * @param pageNumber the page number in the data to retrieve
     * @param pageSize the size of the page to retrieve.
     * @param search the search object which provides the search parameters and pagination specification.
     * @return any found results from the search wrapped in a {@link PaginationResult} instance.
     */
    public PaginationResult search(final int transform, final int pageNumber, final int pageSize, final Search search);

    /**
     * Does the same thing as {@link #search(Search)} but with an
     * additional two flags called <code>pageNumber</code> and <code>pageSize</code>. These flags allow you to
     * limit your data to a specified page number and size.
     *
     * @param pageNumber the page number in the data to retrieve
     * @param pageSize the size of the page to retrieve.
     * @param search the search object which provides the search parameters and pagination specification.
     * @return any found results from the search wrapped in a {@link PaginationResult} instance.
     */
    public PaginationResult search(final int pageNumber, final int pageSize, final Search search);

    /**
     * Does the same thing as {@link #search(Search)} but with an
     * additional flag called <code>transform</code>. If this flag is set to <code>TRANSFORM_NONE</code> then
     * finder results will <strong>NOT</strong> be transformed during retrieval.
     * If this flag is any of the other constants defined here
     * then results <strong>WILL BE</strong> passed through an operation which can optionally
     * transform the entities (into value objects for example). By default, transformation does
     * not occur.
     *
     * @param transform the transformation flag.
     * @param search the search object which provides the search parameters and pagination specification.
     * @return any found results from the search.
     */
    public Set<?> search(final int transform, final Search search);

    /**
     * Performs a search using the parameters specified in the given <code>search</code> object.
     *
     * @param search the search object which provides the search parameters and pagination specification.
     * @return any found results from the search.
     */
    public Set<CompoundMaster> search(final Search search);

    /**
     * Allows transformation of entities into value objects
     * (or something else for that matter), when the <code>transform</code>
     * flag is set to one of the constants defined in <code>mwk.pptp.androdomain.CompoundMasterDao</code>, please note
     * that the {@link #TRANSFORM_NONE} constant denotes no transformation, so the entity itself
     * will be returned.
     *
     * If the integer argument value is unknown {@link #TRANSFORM_NONE} is assumed.
     *
     * @param transform one of the constants declared in {@link mwk.pptp.androdomain.CompoundMasterDao}
     * @param entity an entity that was found
     * @return the transformed entity (i.e. new value object, etc)
     * @see #transformEntities(int,Collection)
     */
    public Object transformEntity(final int transform, final CompoundMaster entity);

    /**
     * Transforms a collection of entities using the
     * {@link #transformEntity(int,CompoundMaster)}
     * method. This method does not instantiate a new collection.
     * <p>
     * This method is to be used internally only.
     *
     * @param transform one of the constants declared in <code>mwk.pptp.androdomain.CompoundMasterDao</code>
     * @param entities the collection of entities to transform
     * @see #transformEntity(int,CompoundMaster)
     */
    public void transformEntities(final int transform, final Collection<?> entities);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param pptpIdentifier the unique pptpIdentifier to be used in the search.'
     * @return a single instance of CompoundMaster.
     */
    public CompoundMaster searchUniquePptpIdentifier(final String pptpIdentifier);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param transform the transformation flag.
     * @param pptpIdentifier the unique pptpIdentifier to be used in the search.
     * @return a value object according the transformation flag.
     */
    public Object searchUniquePptpIdentifier(final int transform, final String pptpIdentifier);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param solidDose the unique solidDose to be used in the search.'
     * @return a single instance of CompoundMaster.
     */
    public CompoundMaster searchUniqueSolidDose(final Double solidDose);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param transform the transformation flag.
     * @param solidDose the unique solidDose to be used in the search.
     * @return a value object according the transformation flag.
     */
    public Object searchUniqueSolidDose(final int transform, final Double solidDose);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param concentrationUnit the unique concentrationUnit to be used in the search.'
     * @return a single instance of CompoundMaster.
     */
    public CompoundMaster searchUniqueConcentrationUnit(final String concentrationUnit);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param transform the transformation flag.
     * @param concentrationUnit the unique concentrationUnit to be used in the search.
     * @return a value object according the transformation flag.
     */
    public Object searchUniqueConcentrationUnit(final int transform, final String concentrationUnit);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param treatmentRoute the unique treatmentRoute to be used in the search.'
     * @return a single instance of CompoundMaster.
     */
    public CompoundMaster searchUniqueTreatmentRoute(final String treatmentRoute);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param transform the transformation flag.
     * @param treatmentRoute the unique treatmentRoute to be used in the search.
     * @return a value object according the transformation flag.
     */
    public Object searchUniqueTreatmentRoute(final int transform, final String treatmentRoute);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param schedule the unique schedule to be used in the search.'
     * @return a single instance of CompoundMaster.
     */
    public CompoundMaster searchUniqueSchedule(final String schedule);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param transform the transformation flag.
     * @param schedule the unique schedule to be used in the search.
     * @return a value object according the transformation flag.
     */
    public Object searchUniqueSchedule(final int transform, final String schedule);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param vehicle the unique vehicle to be used in the search.'
     * @return a single instance of CompoundMaster.
     */
    public CompoundMaster searchUniqueVehicle(final String vehicle);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param transform the transformation flag.
     * @param vehicle the unique vehicle to be used in the search.
     * @return a value object according the transformation flag.
     */
    public Object searchUniqueVehicle(final int transform, final String vehicle);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param allDose the unique allDose to be used in the search.'
     * @return a single instance of CompoundMaster.
     */
    public CompoundMaster searchUniqueAllDose(final Double allDose);

    /**
     * Searches for a single instance of CompoundMaster.
     * @param transform the transformation flag.
     * @param allDose the unique allDose to be used in the search.
     * @return a value object according the transformation flag.
     */
    public Object searchUniqueAllDose(final int transform, final Double allDose);

    // spring-dao merge-point
}