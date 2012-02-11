<?xml version="1.0"?> 
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
 
 
  <xsl:param name="sortValue" select="'none'"/>
  <xsl:param name="sortColumn" select="'false'"/> 
  <xsl:param name="dataType" select="'text'"/> 
  <xsl:param name="sOrder" select="'descending'"/>
  <xsl:param name="xmlFile" select="'2000.xml'"/>
  <xsl:param name="formatFile" select="'default value'"/>


 <xsl:variable name="columns" select="document($formatFile,.)/Format/TableInner" />
 
 <xsl:key name="myCol" match="Column" use="@xmlTag"/>
 <xsl:key name="mySubCol" match="SubColumn" use="@xmlTag"/>
 
 <xsl:template match="/">

  <html> 
  <body>
  
  <table border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF" align="center" width="100%">
  <tr>         
  	<td height="5" colspan="41" background="RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999">
  	</td>
  </tr>

  <tr> 
     <td colspan="41" bgcolor="#999999"><img src="RSRC/en_US/images/shim.gif"/></td>
  </tr>
 
  <tr>
  <xsl:for-each select="$columns">
      
      	<td height="22" rowspan="2" colspan="1" bgcolor="#b4afb3"  align="center"></td>

 	    <xsl:for-each select="*">
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
            		<xsl:when test="contains($sortValue,$tagName) and string-length($sortValue) = string-length($tagName)">
                        	<xsl:choose>
                			<xsl:when test="contains($sOrder,'descending') and string-length($sOrder) = string-length('descending')">
                       			<b>
                             			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_dn.gif" border="0"/> 
                       			</b>
               			</xsl:when> 
                        	<xsl:otherwise>
                      			<b>
                            			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_up.gif" border="0"/> 
                      			</b>
                        	</xsl:otherwise> 
                        	</xsl:choose>
            		</xsl:when>
            		<xsl:otherwise>
             			<b>
                   			<xsl:value-of select="@displayName"/> 
             			</b>
				</xsl:otherwise>
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
            		<xsl:when test="contains($sortValue,$tagName) and string-length($sortValue) = string-length($tagName)">
                        	<xsl:choose>
                			<xsl:when test="contains($sOrder,'descending') and string-length($sOrder) = string-length('descending')">
                       			<b>
                             			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_dn.gif" border="0"/> 
                       			</b>
               			</xsl:when> 
                        	<xsl:otherwise>
                      			<b>
                            			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_up.gif" border="0"/> 
                      			</b>
                        	</xsl:otherwise> 
                        	</xsl:choose>
            		</xsl:when>
            		<xsl:otherwise>
             			<b>
                   			<xsl:value-of select="@displayName"/> 
             			</b>
				</xsl:otherwise>			
			</xsl:choose> 
         </xsl:element>
    </xsl:for-each>
    </tr>

  <tr>
  </tr>


  <xsl:for-each select="*/*" >
  <xsl:sort data-type="{$dataType}" order="{$sOrder}" select="*[local-name()=$sortColumn]"/> 
 
     <xsl:element name="tr">
            <!-- <xsl:choose>
     		<xsl:when test="position() mod 2 = 0">
           		<xsl:attribute name="bgcolor">#ffffff</xsl:attribute></xsl:when>
     		<xsl:otherwise>            
           		<xsl:attribute name="bgcolor">#f0f0f0</xsl:attribute>
		</xsl:otherwise>
     		</xsl:choose>
            -->
      	<xsl:element name="td">
			<xsl:attribute name="align">right</xsl:attribute>
         		<xsl:value-of select="position()"/>  
      	</xsl:element>	
		<xsl:for-each select="*"> 
              <xsl:element name="td">
                  <xsl:variable name="tagName">
                        	<xsl:value-of select="name()"/>
                  </xsl:variable>
                  <xsl:variable name="textValue">
                        	<xsl:value-of select="."/>
                  </xsl:variable>
                 

                  <xsl:for-each select="$columns">
                    <xsl:variable name="type" select="key('myCol', $tagName)"/>
                 
                  <xsl:choose>
                   
                    <xsl:when test="$type/@dataType= 'number'">
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
      </xsl:element>
      <xsl:element name="tr">
	   		 <td colspan="41"><img src="RSRC/en_US/images/999999.gif" width="100%" height="1"/></td>
      </xsl:element>

   </xsl:for-each>
  
  <tr> 
     <td colspan="41" bgcolor="#999999"><img src="RSRC/en_US/images/shim.gif"/></td>
  </tr>
  <tr>         
  	<td height="5" colspan="41" background="RSRC/en_US/images/vertlines_dk.gif" bgcolor="#669999">
  	</td>
  </tr>

  </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>
