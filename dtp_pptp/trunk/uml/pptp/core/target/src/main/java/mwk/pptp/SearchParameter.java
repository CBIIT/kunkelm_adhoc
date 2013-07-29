// license-header java merge-point
//
// Attention: Generated code! Do not modify by hand!
// Generated by: hibernate/search/SearchParameter.java.vsl in andromda-spring-cartridge.
//
package mwk.pptp;

import java.io.Serializable;

/**
 * Represents a generic search parameter that can be used with property
 * searches.
 *
 * @see PropertySearch
 */
public class SearchParameter
    implements Serializable
{
    /** 0 */
    public static final int LIKE_COMPARATOR = 0;
    /** 1 */
    public static final int INSENSITIVE_LIKE_COMPARATOR = 1;
    /** 2 */
    public static final int EQUAL_COMPARATOR = 2;
    /** 3 */
    public static final int GREATER_THAN_OR_EQUAL_COMPARATOR = 3;
    /** 4 */
    public static final int GREATER_THAN_COMPARATOR = 4;
    /** 5 */
    public static final int LESS_THAN_OR_EQUAL_COMPARATOR = 5;
    /** 6 */
    public static final int LESS_THAN_COMPARATOR = 6;
    /** 7 */
    public static final int IN_COMPARATOR = 7;
    /** 8 */
    public static final int NOT_EQUAL_COMPARATOR = 8;
    /** 9 */
    public static final int NOT_NULL_COMPARATOR = 9;
    /** 10 */
    public static final int NULL_COMPARATOR = 10;
    /** 11 */
    public static final int NOT_IN_COMPARATOR = 11;
    /** 12 */
    public static final int NOT_LIKE_COMPARATOR = 12;
    /** 13 */
    public static final int NOT_INSENSITIVE_LIKE_COMPARATOR = 13;
    /** 14 */
    public static final int EMPTY_COMPARATOR = 14;
    /** 15 */
    public static final int NOT_EMPTY_COMPARATOR = 15;

    /** Order unset */
    public static final int ORDER_UNSET = -1;

    /** Ascending order */
    public static final int ORDER_ASC = 0;

    /** Descending order */
    public static final int ORDER_DESC = 1;

    /** 0 */
    public static final int MATCH_ANYWHERE = 0;
    /** 1 */
    public static final int MATCH_START = 1;
    /** 2 */
    public static final int MATCH_END = 2;
    /** 3 */
    public static final int MATCH_EXACT = 3;

    /**
     * Constructor taking name and value properties.
     * @param nameIn
     * @param valueIn
     */
    public SearchParameter(
        String nameIn,
        Object valueIn)
    {
       this(nameIn, valueIn, EQUAL_COMPARATOR);
    }

    /**
     * Constructor taking name and comparator properties.
     * @param nameIn
     * @param comparatorIn
     */
    public SearchParameter(
        String nameIn,
        int comparatorIn)
    {
        this(nameIn, null, comparatorIn);
    }

    /**
     * Constructor taking name, value and comparator properties.
     * @param nameIn
     * @param valueIn
     * @param comparatorIn
     */
    public SearchParameter(
        String nameIn,
        Object valueIn,
        int comparatorIn)
    {
        this(nameIn, valueIn, comparatorIn, MATCH_EXACT);
    }

    /**
     * Constructor taking name, value, comparator and order properties.
     * @param nameIn
     * @param valueIn
     * @param comparatorIn
     * @param matchIn
     */
    public SearchParameter(
        String nameIn,
        Object valueIn,
        int comparatorIn,
        int matchIn)
    {
        this(nameIn, valueIn, comparatorIn, matchIn, ORDER_UNSET);
    }

    /**
     * Constructor taking name, value, comparator, order and match properties.
     * @param nameIn
     * @param valueIn
     * @param comparatorIn
     * @param matchIn
     * @param orderIn
     */
    public SearchParameter(
        String nameIn,
        Object valueIn,
        int comparatorIn,
        int matchIn,
        int orderIn)
    {
        this(nameIn, valueIn, comparatorIn, matchIn, orderIn, false);
    }

    /**
     * Constructor taking all properties.
     * @param nameIn
     * @param valueIn
     * @param comparatorIn
     * @param matchIn
     * @param orderIn
     * @param searchIfNullIn
     */
    public SearchParameter(
        String nameIn,
        Object valueIn,
        int comparatorIn,
        int matchIn,
        int orderIn,
        boolean searchIfNullIn)
    {
        this.name = nameIn;
        this.value = valueIn;
        this.comparator = comparatorIn;
        this.order = orderIn;
        this.match = matchIn;
        this.searchIfNull = searchIfNullIn;
    }

    /**
     * Copies constructor from other SearchParameter
     * @param otherBean
     */
    public SearchParameter(SearchParameter otherBean)
    {
        if (otherBean != null)
        {
            this.name = otherBean.getName();
            this.value = otherBean.getValue();
            this.comparator = otherBean.getComparator();
            this.order = otherBean.getOrder();
            this.match = otherBean.getMatch();
            this.searchIfNull = otherBean.isSearchIfNull();
        }
    }


    private String name;

    /**
     * The parameter name in dot notation (i.e. person.firstNAme).
     * @return name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * @param nameIn
     */
    public void setName(String nameIn)
    {
        this.name = nameIn;
    }

    private Object value;

    /**
     * The value of the parameter.
     * @return value
     */
    public Object getValue()
    {
        return this.value;
    }

    /**
     * @param valueIn
     */
    public void setValue(Object valueIn)
    {
        this.value = valueIn;
    }

    private int comparator = EQUAL_COMPARATOR;

    /**
     * @return comparator
     */
    public int getComparator()
    {
        return this.comparator;
    }

    /**
     * @param comparatorIn
     */
    public void setComparator(int comparatorIn)
    {
        this.comparator = comparatorIn;
    }

    private int order;

    /**
     * Order attribute to control if parameter is used in order by clause.
     * @return order
     */
    public int getOrder()
    {
        return this.order;
    }

    /**
     * @param orderIn
     */
    public void setOrder(int orderIn)
    {
        this.order = orderIn;
    }

    private boolean searchIfNull;

    /**
     * Whether this parameter will be included in the search even if it is <code>null</code>.
     * @return true/false searchIfNull
     */
    public boolean isSearchIfNull()
    {
        return this.searchIfNull;
    }

    /**
     * Defines whether parameter will be included in the search even if it is <code>null</code>.
     *
     * @param searchIfNullIn <code>true</code> if the parameter should be included in the search
     *                     even if it is null, <code>false</code> otherwise.
     */
    public void setSearchIfNull(boolean searchIfNullIn)
    {
        this.searchIfNull = searchIfNullIn;
    }

    private int match;

    /**
     * Gets the match attribute which controls how parameter values are matched (anywhere, start, end or exact).
     *
     * @return the match mode
     */
    public int getMatch()
    {
        return this.match;
    }

    /**
     * Sets the match mode attribute which controls how parameter values are matched (anywhere, start, end or exact).
     * @param matchIn the match mode
     */
    public void setMatch(int matchIn)
    {
        this.match = matchIn;
    }

    private static final long serialVersionUID = 1L;
}