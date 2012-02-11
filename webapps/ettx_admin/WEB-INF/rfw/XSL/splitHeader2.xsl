<?xml version="1.0"?> 
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
 
<!-- Setting xsl parameters -->
  
  <xsl:param name="recordPerPage" select="20"/>
  <xsl:param name="pageNumber" select="'1'"/>
  
  <xsl:param name="sortValue" select="'default value'"/>
  <xsl:param name="sOrder" select="'default value'"/>
  <xsl:param name="dataType" select="'text'"/> 
  <xsl:param name="sortColumn" select="'false'"/> 
  
  <xsl:param name="formatFile" select="'default value'"/>
  

 <xsl:variable name="columns" select="document($formatFile,.)/Format/TableInner" />
 
 <!-- Init arrays of keys to search through format.xml -->
 <xsl:key name="myCol" match="Column" use="@xmlTag"/>
 <xsl:key name="mySubCol" match="SubColumn" use="@xmlTag"/>
 
 <xsl:template match="/">

  <table border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF" align="center" width="100%">
 
  <tr> 
     <td colspan="41" bgcolor="#999999"><img src="RSRC/en_US/images/shim.gif"/></td>
  </tr>
 
  <tr>

  <!-- Looping for column headers from format.xml -->
  <xsl:for-each select="$columns">
      
      	<td height="22" rowspan="2" colspan="1" bgcolor="#b4afb3"  align="center"><xsl:element name="img"><xsl:attribute name="src">RSRC/en_US/images/shim.gif</xsl:attribute></xsl:element></td>

 	<xsl:for-each select="*">
	    <!--  <td height="22" bgcolor="#A7A7A7"  align="center"> 
            <xsl:variable name="display">
               <xsl:value-of select="@displayName"/>
            </xsl:variable>  -->

     		<xsl:variable name="tagName">
               <xsl:value-of select="@xmlTag"/>
      	</xsl:variable>
	
		<xsl:variable name="sortable">
               <xsl:value-of select="@sortable"/>
            </xsl:variable>
            
 		    
      <xsl:variable name="cols" select="count(SubColumn)"/>
      <xsl:variable name="colSpan">
               <xsl:choose>
				<xsl:when test="$cols=0">1</xsl:when>
             		<xsl:otherwise><xsl:value-of select="$cols"/>
				</xsl:otherwise>
		   </xsl:choose>
      </xsl:variable>
      <xsl:variable name="rowSpan">
            <xsl:choose><xsl:when test="$colSpan='1'">2</xsl:when>
             		<xsl:otherwise>1</xsl:otherwise>
     		</xsl:choose>
      </xsl:variable>

     <xsl:element name="td">
     			<xsl:attribute name="rowspan"><xsl:value-of select="$rowSpan"/></xsl:attribute>
			<xsl:attribute name="colspan"><xsl:value-of select="$colSpan"/></xsl:attribute>
			<xsl:attribute name="bgcolor">#b4afb3</xsl:attribute>
			<xsl:attribute name="align">center</xsl:attribute>
     			
                 <xsl:choose>
			<xsl:when test="contains($sortable,'true') and string-length($sortable) = string-length('true')">
			 <xsl:choose> 
            		<xsl:when test="contains($sortValue,$tagName) and string-length($sortValue) = string-length($tagName)">
                        	<xsl:choose>
                			<xsl:when test="contains($sOrder,'descending') and string-length($sOrder) = string-length('descending')">
                       			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$tagName"/>','<xsl:value-of select="@dataType"/>','ascending')</xsl:attribute>
                             			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_dn.gif" border="0"/> 
                       			</a></b>
               			</xsl:when> 
                        	<xsl:otherwise>
                      			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$tagName"/>','<xsl:value-of select="@dataType"/>','descending')</xsl:attribute>
                            			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_up.gif" border="0"/> 
                      			</a></b>
                        	</xsl:otherwise> 
                        	</xsl:choose>
            		</xsl:when>
            		<xsl:otherwise>
             			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$tagName"/>','<xsl:value-of select="@dataType"/>','descending')</xsl:attribute>
                   			<xsl:value-of select="@displayName"/> 
             			</a></b>
				</xsl:otherwise>
			 </xsl:choose>
			</xsl:when>
		<xsl:otherwise> <xsl:value-of select="@displayName"/> </xsl:otherwise>
		</xsl:choose> 
     	</xsl:element>	<!-- end element "td" --> 
	</xsl:for-each>
    <!--   </xsl:element>  end element "tr" --> 
   </xsl:for-each> 
  </tr>
  
<tr>

  <xsl:for-each select="$columns/Column/SubColumn">
      <xsl:variable name="tagName">
               <xsl:value-of select="@xmlTag"/>
      </xsl:variable>

	<xsl:variable name="sortable">
            <xsl:value-of select="@sortable"/>
      </xsl:variable>  
      		<xsl:element name="td">       
			<xsl:attribute name="bgcolor">#b4afb3</xsl:attribute>
			<xsl:attribute name="align">center</xsl:attribute>   
                  <xsl:choose>
			<xsl:when test="contains($sortable,'true') and string-length($sortable) = string-length('true')">
			 <xsl:choose> 
            		<xsl:when test="contains($sortValue,$tagName) and string-length($sortValue) = string-length($tagName)">
                        	<xsl:choose>
                			<xsl:when test="contains($sOrder,'descending') and string-length($sOrder) = string-length('descending')">
                       			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$tagName"/>','<xsl:value-of select="@dataType"/>','ascending')                          
                                              </xsl:attribute>
                             			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_dn.gif" border="0"/> 
                       			</a></b>
               			</xsl:when> 
                        	<xsl:otherwise>
                      			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$tagName"/>','<xsl:value-of select="@dataType"/>','descending')                          
                                              </xsl:attribute>
                            			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_up.gif" border="0"/> 
                      			</a></b>
                        	</xsl:otherwise> 
                        	</xsl:choose>
            		</xsl:when>
            		<xsl:otherwise>
             			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$tagName"/>','<xsl:value-of select="@dataType"/>','descending')                          
                                              </xsl:attribute>
                   			<xsl:value-of select="@displayName"/> 
             			</a></b>
				</xsl:otherwise>
			 </xsl:choose>
			</xsl:when>
			<xsl:otherwise> <xsl:value-of select="@displayName"/> </xsl:otherwise>
		</xsl:choose> 
         </xsl:element>
    </xsl:for-each>
    </tr>

  <tr>
  </tr>
  <!-- Calculate display rows -->

  <xsl:variable name="startRow">
           <xsl:value-of select="number($pageNumber - 1) * $recordPerPage "/>
  </xsl:variable>
  <xsl:variable name="endRow">
           <xsl:value-of select="number($startRow + $recordPerPage)"/>
  </xsl:variable>
  
  <!-- Looping through records, getting data for selected rows -->

  <xsl:for-each select="*/*/*"> 
  <xsl:sort data-type="{$dataType}" order="{$sOrder}" select="*[local-name()=$sortColumn]"/> 
  <xsl:if test="position() &gt; $startRow and position() &lt;= $endRow ">
  
     <xsl:element name="tr">
            <xsl:choose>
     		<xsl:when test="position() mod 2 = 0">
           		<xsl:attribute name="bgcolor">#cecfce</xsl:attribute></xsl:when>
     		<xsl:otherwise>            
           		<xsl:attribute name="bgcolor">#f0f0f0</xsl:attribute>
		</xsl:otherwise>
     		</xsl:choose>

      	<xsl:element name="td">
			<xsl:attribute name="align">right</xsl:attribute>
         		<xsl:value-of select="position()"/>.  
      	</xsl:element>	

		 <xsl:for-each select="*"> 
              <xsl:element name="td">

                  <xsl:variable name="tagName">
                        	<xsl:value-of select="name()"/>
                  </xsl:variable>
			<xsl:variable name="textValue">
                        	<xsl:value-of select="."/>
                  </xsl:variable>
			<xsl:variable name="hrefValue">
                          <xsl:value-of select="@HREF"/>
                  </xsl:variable>
                  <xsl:variable name="onclickValue">
                          <xsl:value-of select="@ONCLICK"/>
                  </xsl:variable>

			<xsl:for-each select="$columns">

                  <xsl:variable name="type" select="key('myCol', $tagName)"/>
			<xsl:variable name="type2" select="key('mySubCol', $tagName)"/>

                  <xsl:choose>
                  
               	 <!-- <xsl:when test="$type2/FontStyle/@value = 'Bold'"> ???  -->
                    <xsl:when test="$type2/@Font = 'Bold'">   
				<xsl:attribute name="align">center</xsl:attribute>
                        <b>  <xsl:value-of select="$textValue"/> </b>
                    </xsl:when>
			
                     <!-- Create link for href -->
                    <xsl:when test="concat( string( $hrefValue ), string( $onclickValue ) )">
                        <xsl:choose>
                        <xsl:when test="$type/@dataType= 'number'">
                                <xsl:attribute name="align">right</xsl:attribute>
                        </xsl:when>
                        <xsl:when test="$type2/@dataType = 'number'">
                                <xsl:attribute name="align">right</xsl:attribute>
                        </xsl:when>
                        </xsl:choose>

                        <xsl:element name="a">
                          <!-- if href output value and target=_blank -->
                          <xsl:if test="string( $hrefValue )">
                            <xsl:attribute name="HREF">
                              <xsl:value-of select="$hrefValue"/>
                            </xsl:attribute>
                            <xsl:attribute name="target">_blank</xsl:attribute>
                          </xsl:if>
                          <!-- if onclick output value -->
                          <xsl:if test="string( $onclickValue )">
                            <!-- if onclick and no href output href void -->
                            <xsl:if test="not( string( $hrefValue ) )">
                              <xsl:attribute name="HREF">javascript:void(0)</xsl:attribute>
                            </xsl:if>
                            <xsl:attribute name="onClick">
                              <xsl:value-of select="$onclickValue"/>
                            </xsl:attribute>
                          </xsl:if>
                          <xsl:value-of select="$textValue"/>
                        </xsl:element>
                    </xsl:when>

                    <!-- number aligned right -->
                    <xsl:when test="$type/@dataType = 'number'">
                        <xsl:attribute name="align">right</xsl:attribute>
                        <xsl:value-of select="$textValue"/>
                    </xsl:when>
                    <xsl:when test="$type2/@dataType = 'number'">
                        <xsl:attribute name="align">right</xsl:attribute>
                        <xsl:value-of select="$textValue"/>
                    </xsl:when>

            	  <xsl:otherwise> 
				<xsl:value-of select="$textValue"/>
                    </xsl:otherwise>
                    </xsl:choose>
                    </xsl:for-each> 
  
                </xsl:element>
		 </xsl:for-each> 
      </xsl:element>  <!-- end element "tr" --> 
   </xsl:if>
   
   </xsl:for-each> 

  </table>

</xsl:template>

</xsl:stylesheet>
