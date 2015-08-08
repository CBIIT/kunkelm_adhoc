/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

/**
 *
 * @author mwkunkel
 */
public class TemplatedPair<FIRST, SECOND> implements Comparable<TemplatedPair<FIRST, SECOND>> {

    public final FIRST first;
    public final SECOND second;

    private TemplatedPair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    public static <FIRST, SECOND> TemplatedPair<FIRST, SECOND> of(FIRST first,
            SECOND second) {
        return new TemplatedPair<FIRST, SECOND>(first, second);
    }

    @Override
    public int compareTo(TemplatedPair<FIRST, SECOND> o) {
        int cmp = compare(first, o.first);
        return cmp == 0 ? compare(second, o.second) : cmp;
    }

    // todo move this to a helper class.
    private static int compare(Object o1, Object o2) {
        return o1 == null ? o2 == null ? 0 : -1 : o2 == null ? +1
                : ((Comparable) o1).compareTo(o2);
    }

    @Override
    public int hashCode() {
        return 31 * hashcode(first) + hashcode(second);
    }

    // todo move this to a helper class.
    private static int hashcode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TemplatedPair)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return equal(first, ((TemplatedPair) obj).first)
                && equal(second, ((TemplatedPair) obj).second);
    }

    // todo move this to a helper class.
    private boolean equal(Object o1, Object o2) {
        return o1 == null ? o2 == null : (o1 == o2 || o1.equals(o2));
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
