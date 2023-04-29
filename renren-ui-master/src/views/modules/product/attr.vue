<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-product__attr}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.attrId" placeholder="attrId" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:attr:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:attr:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="attrId" label="属性id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="attrName" label="属性名" header-align="center" align="center"></el-table-column>
        <el-table-column prop="searchType" label="是否需要检索[0-不需要，1-需要]" header-align="center" align="center"></el-table-column>
        <el-table-column prop="valueType" label="值类型[0-为单个值，1-可以选择多个值]" header-align="center" align="center"></el-table-column>
        <el-table-column prop="icon" label="属性图标" header-align="center" align="center"></el-table-column>
        <el-table-column prop="valueSelect" label="可选值列表[用逗号分隔]" header-align="center" align="center"></el-table-column>
        <el-table-column prop="attrType" label="属性类型[0-销售属性，1-基本属性" header-align="center" align="center"></el-table-column>
        <el-table-column prop="enable" label="启用状态[0 - 禁用，1 - 启用]" header-align="center" align="center"></el-table-column>
        <el-table-column prop="catelogId" label="所属分类" header-align="center" align="center"></el-table-column>
        <el-table-column prop="showDesc" label="快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整" header-align="center" align="center"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('product:attr:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.attrId)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('product:attr:delete')" type="text" size="small" @click="deleteHandle(scope.row.attrId)">{{ $t('delete') }}</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="limit"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="pageSizeChangeHandle"
        @current-change="pageCurrentChangeHandle">
      </el-pagination>
      <!-- 弹窗, 新增 / 修改 -->
      <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    </div>
  </el-card>
</template>

<script>
import mixinViewModule from '@/mixins/view-module'
import AddOrUpdate from './attr-add-or-update'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/product/attr/page',
        getDataListIsPage: true,
        exportURL: '/product/attr/export',
        deleteURL: '/product/attr',
        deleteIsBatch: true
      },
      dataForm: {
        attrId: ''
      }
    }
  },
  components: {
    AddOrUpdate
  }
}
</script>
