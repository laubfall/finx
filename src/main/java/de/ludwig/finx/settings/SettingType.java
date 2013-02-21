package de.ludwig.finx.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO im Prinzip überflüssig...
 * 
 * For some simple types the Settings-Mechanism knows the corresponding {@link AbstractSetting} Type.
 * If there is no {@link AbstractSetting} registered to a given Setting-Value-Type the Settings-Mechanism
 * try to retrieve the Setting-Type by this annotation for instantiaton.
 * 
 * @author Daniel
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface SettingType
{
	Class<? extends AbstractSetting<?>> value();
}
