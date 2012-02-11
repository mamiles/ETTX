<?xml version="1.0"?> 
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
   
  <!-- Setting xsl parameters -->
  
  <xsl:param name="recordPerPage" select="20"/>
  <xsl:param name="pageNumber" select="'1'"/>

  <xsl:param name="sortValue" select="'none'"/>
  <xsl:param name="sOrder" select="'default value'"/>
  <xsl:param name="dataType" select="'text'"/> 
  <xsl:param name="sortColumn" select="'false'"/>
  
  <xsl:param name="formatFile" select="'format.xml'"/>

<!-- Init array of keys to search through format.xml -->

<xsl:key name="myCol" match="Column" use="@xmlTag"/>
<xsl:variable name="columns" select="document($formatFile,.)/Format/TableInner" /> 

<xsl:template match="/">

  <table border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF" align="center" width="100%">
 
  <!-- Looping for column headers from format.xml -->

  <xsl:for-each select="$columns">

    	<xsl:element name="tr">
      	<td height="22" bgcolor="#A7A7A7"  align="center"><xsl:element name="img"><xsl:attribute name="src">RSRC/en_US/images/shim.gif</xsl:attribute></xsl:element></td>

 		<xsl:for-each select="*">
	      <td height="22" bgcolor="#A7A7A7"  align="center">
            
            <xsl:variable name="xmlTag">
               <xsl:value-of select="@xmlTag"/>
            </xsl:variable>
		<xsl:variable name="dataType">
               <xsl:value-of select="@dataType"/>
            </xsl:variable>
		<xsl:variable name="sortable">
               <xsl:value-of select="@sortable"/>
            </xsl:variable>
            
 		<xsl:choose>
			<xsl:when test="contains($sortable,'true') and string-length($sortable) = string-length('true')">
			<xsl:choose> 
            		<xsl:when test="contains($sortValue,$xmlTag) and string-length($sortValue) = string-length($xmlTag)">
                        	<xsl:choose>
                			<xsl:when test="contains($sOrder,'descending') and string-length($sOrder) = string-length('descending')">
                                        <b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$xmlTag"/>','<xsl:value-of select="$dataType"/>','ascending')</xsl:attribute>
                             			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_dn.gif" border="0"/> 
                       			</a></b>
               			</xsl:when> 
                        	<xsl:otherwise>
                      			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$xmlTag"/>','<xsl:value-of select="$dataType"/>','descending')</xsl:attribute>
                            			<xsl:value-of select="@displayName"/>  <img src="RSRC/en_US/images/ico_sort_up.gif" border="0"/> 
                      			</a></b>
                        	</xsl:otherwise> 
                        	</xsl:choose>
            		</xsl:when>
            		<xsl:otherwise>
             			<b><a><xsl:attribute name="href">javascript:sort('<xsl:value-of select="$xmlTag"/>','<xsl:value-of select="$dataType"/>','descending')</xsl:attribute>
                   			<xsl:value-of select="@displayName"/> 
             			</a></b>
				</xsl:otherwise>
			 </xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="@displayName"/> 
			</xsl:otherwise>
		</xsl:choose>

            </td>
	</xsl:for-each>
      </xsl:element> 
   </xsl:for-each> 
  
  <!-- Calculate display rows -->
  <xsl:variable name="startRow">
           <xsl:value-of select="number($pageNumber - 1) * $recordPerPage"/>
  </xsl:variable>
  <xsl:variable name="endRow">
           <xsl:value-of select="number($startRow + $recordPerPage)"/>
  </xsl:variable>
  
  <!-- Looping through records, getting data for selected rows -->

  <xsl:for-each select="*/*"> 
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

            <!-- Looping through data/tags of each record -->
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
                  
                  <!-- Checking the format of column with <tagName> to decide display format -->
                  <xsl:for-each select="$columns">

                    <xsl:variable name="type" select="key('myCol', $tagName)"/>
                    <xsl:choose>
                      <xsl:when test="$type/@dataType= 'number'">
                        <xsl:attribute name="align">right</xsl:attribute>
                      </xsl:when>
                      <!-- otherwise text is aligned left by default -->
                    </xsl:choose>

                    <xsl:choose>

                      <!-- added by edebolle so windows attributes can be
                           controlled by using window.open( ... )  -->
                      <!-- when either href or onclick is present output a
                           link element -->
                      <xsl:when test="concat( string( $hrefValue ), string( $onclickValue ) )">
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

