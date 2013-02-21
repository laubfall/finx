/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.io;

import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

import de.ludwig.finx.io.I18nNode;
import de.ludwig.finx.io.RootNode;

/**
 *
 * @author Daniel
 */
public class RootNodeTest {
    @Test
    public void addNode(){
        final RootNode root = new RootNode();
        root.addNode("de.ludwig.one", "de", "value");
        root.addNode("de.ludwig.two", "de", "value2");
        root.addNode("com.ludwig.one", "de", "another value");

        Assert.assertNotNull(root.getRootNodes());
        Assert.assertEquals(2, root.getRootNodes().size());
        
        final List<I18nNode> rootNodes = root.getRootNodes();
        final I18nNode deNode = rootNodes.get(0);
        Assert.assertNotNull(deNode.getChilds());
        Assert.assertFalse(deNode.getChilds().isEmpty());
        Assert.assertEquals(1, deNode.getChilds().size());
        final I18nNode deNodesOnlyChild = deNode.getChilds().get(0);
        Assert.assertNotNull(deNodesOnlyChild.getChilds());
        Assert.assertFalse(deNodesOnlyChild.getChilds().isEmpty());
        Assert.assertEquals(2, deNodesOnlyChild.getChilds().size());
        
        for(I18nNode deChilds : deNodesOnlyChild.getChilds()){
            final String value = deChilds.value("de");
            if(deChilds.getI18nKeyPart().equals("one")) {
                Assert.assertEquals("value", value);
            } else if(deChilds.getI18nKeyPart().equals("two")){
                Assert.assertEquals("value2", value);
            } else {
                Assert.fail();
            }
        }
        
        final I18nNode comNode = rootNodes.get(1);
        Assert.assertNotNull(comNode.getChilds());
        Assert.assertFalse(comNode.getChilds().isEmpty());
        Assert.assertEquals(1, comNode.getChilds().size());
    }
    
    @Test
    public void removeNode() {
        final RootNode root = new RootNode();
        root.addNode("de.ludwig.one", "de", "value");
        root.addNode("de.ludwig.two", "de", "value2");
        root.addNode("de.ludwig", "de", "value3");
        root.addNode("com.ludwig.one", "de", "another value");
    }
    
    @Test
    public void addAll() {
    	final RootNode rn = new RootNode();
    	rn.addNode("de", "de", "test");
    	rn.addAll("de.micromata");
    	List<I18nNode> childs = rn.getRootNodes();
    	Assert.assertNotNull(childs);
    	Assert.assertEquals(1, childs.size());
    	I18nNode findNode = rn.findNode("de.micromata");
    	Assert.assertNotNull(findNode);
    	
    	rn.addNode("be.blah", "de", "val");
    	childs = rn.getRootNodes();
    	Assert.assertEquals(2, childs.size());
    	
    	rn.addAll("de.test");
    	childs = rn.getRootNodes();
    	Assert.assertNotNull(childs);
    	Assert.assertEquals(2, childs.size());
    	
    	
    }
}
