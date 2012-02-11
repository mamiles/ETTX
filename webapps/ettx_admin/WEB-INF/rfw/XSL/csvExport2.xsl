 
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
<xsl:output method="text"/>
 
  <xsl:param name="formatFile" select="'format.xml'"/>
  <xsl:param name="sortValue" select="'EventId'"/>
  <xsl:param name="dataType" select="'number'"/> 
  <xsl:param name="sOrder" select="'descending'"/>
  <xsl:param name="sortColumn" select="'false'"/> 

<xsl:variable name="columns" select="document($formatFile,.)/Format/TableInner" /> 

<xsl:template match="/">

  <!-- Writing column headers -->
  <xsl:for-each select="$columns/Column"> 
      <xsl:variable name="display">
           <xsl:value-of select="@displayName"/>
      </xsl:variable>
      <!-- Split columns -->
      <xsl:variable name="cols" select="count(SubColumn)"/>
      <xsl:choose> 
      <xsl:when test="$cols=0"> <xsl:value-of select="$display"/>, </xsl:when> <xsl:otherwise> <xsl:for-each select="SubColumn"> <xsl:value-of select="$display"/> - <xsl:value-of select="@displayName"/>, </xsl:for-each></xsl:otherwise>
	</xsl:choose>   
   </xsl:for-each>
      
   <xsl:text>
   </xsl:text>

  <!-- Writing table data -->

  <xsl:for-each select="*/*/*" >
          <xsl:sort data-type="{$dataType}" order="{$sOrder}" select="*[local-name()=$sortColumn]"/> 
          <xsl:for-each select="*" > <xsl:value-of select="."/>, </xsl:for-each>
  <xsl:text>
  </xsl:text>
  </xsl:for-each>
</xsl:template>
</xsl:stylesheet>
