begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|//
end_comment

begin_comment
comment|// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833
end_comment

begin_comment
comment|// See<a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
end_comment

begin_comment
comment|// Any modifications to this file will be lost upon recompilation of the source schema.
end_comment

begin_comment
comment|// Generated on: 2011.01.19 at 04:45:46 PM PST
end_comment

begin_comment
comment|//
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|karaf
operator|.
name|features
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementDecl
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * This object contains factory methods for each   * Java content interface and Java element interface   * generated in the org.apache.karaf.features.internal package.   *<p>An ObjectFactory allows you to programatically   * construct new instances of the Java representation   * for XML content. The Java representation of XML   * content can consist of schema derived interfaces   * and classes representing the binding of schema   * type definitions, element declarations and model   * groups.  Factory methods for each of these are   * provided in this class.  *   */
end_comment

begin_class
annotation|@
name|XmlRegistry
specifier|public
class|class
name|ObjectFactory
block|{
specifier|private
specifier|final
specifier|static
name|QName
name|_Features_QNAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://karaf.apache.org/xmlns/features/v1.0.0"
argument_list|,
literal|"features"
argument_list|)
decl_stmt|;
comment|/**      * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.apache.karaf.features.internal      *       */
specifier|public
name|ObjectFactory
parameter_list|()
block|{     }
comment|/**      * Create an instance of {@link ConfigFile }      *       */
specifier|public
name|ConfigFile
name|createConfigFile
parameter_list|()
block|{
return|return
operator|new
name|ConfigFile
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link Dependency }      *       */
specifier|public
name|Dependency
name|createDependency
parameter_list|()
block|{
return|return
operator|new
name|Dependency
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link Bundle }      *       */
specifier|public
name|Bundle
name|createBundle
parameter_list|()
block|{
return|return
operator|new
name|Bundle
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link FeaturesRoot }      *       */
specifier|public
name|FeaturesRoot
name|createFeaturesRoot
parameter_list|()
block|{
return|return
operator|new
name|FeaturesRoot
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link Config }      *       */
specifier|public
name|Config
name|createConfig
parameter_list|()
block|{
return|return
operator|new
name|Config
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link Feature }      *       */
specifier|public
name|Feature
name|createFeature
parameter_list|()
block|{
return|return
operator|new
name|Feature
argument_list|()
return|;
block|}
comment|/**      * Create an instance of {@link JAXBElement }{@code<}{@link FeaturesRoot }{@code>}}      *       */
annotation|@
name|XmlElementDecl
argument_list|(
name|namespace
operator|=
literal|"http://karaf.apache.org/xmlns/features/v1.0.0"
argument_list|,
name|name
operator|=
literal|"features"
argument_list|)
specifier|public
name|JAXBElement
argument_list|<
name|FeaturesRoot
argument_list|>
name|createFeatures
parameter_list|(
name|FeaturesRoot
name|value
parameter_list|)
block|{
return|return
operator|new
name|JAXBElement
argument_list|<
name|FeaturesRoot
argument_list|>
argument_list|(
name|_Features_QNAME
argument_list|,
name|FeaturesRoot
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

