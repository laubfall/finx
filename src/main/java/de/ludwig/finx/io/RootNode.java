package de.ludwig.finx.io;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.set.UnmodifiableSet;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import de.ludwig.finx.Language;
import de.ludwig.finx.settings.AppSettings;

/**
 * Highlevel Class. Stellt Zugriff auf alle I18nNodes zur Verfügung die auf Basis einer
 * Properties-Datei erzeugt wurden.
 * 
 * @author Daniel
 */
public class RootNode
{
	private Logger log = Logger.getLogger(RootNode.class);

	/**
	 * <code>
	 * de.ludwig.blah=jklfd 
	 * page.title=ckjkdf
	 * </code>
	 * 
	 * dann wären hier zwei I18nNodes mit den keyParts de und page drin
	 */
	private List<I18nNode> rootNodes = new ArrayList<>();

	/**
	 * All iso2 codes of that languages we have some translations also called existing languages
	 */
	private final Set<Language> existingLanguages = new HashSet<>();

	/**
	 * 
	 * @param key
	 *            Immer der absolute Schlüssel! Relative Schlüssel gibt es hier nicht! Beispiel
	 *            key1: de.ludwig nun möchte man unter de.ludwig ein Node mit Key node anlegen. Dann
	 *            muss man als key de.ludwig.node übergeben!
	 * @param language
	 * @param value
	 */
	public void addNode(final String key, final Language language, final String value)
	{
		existingLanguages.add(language);

		if (rootNodes.isEmpty()) {
			rootNodes.add(I18nNode.create(key, language, value));
			log.debug(String.format("added node with key %s for language %s with value %s to empty root", key,
					language, value));
			return;
		}

		final List<I18nNode> tmp = new ArrayList<I18nNode>(rootNodes);
		for (I18nNode root : rootNodes) {
			final I18nNode mostMatchingRootPath = I18nNode.mostMatching(key, root);
			if (mostMatchingRootPath == null) {
				tmp.add(I18nNode.create(key, language, value));
				log.debug(String.format("added node with key %s for language %s with value %s to root", key, language,
						value));
				continue;
			} else if (mostMatchingRootPath.key().equals(key)) {
				mostMatchingRootPath.update(language, value);
				log.debug(String.format("updated node with key %s for language %s with value %s", key, language, value));
				return;
			} else {
				final String mostMatchingKey = mostMatchingRootPath.key();
				String keyPart = StringUtils.substringAfter(key, mostMatchingKey);
				keyPart = StringUtils.removeStart(keyPart, "."); // key()
																	// liefert
																	// immer den
																	// Pfad ohne
																	// . am Ende
				I18nNode.attach(keyPart, mostMatchingRootPath, value, language);
				log.debug(String.format("added node with key %s for language %s with value %s to child with key %s",
						key, language, value, mostMatchingKey));
				return;
			}
		}

		rootNodes.clear();
		rootNodes.addAll(tmp);
	}

	/**
	 * Adds a node for all existing languages under the specified key. Does nothing if an
	 * {@link I18nNode} with the given key already exists.
	 * 
	 * If there are no existing languages no key is added to the structure.
	 * 
	 * @param key
	 * @param value
	 */
	public void addAll(final String key, String value)
	{
		for (Language language : existingLanguages) {
			addNode(key, language, value);
		}
	}

	/**
	 * Adds a node for all existing languages under the specified key. If there are no existing
	 * languages no key is added to the structure.
	 * 
	 * The node value is the value defined by Setting
	 * {@link AppSettings#APP_SETTING_I18N_DEFAULT_VALUE}
	 * 
	 * @param key
	 */
	public void addAll(final String key)
	{
		final Map<String, String> value = AppSettings.i18nDefaultValue.setting().value(this, key);
		for (Language language : existingLanguages) {
			addNode(key, language, value.get(language));
		}
	}

	public void update(String value, String key, Language language)
	{
		findNode(key).update(language, value);
	}

	public void updateAll(String value, String key)
	{
		findNode(key).updateAll(value);
	}

	/**
	 * search for a node by a given key. the key has to match exactly, otherwise no {@link I18nNode}
	 * is returned.
	 * 
	 * @param key
	 *            the key of the node that we wanna find
	 * @return {@link I18nNode} or null if no {@link I18nNode} with the given key exists
	 */
	public I18nNode findNode(final String key)
	{
		for (I18nNode n : rootNodes) {
			I18nNode mostMatching = I18nNode.mostMatching(key, n);
			if (mostMatching == null) {
				continue;
			} else if (mostMatching.key().equals(key)) {
				return mostMatching;
			}
		}
		return null;
	}

	/**
	 * Löscht ein Node aus dem Baum.
	 * 
	 * @param key
	 * @param root
	 *            Wenn null wird direkt in den Wurzel-Nodes (rootNodes) nach einer passenden Node
	 *            geschaut. Andernfalls wird von der übermittelten Node aus gesucht bzw. gelöscht.
	 */
	public void removeNode(final String key, final I18nNode root)
	{
	}

	public List<I18nNode> flatten()
	{
		final List<I18nNode> result = new ArrayList<I18nNode>();
		for (I18nNode node : getRootNodes()) {
			result.addAll(node.flatten());
		}
		return result;
	}

	public List<I18nNode> getRootNodes()
	{
		return rootNodes;
	}

	/**
	 * @return the definedLanguages
	 */
	@SuppressWarnings("unchecked")
	public Set<Language> getExistingLanguages()
	{
		return UnmodifiableSet.decorate(existingLanguages);
	}
}
