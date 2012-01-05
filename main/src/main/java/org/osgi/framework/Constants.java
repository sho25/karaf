begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright (c) OSGi Alliance (2000, 2011). All Rights Reserved.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|osgi
operator|.
name|framework
package|;
end_package

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|launch
operator|.
name|Framework
import|;
end_import

begin_comment
comment|/**  * Defines standard names for the OSGi environment system properties, service  * properties, and Manifest header attribute keys.  *   *<p>  * The values associated with these keys are of type {@code String}, unless  * otherwise indicated.  *   * @since 1.1  * @noimplement  * @version $Id: 517c954ed7d34d2ee762933466f69fa03db7cd37 $  */
end_comment

begin_interface
specifier|public
interface|interface
name|Constants
block|{
comment|/** 	 * Location identifier of the OSGi<i>system bundle</i>, which is defined 	 * to be&quot;System Bundle&quot;. 	 */
name|String
name|SYSTEM_BUNDLE_LOCATION
init|=
literal|"System Bundle"
decl_stmt|;
comment|/** 	 * Alias for the symbolic name of the OSGi<i>system bundle</i>. It is 	 * defined to be&quot;system.bundle&quot;. 	 *  	 * @since 1.3 	 */
name|String
name|SYSTEM_BUNDLE_SYMBOLICNAME
init|=
literal|"system.bundle"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's category. 	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_CATEGORY
init|=
literal|"Bundle-Category"
decl_stmt|;
comment|/** 	 * Manifest header identifying a list of directories and embedded JAR files, 	 * which are bundle resources used to extend the bundle's classpath. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_CLASSPATH
init|=
literal|"Bundle-ClassPath"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's copyright information. 	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_COPYRIGHT
init|=
literal|"Bundle-Copyright"
decl_stmt|;
comment|/** 	 * Manifest header containing a brief description of the bundle's 	 * functionality. 	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_DESCRIPTION
init|=
literal|"Bundle-Description"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's name. 	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_NAME
init|=
literal|"Bundle-Name"
decl_stmt|;
comment|/** 	 * Manifest header identifying a number of hardware environments and the 	 * native language code libraries that the bundle is carrying for each of 	 * these environments. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_NATIVECODE
init|=
literal|"Bundle-NativeCode"
decl_stmt|;
comment|/** 	 * Manifest header identifying the packages that the bundle offers to the 	 * Framework for export. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|EXPORT_PACKAGE
init|=
literal|"Export-Package"
decl_stmt|;
comment|/** 	 * Manifest header identifying the fully qualified class names of the 	 * services that the bundle may register (used for informational purposes 	 * only). 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @deprecated As of 1.2. 	 */
name|String
name|EXPORT_SERVICE
init|=
literal|"Export-Service"
decl_stmt|;
comment|/** 	 * Manifest header identifying the packages on which the bundle depends. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|IMPORT_PACKAGE
init|=
literal|"Import-Package"
decl_stmt|;
comment|/** 	 * Manifest header identifying the packages that the bundle may dynamically 	 * import during execution. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.2 	 */
name|String
name|DYNAMICIMPORT_PACKAGE
init|=
literal|"DynamicImport-Package"
decl_stmt|;
comment|/** 	 * Manifest header identifying the fully qualified class names of the 	 * services that the bundle requires (used for informational purposes only). 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @deprecated As of 1.2. 	 */
name|String
name|IMPORT_SERVICE
init|=
literal|"Import-Service"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's vendor. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_VENDOR
init|=
literal|"Bundle-Vendor"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's version. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_VERSION
init|=
literal|"Bundle-Version"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's documentation URL, from which 	 * further information about the bundle may be obtained. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_DOCURL
init|=
literal|"Bundle-DocURL"
decl_stmt|;
comment|/** 	 * Manifest header identifying the contact address where problems with the 	 * bundle may be reported; for example, an email address. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_CONTACTADDRESS
init|=
literal|"Bundle-ContactAddress"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the bundle's activator class. 	 *  	 *<p> 	 * If present, this header specifies the name of the bundle resource class 	 * that implements the {@code BundleActivator} interface and whose 	 * {@code start} and {@code stop} methods are called by the Framework when 	 * the bundle is started and stopped, respectively. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_ACTIVATOR
init|=
literal|"Bundle-Activator"
decl_stmt|;
comment|/** 	 * Manifest header identifying the location from which a new bundle version 	 * is obtained during a bundle update operation. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 */
name|String
name|BUNDLE_UPDATELOCATION
init|=
literal|"Bundle-UpdateLocation"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the version of a package specified 	 * in the Export-Package or Import-Package manifest header. 	 *  	 * @deprecated As of 1.3. This has been replaced by 	 *             {@link #VERSION_ATTRIBUTE}. 	 */
name|String
name|PACKAGE_SPECIFICATION_VERSION
init|=
literal|"specification-version"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the processor required to run 	 * native bundle code specified in the Bundle-NativeCode manifest header). 	 *  	 *<p> 	 * The attribute value is encoded in the Bundle-NativeCode manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-NativeCode: http.so ; processor=x86 ... 	 *</pre> 	 *  	 * @see #BUNDLE_NATIVECODE 	 */
name|String
name|BUNDLE_NATIVECODE_PROCESSOR
init|=
literal|"processor"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the operating system required to 	 * run native bundle code specified in the Bundle-NativeCode manifest 	 * header). 	 *<p> 	 * The attribute value is encoded in the Bundle-NativeCode manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-NativeCode: http.so ; osname=Linux ... 	 *</pre> 	 *  	 * @see #BUNDLE_NATIVECODE 	 */
name|String
name|BUNDLE_NATIVECODE_OSNAME
init|=
literal|"osname"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the operating system version 	 * required to run native bundle code specified in the Bundle-NativeCode 	 * manifest header). 	 *<p> 	 * The attribute value is encoded in the Bundle-NativeCode manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-NativeCode: http.so ; osversion=&quot;2.34&quot; ... 	 *</pre> 	 *  	 * @see #BUNDLE_NATIVECODE 	 */
name|String
name|BUNDLE_NATIVECODE_OSVERSION
init|=
literal|"osversion"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the language in which the native 	 * bundle code is written specified in the Bundle-NativeCode manifest 	 * header. See ISO 639 for possible values. 	 *<p> 	 * The attribute value is encoded in the Bundle-NativeCode manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-NativeCode: http.so ; language=nl_be ... 	 *</pre> 	 *  	 * @see #BUNDLE_NATIVECODE 	 */
name|String
name|BUNDLE_NATIVECODE_LANGUAGE
init|=
literal|"language"
decl_stmt|;
comment|/** 	 * Manifest header identifying the required execution environment for the 	 * bundle. The service platform may run this bundle if any of the execution 	 * environments named in this header matches one of the execution 	 * environments it implements. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.2 	 * @deprecated As of 1.6. Replaced by the {@code osgi.ee} capability. 	 */
name|String
name|BUNDLE_REQUIREDEXECUTIONENVIRONMENT
init|=
literal|"Bundle-RequiredExecutionEnvironment"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's symbolic name. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.3 	 */
name|String
name|BUNDLE_SYMBOLICNAME
init|=
literal|"Bundle-SymbolicName"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying whether a bundle is a singleton. 	 * The default value is {@code false}. 	 *  	 *<p> 	 * The directive value is encoded in the Bundle-SymbolicName manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-SymbolicName: com.acme.module.test; singleton:=true 	 *</pre> 	 *  	 * @see #BUNDLE_SYMBOLICNAME 	 * @since 1.3 	 */
name|String
name|SINGLETON_DIRECTIVE
init|=
literal|"singleton"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying if and when a fragment may attach 	 * to a host bundle. The default value is 	 * {@link #FRAGMENT_ATTACHMENT_ALWAYS always}. 	 *  	 *<p> 	 * The directive value is encoded in the Bundle-SymbolicName manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-SymbolicName: com.acme.module.test; fragment-attachment:=&quot;never&quot; 	 *</pre> 	 *  	 * @see #BUNDLE_SYMBOLICNAME 	 * @see #FRAGMENT_ATTACHMENT_ALWAYS 	 * @see #FRAGMENT_ATTACHMENT_RESOLVETIME 	 * @see #FRAGMENT_ATTACHMENT_NEVER 	 * @since 1.3 	 */
name|String
name|FRAGMENT_ATTACHMENT_DIRECTIVE
init|=
literal|"fragment-attachment"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a fragment attachment type of 	 * always. A fragment attachment type of always indicates that fragments are 	 * allowed to attach to the host bundle at any time (while the host is 	 * resolved or during the process of resolving the host bundle). 	 *  	 *<p> 	 * The directive value is encoded in the Bundle-SymbolicName manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-SymbolicName: com.acme.module.test; fragment-attachment:=&quot;always&quot; 	 *</pre> 	 *  	 * @see #FRAGMENT_ATTACHMENT_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|FRAGMENT_ATTACHMENT_ALWAYS
init|=
literal|"always"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a fragment attachment type of 	 * resolve-time. A fragment attachment type of resolve-time indicates that 	 * fragments are allowed to attach to the host bundle only during the 	 * process of resolving the host bundle. 	 *  	 *<p> 	 * The directive value is encoded in the Bundle-SymbolicName manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-SymbolicName: com.acme.module.test; fragment-attachment:=&quot;resolve-time&quot; 	 *</pre> 	 *  	 * @see #FRAGMENT_ATTACHMENT_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|FRAGMENT_ATTACHMENT_RESOLVETIME
init|=
literal|"resolve-time"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a fragment attachment type of 	 * never. A fragment attachment type of never indicates that no fragments 	 * are allowed to attach to the host bundle at any time. 	 *  	 *<p> 	 * The directive value is encoded in the Bundle-SymbolicName manifest header 	 * like: 	 *  	 *<pre> 	 *     Bundle-SymbolicName: com.acme.module.test; fragment-attachment:=&quot;never&quot; 	 *</pre> 	 *  	 * @see #FRAGMENT_ATTACHMENT_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|FRAGMENT_ATTACHMENT_NEVER
init|=
literal|"never"
decl_stmt|;
comment|/** 	 * Manifest header identifying the base name of the bundle's localization 	 * entries. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @see #BUNDLE_LOCALIZATION_DEFAULT_BASENAME 	 * @since 1.3 	 */
name|String
name|BUNDLE_LOCALIZATION
init|=
literal|"Bundle-Localization"
decl_stmt|;
comment|/** 	 * Default value for the {@code Bundle-Localization} manifest header. 	 *  	 * @see #BUNDLE_LOCALIZATION 	 * @since 1.3 	 */
name|String
name|BUNDLE_LOCALIZATION_DEFAULT_BASENAME
init|=
literal|"OSGI-INF/l10n/bundle"
decl_stmt|;
comment|/** 	 * Manifest header identifying the symbolic names of other bundles required 	 * by the bundle. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.3 	 */
name|String
name|REQUIRE_BUNDLE
init|=
literal|"Require-Bundle"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying a range of versions for a bundle 	 * specified in the {@code Require-Bundle} or {@code Fragment-Host} manifest 	 * headers. The default value is {@code 0.0.0}. 	 *  	 *<p> 	 * The attribute value is encoded in the Require-Bundle manifest header 	 * like: 	 *  	 *<pre> 	 *     Require-Bundle: com.acme.module.test; bundle-version=&quot;1.1&quot; 	 *     Require-Bundle: com.acme.module.test; bundle-version=&quot;[1.0,2.0)&quot; 	 *</pre> 	 *  	 *<p> 	 * The bundle-version attribute value uses a mathematical interval notation 	 * to specify a range of bundle versions. A bundle-version attribute value 	 * specified as a single version means a version range that includes any 	 * bundle version greater than or equal to the specified version. 	 *  	 * @see #REQUIRE_BUNDLE 	 * @since 1.3 	 */
name|String
name|BUNDLE_VERSION_ATTRIBUTE
init|=
literal|"bundle-version"
decl_stmt|;
comment|/** 	 * Manifest header identifying the symbolic name of another bundle for which 	 * that the bundle is a fragment. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.3 	 */
name|String
name|FRAGMENT_HOST
init|=
literal|"Fragment-Host"
decl_stmt|;
comment|/** 	 * Manifest header attribute is used for selection by filtering based upon 	 * system properties. 	 *  	 *<p> 	 * The attribute value is encoded in manifest headers like: 	 *  	 *<pre> 	 *     Bundle-NativeCode: libgtk.so; selection-filter=&quot;(ws=gtk)&quot;; ... 	 *</pre> 	 *  	 * @see #BUNDLE_NATIVECODE 	 * @since 1.3 	 */
name|String
name|SELECTION_FILTER_ATTRIBUTE
init|=
literal|"selection-filter"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle manifest version. A bundle 	 * manifest may express the version of the syntax in which it is written by 	 * specifying a bundle manifest version. Bundles exploiting OSGi Release 4, 	 * or later, syntax must specify a bundle manifest version. 	 *<p> 	 * The bundle manifest version defined by OSGi Release 4 or, more 	 * specifically, by version 1.3 of the OSGi Core Specification is "2". 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.3 	 */
name|String
name|BUNDLE_MANIFESTVERSION
init|=
literal|"Bundle-ManifestVersion"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the version of a package specified 	 * in the Export-Package or Import-Package manifest header. 	 *  	 *<p> 	 * The attribute value is encoded in the Export-Package or Import-Package 	 * manifest header like: 	 *  	 *<pre> 	 *     Export-Package: org.osgi.framework; version=&quot;1.1&quot; 	 *</pre> 	 *  	 * @see #EXPORT_PACKAGE 	 * @see #IMPORT_PACKAGE 	 * @since 1.3 	 */
name|String
name|VERSION_ATTRIBUTE
init|=
literal|"version"
decl_stmt|;
comment|/** 	 * Manifest header attribute identifying the symbolic name of a bundle that 	 * exports a package specified in the Import-Package manifest header. 	 *  	 *<p> 	 * The attribute value is encoded in the Import-Package manifest header 	 * like: 	 *  	 *<pre> 	 *     Import-Package: org.osgi.framework; bundle-symbolic-name=&quot;com.acme.module.test&quot; 	 *</pre> 	 *  	 * @see #IMPORT_PACKAGE 	 * @since 1.3 	 */
name|String
name|BUNDLE_SYMBOLICNAME_ATTRIBUTE
init|=
literal|"bundle-symbolic-name"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying the resolution type in the 	 * Import-Package, Require-Bundle or Require-Capability manifest header. The 	 * default value is {@link #RESOLUTION_MANDATORY mandatory}. 	 *  	 *<p> 	 * The directive value is encoded in the Import-Package, Require-Bundle or 	 * Require-Capability manifest header like: 	 *  	 *<pre> 	 *     Import-Package: org.osgi.framework; resolution:=&quot;optional&quot; 	 *     Require-Bundle: com.acme.module.test; resolution:=&quot;optional&quot; 	 *     Require-Capability: com.acme.capability; resolution:=&quot;optional&quot; 	 *</pre> 	 *  	 * @see #IMPORT_PACKAGE 	 * @see #REQUIRE_BUNDLE 	 * @see #REQUIRE_CAPABILITY 	 * @see #RESOLUTION_MANDATORY 	 * @see #RESOLUTION_OPTIONAL 	 * @since 1.3 	 */
name|String
name|RESOLUTION_DIRECTIVE
init|=
literal|"resolution"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a mandatory resolution type. 	 * A mandatory resolution type indicates that the import package, require 	 * bundle or require capability must be resolved when the bundle is 	 * resolved. If such an import, require bundle or require capability cannot 	 * be resolved, the module fails to resolve. 	 *  	 *<p> 	 * The directive value is encoded in the Import-Package, Require-Bundle or 	 * Require-Capability manifest header like: 	 *  	 *<pre> 	 *     Import-Package: org.osgi.framework; resolution:=&quot;mandatory&quot; 	 *     Require-Bundle: com.acme.module.test; resolution:=&quot;mandatory&quot; 	 *     Require-Capability: com.acme.capability; resolution:=&quot;mandatory&quot; 	 *</pre> 	 *  	 * @see #RESOLUTION_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|RESOLUTION_MANDATORY
init|=
literal|"mandatory"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying an optional resolution type. 	 * An optional resolution type indicates that the import, require bundle or 	 * require capability is optional and the bundle may be resolved without the 	 * import, require bundle or require capability being resolved. If the 	 * import, require bundle or require capability is not resolved when the 	 * bundle is resolved, the import, require bundle or require capability may 	 * not be resolved until the bundle is refreshed. 	 *  	 *<p> 	 * The directive value is encoded in the Import-Package, Require-Bundle or 	 * Require-Capability manifest header like: 	 *  	 *<pre> 	 *     Import-Package: org.osgi.framework; resolution:=&quot;optional&quot; 	 *     Require-Bundle: com.acme.module.test; resolution:=&quot;optional&quot; 	 *     Require-Capability: com.acme.capability; resolution:=&quot;optional&quot; 	 *</pre> 	 *  	 * @see #RESOLUTION_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|RESOLUTION_OPTIONAL
init|=
literal|"optional"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying a list of packages that an exported 	 * package or provided capability uses. 	 *  	 *<p> 	 * The directive value is encoded in the Export-Package or 	 * Provide-Capability manifest header like: 	 *  	 *<pre> 	 *     Export-Package: org.osgi.util.tracker; uses:=&quot;org.osgi.framework&quot; 	 *     Provide-Capability: com.acme.capability; uses:=&quot;com.acme.service&quot; 	 *</pre> 	 *  	 * @see #EXPORT_PACKAGE 	 * @see #PROVIDE_CAPABILITY 	 * @since 1.3 	 */
name|String
name|USES_DIRECTIVE
init|=
literal|"uses"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying a list of classes to include in the 	 * exported package. 	 *  	 *<p> 	 * This directive is used by the Export-Package manifest header to identify 	 * a list of classes of the specified package which must be allowed to be 	 * exported. The directive value is encoded in the Export-Package manifest 	 * header like: 	 *  	 *<pre> 	 *     Export-Package: org.osgi.framework; include:=&quot;MyClass*&quot; 	 *</pre> 	 *  	 *<p> 	 * This directive is also used by the Bundle-ActivationPolicy manifest 	 * header to identify the packages from which class loads will trigger lazy 	 * activation. The directive value is encoded in the Bundle-ActivationPolicy 	 * manifest header like: 	 *  	 *<pre> 	 *     Bundle-ActivationPolicy: lazy; include:=&quot;org.osgi.framework&quot; 	 *</pre> 	 *  	 * @see #EXPORT_PACKAGE 	 * @see #BUNDLE_ACTIVATIONPOLICY 	 * @since 1.3 	 */
name|String
name|INCLUDE_DIRECTIVE
init|=
literal|"include"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying a list of classes to exclude in the 	 * exported package.. 	 *<p> 	 * This directive is used by the Export-Package manifest header to identify 	 * a list of classes of the specified package which must not be allowed to 	 * be exported. The directive value is encoded in the Export-Package 	 * manifest header like: 	 *  	 *<pre> 	 *     Export-Package: org.osgi.framework; exclude:=&quot;*Impl&quot; 	 *</pre> 	 *  	 *<p> 	 * This directive is also used by the Bundle-ActivationPolicy manifest 	 * header to identify the packages from which class loads will not trigger 	 * lazy activation. The directive value is encoded in the 	 * Bundle-ActivationPolicy manifest header like: 	 *  	 *<pre> 	 *     Bundle-ActivationPolicy: lazy; exclude:=&quot;org.osgi.framework&quot; 	 *</pre> 	 *  	 * @see #EXPORT_PACKAGE 	 * @see #BUNDLE_ACTIVATIONPOLICY 	 * @since 1.3 	 */
name|String
name|EXCLUDE_DIRECTIVE
init|=
literal|"exclude"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying names of matching attributes which 	 * must be specified by matching Import-Package statements in the 	 * Export-Package manifest header. 	 *  	 *<p> 	 * The directive value is encoded in the Export-Package manifest header 	 * like: 	 *  	 *<pre> 	 *     Export-Package: org.osgi.framework; mandatory:=&quot;bundle-symbolic-name&quot; 	 *</pre> 	 *  	 * @see #EXPORT_PACKAGE 	 * @since 1.3 	 */
name|String
name|MANDATORY_DIRECTIVE
init|=
literal|"mandatory"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying the visibility of a required bundle 	 * in the Require-Bundle manifest header. The default value is 	 * {@link #VISIBILITY_PRIVATE private}. 	 *  	 *<p> 	 * The directive value is encoded in the Require-Bundle manifest header 	 * like: 	 *  	 *<pre> 	 *     Require-Bundle: com.acme.module.test; visibility:=&quot;reexport&quot; 	 *</pre> 	 *  	 * @see #REQUIRE_BUNDLE 	 * @see #VISIBILITY_PRIVATE 	 * @see #VISIBILITY_REEXPORT 	 * @since 1.3 	 */
name|String
name|VISIBILITY_DIRECTIVE
init|=
literal|"visibility"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a private visibility type. A 	 * private visibility type indicates that any packages that are exported by 	 * the required bundle are not made visible on the export signature of the 	 * requiring bundle. 	 *  	 *<p> 	 * The directive value is encoded in the Require-Bundle manifest header 	 * like: 	 *  	 *<pre> 	 *     Require-Bundle: com.acme.module.test; visibility:=&quot;private&quot; 	 *</pre> 	 *  	 * @see #VISIBILITY_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|VISIBILITY_PRIVATE
init|=
literal|"private"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a reexport visibility type. A 	 * reexport visibility type indicates any packages that are exported by the 	 * required bundle are re-exported by the requiring bundle. Any arbitrary 	 * arbitrary matching attributes with which they were exported by the 	 * required bundle are deleted. 	 *  	 *<p> 	 * The directive value is encoded in the Require-Bundle manifest header 	 * like: 	 *  	 *<pre> 	 *     Require-Bundle: com.acme.module.test; visibility:=&quot;reexport&quot; 	 *</pre> 	 *  	 * @see #VISIBILITY_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|VISIBILITY_REEXPORT
init|=
literal|"reexport"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying the type of the extension fragment. 	 *  	 *<p> 	 * The directive value is encoded in the Fragment-Host manifest header like: 	 *  	 *<pre> 	 *     Fragment-Host: system.bundle; extension:=&quot;framework&quot; 	 *</pre> 	 *  	 * @see #FRAGMENT_HOST 	 * @see #EXTENSION_FRAMEWORK 	 * @see #EXTENSION_BOOTCLASSPATH 	 * @since 1.3 	 */
name|String
name|EXTENSION_DIRECTIVE
init|=
literal|"extension"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying the type of extension 	 * fragment. An extension fragment type of framework indicates that the 	 * extension fragment is to be loaded by the framework's class loader. 	 *  	 *<p> 	 * The directive value is encoded in the Fragment-Host manifest header like: 	 *  	 *<pre> 	 *     Fragment-Host: system.bundle; extension:=&quot;framework&quot; 	 *</pre> 	 *  	 * @see #EXTENSION_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|EXTENSION_FRAMEWORK
init|=
literal|"framework"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying the type of extension 	 * fragment. An extension fragment type of bootclasspath indicates that the 	 * extension fragment is to be loaded by the boot class loader. 	 *  	 *<p> 	 * The directive value is encoded in the Fragment-Host manifest header like: 	 *  	 *<pre> 	 *     Fragment-Host: system.bundle; extension:=&quot;bootclasspath&quot; 	 *</pre> 	 *  	 * @see #EXTENSION_DIRECTIVE 	 * @since 1.3 	 */
name|String
name|EXTENSION_BOOTCLASSPATH
init|=
literal|"bootclasspath"
decl_stmt|;
comment|/** 	 * Manifest header identifying the bundle's activation policy. 	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.4 	 * @see #ACTIVATION_LAZY 	 * @see #INCLUDE_DIRECTIVE 	 * @see #EXCLUDE_DIRECTIVE 	 */
name|String
name|BUNDLE_ACTIVATIONPOLICY
init|=
literal|"Bundle-ActivationPolicy"
decl_stmt|;
comment|/** 	 * Bundle activation policy declaring the bundle must be activated when the 	 * first class load is made from the bundle. 	 *<p> 	 * A bundle with the lazy activation policy that is started with the 	 * {@link Bundle#START_ACTIVATION_POLICY START_ACTIVATION_POLICY} option 	 * will wait in the {@link Bundle#STARTING STARTING} state until the first 	 * class load from the bundle occurs. The bundle will then be activated 	 * before the class is returned to the requester. 	 *<p> 	 * The activation policy value is specified as in the 	 * Bundle-ActivationPolicy manifest header like: 	 *  	 *<pre> 	 *       Bundle-ActivationPolicy: lazy 	 *</pre> 	 *  	 * @see #BUNDLE_ACTIVATIONPOLICY 	 * @see Bundle#start(int) 	 * @see Bundle#START_ACTIVATION_POLICY 	 * @since 1.4 	 */
name|String
name|ACTIVATION_LAZY
init|=
literal|"lazy"
decl_stmt|;
comment|/** 	 * Framework environment property identifying the Framework version. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 */
name|String
name|FRAMEWORK_VERSION
init|=
literal|"org.osgi.framework.version"
decl_stmt|;
comment|/** 	 * Framework environment property identifying the Framework implementation 	 * vendor. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 */
name|String
name|FRAMEWORK_VENDOR
init|=
literal|"org.osgi.framework.vendor"
decl_stmt|;
comment|/** 	 * Framework launching property identifying the Framework implementation 	 * language (see ISO 639 for possible values). 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 */
name|String
name|FRAMEWORK_LANGUAGE
init|=
literal|"org.osgi.framework.language"
decl_stmt|;
comment|/** 	 * Framework launching property identifying the Framework host-computer's 	 * operating system. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 */
name|String
name|FRAMEWORK_OS_NAME
init|=
literal|"org.osgi.framework.os.name"
decl_stmt|;
comment|/** 	 * Framework launching property identifying the Framework host-computer's 	 * operating system version number. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 */
name|String
name|FRAMEWORK_OS_VERSION
init|=
literal|"org.osgi.framework.os.version"
decl_stmt|;
comment|/** 	 * Framework launching property identifying the Framework host-computer's 	 * processor name. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 */
name|String
name|FRAMEWORK_PROCESSOR
init|=
literal|"org.osgi.framework.processor"
decl_stmt|;
comment|/** 	 * Framework launching property identifying execution environments provided 	 * by the Framework. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.2 	 * @deprecated As of 1.6. Replaced by the {@code osgi.ee} capability. 	 */
name|String
name|FRAMEWORK_EXECUTIONENVIRONMENT
init|=
literal|"org.osgi.framework.executionenvironment"
decl_stmt|;
comment|/** 	 * Framework launching property identifying packages for which the Framework 	 * must delegate class loading to the parent class loader of the bundle. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @see #FRAMEWORK_BUNDLE_PARENT 	 * @since 1.3 	 */
name|String
name|FRAMEWORK_BOOTDELEGATION
init|=
literal|"org.osgi.framework.bootdelegation"
decl_stmt|;
comment|/** 	 * Framework launching property identifying packages which the system bundle 	 * must export. 	 *  	 *<p> 	 * If this property is not specified then the framework must calculate a 	 * reasonable default value for the current execution environment. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.3 	 */
name|String
name|FRAMEWORK_SYSTEMPACKAGES
init|=
literal|"org.osgi.framework.system.packages"
decl_stmt|;
comment|/** 	 * Framework launching property identifying extra packages which the system 	 * bundle must export from the current execution environment. 	 *  	 *<p> 	 * This property is useful for configuring extra system packages in addition 	 * to the system packages calculated by the framework. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @see #FRAMEWORK_SYSTEMPACKAGES 	 * @since 1.5 	 */
name|String
name|FRAMEWORK_SYSTEMPACKAGES_EXTRA
init|=
literal|"org.osgi.framework.system.packages.extra"
decl_stmt|;
comment|/** 	 * Framework environment property identifying whether the Framework supports 	 * framework extension bundles. 	 *  	 *<p> 	 * As of version 1.4, the value of this property must be {@code true}. The 	 * Framework must support framework extension bundles. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.3 	 */
name|String
name|SUPPORTS_FRAMEWORK_EXTENSION
init|=
literal|"org.osgi.supports.framework.extension"
decl_stmt|;
comment|/** 	 * Framework environment property identifying whether the Framework supports 	 * bootclasspath extension bundles. 	 *  	 *<p> 	 * If the value of this property is {@code true}, then the Framework 	 * supports bootclasspath extension bundles. The default value is 	 * {@code false}. 	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.3 	 */
name|String
name|SUPPORTS_BOOTCLASSPATH_EXTENSION
init|=
literal|"org.osgi.supports.bootclasspath.extension"
decl_stmt|;
comment|/** 	 * Framework environment property identifying whether the Framework supports 	 * fragment bundles. 	 *  	 *<p> 	 * As of version 1.4, the value of this property must be {@code true}. The 	 * Framework must support fragment bundles. 	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.3 	 */
name|String
name|SUPPORTS_FRAMEWORK_FRAGMENT
init|=
literal|"org.osgi.supports.framework.fragment"
decl_stmt|;
comment|/** 	 * Framework environment property identifying whether the Framework supports 	 * the {@link #REQUIRE_BUNDLE Require-Bundle} manifest header. 	 *  	 *<p> 	 * As of version 1.4, the value of this property must be {@code true}. The 	 * Framework must support the {@code Require-Bundle} manifest header. 	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.3 	 */
name|String
name|SUPPORTS_FRAMEWORK_REQUIREBUNDLE
init|=
literal|"org.osgi.supports.framework.requirebundle"
decl_stmt|;
comment|/** 	 * Framework launching property specifying the type of security manager the 	 * framework must use. If not specified then the framework will not set the 	 * VM security manager. 	 *  	 * @see #FRAMEWORK_SECURITY_OSGI 	 * @since 1.5 	 */
name|String
name|FRAMEWORK_SECURITY
init|=
literal|"org.osgi.framework.security"
decl_stmt|;
comment|/** 	 * Specifies that a security manager that supports all security aspects of 	 * the OSGi core specification including postponed conditions must be 	 * installed. 	 *  	 *<p> 	 * If this value is specified and there is a security manager already 	 * installed, then a {@code SecurityException} must be thrown when the 	 * Framework is initialized. 	 *  	 * @see #FRAMEWORK_SECURITY 	 * @since 1.5 	 */
name|String
name|FRAMEWORK_SECURITY_OSGI
init|=
literal|"osgi"
decl_stmt|;
comment|/** 	 * Framework launching property specifying the persistent storage area used 	 * by the framework. The value of this property must be a valid file path in 	 * the file system to a directory. If the specified directory does not exist 	 * then the framework will create the directory. If the specified path 	 * exists but is not a directory or if the framework fails to create the 	 * storage directory, then framework initialization must fail. The framework 	 * is free to use this directory as it sees fit. This area can not be shared 	 * with anything else. 	 *<p> 	 * If this property is not set, the framework should use a reasonable 	 * platform default for the persistent storage area. 	 *  	 * @since 1.5 	 */
name|String
name|FRAMEWORK_STORAGE
init|=
literal|"org.osgi.framework.storage"
decl_stmt|;
comment|/** 	 * Framework launching property specifying if and when the persistent 	 * storage area for the framework should be cleaned. If this property is not 	 * set, then the framework storage area must not be cleaned. 	 *  	 * @see #FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT 	 * @since 1.5 	 */
name|String
name|FRAMEWORK_STORAGE_CLEAN
init|=
literal|"org.osgi.framework.storage.clean"
decl_stmt|;
comment|/** 	 * Specifies that the framework storage area must be cleaned before the 	 * framework is initialized for the first time. Subsequent inits, starts or 	 * updates of the framework will not result in cleaning the framework 	 * storage area. 	 *  	 * @since 1.5 	 */
name|String
name|FRAMEWORK_STORAGE_CLEAN_ONFIRSTINIT
init|=
literal|"onFirstInit"
decl_stmt|;
comment|/** 	 * Framework launching property specifying a comma separated list of 	 * additional library file extensions that must be used when a bundle's 	 * class loader is searching for native libraries. If this property is not 	 * set, then only the library name returned by 	 * {@code System.mapLibraryName(String)} will be used to search. This is 	 * needed for certain operating systems which allow more than one extension 	 * for a library. For example, AIX allows library extensions of {@code .a} 	 * and {@code .so}, but {@code System.mapLibraryName(String)} will only 	 * return names with the {@code .a} extension. 	 *  	 * @since 1.5 	 */
name|String
name|FRAMEWORK_LIBRARY_EXTENSIONS
init|=
literal|"org.osgi.framework.library.extensions"
decl_stmt|;
comment|/** 	 * Framework launching property specifying an optional OS specific command 	 * to set file permissions on extracted native code. On some operating 	 * systems, it is required that native libraries be set to executable. This 	 * optional property allows you to specify the command. For example, on a 	 * UNIX style OS, this property could have the following value. 	 *  	 *<pre> 	 * chmod +rx ${abspath} 	 *</pre> 	 *  	 * The<code>${abspath}</code> is used by the framework to substitute the 	 * actual absolute file path. 	 *  	 * @since 1.5 	 */
name|String
name|FRAMEWORK_EXECPERMISSION
init|=
literal|"org.osgi.framework.command.execpermission"
decl_stmt|;
comment|/** 	 * Specified the substitution string for the absolute path of a file. 	 *  	 * @see #FRAMEWORK_EXECPERMISSION 	 * @since 1.6 	 */
name|String
name|FRAMEWORK_COMMAND_ABSPATH
init|=
literal|"abspath"
decl_stmt|;
comment|/** 	 * Framework launching property specifying the trust repositories used by 	 * the framework. The value is a {@code java.io.File.pathSeparator} 	 * separated list of valid file paths to files that contain key stores of 	 * type {@code JKS}. The framework will use the key stores as trust 	 * repositories to authenticate certificates of trusted signers. The key 	 * stores are only used as read-only trust repositories to access public 	 * keys. No passwords are required to access the key stores' public keys. 	 *<p> 	 * Note that framework implementations are allowed to use other trust 	 * repositories in addition to the trust repositories specified by this 	 * property. How these other trust repositories are configured and populated 	 * is implementation specific. 	 *  	 * @since 1.5 	 */
name|String
name|FRAMEWORK_TRUST_REPOSITORIES
init|=
literal|"org.osgi.framework.trust.repositories"
decl_stmt|;
comment|/** 	 * Framework launching property specifying the current windowing system. The 	 * framework should provide a reasonable default if this is not set. 	 *  	 * @since 1.5 	 */
name|String
name|FRAMEWORK_WINDOWSYSTEM
init|=
literal|"org.osgi.framework.windowsystem"
decl_stmt|;
comment|/** 	 * Framework launching property specifying the beginning start level of the 	 * framework. 	 *  	 * @see "Core Specification, Starting the Framework." 	 * @since 1.5 	 */
name|String
name|FRAMEWORK_BEGINNING_STARTLEVEL
init|=
literal|"org.osgi.framework.startlevel.beginning"
decl_stmt|;
comment|/** 	 * Framework launching property specifying the parent class loader type for 	 * all bundle class loaders. Default value is 	 * {@link #FRAMEWORK_BUNDLE_PARENT_BOOT boot}. 	 *  	 * @see #FRAMEWORK_BUNDLE_PARENT_BOOT 	 * @see #FRAMEWORK_BUNDLE_PARENT_EXT 	 * @see #FRAMEWORK_BUNDLE_PARENT_APP 	 * @see #FRAMEWORK_BUNDLE_PARENT_FRAMEWORK 	 * @since 1.5 	 */
name|String
name|FRAMEWORK_BUNDLE_PARENT
init|=
literal|"org.osgi.framework.bundle.parent"
decl_stmt|;
comment|/** 	 * Specifies to use of the boot class loader as the parent class loader for 	 * all bundle class loaders. 	 *  	 * @since 1.5 	 * @see #FRAMEWORK_BUNDLE_PARENT 	 */
name|String
name|FRAMEWORK_BUNDLE_PARENT_BOOT
init|=
literal|"boot"
decl_stmt|;
comment|/** 	 * Specifies to use the extension class loader as the parent class loader 	 * for all bundle class loaders. 	 *  	 * @since 1.5 	 * @see #FRAMEWORK_BUNDLE_PARENT 	 */
name|String
name|FRAMEWORK_BUNDLE_PARENT_EXT
init|=
literal|"ext"
decl_stmt|;
comment|/** 	 * Specifies to use the application class loader as the parent class loader 	 * for all bundle class loaders. Depending on how the framework is launched, 	 * this may refer to the same class loader as 	 * {@link #FRAMEWORK_BUNDLE_PARENT_FRAMEWORK}. 	 *  	 * @since 1.5 	 * @see #FRAMEWORK_BUNDLE_PARENT 	 */
name|String
name|FRAMEWORK_BUNDLE_PARENT_APP
init|=
literal|"app"
decl_stmt|;
comment|/** 	 * Specifies to use the framework class loader as the parent class loader 	 * for all bundle class loaders. The framework class loader is the class 	 * loader used to load the framework implementation. Depending on how the 	 * framework is launched, this may refer to the same class loader as 	 * {@link #FRAMEWORK_BUNDLE_PARENT_APP}. 	 *  	 * @since 1.5 	 * @see #FRAMEWORK_BUNDLE_PARENT 	 */
name|String
name|FRAMEWORK_BUNDLE_PARENT_FRAMEWORK
init|=
literal|"framework"
decl_stmt|;
comment|/* 	 * Service properties. 	 */
comment|/** 	 * Service property identifying all of the class names under which a service 	 * was registered in the Framework. The value of this property must be of 	 * type {@code String[]}. 	 *  	 *<p> 	 * This property is set by the Framework when a service is registered. 	 */
name|String
name|OBJECTCLASS
init|=
literal|"objectClass"
decl_stmt|;
comment|/** 	 * Service property identifying a service's registration number. The value 	 * of this property must be of type {@code Long}. 	 *  	 *<p> 	 * The value of this property is assigned by the Framework when a service is 	 * registered. The Framework assigns a unique value that is larger than all 	 * previously assigned values since the Framework was started. These values 	 * are NOT persistent across restarts of the Framework. 	 */
name|String
name|SERVICE_ID
init|=
literal|"service.id"
decl_stmt|;
comment|/** 	 * Service property identifying a service's persistent identifier. 	 *  	 *<p> 	 * This property may be supplied in the {@code properties} 	 * {@code Dictionary} object passed to the 	 * {@code BundleContext.registerService} method. The value of this property 	 * must be of type {@code String}, {@code String[]}, or {@code Collection} 	 * of {@code String}. 	 *  	 *<p> 	 * A service's persistent identifier uniquely identifies the service and 	 * persists across multiple Framework invocations. 	 *  	 *<p> 	 * By convention, every bundle has its own unique name space, starting with 	 * the bundle's identifier (see {@link Bundle#getBundleId}) and followed by 	 * a dot (.). A bundle may use this as the prefix of the persistent 	 * identifiers for the services it registers. 	 */
name|String
name|SERVICE_PID
init|=
literal|"service.pid"
decl_stmt|;
comment|/** 	 * Service property identifying a service's ranking number. 	 *  	 *<p> 	 * This property may be supplied in the {@code properties 	 * Dictionary} object passed to the {@code BundleContext.registerService} 	 * method. The value of this property must be of type {@code Integer}. 	 *  	 *<p> 	 * The service ranking is used by the Framework to determine the<i>natural 	 * order</i> of services, see {@link ServiceReference#compareTo}, and the 	 *<i>default</i> service to be returned from a call to the 	 * {@link BundleContext#getServiceReference} method. 	 *  	 *<p> 	 * The default ranking is zero (0). A service with a ranking of 	 * {@code Integer.MAX_VALUE} is very likely to be returned as the default 	 * service, whereas a service with a ranking of {@code Integer.MIN_VALUE} is 	 * very unlikely to be returned. 	 *  	 *<p> 	 * If the supplied property value is not of type {@code Integer}, it is 	 * deemed to have a ranking value of zero. 	 */
name|String
name|SERVICE_RANKING
init|=
literal|"service.ranking"
decl_stmt|;
comment|/** 	 * Service property identifying a service's vendor. 	 *  	 *<p> 	 * This property may be supplied in the properties {@code Dictionary} object 	 * passed to the {@code BundleContext.registerService} method. 	 */
name|String
name|SERVICE_VENDOR
init|=
literal|"service.vendor"
decl_stmt|;
comment|/** 	 * Service property identifying a service's description. 	 *  	 *<p> 	 * This property may be supplied in the properties {@code Dictionary} object 	 * passed to the {@code BundleContext.registerService} method. 	 */
name|String
name|SERVICE_DESCRIPTION
init|=
literal|"service.description"
decl_stmt|;
comment|/** 	 * Framework environment property identifying the Framework's universally 	 * unique identifier (UUID). A UUID represents a 128-bit value. A new UUID 	 * is generated by the {@link Framework#init()} method each time a framework 	 * is initialized. The value of this property must conform to the UUID 	 * string representation specified in<a 	 * href="http://www.ietf.org/rfc/rfc4122.txt">RFC 4122</a>. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.6 	 */
name|String
name|FRAMEWORK_UUID
init|=
literal|"org.osgi.framework.uuid"
decl_stmt|;
comment|/** 	 * Service property identifying the configuration types supported by a 	 * distribution provider. Registered by the distribution provider on one of 	 * its services to indicate the supported configuration types. 	 *  	 *<p> 	 * The value of this property must be of type {@code String}, 	 * {@code String[]}, or {@code Collection} of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|REMOTE_CONFIGS_SUPPORTED
init|=
literal|"remote.configs.supported"
decl_stmt|;
comment|/** 	 * Service property identifying the intents supported by a distribution 	 * provider. Registered by the distribution provider on one of its services 	 * to indicate the vocabulary of implemented intents. 	 *  	 *<p> 	 * The value of this property must be of type {@code String}, 	 * {@code String[]}, or {@code Collection} of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|REMOTE_INTENTS_SUPPORTED
init|=
literal|"remote.intents.supported"
decl_stmt|;
comment|/** 	 * Service property identifying the configuration types that should be used 	 * to export the service. Each configuration type represents the 	 * configuration parameters for an endpoint. A distribution provider should 	 * create an endpoint for each configuration type that it supports. 	 *  	 *<p> 	 * This property may be supplied in the {@code properties} 	 * {@code Dictionary} object passed to the 	 * {@code BundleContext.registerService} method. The value of this property 	 * must be of type {@code String}, {@code String[]}, or {@code Collection} 	 * of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|SERVICE_EXPORTED_CONFIGS
init|=
literal|"service.exported.configs"
decl_stmt|;
comment|/** 	 * Service property identifying the intents that the distribution provider 	 * must implement to distribute the service. Intents listed in this property 	 * are reserved for intents that are critical for the code to function 	 * correctly, for example, ordering of messages. These intents should not be 	 * configurable. 	 *  	 *<p> 	 * This property may be supplied in the {@code properties} 	 * {@code Dictionary} object passed to the 	 * {@code BundleContext.registerService} method. The value of this property 	 * must be of type {@code String}, {@code String[]}, or {@code Collection} 	 * of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|SERVICE_EXPORTED_INTENTS
init|=
literal|"service.exported.intents"
decl_stmt|;
comment|/** 	 * Service property identifying the extra intents that the distribution 	 * provider must implement to distribute the service. This property is 	 * merged with the {@code service.exported.intents} property before the 	 * distribution provider interprets the listed intents; it has therefore the 	 * same semantics but the property should be configurable so the 	 * administrator can choose the intents based on the topology. Bundles 	 * should therefore make this property configurable, for example through the 	 * Configuration Admin service. 	 *  	 *<p> 	 * This property may be supplied in the {@code properties} 	 * {@code Dictionary} object passed to the 	 * {@code BundleContext.registerService} method. The value of this property 	 * must be of type {@code String}, {@code String[]}, or {@code Collection} 	 * of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|SERVICE_EXPORTED_INTENTS_EXTRA
init|=
literal|"service.exported.intents.extra"
decl_stmt|;
comment|/** 	 * Service property marking the service for export. It defines the 	 * interfaces under which this service can be exported. This list must be a 	 * subset of the types under which the service was registered. The single 	 * value of an asterisk (&quot;*&quot;,&#92;u002A) indicates all the 	 * interface types under which the service was registered excluding the 	 * non-interface types. It is strongly recommended to only export interface 	 * types and not concrete classes due to the complexity of creating proxies 	 * for some type of concrete classes. 	 *  	 *<p> 	 * This property may be supplied in the {@code properties} 	 * {@code Dictionary} object passed to the 	 * {@code BundleContext.registerService} method. The value of this property 	 * must be of type {@code String}, {@code String[]}, or {@code Collection} 	 * of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|SERVICE_EXPORTED_INTERFACES
init|=
literal|"service.exported.interfaces"
decl_stmt|;
comment|/** 	 * Service property identifying the service as imported. This service 	 * property must be set by a distribution provider to any value when it 	 * registers the endpoint proxy as an imported service. A bundle can use 	 * this property to filter out imported services. 	 *  	 *<p> 	 * The value of this property may be of any type. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|SERVICE_IMPORTED
init|=
literal|"service.imported"
decl_stmt|;
comment|/** 	 * Service property identifying the configuration types used to import the 	 * service. Any associated properties for this configuration types must be 	 * properly mapped to the importing system. For example, a URL in these 	 * properties must point to a valid resource when used in the importing 	 * framework. If multiple configuration types are listed in this property, 	 * then they must be synonyms for exactly the same remote endpoint that is 	 * used to export this service. 	 *  	 *<p> 	 * The value of this property must be of type {@code String}, 	 * {@code String[]}, or {@code Collection} of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 * @see #SERVICE_EXPORTED_CONFIGS 	 */
name|String
name|SERVICE_IMPORTED_CONFIGS
init|=
literal|"service.imported.configs"
decl_stmt|;
comment|/** 	 * Service property identifying the intents that this service implement. 	 * This property has a dual purpose: 	 *<ul> 	 *<li>A bundle can use this service property to notify the distribution 	 * provider that these intents are already implemented by the exported 	 * service object.</li> 	 *<li>A distribution provider must use this property to convey the combined 	 * intents of: the exporting service, the intents that the exporting 	 * distribution provider adds, and the intents that the importing 	 * distribution provider adds.</li> 	 *</ul> 	 *  	 * To export a service, a distribution provider must expand any qualified 	 * intents. Both the exporting and importing distribution providers must 	 * recognize all intents before a service can be distributed. 	 *  	 *<p> 	 * The value of this property must be of type {@code String}, 	 * {@code String[]}, or {@code Collection} of {@code String}. 	 *  	 * @since 1.6 	 * @see "Remote Services Specification" 	 */
name|String
name|SERVICE_INTENTS
init|=
literal|"service.intents"
decl_stmt|;
comment|/** 	 * Manifest header identifying the capabilities that the bundle offers to 	 * provide to other bundles. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.6 	 */
name|String
name|PROVIDE_CAPABILITY
init|=
literal|"Provide-Capability"
decl_stmt|;
comment|/** 	 * Manifest header identifying the capabilities on which the bundle depends. 	 *  	 *<p> 	 * The header value may be retrieved from the {@code Dictionary} object 	 * returned by the {@code Bundle.getHeaders} method. 	 *  	 * @since 1.6 	 */
name|String
name|REQUIRE_CAPABILITY
init|=
literal|"Require-Capability"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying the effective time of the provided 	 * capability. The default value is {@link #EFFECTIVE_RESOLVE resolve}. 	 *  	 *<p> 	 * The directive value is encoded in the Provide-Capability manifest header 	 * like: 	 *  	 *<pre> 	 *     Provide-Capability: com.acme.capability; effective:=&quot;resolve&quot; 	 *</pre> 	 *  	 * @see #PROVIDE_CAPABILITY 	 * @see #EFFECTIVE_RESOLVE 	 * @see #EFFECTIVE_ACTIVE 	 * @since 1.6 	 */
name|String
name|EFFECTIVE_DIRECTIVE
init|=
literal|"effective"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a capability that is 	 * effective at resolve time. Capabilities with an effective time of resolve 	 * are the only capabilities which are processed by the resolver. 	 *  	 *<p> 	 * The directive value is encoded in the Provide-Capability manifest header 	 * like: 	 *  	 *<pre> 	 *     Provide-Capability: com.acme.capability; effective:=&quot;resolve&quot; 	 *</pre> 	 *  	 * @see #EFFECTIVE_DIRECTIVE 	 * @since 1.6 	 */
name|String
name|EFFECTIVE_RESOLVE
init|=
literal|"resolve"
decl_stmt|;
comment|/** 	 * Manifest header directive value identifying a capability that is 	 * effective at active time. Capabilities with an effective time of active 	 * are ignored by the resolver. 	 *  	 *<p> 	 * The directive value is encoded in the Provide-Capability manifest header 	 * like: 	 *  	 *<pre> 	 *     Provide-Capability: com.acme.capability; effective:=&quot;active&quot; 	 *</pre> 	 *  	 * @see #EFFECTIVE_DIRECTIVE 	 * @since 1.6 	 */
name|String
name|EFFECTIVE_ACTIVE
init|=
literal|"active"
decl_stmt|;
comment|/** 	 * Manifest header directive identifying the capability filter specified in 	 * the Require-Capability manifest header. 	 *  	 *<p> 	 * The directive value is encoded in the Require-Capability manifest header 	 * like: 	 *  	 *<pre> 	 *     Require-Capability: com.acme.capability; filter:=&quot;(someattr=somevalue)&quot; 	 *</pre> 	 *  	 * @see #REQUIRE_CAPABILITY 	 * @since 1.6 	 */
name|String
name|FILTER_DIRECTIVE
init|=
literal|"filter"
decl_stmt|;
comment|/** 	 * Framework launching property identifying capabilities which the system 	 * bundle must provide. 	 *  	 *<p> 	 * If this property is not specified then the framework must calculate a 	 * reasonable default value for the current execution environment. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @since 1.6 	 */
name|String
name|FRAMEWORK_SYSTEMCAPABILITIES
init|=
literal|"org.osgi.framework.system.capabilities"
decl_stmt|;
comment|/** 	 * Framework launching property identifying extra capabilities which the 	 * system bundle must additionally provide. 	 *  	 *<p> 	 * This property is useful for configuring extra system capabilities in 	 * addition to the system capabilities calculated by the framework. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @see #FRAMEWORK_SYSTEMCAPABILITIES 	 * @since 1.6 	 */
name|String
name|FRAMEWORK_SYSTEMCAPABILITIES_EXTRA
init|=
literal|"org.osgi.framework.system.capabilities.extra"
decl_stmt|;
comment|/** 	 * Framework launching property specifying whether multiple bundles having 	 * the same {@link #BUNDLE_SYMBOLICNAME symbolic name} and 	 * {@link #BUNDLE_VERSION version} may be installed. 	 *  	 *<p> 	 * Default value is {@link #FRAMEWORK_BSNVERSION_SINGLE single} in this 	 * release of the specification. This default may change to 	 * {@link #FRAMEWORK_BSNVERSION_MULTIPLE multiple} in a future specification 	 * release. Therefore, code must not assume the default behavior is 	 * {@code single} and should interrogate the value of this property to 	 * determine the behavior. 	 *  	 *<p> 	 * The value of this property may be retrieved by calling the 	 * {@code BundleContext.getProperty} method. 	 *  	 * @see #FRAMEWORK_BSNVERSION_MULTIPLE 	 * @see #FRAMEWORK_BSNVERSION_SINGLE 	 * @since 1.6 	 */
name|String
name|FRAMEWORK_BSNVERSION
init|=
literal|"org.osgi.framework.bsnversion"
decl_stmt|;
comment|/** 	 * Specifies the framework will allow multiple bundles to be installed 	 * having the same symbolic name and version. 	 *  	 * @since 1.6 	 * @see #FRAMEWORK_BSNVERSION 	 */
name|String
name|FRAMEWORK_BSNVERSION_MULTIPLE
init|=
literal|"multiple"
decl_stmt|;
comment|/** 	 * Specifies the framework will only allow a single bundle to be installed 	 * for a given symbolic name and version. It will be an error to install a 	 * bundle or update a bundle to have the same symbolic name and version as 	 * another installed bundle. 	 *  	 * @since 1.6 	 * @see #FRAMEWORK_BSNVERSION 	 */
name|String
name|FRAMEWORK_BSNVERSION_SINGLE
init|=
literal|"single"
decl_stmt|;
block|}
end_interface

end_unit

