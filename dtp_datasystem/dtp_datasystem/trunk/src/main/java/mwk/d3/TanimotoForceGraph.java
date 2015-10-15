/*
 
 
 
 */
package mwk.d3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class TanimotoForceGraph implements Serializable {

    private static final long serialVersionUID = 7816871687987333379L;

    public Info info;
    public List<Node> nodes;
    public List<TanimotoLink> links;

    public TanimotoForceGraph() {
        info = new Info();
        nodes = new ArrayList<Node>();
        links = new ArrayList<TanimotoLink>();
    }

}
