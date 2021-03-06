package de.ludwig.finx.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import de.ludwig.finx.ApplicationCodingException;
import de.ludwig.finx.ApplicationException;

/**
 * 
 * @author Daniel
 */
public class SettingsDaoImpl
{
	/**
	 * Filename of the file which contains the default Settings for the application
	 */
	public static final String SETTINGS_FILENAME = "settings.i18n";

	public static final String USER_SETTINGS_FILENAME = "usettings.i18n";

	private static SettingsDaoImpl settingsDao;

	private static final Logger LOG = Logger.getLogger(SettingsDaoImpl.class);

	/**
	 * Setting Value Type to Setting Mapping. In this map application finds the Value Type that is
	 * returned by a specific Setting.
	 */
	private Map<Class<?>, Class<? extends AbstractSetting<?>>> settingRegistry = new HashMap<>();

	/**
	 * key: name of the settings-field. value: class where the setting is present
	 */
	private Map<String, Class<?>> settingNameRegistry = new HashMap<>();

	private Map<String, Class<?>> userStorableFields = new HashMap<>();

	private Map<Class<?>, List<WeakReference<SettingsChangedListener>>> settingsChangedRegistry = new HashMap<>();

	private ReferenceQueue<SettingsChangedListener> refsToRemove = new ReferenceQueue<>();

	private final Properties staticApplicationProperties = new Properties();

	private Properties userApplicationProperties = new Properties(staticApplicationProperties);

	private SettingsDaoImpl()
	{
		register();

		loadUserApplicationSettings();

		try {
			final File settings = new File(getClass().getResource("/" + SETTINGS_FILENAME).toURI());
			staticApplicationProperties.load(new FileInputStream(settings));
		} catch (FileNotFoundException e1) {
			throw new ApplicationCodingException("Setting-properties-file not found", e1);
		} catch (IOException e1) {
			throw new ApplicationCodingException("IOExcption during setting-properties read", e1);
		} catch (URISyntaxException e) {
			throw new ApplicationCodingException("URI Syntax for setting-properties-file not valid", e);
		}
	}

	public static SettingsDaoImpl instance()
	{
		if (settingsDao == null) {
			settingsDao = new SettingsDaoImpl();
			return settingsDao;
		}

		return settingsDao;
	}

	/**
	 * Stores all Settings in a system specific settings file.
	 * 
	 * Reloads the settings after a successfull save operation to secure that changes are visible
	 * for programm code.
	 * 
	 * @param settings
	 *            Not optional. {@link AppSettings#APP_SETTING_OWNSETTINGS_LOCATION} has to be set.
	 */
	public void saveSettings()
	{
		final Set<String> fieldNames = userStorableFields.keySet();
		for (final String fN : fieldNames) {
			final Class<?> settingholder = userStorableFields.get(fN);
			final Set<Field> settingFields = settingFields(settingholder);
			Field fieldToStore = (Field) CollectionUtils.find(settingFields, new Predicate() {
				@Override
				public boolean evaluate(Object object)
				{
					Field f = (Field) object;
					return f.getName().equals(fN);
				}
			});

			if (fieldToStore == null) { // should never happen
				throw new ApplicationCodingException("user storable field not found! " + fN);
			}

			try {
				final UpdatableSetting<?> setting = (UpdatableSetting<?>) fieldToStore.get(null);
				UserStorable storable = (UserStorable) setting.setting();
				final String storeValue = storable.storeValue();
				userApplicationProperties.put(fN, storeValue);
				LOG.debug(String.format("Stored value %s for field %s", storeValue, fN));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				LOG.error("exception while retrieving user storable " + fN);
				throw new ApplicationCodingException("unable to store user-settings");
			}
		}

		try {
			userApplicationProperties.store(new FileOutputStream(userSettingsFile()), null);
		} catch (IOException e) {
			LOG.error("exception while storing user-settings", e);
			throw new ApplicationCodingException("unable to store user-settings due to IOException");
		}
	}

	/**
	 * Initialize all static Fields of given Class that are type of {@link Setting}
	 * 
	 * @param settingHolderType
	 */
	public void init(Class<?> settingHolderType)
	{
		for (Field f : settingFields(settingHolderType)) {
			initSettingField(f, userApplicationProperties.getProperty(f.getName()));
			settingNameRegistry.put(f.getName(), settingHolderType);

			Class<? extends AbstractSetting<?>> settingClass = settingClass(f);
			if (UserStorable.class.isAssignableFrom(settingClass)) {
				userStorableFields.put(f.getName(), settingHolderType);
			}
		}
	}

	/**
	 * 
	 * @param settingHolderType
	 *            The class where the settings are present.
	 * @param listener
	 *            the listener hold as a weak-reference.
	 */
	public final void addListener(final Class<?> settingHolderType, final SettingsChangedListener listener)
	{
		if (settingNameRegistry.containsValue(settingHolderType) == false) {
			// otherwise we do not find any listeners if there is a change on a settings-field
			throw new ApplicationCodingException(String.format("class %s is not registered as a setting-holder",
					settingHolderType.getName()));
		}

		if (settingsChangedRegistry.containsKey(settingHolderType) == false) {
			settingsChangedRegistry.put(settingHolderType, new ArrayList<WeakReference<SettingsChangedListener>>());
		}

		settingsChangedRegistry.get(settingHolderType).add(
				new WeakReference<SettingsChangedListener>(listener, refsToRemove));
		LOG.debug(String.format("added %s as settings-changed-listener", listener.getClass()));

		while (refsToRemove.poll() != null)
			;
	}

	/**
	 * does some default registry entries.
	 */
	private void register()
	{
		settingRegistry.put(String.class, StringSetting.class);
		settingRegistry.put(Integer.class, IntegerSetting.class);
		settingRegistry.put(List.class, ListSetting.class);
		settingRegistry.put(File.class, FileSetting.class);
	}

	private void loadUserApplicationSettings()
	{
		final File settings = userSettingsFile();

		try {
			staticApplicationProperties.load(new FileInputStream(settings));
		} catch (IOException e) {
			LOG.error("exception while loading user-settings", e);
			throw new ApplicationException("unable to load user-settings-file from: " + settings.getPath());
		}
	}

	/**
	 * @return
	 */
	private File userSettingsFile()
	{
		// we assume that the user do not start the application every time from a different
		// working-dir.
		// if so, the application finds the user-settings Property-File.
		final String appDir = System.getProperty("user.dir");
		final File settings = new File(appDir, USER_SETTINGS_FILENAME);
		if (settings.exists() == false) {
			try {
				settings.createNewFile();
			} catch (IOException e) {
				LOG.error("exception while create user-settings-file", e);
				throw new ApplicationException("unable to store user-settings-file in: " + settings.getPath());
			}
		}
		return settings;
	}

	private Set<Field> settingFields(final Class<?> settingHolder)
	{
		final Set<Field> settingFields = new HashSet<Field>();
		Field[] declaredFields = settingHolder.getDeclaredFields();
		for (Field f : declaredFields) {
			final Class<?> fieldType = f.getType();
			if (Setting.class.isAssignableFrom(fieldType)) {
				settingFields.add(f);
			}
		}
		return settingFields;
	}

	private Class<? extends AbstractSetting<?>> settingClass(final Field settingField)
	{
		Type gt = settingField.getType();
		if (gt.equals(UpdatableSetting.class) == false) {
			// otherwise we not able to assign the proxy to this field
			throw new ApplicationCodingException(String.format("setting field %s in class %s has to be of type %s",
					settingField.getName(), settingField.getDeclaringClass().getName(),
					UpdatableSetting.class.getName()));
		}
		final ParameterizedType genericType = (ParameterizedType) settingField.getGenericType();
		final Type settingTypeValue = genericType.getActualTypeArguments()[0];
		final Class<?> settingTypeValueClass = (Class<?>) settingTypeValue;

		Class<? extends AbstractSetting<?>> settingClass = settingRegistry.get(settingTypeValueClass);
		if (settingClass == null) {
			final SettingType settingTypeAnno = settingTypeValueClass.getAnnotation(SettingType.class);
			if (settingTypeAnno == null) {
				throw new ApplicationCodingException(
						"did not found a settingtype definition, use SettingType Annotation");
			}

			settingClass = settingTypeAnno.value();
		}
		return settingClass;
	}

	/**
	 * Initializes a static field of Type {@link AbstractSetting} with the given value by the
	 * settings-properties-file.
	 * 
	 * @param settingField
	 * @param initialValue
	 * 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private void initSettingField(final Field settingField, String initialValue)
	{
		try {
			final AbstractSetting<?> setting = settingClass(settingField).newInstance();
			setting.initialize(initialValue);
			final Object newProxyInstance = Proxy.newProxyInstance(settingField.getType().getClassLoader(),
					new Class<?>[] { Modifieable.class, UpdatableSetting.class }, new InvocationHandler() {
						@Override
						public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable
						{
							final Class<?> decClass = arg1.getDeclaringClass();
							if (UpdatableSetting.class.isAssignableFrom(decClass)) {
								setting.setDirty(true);
								Class<?> fieldDecClass = settingField.getDeclaringClass();
								final List<WeakReference<SettingsChangedListener>> list = settingsChangedRegistry
										.get(fieldDecClass);
								if (list != null) {
									for (WeakReference<SettingsChangedListener> wr : list) {
										final SettingsChangedListener settingsChangedListener = wr.get();
										if (settingsChangedListener == null)
											continue;

										settingsChangedListener.settingChanged(settingField.getName(),
												(Setting<?>) arg0);
									}
								}
							}

							return arg1.invoke(setting, arg2);
						}
					});
			settingField.set(null, newProxyInstance);
		} catch (IllegalArgumentException e) {
			throw new ApplicationCodingException("IllegalArgumentException during setting update", e);
		} catch (IllegalAccessException e) {
			throw new ApplicationCodingException("IllegalAccessException during setting update", e);
		} catch (InstantiationException e) {
			throw new ApplicationCodingException("InstantiationException during setting update", e);
		}
	}

	/**
	 * Reload all Settings from the settings file. All unsaved Changes will be lost.
	 * 
	 * @return same as {@link #loadSettings()} but without unsaved changes.
	 */
	public AppSettings reloadSettings()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Changes a setting but does not save any changes
	 * 
	 * @param key
	 *            Not optional. Name of the Setting as defined in {@link AppSettings}
	 * @param rawValue
	 *            Optional. The Payload to initialize the setting with
	 */
	public void changeSetting(String settingName, String rawValue)
	{
		final Class<?> settingHolder = settingNameRegistry.get(settingName);
		for (final Field settingField : settingFields(settingHolder)) {
			if (settingField.getName().equals(settingName) == false) {
				continue;
			}

			initSettingField(settingField, rawValue);
		}
	}

	public Collection<String> namesOfKnownSettings()
	{
		Collection<String> result = new ArrayList<String>();
		for (Object o : staticApplicationProperties.keySet()) {
			result.add(o.toString());
		}
		return result;
	}

	// private class SettingTypeRegistryKey
	// {
	// public SettingTypeRegistryKey(final ParameterizedType pType)
	// {
	//
	// }
	// }
}