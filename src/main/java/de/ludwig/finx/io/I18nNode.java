package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.list.UnmodifiableList;
import org.apache.commons.lang3.StringUtils;

import de.ludwig.finx.Language;

/**
 * 
 * @author Daniel
 */
public class I18nNode
{

	/**
	 * Wenn der Key de.ludwig.blah ist wäre ein Key-Part "de" Das Child dieser Node hätte als
	 * Key-Part "ludwig" usw.
	 */
	private String i18nKeyPart;

	private I18nNode parent;

	private List<I18nNode> childs = new ArrayList<I18nNode>();

	/**
	 * Key Language ISO2 Code Value Der Wert des Keys, null wenn es sich nicht um das letzte
	 * Node-Element handelt oder der Key für die gewählte Sprache einfach keinen Wert hat.
	 */
	private Map<Language, String> value = new HashMap<>();

	private I18nNode()
	{

	}

	/**
	 * Erzeugt unterhalb eines I18nNodes ein Child-Element und liefert dieses zurück. Fehlende
	 * Elemente innerhalb des Pfads werden automatisch erzeugt.
	 * 
	 * Beispiel: parent: x neues child: y.z
	 * 
	 * Ergebnis: x.y.z
	 * 
	 * Beispiel: parent: y neues Child y.z
	 * 
	 * Ergebnis y.y.z
	 * 
	 * Beispiel: parent de.a.b.c
	 * 
	 * neues Child: d
	 * 
	 * Ergebnis de.a.b.c.d
	 * 
	 * Beispiel: parent de.a.b.c (c hat Childs d.e) neues Child d.e
	 * 
	 * Ergebnis: de.a.b.c.d.e
	 * 
	 * @param key
	 * @param iso2
	 * @param value
	 * @param parent
	 *            Unter diese NOde werden die neu zu erstellenden (falls noch nicht vorhandenen)
	 *            Nodes eingehangen.
	 * @return
	 */
	static I18nNode attach(final String key, final I18nNode parent, String value, Language iso2Language)
	{
		if (parent == null)
			return create(key, iso2Language, value);

		String[] parts = i18nKeySplit(key);
		List<I18nNode> childs = parent.getChilds();
		I18nNode matchingChild = (I18nNode) CollectionUtils.find(childs, new KeyPartPredicate(parts[0]));
		if (matchingChild == null) {
			I18nNode create = create(key, iso2Language, value);
			parent.childs.add(create);
			create.parent = parent;
			return create;
		}

		String partKey = "";
		if (parts.length == 1) {
			partKey = key;
		} else {
			partKey = StringUtils.join(parts, ".", 1, parts.length - 1);
		}

		return attach(partKey, matchingChild, value, iso2Language);
	}

	/**
	 * Entfernt das Leaf-Node aus einer bestehenden Hierarchie an Nodes.
	 * 
	 * Beispiel: key "de.ludwig.x"
	 * 
	 * root keyPart "de" Methode sucht unter root nach I18nNode mit keyPart "ludwig" und dann
	 * darunter (wenn gefunden) nach I18nNode mit keyPart "x". Wenn gefunden wird diese Node
	 * gelöscht.
	 * 
	 * Beispiel: key "de.ludwig.x"
	 * 
	 * root keyPart "blub" Keine Aktion da keyPart des Roots nicht mit erstem Teil des keys
	 * übereinstimmt.
	 * 
	 * @param key
	 * @param root
	 * @return Leaf-Node welches entfernt wurde. Null wenn keines unter dem übermittelten Key
	 *         gefunden wurde.
	 */
	static I18nNode remove(final String key, final I18nNode root)
	{
		final String[] i18nKeySplit = i18nKeySplit(key);
		if (i18nKeySplit.length == 0)
			return null;

		final I18nNode leafToRemove = mostMatching(key, root);
		if (leafToRemove == null)
			return null;

		boolean remove = leafToRemove.parent.childs.remove(leafToRemove);
		if (remove == false)
			throw new RuntimeException("Leaf-Node konnte nicht entfernt werden");

		return leafToRemove;
	}

	private static class KeyPartPredicate implements Predicate
	{
		private String keyPartToMatch;

		public KeyPartPredicate(final String keyPartToMatch)
		{
			this.keyPartToMatch = keyPartToMatch;
		}

		public boolean evaluate(Object o)
		{
			final I18nNode n = (I18nNode) o;
			return n.getI18nKeyPart().equals(keyPartToMatch);
		}

	}

	static I18nNode create(final String key, final Language iso2, final String value)
	{
		final Map<Language, String> val = new HashMap<>();
		val.put(iso2, value);
		return create(key, val);
	}

	/**
	 * Erzeugt auf Basis eine . separierten Schlüsselwerts die Kette von I18nNodes.
	 * 
	 * Beispiel: de.ludwig.x.y
	 * 
	 * Ergebnis: vier I18nNodes -> 1. de 2. ludwig 3. x 4. y
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	static I18nNode create(final String key, final Map<Language, String> values)
	{
		String[] parts = i18nKeySplit(key);
		I18nNode current = new I18nNode();
		I18nNode root = current;

		for (int i = 0; i < parts.length; i++) {
			current.i18nKeyPart = parts[i];
			if (i == parts.length - 1)
				current.value = values;

			if (i < parts.length - 1) {
				// current.parent = new I18nNode();
				I18nNode child = new I18nNode();
				child.parent = current;
				current.childs.add(child);
				// current = current.parent;
				current = child;
			}
		}

		return root;
	}

	/**
	 * Hilfsmethode zum Aufsplitten eines I18n-Keys.
	 * 
	 * @param key
	 * @return Nie Null.
	 */
	public static String[] i18nKeySplit(final String key)
	{
		if (key == null || StringUtils.isBlank(key)) {
			return new String[0];
		}

		String[] parts = key.split("[.]");
		return parts;
	}

	/**
	 * 
	 * page
	 * 
	 * key: page.title.one
	 * 
	 * return one
	 * 
	 * page.one
	 * 
	 * key: page.two
	 * 
	 * return page
	 * 
	 * page
	 * 
	 * key: popup.title
	 * 
	 * @param rootNode
	 *            Nicht optional. return null
	 * 
	 **/
	public static I18nNode mostMatching(final String key, I18nNode rootNode)
	{
		final String[] parts = i18nKeySplit(key);
		I18nNode lastMatching = null;

		if (rootNode.i18nKeyPart.equals(parts[0]) == false) {
			return lastMatching;
		} else {
			lastMatching = rootNode;
		}

		String partKey = "";
		if (parts.length == 1) {
			partKey = key;
		} else {
			partKey = StringUtils.join(parts, ".", 1, parts.length);
		}

		for (I18nNode c : rootNode.childs) {
			final I18nNode childMatching = mostMatching(partKey, c);
			if (childMatching == null)
				continue;

			lastMatching = childMatching;
			break;
		}

		return lastMatching;
	}

	/**
	 * Liefert den kompletten Key vom Root I18nNode bis zu dieser Node
	 * 
	 * @return
	 */
	public String key()
	{
		final List<String> keyParts = new ArrayList<String>();
		I18nNode tmp = this;
		do {
			keyParts.add(tmp.getI18nKeyPart());
			tmp = tmp.getParent();
		} while (tmp != null);

		final StringBuilder sb = new StringBuilder();
		for (int i = keyParts.size() - 1; i >= 0; i--) {
			sb.append(keyParts.get(i));
			if (i > 0) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	public I18nNode child(final String key)
	{
		I18nNode mostMatching = mostMatching(key, this);
		if (key.equals(mostMatching.key())) {
			return mostMatching;
		}
		return null;
	}

	/**
	 * Brings all nodes of the Treestructure in one line (an array).
	 * 
	 * @return
	 */
	public List<I18nNode> flatten()
	{
		return flatten(this, new ArrayList<I18nNode>());
	}

	public String keyValue(final Language language)
	{
		return key() + "=" + value(language);
	}

	/**
	 * 
	 * @param parent
	 * @param flattened
	 * @return flattened Tree-Structure with all childs of the parent (and sub-childs) and the
	 *         parent itself.
	 */
	private List<I18nNode> flatten(final I18nNode parent, final List<I18nNode> flattened)
	{
		boolean parentAdded = false;

		if ((parent.childs == null || parent.childs.isEmpty())) {
			flattened.add(parent);
			parentAdded = true;
		}

		for (I18nNode c : parent.childs) {
			flatten(c, flattened);
		}

		if (parentAdded == false) {
			flattened.add(parent);
		}

		return flattened;
	}

	void update(final Language language, final String value)
	{
		this.value.put(language, value);
	}

	/**
	 * use this method to set a value for all existing languages. existing means: all languages that
	 * are defined for this node.
	 * 
	 * @param value
	 *            the value to be set
	 */
	void updateAll(final String value)
	{
		for (final Language iso2 : this.value.keySet()) {
			this.value.put(iso2, value);
		}
	}

	@SuppressWarnings("unchecked")
	public List<I18nNode> getChilds()
	{
		return UnmodifiableList.decorate(childs);
	}

	public String value(Language language)
	{
		if (value.containsKey(language) == false)
			return "";

		return value.get(language);
	}

	public I18nNode getParent()
	{
		return parent;
	}

	public String getI18nKeyPart()
	{
		return i18nKeyPart;
	}
}
