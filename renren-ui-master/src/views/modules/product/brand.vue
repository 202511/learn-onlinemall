<template>
  <el-card shadow="never" class="aui-card--fill">
    <div class="mod-product__brand}">
      <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
        <el-form-item>
          <el-input v-model="dataForm.brandId" placeholder="brandId" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="getDataList()">{{ $t('query') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="exportHandle()">{{ $t('export') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:brand:save')" type="primary" @click="addOrUpdateHandle()">{{ $t('add') }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="$hasPermission('product:brand:delete')" type="danger" @click="deleteHandle()">{{ $t('deleteBatch') }}</el-button>
        </el-form-item>
      </el-form>
      <el-table v-loading="dataListLoading" :data="dataList" border @selection-change="dataListSelectionChangeHandle" style="width: 100%;">
        <el-table-column type="selection" header-align="center" align="center" width="50"></el-table-column>
        <el-table-column prop="brandId" label="品牌id" header-align="center" align="center"></el-table-column>
        <el-table-column prop="name" label="品牌名" header-align="center" align="center"></el-table-column>
        <el-table-column prop="logo" label="品牌logo地址" header-align="center" align="center"></el-table-column>
        <el-table-column prop="descript" label="介绍" header-align="center" align="center"></el-table-column>
        <el-table-column prop="showStatus" label="显示状态[0-不显示；1-显示]" header-align="center" align="center"></el-table-column>
        <el-table-column prop="firstLetter" label="检索首字母" header-align="center" align="center"></el-table-column>
        <el-table-column prop="sort" label="排序" header-align="center" align="center"></el-table-column>
        <el-table-column :label="$t('handle')" fixed="right" header-align="center" align="center" width="150">
          <template slot-scope="scope">
            <el-button v-if="$hasPermission('product:brand:update')" type="text" size="small" @click="addOrUpdateHandle(scope.row.brandId)">{{ $t('update') }}</el-button>
            <el-button v-if="$hasPermission('product:brand:delete')" type="text" size="small" @click="deleteHandle(scope.row.brandId)">{{ $t('delete') }}</el-button>
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
import AddOrUpdate from './brand-add-or-update'
export default {
  mixins: [mixinViewModule],
  data () {
    return {
      mixinViewModuleOptions: {
        getDataListURL: '/product/brand/page',
        getDataListIsPage: true,
        exportURL: '/product/brand/export',
        deleteURL: '/product/brand',
        deleteIsBatch: true
      },
      dataForm: {
        brandId: ''
      }
    }
  },
  components: {
    AddOrUpdate
  }
}
</script>
