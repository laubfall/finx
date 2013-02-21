/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.ludwig.finx.io;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.text.StrMatcher;
import org.apache.commons.lang3.text.StrTokenizer;
import org.junit.Assert;
import org.junit.Test;

import de.ludwig.finx.io.I18nNode;

/**
 *
 * @author Daniel
 */
public class I18nNodeTest {
    @Test
    public void testCreateNode(){
        I18nNode create = I18nNode.create("de.ludwig.test", "DE", "Hello World");
        Assert.assertNotNull(create);
        Assert.assertEquals("de", create.getI18nKeyPart());
        
        I18nNode parent = create.getParent();
        Assert.assertNull(parent);
        
        List<I18nNode> childs = create.getChilds();
        Assert.assertNotNull(childs);
        Assert.assertFalse(childs.isEmpty());
        
        I18nNode child = childs.get(0);
        Assert.assertEquals("ludwig", child.getI18nKeyPart());
        Assert.assertNotNull(child.getParent());
        Assert.assertEquals("de", child.getParent().getI18nKeyPart());
        
        Assert.assertNotNull(child.getChilds());
        
        child = child.getChilds().get(0);
        Assert.assertEquals("test", child.getI18nKeyPart());
        Assert.assertNotNull(child.getParent());
        Assert.assertEquals("ludwig", child.getParent().getI18nKeyPart());
    }
    
    @Test
    public void testMostMatching(){
        I18nNode n = I18nNode.create("page.div.table.head.title", "DE", "Hello you");
        I18nNode matching = I18nNode.mostMatching("page.div.table", n);
        Assert.assertNotNull(matching);
        Assert.assertEquals("table", matching.getI18nKeyPart());
        Assert.assertNotNull(matching.getParent());
        Assert.assertFalse(matching.getChilds().isEmpty());
        Assert.assertEquals(1, matching.getChilds().size());
        Assert.assertEquals("head", matching.getChilds().get(0).getI18nKeyPart());
        
        matching = I18nNode.mostMatching("page.div.table.head.title", n);
        Assert.assertNotNull(matching);
        Assert.assertEquals("title", matching.getI18nKeyPart());
        Assert.assertTrue(matching.getChilds().isEmpty());
        Assert.assertNotNull(matching.getParent());
        Assert.assertEquals("head", matching.getParent().getI18nKeyPart());
        Assert.assertEquals("Hello you", matching.value("DE"));

        matching = I18nNode.mostMatching("page.div.footer", n);
        Assert.assertNotNull(matching);
        Assert.assertEquals("div", matching.getI18nKeyPart());
        
        matching = I18nNode.mostMatching("unknown.key", n);
        Assert.assertNull(matching);
    }
    
    @Test
    public void testCreateWithParent(){
        I18nNode parent = I18nNode.attach("de.x", null, null, null);
        Assert.assertNotNull(parent);
        Assert.assertEquals("de", parent.getI18nKeyPart());
               
        I18nNode leaf = I18nNode.attach("y.z.n", I18nNode.mostMatching("de.x", parent), null, null);
        Assert.assertNotNull(leaf);
        Assert.assertNotNull(leaf.getParent());
        Assert.assertEquals("x", leaf.getParent().getI18nKeyPart());
        
        I18nNode root = leaf.getParent().getParent();
        Assert.assertNotNull(root);
        Assert.assertEquals("de", root.getI18nKeyPart());
        
        leaf = I18nNode.attach("m", leaf, "mValue", "de");
        Assert.assertNotNull(leaf);
        Assert.assertNotNull(leaf.getParent());
        leaf = root.child("de.x.y.m");
        Assert.assertNotNull(leaf);
        Assert.assertEquals("m", leaf.getI18nKeyPart());
    }
    
    @Test
    public void testRemove() {
        I18nNode root = I18nNode.create("de.ludwig.x.y.z", "DE", "text");
        I18nNode removedLeaf = I18nNode.remove("de.ludwig.x", root);
        
        Assert.assertNotNull(removedLeaf);
        I18nNode mostMatching = I18nNode.mostMatching("de.ludwig", root);
        Assert.assertNotNull(mostMatching);
        Assert.assertEquals("ludwig", mostMatching.getI18nKeyPart());
        Assert.assertTrue(mostMatching.getChilds().isEmpty());
        
        mostMatching = I18nNode.remove("blah", root);
        Assert.assertNull(mostMatching);
    }
    
    @Test
    public void retrieveKey() {
        I18nNode root = I18nNode.create("de.ludwig.x.y.z", "DE", "text");
        String key = root.key();
        
        Assert.assertNotNull(key);
        Assert.assertEquals("de", key);
        I18nNode child = root.child("de.ludwig.x.y.z");
        Assert.assertNotNull(child);
        key = child.key();
        Assert.assertNotNull(key);
        Assert.assertEquals("de.ludwig.x.y.z", key);
    }
    
    @Test
    public void flatten() {
    	I18nNode root = I18nNode.attach("de", null, null, "de");
    	I18nNode.attach("test", root, null, "de");
    	I18nNode test2 = I18nNode.attach("test2", root, null, "de");
    	I18nNode.attach("test2b", test2, null, "de");
    	
    	Assert.assertNotNull(test2.getChilds());
    	Assert.assertFalse(test2.getChilds().isEmpty());
    	
    	Assert.assertNotNull(root);
    	Assert.assertNotNull(root.getChilds());
    	Assert.assertEquals(2, root.getChilds().size());
    	
    	List<I18nNode> flatten = root.flatten();
    	Assert.assertNotNull(flatten);
    	Assert.assertEquals(4, flatten.size());
    }
}
