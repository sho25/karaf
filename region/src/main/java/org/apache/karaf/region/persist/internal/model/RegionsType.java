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
comment|// Generated on: 2011.10.28 at 03:20:55 PM PDT
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
name|region
operator|.
name|persist
operator|.
name|internal
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|XmlAccessType
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
name|XmlAccessorType
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
name|XmlRootElement
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
name|XmlType
import|;
end_import

begin_comment
comment|/**  *   *                 Regions element  *               *   *<p>Java class for regionsType complex type.  *   *<p>The following schema fragment specifies the expected content contained within this class.  *   *<pre>  *&lt;complexType name="regionsType">  *&lt;complexContent>  *&lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">  *&lt;sequence>  *&lt;element name="region" type="{http://karaf.apache.org/xmlns/region/v1.0.0}regionType" maxOccurs="unbounded" minOccurs="0"/>  *&lt;element name="filter" type="{http://karaf.apache.org/xmlns/region/v1.0.0}filterType" maxOccurs="unbounded" minOccurs="0"/>  *&lt;/sequence>  *&lt;/restriction>  *&lt;/complexContent>  *&lt;/complexType>  *</pre>  *   *   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"regions"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"regionsType"
argument_list|,
name|propOrder
operator|=
block|{
literal|"region"
block|,
literal|"filter"
block|}
argument_list|)
specifier|public
class|class
name|RegionsType
block|{
specifier|protected
name|List
argument_list|<
name|RegionType
argument_list|>
name|region
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|FilterType
argument_list|>
name|filter
decl_stmt|;
comment|/**      * Gets the value of the region property.      *       *<p>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the region property.      *       *<p>      * For example, to add a new item, do as follows:      *<pre>      *    getRegion().add(newItem);      *</pre>      *       *       *<p>      * Objects of the following type(s) are allowed in the list      * {@link RegionType }      *       *       */
specifier|public
name|List
argument_list|<
name|RegionType
argument_list|>
name|getRegion
parameter_list|()
block|{
if|if
condition|(
name|region
operator|==
literal|null
condition|)
block|{
name|region
operator|=
operator|new
name|ArrayList
argument_list|<
name|RegionType
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|region
return|;
block|}
comment|/**      * Gets the value of the filter property.      *       *<p>      * This accessor method returns a reference to the live list,      * not a snapshot. Therefore any modification you make to the      * returned list will be present inside the JAXB object.      * This is why there is not a<CODE>set</CODE> method for the filter property.      *       *<p>      * For example, to add a new item, do as follows:      *<pre>      *    getFilter().add(newItem);      *</pre>      *       *       *<p>      * Objects of the following type(s) are allowed in the list      * {@link FilterType }      *       *       */
specifier|public
name|List
argument_list|<
name|FilterType
argument_list|>
name|getFilter
parameter_list|()
block|{
if|if
condition|(
name|filter
operator|==
literal|null
condition|)
block|{
name|filter
operator|=
operator|new
name|ArrayList
argument_list|<
name|FilterType
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|filter
return|;
block|}
block|}
end_class

end_unit
