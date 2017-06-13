<#-- 公用分页组件:使用方式 1.导入<#include "../include/page.ftl">  在相应位置写上 <@page data=goodsDetailPage /> -->
<#macro page data>     
  <#if (goodsDetailPage.totalPage > 0) >
    <div class="ui right floated pagination menu">
        <a class="icon item <#if (goodsDetailPage.hasPrevPage == false) > disabled </#if> " href="?orderCode=${formData.orderCode!}&pageSize=${formData.pageSize!}&currentPage=${goodsDetailPage.prevPage!}">
            <i class="left chevron icon"></i>
        </a>
        <#list 1..5 as index> 
           <#assign currentPage=goodsDetailPage.currentPage+index - 3 />   
           <#if (currentPage>0 && currentPage <= goodsDetailPage.totalPage) >
              <a class="item <#if (goodsDetailPage.currentPage == currentPage) > active </#if> " 
              href="?orderCode=1&pageSize=${formData.pageSize!}&currentPage=${currentPage!}">${currentPage!}</a>
           </#if>
        </#list>
        <a class="icon item <#if (goodsDetailPage.hasNextPage == false) >  disabled </#if> " href="?orderCode=${formData.orderCode!}&pageSize=${formData.pageSize!}&currentPage=${goodsDetailPage.nextPage!}">
            <i class="right chevron icon"></i>
        </a>
    </div>
  </#if>
</#macro> 